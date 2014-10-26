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
package arkhados.characters;

import arkhados.controls.ActionQueueControl;
import arkhados.controls.CharacterAnimationControl;
import arkhados.controls.CharacterBuffControl;
import arkhados.controls.CharacterHudControl;
import arkhados.controls.CharacterPhysicsControl;
import arkhados.controls.EliteSoldierAmmunitionControl;
import arkhados.controls.InfluenceInterfaceControl;
import arkhados.controls.SpellCastControl;
import arkhados.controls.SyncControl;
import arkhados.controls.SyncInterpolationControl;
import arkhados.effects.EffectBox;
import arkhados.effects.RocketExplosionEffect;
import arkhados.effects.SimpleSoundEffect;
import arkhados.messages.syncmessages.statedata.StateData;
import arkhados.spell.Spell;
import arkhados.ui.hud.ClientHudManager;
import arkhados.util.AnimationData;
import arkhados.util.InputMappingStrings;
import arkhados.util.AbstractNodeBuilder;
import arkhados.util.UserDataStrings;
import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;


/**
 * Creates entity with EliteSoldiers's features.
 *
 * @author william
 */
public class EliteSoldier extends AbstractNodeBuilder {
    public static final int ACTION_ROCKET_JUMP = 0;
    public static final int ACTION_SHOTGUN = 1;  
    private ClientHudManager clientHudManager;    
    
    public EliteSoldier(ClientHudManager clientHudManager) {
        this.clientHudManager = clientHudManager;
        setEffectBox(new EffectBox());
        getEffectBox().addActionEffect(ACTION_ROCKET_JUMP, new RocketExplosionEffect());       
        getEffectBox().addActionEffect(ACTION_SHOTGUN,
                new SimpleSoundEffect("Effects/Sound/Shotgun.wav"));
    }
    @Override
    public Node build() {
        Node entity = (Node) assetManager.loadModel("Models/Archer.j3o");
        float movementSpeed = 36f;
        entity.setUserData(UserDataStrings.SPEED_MOVEMENT, movementSpeed);
        entity.setUserData(UserDataStrings.SPEED_MOVEMENT_BASE, movementSpeed);
        entity.setUserData(UserDataStrings.SPEED_ROTATION, 0.0f);
        float radius = 5.0f;
        entity.setUserData(UserDataStrings.RADIUS, radius);
        float health = 1600f;
        entity.setUserData(UserDataStrings.HEALTH_MAX, health);
        entity.setUserData(UserDataStrings.HEALTH_CURRENT, health);
        entity.setUserData(UserDataStrings.DAMAGE_FACTOR, 1f);
        entity.setUserData(UserDataStrings.LIFE_STEAL, 0f);

        for (Spatial childToScale : entity.getChildren()) {
            childToScale.scale(1.2f);
        }
        entity.addControl(new CharacterPhysicsControl(radius, 20.0f, 75.0f));

        /**
         * By setting physics damping to low value, we can effectively apply
         * impulses on it.
         */
        entity.getControl(CharacterPhysicsControl.class).setPhysicsDamping(0.2f);
        entity.addControl(new ActionQueueControl());

        /**
         * To add spells to entity, create SpellCastControl and call its
         * putSpell-method with name of the spell as argument.
         */
        EliteSoldierAmmunitionControl ammunitionControl = new EliteSoldierAmmunitionControl();
        entity.addControl(ammunitionControl);

        SpellCastControl spellCastControl = new SpellCastControl();
        entity.addControl(spellCastControl);
        spellCastControl.addCastValidator(ammunitionControl);
        spellCastControl.addCastListeners(ammunitionControl);

        spellCastControl.putSpell(Spell.getSpell("Shotgun"),
                InputMappingStrings.getId(InputMappingStrings.M1));

//        spellCastControl.putSpell(Spell.getSpell("Machinegun"), InputMappingStrings.getId(InputMappingStrings.M2));
        spellCastControl.putSpell(Spell.getSpell("Railgun"),
                InputMappingStrings.getId(InputMappingStrings.M2));
        spellCastControl.putSpell(Spell.getSpell("Plasmagun"),
                InputMappingStrings.getId(InputMappingStrings.Q));
        spellCastControl.putSpell(Spell.getSpell("Rocket Launcher"),
                InputMappingStrings.getId(InputMappingStrings.E));
        spellCastControl.putSpell(Spell.getSpell("Like a Pro"),
                InputMappingStrings.getId(InputMappingStrings.R));
        spellCastControl.putSpell(Spell.getSpell("Rocket Jump"),
                InputMappingStrings.getId(InputMappingStrings.SPACE));

        /**
         * Map Spell names to casting animation's name. In this case all spells
         * use same animation.
         */
        AnimControl animControl = entity.getControl(AnimControl.class);
        CharacterAnimationControl characterAnimControl = new CharacterAnimationControl(animControl);
        AnimationData deathAnim = new AnimationData("Die", 1f, LoopMode.DontLoop);
        AnimationData walkAnim = new AnimationData("Walk", 1f, LoopMode.DontLoop);

        characterAnimControl.setDeathAnimation(deathAnim);
        characterAnimControl.setWalkAnimation(walkAnim);
        entity.addControl(characterAnimControl);

        AnimationData animationData = new AnimationData("Attack", 1f, LoopMode.DontLoop);

        characterAnimControl.addSpellAnimation("Shotgun", animationData);
//        animControl.addSpellAnimation("Machinegun", animationData);
        characterAnimControl.addSpellAnimation("Railgun", animationData);
        characterAnimControl.addSpellAnimation("Plasmagun", animationData);
        characterAnimControl.addSpellAnimation("Rocket Launcher", animationData);
        characterAnimControl.addSpellAnimation("Like a Pro", null);
        characterAnimControl.addSpellAnimation("Rocket Jump", animationData);

        entity.addControl(new InfluenceInterfaceControl());
        entity.addControl(new EliteSoldierSyncControl());

        if (worldManager.isClient()) {
            entity.addControl(new CharacterBuffControl());
            entity.addControl(new CharacterHudControl());

            clientHudManager.addCharacter(entity);
            entity.addControl(new SyncInterpolationControl());
            entity.getControl(InfluenceInterfaceControl.class).setIsServer(false);
        }

        return entity;
    }
}

class EliteSoldierSyncControl extends AbstractControl implements SyncControl {

    @Override
    public StateData getSyncableData(StateData stateData) {
        return new EliteSoldierSyncData((int) getSpatial().getUserData(UserDataStrings.ENTITY_ID),
                getSpatial());
    }

    @Override
    protected void controlUpdate(float tpf) {
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
}
