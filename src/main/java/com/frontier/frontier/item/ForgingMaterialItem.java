package com.frontier.frontier.item;

import com.frontier.frontier.forge.ForgedProperty;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

/** Whetstones and crystals consumed by the Master Grindstone. */
public class ForgingMaterialItem extends Item {
    private final ForgedProperty property;

    public ForgingMaterialItem(Properties properties, ForgedProperty property) {
        super(properties);
        this.property = property;
    }

    public ForgedProperty property() {
        return property;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable(getDescriptionId() + ".tooltip").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("material.frontier.grants", property.displayName(1)).withStyle(ChatFormatting.DARK_GRAY));
    }
}
