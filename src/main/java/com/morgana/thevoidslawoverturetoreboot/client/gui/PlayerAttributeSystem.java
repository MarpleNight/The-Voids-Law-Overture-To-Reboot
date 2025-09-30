package com.morgana.thevoidslawoverturetoreboot.client.gui;

import com.morgana.thevoidslawoverturetoreboot.TheVoidsLawOvertureReboot;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = TheVoidsLawOvertureReboot.MOD_ID, value = Dist.CLIENT)
public class PlayerAttributeSystem {
    public static final KeyMapping OPEN_ATTRIBUTES_KEY = new KeyMapping(
            "key.the_voids_law_overture_to_reboot.open_attributes",
            GLFW.GLFW_KEY_U,
            "key.categories.the_voids_law_overture_to_reboot"
    );

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(OPEN_ATTRIBUTES_KEY);
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().screen == null && OPEN_ATTRIBUTES_KEY.consumeClick()) {
            Minecraft.getInstance().setScreen(new AttributeScreen(Minecraft.getInstance().player));
        }
    }

    @SubscribeEvent
    public static void onInventoryInit(ScreenEvent.Init.Post event) {
        if (event.getScreen() instanceof InventoryScreen inventoryScreen) {
            int buttonX = inventoryScreen.getGuiLeft() + 80;
            int buttonY = inventoryScreen.getGuiTop() - 20;

            event.addListener(Button.builder(
                    Component.literal(I18n.get("screen.the_voids_law_overture_to_reboot.attributes_button")),
                    (btn) -> Minecraft.getInstance().setScreen(new AttributeScreen(Minecraft.getInstance().player))
            ).pos(buttonX, buttonY).size(40, 20).build());
        }
    }

    public static class AttributeScreen extends Screen {
        private final Player player;

        public AttributeScreen(Player player) {
            super(Component.translatable("screen.the_voids_law_overture_to_reboot.attributes"));
            this.player = player;
        }

        @Override
        public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
            super.render(guiGraphics, mouseX, mouseY, partialTick);

            // 绘制标题
            guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);

            // 绘制属性信息
            int y = 50;
            guiGraphics.drawString(this.font, "生命值: " + player.getHealth() + "/" + player.getMaxHealth(),
                                  50, y, 0xFFFFFF);
        }

        @Override
        public boolean isPauseScreen() {
            return false;
        }
    }
}
