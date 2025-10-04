package com.morgana.thevoidslawoverturetoreboot.attribute.attachment;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.morgana.thevoidslawoverturetoreboot.Config;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public class PlayerAttributeData {
    private int level = 1;
    private int experience = 0;
    private int skillPoints = 0;

    private int vitality = 1;
    private int strength = 1;
    private int agility = 1;
    private int intelligence = 1;

    public PlayerAttributeData() {}

    public int getExpToNextLevel() {
        return Config.BASE_EXP_TO_LEVEL_UP.get() * level;
    }

    public float getIntelligenceExpMultiplier() {
        return 1.0f + (intelligence - 1) * 0.05f;
    }

    public void addExperience(int amount, Player player) {
        float multiplier = getIntelligenceExpMultiplier();
        int actualAmount = (int) (amount * multiplier);

        this.experience += actualAmount;
        boolean leveledUp = false;

        while (experience >= getExpToNextLevel()) {
            this.experience -= getExpToNextLevel();
            this.level++;
            leveledUp = true;
            this.skillPoints++;
        }

        if (leveledUp) {
            applyAttributeEffects(player);
        }
    }

    public void applyAttributeEffects(Player player) {
        if (player.level().isClientSide()) return;

        // 应用体质到最大生命值
        AttributeInstance maxHealth = player.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealth != null) {
            double baseHealth = 20.0;
            double bonusHealth = (vitality - 1) * 2.0;
            maxHealth.setBaseValue(baseHealth + bonusHealth);

            if (player.getHealth() > maxHealth.getValue()) {
                player.setHealth((float) maxHealth.getValue());
            }
        }

        // 应用力量到攻击伤害
        AttributeInstance attackDamage = player.getAttribute(Attributes.ATTACK_DAMAGE);
        if (attackDamage != null) {
            double baseDamage = 1.0;
            double bonusDamage = (strength - 1) * 0.5;
            attackDamage.setBaseValue(baseDamage + bonusDamage);
        }

        // 应用敏捷到移动速度
        AttributeInstance movementSpeed = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if (movementSpeed != null) {
            double baseSpeed = 0.1;
            double bonusSpeed = (agility - 1) * 0.005;
            movementSpeed.setBaseValue(baseSpeed * (1 + bonusSpeed));
        }
    }

    public boolean allocateSkillPoint(String attribute, Player player) {
        if (skillPoints <= 0) return false;

        switch (attribute) {
            case "vitality" -> vitality++;
            case "strength" -> strength++;
            case "agility" -> agility++;
            case "intelligence" -> intelligence++;
            default -> { return false; }
        }

        skillPoints--;
        applyAttributeEffects(player);
        return true;
    }

    // Getters
    public int getLevel() { return level; }
    public int getExperience() { return experience; }
    public int getSkillPoints() { return skillPoints; }
    public int getVitality() { return vitality; }
    public int getStrength() { return strength; }
    public int getAgility() { return agility; }
    public int getIntelligence() { return intelligence; }

    // Setters
    public void setLevel(int level) { this.level = level; }
    public void setExperience(int experience) { this.experience = experience; }
    public void setSkillPoints(int skillPoints) { this.skillPoints = skillPoints; }
    public void setVitality(int vitality) { this.vitality = vitality; }
    public void setStrength(int strength) { this.strength = strength; }
    public void setAgility(int agility) { this.agility = agility; }
    public void setIntelligence(int intelligence) { this.intelligence = intelligence; }

    public String getVitalityBonus() { return "+" + ((vitality - 1) * 2) + " 生命值"; }
    public String getStrengthBonus() { return "+" + ((strength - 1) * 0.5) + " 攻击伤害"; }
    public String getAgilityBonus() { return "+" + ((agility - 1) * 0.5) + "% 移动速度"; }
    public String getIntelligenceBonus() { return "+" + ((intelligence - 1) * 5) + "% 经验获取"; }

    public float getExperiencePercent() {
        int expToNext = getExpToNextLevel();
        return expToNext > 0 ? (float) experience / expToNext : 0.0f;
    }

    public void increaseStrength() {
        this.strength++;
        this.skillPoints--;
    }

    public void increaseAgility() {
        this.agility++;
        this.skillPoints--;
    }

    public void increaseIntelligence() {
        this.intelligence++;
        this.skillPoints--;
    }

    public void increaseVitality() {
        this.vitality++;
        this.skillPoints--;
    }

    public void fromNBT(CompoundTag tag) {
        this.level = tag.getInt("level");
        this.experience = tag.getInt("experience");
        this.skillPoints = tag.getInt("skillPoints");
        this.vitality = tag.getInt("vitality");
        this.strength = tag.getInt("strength");
        this.agility = tag.getInt("agility");
        this.intelligence = tag.getInt("intelligence");
    }


    public CompoundTag toNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("level", this.level);
        tag.putInt("experience", this.experience);
        tag.putInt("skillPoints", this.skillPoints);
        tag.putInt("vitality", this.vitality);
        tag.putInt("strength", this.strength);
        tag.putInt("agility", this.agility);
        tag.putInt("intelligence", this.intelligence);
        return tag;
    }
}