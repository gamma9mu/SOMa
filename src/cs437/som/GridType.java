package cs437.som;

/**
 * Grid type for self-organizing maps.
 */
public interface GridType {

    /**
     * Inform the GridType how many neurons exist in the map.
     *
     * @param neuronCount The count of neurons to manage.
     */
    void setNeuronCount(int neuronCount);

    /**
     * Calculate the distance between two neurons in the neuron grid.
     *
     * @param neuron0 The index of the first neuron.
     * @param neuron1 The index of the second neuron.
     * @return The distance between the two neurons across the neuron grid.
     */
    double gridDistance(int neuron0, int neuron1);
    
}
