package cs437.som.network;

import cs437.som.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class CustomSOMFileReader extends SOMFileReader {
    private static final Pattern distanceMetricRegEx = Pattern.compile(
            "distance\\s*(?:metric)?\\s*:\\s*(\\w*)", Pattern.CASE_INSENSITIVE);
    private static final Pattern learningRateRegEx = Pattern.compile(
            "learning\\s*(?:rate)?\\s*(?:function)?\\s*:\\s*(\\w*)\\s*(.*)",
            Pattern.CASE_INSENSITIVE);
    private static final Pattern neighborhoodRegEx = Pattern.compile(
            "neighborhood\\s*(?:width)?\\s*(?:function)?\\s*:\\s*(\\w*)\\s*(.*)",
            Pattern.CASE_INSENSITIVE);
    private static final Pattern gridTypeRegEx = Pattern.compile(
            "(?:grid)?\\s*type\\s*:\\s*(\\w*)", Pattern.CASE_INSENSITIVE);
    private static final Pattern iterationsRegEx = Pattern.compile(
            "iterations\\s*:\\s*(\\d*)\\s*of\\s*(\\d*)",
            Pattern.CASE_INSENSITIVE);

    private int time = 0;
    private DistanceMetric distanceMetric = null;
    private LearningRateFunction learningRate = null;
    private NeighborhoodWidthFunction neighborhoodWidth = null;
    private GridType gridType = null;

    @Override
    protected void unmatchedLine(String line) {
        if (!matchIterations(line)
                && !matchDistanceMetric(line)
                && !matchGridType(line)
                && !matchLearningRate(line)) {
            matchNeighborhood(line);
        }
    }

    @Override
    protected boolean matchIterations(String line) {
        Matcher iterationsMatch = iterationsRegEx.matcher(line);
        if (iterationsMatch.matches()) {
            time = Integer.parseInt(iterationsMatch.group(1));
            iterations = Integer.parseInt(iterationsMatch.group(2));
            return true;
        }
        return false;
    }

    private boolean matchDistanceMetric(String line) {
        Matcher distanceMatch = distanceMetricRegEx.matcher(line);
        if (distanceMatch.matches()) {
            distanceMetric = (DistanceMetric)
                    instantiateClass("cs437.som.distancemetrics",
                            distanceMatch.group(1));
            return true;
        }
        return false;
    }

    private boolean matchLearningRate(String line) {
        Matcher learningRateMatch = learningRateRegEx.matcher(line);
        if (learningRateMatch.matches()) {
            learningRate = (LearningRateFunction)
                    instantiateFromString("cs437.som.learningrate",
                            learningRateMatch.group(1),
                            learningRateMatch.group(2));
            return true;
        }
        return false;
    }

    private boolean matchNeighborhood(String line) {
        Matcher neighborhoodMatch = neighborhoodRegEx.matcher(line);
        if (neighborhoodMatch.matches()) {
            neighborhoodWidth = (NeighborhoodWidthFunction)
                    instantiateFromString("cs437.som.neighborhood",
                            neighborhoodMatch.group(1),
                            neighborhoodMatch.group(2));
            return true;
        }
        return false;
    }

    private boolean matchGridType(String line) {
        Matcher gridTypeMatch = gridTypeRegEx.matcher(line);
        if (gridTypeMatch.matches()) {
            gridType = (GridType)
                    instantiateClass("cs437.som.topology",
                            gridTypeMatch.group(1));
            return true;
        }
        return false;
    }

    private Object instantiateClass(String pkg, String cls) {
        String className = pkg + '.' + cls;
        Object object;
        try {
            Class<?> clsObj = Class.forName(className);
            object = clsObj.newInstance();
        } catch (ClassNotFoundException e) {
            throw new SOMError("Cannot find " + className);
        } catch (InstantiationException e) {
            throw new SOMError("Cannot create " + className);
        } catch (IllegalAccessException e) {
            throw new SOMError("Cannot create " + className);
        }
        return object;
    }

    private Object instantiateFromString(String pkg, String cls, String args) {
        String className = pkg + '.' + cls;
        Object object;
        try {
            Class<?> clsObj = Class.forName(className);
            Constructor<?> ctor = clsObj.getConstructor(String.class);
            object = ctor.newInstance(args);
        } catch (ClassNotFoundException e) {
            throw new SOMError("Cannot find " + className);
        } catch (InstantiationException e) {
            throw new SOMError("Cannot create " + className);
        } catch (IllegalAccessException e) {
            throw new SOMError("Cannot create " + className);
        } catch (NoSuchMethodException e) {
            throw new SOMError("Cannot create " + className);
        } catch (InvocationTargetException e) {
            throw new SOMError("Cannot create " + className +
                    ": bad arguments.");
        }
        return object;
    }

    public int getTime() {
        return time;
    }

    public DistanceMetric getDistanceMetric() {
        return distanceMetric;
    }

    public LearningRateFunction getLearningRate() {
        return learningRate;
    }

    public NeighborhoodWidthFunction getNeighborhoodWidth() {
        return neighborhoodWidth;
    }

    public GridType getGridType() {
        return gridType;
    }

    @Override
    public String toString() {
        return "CustomSOMFileReader";
    }
}
