package me.mneri.mysql.proto.flag;

public class Capabilities {
    //@formatter:off
    public static final int CLIENT_LONG_PASSWORD                  = 1;
    public static final int CLIENT_FOUND_ROWS                     = 2;
    public static final int CLIENT_LONG_FLAG                      = 4;
    public static final int CLIENT_CONNECT_WITH_DB                = 8;
    public static final int CLIENT_NO_SCHEMA                      = 16;
    public static final int CLIENT_COMPRESS                       = 32;
    public static final int CLIENT_ODBC                           = 64;
    public static final int CLIENT_LOCAL_FILES                    = 128;
    public static final int CLIENT_IGNORE_SPACE                   = 256;
    public static final int CLIENT_PROTOCOL_41                    = 512;
    public static final int CLIENT_INTERACTIVE                    = 1024;
    public static final int CLIENT_SSL                            = 2048;
    public static final int CLIENT_IGNORE_SIGPIPE                 = 4096;
    public static final int CLIENT_TRANSACTIONS                   = 8192;
    public static final int CLIENT_RESERVED                       = 16384;
    public static final int CLIENT_SECURE_CONNECTION              = 32768;
    public static final int CLIENT_MULTI_STATEMENTS               = 1 << 16;
    public static final int CLIENT_MULTI_RESULTS                  = 1 << 17;
    public static final int CLIENT_PS_MULTI_RESULTS               = 1 << 18;
    public static final int CLIENT_PLUGIN_AUTH                    = 1 << 19;
    public static final int CLIENT_CONNECT_ATTRS                  = 1 << 20;
    public static final int CLIENT_PLUGIN_AUTH_LENENC_CLIENT_DATA = 1 << 21;
    public static final int CLIENT_CAN_HANDLE_EXPIRED_PASSWORDS   = 1 << 22;
    public static final int CLIENT_SSL_VERIFY_SERVER_CERT         = 1 << 30;
    public static final int CLIENT_REMEMBER_OPTIONS               = 1 << 31;
    //@formatter:on
}
