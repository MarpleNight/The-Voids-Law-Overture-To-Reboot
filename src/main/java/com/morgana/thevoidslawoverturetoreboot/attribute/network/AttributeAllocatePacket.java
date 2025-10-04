package com.morgana.thevoidslawoverturetoreboot.attribute.network;

import com.morgana.thevoidslawoverturetoreboot.TheVoidsLawOvertureReboot;
import com.morgana.thevoidslawoverturetoreboot.attribute.attachment.ModAttachmentTypes;
import com.morgana.thevoidslawoverturetoreboot.attribute.attachment.PlayerAttributeData;
import com.morgana.thevoidslawoverturetoreboot.attribute.client.AttributeTabScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record AttributeAllocatePacket(String attribute) implements CustomPacketPayload {
    public static final Type<AttributeAllocatePacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(TheVoidsLawOvertureReboot.MOD_ID, "attribute_allocate"));

    public static final StreamCodec<FriendlyByteBuf, AttributeAllocatePacket> STREAM_CODEC = StreamCodec.of(
            (buf, packet) -> buf.writeUtf(packet.attribute),
            buf -> new AttributeAllocatePacket(buf.readUtf())
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(AttributeAllocatePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            var player = context.player();
            if (packet == null) return;

            PlayerAttributeData attributeData = player.getData(ModAttachmentTypes.PLAYER_ATTRIBUTES);

                if (attributeData.getSkillPoints() > 0) {
                    switch (packet.attribute()) {
                        case "strength" -> attributeData.increaseStrength();
                        case "agility" -> attributeData.increaseAgility();
                        case "intelligence" -> attributeData.increaseIntelligence();
                        case "vitality" -> attributeData.increaseVitality();
                    }
                }


        });
    }
}