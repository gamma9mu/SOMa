package cs437.som;

/**
 * Grid type strategy interface for self-organizing maps.
 *
 * Implementations of this interface are intended to define a grid type for
 * organizing the neurons in an SOM.  Once an object of an implementing class
 * is given to an SOM, that SOM assumes control of that object.  It should not
 * be given to multiple SOMs or modified once it has been handed to an SOM.
 *
 */
public interface GridType {

    /**
     * Inform the GridType how many neurons exist in the map.
     *
     * @param dimension The dimensions of the grid.
     */
    void setNeuronCount(Dimension dimension);

    /**
     * Calculate the distance between two neurons in the neuron grid.
     *
     * @param neuron0 The index of the first neuron.
     * @param neuron1 The index of the second neuron.
     * @return The distance between the two neurons across the neuron grid.
     */
    double gridDistance(int neuron0, int neuron1);
    
}
