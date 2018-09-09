package me.mneri.mysql.proto.packet;

import me.mneri.mysql.proto.util.ByteArrayReader;

import static me.mneri.mysql.proto.packet.Capabilities.*;

public class HandshakeResponse41 extends Packet {
    private String authPluginName;
    private String authResponse;
    private long authResponseLength;
    private int capabilities;
    private int characterSet;
    private String database;
    private int maxPacketSize;
    private byte sequenceId;
    private String username;

    public HandshakeResponse41(byte sequenceId) {
        this.sequenceId = sequenceId;
    }

    public String getAuthPluginName() {
        return authPluginName;
    }

    public String getAuthResponse() {
        return authResponse;
    }

    public long getAuthResponseLength() {
        return authResponseLength;
    }

    public int getCapabilities() {
        return capabilities;
    }

    public int getCharacterSet() {
        return characterSet;
    }

    public String getDatabase() {
        return database;
    }

    public int getMaxPacketSize() {
        return maxPacketSize;
    }

    @Override
    public byte getSequenceId() {
        return sequenceId;
    }

    public String getUsername() {
        return username;
    }

    private boolean isCapabilitySet(int cap) {
        return (getCapabilities() & cap) != 0;
    }

    @Override
    public byte[] payloadBytes() {
        return new byte[0];
    }

    @Override
    public void readPayload(byte[] bytes) {
        ByteArrayReader reader = new ByteArrayReader(bytes);

        //@formatter:off
        capabilities  = reader.getInt4();
        maxPacketSize = reader.getInt4();
        characterSet  = reader.getInt1();
                        reader.skip(23);
        username      = reader.getNullTerminatedString();
        //@formatter:on

        if (isCapabilitySet(CLIENT_PLUGIN_AUTH_LENENC_CLIENT_DATA)) {
            authResponseLength = reader.getLengthEncodedInt();
            authResponse = reader.getFixedLengthString((int) authResponseLength);
        } else if (isCapabilitySet(CLIENT_SECURE_CONNECTION)) {
            authResponseLength = reader.getInt1();
            authResponse = reader.getFixedLengthString((int) authResponseLength);
        } else {
            authResponse = reader.getNullTerminatedString();
        }

        if (isCapabilitySet(CLIENT_CONNECT_WITH_DB))
            database = reader.getNullTerminatedString();

        if (isCapabilitySet(CLIENT_PLUGIN_AUTH))
            authPluginName = reader.getNullTerminatedString();
    }

    public void setAuthPluginName(String authPluginName) {
        this.authPluginName = authPluginName;
    }

    public void setAuthResponse(String authResponse) {
        this.authResponse = authResponse;
    }

    public void setAuthResponseLength(long authResponseLength) {
        this.authResponseLength = authResponseLength;
    }

    public void setCapabilities(int capabilities) {
        this.capabilities = capabilities;
    }

    public void setCharacterSet(int characterSet) {
        this.characterSet = characterSet;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public void setMaxPacketSize(int maxPacketSize) {
        this.maxPacketSize = maxPacketSize;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
