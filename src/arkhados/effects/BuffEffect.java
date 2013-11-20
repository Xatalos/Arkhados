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
package arkhados.effects;

import arkhados.spell.buffs.buffinformation.BuffInformation;
import com.jme3.asset.AssetManager;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.HashMap;

/**
 *
 * @author william
 */
public abstract class BuffEffect {

    protected static AssetManager assetManager = null;

    public static void setAssetManager(AssetManager aAssetManager) {
        assetManager = aAssetManager;
    }

    // TODO: Get this from some central place
    private float timeLeft;

    protected BuffEffect(float timeLeft) {
        this.timeLeft = timeLeft;
    }

    public void update(final float tpf) {
        this.timeLeft -= tpf;
    }

    public void updateRender(RenderManager rm, ViewPort vp) {
    }

    public void destroy() {
    }

    public float getTimeLeft() {
        return this.timeLeft;
    }
}