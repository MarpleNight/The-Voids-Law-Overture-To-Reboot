package com.morgana.thevoidslawoverturetoreboot.attribute.attachment;

import com.morgana.thevoidslawoverturetoreboot.TheVoidsLawOvertureReboot;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModAttachmentTypes {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, TheVoidsLawOvertureReboot.MOD_ID);

    public static final Supplier<AttachmentType<PlayerAttributeData>> PLAYER_ATTRIBUTES =
            ATTACHMENT_TYPES.register("player_attributes",
                    () -> AttachmentType.builder(() -> new PlayerAttributeData()).build());
}