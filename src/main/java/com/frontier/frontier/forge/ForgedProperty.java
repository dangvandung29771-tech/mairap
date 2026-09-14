package com.frontier.frontier.forge;

import com.mojang.serialization.Codec;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;

/**
 * The six forging properties. Whetstones (SHARP, BALANCED, HEAVY) and
 * Crystals (PRECISION, BLOOD, VOID) each map to one property. Values follow
 * a diminishing curve so stacking levels past I gives clearly reduced gains.
 */
public enum ForgedProperty implements StringRepresentable {
    SHARP("sharp", ChatFormatting.GRAY, 0xFFE9E9E9),
    BALANCED("balanced", ChatFormatting.AQUA, 0xFF7FD4D4),
    HEAVY("heavy", ChatFormatting.GOLD, 0xFFD8A040),
    PRECISION("precision", ChatFormatting.YELLOW, 0xFFF0E060),
    BLOOD("blood", ChatFormatting.RED, 0xFFE04040),
    VOID("void", ChatFormatting.DARK_PURPLE, 0xFF9040E0);

    public static final int MAX_LEVEL = 3;
    public static final Codec<ForgedProperty> CODEC = StringRepresentable.fromEnum(ForgedProperty::values);

    private final String name;
    private final ChatFormatting color;
    private final int rgb;

    ForgedProperty(String name, ChatFormatting color, int rgb) {
        this.name = name;
        this.color = color;
        this.rgb = rgb;
    }

    @Override
    public String getSerializedName() {
        return name;
    }

    public ChatFormatting color() {
        return color;
    }

    public int rgb() {
        return rgb;
    }

    public boolean isCrystal() {
        return this == PRECISION || this == BLOOD || this == VOID;
    }

    /** Diminishing multiplier for level 1..3: 1.0, 0.6, 0.35. */
    public static float levelWeight(int level) {
        return switch (level) {
            case 1 -> 1.0F;
            case 2 -> 0.6F;
            case 3 -> 0.35F;
            default -> 0.0F;
        };
    }

    public Component displayName(int level) {
        String numeral = switch (level) {
            case 1 -> "I";
            case 2 -> "II";
            case 3 -> "III";
            default -> String.valueOf(level);
        };
        return Component.translatable("forged.frontier." + name, numeral).withStyle(color);
    }
}
