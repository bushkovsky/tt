package org.bushkovsky;

public class TrackPoint {

    public TrackPoint(double time, double distance, double speed, double elevationGain, long heartRate, double grade) {
        this.time = time;
        this.distance = distance;
        this.speed = speed;
        this.elevationGain = elevationGain;
        this.heartRate = heartRate;
        this.grade = grade;
    }

    private final double distance;
    private final double time;
    private final double speed;
    private final double elevationGain;
    private final long heartRate;
    private final double grade;

    public double getDistance() {
        return distance;
    }

    public double getTime() {
        return time;
    }

    public double getSpeed() {
        return speed;
    }

    public double getElevationGain() {
        return elevationGain;
    }

    public long getHeartRate() {
        return heartRate;
    }

    public double getGrade() {
        return grade;
    }
}
