package com.morgana.thevoidslawoverturetoreboot.attribute.client;

import com.morgana.thevoidslawoverturetoreboot.TheVoidsLawOvertureReboot;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class AttributeTabButton extends Button {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(TheVoidsLawOvertureReboot.MOD_ID, "textures/gui/tabs.png");

    private final boolean selected;

    public AttributeTabButton(int x, int y, boolean selected, OnPress onPress) {
        super(x, y, 28, 32, Component.literal(""), onPress, DEFAULT_NARRATION);
        this.selected = selected;
        this.setTooltip(Tooltip.create(Component.translatable("gui." + TheVoidsLawOvertureReboot.MOD_ID + ".attributes")));
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int v = this.selected ? 32 : 0;

        if (this.isHovered()) {
            v += 64;
        }

        guiGraphics.blit(TEXTURE, this.getX(), this.getY(), 0, v, this.width, this.height);
    }
}