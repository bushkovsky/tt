package org.bushkovsky;

import java.util.Arrays;

public class TrackRecommendation {

    private static double normalize(double value, double min, double max) {
        return (value - min) / (max - min);
    }

    public static double calculateRecommendation(
            double distance, double elevationGain, double time, double avgSpeed, double avgHeartRate,
            double minDistance, double maxDistance,
            double minElevationGain, double maxElevationGain,
            double minTime, double maxTime,
            double minAvgSpeed, double maxAvgSpeed,
            double minAvgHeartRate, double maxAvgHeartRate,
            double[] weights
    ) {
        double normDistance = normalize(distance, minDistance, maxDistance);
        double normElevationGain = normalize(elevationGain, minElevationGain, maxElevationGain);
        double normTime = normalize(time, minTime, maxTime);
        double normAvgSpeed = normalize(avgSpeed, minAvgSpeed, maxAvgSpeed);
        double normAvgHeartRate = normalize(avgHeartRate, minAvgHeartRate, maxAvgHeartRate);

        double recommendation =
                weights[0] * normDistance +
                        weights[1] * normElevationGain +
                        weights[2] * normTime +
                        weights[3] * normAvgSpeed +
                        weights[4] * normAvgHeartRate;

        return recommendation * 10; // переводим в шкалу от 0 до 10
    }

    public static double[] trainWeights(double[][] features, double[] labels) {
        int numFeatures = features[0].length;
        double[] weights = new double[numFeatures];
        double learningRate = 0.01;
        int numIterations = 1000;

        for (int iter = 0; iter < numIterations; iter++) {
            double[] gradients = new double[numFeatures];
            for (int i = 0; i < features.length; i++) {
                double predicted = 0.0;
                for (int j = 0; j < numFeatures; j++) {
                    predicted += weights[j] * features[i][j];
                }
                double error = predicted - labels[i];
                for (int j = 0; j < numFeatures; j++) {
                    gradients[j] += error * features[i][j];
                }
            }
            for (int j = 0; j < numFeatures; j++) {
                weights[j] -= learningRate * gradients[j] / features.length;
            }
        }
        return weights;
    }

    public static void main(String[] args) {
        // Пример данных маршрутов и отзывов
        double[][] routes = {
                {10.0, 500.0, 120.0, 5.0, 150.0}, //расстояние, набор высоты, время, средняя ск, ср пульс
                {15.0, 800.0, 180.0, 4.0, 160.0},
                {5.0, 200.0, 60.0, 6.0, 140.0}
        };
        double[] reviews = {7.0, 8.5, 3.5}; // соответствующие отзывы на маршруты (можем использовать среднее от сложности и усталости или общую оценку)

        // Диапазоны для нормализации
        double minDistance = 1.0;
        double maxDistance = 20.0;
        double minElevationGain = 0.0;
        double maxElevationGain = 1000.0;
        double minTime = 30.0;
        double maxTime = 300.0;
        double minAvgSpeed = 3.0;
        double maxAvgSpeed = 10.0;
        double minAvgHeartRate = 60.0;
        double maxAvgHeartRate = 180.0;

        // Нормализация данных маршрутов
        double[][] normalizedRoutes = new double[routes.length][5];
        for (int i = 0; i < routes.length; i++) {
            normalizedRoutes[i][0] = normalize(routes[i][0], minDistance, maxDistance);
            normalizedRoutes[i][1] = normalize(routes[i][1], minElevationGain, maxElevationGain);
            normalizedRoutes[i][2] = normalize(routes[i][2], minTime, maxTime);
            normalizedRoutes[i][3] = normalize(routes[i][3], minAvgSpeed, maxAvgSpeed);
            normalizedRoutes[i][4] = normalize(routes[i][4], minAvgHeartRate, maxAvgHeartRate);
        }

        // Обучение весов
        double[] weights = trainWeights(normalizedRoutes, reviews);
        System.out.println("Trained Weights: " + Arrays.toString(weights));

        // Новый маршрут для рекомендации
        double distance = 12.0;
        double elevationGain = 600.0;
        double time = 150.0;
        double avgSpeed = 5.5;
        double avgHeartRate = 155.0;

        double recommendation = calculateRecommendation(
                distance, elevationGain, time, avgSpeed, avgHeartRate,
                minDistance, maxDistance,
                minElevationGain, maxElevationGain,
                minTime, maxTime,
                minAvgSpeed, maxAvgSpeed,
                minAvgHeartRate, maxAvgHeartRate,
                weights
        );

        System.out.println("Recommendation: " + recommendation);
    }
}
