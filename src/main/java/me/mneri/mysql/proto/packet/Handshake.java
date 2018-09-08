package me.mneri.mysql.proto.packet;

import me.mneri.mysql.proto.ByteArrayReader;

public class Handshake extends Packet {
    private String authPluginData1;
    private String authPluginData2;
    private int authPluginDataLength;
    private int capabilityFlags;
    private int characterSet;
    private int connectionId;
    private int payloadLength;
    private int protocolVersion;
    private int sequenceId;
    private String serverVersion;
    private int statusFlag;

    public Handshake(byte[] buff) {
        ByteArrayReader reader = new ByteArrayReader(buff);

        //@formatter:off
        payloadLength   = reader.getInt3();
        sequenceId      = reader.getInt1();
        protocolVersion = reader.getInt1();
        serverVersion   = reader.getNullTerminatedString();
        connectionId    = reader.getInt4();
        authPluginData1 = reader.getFixedLengthString(8);
                          reader.skip(1);
        capabilityFlags = reader.getInt2() << 16;
        //@formatter:on

        if (reader.hasMore())
            return;

        //@formatter:off
        characterSet    = reader.getInt1();
        statusFlag      = reader.getInt2();
        capabilityFlags = capabilityFlags | reader.getInt2();
        //@formatter:on

        if ((capabilityFlags & Capabilities.CLIENT_PLUGIN_AUTH) != 0)
            authPluginDataLength = reader.getInt1();
        else
            reader.skip(1);

        reader.skip(10);
    }

    public int getCharacterSet() {
        return characterSet;
    }

    public int getConnectionId() {
        return connectionId;
    }

    @Override
    public int getPayloadLength() {
        return payloadLength;
    }

    public int getProtocolVersion() {
        return protocolVersion;
    }

    @Override
    public int getSequenceId() {
        return sequenceId;
    }

    public String getServerVersion() {
        return serverVersion;
    }

    @Override
    public byte[] toByteArray() {
        return new byte[0];
    }
}
