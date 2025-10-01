package com.morgana.thevoidslawoverturetoreboot.attribute.client;

import com.morgana.thevoidslawoverturetoreboot.TheVoidsLawOvertureReboot;
import com.morgana.thevoidslawoverturetoreboot.attribute.attachment.ModAttachmentTypes;
import com.morgana.thevoidslawoverturetoreboot.attribute.attachment.PlayerAttributeData;
import com.morgana.thevoidslawoverturetoreboot.attribute.network.AttributeAllocatePacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;

public class AttributeTabScreen extends Screen {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(TheVoidsLawOvertureReboot.MOD_ID, "textures/gui/attributes.png");

    private final int imageWidth = 200;
    private final int imageHeight = 180;
    private int leftPos;
    private int topPos;

    public AttributeTabScreen() {
        super(Component.translatable("gui." + TheVoidsLawOvertureReboot.MOD_ID + ".attributes"));
    }

    @Override
    protected void init() {
        super.init();

        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;

        if (minecraft != null && minecraft.player != null) {
            var attributeData = minecraft.player.getData(ModAttachmentTypes.PLAYER_ATTRIBUTES);

            // 将按钮移到上方，使用更好的样式
            int buttonY = topPos + 60;
            int buttonSpacing = 25;

            // 体质按钮
            addRenderableWidget(Button.builder(
                            Component.literal("体质 +"),
                            button -> allocatePoint("vitality")
                    )
                    .bounds(leftPos + 20, buttonY, 60, 20)
                    .build());

            // 力量按钮
            addRenderableWidget(Button.builder(
                            Component.literal("力量 +"),
                            button -> allocatePoint("strength")
                    )
                    .bounds(leftPos + 20, buttonY + buttonSpacing, 60, 20)
                    .build());

            // 敏捷按钮
            addRenderableWidget(Button.builder(
                            Component.literal("敏捷 +"),
                            button -> allocatePoint("agility")
                    )
                    .bounds(leftPos + 20, buttonY + buttonSpacing * 2, 60, 20)
                    .build());

            // 智力按钮
            addRenderableWidget(Button.builder(
                            Component.literal("智力 +"),
                            button -> allocatePoint("intelligence")
                    )
                    .bounds(leftPos + 20, buttonY + buttonSpacing * 3, 60, 20)
                    .build());

            // 关闭按钮 - 移到右下角
            addRenderableWidget(Button.builder(
                            Component.literal("关闭"),
                            button -> onClose()
                    )
                    .bounds(leftPos + imageWidth - 60, topPos + imageHeight - 25, 50, 20)
                    .build());

            // 禁用没有技能点时的按钮
            updateButtonStates(attributeData.getSkillPoints() > 0);
        }
    }

    // 更新按钮状态
    private void updateButtonStates(boolean enabled) {
        for (var widget : this.renderables) {
            if (widget instanceof Button button) {
                // 只禁用属性加点按钮，不禁用关闭按钮
                if (button.getMessage().getString().contains("+")) {
                    button.active = enabled;
                }
            }
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);

        if (minecraft != null && minecraft.player != null) {
            var attributeData = minecraft.player.getData(ModAttachmentTypes.PLAYER_ATTRIBUTES);

            // 绘制半透明背景
            guiGraphics.fillGradient(leftPos, topPos, leftPos + imageWidth, topPos + imageHeight,
                    0xCC1a1a1a, 0xCC2d2d2d);

            // 绘制边框
            guiGraphics.renderOutline(leftPos, topPos, imageWidth, imageHeight, 0xFF444444);

            super.render(guiGraphics, mouseX, mouseY, partialTick);

            // 绘制标题
            guiGraphics.drawString(font, Component.literal("=== 角色属性 ==="), leftPos + 50, topPos + 15, 0xFFFFFF, true);

            // 绘制等级和经验信息
            guiGraphics.drawString(font, Component.literal("等级: " + attributeData.getLevel()), leftPos + 100, topPos + 35, 0xFFFFFF);
            guiGraphics.drawString(font,
                    Component.literal("经验: " + attributeData.getExperience() + "/" + attributeData.getExpToNextLevel()),
                    leftPos + 100, topPos + 45, 0xFFFFFF);

            // 技能点信息 - 用不同颜色显示
            int skillPointsColor = attributeData.getSkillPoints() > 0 ? 0x00FF00 : 0xFF5555;
            guiGraphics.drawString(font,
                    Component.literal("技能点: " + attributeData.getSkillPoints()),
                    leftPos + 100, topPos + 55, skillPointsColor);

            // 绘制属性信息（在按钮旁边）
            int infoX = leftPos + 90;
            int infoY = topPos + 65;
            int spacing = 25;

            guiGraphics.drawString(font, Component.literal("体质: " + attributeData.getVitality()), infoX, infoY, 0xFFFFFF);
            guiGraphics.drawString(font, Component.literal("力量: " + attributeData.getStrength()), infoX, infoY + spacing, 0xFFFFFF);
            guiGraphics.drawString(font, Component.literal("敏捷: " + attributeData.getAgility()), infoX, infoY + spacing * 2, 0xFFFFFF);
            guiGraphics.drawString(font, Component.literal("智力: " + attributeData.getIntelligence()), infoX, infoY + spacing * 3, 0xFFFFFF);

            // 绘制加成信息
            int bonusX = leftPos + 150;
            guiGraphics.drawString(font, Component.literal(attributeData.getVitalityBonus()), bonusX, infoY, 0x00FF00);
            guiGraphics.drawString(font, Component.literal(attributeData.getStrengthBonus()), bonusX, infoY + spacing, 0x00FF00);
            guiGraphics.drawString(font, Component.literal(attributeData.getAgilityBonus()), bonusX, infoY + spacing * 2, 0x00FF00);
            guiGraphics.drawString(font, Component.literal(attributeData.getIntelligenceBonus()), bonusX, infoY + spacing * 3, 0x00FF00);

            // 绘制经验条
            drawExperienceBar(guiGraphics, attributeData);
        }
    }

    private void drawExperienceBar(GuiGraphics guiGraphics, PlayerAttributeData attributeData) {
        int expBarWidth = 180;
        int expBarHeight = 8;
        int expBarX = leftPos + 10;
        int expBarY = topPos + imageHeight - 40;
        float expPercent = attributeData.getExperiencePercent();

        // 经验条背景
        guiGraphics.fill(expBarX, expBarY, expBarX + expBarWidth, expBarY + expBarHeight, 0xFF555555);

        // 经验条填充
        if (expPercent > 0) {
            guiGraphics.fill(expBarX, expBarY, expBarX + (int)(expBarWidth * expPercent), expBarY + expBarHeight, 0xFF00FF00);
        }

        // 经验条边框
        guiGraphics.renderOutline(expBarX, expBarY, expBarWidth, expBarHeight, 0xFF000000);

        // 经验文本
        String expText = attributeData.getExperience() + " / " + attributeData.getExpToNextLevel() + " (" + (int)(expPercent * 100) + "%)";
        guiGraphics.drawString(font, Component.literal(expText), expBarX + (expBarWidth - font.width(expText)) / 2, expBarY - 12, 0xFFFFFF);
    }

    private void allocatePoint(String attribute) {
        if (minecraft != null && minecraft.player != null) {
            var attributeData = minecraft.player.getData(ModAttachmentTypes.PLAYER_ATTRIBUTES);

            if (attributeData.getSkillPoints() > 0) {
                // 发送网络包
                var packet = new AttributeAllocatePacket(attribute);
                PacketDistributor.sendToServer(packet);

                // 立即更新按钮状态（客户端预测）
                attributeData.allocateSkillPoint(attribute, minecraft.player);
                updateButtonStates(attributeData.getSkillPoints() > 0);
            }
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}