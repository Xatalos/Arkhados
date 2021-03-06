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

import arkhados.CharacterInteraction;
import arkhados.Globals;
import arkhados.ServerFog;
import arkhados.World;
import arkhados.actions.EntityAction;
import arkhados.actions.ASplash;
import arkhados.actions.ATrance;
import arkhados.characters.RockGolem;
import arkhados.controls.CActionQueue;
import arkhados.controls.CCharacterPhysics;
import arkhados.controls.CEntityVariable;
import arkhados.controls.CInfluenceInterface;
import arkhados.controls.CSpellCast;
import arkhados.messages.CmdWorldEffect;
import arkhados.spell.Spell;
import arkhados.util.DistanceScaling;
import arkhados.util.PathCheck;
import arkhados.util.UserData;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.MotionPathListener;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.math.Spline;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class Toss extends Spell {

    public static final float SPLASH_RADIUS = 20f;
    public static final float PICK_RANGE = 20f;

    {
        iconName = "Toss.png";
    }

    public Toss(String name, float cooldown, float range, float castTime) {
        super(name, cooldown, range, castTime);
    }

    public static Toss create() {
        float cooldown = 8f;
        float range = 80f;
        float castTime = 0.3f;

        final Toss toss = new Toss("Toss", cooldown, range, castTime);

        toss.castSpellActionBuilder = (Node caster, Vector3f vec)
                -> new ACastToss(toss);

        toss.nodeBuilder = null;
        return toss;

    }
}

class ACastToss extends EntityAction {

    private final Spell spell;

    public ACastToss(Spell spell) {
        this.spell = spell;
    }

    @Override
    public boolean update(float tpf) {
        CActionQueue actionQueue = spatial.getControl(CActionQueue.class);

        AToss tossAction = new AToss(spell);
        actionQueue.enqueueAction(tossAction);
        return false;
    }
}

class AToss extends EntityAction {

    private final Spell spell;
    private static final float forwardSpeed = 105f;

    public AToss(Spell spell) {
        this.spell = spell;
    }

    @Override
    public boolean update(float tpf) {
        Spatial closest = TossSelect.select(spatial);

        if (closest != null) {
            CActionQueue cQueue = closest.getControl(CActionQueue.class);
            if (cQueue != null) {
                EntityAction current = cQueue.getCurrent();
                if (current != null && current instanceof ATrance) {
                    ((ATrance) current).activate(spatial);
                    return false;
                }
            }
            toss(closest);
        }

        return false;
    }

    private void toss(final Spatial target) {
        Vector3f startLocation = spatial.getLocalTranslation().clone().setY(1);
        Spatial walls = Globals.app.getStateManager().getState(World.class)
                .getWorldRoot().getChild("Walls");
        Vector3f finalLocation = PathCheck.closestNonColliding(walls,
                startLocation, spatial.getControl(CSpellCast.class)
                .getClosestPointToTarget(spell), 0);

        final MotionPath path = new MotionPath();
        path.addWayPoint(startLocation);
        path.addWayPoint(spatial.getLocalTranslation().add(finalLocation)
                .divideLocal(2)
                .setY(finalLocation.distance(startLocation) / 2f));
        path.addWayPoint(finalLocation);

        path.setPathSplineType(Spline.SplineType.CatmullRom);
        path.setCurveTension(0.75f);

        MotionEvent motionControl = new MotionEvent(target, path);
        motionControl.setInitialDuration(finalLocation
                .distance(startLocation) / forwardSpeed);
        motionControl.setSpeed(1.6f);

        final CSpiritStonePhysics stonePhysics
                = target.getControl(CSpiritStonePhysics.class);

        MotionPathListener motionPathListener = new MotionPathListener() {
            @Override
            public void onWayPointReach(MotionEvent motionControl,
                    int wayPointIndex) {
                if (wayPointIndex == path.getNbWayPoints() - 1) {
                    if (stonePhysics == null) {
                        target.getControl(CCharacterPhysics.class)
                                .switchToNormalPhysicsMode();
                    }
                    landingEffect();
                    motionControl.setEnabled(false);
                }
            }

            private void landingEffect() {
                int myTeam = spatial.getUserData(UserData.TEAM_ID);

                ASplash splashAction
                        = new ASplash(Toss.SPLASH_RADIUS, 350, 0,
                                DistanceScaling.CONSTANT, null);
                splashAction.setSpatial(target);
                splashAction.excludeSpatial(spatial);
                splashAction.excludeSpatial(target);
                splashAction.setExcludedTeam(myTeam);
                splashAction.setCasterInterface(spatial
                        .getControl(CInfluenceInterface.class));
                splashAction.update(0f);

                int targetTeam = target.getUserData(UserData.TEAM_ID);

                if (stonePhysics == null && myTeam != targetTeam) {
                    CInfluenceInterface targetInterface
                            = target.getControl(CInfluenceInterface.class);
                    CInfluenceInterface myInterface
                            = spatial.getControl(CInfluenceInterface.class);

                    CharacterInteraction.harm(myInterface, targetInterface,
                            200f, null, true);
                } else {
                    target.getLocalTranslation().setY(10f);
                }

                ServerFog fog = spatial.getControl(CEntityVariable.class)
                        .getAwareness().getFog();
                fog.addCommand(target,
                        new CmdWorldEffect(RockGolem.WORLDEFFECT_TOSS_HIT,
                                target.getLocalTranslation()));
            }
        };

        path.addListener(motionPathListener);

        motionControl.play();

        if (stonePhysics == null) {
            target.getControl(CCharacterPhysics.class)
                    .switchToMotionCollisionMode();
            target.getControl(CActionQueue.class).clear();
        }
    }
}
