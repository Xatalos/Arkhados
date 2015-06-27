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
package arkhados.spell.spells.venator;

import arkhados.actions.EntityAction;
import arkhados.actions.castspellactions.ACastSelfBuff;
import arkhados.controls.CInfluenceInterface;
import arkhados.spell.CastSpellActionBuilder;
import arkhados.spell.Spell;
import arkhados.spell.buffs.AbstractBuff;
import arkhados.spell.buffs.SpeedBuff;
import arkhados.util.BuffTypeIds;
import arkhados.util.UserDataStrings;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author william
 */
public class SurvivalInstinct extends Spell {
    static final float COOLDOWN = 12f;
    static final float RANGE = 0f;
    static final float CAST_TIME = 0f;
    static final float DURATION = 6f;

    {
        iconName = "survival_instinct.png";
    }

    public SurvivalInstinct(String name, float cooldown, float range,
            float castTime) {
        super(name, cooldown, range, castTime);
    }

    public static SurvivalInstinct create() {
        final SurvivalInstinct spell = new SurvivalInstinct("Survival Instinct",
                COOLDOWN, RANGE, CAST_TIME);

        spell.castSpellActionBuilder = new CastSpellActionBuilder() {
            @Override
            public EntityAction newAction(Node caster, Vector3f vec) {
                ACastSelfBuff buffAction = new ACastSelfBuff();
                buffAction.addBuff(
                        new DamagePerHealthPercentBuff(DURATION));
                buffAction.addBuff(
                        new MovementSpeedPerHealthMissingBuff(DURATION));
                return buffAction;
            }
        };

        spell.nodeBuilder = null;
        return spell;
    }
}

class DamagePerHealthPercentBuff extends AbstractBuff {

    private Spatial spatial = null;

    {
        friendly = true;
    }

    public DamagePerHealthPercentBuff(float duration) {
        super(duration);
    }

    @Override
    public void attachToCharacter(
            CInfluenceInterface influenceInterface) {
        super.attachToCharacter(influenceInterface);
        spatial = influenceInterface.getSpatial();
    }

    @Override
    public void update(float time) {
        super.update(time);
        float healthCurrent =
                spatial.getUserData(UserDataStrings.HEALTH_CURRENT);
        float healthMax = spatial.getUserData(UserDataStrings.HEALTH_MAX);
        float damageFactor = spatial.getUserData(UserDataStrings.DAMAGE_FACTOR);

        float healthPercent = healthCurrent / healthMax;
        damageFactor *= 1 + healthPercent / 10f;

        spatial.setUserData(UserDataStrings.DAMAGE_FACTOR, damageFactor);
    }
}

class MovementSpeedPerHealthMissingBuff extends SpeedBuff {

    private Spatial spatial;
    private float originalHealth;

    {
        name = "Survival Instinct";
        setTypeId(BuffTypeIds.SURVIVAL_INSTINCT);
        friendly = true;
    }

    public MovementSpeedPerHealthMissingBuff(float duration) {
        super(0, 5f, duration);
    }

    @Override
    public void attachToCharacter(
            CInfluenceInterface influenceInterface) {
        super.attachToCharacter(influenceInterface);
        spatial = influenceInterface.getSpatial();
        originalHealth = spatial.getUserData(UserDataStrings.HEALTH_CURRENT);
    }

    @Override
    public float getFactor() {
        float healthMax = spatial.getUserData(UserDataStrings.HEALTH_MAX);
        float inverseHealthPercent = 1f - (originalHealth / healthMax);
        return 1 + inverseHealthPercent / 8f;        
    }        
}