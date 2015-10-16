package com.example.android.sapo.app.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Alejandro on 15-Oct-15.
 */
public class SAPoContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.sapo.app";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_ALMACEN = "almacen";
    public static final String PATH_CATEGORIA = "categoria";

    // Inner class que define la tabla almacenes.
    public static final class AlmacenEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ALMACEN).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ALMACEN;

        public static final String TABLE_NAME = "almacenes";

        // ID del almacén.
        public static final String COLUMN_ALMACEN_ID = "almacen_id";
        // NOMBRE del almacén.
        public static final String COLUMN_ALMACEN_NOMBRE = "almacen_nombre";
        // DESCRIPCIÓN del almacén.
        public static final String COLUMN_ALMACEN_DESCRIPCION = "almacen_descripcion";
        // URL del almacén.
        public static final String COLUMN_ALMACEN_URL = "almacen_url";

        public static Uri buildAlmacenUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildWeatherLocation(String locationSetting) {
            return CONTENT_URI.buildUpon().appendPath(locationSetting).build();
        }

        public static String getLocationSettingFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static long getDateFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(2));
        }

    }

    // Inner class que define la tabla categorias.
    public static final class CategoriaEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CATEGORIA).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CATEGORIA;

        public static final String TABLE_NAME = "categorias";

        // ID del categoría.
        public static final String COLUMN_CATEGORIA_ID = "id";
        // NOMBRE del categoría.
        public static final String COLUMN_CATEGORIA_NOMBRE = "categoria_nombre";
        // DESCRIPCIÓN del almacén.
        public static final String COLUMN_ALMACEN_FOREIGN_KEY = "almacen_id";

    }


}
