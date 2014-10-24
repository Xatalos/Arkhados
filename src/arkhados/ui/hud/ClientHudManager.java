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
package arkhados.ui.hud;

import arkhados.PlayerData;
import arkhados.Topic;
import arkhados.UserCommandManager;
import arkhados.controls.ActionQueueControl;
import arkhados.controls.CharacterHudControl;
import arkhados.controls.SpellCastControl;
import arkhados.messages.TopicOnlyCommand;
import arkhados.net.Sender;
import arkhados.spell.Spell;
import arkhados.util.InputMappingStrings;
import arkhados.util.PlayerRoundStats;
import arkhados.util.UserDataStrings;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.font.Rectangle;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author william
 */
// TODO: ClientHudManager is messy and fragile. Clean it up.
public class ClientHudManager extends AbstractAppState implements ScreenController {

    private Nifty nifty;
    private Screen screen;
    private Camera cam;
    private Node guiNode;
    private BitmapFont guiFont;
    private List<Node> characters = new ArrayList<>();
    private List<BitmapText> hpBars = new ArrayList<>();
    private int currentSeconds = -1;
    private Map<String, Element> spellIcons = new HashMap<>(6);
    private Spatial playerCharacter = null;
    private AppStateManager stateManager;
    // HACK: This is only meant for initial implementation testing. Remove this when all round statistics are accessible via GUI
    private boolean roundTableCreated = false;
    private List<Element> statisticsPanels = new ArrayList<>();
    // HACK: 
    private boolean hudCreated = false;
    private HashMap<Integer, Float> cooldowns = null;
    private List<PlayerRoundStats> latestRoundStatsList = null;

    public ClientHudManager(Camera cam, Node guiNode, BitmapFont guiFont) {
        this.cam = cam;
        this.guiNode = guiNode;
        this.guiFont = guiFont;
        guiNode.addControl(new ActionQueueControl());
    }

    public void setNifty(Nifty nifty) {
        this.nifty = nifty;
        screen = nifty.getScreen("default_hud");
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        cam = app.getCamera();
        this.stateManager = stateManager;
    }

    @Override
    public void update(float tpf) {
        if (playerCharacter == null) {
            UserCommandManager userCommandManager = stateManager.getState(UserCommandManager.class);
            playerCharacter = userCommandManager.getCharacter();
            if (playerCharacter != null && !hudCreated) {
                loadSpellIcons();
                hudCreated = true;
                cooldowns = playerCharacter.getControl(SpellCastControl.class).getCooldowns();
            } else if (playerCharacter != null && hudCreated) {
                playerCharacter.getControl(SpellCastControl.class).setCooldowns(cooldowns);
            }
        } else {
            updateSpellIcons();
        }

        for (int i = 0; i < characters.size(); ++i) {
            updateHpBar(i);
        }
    }

    public void addCharacter(Spatial character) {
        // TODO: Add some checks
        characters.add((Node) character);
        createHpBar();
    }

    public void startRound() {
        Element layerCountdown = screen.findElementByName("layer_countdown");
        layerCountdown.disable();
        layerCountdown.hide();

        // TODO: Create statistics panel creation to more appropriate place
        // HACK: This is only meant for initial implementation testing. Remove this "if" when all round statistics are accessible via GUI
        if (!roundTableCreated) {
            roundTableCreated = true;
        }
    }

    public void setSecondsLeftToStart(int seconds) {
        if (currentSeconds == -1) {
            Element layerCountdown = screen.findElementByName("layer_countdown");

            layerCountdown.enable();
            layerCountdown.show();
        }
        if (seconds != currentSeconds) {
            currentSeconds = seconds;
            Element textElement = screen.findElementByName("text_countdown");
            textElement.getRenderer(TextRenderer.class).setText(Integer.toString(seconds));
        }
    }

    public void clear() {
        characters.clear();
        for (BitmapText hpBar : hpBars) {
            hpBar.removeFromParent();
        }

        hpBars.clear();
        clearAllButHpBars();
    }

    public void clearAllButHpBars() {
        currentSeconds = -1;
        playerCharacter = null;

        for (Iterator<Element> it = screen.findElementByName("panel_bottom")
                .getElements().iterator(); it.hasNext();) {

            Element element = it.next();
            nifty.removeElement(screen, element);
        }

        for (Iterator<Element> it = statisticsPanels.iterator(); it.hasNext();) {
            Element element = it.next();
            // TODO: Unregister element ids here
            nifty.removeElement(screen, element);
        }

        statisticsPanels.clear();

        hideRoundStatistics();

        spellIcons.clear();

        hudCreated = false;
    }

    private void createHpBar() {
        BitmapText hpBar = new BitmapText(guiFont);

        hpBar.setSize(guiFont.getCharSet().getRenderedSize());
        hpBar.setBox(new Rectangle(0, 0, 40, 10));
        hpBar.setColor(ColorRGBA.Red);
        hpBar.setAlignment(BitmapFont.Align.Center);
        hpBar.center();
        guiNode.attachChild(hpBar);
        hpBar.setQueueBucket(RenderQueue.Bucket.Gui);
        hpBars.add(hpBar);
    }

    private void updateHpBar(int index) {
        Node character = characters.get(index);
        BitmapText hpBar = hpBars.get(index);
        float health = character.getUserData(UserDataStrings.HEALTH_CURRENT);
        if (health == 0) {
            hpBar.setText("");
            return;
        }
        // TODO: Implement better method to get character's head's location
        Vector3f hpBarLocation = cam.getScreenCoordinates(
                character.getLocalTranslation().add(0, 20, 0)).add(-15, 40, 0);
        hpBar.setLocalTranslation(hpBarLocation);
        hpBar.setText(String.format("%.0f", health));
    }

    public void entityDisappeared(Spatial spatial) {
        if (!(spatial instanceof Node)) {
            return;
        }

        Node node = (Node) spatial;

        int index = characters.indexOf(node);

        if (index != -1) {
            BitmapText hpBar = hpBars.get(index);
            hpBar.removeFromParent();
            hpBars.remove(index);

            characters.remove(index);
        }

        if (spatial == playerCharacter) {
            SpellCastControl castControl = playerCharacter.getControl(SpellCastControl.class);
            playerCharacter.removeControl(castControl);
            playerCharacter = null;
        }
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
    }

    @Override
    public void onStartScreen() {
    }

    @Override
    public void onEndScreen() {
    }

    private void initializePlayerStatisticsPanels() {
        Element statisticsPanel = screen.findElementByName("panel_statistics");
        List<PlayerData> playerDataList = PlayerData.getPlayers();
        for (PlayerData playerData : playerDataList) {
            statisticsPanels.add(new PlayerStatisticsPanelBuilder(
                    playerData.getId()).build(nifty, screen, statisticsPanel));
        }
    }

    public void showRoundStatistics() {
        initializePlayerStatisticsPanels();
        Sender sender = stateManager.getState(Sender.class);
        sender.addCommand(new TopicOnlyCommand(Topic.BATTLE_STATISTICS_REQUEST));
        Element statisticsLayer = screen.findElementByName("layer_statistics");
        statisticsLayer.show();
    }

    public void hideRoundStatistics() {
        Element statisticsLayer = screen.findElementByName("layer_statistics");
        statisticsLayer.hideWithoutEffect();
    }

    public void updateStatistics() {
        if (latestRoundStatsList == null) {
            return;
        }

        Element statisticsPanel = screen.findElementByName("panel_statistics");
        for (PlayerRoundStats playerRoundStats : latestRoundStatsList) {

            Element damagePanel = statisticsPanel.findElementByName(
                    playerRoundStats.playerId + "-damage");
            Element restorationPanel = statisticsPanel.findElementByName(
                    playerRoundStats.playerId + "-restoration");
            Element killsPanel = statisticsPanel.findElementByName(
                    playerRoundStats.playerId + "-kills");

            damagePanel.getRenderer(TextRenderer.class).setText(
                    String.format("%d", (int) playerRoundStats.damageDone));
            restorationPanel.getRenderer(TextRenderer.class).setText(
                    String.format("%d", (int) playerRoundStats.healthRestored));
            killsPanel.getRenderer(TextRenderer.class).setText(
                    String.format("%d", playerRoundStats.kills));
        }
    }

    public void endGame() {
        clear();

        nifty.gotoScreen("main_menu");

        clear();
        roundTableCreated = false;
    }

    private void loadSpellIcons() {
        addSpellIcon(InputMappingStrings.getId(InputMappingStrings.M1));
        addSpellIcon(InputMappingStrings.getId(InputMappingStrings.M2));
        addSpellIcon(InputMappingStrings.getId(InputMappingStrings.Q));
        addSpellIcon(InputMappingStrings.getId(InputMappingStrings.E));
        addSpellIcon(InputMappingStrings.getId(InputMappingStrings.R));
        addSpellIcon(InputMappingStrings.getId(InputMappingStrings.SPACE));
    }

    private void addSpellIcon(int key) {
        final Element bottomPanel = screen.findElementByName("panel_bottom");
        final SpellCastControl castControl = playerCharacter.getControl(SpellCastControl.class);
        final Spell spell = castControl.getKeySpellNameMapping(key);
        String iconPath;
        if (spell.getIconName() != null) {
            iconPath = "Interface/Images/SpellIcons/" + spell.getIconName();
        } else {
            iconPath = "Interface/Images/SpellIcons/placeholder.png";
        }
        spellIcons.put(spell.getName(), new SpellIconBuilder(spell.getName(),
                iconPath).build(nifty, screen, bottomPanel));
    }

    private void updateSpellIcons() {
        if (playerCharacter == null) {
            return;
        }
        final SpellCastControl castControl = playerCharacter.getControl(SpellCastControl.class);

        for (Map.Entry<String, Element> entry : spellIcons.entrySet()) {
            float cooldown = castControl.getCooldown(Spell.getSpell(entry.getKey()).getId());
            Element overlay = entry.getValue().findElementByName(entry.getKey() + "-overlay");
            if (cooldown <= 0) {
                if (overlay.isVisible()) {
                    overlay.hide();
                }
            } else {
                if (!overlay.isVisible()) {
                    overlay.show();
                }

                Element cooldownText = overlay.findElementByName(entry.getKey() + "-counter");

                if (cooldown > 3) {
                    cooldownText.getRenderer(TextRenderer.class).setText(String.format("%d", (int) cooldown));
                } else {
                    cooldownText.getRenderer(TextRenderer.class).setText(String.format("%.1f", cooldown));
                }
            }
        }
    }

    public void clearBuffIcons() {
        Element buffIcons = screen.findElementByName("panel_right");
        Iterator<Element> it = buffIcons.getElements().iterator();
        for (; it.hasNext();) {
            Element element = it.next();
            nifty.removeElement(screen, element);
        }
    }

    public Nifty getNifty() {
        return nifty;
    }

    public Screen getScreen() {
        return screen;
    }

    public void setLatestRoundStatsList(List<PlayerRoundStats> latestRoundStatsList) {
        this.latestRoundStatsList = latestRoundStatsList;
        Element statisticsLayer = screen.findElementByName("layer_statistics");

        if (statisticsLayer.isVisible()) {
            updateStatistics();
        }
    }

    public void disableCharacterHudControl() {
        if (playerCharacter == null) {
            return;
        }

        playerCharacter.getControl(CharacterHudControl.class).setEnabled(false);
    }
}