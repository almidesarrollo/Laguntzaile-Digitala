package com.asistencia.digital.monitor.geoutils;

import android.util.Log;

import com.asistencia.digital.monitor.R;

public class GeoUtils {

    public static GeoItem getCurrentLocation() {
        try {
            final String path = GeoContext.getString(R.string.const_path_logs);
            final String ext = GeoContext.getString(R.string.const_path_file_ext);
            String[] files = ShellUtils.execShell("ls "+path).split("\n");
            String last = null;
            for (int i=0;i<files.length;i++)
                if (files[i].endsWith(ext))
                    last = files[i];
            if (last != null) {
                String[] lines = ShellUtils.execShell("cat " + (path + "/" + last)).split("\n");
                String current = null;
                for (int i=lines.length-1;i>=0;i--) {
                    if (lines[i].contains("lat=\"") && lines[i].contains("lon=\"")) {
                        current = lines[i];
                        break;
                    }
                }
                if (current != null) {
                    String lat = re.search("(?<=lat=\")[^\"]*(?=\")", current);
                    String lng = re.search("(?<=lon=\")[^\"]*(?=\")", current);
                    return new GeoItem(lat, lng);
                }
            }
        } catch (Exception e) {
            Log.e("getCurrentLocation", e.getMessage());
            return null;
        }

        return null;
    }

}
