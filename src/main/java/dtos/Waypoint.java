package dtos;

public class Waypoint {
    private float lng;
    private float lat;

    public Waypoint(float lng, float lat) {
        this.lng = lng;
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public float getLat() {
        return lat;
    }
}
