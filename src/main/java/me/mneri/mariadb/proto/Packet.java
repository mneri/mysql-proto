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

package me.mneri.mariadb.proto;

import me.mneri.mariadb.exception.MalformedPacketException;

public abstract class Packet {
    private Context context;
    private byte sequenceId;

    public abstract void deserialize(PayloadReader reader) throws MalformedPacketException;

    protected Context getContext() {
        return context;
    }

    void setContext(Context context) {
        this.context = context;
    }

    public byte getSequenceId() {
        return sequenceId;
    }

    void setSequenceId(byte sequenceId) {
        this.sequenceId = sequenceId;
    }

    public abstract void serialize(PayloadWriter writer) throws MalformedPacketException;
}
