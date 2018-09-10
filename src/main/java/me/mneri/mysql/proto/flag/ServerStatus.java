package me.mneri.mysql.proto.flag;

public class ServerStatus {
    //@formatter:off
    public static final int IN_TRANS                 = 1;
    public static final int AUTOCOMMIT               = 2;
    public static final int MORE_RESULTS_EXISTS      = 8;
    public static final int QUERY_NO_GOOD_INDEX_USED = 16;
    public static final int NO_GOOD_INDEX_USED       = QUERY_NO_GOOD_INDEX_USED;
    public static final int QUERY_NO_INDEX_USED      = 32;
    public static final int NO_INDEX_USED            = QUERY_NO_INDEX_USED;
    public static final int CURSOR_EXISTS            = 64;
    public static final int LAST_ROW_SENT            = 128;
    public static final int DB_DROPPED               = 256;
    public static final int NO_BACKSLASH_ESCAPES     = 512;
    public static final int METADATA_CHANGED         = 1024;
    public static final int QUERY_WAS_SLOW           = 2048;
    public static final int PS_OUT_PARAMS            = 4096;
    public static final int IN_TRANS_READONLY        = 8192;
    public static final int SESSION_STATE_CHANGED    = 16384;
    //@formatter:on
    
    private ServerStatus() {
    }
}
