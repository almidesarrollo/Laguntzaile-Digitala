package com.asistencia.digital.monitor.localdb;

import android.content.Context;
import android.content.SharedPreferences;

import com.asistencia.digital.monitor.webservice.AsistRestApiClient;

public class LocalDbManager {

    public static boolean isFirstExecution(Context c) {
        SharedPreferences preferences = c.getSharedPreferences(AsistRestApiClient.SHARED_PREFS, 1);
        return !preferences.contains("user") && !preferences.contains("password");
    }

}
