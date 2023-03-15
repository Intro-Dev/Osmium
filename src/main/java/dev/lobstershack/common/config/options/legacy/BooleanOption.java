package dev.lobstershack.common.config.options.legacy;

public class BooleanOption extends LegacyOption {

    public boolean variable;

    public final boolean def;

    public BooleanOption(String identifier, boolean variable) {
        super(identifier, "BooleanOption");
        this.variable = variable;
        this.def = variable;
    }

    @Override
    public BooleanOption get() {
        throw new UnsupportedOperationException("Tried to call an operation on legacy option: " + this);
    }

    @Override
    public void put() {
        throw new UnsupportedOperationException("Tried to call an operation on legacy option: " + this);
    }


}
