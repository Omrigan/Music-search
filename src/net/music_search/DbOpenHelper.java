package net.music_search;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbOpenHelper extends SQLiteOpenHelper{

        private static final int DB_VERSION = 1;
        private static final String DB_NAME = "test";
        public static final String TABLE_NAME = "settings";



        public DbOpenHelper(Context context) {
            super(context, DB_NAME, null,DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        }
    }

