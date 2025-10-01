package com.morgana.thevoidslawoverturetoreboot.attribute.event;

import com.morgana.thevoidslawoverturetoreboot.TheVoidsLawOvertureReboot;
import com.morgana.thevoidslawoverturetoreboot.attribute.attachment.ModAttachmentTypes;
import com.morgana.thevoidslawoverturetoreboot.attribute.network.AttributeDataSyncPacket;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = TheVoidsLawOvertureReboot.MOD_ID)
public class CommonEventHandler {

    @SubscribeEvent
    public static void onMobKill(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof Player player &&
                event.getEntity() instanceof Monster) {
            handleExperienceGain(player, 10);
        }
    }

    @SubscribeEvent
    public static void onOreMined(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (player != null) {
            if (event.getState().getBlock().getName().getString().toLowerCase().contains("ore")) {
                handleExperienceGain(player, 5);
            }
        }
    }

    private static void handleExperienceGain(Player player, int expAmount) {
        var attributeData = player.getData(ModAttachmentTypes.PLAYER_ATTRIBUTES);
        int oldLevel = attributeData.getLevel();

        attributeData.addExperience(expAmount, player);

        if (!player.level().isClientSide() && (oldLevel != attributeData.getLevel())) {
            // 使用新的方法名
            var packet = new AttributeDataSyncPacket(attributeData.toNBT());
            PacketDistributor.sendToAllPlayers(packet);
        }
    }
}