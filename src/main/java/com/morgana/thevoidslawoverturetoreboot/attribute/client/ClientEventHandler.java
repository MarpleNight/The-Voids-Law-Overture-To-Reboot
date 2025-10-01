package com.morgana.thevoidslawoverturetoreboot.attribute.client;

import com.morgana.thevoidslawoverturetoreboot.TheVoidsLawOvertureReboot;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = TheVoidsLawOvertureReboot.MOD_ID, value = Dist.CLIENT)
public class ClientEventHandler {
    public static final KeyMapping OPEN_ATTRIBUTES_KEY = new KeyMapping(
            "key." + TheVoidsLawOvertureReboot.MOD_ID + ".open_attributes",
            GLFW.GLFW_KEY_UNKNOWN, // 默认不绑定，可以在控制设置中绑定
            "key.categories." + TheVoidsLawOvertureReboot.MOD_ID
    );

    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(OPEN_ATTRIBUTES_KEY);
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (OPEN_ATTRIBUTES_KEY.consumeClick()) {
            Minecraft.getInstance().setScreen(new AttributeTabScreen());
        }
    }
}