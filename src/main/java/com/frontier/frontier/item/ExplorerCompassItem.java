package com.frontier.frontier.item;

import com.frontier.frontier.init.FrontierComponents;
import com.frontier.frontier.world.gen.CryptLocator;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * Attunes to the nearest Ancient Crypt when used. As an equipped relic it
 * whispers the direction every few seconds; in hand it shows the bearing.
 */
public class ExplorerCompassItem extends RelicItem {
    public ExplorerCompassItem(Properties properties) {
        super(properties, false);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide) {
            return InteractionResultHolder.success(stack);
        }
        ServerPlayer serverPlayer = (ServerPlayer) player;
        BlockPos found = CryptLocator.findNearestCrypt((ServerLevel) level, player.blockPosition(), 2500);
        if (found == null) {
            serverPlayer.displayClientMessage(Component.translatable("item.frontier.explorer_compass.not_found")
                    .withStyle(ChatFormatting.RED), true);
        } else {
            stack.set(FrontierComponents.CRYPT_POS.get(), found);
            serverPlayer.displayClientMessage(Component.translatable("item.frontier.explorer_compass.attuned")
                    .withStyle(ChatFormatting.GREEN), true);
            serverPlayer.getCooldowns().addCooldown(this, 100);
        }
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        BlockPos pos = stack.get(FrontierComponents.CRYPT_POS.get());
        if (pos != null) {
            tooltip.add(Component.translatable("item.frontier.explorer_compass.attuned_pos",
                    pos.getX(), pos.getY(), pos.getZ()).withStyle(ChatFormatting.DARK_GREEN));
        } else {
            tooltip.add(Component.translatable("item.frontier.explorer_compass.unattuned").withStyle(ChatFormatting.DARK_GRAY));
        }
    }
}
