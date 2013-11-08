package net.music_search;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbOpenHelper extends SQLiteOpenHelper{

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "musicDB";

    public static final String TABLE_NAME = "songs";
    private static final String CREATE_TABLE = "create table songs ( _id integer primary key autoincrement, place ,  TEXT,  TEXT)";

        public DbOpenHelper(Context context) {
            super(context, DB_NAME, null,DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
             sqLiteDatabase.execSQL(getQuery(0));
            sqLiteDatabase.execSQL(getQuery(1));
            sqLiteDatabase.execSQL(getQuery(2));
            sqLiteDatabase.execSQL(getQueryAuthor(0));
            sqLiteDatabase.execSQL(getQueryAuthor(1));
            sqLiteDatabase.execSQL(getQueryAuthor(2));
        }

    @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        }
    public String getQuery(int id){
        return "create table songs_" + Integer.toString(id)+ " ( s_id integer primary key autoincrement, s_name TEXT, s_author INTEGER, s_text TEXT)";
    }
    public String getQueryAuthor(int id){
        return "create table authors_" + Integer.toString(id)+ " ( a_id integer primary key autoincrement, a_name TEXT)";
    }
}

