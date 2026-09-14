package com.frontier.frontier.init;

import com.frontier.frontier.FrontierMod;
import com.frontier.frontier.block.entity.MasterGrindstoneBlockEntity;
import com.frontier.frontier.menu.MasterGrindstoneMenu;
import com.frontier.frontier.menu.RelicMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class FrontierMenus {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(Registries.MENU, FrontierMod.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<MasterGrindstoneMenu>> MASTER_GRINDSTONE =
            MENU_TYPES.register("master_grindstone", () -> IMenuTypeExtension.create((windowId, inv, data) -> {
                var pos = data.readBlockPos();
                var be = inv.player.level().getBlockEntity(pos);
                if (be instanceof MasterGrindstoneBlockEntity grindstone) {
                    return new MasterGrindstoneMenu(windowId, inv, grindstone, pos);
                }
                return new MasterGrindstoneMenu(windowId, inv, new SimpleContainer(3), pos);
            }));

    public static final DeferredHolder<MenuType<?>, MenuType<RelicMenu>> RELIC_MENU =
            MENU_TYPES.register("relic_pouch", () -> new MenuType<>(RelicMenu::new, FeatureFlags.VANILLA_SET));
}
