package com.asistencia.digital.monitor.geoutils;

import android.content.Context;

public class GeoContext {

    public static Context appContext;

    public static String getString(int id) {
        return GeoContext.appContext.getResources().getString(id);
    }
}
