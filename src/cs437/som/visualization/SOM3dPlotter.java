/*
 * Copyright (c) 2011 Brian A. Guthrie, Colin Murphy
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice,
 *        this list of conditions and the following disclaimer.
 *     2. Redistributions in binary form must reproduce the above copyright
 *        notice, this list of conditions and the following disclaimer in the
 *        documentation and/or other materials provided with the distribution.
 *     3. The name of the authors may not be used to endorse or promote products
 *        derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHORS ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package cs437.som.visualization;

import cs437.som.SOM;
import cs437.som.Dimension;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Visualization for SOM with a 3-dimensional input.
 * Adapted from 2D plotter. Instead of using position to map neuron weights, it uses color.
 */
public class SOM3dPlotter extends JFrame {
    private static final long serialVersionUID = 0L;
    
    public static final int WIDTH = 500;
    public static final int HEIGHT = 500;
    SOM map = null;
    Dimension dims;
    BufferedImage img;

    /**
     * Create and setup a dot plot for a 3D input SOM.
     * @param map The SOM to plot.
     */
    public SOM3dPlotter(SOM map) {
        super("SOM Plot");

        if (map.getInputLength() != 3) throw new IllegalArgumentException("SOM does not map 3d inputs");

        this.map = map;

        dims = this.map.getGridSize();
        img = new BufferedImage(dims.x, dims.y, BufferedImage.TYPE_INT_ARGB);

        setSize(WIDTH, HEIGHT);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        createBufferStrategy(2);
    }

    /**
     * Draw the current weights of the SOM's neurons.
     */
    public void draw() {
        Graphics g = getBufferStrategy().getDrawGraphics();

        int neuronCount = map.getNeuronCount();
        int[] pts = new int[neuronCount];

        for (int i = 0; i < neuronCount; i++) {
            pts[i] = 0xFF000000;
            pts[i] += (int)(map.getWeight(i,0)*255)<<16;
            pts[i] += (int)(map.getWeight(i,1)*255)<<8;
            pts[i] += (int)(map.getWeight(i,2)*255);
        }

        img.setRGB(0,0, dims.x, dims.y, pts, 0, dims.x);

        g.drawImage(img.getScaledInstance(WIDTH, HEIGHT, Image.SCALE_DEFAULT), 0, 0, null);

        getBufferStrategy().show();
    }

    @Override
    public String toString() {
        return "SOM3dPlotter{map=" + map + '}';
    }
}
