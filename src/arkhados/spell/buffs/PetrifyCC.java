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
package arkhados.spell.buffs;

import arkhados.util.BuffTypeIds;

public class PetrifyCC extends CrowdControlBuff {
        
    private static final float damageCap = 100;
    private float totalDamageTaken = 0f;
    private static final float damageReduction = 0.85f;

    private PetrifyCC(float duration) {
        super(duration);
    }

    public float damage(float damage) {
        float damageTaken = damage * (1 - damageReduction);
        totalDamageTaken += damageTaken;
        return damageTaken;
    }

    @Override
    public boolean shouldContinue() {
        return super.shouldContinue() && totalDamageTaken <= damageCap;
    }

    @Override
    public boolean preventsCasting() {
        return true;
    }

    @Override
    public boolean preventsMoving() {
        return true;
    }

    public static class MyBuilder extends AbstractBuffBuilder {
        public MyBuilder(float duration) {
            super(duration);
            setTypeId(BuffTypeIds.PETRIFY);
        }

        @Override
        public AbstractBuff build() {
            return set(new PetrifyCC(duration));
        }
    }
}