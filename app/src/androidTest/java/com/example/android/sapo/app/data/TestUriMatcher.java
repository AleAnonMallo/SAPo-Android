package com.example.android.sapo.app.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

/**
 * Created by Alejandro on 16-Oct-15.
 */
public class TestUriMatcher extends AndroidTestCase {
    private static final String LOCATION_QUERY = "London, UK";
    private static final long TEST_DATE = 1419033600L;  // December 20th, 2014
    private static final long TEST_LOCATION_ID = 10L;

    // content://com.example.android.sapo.app/almacen"
    private static final Uri TEST_ALMACEN_DIR = SAPoContract.AlmacenEntry.CONTENT_URI;
    /*private static final Uri TEST_WEATHER_WITH_LOCATION_DIR = WeatherContract.WeatherEntry.buildWeatherLocation(LOCATION_QUERY);
    private static final Uri TEST_WEATHER_WITH_LOCATION_AND_DATE_DIR = WeatherContract.WeatherEntry.buildWeatherLocationWithDate(LOCATION_QUERY, TEST_DATE);*/
    // content://com.example.android.sapo.app/categorias"
    private static final Uri TEST_CATEGORIA_DIR = SAPoContract.CategoriaEntry.CONTENT_URI;

    /*
        Students: This function tests that your UriMatcher returns the correct integer value
        for each of the Uri types that our ContentProvider can handle.  Uncomment this when you are
        ready to test your UriMatcher.
     */
    public void testUriMatcher() {
        UriMatcher testMatcher = SAPoProvider.buildUriMatcher();

        assertEquals("Error: The ALMACEN URI was matched incorrectly.",
                testMatcher.match(TEST_ALMACEN_DIR), SAPoProvider.ALMACEN);
        /*assertEquals("Error: The WEATHER WITH LOCATION URI was matched incorrectly.",
                testMatcher.match(TEST_WEATHER_WITH_LOCATION_DIR), WeatherProvider.WEATHER_WITH_LOCATION);
        assertEquals("Error: The WEATHER WITH LOCATION AND DATE URI was matched incorrectly.",
                testMatcher.match(TEST_WEATHER_WITH_LOCATION_AND_DATE_DIR), WeatherProvider.WEATHER_WITH_LOCATION_AND_DATE);*/
        assertEquals("Error: The CATEGORIA URI was matched incorrectly.",
                testMatcher.match(TEST_CATEGORIA_DIR), SAPoProvider.CATEGORIA);
    }
}