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
package arkhados.controls;

import arkhados.messages.sync.statedata.GenericSyncData;
import arkhados.messages.sync.statedata.StateData;
import arkhados.util.UserData;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

public class CGenericSync extends AbstractControl implements CSync{

    @Override
    protected void controlUpdate(float tpf) {        
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {        
    }

    @Override
    public StateData getSyncableData(StateData stateData) {
        return new GenericSyncData((int) 
                getSpatial().getUserData(UserData.ENTITY_ID), spatial);
    }    
}
