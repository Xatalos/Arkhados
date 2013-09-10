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

package arkhados.messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 *
 * @author william
 */
@Serializable
public class SetPlayersCharacterMessage extends AbstractMessage {
    private long entityId;
    private long playerId;

    public SetPlayersCharacterMessage() {

    }

    public SetPlayersCharacterMessage(long entityId, long playerId) {
        this.entityId = entityId;
        this.playerId = playerId;
    }

    public long getEntityId() {
        return this.entityId;
    }

    public long getPlayerId() {
        return this.playerId;
    }




}