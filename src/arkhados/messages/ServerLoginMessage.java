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
public class ServerLoginMessage extends AbstractMessage {
    private String name;
    private long playerId;
    private boolean accepted;

    public ServerLoginMessage() {

    }

    public ServerLoginMessage(String nick, long playerId, boolean accepted) {
        this.name = nick;
        this.playerId = playerId;
        this.accepted = accepted;
    }

    public String getName() {
        return this.name;
    }

    public long getPlayerId() {
        return this.playerId;
    }

    public boolean isAccepted() {
        return this.accepted;
    }

}