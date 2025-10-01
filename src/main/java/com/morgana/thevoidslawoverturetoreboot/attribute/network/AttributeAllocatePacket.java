package com.morgana.thevoidslawoverturetoreboot.attribute.network;

import com.morgana.thevoidslawoverturetoreboot.TheVoidsLawOvertureReboot;
import com.morgana.thevoidslawoverturetoreboot.attribute.attachment.ModAttachmentTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.PacketDistributor;

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
            var attributeData = player.getData(ModAttachmentTypes.PLAYER_ATTRIBUTES);

            boolean success = attributeData.allocateSkillPoint(packet.attribute(), player);
            if (success) {
                var syncPacket = new AttributeDataSyncPacket(attributeData.serializeNBT());
                PacketDistributor.sendToAllPlayers(syncPacket);
            }
        });
    }
}