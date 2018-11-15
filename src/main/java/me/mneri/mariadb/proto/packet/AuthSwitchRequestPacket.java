package me.mneri.mariadb.proto.packet;

import me.mneri.mariadb.proto.Packet;
import me.mneri.mariadb.proto.PayloadReader;
import me.mneri.mariadb.proto.PayloadWriter;
import me.mneri.mariadb.proto.exception.MalformedPacketException;

public class AuthSwitchRequestPacket extends Packet {
    private String pluginName;
    private String pluginProvidedData;

    @Override
    public void deserialize(PayloadReader reader) throws MalformedPacketException {
        if ((reader.getInt1() & 0xFF) != 0xFE) {
            throw new MalformedPacketException();
        }

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
    public void serialize(PayloadWriter writer) {
        //@formatter:off
        writer.putInt1                 ((byte) 0xFE);
        writer.putNullTerminatedString (getPluginName());
        writer.putFixedLengthString    (getPluginProvidedData(), getPluginProvidedData().length());
        //@formatter:on
    }
}
