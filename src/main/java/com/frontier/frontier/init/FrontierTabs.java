package com.frontier.frontier.init;

import com.frontier.frontier.FrontierMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class FrontierTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, FrontierMod.MODID);

    public static final Supplier<CreativeModeTab> FRONTIER_TAB = CREATIVE_TABS.register("frontier",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.frontier"))
                    .icon(() -> new ItemStack(FrontierItems.MASTER_GRINDSTONE_ITEM.get()))
                    .displayItems((parameters, output) -> {
                        // Forging
                        output.accept(FrontierBlocks.MASTER_GRINDSTONE.get());
                        output.accept(FrontierItems.SHARP_WHETSTONE.get());
                        output.accept(FrontierItems.BALANCED_WHETSTONE.get());
                        output.accept(FrontierItems.HEAVY_WHETSTONE.get());
                        output.accept(FrontierItems.PRECISION_CRYSTAL.get());
                        output.accept(FrontierItems.BLOOD_CRYSTAL.get());
                        output.accept(FrontierItems.VOID_CRYSTAL.get());

                        // Ancient materials
                        output.accept(FrontierItems.ANCIENT_SHARD.get());
                        output.accept(FrontierItems.GOLD_FRAGMENT.get());
                        output.accept(FrontierBlocks.ANCIENT_STONE.get());
                        output.accept(FrontierBlocks.ANCIENT_STONE_BRICKS.get());
                        output.accept(FrontierBlocks.CRACKED_ANCIENT_STONE_BRICKS.get());
                        output.accept(FrontierBlocks.CHISELED_ANCIENT_STONE.get());
                        output.accept(FrontierBlocks.ANCIENT_RUNE_BLOCK.get());
                        output.accept(FrontierBlocks.ANCIENT_SECRET_DOOR.get());
                        output.accept(FrontierBlocks.SARCOPHAGUS.get());

                        // Relics
                        output.accept(FrontierItems.CLIMBERS_CLAW.get());
                        output.accept(FrontierItems.FEATHER_OF_GRACE.get());
                        output.accept(FrontierItems.INFERNAL_RING.get());
                        output.accept(FrontierItems.EXPLORER_COMPASS.get());
                        output.accept(FrontierItems.STONEHEART.get());
                        output.accept(FrontierItems.BLOOD_PENDANT.get());

                        // Vehicles
                        output.accept(FrontierItems.LARGE_RAFT.get());
                        output.accept(FrontierItems.POCKET_AIRSHIP.get());
                        output.accept(FrontierItems.COPPER_SHIELD_SKELETON_SPAWN_EGG.get());
                        output.accept(FrontierItems.GOLD_DIGGER_ZOMBIE_SPAWN_EGG.get());

                        // Irrigation & farming
                        output.accept(FrontierBlocks.WOOD_CANAL.get());
                        output.accept(FrontierBlocks.STONE_PIPE.get());
                        output.accept(FrontierBlocks.SPRINKLER.get());
                        output.accept(FrontierBlocks.ENRICHED_FARMLAND.get());
                        output.accept(FrontierItems.COMPOST_CAKE.get());
                    })
                    .build());
}
