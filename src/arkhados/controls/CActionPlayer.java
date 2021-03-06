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

import arkhados.Globals;
import arkhados.effects.EffectHandle;
import arkhados.effects.WorldEffect;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
import com.jme3.util.IntMap;

public class CActionPlayer extends AbstractControl {

    private final IntMap<WorldEffect> actionEffects = new IntMap<>();
    private final IntMap<WorldEffect> castEffects = new IntMap<>();
    private EffectHandle effectHandle;

    public void putEffect(int id, WorldEffect effect) {
        actionEffects.put(id, effect);
    }
    
    public void putCastEffect(int id, WorldEffect effect) {
        castEffects.put(id, effect);
    }

    public void playEffect(int actionId) {
        playEffect(actionId, actionEffects);
    }
    
    public void playCastEffect(int id) {
        playEffect(id, castEffects);
    }
    
    private void playEffect(final int id, final IntMap<WorldEffect> map) {
        Globals.app.enqueue(() -> {
            if (effectHandle != null) {
                effectHandle.end();
                effectHandle = null;
            }
            
            WorldEffect fx = map.get(id);
            if (fx != null) {
                effectHandle = fx.execute((Node) spatial,
                        Vector3f.ZERO, null);
            }
            
            return null;
        });
    }

    public void endEffect() {
        if (effectHandle != null) {
            Globals.app.enqueue(() -> {
                effectHandle.end();
                effectHandle = null;
                return null;
            });
        }
    }

    @Override
    protected void controlUpdate(float tpf) {
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
}
