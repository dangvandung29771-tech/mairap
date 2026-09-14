package com.frontier.frontier.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

/** Base class for the six Frontier relics. Relics are equipped in the two
 *  relic slots of the Relic Pouch (or in Curios charm slots when present). */
public class RelicItem extends Item {
    public static final Rarity RELIC_RARITY = Rarity.create("frontier_relic", ChatFormatting.GOLD);
    public static final Rarity RELIC_RARITY_EPIC = Rarity.create("frontier_relic_epic", ChatFormatting.LIGHT_PURPLE);

    private final boolean epic;

    public RelicItem(Properties properties, boolean epic) {
        super(properties.stacksTo(1).rarity(epic ? RELIC_RARITY_EPIC : RELIC_RARITY));
        this.epic = epic;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable(getDescriptionId() + ".tooltip").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("relic.frontier.type").withStyle(ChatFormatting.DARK_AQUA));
    }
}
