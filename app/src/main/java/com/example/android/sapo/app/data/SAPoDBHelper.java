package com.example.android.sapo.app.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Alejandro on 15-Oct-15.
 * Manages a local database for SAPo data.
 */
public class SAPoDBHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "sapo.db";

    public SAPoDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold locations.  A location consists of the string supplied in the
        // location setting, the city name, and the latitude and longitude
        final String SQL_CREATE_ALMACEN_TABLE = "CREATE TABLE " + SAPoContract.AlmacenEntry.TABLE_NAME + " (" +
                SAPoContract.AlmacenEntry._ID + " INTEGER PRIMARY KEY," +
                SAPoContract.AlmacenEntry.COLUMN_ALMACEN_ID + " TEXT NOT NULL," +
                SAPoContract.AlmacenEntry.COLUMN_ALMACEN_NOMBRE + " TEXT NOT NULL, " +
                SAPoContract.AlmacenEntry.COLUMN_ALMACEN_DESCRIPCION + " TEXT NOT NULL, " +
                SAPoContract.AlmacenEntry.COLUMN_ALMACEN_URL + " TEXT NOT NULL " +
                " );";

        final String SQL_CREATE_CATEGORIA_TABLE = "CREATE TABLE " + SAPoContract.CategoriaEntry.TABLE_NAME + " (" +
                SAPoContract.CategoriaEntry.COLUMN_CATEGORIA_ID+ " INTEGER PRIMARY KEY," +
                SAPoContract.CategoriaEntry.COLUMN_CATEGORIA_NOMBRE + " TEXT NOT NULL " +

                // Set up the location column as a foreign key to location table.
               // "FOREIGN KEY (" + SAPoContract.CategoriaEntry.COLUMN_ALMACEN_FOREIGN_KEY + ") REFERENCES " +
                //SAPoContract.AlmacenEntry.TABLE_NAME + "(" + SAPoContract.AlmacenEntry.COLUMN_ALMACEN_ID + ") " +

                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_ALMACEN_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_CATEGORIA_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SAPoContract.AlmacenEntry.TABLE_NAME);
        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SAPoContract.CategoriaEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
