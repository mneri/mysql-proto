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

import me.mneri.mariadb.exception.MalformedPacketException;
import me.mneri.mariadb.proto.Context;
import me.mneri.mariadb.proto.Packet;
import me.mneri.mariadb.proto.PayloadReader;
import me.mneri.mariadb.proto.PayloadWriter;

import static me.mneri.mariadb.proto.Capabilities.*;
import static me.mneri.mariadb.proto.ServerStatus.SESSION_STATE_CHANGED;

public class OkPacket extends Packet {
    private long affectedRows;
    private String info;
    private long lastInsertId;
    private String sessionStateInfo;
    private short statusFlags;
    private short warnings;

    @Override
    public void deserialize(PayloadReader reader) throws MalformedPacketException {
        Context context = getContext();
        int header = reader.getInt1();

        if (header != 0x00 && header != 0xFE) {
            throw new MalformedPacketException();
        }

        setAffectedRows(reader.getLengthEncodedInt());
        setLastInsertId(reader.getLengthEncodedInt());

        if (context.isCapabilitySet(PROTOCOL_41)) {
            //@formatter:off
            setStatusFlags (reader.getInt2());
            setWarnings    (reader.getInt2());
            //@formatter:on
        } else if (context.isCapabilitySet(TRANSACTIONS)) {
            setStatusFlags(reader.getInt2());
        }

        if (context.isCapabilitySet(CLIENT_SESSION_TRACK)) {
            setInfo(reader.getLengthEncodedString());

            if (context.isStatusFlagSet(SESSION_STATE_CHANGED)) {
                setSessionStateInfo(reader.getLengthEncodedString());
            }
        } else {
            setInfo(reader.getStringEOF());
        }
    }

    public long getAffectedRows() {
        return affectedRows;
    }

    public void setAffectedRows(long affectedRows) {
        this.affectedRows = affectedRows;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public long getLastInsertId() {
        return lastInsertId;
    }

    public void setLastInsertId(long lastInsertId) {
        this.lastInsertId = lastInsertId;
    }

    public String getSessionStateInfo() {
        return sessionStateInfo;
    }

    public void setSessionStateInfo(String sessionStateInfo) {
        this.sessionStateInfo = sessionStateInfo;
    }

    public short getStatusFlags() {
        return statusFlags;
    }

    public void setStatusFlags(short statusFlags) {
        this.statusFlags = statusFlags;
    }

    public short getWarnings() {
        return warnings;
    }

    public void setWarnings(short warnings) {
        this.warnings = warnings;
    }

    @Override
    public void serialize(PayloadWriter writer) {
        Context context = getContext();

        //@formatter:off
        writer.putInt1             ((byte) 0x00);
        writer.putLengthEncodedInt (getAffectedRows());
        writer.putLengthEncodedInt (getLastInsertId());
        //@formatter:on

        if (context.isCapabilitySet(PROTOCOL_41)) {
            writer.putInt2(getStatusFlags());
            writer.putInt2(getWarnings());
        } else if (context.isCapabilitySet(TRANSACTIONS)) {
            writer.putInt2(getStatusFlags());
        }

        if (context.isCapabilitySet(CLIENT_SESSION_TRACK)) {
            writer.putLengthEncodedString(getInfo());

            if (context.isStatusFlagSet(SESSION_STATE_CHANGED)) {
                writer.putLengthEncodedString(getSessionStateInfo());
            }
        } else {
            writer.putFixedLengthString(getInfo(), getInfo().length());
        }
    }
}
