package me.mneri.mysql.proto;

import me.mneri.mysql.proto.exception.MalformedPacketException;

public abstract class Packet {
    private Context context;
    private byte sequenceId;

    protected Context getContext() {
        return context;
    }

    public byte getSequenceId() {
        return sequenceId;
    }

    public abstract void payload(byte[] bytes) throws MalformedPacketException;

    public abstract byte[] payload() throws MalformedPacketException;

    void setContext(Context context) {
        this.context = context;
    }

    void setSequenceId(byte sequenceId) {
        this.sequenceId = sequenceId;
    }
}
