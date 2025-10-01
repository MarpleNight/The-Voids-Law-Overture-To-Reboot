package com.morgana.thevoidslawoverturetoreboot.attribute.network;

import com.morgana.thevoidslawoverturetoreboot.TheVoidsLawOvertureReboot;
import com.morgana.thevoidslawoverturetoreboot.attribute.attachment.ModAttachmentTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record AttributeDataSyncPacket(CompoundTag data) implements CustomPacketPayload {
    public static final Type<AttributeDataSyncPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(TheVoidsLawOvertureReboot.MOD_ID, "attribute_sync"));

    public static final StreamCodec<FriendlyByteBuf, AttributeDataSyncPacket> STREAM_CODEC = StreamCodec.of(
            (buf, packet) -> buf.writeNbt(packet.data),
            buf -> new AttributeDataSyncPacket(buf.readNbt())
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(AttributeDataSyncPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            var player = context.player();
            var attributeData = player.getData(ModAttachmentTypes.PLAYER_ATTRIBUTES);
            attributeData.deserializeNBT(packet.data());
        });
    }
}