package cs437.som.visualization;

import java.awt.*;

/**
 * A color progression from white (#FFFFFF) to black (#000000).
 */
public class GreyScaleHeat implements ColorProgression {
    private static final float SCALE = 100.0f;

    @Override
    public Color getColor(int intensity) {
        if (intensity < 0)
            return Color.white;
        else if (intensity > 100)
            return Color.black;
        else {
            final float brightness = (100-intensity) / SCALE;
            final int rgb = Color.HSBtoRGB(0.0f, 0.0f, brightness);
            return new Color(rgb);
        }
    }
}
