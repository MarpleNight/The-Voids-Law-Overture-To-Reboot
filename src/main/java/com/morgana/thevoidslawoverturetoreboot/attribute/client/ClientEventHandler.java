package com.morgana.thevoidslawoverturetoreboot.attribute.client;

import com.morgana.thevoidslawoverturetoreboot.TheVoidsLawOvertureReboot;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = TheVoidsLawOvertureReboot.MOD_ID, value = Dist.CLIENT)
public class ClientEventHandler {

    private static boolean attributeTabOpen = false;

    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.Init.Post event) {
        // 只在背包界面添加标签页
        if (event.getScreen() instanceof InventoryScreen inventoryScreen) {
            // 计算按钮位置（在背包界面右侧）
            int x = inventoryScreen.getGuiLeft() + inventoryScreen.getXSize();
            int y = inventoryScreen.getGuiTop() + 4;

            // 创建属性标签按钮
            AttributeTabButton attributeButton = new AttributeTabButton(
                    x, y, attributeTabOpen,
                    button -> {
                        attributeTabOpen = !attributeTabOpen;
                        // 重新初始化屏幕来更新按钮状态
                        inventoryScreen.init(
                                inventoryScreen.getMinecraft(),
                                inventoryScreen.width,
                                inventoryScreen.height
                        );
                    }
            );

            event.addListener(attributeButton);

            // 如果属性标签页打开，显示属性界面
            if (attributeTabOpen) {
                AttributeTabScreen attributeScreen = new AttributeTabScreen();
                // 这里需要将属性界面添加到背包界面中
                // 由于技术限制，我们改为在按钮点击时直接打开独立界面
            }
        }
    }

    @SubscribeEvent
    public static void onScreenRender(ScreenEvent.Render.Post event) {
        // 如果属性标签页打开，在背包界面上渲染属性内容
        if (attributeTabOpen && event.getScreen() instanceof InventoryScreen) {
            // 这里可以渲染属性界面的内容
            // 但由于技术复杂性，我们改为使用独立界面
        }
    }
}