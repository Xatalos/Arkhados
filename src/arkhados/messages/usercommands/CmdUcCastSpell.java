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
package arkhados.messages.usercommands;

import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import com.jme3.scene.Spatial;
import arkhados.controls.CSpellCast;
import arkhados.messages.sync.statedata.StateData;

@Serializable
public class CmdUcCastSpell extends StateData {

    private byte input;
    private Vector3f direction;

    public CmdUcCastSpell() {
    }

    public CmdUcCastSpell(int input, boolean modifier, Vector3f location) {
        this.input = (byte) (modifier ? -input : input);
        this.direction = location;
    }

    @Override
    public void applyData(Object target) {
        Spatial character = (Spatial) target;
        character.getControl(CSpellCast.class)
                .castIfDifferentSpell(input, direction);
    }

    public int getInput() {
        return input;
    }

    public Vector3f getLocation() {
        return direction;
    }    
}
