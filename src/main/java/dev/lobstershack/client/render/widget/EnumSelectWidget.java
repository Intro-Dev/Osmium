package dev.lobstershack.client.render.widget;

import dev.lobstershack.client.config.Options;
import dev.lobstershack.client.config.options.Option;
import dev.lobstershack.client.util.EnumUtil;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class EnumSelectWidget extends AbstractScalableButton {

    public final Option<? extends Enum<?>> attachedOption;
    public final String key;

    @SuppressWarnings("unsafe")
    public EnumSelectWidget(int x, int y, int width, int height, @NotNull Option<? extends Enum<?>> attachedOption, String key) {
        super(x, y, width, height, Component.literal(""), button -> {
            if(!Options.getOverwrittenOptions().containsKey(attachedOption.getIdentifier())) {
                Enum<?> attachedEnum;
                attachedEnum = EnumUtil.nextEnum(attachedOption.get());
                attachedOption.setUnsafe(attachedEnum);
                button.setMessage(Component.translatable(key + attachedEnum.name().toLowerCase()));
            } else {
                System.out.println("inactive");
                button.active = false;
            }
        }, 1f);


        if(Options.getOverwrittenOptions().containsKey(attachedOption.getIdentifier())) {
            this.active = false;
        }
        Enum<?> attachedEnum = attachedOption.get();
        this.setMessage(Component.translatable(key + attachedEnum.name().toLowerCase()));

        this.attachedOption = attachedOption;
        this.key = key;
    }

    public EnumSelectWidget(int x, int y, int width, int height, @NotNull Option<? extends Enum<?>> attachedOption, String key, float scale) {
        super(x, y, width, height, Component.literal(""), button -> {
            if(!Options.getOverwrittenOptions().containsKey(attachedOption.getIdentifier())) {
                Enum<?> attachedEnum;
                attachedEnum = EnumUtil.nextEnum(attachedOption.get());
                attachedOption.setUnsafe(attachedEnum);
                button.setMessage(Component.translatable(key + attachedEnum.name().toLowerCase()));
            } else {
                System.out.println("inactive");
                button.active = false;
            }
        }, 1f);


        if(Options.getOverwrittenOptions().containsKey(attachedOption.getIdentifier())) {
            this.active = false;
        }
        Enum<?> attachedEnum = attachedOption.get();
        this.setMessage(Component.translatable(key + attachedEnum.name().toLowerCase()));

        this.attachedOption = attachedOption;
        this.key = key;
        this.setScale(scale);
    }


}
