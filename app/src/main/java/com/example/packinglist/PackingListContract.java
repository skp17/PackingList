package com.example.packinglist;

import android.provider.BaseColumns;

public final class PackingListContract {

    public void PackingListContract() {} //Empty

    /* Inner class that defines the table contents */
    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "Packing_List";
        public static final String COLUMN_ID = "id";
        public static final String ITEM = "item";
        public static final String IS_CHECKED = "is_checked";
    }
}
