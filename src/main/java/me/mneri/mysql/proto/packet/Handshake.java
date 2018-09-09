package me.mneri.mysql.proto.packet;

import me.mneri.mysql.proto.util.ByteArrayBuilder;
import me.mneri.mysql.proto.util.ByteArrayReader;

import static me.mneri.mysql.proto.packet.Capabilities.*;

public class Handshake implements Packet {
    private String authPluginName;
    private int capabilities;
    private String challenge1;
    private String challenge2;
    private int challenge2Length;
    private int characterSet;
    private int connectionId;
    private int payloadLength;
    private int protocolVersion;
    private String serverVersion;
    private int statusFlag;

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

    public int getProtocolVersion() {
        return protocolVersion;
    }

    public String getServerVersion() {
        return serverVersion;
    }

    public int getStatusFlag() {
        return statusFlag;
    }

    public void readBytes(byte[] bytes) {
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

    public void setProtocolVersion(int protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public void setServerVersion(String serverVersion) {
        this.serverVersion = serverVersion;
    }

    public void setStatusFlag(int statusFlag) {
        this.statusFlag = statusFlag;
    }

    @Override
    public byte[] toByteArray() {
        ByteArrayBuilder builder = new ByteArrayBuilder();
        builder.putInt1((byte) protocolVersion);
        builder.putNullTerminatedString(serverVersion);
        builder.putInt4(connectionId);
        builder.putFixedLengthString(challenge1, 8);
        builder.skip(1);
        builder.putInt2((short) (capabilities >> 16));

        return builder.build();
    }
}
