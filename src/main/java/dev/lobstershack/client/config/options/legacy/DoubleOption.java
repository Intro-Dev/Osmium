package dev.lobstershack.client.config.options.legacy;

public class DoubleOption extends LegacyOption {

    public double variable;

    public final double def;

    public DoubleOption(String identifier, double variable) {
        super(identifier, "DoubleOption");
        this.variable = variable;
        this.def = variable;
    }

    @Override
    public DoubleOption get() {
        throw new UnsupportedOperationException("Tried to call an operation on legacy option: " + this);
    }

    @Override
    public void put() {
        throw new UnsupportedOperationException("Tried to call an operation on legacy option: " + this);
    }


}
