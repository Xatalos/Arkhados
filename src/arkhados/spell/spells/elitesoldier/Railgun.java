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
package arkhados.spell.spells.elitesoldier;

import arkhados.CollisionGroups;
import arkhados.WorldManager;
import arkhados.actions.EntityAction;
import arkhados.actions.castspellactions.CastProjectileAction;
import arkhados.controls.EntityEventControl;
import arkhados.controls.ProjectileControl;
import arkhados.controls.SpellBuffControl;
import arkhados.controls.TimedExistenceControl;
import arkhados.entityevents.RemovalEventAction;
import arkhados.spell.CastSpellActionBuilder;
import arkhados.spell.Spell;
import arkhados.util.AbstractNodeBuilder;
import arkhados.util.UserDataStrings;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;

/**
 *
 * @author william
 */
public class Railgun extends Spell {

    {
        iconName = "railgun.png";
        setMoveTowardsTarget(false);
    }

    public Railgun(String name, float cooldown, float range, float castTime) {
        super(name, cooldown, range, castTime);
    }

    public static Spell create() {
        final float cooldown = 9f;
        final float range = 130f;
        final float castTime = 0.6f;

        final Railgun spell = new Railgun("Railgun", cooldown, range, castTime);

        spell.castSpellActionBuilder = new CastSpellActionBuilder() {
            @Override
            public EntityAction newAction(Node caster, Vector3f vec) {
                return new CastProjectileAction(spell, worldManager);
            }
        };

        spell.nodeBuilder = new RailgunBuilder();

        return spell;
    }
}

class RailgunBuilder extends AbstractNodeBuilder {

    private ParticleEmitter createTrailEmitter() {
        final ParticleEmitter trail = new ParticleEmitter("trail-emitter",
                ParticleMesh.Type.Triangle, 600);
        Material materialGray = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        materialGray.setTexture("Texture", assetManager.loadTexture("Effects/flame.png"));
        trail.setMaterial(materialGray);
        trail.setImagesX(2);
        trail.setImagesY(2);
        trail.setSelectRandomImage(true);
        trail.setStartColor(new ColorRGBA(0.3f, 0.3f, 0.9f, 1f));
        trail.setStartColor(new ColorRGBA(0.3f, 0.3f, 0.9f, 1f));
        trail.getParticleInfluencer().setInitialVelocity(Vector3f.ZERO);
//        fire.getParticleInfluencer().setInitialVelocity(Vector3f.UNIT_Z.mult(10));
//        fire.getParticleInfluencer().setVelocityVariation(0.5f);
        trail.setStartSize(1.0f);
        trail.setEndSize(1.0f);
        trail.setGravity(Vector3f.ZERO);
        trail.setLowLife(1f);
        trail.setHighLife(1.3f);
        trail.setParticlesPerSec(200);

        trail.setRandomAngle(true);
        return trail;
    }    

    @Override
    public Node build() {
        Sphere sphere = new Sphere(32, 32, 0.5f);

        Geometry projectileGeom = new Geometry("rail-geom", sphere);

        Node node = new Node("rail");
        node.attachChild(projectileGeom);

        // TODO: Give at least bit better material
        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setColor("Color", ColorRGBA.Cyan);
        node.setMaterial(material);

        node.setUserData(UserDataStrings.SPEED_MOVEMENT, 200f);
        node.setUserData(UserDataStrings.MASS, 0.30f);
        node.setUserData(UserDataStrings.DAMAGE, 300f);
        node.setUserData(UserDataStrings.IMPULSE_FACTOR, 0f);

        if (worldManager.isClient()) {
            final ParticleEmitter smoke = createTrailEmitter();
            node.attachChild(smoke);

            node.addControl(new EntityEventControl());
            /**
             * Here we specify what happens on client side when fireball is removed. In this case we
             * want explosion effect.
             */
            final RailgunRemovalAction removalAction = new RailgunRemovalAction(assetManager);
            removalAction.setBullet(node);
            removalAction.setSmokeTrail(smoke);

            node.getControl(EntityEventControl.class).setOnRemoval(removalAction);
        }

        final SphereCollisionShape collisionShape = new SphereCollisionShape(2.5f);
        final RigidBodyControl physicsBody = new RigidBodyControl(collisionShape, (Float) node.getUserData(UserDataStrings.MASS));
        /**
         * We don't want projectiles to collide with each other so we give them their own collision
         * group and prevent them from colliding with that group.
         */
        physicsBody.setCollisionGroup(CollisionGroups.PROJECTILES);
        physicsBody.removeCollideWithGroup(CollisionGroups.PROJECTILES);

        /**
         * Add collision group of characters
         */
        final GhostControl characterCollision = new GhostControl(collisionShape);
        characterCollision.setCollideWithGroups(CollisionGroups.CHARACTERS);
        characterCollision.setCollisionGroup(CollisionGroups.PROJECTILES);
        node.addControl(characterCollision);

        node.addControl(physicsBody);
        ProjectileControl projectileControl = new ProjectileControl();
        projectileControl.setIsProjectile(false);
        node.addControl(projectileControl);
        final SpellBuffControl buffControl = new SpellBuffControl();
        node.addControl(buffControl);

        return node;
    }
}

class RailgunRemovalAction implements RemovalEventAction {

    private Node bullet;
    private ParticleEmitter smokeTrail;
    private AssetManager assetManager;

    public RailgunRemovalAction(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public void setBullet(Node bullet) {
        this.bullet = bullet;
    }

    private void leaveSmokeTrail(final Node worldRoot, Vector3f worldTranslation) {
        smokeTrail.setParticlesPerSec(0);
        worldRoot.attachChild(smokeTrail);
        smokeTrail.setLocalTranslation(worldTranslation);
        smokeTrail.addControl(new TimedExistenceControl(5f));
    }
    
    @Override
    public void exec(WorldManager worldManager, int reason) {
        Vector3f worldTranslation = bullet.getLocalTranslation();
        leaveSmokeTrail(worldManager.getWorldRoot(), worldTranslation);
    }

    public void setSmokeTrail(ParticleEmitter smoke) {
        smokeTrail = smoke;
    }
}