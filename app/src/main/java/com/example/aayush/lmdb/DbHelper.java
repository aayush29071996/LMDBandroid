package com.example.aayush.lmdb;

/**
 * Created by aayush on 26/6/17.
 */


        import android.content.Context;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;

class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private String CREATE_STR1;
    private String CREATE_STR2;


    DbHelper(Context inpCtx, String dbName, final String inpCreateStr1, final String inpCreateStr2) {
        super(inpCtx, dbName, null, DATABASE_VERSION);
        CREATE_STR1 = inpCreateStr1;
        CREATE_STR2 = inpCreateStr2;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STR1);
        db.execSQL(CREATE_STR2);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    /*public static int fetchData() { // pattern 2
        SQLiteDatabase db = openHelper.getWritableDatabase();
        // run some queries on the db and return result
    }*/
}