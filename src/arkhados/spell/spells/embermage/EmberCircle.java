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
package arkhados.spell.spells.embermage;

import arkhados.CollisionGroups;
import arkhados.actions.ADelay;
import arkhados.actions.EntityAction;
import arkhados.actions.cast.ACastOnGround;
import arkhados.controls.CActionQueue;
import arkhados.controls.CAreaEffect;
import arkhados.controls.CTimedExistence;
import arkhados.controls.CVisibility;
import arkhados.effects.EmitterCircleShape;
import arkhados.spell.Spell;
import arkhados.spell.buffs.AbstractBuffBuilder;
import arkhados.spell.influences.DamageOverTimeInfluence;
import arkhados.spell.influences.SlowInfluence;
import arkhados.util.AbstractNodeBuilder;
import arkhados.util.BuildParameters;
import arkhados.util.UserData;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.collision.shapes.CylinderCollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;

/**
 * Embermage's Ember Circle (Q) spell. Area of Effect spell that deals damage
 * over time in certain area. Has small activation delay.
 */
public class EmberCircle extends Spell {

    {
        iconName = "ember_circle.png";
    }

    public EmberCircle(String name, float cooldown, float range,
            float castTime) {
        super(name, cooldown, range, castTime);
    }

    public static EmberCircle create() {
        final float cooldown = 6f;
        final float range = 100f;
        final float castTime = 0.4f;

        final EmberCircle spell = new EmberCircle("Ember Circle", cooldown,
                range, castTime);

        spell.castSpellActionBuilder = (Node caster, Vector3f vec) -> {
            ACastOnGround castOnGround = new ACastOnGround(world, spell);
            AbstractBuffBuilder ignite
                    = Ignite.ifNotCooldownCreateDamageOverTimeBuff(caster);
            if (ignite != null) {
                castOnGround.addEnterBuff(ignite);
            }
            return castOnGround;
        };

        spell.nodeBuilder = new EmberCircleBuilder();

        return spell;
    }
}

class EmberCircleBuilder extends AbstractNodeBuilder {

    private static final class AFinish extends EntityAction {
        private final CAreaEffect cAreaEffect;
        private final Node node;

        public AFinish(CAreaEffect cAreaEffect, Node node) {
            this.cAreaEffect = cAreaEffect;
            this.node = node;
        }

        @Override
        public boolean update(float tpf) {
            float dps = spatial
                    .getUserData(UserData.DAMAGE_PER_SECOND);
            cAreaEffect.addInfluence(new DamageOverTimeInfluence(dps));
            SlowInfluence slowInfluence = new SlowInfluence();
            slowInfluence.setSlowFactor(0.67f);
            cAreaEffect.addInfluence(slowInfluence);

            node.addControl(new CTimedExistence(5f, true));

            return false;
        }
    }

    private ParticleEmitter createFire(float radius) {
        ParticleEmitter fire = new ParticleEmitter("fire-emitter",
                ParticleMesh.Type.Triangle, 10 * (int) radius);
        Material material
                = new Material(assets, "Common/MatDefs/Misc/Particle.j3md");
        material.setTexture("Texture",
                assets.loadTexture("Effects/flame.png"));
        fire.setMaterial(material);
        fire.setImagesX(2);
        fire.setImagesY(2);
        fire.setSelectRandomImage(true);
        fire.setStartColor(new ColorRGBA(0.95f, 0.15f, 0f, 1f));
        fire.setEndColor(new ColorRGBA(1f, 1f, 0f, 0.5f));
        fire.getParticleInfluencer()
                .setInitialVelocity(Vector3f.UNIT_Y.mult(2f));
        fire.setStartSize(6.5f);
        fire.setEndSize(1.5f);
        fire.setGravity(Vector3f.ZERO);
        fire.setLowLife(1f);
        fire.setHighLife(2f);
        fire.setParticlesPerSec((int) (0.4 * radius * radius));
        fire.getParticleInfluencer().setVelocityVariation(0.2f);
        fire.setRandomAngle(true);

        EmitterCircleShape emitterShape
                = new EmitterCircleShape(Vector3f.ZERO, radius);
        fire.setShape(emitterShape);

        return fire;
    }

    private ParticleEmitter createSmoke(float radius) {
        ParticleEmitter smoke = new ParticleEmitter("smoke-puff",
                ParticleMesh.Type.Triangle, (int) radius * 10);
        Material material = new Material(assets,
                "Common/MatDefs/Misc/Particle.j3md");
        material.setTexture("Texture",
                assets.loadTexture("Effects/flame_alpha.png"));
        material.getAdditionalRenderState()
                .setBlendMode(RenderState.BlendMode.Alpha);
        smoke.setMaterial(material);
        smoke.setImagesX(2);
        smoke.setImagesY(2);
        smoke.setSelectRandomImage(true);
        smoke.setStartColor(new ColorRGBA(0.3f, 0.3f, 0.3f, 0.091f));
        smoke.setStartColor(new ColorRGBA(0.3f, 0.3f, 0.3f, 0.091f));
        smoke.getParticleInfluencer()
                .setInitialVelocity(Vector3f.UNIT_Y.mult(15f));
        smoke.getParticleInfluencer().setVelocityVariation(0.1f);
        smoke.setStartSize(8f);
        smoke.setEndSize(16f);
        smoke.setGravity(Vector3f.ZERO);
        smoke.setLowLife(4f);
        smoke.setHighLife(6f);
        smoke.setParticlesPerSec((int) (0.5 * radius * radius));
        smoke.setRandomAngle(true);
        smoke.setShape(new EmitterCircleShape(Vector3f.ZERO, radius));

        return smoke;
    }

    @Override
    public Node build(final BuildParameters params) {
        final Node node = (Node) assets.loadModel("Models/Circle.j3o");
        node.setLocalTranslation(params.location);
        final float radius = 15f;
        node.scale(radius, 1f, radius);

        Material material
                = assets.loadMaterial("Materials/EmberCircleGround.j3m");
        material.getAdditionalRenderState()
                .setBlendMode(RenderState.BlendMode.Alpha);
        node.setQueueBucket(RenderQueue.Bucket.Transparent);
        material.setTexture("AlphaMap",
                assets.loadTexture("Textures/EmberCircleAlphaMap.png"));
        node.setMaterial(material);

        node.setUserData(UserData.DAMAGE_PER_SECOND, 100f);
        CActionQueue actionQueue = new CActionQueue();
        node.addControl(actionQueue);

        float delay = Math.max(0.8f - params.age, 0f);
        actionQueue.enqueueAction(new ADelay(delay));

        if (world.isServer()) {
            node.addControl(new CEmberCircleVisibility(radius));

            GhostControl ghost = new GhostControl(new CylinderCollisionShape(
                    new Vector3f(radius, 0.05f, radius), 1));
            ghost.setCollideWithGroups(CollisionGroups.CHARACTERS);
            node.addControl(ghost);

            CAreaEffect cAreaEffect = new CAreaEffect(ghost);
            node.addControl(cAreaEffect);

            actionQueue.enqueueAction(new AFinish(cAreaEffect, node));
        } else if (world.isClient()) {
            actionQueue.enqueueAction(new EntityAction() {
                @Override
                public boolean update(float tpf) {
                    Vector3f worldTranslation = spatial.getWorldTranslation();

                    final ParticleEmitter fire = createFire(radius);
                    world.getWorldRoot().attachChild(fire);
                    fire.setLocalTranslation(worldTranslation);
                    fire.move(0f, 1f, 0f);
                    fire.addControl(new CTimedExistence(10f));

                    // Smoke causes performance problems so it's disabled now
                    // TODO: Find a way to make smoke useable performancewise
//                    final ParticleEmitter smoke = createSmoke(radius);
//                    world.getWorldRoot().attachChild(smoke);
//                    smoke.setLocalTranslation(worldTranslation);
//                    smoke.move(0f, 1f, 0f);
//                    smoke.addControl(new CTimedExistence(10f));
                    float removalDelay = Math.max(0, 5f - params.age);

                    CActionQueue fireActions = new CActionQueue();
                    fire.addControl(fireActions);
                    fireActions.enqueueAction(new ADelay(removalDelay));
                    fireActions.enqueueAction(new EntityAction() {
                        @Override
                        public boolean update(float tpf) {
                            fire.setParticlesPerSec(0);
                            return false;
                        }
                    });

//                    CActionQueue smokeActions = new CActionQueue();
//                    smoke.addControl(smokeActions);
//
//                    smokeActions.enqueueAction(new ADelay(removalDelay));
//                    smokeActions.enqueueAction(new EntityAction() {
//                        @Override
//                        public boolean update(float tpf) {
//                            smoke.setParticlesPerSec(0);
//                            return false;
//                        }
//                    });
                    AudioNode sound = new AudioNode(assets,
                            "Effects/Sound/EmberCircle.wav");
                    ((Node) spatial).attachChild(sound);
                    sound.setPositional(true);
                    sound.setReverbEnabled(false);
                    sound.setVolume(1f);
                    sound.play();

                    return false;
                }
            });
        }

        return node;
    }
}

class CEmberCircleVisibility extends AbstractControl implements CVisibility {

    private final float radius;
    private final float radiusSquared;

    public CEmberCircleVisibility(float radius) {
        this.radius = radius;
        this.radiusSquared = radius * radius;
    }

    @Override
    protected void controlUpdate(float tpf) {
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    @Override
    public Vector3f giveClosestPoint(Vector3f lookerLocation) {
        if (lookerLocation.distanceSquared(spatial.getLocalTranslation())
                <= radiusSquared) {
            return lookerLocation;
        }

        Vector3f vec = new Vector3f(lookerLocation);
        vec.subtractLocal(spatial.getLocalTranslation());
        vec.normalizeLocal().multLocal(radius);
        return vec.addLocal(spatial.getLocalTranslation());
    }
}
