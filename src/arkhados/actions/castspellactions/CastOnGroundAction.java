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
package arkhados.actions.castspellactions;

import arkhados.WorldManager;
import arkhados.actions.EntityAction;
import arkhados.controls.AreaEffectControl;
import arkhados.controls.CharacterPhysicsControl;
import arkhados.controls.InfluenceInterfaceControl;
import arkhados.controls.SpellBuffControl;
import arkhados.spell.Spell;
import arkhados.spell.buffs.AbstractBuff;
import arkhados.util.UserDataStrings;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author william
 */
public class CastOnGroundAction extends EntityAction {

    private WorldManager worldManager;
    private Spell spell;
    private final List<AbstractBuff> additionalEnterBuffs = new ArrayList<AbstractBuff>();
    // NOTE: Add additionalExitBuffs -list if needed

    public CastOnGroundAction(WorldManager worldManager, Spell spell) {
        this.worldManager = worldManager;
        this.spell = spell;
    }

    @Override
    public boolean update(float tpf) {
        final Vector3f targetLocation = super.spatial.getControl(CharacterPhysicsControl.class).getTargetLocation();
        final Vector3f adjustedTarget = this.getClosestPointToTarget().setY(0.1f);
        final Long playerId = super.spatial.getUserData(UserDataStrings.PLAYER_ID);
        final Long entityId = worldManager.addNewEntity(spell.getName(), adjustedTarget, Quaternion.IDENTITY, playerId);

        final Spatial entity = worldManager.getEntity(entityId);
        final AreaEffectControl aoeControl = entity.getControl(AreaEffectControl.class);
        aoeControl.setOwnerInterface(super.spatial.getControl(InfluenceInterfaceControl.class));
        for (AbstractBuff buff : this.additionalEnterBuffs) {
            aoeControl.addEnterBuff(buff);
        }
        return false;
    }

    public void addEnterBuff(final AbstractBuff buff) {
        this.additionalEnterBuffs.add(buff);
    }

    public Vector3f getClosestPointToTarget() {
        final Vector3f targetLocation = super.spatial.getControl(CharacterPhysicsControl.class).getTargetLocation();
        final Vector3f displacement = targetLocation.subtract(super.spatial.getLocalTranslation());

        if (displacement.lengthSquared() <= FastMath.sqr(this.spell.getRange())) {
            return targetLocation;
        }
        displacement.normalizeLocal().multLocal(this.spell.getRange());
        return displacement.addLocal(super.spatial.getLocalTranslation());
    }
}
