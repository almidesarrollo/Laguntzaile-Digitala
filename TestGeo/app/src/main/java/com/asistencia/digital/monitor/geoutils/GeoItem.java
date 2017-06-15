package com.asistencia.digital.monitor.geoutils;

public class GeoItem {

    public GeoItem(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public GeoItem(double latitude, double longitude) {
        this.latitude = String.valueOf(latitude);
        this.longitude = String.valueOf(longitude);
    }

    public String latitude;
    public String longitude;

    public boolean equals(GeoItem item) {
        return (item != null) && (item.latitude+item.longitude).equals(this.latitude+this.longitude);
    }

    @Override
    public String toString() {
        return this.latitude+", "+this.longitude;
    }
}
