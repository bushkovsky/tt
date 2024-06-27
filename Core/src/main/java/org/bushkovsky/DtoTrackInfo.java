package org.bushkovsky;

import java.util.ArrayList;

public class DtoTrackInfo {
    private final double totalDistance;
    private final double totalTime;
    private final double totalElevationGain;
    private final long heartRate;
    private final long countMesg;
    private final long firstLatitude;
    private final long firstLongitude;

    private final long lastLatitude;
    private final long lastLongitude;
    ArrayList<TrackPoint> trackPoints = new ArrayList<>();

    public DtoTrackInfo(double totalDistance, double totalTime, double totalElevationGain, long heartRate, long countMesg, long firstLatitude, long firstLongitude, long lastLatitude, long lastLongitude, ArrayList<TrackPoint> trackPoints) {
        this.totalDistance = totalDistance;
        this.totalTime = totalTime;
        this.totalElevationGain = totalElevationGain;
        this.heartRate = heartRate;
        this.countMesg = countMesg;
        this.firstLatitude = firstLatitude;
        this.firstLongitude = firstLongitude;
        this.lastLatitude = lastLatitude;
        this.lastLongitude = lastLongitude;
        this.trackPoints = trackPoints;
    }
}
