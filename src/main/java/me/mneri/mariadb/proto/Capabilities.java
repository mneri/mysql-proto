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

public final class Capabilities {
    //@formatter:off
    public static final long CLIENT_MYSQL                        = 1;
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
