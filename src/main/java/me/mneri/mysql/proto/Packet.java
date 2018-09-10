package me.mneri.mysql.proto;

import me.mneri.mysql.proto.exception.MalformedPacketException;

public abstract class Packet {
    private Context context;
    private byte sequenceId;

    public abstract void deserialize(byte[] payload) throws MalformedPacketException;

    protected Context getContext() {
        return context;
    }

    public byte getSequenceId() {
        return sequenceId;
    }

    public abstract byte[] serialize() throws MalformedPacketException;

    void setContext(Context context) {
        this.context = context;
    }

    void setSequenceId(byte sequenceId) {
        this.sequenceId = sequenceId;
    }
}
