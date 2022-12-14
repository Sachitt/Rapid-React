package frc.robot.util;

import java.util.TreeMap;
import edu.wpi.first.math.Pair;

/**
 * <br>
 * </br>
 * <i>
 * Returns a treeMap of linearly interpolated values given a set of defined
 * shooter macros
 * </i>
 * <br>
 * </br>
 * <b> WIP 
 * TODO: Add Cubic Spline Interpolation
 * @author Karan Thakkar
 */
public class InterpolatingTreeMap {
    public TreeMap<Double, Pair<Double, Double>> map = new TreeMap<Double, Pair<Double, Double>>();

    public InterpolatingTreeMap(TreeMap<Double, Pair<Double, Double>> map) {
        this.map = map;
    }

    /** @return New interpolated value between two given points */
    private static double LinearInterpolatePoint(double point, Pair<Double, Double> a, Pair<Double, Double> b) {
        return a.getSecond() + (point - a.getFirst()) * (b.getSecond() - a.getSecond()) / (b.getFirst() - a.getFirst());
    }

    /** @return A rounded double to specifed precision */
    private static double round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    /**
     * Fills treeMap between each specified Macro
     * 
     * @param precision specifices change in distance between each interpolated
     *                  point
     */
    public void interpolate(double precision) {
        Object[] distanceMacros = map.keySet().toArray();
        int r = map.keySet().size();
        int j = r - 1;

        for (int x = 0; x < (j); x++) {
            Pair<Double, Double> a = map.get(distanceMacros[x]);
            Pair<Double, Double> b = map.get(distanceMacros[x + 1]);
            for (double i = (double) distanceMacros[x]; i <= (double) distanceMacros[x + 1]; i += ((precision) / 10)) {
                double RPM = LinearInterpolatePoint(i,
                        new Pair<Double, Double>(toDouble(distanceMacros[x]), a.getFirst()),
                        new Pair<Double, Double>(toDouble(distanceMacros[x + 1]), b.getFirst()));
                double hoodAngle = LinearInterpolatePoint(i,
                        new Pair<Double, Double>(toDouble(distanceMacros[x]), a.getSecond()),
                        new Pair<Double, Double>(toDouble(distanceMacros[x + 1]), b.getSecond()));
                Pair<Double, Double> newPoint = new Pair<Double, Double>(round(RPM, 3), round(hoodAngle, 3));
                map.put(round(i, 1), newPoint);
            }

        }
    }

    /**
     * 
     * @param distance
     * @return RPM at given distance
     */
    public double getRPM(double distance) {
        return map.get(distance).getFirst();
    }

    /**
     * 
     * @param distance
     * @return Hood Angle at given distance
     */
    public double getAngle(double distance) {
        return map.get(distance).getSecond();
    }

    /* */
    private Double toDouble(Object a) {
        return (Double) a;
    }

}
