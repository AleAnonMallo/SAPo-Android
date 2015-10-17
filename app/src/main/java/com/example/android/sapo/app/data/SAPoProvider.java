package com.example.android.sapo.app.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by Alejandro on 15-Oct-15.
 */
public class SAPoProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private SAPoDBHelper mOpenHelper;

    static final int ALMACEN = 100;
    static final int CATEGORIA = 300;

    /*
        private static final SQLiteQueryBuilder sWeatherByLocationSettingQueryBuilder;

        static{
            sWeatherByLocationSettingQueryBuilder = new SQLiteQueryBuilder();

            //This is an inner join which looks like
            //weather INNER JOIN location ON weather.location_id = location._id
            sWeatherByLocationSettingQueryBuilder.setTables(
                    SAPoContract.AlmacenEntry.TABLE_NAME + " INNER JOIN " +
                            WeatherContract.LocationEntry.TABLE_NAME +
                            " ON " + WeatherContract.WeatherEntry.TABLE_NAME +
                            "." + WeatherContract.WeatherEntry.COLUMN_LOC_KEY +
                            " = " + WeatherContract.LocationEntry.TABLE_NAME +
                            "." + WeatherContract.LocationEntry._ID);
        }

        //location.location_setting = ?
        private static final String sLocationSettingSelection =
                WeatherContract.LocationEntry.TABLE_NAME+
                        "." + WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING + " = ? ";

        //location.location_setting = ? AND date >= ?
        private static final String sLocationSettingWithStartDateSelection =
                WeatherContract.LocationEntry.TABLE_NAME+
                        "." + WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING + " = ? AND " +
                        WeatherContract.WeatherEntry.COLUMN_DATE + " >= ? ";

        //location.location_setting = ? AND date = ?
        private static final String sLocationSettingAndDaySelection =
                WeatherContract.LocationEntry.TABLE_NAME +
                        "." + WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING + " = ? AND " +
                        WeatherContract.WeatherEntry.COLUMN_DATE + " = ? ";

        private Cursor getWeatherByLocationSetting(Uri uri, String[] projection, String sortOrder) {
            String locationSetting = WeatherContract.WeatherEntry.getLocationSettingFromUri(uri);
            long startDate = WeatherContract.WeatherEntry.getStartDateFromUri(uri);

            String[] selectionArgs;
            String selection;

            if (startDate == 0) {
                selection = sLocationSettingSelection;
                selectionArgs = new String[]{locationSetting};
            } else {
                selectionArgs = new String[]{locationSetting, Long.toString(startDate)};
                selection = sLocationSettingWithStartDateSelection;
            }

            return sWeatherByLocationSettingQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
            );
        }

        private Cursor getWeatherByLocationSettingAndDate(
                Uri uri, String[] projection, String sortOrder) {
            String locationSetting = WeatherContract.WeatherEntry.getLocationSettingFromUri(uri);
            long date = WeatherContract.WeatherEntry.getDateFromUri(uri);

            return sWeatherByLocationSettingQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                    projection,
                    sLocationSettingAndDaySelection,
                    new String[]{locationSetting, Long.toString(date)},
                    null,
                    null,
                    sortOrder
            );
        }
    */

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = SAPoContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, SAPoContract.PATH_ALMACEN, ALMACEN);
        matcher.addURI(authority, SAPoContract.PATH_CATEGORIA, CATEGORIA);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new SAPoDBHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case ALMACEN:
                return SAPoContract.AlmacenEntry.CONTENT_TYPE;
           // case CATEGORIA:
             //   return SAPoContract.CategoriaEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case ALMACEN: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        SAPoContract.AlmacenEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case CATEGORIA: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        SAPoContract.CategoriaEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case ALMACEN: {
                long _id = db.insert(SAPoContract.AlmacenEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = SAPoContract.AlmacenEntry.buildAlmacenUri(_id);
                    //returnUri = null;
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        db.close();
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match) {
            case ALMACEN:
                rowsDeleted = db.delete(
                        SAPoContract.AlmacenEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case CATEGORIA:
                rowsDeleted = db.delete(
                        SAPoContract.CategoriaEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case ALMACEN:
                rowsUpdated = db.update(SAPoContract.AlmacenEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case CATEGORIA:
                rowsUpdated = db.update(SAPoContract.CategoriaEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ALMACEN:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        //normalizeDate(value);
                        long _id = db.insert(SAPoContract.AlmacenEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    // You do not need to call this method. This is a method specifically to assist the testing
    // framework in running smoothly. You can read more at:
    // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}