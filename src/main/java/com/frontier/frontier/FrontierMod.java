package com.frontier.frontier;

import com.frontier.frontier.client.FrontierClient;
import com.frontier.frontier.event.CommonEvents;
import com.frontier.frontier.init.*;
import com.frontier.frontier.network.FrontierPayloads;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

/**
 * FRONTIER — Vanilla+ Expansion
 *
 * A cohesive survival expansion for Minecraft 1.21.1 (NeoForge):
 * weapon forging, ancient crypts, relics, rafts, pocket airships and irrigation.
 */
@Mod(FrontierMod.MODID)
public class FrontierMod {
    public static final String MODID = "frontier";
    public static final Logger LOGGER = LogUtils.getLogger();

    public FrontierMod(IEventBus modBus) {
        FrontierSounds.SOUNDS.register(modBus);
        FrontierComponents.COMPONENTS.register(modBus);
        FrontierAttachments.ATTACHMENT_TYPES.register(modBus);
        FrontierBlocks.BLOCKS.register(modBus);
        FrontierBlockEntities.BLOCK_ENTITY_TYPES.register(modBus);
        FrontierItems.ITEMS.register(modBus);
        FrontierEntities.ENTITY_TYPES.register(modBus);
        FrontierMenus.MENU_TYPES.register(modBus);
        FrontierParticles.PARTICLE_TYPES.register(modBus);
        FrontierStructures.STRUCTURE_TYPES.register(modBus);
        FrontierStructures.STRUCTURE_PIECE_TYPES.register(modBus);
        FrontierTabs.CREATIVE_TABS.register(modBus);

        modBus.addListener(CommonEvents::onAttributeCreation);
        modBus.addListener(FrontierPayloads::onRegisterPayloads);

        if (FMLEnvironment.dist.isClient()) {
            FrontierClient.init(modBus);
        }

        LOGGER.info("FRONTIER loaded — explore, discover, collect, forge, travel.");
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
