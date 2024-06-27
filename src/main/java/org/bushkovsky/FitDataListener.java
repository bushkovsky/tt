package org.bushkovsky;

import com.garmin.fit.RecordMesg;
import com.garmin.fit.RecordMesgListener;

import java.util.ArrayList;

public class FitDataListener implements RecordMesgListener {

    private double tmpTime = 0.0;
    private double totalDistance = 0.0;
    private double lastDistance = 0.0;
    private double totalTime = 0.0;
    private double totalElevationGain = 0.0;
    private double lastAltitude = 0.0;
    private boolean isFirstRecord = true;

    private long heartRate = 0;
    private long countMesg = 0;

    private long firstLatitude = 0;
    private long firstLongitude = 0;

    private long lastLatitude = 0;
    private long lastLongitude = 0;

    ArrayList<TrackPoint> trackPoints = new ArrayList<>();

    @Override
    public void onMesg(RecordMesg mesg) {
        countMesg++;

        // Обновление местоположения (широты и долготы)
        if (isFirstRecord && mesg.getPositionLat() != null && mesg.getPositionLong() != null) {
            Long firstLatitude = (long) (mesg.getPositionLat() * (180.0 / Math.pow(2, 31)));
            Long firstLongitude = (long) (mesg.getPositionLong() * (180.0 / Math.pow(2, 31)));
        }
        if (mesg.getPositionLat() != null && mesg.getPositionLong() != null) {
            Long lastLatitude = (long) (mesg.getPositionLat() * (180.0 / Math.pow(2, 31)));
            Long lastLongitude = (long) (mesg.getPositionLong() * (180.0 / Math.pow(2, 31)));
        }

        if(mesg.getHeartRate() != null) {
            heartRate += mesg.getHeartRate();
        }


        if (mesg.getDistance() != null) {
            lastDistance = totalDistance;
            totalDistance = mesg.getDistance();
        }
        if (mesg.getTimestamp() != null) {
            System.out.println('f');
            if (isFirstRecord) {
                isFirstRecord = false;
                tmpTime = mesg.getTimestamp().getTimestamp();
            } else {
                totalTime += mesg.getTimestamp().getTimestamp() - tmpTime;
                tmpTime = mesg.getTimestamp().getTimestamp();
            }
        }
        if (mesg.getAltitude() != null) {
            if (!isFirstRecord) {
                double altitude = mesg.getAltitude();
                if (altitude > lastAltitude) {
                    totalElevationGain += (altitude - lastAltitude);
                }
                lastAltitude = altitude;
            }
        }

        double grade = 0.0;
        if (mesg.getAltitude() != null) {
            double currentAltitude = mesg.getAltitude();

            // Вычисление изменения высоты и расстояния
            double deltaAltitude = currentAltitude - lastAltitude;
            double deltaDistance = totalDistance - lastDistance; // или totalDistance между текущим и предыдущим RecordMesg


            if (!isFirstRecord) {
                // grade теперь содержит угол подъема или спуска в процентах
                grade = (deltaAltitude / deltaDistance) * 100.0;
            }
        }

        TrackPoint trackPoint = new TrackPoint(getTotalTime(), mesg.getDistance(), mesg.getSpeed(), mesg.getAltitude(), mesg.getHeartRate(), grade);
        trackPoints.add(trackPoint);
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public double getTotalTime() {
        return totalTime;
    }

    public double getTotalElevationGain() {
        return totalElevationGain;
    }

    public long getHeartRate() {
        if(countMesg == 0) {
            return 0;
        }
        return heartRate / countMesg;
    }

    public double getAverageSpeed() {
        return totalDistance / totalTime;
    }


    public long getFirstLatitude() {
        return firstLatitude;
    }

    public long getFirstLongitude() {
        return firstLongitude;
    }

    public long getLastLatitude() {
        return lastLatitude;
    }

    public long getLastLongitude() {
        return lastLongitude;
    }

    public DtoTrackInfo getTrackInfo() {
        return new DtoTrackInfo(totalDistance, totalTime, totalElevationGain, heartRate, countMesg, firstLatitude, firstLongitude, lastLatitude, lastLongitude, trackPoints);
    }
}
