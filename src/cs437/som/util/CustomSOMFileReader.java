package cs437.som.util;

import cs437.som.*;
import cs437.som.neighborhood.CompoundNeighborhood;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Provides for reading in CustomizableSOMs from input streams.
 */
public class CustomSOMFileReader extends SOMFileReader {
    private static final Pattern distanceMetricRegEx = Pattern.compile(
            "distance\\s*(?:metric)?\\s*:\\s*(\\w*)", Pattern.CASE_INSENSITIVE);
    private static final Pattern learningRateRegEx = Pattern.compile(
            "learning\\s*(?:rate)?\\s*(?:function)?\\s*:\\s*(\\w*)\\s*(.*)",
            Pattern.CASE_INSENSITIVE);
    private static final Pattern neighborhoodRegEx = Pattern.compile(
            "neighborhood\\s*(?:width)?\\s*(?:function)?\\s*:\\s*" +
                    "(?:(\\w*)\\s*(.*)|(CompoundNeighborhood)\\s*(begin))",
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
    protected void unmatchedLine(String line) throws IOException {
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

    /**
     * Match a distance metric line.
     *
     * @param line The input's line to attempt to match and extract from.
     * @return {@code true} if the line is matched, {@code false} otherwise.
     */
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

    /**
     * Match a learning rate function line.
     *
     * @param line The input's line to attempt to match and extract from.
     * @return {@code true} if the line is matched, {@code false} otherwise.
     */
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

    /**
     * Match a neighborhood function line.
     *
     * @param line The input's line to attempt to match and extract from.
     * @return {@code true} if the line is matched, {@code false} otherwise.
     * @throws java.io.IOException if I/O fails.
     */
    private boolean matchNeighborhood(String line) throws IOException {
        Matcher neighborhoodMatch = neighborhoodRegEx.matcher(line);
        if (neighborhoodMatch.matches()) {
            if (neighborhoodMatch.group(1)
                    .compareToIgnoreCase("CompoundNeighborhood") == 0) {
                neighborhoodWidth = CompoundNeighborhood.parse(inputReader);
            }
            neighborhoodWidth = (NeighborhoodWidthFunction)
                    instantiateFromString("cs437.som.neighborhood",
                            neighborhoodMatch.group(1),
                            neighborhoodMatch.group(2));
            return true;
        }
        return false;
    }

    /**
     * Match a grid type line.
     *
     * @param line The input's line to attempt to match and extract from.
     * @return {@code true} if the line is matched, {@code false} otherwise.
     */
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

    /**
     * Create an object, from its default constructor, by reflection.
     *
     * @param pkg The package in which to find the class.
     * @param cls The class to instantiate.
     * @return A default constructed object of type {@code cls}.
     */
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

    /**
     * Create an object, from its single string constructor, by reflection.
     * The single argument is taken as the arguments provided on the line from
     * the input stream.
     *
     * @param pkg The package in which to find the class.
     * @param cls The class to instantiate.
     * @param args The arguments {@code String} to provide.
     * @return A constructed object of type {@code cls}.
     */
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

    /**
     * Return the parsed time count.
     *
     * @return The time count from the input stream.
     */
    public int getTime() {
        return time;
    }

    /**
     * Return the parsed distance metric.
     *
     * @return The distance metric from the input stream.
     */
    public DistanceMetric getDistanceMetric() {
        return distanceMetric;
    }

    /**
     * Return the parsed learning rate function.
     *
     * @return The learning rate function from the input stream.
     */
    public LearningRateFunction getLearningRate() {
        return learningRate;
    }

    /**
     * Return the parsed neighborhood width function.
     *
     * @return The neighborhood function from the input stream.
     */
    public NeighborhoodWidthFunction getNeighborhoodWidth() {
        return neighborhoodWidth;
    }

    /**
     * Return the parsed grid type.
     *
     * @return The grid type from the input stream.
     */
    public GridType getGridType() {
        return gridType;
    }

    @Override
    public String toString() {
        return "CustomSOMFileReader";
    }
}
