package dev.lobstershack.common.config.options.legacy;

public class StringOption extends LegacyOption {

    public String variable;

    public final String def;

    public StringOption(String identifier, String variable) {
        super(identifier, "StringOption");
        this.variable = variable;
        this.def = variable;
    }

    @Override
    public StringOption get() {
        throw new UnsupportedOperationException("Tried to call an operation on legacy option: " + this);
    }

    @Override
    public void put() {
        throw new UnsupportedOperationException("Tried to call an operation on legacy option: " + this);
    }

}
