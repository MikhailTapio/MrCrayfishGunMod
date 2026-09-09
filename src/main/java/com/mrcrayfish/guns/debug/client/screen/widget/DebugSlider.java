package com.mrcrayfish.guns.debug.client.screen.widget;

import com.mrcrayfish.guns.debug.IDebugWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.gui.widget.ExtendedSlider;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Author: MrCrayfish
 */
public class DebugSlider extends ExtendedSlider implements IDebugWidget {
    private final Consumer<Double> callback;

    public DebugSlider(double minValue, double maxValue, double currentValue, double stepSize, int precision, Consumer<Double> callback) {
        super(0, 0, 0, 14, Component.empty(), Component.empty(), minValue, maxValue, currentValue, stepSize, precision, true);
        this.callback = callback;
    }

    @Override
    protected void applyValue() {
        this.callback.accept(this.getValue());
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.renderWidget(graphics, mouseX, mouseY, partialTick);
    }
}
