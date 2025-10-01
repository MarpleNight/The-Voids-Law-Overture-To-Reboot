package com.morgana.thevoidslawoverturetoreboot.attribute.network;

import com.morgana.thevoidslawoverturetoreboot.TheVoidsLawOvertureReboot;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class PacketHandler {

    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(TheVoidsLawOvertureReboot.MOD_ID);

        registrar.playToClient(AttributeDataSyncPacket.TYPE, AttributeDataSyncPacket.STREAM_CODEC,
                AttributeDataSyncPacket::handle);

        registrar.playToServer(AttributeAllocatePacket.TYPE, AttributeAllocatePacket.STREAM_CODEC,
                AttributeAllocatePacket::handle);
    }
}