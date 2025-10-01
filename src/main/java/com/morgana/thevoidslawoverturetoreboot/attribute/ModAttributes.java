package com.morgana.thevoidslawoverturetoreboot.attribute;

import com.morgana.thevoidslawoverturetoreboot.TheVoidsLawOvertureReboot;
import com.morgana.thevoidslawoverturetoreboot.attribute.attachment.ModAttachmentTypes;
import com.morgana.thevoidslawoverturetoreboot.attribute.network.PacketHandler;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

public class ModAttributes {

    public static void register(IEventBus modEventBus) {
        // 注册附件类型
        ModAttachmentTypes.ATTACHMENT_TYPES.register(modEventBus);

        // 注册网络包
        modEventBus.addListener(ModAttributes::onRegisterPackets);

        TheVoidsLawOvertureReboot.LOGGER.info("Attribute system registered successfully!");
    }

    private static void onRegisterPackets(final RegisterPayloadHandlersEvent event) {
        PacketHandler.register(event);
    }
}