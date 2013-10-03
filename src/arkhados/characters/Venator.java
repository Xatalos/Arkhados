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
import arkhados.controls.CharacterPhysicsControl;
import arkhados.controls.InfluenceInterfaceControl;
import arkhados.controls.SpellCastControl;
import arkhados.util.NodeBuilder;
import arkhados.util.UserDataStrings;
import com.jme3.scene.Node;

/**
 *
 * @author william
 */
public class Venator extends NodeBuilder {

    @Override
    public Node build() {
        Node entity = (Node) NodeBuilder.assetManager.loadModel("Models/Warwolf.j3o");
        entity.setUserData(UserDataStrings.SPEED_MOVEMENT, 37f);
        entity.setUserData(UserDataStrings.SPEED_ROTATION, 0f);
        float radius = 3.0f;
        entity.setUserData(UserDataStrings.RADIUS, radius);
        entity.setUserData(UserDataStrings.HEALTH_CURRENT, 2100f);

        entity.scale(radius);

        entity.addControl(new CharacterPhysicsControl(radius, 22f, 100f));

        entity.getControl(CharacterPhysicsControl.class).setPhysicsDamping(0.2f);
        entity.addControl(new ActionQueueControl());

        SpellCastControl spellCastControl = new SpellCastControl(this.worldManager);
        entity.addControl(spellCastControl);

        CharacterAnimationControl animControl = new CharacterAnimationControl();
        animControl.setDeathAnimation("Die-1");
        animControl.setWalkAnimation("Run");
        entity.addControl(animControl);

        entity.addControl(new InfluenceInterfaceControl());

        return entity;
    }

}
