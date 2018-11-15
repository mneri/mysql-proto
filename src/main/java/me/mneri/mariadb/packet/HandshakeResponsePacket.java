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

import java.util.HashMap;
import java.util.Map;

import static me.mneri.mariadb.proto.Capabilities.*;

public class HandshakeResponsePacket extends Packet {
    private String authPluginName;
    private String authResponse;
    private long capabilities;
    private byte characterSet;
    private Map<String, String> connectAttributes;
    private String database;
    private int maxPacketSize;
    private String username;

    @Override
    public void deserialize(PayloadReader reader) {
        //@formatter:off
        setCapabilities  (reader.getInt4());
        setMaxPacketSize (reader.getInt4());
        setCharacterSet  (reader.getInt1());
                          reader.skip(19);
        //@formatter:on

        if (!isCapabilitySet(CLIENT_MYSQL)) {
            setCapabilities(reader.getInt4() << 16 | getCapabilities());
        } else {
            reader.skip(4);
        }

        setUsername(reader.getNullTerminatedString());

        if (isCapabilitySet(PLUGIN_AUTH_LENENC_DATA)) {
            setAuthResponse(reader.getLengthEncodedString());
        } else if (isCapabilitySet(SECURE_CONNECTION)) {
            int length = reader.getInt1();
            setAuthResponse(reader.getFixedLengthString(length));
        } else {
            reader.skip(1);
        }

        if (isCapabilitySet(CONNECT_WITH_DB)) {
            setDatabase(reader.getNullTerminatedString());
        }

        if (isCapabilitySet(PLUGIN_AUTH)) {
            setAuthPluginName(reader.getNullTerminatedString());
        }

        if (isCapabilitySet(CONNECT_ATTRS)) {
            int size = (int) reader.getLengthEncodedInt();
            Map<String, String> connectAttributes = new HashMap<>();

            for (int i = 0; i < size; i++) {
                connectAttributes.put(reader.getLengthEncodedString(), reader.getLengthEncodedString());
            }

            setConnectAttributes(connectAttributes);
        }
    }

    public String getAuthPluginName() {
        return authPluginName;
    }

    public void setAuthPluginName(String authPluginName) {
        this.authPluginName = authPluginName;
    }

    public String getAuthResponse() {
        return authResponse;
    }

    public void setAuthResponse(String authResponse) {
        this.authResponse = authResponse;
    }

    public long getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(long capabilities) {
        this.capabilities = capabilities;
    }

    public byte getCharacterSet() {
        return characterSet;
    }

    public void setCharacterSet(byte characterSet) {
        this.characterSet = characterSet;
    }

    public Map<String, String> getConnectAttributes() {
        return connectAttributes;
    }

    public void setConnectAttributes(Map<String, String> connectAttributes) {
        this.connectAttributes = connectAttributes;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public int getMaxPacketSize() {
        return maxPacketSize;
    }

    public void setMaxPacketSize(int maxPacketSize) {
        this.maxPacketSize = maxPacketSize;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private boolean isCapabilitySet(long capability) {
        return (getCapabilities() & capability) != 0;
    }

    @Override
    public void serialize(PayloadWriter writer) {
        //@formatter:off
        writer.putInt4 ((int) getCapabilities());
        writer.putInt4 (getMaxPacketSize());
        writer.putInt1 (getCharacterSet());
        writer.skip    (19);
        //@formatter:on

        if (!isCapabilitySet(CLIENT_MYSQL)) {
            writer.putInt4((int) (getCapabilities() >> 16));
        } else {
            writer.skip(4);
        }

        writer.putNullTerminatedString(getUsername());

        if (isCapabilitySet(PLUGIN_AUTH_LENENC_DATA)) {
            writer.putLengthEncodedString(getAuthResponse());
        } else if (isCapabilitySet(SECURE_CONNECTION)) {
            int length = getAuthResponse().length();
            writer.putInt1((byte) length);
            writer.putFixedLengthString(getAuthResponse(), length);
        } else {
            writer.skip(1);
        }

        if (isCapabilitySet(CONNECT_WITH_DB)) {
            writer.putNullTerminatedString(getDatabase());
        }

        if (isCapabilitySet(PLUGIN_AUTH)) {
            writer.putNullTerminatedString(getAuthPluginName());
        }

        if (isCapabilitySet(CONNECT_ATTRS)) {
            Map<String, String> connectAttributes = getConnectAttributes();
            int size = connectAttributes.size();

            writer.putLengthEncodedInt(size);

            for (String key : connectAttributes.keySet()) {
                writer.putLengthEncodedString(key);
                writer.putLengthEncodedString(connectAttributes.get(key));
            }
        }
    }
}
