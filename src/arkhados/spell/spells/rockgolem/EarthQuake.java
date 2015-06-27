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
package arkhados.spell.spells.rockgolem;

import arkhados.SpatialDistancePair;
import arkhados.actions.ACharge;
import arkhados.actions.EntityAction;
import arkhados.actions.ASplash;
import arkhados.characters.RockGolem;
import arkhados.controls.CActionQueue;
import arkhados.controls.CCharacterPhysics;
import arkhados.controls.CInfluenceInterface;
import arkhados.spell.CastSpellActionBuilder;
import arkhados.spell.Spell;
import arkhados.spell.buffs.AbstractBuff;
import arkhados.spell.buffs.IncapacitateCC;
import arkhados.util.DistanceScaling;
import arkhados.util.Selector;
import arkhados.util.UserDataStrings;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.MotionPathListener;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.ArrayList;

/**
 *
 * @author william
 */
public class EarthQuake extends Spell {
    public static final float RADIUS = 22f;

    {
        iconName = "EarthQuake.png";
    }

    public EarthQuake(String name, float cooldown, float range,
            float castTime) {
        super(name, cooldown, range, castTime);
    }

    public static Spell create() {
        final float cooldown = 7f;
        final float range = 90f;
        final float castTime = 0.3f;

        EarthQuake quake =
                new EarthQuake("EarthQuake", cooldown, range, castTime);
        quake.nodeBuilder = null;

        quake.castSpellActionBuilder = new CastSpellActionBuilder() {
            @Override
            public EntityAction newAction(Node caster, Vector3f vec) {
                return new ACastEarthQuake();
            }
        };

        return quake;
    }
}

class ACastEarthQuake extends EntityAction {

    private final float chargeRange = 90f;

    public ACastEarthQuake() {
    }

    @Override
    public boolean update(float tpf) {
        ACharge charge = new ACharge(chargeRange);
        charge.setChargeSpeed(150f);
        charge.setHitDamage(30f);
        
        // TODO: ACharge takes ATrance into action, but we need to do something
        // here too.

        IncapacitateCC incapacitate = new IncapacitateCC(1.2f);
        ArrayList<AbstractBuff> buffs = new ArrayList<>();
        buffs.add(incapacitate);

        final float splashRadius = EarthQuake.RADIUS;

        ASplash splash = new ASplash(splashRadius, 180f, 0f,
                DistanceScaling.CONSTANT, buffs);
        final int teamId = spatial.getUserData(UserDataStrings.TEAM_ID);
        splash.setExcludedTeam(teamId);
        CInfluenceInterface casterInterface =
                spatial.getControl(CInfluenceInterface.class);
        splash.setCasterInterface(casterInterface);

        CActionQueue queue = spatial.getControl(CActionQueue.class);
        queue.enqueueAction(charge);
        queue.enqueueAction(splash);
        splash.setTypeId(RockGolem.ACTION_EARTHQUAKE);

        queue.enqueueAction(new EntityAction() {
            @Override
            public boolean update(float tpf) {
                ArrayList<SpatialDistancePair> spatialsWithinDistance = Selector
                        .getSpatialsWithinDistance(
                        new ArrayList<SpatialDistancePair>(),
                        spatial.getLocalTranslation(), splashRadius);

                for (SpatialDistancePair pair : spatialsWithinDistance) {
                    if (pair.spatial.getControl(CInfluenceInterface.class)
                            == null) {
                        continue;
                    } else if (pair.spatial.getUserData(UserDataStrings.TEAM_ID)
                            .equals(teamId)) {
                        continue;
                    }

                    pair.spatial.getControl(CActionQueue.class)
                            .enqueueAction(new AKnockup());
                }

                return false;
            }
        });

        return false;
    }
}

/**
 * @author william
 */
class AKnockup extends EntityAction {

    private boolean firstTime = true;
    private boolean shouldContinue = true;

    @Override
    public boolean update(float tpf) {
        if (firstTime) {
            motionPath();
        }

        return shouldContinue;
    }

    private void motionPath() {
        firstTime = false;
        final MotionPath path = new MotionPath();

        final Vector3f location = spatial.getLocalTranslation();
        path.addWayPoint(location);
        path.addWayPoint(location.add(0, 40f, 0));
        path.addWayPoint(location);

        path.setCurveTension(0.75f);

        MotionEvent motionControl = new MotionEvent(spatial, path);
        motionControl.setInitialDuration(0.75f);
        motionControl.setSpeed(1f);

        MotionPathListener pathListener = new MotionPathListener() {
            @Override
            public void onWayPointReach(MotionEvent motionControl,
                    int wayPointIndex) {
                if (wayPointIndex == path.getNbWayPoints() - 1) {
                    spatial.getControl(CCharacterPhysics.class)
                            .switchToNormalPhysicsMode();
                    shouldContinue = false;
                }
            }
        };

        path.addListener(pathListener);
        motionControl.play();

        spatial.getControl(CCharacterPhysics.class)
                .switchToMotionCollisionMode();
    }
}
