package com.asistencia.digital.monitor.geoutils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ShellUtils {
    public static String execShell(String command) {
        try {
            Process p = Runtime.getRuntime().exec(command);
            p.waitFor();

            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = br.readLine()) != null)
                result.append(line.concat("\n"));
            br.close();

            return result.toString();
        } catch (Exception e) {
            Log.e("mytag", "ERROR: "+e.getMessage());
            return null;
        }
    }
}
