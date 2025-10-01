package com.morgana.thevoidslawoverturetoreboot.attribute.client;

import com.morgana.thevoidslawoverturetoreboot.TheVoidsLawOvertureReboot;
import com.morgana.thevoidslawoverturetoreboot.attribute.attachment.ModAttachmentTypes;
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

    private final int imageWidth = 176;
    private final int imageHeight = 166;
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

            if (attributeData.getSkillPoints() > 0) {
                addRenderableWidget(Button.builder(Component.literal("+"), button -> allocatePoint("vitality"))
                        .bounds(leftPos + 50, topPos + 50, 20, 20)
                        .build());

                addRenderableWidget(Button.builder(Component.literal("+"), button -> allocatePoint("strength"))
                        .bounds(leftPos + 50, topPos + 80, 20, 20)
                        .build());

                addRenderableWidget(Button.builder(Component.literal("+"), button -> allocatePoint("agility"))
                        .bounds(leftPos + 50, topPos + 110, 20, 20)
                        .build());
            }
        }

        addRenderableWidget(Button.builder(Component.literal("关闭"), button -> onClose())
                .bounds(leftPos + imageWidth - 60, topPos + imageHeight - 25, 50, 20)
                .build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);

        if (minecraft != null && minecraft.player != null) {
            var attributeData = minecraft.player.getData(ModAttachmentTypes.PLAYER_ATTRIBUTES);

            // 绘制背景
            guiGraphics.fillGradient(leftPos, topPos, leftPos + imageWidth, topPos + imageHeight,
                    0xFF1a1a1a, 0xFF2d2d2d);

            super.render(guiGraphics, mouseX, mouseY, partialTick);

            // 绘制标题
            guiGraphics.drawString(font, Component.literal("角色属性"), leftPos + 20, topPos + 10, 0xFFFFFF, true);

            // 绘制等级和经验信息
            guiGraphics.drawString(font, Component.literal("等级: " + attributeData.getLevel()), leftPos + 20, topPos + 25, 0xFFFFFF);
            guiGraphics.drawString(font,
                    Component.literal("经验: " + attributeData.getExperience() + "/" + attributeData.getExpToNextLevel()),
                    leftPos + 20, topPos + 35, 0xFFFFFF);
            guiGraphics.drawString(font,
                    Component.literal("技能点: " + attributeData.getSkillPoints()),
                    leftPos + 20, topPos + 45, attributeData.getSkillPoints() > 0 ? 0x00FF00 : 0xFFFFFF);

            // 绘制属性信息
            guiGraphics.drawString(font, Component.literal("体质: " + attributeData.getVitality()), leftPos + 20, topPos + 60, 0xFFFFFF);
            guiGraphics.drawString(font, Component.literal("力量: " + attributeData.getStrength()), leftPos + 20, topPos + 85, 0xFFFFFF);
            guiGraphics.drawString(font, Component.literal("敏捷: " + attributeData.getAgility()), leftPos + 20, topPos + 110, 0xFFFFFF);
            guiGraphics.drawString(font, Component.literal("智力: " + attributeData.getIntelligence()), leftPos + 20, topPos + 135, 0xFFFFFF);

            // 绘制加成信息
            guiGraphics.drawString(font, Component.literal(attributeData.getVitalityBonus()), leftPos + 80, topPos + 60, 0x00FF00);
            guiGraphics.drawString(font, Component.literal(attributeData.getStrengthBonus()), leftPos + 80, topPos + 85, 0x00FF00);
            guiGraphics.drawString(font, Component.literal(attributeData.getAgilityBonus()), leftPos + 80, topPos + 110, 0x00FF00);
            guiGraphics.drawString(font, Component.literal(attributeData.getIntelligenceBonus()), leftPos + 80, topPos + 135, 0x00FF00);

            // 绘制经验条
            int expBarWidth = 150;
            int expBarHeight = 5;
            int expBarX = leftPos + 13;
            int expBarY = topPos + 150;
            float expPercent = attributeData.getExperiencePercent();

            guiGraphics.fill(expBarX, expBarY, expBarX + expBarWidth, expBarY + expBarHeight, 0xFF555555);
            guiGraphics.fill(expBarX, expBarY, expBarX + (int)(expBarWidth * expPercent), expBarY + expBarHeight, 0xFF00FF00);
            guiGraphics.renderOutline(expBarX, expBarY, expBarWidth, expBarHeight, 0xFF000000);
        }
    }

    private void allocatePoint(String attribute) {
        if (minecraft != null && minecraft.player != null) {
            var attributeData = minecraft.player.getData(ModAttachmentTypes.PLAYER_ATTRIBUTES);

            if (attributeData.getSkillPoints() > 0) {
                var packet = new AttributeAllocatePacket(attribute);
                PacketDistributor.sendToServer(packet);
            }
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}