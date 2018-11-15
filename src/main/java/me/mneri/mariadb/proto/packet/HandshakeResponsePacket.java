package me.mneri.mariadb.proto.packet;

import me.mneri.mariadb.proto.Packet;
import me.mneri.mariadb.proto.util.ByteArrayReader;
import me.mneri.mariadb.proto.util.ByteArrayWriter;

import java.util.HashMap;
import java.util.Map;

import static me.mneri.mariadb.proto.Capabilities.*;

public class HandshakeResponsePacket extends Packet {
    private String authPluginName;
    private String authResponse;
    private long capabilities;
    private byte characterSet;
    private Map<String, String> connectAttributes;
    private String database;
    private int maxPacketSize;
    private String username;

    @Override
    public void deserialize(byte[] payload) {
        ByteArrayReader reader = new ByteArrayReader(payload);

        //@formatter:off
        setCapabilities  (reader.getInt4());
        setMaxPacketSize (reader.getInt4());
        setCharacterSet  (reader.getInt1());
                          reader.skip(19);
        //@formatter:on

        if (!isCapabilitySet(CLIENT_MYSQL)) {
            setCapabilities(reader.getInt4() << 16 | getCapabilities());
        } else {
            reader.skip(4);
        }

        setUsername(reader.getNullTerminatedString());

        if (isCapabilitySet(PLUGIN_AUTH_LENENC_DATA)) {
            setAuthResponse(reader.getLengthEncodedString());
        } else if (isCapabilitySet(SECURE_CONNECTION)) {
            int length = reader.getInt1();
            setAuthResponse(reader.getFixedLengthString(length));
        } else {
            reader.skip(1);
        }

        if (isCapabilitySet(CONNECT_WITH_DB)) {
            setDatabase(reader.getNullTerminatedString());
        }

        if (isCapabilitySet(PLUGIN_AUTH)) {
            setAuthPluginName(reader.getNullTerminatedString());
        }

        if (isCapabilitySet(CONNECT_ATTRS)) {
            int size = (int) reader.getLengthEncodedInt();
            Map<String, String> connectAttributes = new HashMap<>();

            for (int i = 0; i < size; i++) {
                connectAttributes.put(reader.getLengthEncodedString(), reader.getLengthEncodedString());
            }

            setConnectAttributes(connectAttributes);
        }
    }

    public String getAuthPluginName() {
        return authPluginName;
    }

    public void setAuthPluginName(String authPluginName) {
        this.authPluginName = authPluginName;
    }

    public String getAuthResponse() {
        return authResponse;
    }

    public void setAuthResponse(String authResponse) {
        this.authResponse = authResponse;
    }

    public long getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(long capabilities) {
        this.capabilities = capabilities;
    }

    public byte getCharacterSet() {
        return characterSet;
    }

    public void setCharacterSet(byte characterSet) {
        this.characterSet = characterSet;
    }

    public Map<String, String> getConnectAttributes() {
        return connectAttributes;
    }

    public void setConnectAttributes(Map<String, String> connectAttributes) {
        this.connectAttributes = connectAttributes;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public int getMaxPacketSize() {
        return maxPacketSize;
    }

    public void setMaxPacketSize(int maxPacketSize) {
        this.maxPacketSize = maxPacketSize;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private boolean isCapabilitySet(long capability) {
        return (getCapabilities() & capability) != 0;
    }

    @Override
    public byte[] serialize() {
        ByteArrayWriter builder = new ByteArrayWriter();

        //@formatter:off
        builder.putInt4 ((int) getCapabilities());
        builder.putInt4 (getMaxPacketSize());
        builder.putInt1 (getCharacterSet());
        builder.skip    (19);
        //@formatter:on

        if (!isCapabilitySet(CLIENT_MYSQL)) {
            builder.putInt4((int) (getCapabilities() >> 16));
        } else {
            builder.skip(4);
        }

        builder.putNullTerminatedString(getUsername());

        if (isCapabilitySet(PLUGIN_AUTH_LENENC_DATA)) {
            builder.putLengthEncodedString(getAuthResponse());
        } else if (isCapabilitySet(SECURE_CONNECTION)) {
            int length = getAuthResponse().length();
            builder.putInt1((byte) length);
            builder.putFixedLengthString(getAuthResponse(), length);
        } else {
            builder.skip(1);
        }

        if (isCapabilitySet(CONNECT_WITH_DB)) {
            builder.putNullTerminatedString(getDatabase());
        }

        if (isCapabilitySet(PLUGIN_AUTH)) {
            builder.putNullTerminatedString(getAuthPluginName());
        }

        if (isCapabilitySet(CONNECT_ATTRS)) {
            Map<String, String> connectAttributes = getConnectAttributes();
            int size = connectAttributes.size();

            builder.putLengthEncodedInt(size);

            for (String key : connectAttributes.keySet()) {
                builder.putLengthEncodedString(key);
                builder.putLengthEncodedString(connectAttributes.get(key));
            }
        }

        return builder.build();
    }
}
