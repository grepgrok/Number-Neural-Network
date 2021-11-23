import java.util.Map;
import java.util.HashMap;

public class Neuron {
    //input neurons have null forward_weights
    public static final byte TYPE_INPUT = 0;
    //hidden neurons have both forward_weights and back_weights
    public static final byte TYPE_HIDDEN = 1;
    //output neurons have null back_weights
    public static final byte TYPE_OUTPUT = 2;

    private Weight[] forward_weights = null; //all the weights used in forward prop
    private Weight[] back_weights = null; //all the weights used in back prop
    private double bias;

    private int neuron_type;
    private int[] location;
    private boolean hasWeights;

    public Neuron(int type, int layer, int index) throws IllegalArgumentException{
        if (type == TYPE_INPUT || type == TYPE_HIDDEN || type == TYPE_OUTPUT) {
            neuron_type = type;
        } else {
            throw new IllegalArgumentException("Neuron type not found");
        }
        location = new int[] {layer, index};
        bias = Math.random() * 8.0 - 4.0;
        hasWeights = false;
    }

    //since every Neuron must be connected to a Neuron, you can't create the first one without a Neuron existing, which is an infinite loop
    //setWeights must always be called before using a Neuron
    public void setWeights(Neuron[] sources, Neuron[] sinks){
        //You CAN give the wrong params for the neuron type but I will just do whatever is logical (inputs get sources, outputs get sinks)
        if (neuron_type == TYPE_HIDDEN) {
            forward_weights = new Weight[sources.length];
            back_weights = new Weight[sinks.length];

            //make a new weight when this is the source
            for (int i = 0; i < back_weights.length; i++) {
                back_weights[i] = new Weight(this, sinks[i]);
            }

            for (int i = 0; i < forward_weights.length; i++) {
                forward_weights[i] = sources[i].getSink(location[1]);
            }
        } else if (neuron_type == TYPE_INPUT) {
            setWeights(sources);
        } else if (neuron_type == TYPE_OUTPUT) {
            setWeights(sinks);
        }
        hasWeights = true;
    }

    public void setWeights(Neuron[] others) {
        if (neuron_type == TYPE_INPUT) {
            back_weights = new Weight[others.length];
            for (int i = 0; i < back_weights.length; i++) {
                back_weights[i] = new Weight(this, others[i]);
            }
        } else if (neuron_type == TYPE_OUTPUT) {
            forward_weights = new Weight[others.length];
            for (int i = 0; i < forward_weights.length; i++) {
                forward_weights[i] = others[i].getSink(location[1]);
            }
        }
        hasWeights = true;
    }

    public Weight getSink(int i) {
        return back_weights[i];
    }

    public int getType() {
        return neuron_type;
    }
}
