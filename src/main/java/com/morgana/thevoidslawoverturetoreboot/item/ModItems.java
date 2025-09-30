package com.morgana.thevoidslawoverturetoreboot.item;

import com.morgana.thevoidslawoverturetoreboot.TheVoidsLawOvertureReboot;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(TheVoidsLawOvertureReboot.MOD_ID);

    public static final DeferredItem<Item> PEPSI =
            ITEMS.register("pepsi", () -> new Item(new Item.Properties().food(ModFoods.PEPSI)));
    public static final DeferredItem<Item> CARDBOARD =
            ITEMS.register("material/cardboard", () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
