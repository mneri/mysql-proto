package me.mneri.mariadb.proto.packet;

import me.mneri.mariadb.proto.Packet;
import me.mneri.mariadb.proto.exception.MalformedPacketException;
import me.mneri.mariadb.proto.util.ByteArrayBuilder;
import me.mneri.mariadb.proto.util.ByteArrayWriter;

public class AuthSwitchRequest extends Packet {
    private String pluginName;
    private String pluginProvidedData;

    @Override
    public void deserialize(byte[] payload) throws MalformedPacketException {
        ByteArrayWriter reader = new ByteArrayWriter(payload);

        if ((reader.getInt1() & 0xFF) != 0xFE)
            throw new MalformedPacketException();

        //@formatter:off
        setPluginName         (reader.getNullTerminatedString());
        setPluginProvidedData (reader.getStringEOF());
        //@formatter:on
    }

    public String getPluginName() {
        return pluginName;
    }

    public void setPluginName(String pluginName) {
        this.pluginName = pluginName;
    }

    public String getPluginProvidedData() {
        return pluginProvidedData;
    }

    public void setPluginProvidedData(String pluginProvidedData) {
        this.pluginProvidedData = pluginProvidedData;
    }

    @Override
    public byte[] serialize() {
        ByteArrayBuilder builder = new ByteArrayBuilder();

        //@formatter:off
        builder.putInt1                 ((byte) 0xFE);
        builder.putNullTerminatedString (getPluginName());
        builder.putFixedLengthString    (getPluginProvidedData(), getPluginProvidedData().length());
        //@formatter:on

        return builder.build();
    }
}
