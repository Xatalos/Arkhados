/*    This file is part of JMageBattle.

    JMageBattle is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    JMageBattle is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with JMageBattle.  If not, see <http://www.gnu.org/licenses/>. */

package magebattle.messages;

import magebattle.messages.roundprotocol.RoundFinishedMessage;
import magebattle.messages.roundprotocol.NewRoundMessage;
import com.jme3.network.serializing.Serializer;
import magebattle.messages.roundprotocol.ClientWorldCreatedMessage;
import magebattle.messages.roundprotocol.PlayerReadyForNewRoundMessage;
import magebattle.messages.roundprotocol.CreateWorldMessage;
import magebattle.messages.syncmessages.AddEntityMessage;
import magebattle.messages.syncmessages.RemoveEntityMessage;
import magebattle.messages.syncmessages.StartCastingSpellMessage;
import magebattle.messages.syncmessages.SyncCharacterMessage;
import magebattle.messages.syncmessages.SyncProjectileMessage;
import magebattle.messages.usercommands.UcCastSpellMessage;
import magebattle.messages.usercommands.UcWalkDirection;

/**
 *
 * @author william
 */
public class MessageUtils {

    public static void registerMessages() {

        // <Lobby>
        Serializer.registerClass(ServerLoginMessage.class);
        Serializer.registerClass(ClientLoginMessage.class);
        Serializer.registerClass(PlayerDataTableMessage.class);
        Serializer.registerClass(ChatMessage.class);
        Serializer.registerClass(StartGameMessage.class);

        // </Lobby>

        // <RoundProtocol>
        Serializer.registerClass(CreateWorldMessage.class);
        Serializer.registerClass(ClientWorldCreatedMessage.class);
        Serializer.registerClass(PlayerReadyForNewRoundMessage.class);
        Serializer.registerClass(NewRoundMessage.class);
        Serializer.registerClass(RoundFinishedMessage.class);
        // </RoundProtocol>

        Serializer.registerClass(SetPlayersCharacterMessage.class);

        // <Sync>
        Serializer.registerClass(AddEntityMessage.class);
        Serializer.registerClass(RemoveEntityMessage.class);
        Serializer.registerClass(SyncCharacterMessage.class);
        Serializer.registerClass(SyncProjectileMessage.class);
        Serializer.registerClass(StartCastingSpellMessage.class);
        // </Sync>

        // <UserCommands>
        Serializer.registerClass(UcCastSpellMessage.class);
        Serializer.registerClass(UcWalkDirection.class);
        // </UserCommands>
    }
}
