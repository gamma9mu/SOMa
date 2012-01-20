package cs437.som.visualization;

import java.awt.Color;

/**
 * Interface for accessing color progressions.
 */
public interface ColorProgression {

    /**
     * Provide a Color object corresponding to this progression and based on a
     * scale of 0-100 (inclusive).
     *
     * @param intensity    The intensity the color should correspond to.
     * @return              The appropriate color to heat map {@code intensity}.
     */
    Color getColor(int intensity);
}
