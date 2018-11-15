package me.mneri.mariadb.proto.packet;

import me.mneri.mariadb.proto.Capabilities;
import me.mneri.mariadb.proto.Packet;
import me.mneri.mariadb.proto.exception.ProtocolMismatchException;
import me.mneri.mariadb.proto.util.ByteArrayReader;
import me.mneri.mariadb.proto.util.ByteArrayWriter;

public class Handshake10Packet extends Packet {
    private String authPluginData;
    private String authPluginName;
    private long capabilities;
    private int characterSet;
    private int connectionId;
    private int serverStatus;
    private String serverVersion;

    public void deserialize(byte[] payload) {
        ByteArrayReader reader = new ByteArrayReader(payload);

        if (reader.getInt1() != 10) {
            throw new ProtocolMismatchException();
        }

        //@formatter:off
        setServerVersion  (reader.getNullTerminatedString());
        setConnectionId   (reader.getInt4());
        setAuthPluginData (reader.getFixedLengthString(8));
                           reader.skip(1);
        setCapabilities   (reader.getInt2() << 16);
        //@formatter:on

        if (!reader.hasMore()) {
            return;
        }

        //@formatter:off
        setCharacterSet (reader.getInt1());
        setServerStatus (reader.getInt2());
        setCapabilities (reader.getInt2() | getCapabilities());
        //@formatter:on

        int length = 0;

        if (isCapabilitySet(Capabilities.PLUGIN_AUTH)) {
            length = reader.getInt1();
        } else {
            reader.skip(1);
        }

        reader.skip(10);

        if (isCapabilitySet(Capabilities.SECURE_CONNECTION)) {
            setAuthPluginData(getAuthPluginData() + reader.getFixedLengthString(Math.max(13, length)));
        }

        if (isCapabilitySet(Capabilities.PLUGIN_AUTH)) {
            setAuthPluginName(reader.getNullTerminatedString());
        }
    }

    public String getAuthPluginData() {
        return authPluginData;
    }

    public void setAuthPluginData(String authPluginData) {
        this.authPluginData = authPluginData;
    }

    public String getAuthPluginName() {
        return authPluginName;
    }

    public void setAuthPluginName(String authPluginName) {
        this.authPluginName = authPluginName;
    }

    public long getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(long capabilities) {
        this.capabilities = capabilities;
    }

    public int getCharacterSet() {
        return characterSet;
    }

    public void setCharacterSet(int characterSet) {
        this.characterSet = characterSet;
    }

    public int getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(int connectionId) {
        this.connectionId = connectionId;
    }

    public int getServerStatus() {
        return serverStatus;
    }

    public void setServerStatus(int serverStatus) {
        this.serverStatus = serverStatus;
    }

    public String getServerVersion() {
        return serverVersion;
    }

    public void setServerVersion(String serverVersion) {
        this.serverVersion = serverVersion;
    }

    private boolean isCapabilitySet(long capability) {
        return (getCapabilities() & capability) != 0;
    }

    @Override
    public byte[] serialize() {
        ByteArrayWriter builder = new ByteArrayWriter();

        //@formatter:off
        builder.putInt1                 ((byte) 10);
        builder.putNullTerminatedString (getServerVersion());
        builder.putInt4                 (getConnectionId());
        builder.putFixedLengthString    (getAuthPluginData().substring(0, 8), 8);
        builder.skip                    (1);
        builder.putInt2                 ((short) getCapabilities());
        builder.putInt1                 ((byte)  getCharacterSet());
        builder.putInt2                 ((short) getServerStatus());
        builder.putInt2                 ((short) (getCapabilities() >> 16));
        //@formatter:on

        if (isCapabilitySet(Capabilities.PLUGIN_AUTH)) {
            builder.putInt1((byte) (getAuthPluginData().length() - 8));
        } else {
            builder.putInt1((byte) 0);
        }

        builder.skip(10);

        if (isCapabilitySet(Capabilities.SECURE_CONNECTION)) {
            builder.putNullTerminatedString(getAuthPluginData().substring(8));
        }

        if (isCapabilitySet(Capabilities.PLUGIN_AUTH)) {
            builder.putNullTerminatedString(getAuthPluginName());
        }

        return builder.build();
    }
}
