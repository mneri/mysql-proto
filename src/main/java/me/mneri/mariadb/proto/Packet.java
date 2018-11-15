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
