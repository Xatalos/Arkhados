/*    This file is part of Arkhados.

 Arkhados is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 Arkhados is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Arkhados.  If not, see <http://www.gnu.org/licenses/>. */
package arkhados.spell.spells.venator;

import arkhados.CharacterInteraction;
import arkhados.Globals;
import arkhados.SpatialDistancePair;
import arkhados.World;
import arkhados.actions.ATrance;
import arkhados.actions.EntityAction;
import arkhados.characters.Venator;
import arkhados.controls.CActionQueue;
import arkhados.controls.CCharacterPhysics;
import arkhados.controls.CInfluenceInterface;
import arkhados.controls.CSpellCast;
import arkhados.spell.Spell;
import arkhados.spell.buffs.AbstractBuffBuilder;
import arkhados.spell.buffs.IncapacitateCC;
import arkhados.util.PathCheck;
import arkhados.util.Selector;
import arkhados.util.UserData;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.math.Spline;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;

public class Leap extends Spell {

    {
        iconName = "leap.png";
    }

    public Leap(String name, float cooldown, float range, float castTime) {
        super(name, cooldown, range, castTime);
    }

    public static Spell create() {
        final float cooldown = 10f;
        final float range = 90f;
        final float castTime = 0.2f;

        final Leap spell = new Leap("Leap", cooldown, range, castTime);

        spell.castSpellActionBuilder = (Node caster, Vector3f vec)
                -> new ACastLeap(spell);
        spell.nodeBuilder = null;

        return spell;
    }
}

class ACastLeap extends EntityAction {

    private static final float forwardSpeed = 130f;
    private final Spell spell;
    private boolean motionSet = false;
    private Vector3f direction;
    private boolean motionPending = true;

    public ACastLeap(final Spell spell) {
        this.spell = spell;
    }

    private void motionPathVersion() {
        final CCharacterPhysics physics
                = spatial.getControl(CCharacterPhysics.class);
        physics.switchToMotionCollisionMode();
        Spatial walls = Globals.app.getStateManager().getState(World.class)
                .getWorldRoot().getChild("Walls");

        // We set y to 1 to prevent ground collision on start
        Vector3f startLocation = spatial.getLocalTranslation().add(0, 1f, 0);
        Vector3f finalLocation = PathCheck.closestNonColliding(walls,
                startLocation, spatial.getControl(CSpellCast.class)
                .getClosestPointToTarget(spell),
                physics.getCapsuleShape().getRadius());

        final MotionPath path = new MotionPath();
        path.addWayPoint(startLocation);
        path.addWayPoint(spatial.getLocalTranslation().add(finalLocation)
                .divideLocal(2).setY(
                        finalLocation.distance(startLocation) / 2f));
        path.addWayPoint(finalLocation);

        path.setPathSplineType(Spline.SplineType.CatmullRom);
        path.setCurveTension(0.75f);

        MotionEvent motionControl = new MotionEvent(spatial, path);
        motionControl.setInitialDuration(
                finalLocation.distance(startLocation) / forwardSpeed);
        motionControl.setSpeed(1f);

        direction = finalLocation.subtract(startLocation);
        physics.setViewDirection(direction);

        path.addListener((MotionEvent cMotion, int index) -> {
            if (index == path.getNbWayPoints() - 2) {
            } else if (index == path.getNbWayPoints() - 1) {
                physics.switchToNormalPhysicsMode();
                spatial.getControl(CActionQueue.class).enqueueAction(
                        new AChangeAnimation(Venator.ANIM_LAND));
                landingEffect();
                motionPending = false;
            }
        });

        motionControl.play();
    }

    private void landingEffect() {
        int myTeamId = spatial.getUserData(UserData.TEAM_ID);
        List<SpatialDistancePair> spatialsOnDistance
                = Selector.getSpatialsWithinDistance(
                        new ArrayList<SpatialDistancePair>(), spatial, 17.5f,
                        new Selector.IsCharacterOfOtherTeam(myTeamId));

        SpatialDistancePair pairSmallestDistance = null;
        for (SpatialDistancePair pair : spatialsOnDistance) {
            // Check if spatial is character

            if (pairSmallestDistance == null
                    || pair.distance < pairSmallestDistance.distance) {
                pairSmallestDistance = pair;
            }
        }

        if (pairSmallestDistance != null) {
            EntityAction currentAction = pairSmallestDistance.spatial
                    .getControl(CActionQueue.class).getCurrent();

            if (currentAction != null
                    && currentAction instanceof ATrance) {
                ((ATrance) currentAction).activate(spatial);
                return;
            }

            float damageFactor = spatial.getUserData(UserData.DAMAGE_FACTOR);
            float damage = 200f * damageFactor;

            List<AbstractBuffBuilder> buffs = new ArrayList<>(1);
            buffs.add(0, new IncapacitateCC.MyBuilder(1f));

            CInfluenceInterface myInterface
                    = spatial.getControl(CInfluenceInterface.class);
            CInfluenceInterface targetInterface
                    = pairSmallestDistance.spatial
                    .getControl(CInfluenceInterface.class);

            CharacterInteraction.harm(myInterface, targetInterface,
                    damage, buffs, true);
        }
    }

    @Override
    public boolean update(float tpf) {
        if (!motionSet) {
            motionPathVersion();
        }

        motionSet = true;
        return motionPending;
    }
}

/**
 * EntityAction that does nothing but chages animation by having name
 */
class AChangeAnimation extends EntityAction {

    public AChangeAnimation(int id) {
        setTypeId(id);
    }

    @Override
    public boolean update(float tpf) {
        return false;
    }
}
