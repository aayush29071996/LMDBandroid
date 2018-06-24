package com.example.aayush.lmdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aayush on 23/6/17.
 */


public class sqlite  {

    private static final String DATABASE_NAME = "LMdb";
    private static final int DATABASE_VERSION = 1;

    public static final String EVENTS_TABLE = "Events";
    public static final String TRIPS_TABLE = "Trips";


    //columns
    public static final String ID_COLUMN_EVENTS = "id";
    public static final String ID_COLUMN_TRIPS = "id";


    public static final String NAME_EVENTS = "events";
    public static final String NAME_TRIPS = "trips";


    public static final String FLAG_EVENTS ="flag" ;
    public static final String FLAG_TRIPS ="flag" ;

    public static final String TIMESTAMP_EVENTS ="timestamp" ;
    public static final String TIMESTAMP_TRIPS ="timestamp" ;



    private Context mContext;
    private static DbHelper dbHelperInst = null;
    private SQLiteDatabase database;


    public static final String CREATE_EVENTS_TABLE = "CREATE TABLE "
            + EVENTS_TABLE + "(" + ID_COLUMN_EVENTS + " INTEGER PRIMARY KEY," + NAME_EVENTS + " TEXT," + FLAG_EVENTS + " INTEGER," + TIMESTAMP_EVENTS + " TEXT)";

    public static final String CREATE_TRIPS_TABLE = "CREATE TABLE "
            + TRIPS_TABLE + "(" + ID_COLUMN_TRIPS + " INTEGER PRIMARY KEY," + NAME_TRIPS + " TEXT," + FLAG_TRIPS + " INTEGER," + TIMESTAMP_TRIPS + " TEXT)";


    private static final String WHERE_ID_EQUALS_EVENTS = ID_COLUMN_EVENTS
                + " =?";
    private static final String WHERE_TIME_EQUALS_EVENTS = TIMESTAMP_EVENTS
            + " =?";
    private static final String WHERE_TIME_EQUALS_TRIPS = TIMESTAMP_TRIPS
            + " =?";
    private static final String WHERE_ID_EQUALS_TRIPS = ID_COLUMN_TRIPS
            + " =?";
    private static final String TAG = "ERROR";

    sqlite(Context context, String direc) {
        //super(context);
        this.mContext = context;
        dbHelperInst = new DbHelper(mContext, direc + File.separator + DATABASE_NAME, CREATE_EVENTS_TABLE, CREATE_TRIPS_TABLE);
        database = dbHelperInst.getWritableDatabase();
    }


    public long save1(String data, String timestamp) {

            ContentValues values = new ContentValues();
            values.put(NAME_EVENTS, data);
            values.put(FLAG_EVENTS, 0);
            values.put(TIMESTAMP_EVENTS, timestamp);


        Log.e(TAG,"EVENTS ADDED SUCCESSFULLY");

        return      database
                    .insert(EVENTS_TABLE, null,  values);
        }

    public void save2(String data) {

        ContentValues values = new ContentValues();
        values.put(FLAG_EVENTS, 1);
        long result = database.update(EVENTS_TABLE,
                values, WHERE_ID_EQUALS_EVENTS,new String[]{data});


        Log.e(TAG,"EVENTS FLAG SETTED SUCCESSFULLY");


    }
    public long save3(String data, String timestamp) {

        ContentValues values = new ContentValues();
        values.put(NAME_TRIPS, data);
        values.put(FLAG_TRIPS, 0);
        values.put(TIMESTAMP_TRIPS, timestamp);


        Log.e(TAG,"TRIPS ADDED SUCCESSFULLY");

        return      database
                .insert(TRIPS_TABLE, null,  values);
    }

    public void save4(String data) {

        ContentValues values = new ContentValues();
        values.put(FLAG_TRIPS, 1);
        long result = database.update(TRIPS_TABLE,
                values, WHERE_ID_EQUALS_TRIPS,new String[]{data});


        Log.e(TAG,"TRIPS FLAG SETTED SUCCESSFULLY");


    }

    public ArrayList<String> getEvents() {
        ArrayList<String> event_list = new ArrayList<String>();
        Cursor cursor = database.query(EVENTS_TABLE,
                new String[]{ID_COLUMN_EVENTS, NAME_EVENTS,
                        FLAG_EVENTS,TIMESTAMP_EVENTS}, null, null, null,
                null, null,null);

        if (cursor.moveToFirst()) {
            do {

                if (cursor.getInt(2) == 0) {
                    //only one column
                    String id = cursor.getString(0);
                    event_list.add(id);

                    String event = cursor.getString(1);
                    event_list.add(event);

                    String timestamp = cursor.getString(3);
                    event_list.add(timestamp);

                    //you could add additional columns here..
                }
            } while (cursor.moveToNext());
        }
        return event_list;

    }


    public ArrayList<String> getTrips() {
        ArrayList<String> trip_list = new ArrayList<String>();
        Cursor cursor = database.query(TRIPS_TABLE,
                new String[]{ID_COLUMN_TRIPS, NAME_TRIPS,
                        FLAG_TRIPS,TIMESTAMP_TRIPS}, null, null, null,
                null, null,null);

        if (cursor.moveToFirst()) {
            do {

                if (cursor.getInt(2) == 0) {
                    //only one column
                    String id = cursor.getString(0);
                    trip_list.add(id);

                    String event = cursor.getString(1);
                    trip_list.add(event);

                    String timestamp = cursor.getString(3);
                    trip_list.add(timestamp);

                    //you could add additional columns here..
                }
            } while (cursor.moveToNext());
        }
        return trip_list;

    }

    public  ArrayList<String> getEventsTimestamp(){

        ArrayList<String> timestamp_list = new ArrayList<String>();

        Cursor cursor = database.query(EVENTS_TABLE,
                new String[]{ID_COLUMN_EVENTS, NAME_EVENTS,
                        FLAG_EVENTS,TIMESTAMP_EVENTS}, null, null, null,
                null, null,null);

        if (cursor.moveToFirst()) {
            do {

                if (cursor.getInt(2) == 1) {



                    String timestamp = cursor.getString(3);
                    timestamp_list.add(timestamp);

                    //you could add additional columns here..
                }
            } while (cursor.moveToNext());
        }

        return timestamp_list;


    }


    public  ArrayList<String> getTripsTimestamp(){

        ArrayList<String> timestamp_list = new ArrayList<String>();

        Cursor cursor = database.query(TRIPS_TABLE,
                new String[]{ID_COLUMN_TRIPS, NAME_TRIPS,
                        FLAG_TRIPS,TIMESTAMP_TRIPS}, null, null, null,
                null, null,null);

        if (cursor.moveToFirst()) {
            do {

                if (cursor.getInt(2) == 1) {



                    String timestamp = cursor.getString(3);
                    timestamp_list.add(timestamp);

                    //you could add additional columns here..
                }
            } while (cursor.moveToNext());
        }

        return timestamp_list;


    }

    public int delete1(String timestamp) {
        return database.delete(EVENTS_TABLE,
                WHERE_TIME_EQUALS_EVENTS, new String[]{timestamp + ""});
    }


    public int delete2(String timestamp) {
        return database.delete(TRIPS_TABLE,
                WHERE_TIME_EQUALS_TRIPS, new String[]{timestamp + ""});
    }


    /*    public long update(PLang plang_data) {
            ContentValues values = new ContentValues();
            values.put(DataBaseHelper.POI_ID, plang_data.getPoi_id());
            values.put(DataBaseHelper.POILANGS_COLUMN, plang_data.getLangarr());

            long result = database.update(DataBaseHelper.POILANGS_TABLE,
                    values, WHERE_ID_EQUALS,
                    new String[]{String.valueOf(plang_data.getId())});
            Log.d("Update Result:", "=" + result);
            return result;

        }

        public int deleteDept(PLang plang_data) {
            return database.delete(DataBaseHelper.POILANGS_TABLE,
                    WHERE_ID_EQUALS, new String[]{plang_data.getId() + ""});
        }

        public List<PLang> getPLangs1() {
            List<PLang> plang_list = new ArrayList<PLang>();
            Cursor cursor = database.query(DataBaseHelper.POILANGS_TABLE,
                    new String[]{DataBaseHelper.ID_COLUMN, DataBaseHelper.POI_ID,
                            DataBaseHelper.POILANGS_COLUMN}, null, null, null,
                    null, null);

            while (cursor.moveToNext()) {
                PLang plang_bin = new PLang();
                plang_bin.setId(cursor.getInt(0));
                plang_bin.setPoi_id(cursor.getString(1));
                plang_bin.setLangarr(cursor.getString(2));
                plang_list.add(plang_bin);
            }
            return plang_list;
        }
*/




    }

