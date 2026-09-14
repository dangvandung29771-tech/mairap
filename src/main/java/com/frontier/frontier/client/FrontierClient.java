package com.frontier.frontier.client;

import com.frontier.frontier.client.gui.AirshipHudLayer;
import com.frontier.frontier.client.gui.MasterGrindstoneScreen;
import com.frontier.frontier.client.gui.RelicScreen;
import com.frontier.frontier.client.particle.ForgeSparkParticle;
import com.frontier.frontier.client.particle.RuneGlowParticle;
import com.frontier.frontier.client.renderer.GrindstoneModel;
import com.frontier.frontier.client.renderer.MasterGrindstoneRenderer;
import com.frontier.frontier.client.renderer.SprinklerModel;
import com.frontier.frontier.client.renderer.SprinklerRenderer;
import com.frontier.frontier.entity.model.AirshipModel;
import com.frontier.frontier.entity.model.CopperShieldSkeletonModel;
import com.frontier.frontier.entity.model.GoldDiggerZombieModel;
import com.frontier.frontier.entity.model.RaftModel;
import com.frontier.frontier.entity.renderer.AirshipRenderer;
import com.frontier.frontier.entity.renderer.CopperShieldSkeletonRenderer;
import com.frontier.frontier.entity.renderer.GoldDiggerZombieRenderer;
import com.frontier.frontier.entity.renderer.RaftRenderer;
import com.frontier.frontier.event.ClientEvents;
import com.frontier.frontier.init.FrontierBlockEntities;
import com.frontier.frontier.init.FrontierEntities;
import com.frontier.frontier.init.FrontierMenus;
import com.frontier.frontier.init.FrontierParticles;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.bus.api.SubscribeEvent;
import com.frontier.frontier.FrontierMod;

/** All client-only registration (mod bus). */
public final class FrontierClient {
    private FrontierClient() {
    }

    public static void init(IEventBus modBus) {
        modBus.addListener(FrontierClient::onRegisterRenderers);
        modBus.addListener(FrontierClient::onRegisterLayerDefinitions);
        modBus.addListener(FrontierClient::onRegisterParticles);
        modBus.addListener(FrontierClient::onRegisterMenuScreens);
        modBus.addListener(FrontierClient::onRegisterKeyMappings);
        modBus.addListener(FrontierClient::onRegisterGuiLayers);
    }

    private static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(FrontierBlockEntities.MASTER_GRINDSTONE.get(), MasterGrindstoneRenderer::new);
        event.registerBlockEntityRenderer(FrontierBlockEntities.SPRINKLER.get(), SprinklerRenderer::new);
        event.registerEntityRenderer(FrontierEntities.LARGE_RAFT.get(), RaftRenderer::new);
        event.registerEntityRenderer(FrontierEntities.POCKET_AIRSHIP.get(), AirshipRenderer::new);
        event.registerEntityRenderer(FrontierEntities.COPPER_SHIELD_SKELETON.get(), CopperShieldSkeletonRenderer::new);
        event.registerEntityRenderer(FrontierEntities.GOLD_DIGGER_ZOMBIE.get(), GoldDiggerZombieRenderer::new);
    }

    private static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(FrontierModelLayers.GRINDSTONE, GrindstoneModel::createLayer);
        event.registerLayerDefinition(FrontierModelLayers.SPRINKLER, SprinklerModel::createLayer);
        event.registerLayerDefinition(FrontierModelLayers.RAFT, RaftModel::createLayer);
        event.registerLayerDefinition(FrontierModelLayers.AIRSHIP, AirshipModel::createLayer);
        event.registerLayerDefinition(FrontierModelLayers.COPPER_SHIELD_SKELETON, CopperShieldSkeletonModel::createBodyLayer);
        event.registerLayerDefinition(FrontierModelLayers.GOLD_DIGGER_ZOMBIE, GoldDiggerZombieModel::createBodyLayer);
    }

    private static void onRegisterParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(FrontierParticles.FORGE_SPARK.get(), ForgeSparkParticle.Provider::new);
        event.registerSpriteSet(FrontierParticles.RUNE_GLOW.get(), RuneGlowParticle.Provider::new);
        event.registerSpriteSet(FrontierParticles.STEAM_PUFF.get(), net.minecraft.client.particle.SmokeParticle.Provider::new);
    }

    private static void onRegisterMenuScreens(RegisterMenuScreensEvent event) {
        event.register(FrontierMenus.MASTER_GRINDSTONE.get(), MasterGrindstoneScreen::new);
        event.register(FrontierMenus.RELIC_MENU.get(), RelicScreen::new);
    }

    private static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(ClientEvents.RELIC_KEY);
    }

    private static void onRegisterGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.HOTBAR, FrontierMod.id("airship_hud"), new AirshipHudLayer());
    }
}
