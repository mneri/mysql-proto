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
import me.mneri.mariadb.exception.MalformedPacketException;

public class AuthSwitchRequestPacket extends Packet {
    private String pluginName;
    private String pluginProvidedData;

    @Override
    public void deserialize(PayloadReader reader) throws MalformedPacketException {
        if ((reader.getInt1() & 0xFF) != 0xFE) {
            throw new MalformedPacketException();
        }

        //@formatter:off
        setPluginName         (reader.getNullTerminatedString());
        setPluginProvidedData (reader.getStringEOF());
        //@formatter:on
    }

    public String getPluginName() {
        return pluginName;
    }

    public void setPluginName(String pluginName) {
        this.pluginName = pluginName;
    }

    public String getPluginProvidedData() {
        return pluginProvidedData;
    }

    public void setPluginProvidedData(String pluginProvidedData) {
        this.pluginProvidedData = pluginProvidedData;
    }

    @Override
    public void serialize(PayloadWriter writer) {
        //@formatter:off
        writer.putInt1                 ((byte) 0xFE);
        writer.putNullTerminatedString (getPluginName());
        writer.putFixedLengthString    (getPluginProvidedData(), getPluginProvidedData().length());
        //@formatter:on
    }
}
