package me.mneri.mysql.proto.packet;

import me.mneri.mysql.proto.util.ByteArrayBuilder;
import me.mneri.mysql.proto.util.ByteArrayReader;

import static me.mneri.mysql.proto.flag.Capabilities.*;

public class Handshake10 extends Packet {
    private String authPluginName;
    private int capabilities;
    private String challenge1;
    private String challenge2;
    private int challenge2Length;
    private int characterSet;
    private int connectionId;
    private byte protocolVersion;
    private byte sequenceId;
    private String serverVersion;
    private int statusFlag;

    public Handshake10(byte sequenceId) {
        this.sequenceId = sequenceId;
    }

    public String getAuthPluginName() {
        return authPluginName;
    }

    public int getCapabilities() {
        return capabilities;
    }

    public String getChallenge1() {
        return challenge1;
    }

    public String getChallenge2() {
        return challenge2;
    }

    public int getChallenge2Length() {
        return challenge2Length;
    }

    public int getCharacterSet() {
        return characterSet;
    }

    public int getConnectionId() {
        return connectionId;
    }

    public byte getProtocolVersion() {
        return protocolVersion;
    }

    @Override
    public byte getSequenceId() {
        return sequenceId;
    }

    public String getServerVersion() {
        return serverVersion;
    }

    public int getStatusFlag() {
        return statusFlag;
    }

    public boolean isCapabilitySet(int cap) {
        return (getCapabilities() & cap) != 0;
    }

    @Override
    public byte[] payload() {
        ByteArrayBuilder builder = new ByteArrayBuilder();

        //@formatter:off
        builder.putInt1                 (getProtocolVersion());
        builder.putNullTerminatedString (getServerVersion());
        builder.putInt4                 (getConnectionId());
        builder.putFixedLengthString    (getChallenge1(), 8);
        builder.skip                    (1);
        builder.putInt2                 ((short) (getCapabilities() & 0xffff));
        builder.putInt1                 ((byte) getCharacterSet());
        builder.putInt2                 ((short) getStatusFlag());
        builder.putInt2                 ((short) (getCapabilities() >> 16));
        //@formatter:on

        if (isCapabilitySet(CLIENT_PLUGIN_AUTH))
            builder.putInt1((byte) getChallenge2Length());
        else
            builder.putInt1((byte) 0);

        builder.skip(10);

        if (isCapabilitySet(CLIENT_SECURE_CONNECTION)) {
            int length = Math.max(13, getChallenge2Length() - 8);
            builder.putNullTerminatedString(getChallenge2());
        }

        if (isCapabilitySet(CLIENT_PLUGIN_AUTH))
            builder.putNullTerminatedString(getAuthPluginName());

        return builder.build();
    }

    public void payload(byte[] bytes) {
        ByteArrayReader reader = new ByteArrayReader(bytes);

        //@formatter:off
        protocolVersion = reader.getInt1();
        serverVersion   = reader.getNullTerminatedString();
        connectionId    = reader.getInt4();
        challenge1      = reader.getFixedLengthString(8);
                          reader.skip(1);
        capabilities    = reader.getInt2() << 16;
        //@formatter:on

        if (reader.hasMore())
            return;

        //@formatter:off
        characterSet    = reader.getInt1();
        statusFlag      = reader.getInt2();
        capabilities = capabilities | reader.getInt2();
        //@formatter:on

        if ((capabilities & CLIENT_PLUGIN_AUTH) != 0)
            challenge2Length = reader.getInt1();
        else
            reader.skip(1);

        reader.skip(10);

        if ((capabilities & CLIENT_SECURE_CONNECTION) != 0)
            challenge2 = reader.getFixedLengthString(Math.max(13, challenge2Length));

        if ((capabilities & CLIENT_PLUGIN_AUTH) != 0)
            authPluginName = reader.getNullTerminatedString();
    }

    public void setAuthPluginName(String authPluginName) {
        this.authPluginName = authPluginName;
    }

    public void setCapabilities(int capabilities) {
        this.capabilities = capabilities;
    }

    public void setChallenge1(String challenge1) {
        this.challenge1 = challenge1;
    }

    public void setChallenge2(String challenge2) {
        this.challenge2 = challenge2;
    }

    public void setChallenge2Length(int challenge2Length) {
        this.challenge2Length = challenge2Length;
    }

    public void setCharacterSet(int characterSet) {
        this.characterSet = characterSet;
    }

    public void setConnectionId(int connectionId) {
        this.connectionId = connectionId;
    }

    public void setProtocolVersion(byte protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public void setServerVersion(String serverVersion) {
        this.serverVersion = serverVersion;
    }

    public void setStatusFlag(int statusFlag) {
        this.statusFlag = statusFlag;
    }
}
