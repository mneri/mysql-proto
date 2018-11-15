/*
 * Copyright 2018 Massimo Neri <hello@mneri.me>
 *
 * This file is part of mariadb-proto.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.mneri.mariadb.packet;

import me.mneri.mariadb.proto.Packet;
import me.mneri.mariadb.proto.PayloadReader;
import me.mneri.mariadb.proto.PayloadWriter;

import static me.mneri.mariadb.proto.Capabilities.*;

public class HandshakePacket extends Packet {
    private String authPluginData;
    private String authPluginName;
    private long capabilities;
    private int characterSet;
    private int connectionId;
    private byte protocolVersion;
    private int serverStatus;
    private String serverVersion;

    public void deserialize(PayloadReader reader) {
        //@formatter:off
        setProtocolVersion (reader.getInt1());
        setServerVersion   (reader.getNullTerminatedString());
        setConnectionId    (reader.getInt4());
        //@formatter:on

        if (getProtocolVersion() < 10) {
            setAuthPluginData(reader.getNullTerminatedString());
            return;
        }

        //@formatter:off
        setAuthPluginData (reader.getFixedLengthString(8));
                           reader.skip(1);
        setCapabilities   (reader.getInt2());
        //@formatter:on

        if (!reader.hasMore()) {
            return;
        }

        //@formatter:off
        setCharacterSet (reader.getInt1());
        setServerStatus (reader.getInt2());
        setCapabilities (((int) reader.getInt2() << 16) | getCapabilities());
        //@formatter:on

        int length = 0;

        if (isCapabilitySet(PLUGIN_AUTH)) {
            length = reader.getInt1();
        } else {
            reader.skip(1);
        }

        reader.skip(6);

        if (isCapabilitySet(CLIENT_MYSQL)) {
            reader.skip(4);
        } else {
            setCapabilities(((long) reader.getInt4()) << 32 | getCapabilities());
        }

        if (isCapabilitySet(SECURE_CONNECTION)) {
            setAuthPluginData(getAuthPluginData() + reader.getFixedLengthString(Math.max(12, length)));
            reader.skip(1);
        }

        if (isCapabilitySet(PLUGIN_AUTH)) {
            setAuthPluginName(reader.getNullTerminatedString());
        }
    }

    public String getAuthPluginData() {
        return authPluginData;
    }

    public void setAuthPluginData(String authPluginData) {
        this.authPluginData = authPluginData;
    }

    public String getAuthPluginName() {
        return authPluginName;
    }

    public void setAuthPluginName(String authPluginName) {
        this.authPluginName = authPluginName;
    }

    public long getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(long capabilities) {
        this.capabilities = capabilities;
    }

    public int getCharacterSet() {
        return characterSet;
    }

    public void setCharacterSet(int characterSet) {
        this.characterSet = characterSet;
    }

    public int getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(int connectionId) {
        this.connectionId = connectionId;
    }

    public byte getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(byte protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public int getServerStatus() {
        return serverStatus;
    }

    public void setServerStatus(int serverStatus) {
        this.serverStatus = serverStatus;
    }

    public String getServerVersion() {
        return serverVersion;
    }

    public void setServerVersion(String serverVersion) {
        this.serverVersion = serverVersion;
    }

    private boolean isCapabilitySet(long capability) {
        return (getCapabilities() & capability) != 0;
    }

    @Override
    public void serialize(PayloadWriter writer) {
        //@formatter:off
        writer.putInt1                 (getProtocolVersion());
        writer.putNullTerminatedString (getServerVersion());
        writer.putInt4                 (getConnectionId());
        //@formatter:on

        if (getProtocolVersion() < 10) {
            writer.putNullTerminatedString(getAuthPluginData());
            return;
        }

        //@formatter:off
        writer.putFixedLengthString (getAuthPluginData().substring(0, 8), 8);
        writer.skip                 (1);
        writer.putInt2              ((short) getCapabilities());
        writer.putInt1              ((byte)  getCharacterSet());
        writer.putInt2              ((short) getServerStatus());
        writer.putInt2              ((short) (getCapabilities() >> 16));
        //@formatter:on

        if (isCapabilitySet(PLUGIN_AUTH)) {
            writer.putInt1((byte) (getAuthPluginData().length() - 8));
        } else {
            writer.putInt1((byte) 0);
        }

        writer.skip(6);

        if (isCapabilitySet(CLIENT_MYSQL)) {
            writer.skip(4);
        } else {
            writer.putInt4((int) (getCapabilities() >> 32));
        }

        if (isCapabilitySet(SECURE_CONNECTION)) {
            writer.putNullTerminatedString(getAuthPluginData().substring(8));
        }

        if (isCapabilitySet(PLUGIN_AUTH)) {
            writer.putNullTerminatedString(getAuthPluginName());
        }
        ;
    }
}
