/*
 * Copyright 2018 Massimo Neri <hello@mneri.me>
 *
 * This file is part of mariadb-proto.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.mneri.mariadb.proto;

public final class ServerStatus {
    //@formatter:off
    public static final int IN_TRANS                 = 1;
    public static final int AUTOCOMMIT               = 1 << 1;
    public static final int MORE_RESULTS_EXISTS      = 1 << 3;
    public static final int QUERY_NO_GOOD_INDEX_USED = 1 << 4;
    public static final int NO_GOOD_INDEX_USED       = QUERY_NO_GOOD_INDEX_USED;
    public static final int QUERY_NO_INDEX_USED      = 1 << 5;
    public static final int NO_INDEX_USED            = QUERY_NO_INDEX_USED;
    public static final int CURSOR_EXISTS            = 1 << 6;
    public static final int LAST_ROW_SENT            = 1 << 7;
    public static final int DB_DROPPED               = 1 << 8;
    public static final int NO_BACKSLASH_ESCAPES     = 1 << 9;
    public static final int METADATA_CHANGED         = 1 << 10;
    public static final int QUERY_WAS_SLOW           = 1 << 11;
    public static final int PS_OUT_PARAMS            = 1 << 12;
    public static final int IN_TRANS_READONLY        = 1 << 13;
    public static final int SESSION_STATE_CHANGED    = 1 << 14;
    //@formatter:on

    private ServerStatus() {
    }
}
