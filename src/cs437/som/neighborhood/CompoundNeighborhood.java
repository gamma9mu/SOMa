package cs437.som.neighborhood;

import cs437.som.NeighborhoodWidthFunction;

import java.util.*;

/**
 * Collects a series of neighborhood width functions to be used sequentially in
 * training a SOM.  This allows a SOM to be trained with a large, linearly
 * decreasing neighborhood width initially and, afterwards, the width function
 * can change to a exponentially decreasing function.  Any number of
 * combinations should be possible.
 *
 * Using neighborhood width functions in a CompoundNeighborhood is an exception
 * to the instructions given in the NeighborhoodWidthFunction interface
 * documentation:
 *
 * The NeighborhoodWidthFunction object should have its expected iterations
 * property set before it is added to a CompoundNeighborhood object.
 *
 * Once a NeighborhoodWidthFunction has been added to a CompoundNeighborhood
 * object, its ownership is passed to the CompoundNeighborhood object ad should
 * not be modified by the user afterward.
 */
public class CompoundNeighborhood implements NeighborhoodWidthFunction {
    private int nextTransition = -1;

    private NeighborhoodWidthFunction currentFunction;

    private Map<Integer, NeighborhoodWidthFunction> widthFunctions
            = new HashMap<Integer, NeighborhoodWidthFunction>(2);

    public CompoundNeighborhood(NeighborhoodWidthFunction initialWidthFuncton) {
        widthFunctions.put(0, initialWidthFuncton);
        currentFunction = initialWidthFuncton;
    }

    public void setExpectedIterations(int expectedIterations) {
        if (nextTransition == -1) {
            nextTransition = expectedIterations;
        }
    }

    public double neighborhoodWidth(int iteration) {
        if (iteration == nextTransition) {
            shiftFunctions();
        }
        return currentFunction.neighborhoodWidth(iteration);
    }

    /**
     * Add a neighborhood function to be used after a specific number of
     * iterations.
     *
     * @param neighborhood The neighborhood function object to add.
     * @param startAt The iteration at which to use {@code neighborhood}.
     */
    public void addNeighborhood(NeighborhoodWidthFunction neighborhood, int startAt) {
        widthFunctions.put(startAt, neighborhood);
    }

    /**
     * Find the next neighborhood width function shift it into the current
     * slot.
     */
    private void shiftFunctions() {
        // Don't try to shift if there's nothing left to use.
        if (widthFunctions.isEmpty()) {
            return;
        }

        // Find the next lowest transition point
        int low = nextTransition;
        for (Integer i : widthFunctions.keySet()) {
            if (i < low) {
                low = i;
            }
        }

        // Move the next function into place.
        currentFunction = widthFunctions.remove(low);
        nextTransition = low;
    }

    @Override
    public String toString() {
        return "CompoundNeighborhood";
    }
}
