package com.morgana.thevoidslawoverturetoreboot.client.gui;

import com.morgana.thevoidslawoverturetoreboot.TheVoidsLawOvertureReboot;
import com.morgana.thevoidslawoverturetoreboot.attributes.ModAttributes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.TabButton;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.gui.GuiScreenEvent;
import org.lwjgl.glfw.GLFW;

// 整合属性面板核心类（适配NeoForge 1.21.1）
@EventBusSubscriber(modid = TheVoidsLawOvertureReboot.MOD_ID, value = Dist.CLIENT)
public class PlayerAttributeSystem {

    // 按键绑定定义（使用1.21.1的构造方法）
    public static final KeyMapping OPEN_ATTRIBUTES_KEY = new KeyMapping(
            Component.translatable("key.the_voids_law_overture_to_reboot.open_attributes"),
            GLFW.GLFW_KEY_U,
            Component.translatable("key.categories.the_voids_law_overture_to_reboot")
    );

    // 标签页常量
    public static final String ATTRIBUTES_TAB = "attributes";
    private static String activeTab = "inventory"; // 默认显示背包

    // 注册按键绑定
    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(OPEN_ATTRIBUTES_KEY);
    }

    // 监听按键按下事件
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && OPEN_ATTRIBUTES_KEY.consumeClick()) {
            if (mc.screen instanceof InventoryScreen) {
                activeTab = ATTRIBUTES_TAB;
            } else {
                activeTab = ATTRIBUTES_TAB;
                mc.setScreen(new InventoryScreen(mc.player));
            }
        }
    }

    // 添加标签按钮到背包界面
    @SubscribeEvent
    public static void onInventoryInit(GuiScreenEvent.Init.Post event) {
        if (event.getScreen() instanceof InventoryScreen inventoryScreen) {
            // 创建水平布局
            LinearLayout tabLayout = LinearLayout.horizontal().spacing(2);

            // 背包标签
            TabButton inventoryTab = TabButton.builder(
                            Component.translatable("container.inventory"),
                            button -> activeTab = "inventory",
                            inventoryScreen.getFont()
                    )
                    .width(80)
                    .height(20)
                    .build();
            inventoryTab.setSelected(activeTab.equals("inventory"));

            // 属性标签
            TabButton attributesTab = TabButton.builder(
                            Component.translatable("screen.the_voids_law_overture_to_reboot.attributes"),
                            button -> activeTab = ATTRIBUTES_TAB,
                            inventoryScreen.getFont()
                    )
                    .width(80)
                    .height(20)
                    .build();
            attributesTab.setSelected(activeTab.equals(ATTRIBUTES_TAB));

            // 添加到布局
            tabLayout.addChild(inventoryTab);
            tabLayout.addChild(attributesTab);

            // 计算位置（居中显示在背包上方）
            int tabsX = inventoryScreen.getGuiLeft() + (inventoryScreen.getXSize() / 2) - (tabLayout.getWidth() / 2);
            int tabsY = inventoryScreen.getGuiTop() - 24;
            tabLayout.setPosition(tabsX, tabsY);

            // 添加到界面
            tabLayout.visitWidgets(event::addWidget);
        }
    }

    // 渲染属性面板内容
    @SubscribeEvent
    public static void onInventoryRender(GuiScreenEvent.Render.Post event) {
        if (event.getScreen() instanceof InventoryScreen inventoryScreen &&
                activeTab.equals(ATTRIBUTES_TAB)) {

            GuiGraphics gui = event.getGuiGraphics();
            Player player = Minecraft.getInstance().player;
            if (player == null) return;

            // 获取界面尺寸和位置
            int x = inventoryScreen.getGuiLeft();
            int y = inventoryScreen.getGuiTop();
            int width = inventoryScreen.getXSize();
            int height = inventoryScreen.getYSize();

            // 绘制背景覆盖层
            gui.fill(x, y, x + width, y + height, 0xCC000000);

            // 绘制标题
            Component title = Component.translatable("screen.the_voids_law_overture_to_reboot.attributes");
            int titleX = x + (width / 2);
            gui.drawCenteredString(inventoryScreen.getFont(), title, titleX, y + 10, 0xFFFFFF);

            // 绘制属性
            int contentY = y + 40;

            // 最大生命值
            drawAttribute(gui, inventoryScreen,
                    Component.translatable("attribute.name.generic.max_health"),
                    player.getAttributeValue(Attributes.MAX_HEALTH),
                    x, width, contentY);
            contentY += 24;

            // 攻击力
            drawAttribute(gui, inventoryScreen,
                    Component.translatable("attribute.name.generic.attack_damage"),
                    player.getAttributeValue(Attributes.ATTACK_DAMAGE),
                    x, width, contentY);
            contentY += 24;

            // 自定义属性
            AttributeInstance extraHealth = player.getAttribute(ModAttributes.EXTRA_HEALTH_ATTRIBUTE.get());
            if (extraHealth != null) {
                drawAttribute(gui, inventoryScreen,
                        Component.translatable("attribute.name.generic.extra_health"),
                        extraHealth.getValue(),
                        x, width, contentY);
            }
        }
    }

    // 辅助方法：绘制单个属性
    private static void drawAttribute(GuiGraphics gui, InventoryScreen screen,
                                      Component name, double value,
                                      int x, int width, int y) {
        // 属性名称（左对齐）
        gui.drawString(screen.getFont(), name, x + 30, y, 0xFFFFFF);

        // 属性值（右对齐）
        Component valueStr = Component.literal(String.format("%.1f", value));
        int valueX = x + width - 30 - screen.getFont().width(valueStr);
        gui.drawString(screen.getFont(), valueStr, valueX, y, 0xFFFFAA);
    }

    // 控制背包内容渲染
    @SubscribeEvent
    public static void onBeforeInventoryRender(GuiScreenEvent.Render.Pre event) {
        if (event.getScreen() instanceof InventoryScreen && activeTab.equals(ATTRIBUTES_TAB)) {
            // 保存当前激活的标签状态
            String currentTab = activeTab;

            // 临时切换到背包标签以确保背景正常渲染
            activeTab = "inventory";
            event.getScreen().render(event.getGuiGraphics(), event.getMouseX(), event.getMouseY(), event.getPartialTick());

            // 恢复标签状态
            activeTab = currentTab;

            // 取消原版渲染流程
            event.setCanceled(true);
        }
    }
}
