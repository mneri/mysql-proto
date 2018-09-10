package me.mneri.mysql.proto.packet;

import java.util.HashMap;
import java.util.Map;
import me.mneri.mysql.proto.Packet;
import me.mneri.mysql.proto.util.ByteArrayBuilder;
import me.mneri.mysql.proto.util.ByteArrayReader;

import static me.mneri.mysql.proto.Capabilities.*;

public class HandshakeResponse41 extends Packet {
    private String authPluginName;
    private String authResponse;
    private int capabilities;
    private byte characterSet;
    private Map<String, String> connectAttributes;
    private String database;
    private int maxPacketSize;
    private String username;

    public String getAuthPluginName() {
        return authPluginName;
    }

    public String getAuthResponse() {
        return authResponse;
    }

    public int getCapabilities() {
        return capabilities;
    }

    public byte getCharacterSet() {
        return characterSet;
    }

    public Map<String, String> getConnectAttributes() {
        return connectAttributes;
    }

    public String getDatabase() {
        return database;
    }

    public int getMaxPacketSize() {
        return maxPacketSize;
    }

    public String getUsername() {
        return username;
    }

    private boolean isCapabilitySet(int capability) {
        return (getCapabilities() & capability) != 0;
    }

    @Override
    public byte[] payload() {
        ByteArrayBuilder builder = new ByteArrayBuilder();

        //@formatter:off
        builder.putInt4                 (getCapabilities());
        builder.putInt4                 (getMaxPacketSize());
        builder.putInt1                 (getCharacterSet());
        builder.skip                    (23);
        builder.putNullTerminatedString (getUsername());
        //@formatter:on

        if (isCapabilitySet(CLIENT_PLUGIN_AUTH_LENENC_CLIENT_DATA)) {
            builder.putLengthEncodedString(getAuthResponse());
        } else {
            int length = getAuthResponse().length();
            builder.putInt1((byte) length);
            builder.putFixedLengthString(getAuthResponse(), length);
        }

        if (isCapabilitySet(CLIENT_CONNECT_WITH_DB))
            builder.putNullTerminatedString(getDatabase());

        if (isCapabilitySet(CLIENT_PLUGIN_AUTH))
            builder.putNullTerminatedString(getAuthPluginName());

        if (isCapabilitySet(CLIENT_CONNECT_ATTRS)) {
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

    @Override
    public void payload(byte[] bytes) {
        ByteArrayReader reader = new ByteArrayReader(bytes);

        //@formatter:off
        setCapabilities (reader.getInt4());
        setMaxPacketSize(reader.getInt4());
        setCharacterSet (reader.getInt1());
                         reader.skip(23);
        setUsername     (reader.getNullTerminatedString());
        //@formatter:on

        if (isCapabilitySet(CLIENT_PLUGIN_AUTH_LENENC_CLIENT_DATA)) {
            setAuthResponse(reader.getLengthEncodedString());
        } else {
            int length = reader.getInt1();
            setAuthResponse(reader.getFixedLengthString(length));
        }

        if (isCapabilitySet(CLIENT_CONNECT_WITH_DB))
            setDatabase(reader.getNullTerminatedString());

        if (isCapabilitySet(CLIENT_PLUGIN_AUTH))
            setAuthPluginName(reader.getNullTerminatedString());

        if (isCapabilitySet(CLIENT_CONNECT_ATTRS)) {
            int size = (int) reader.getLengthEncodedInt();
            Map<String, String> connectAttributes = new HashMap<>();

            for (int i = 0; i < size; i++)
                connectAttributes.put(reader.getLengthEncodedString(), reader.getLengthEncodedString());

            setConnectAttributes(connectAttributes);
        }
    }

    public void setAuthPluginName(String authPluginName) {
        this.authPluginName = authPluginName;
    }

    public void setAuthResponse(String authResponse) {
        this.authResponse = authResponse;
    }

    public void setCapabilities(int capabilities) {
        this.capabilities = capabilities;
    }

    public void setCharacterSet(byte characterSet) {
        this.characterSet = characterSet;
    }

    public void setConnectAttributes(Map<String, String> connectAttributes) {
        this.connectAttributes = connectAttributes;
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
