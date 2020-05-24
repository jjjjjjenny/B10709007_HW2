package com.example.b10709007_hw2.data;

import android.provider.BaseColumns;

public class WaitlistContract {//放置我的資料庫的設定

    public static final class WaitlistEntry implements BaseColumns {
        public static final String TABLE_NAME = "waitlist";
        public static final String COLUMN_GUEST_NAME = "guestName";
        public static final String COLUMN_PARTY_SIZE = "partySize";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }

}

