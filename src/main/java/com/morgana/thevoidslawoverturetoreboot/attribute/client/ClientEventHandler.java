package com.morgana.thevoidslawoverturetoreboot.attribute.client;

import com.morgana.thevoidslawoverturetoreboot.Config;
import com.morgana.thevoidslawoverturetoreboot.TheVoidsLawOvertureReboot;
import com.morgana.thevoidslawoverturetoreboot.attribute.attachment.ModAttachmentTypes;
import com.morgana.thevoidslawoverturetoreboot.attribute.network.AttributeDataSyncPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = TheVoidsLawOvertureReboot.MOD_ID)
public class ClientEventHandler {
    @SubscribeEvent
    public static void onMobKill(LivingDeathEvent  event) {
        if (event.getSource().getEntity() instanceof Player player && event.getEntity() instanceof Monster) {
            handleExperience(player, Config.EXP_PER_MOB_KILL.get());
        }
    }

    @SubscribeEvent
    public static void onPlayerHurt(LivingDamageEvent  event) {
        if (event.getEntity() instanceof Player player && event.getEntity() instanceof Monster) {
            handleExperience(player, 2);
        }
    }



    private static void handleExperience(Player player, int expAmount) {
        var attributeData = player.getData(ModAttachmentTypes.PLAYER_ATTRIBUTES);
        int oldLevel = attributeData.getLevel();

        attributeData.addExperience(expAmount, player);

        if (!player.level().isClientSide() && (oldLevel != attributeData.getLevel())) {
            CompoundTag tag = attributeData.toNBT();
            AttributeDataSyncPacket packet = new AttributeDataSyncPacket(tag);
            PacketDistributor.sendToAllPlayers(packet);
        }
    }

}