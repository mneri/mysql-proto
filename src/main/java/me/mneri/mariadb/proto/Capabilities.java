package me.mneri.mariadb.proto;

public final class Capabilities {
    //@formatter:off
    public static final long LONG_PASSWORD                       = 1;
    public static final long FOUND_ROWS                          = 1 << 1;
    public static final long LONG_FLAG                           = 1 << 2;
    public static final long CONNECT_WITH_DB                     = 1 << 3;
    public static final long NO_SCHEMA                           = 1 << 4;
    public static final long COMPRESS                            = 1 << 5;
    public static final long ODBC                                = 1 << 6;
    public static final long LOCAL_FILES                         = 1 << 7;
    public static final long IGNORE_SPACE                        = 1 << 8;
    public static final long PROTOCOL_41                         = 1 << 9;
    public static final long INTERACTIVE                         = 1 << 10;
    public static final long SSL                                 = 1 << 11;
    public static final long IGNORE_SIGPIPE                      = 1 << 12;
    public static final long TRANSACTIONS                        = 1 << 13;
    public static final long RESERVED                            = 1 << 14;
    public static final long SECURE_CONNECTION                   = 1 << 15;
    public static final long MULTI_STATEMENTS                    = 1 << 16;
    public static final long MULTI_RESULTS                       = 1 << 17;
    public static final long PS_MULTI_RESULTS                    = 1 << 18;
    public static final long PLUGIN_AUTH                         = 1 << 19;
    public static final long CONNECT_ATTRS                       = 1 << 20;
    public static final long PLUGIN_AUTH_LENENC_DATA             = 1 << 21;
    public static final long CAN_HANDLE_EXPIRED_PASSWORDS        = 1 << 22;
    public static final long CLIENT_SESSION_TRACK                = 1 << 23;
    public static final long CLIENT_DEPRECATE_EOF                = 1 << 24;
    public static final long SSL_VERIFY_SERVER_CERT              = 1 << 30;
    public static final long REMEMBER_OPTIONS                    = 1 << 31;
    public static final long MARIADB_CLIENT_PROGRESS             = 1 << 32;
    public static final long MARIADB_CLIENT_COM_MULTI            = 1 << 33;
    public static final long MARIADB_CLIENT_STMT_BULK_OPERATIONS = 1 << 34;
    //@formatter:on

    private Capabilities() {
    }
}
