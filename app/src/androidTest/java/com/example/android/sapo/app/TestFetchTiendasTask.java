package com.example.android.sapo.app;

import android.annotation.TargetApi;
import android.database.Cursor;
import android.test.AndroidTestCase;

import com.example.android.sapo.app.data.SAPoContract;
import com.example.android.sapo.app.webservices.FetchAlmacenTask;

/**
 * Created by Alejandro on 15-Oct-15.
 */
public class TestFetchTiendasTask extends AndroidTestCase {
    static final String ADD_ALMACEN_ID = "13";
    static final String ADD_ALMACEN_NOMBRE = "NACIONAL";
    static final String ADD_ALMACEN_DESCRIPCION = "DESCR";
    static final String ADD_ALMACEN_URL = "primeronacional.org";

    @TargetApi(11)
    public void testAddLocation() {
        // start from a clean state
        getContext().getContentResolver().delete(SAPoContract.AlmacenEntry.CONTENT_URI,
                SAPoContract.AlmacenEntry.COLUMN_ALMACEN_ID + " = ?",
                new String[]{ADD_ALMACEN_ID});

        FetchAlmacenTask fwt = new FetchAlmacenTask(getContext(), null);
        long locationId = fwt.addAlmacen(ADD_ALMACEN_ID, ADD_ALMACEN_NOMBRE,
                ADD_ALMACEN_DESCRIPCION, ADD_ALMACEN_URL);

        // does addLocation return a valid record ID?
        assertFalse("Error: addLocation returned an invalid ID on insert",
                locationId == -1);

        // test all this twice
        for ( int i = 0; i < 2; i++ ) {

            // does the ID point to our location?
            Cursor locationCursor = getContext().getContentResolver().query(
                    SAPoContract.AlmacenEntry.CONTENT_URI,
                    new String[]{
                            SAPoContract.AlmacenEntry._ID,
                            SAPoContract.AlmacenEntry.COLUMN_ALMACEN_ID,
                            SAPoContract.AlmacenEntry.COLUMN_ALMACEN_NOMBRE,
                            SAPoContract.AlmacenEntry.COLUMN_ALMACEN_DESCRIPCION,
                            SAPoContract.AlmacenEntry.COLUMN_ALMACEN_URL
                    },
                    SAPoContract.AlmacenEntry.COLUMN_ALMACEN_ID + " = ?",
                    new String[]{ADD_ALMACEN_ID},
                    null);

            // these match the indices of the projection
            if (locationCursor.moveToFirst()) {
                assertEquals("Error: the queried value of locationId does not match the returned value" +
                        "from addLocation", locationCursor.getLong(0), locationId);
                assertEquals("Error: the queried value of location setting is incorrect",
                        locationCursor.getString(1), ADD_ALMACEN_ID);
                assertEquals("Error: the queried value of location city is incorrect",
                        locationCursor.getString(2), ADD_ALMACEN_NOMBRE);
                assertEquals("Error: the queried value of latitude is incorrect",
                        locationCursor.getString(3), ADD_ALMACEN_DESCRIPCION);
                assertEquals("Error: the queried value of longitude is incorrect",
                        locationCursor.getString(4), ADD_ALMACEN_URL);
            } else {
                fail("Error: the id you used to query returned an empty cursor");
            }

            // there should be no more records
            assertFalse("Error: there should be only one record returned from a location query",
                    locationCursor.moveToNext());

            // add the location again
            long newLocationId = fwt.addAlmacen(ADD_ALMACEN_ID, ADD_ALMACEN_NOMBRE,
                    ADD_ALMACEN_DESCRIPCION, ADD_ALMACEN_URL);

            assertEquals("Error: inserting a location again should return the same ID",
                    locationId, newLocationId);
        }
        // reset our state back to normal
        getContext().getContentResolver().delete(SAPoContract.AlmacenEntry.CONTENT_URI,
                SAPoContract.AlmacenEntry.COLUMN_ALMACEN_ID + " = ?",
                new String[]{ADD_ALMACEN_ID});

        // clean up the test so that other tests can use the content provider
        getContext().getContentResolver().
                acquireContentProviderClient(SAPoContract.AlmacenEntry.CONTENT_URI).
                getLocalContentProvider().shutdown();

      }
}
