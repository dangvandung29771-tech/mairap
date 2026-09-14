package com.frontier.frontier.network;

import com.frontier.frontier.FrontierMod;
import com.frontier.frontier.block.entity.MasterGrindstoneBlockEntity;
import com.frontier.frontier.entity.AirshipEntity;
import com.frontier.frontier.menu.RelicMenu;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.network.NetworkHooks;

/** Client → server payloads. All gameplay mutations happen server-side. */
public final class FrontierPayloads {
    private FrontierPayloads() {
    }

    public record ForgeButtonPayload(BlockPos pos) implements CustomPacketPayload {
        public static final Type<ForgeButtonPayload> TYPE =
                new Type<>(FrontierMod.id("forge_button"));
        public static final StreamCodec<ByteBuf, ForgeButtonPayload> STREAM_CODEC =
                StreamCodec.composite(ByteBufCodecs.BLOCK_POS, ForgeButtonPayload::pos, ForgeButtonPayload::new);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public record OpenRelicPayload() implements CustomPacketPayload {
        public static final Type<OpenRelicPayload> TYPE =
                new Type<>(FrontierMod.id("open_relics"));
        public static final StreamCodec<ByteBuf, OpenRelicPayload> STREAM_CODEC =
                StreamCodec.unit(new OpenRelicPayload());

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public record AirshipInputPayload(float forward, float turn, boolean ascend, boolean descend) implements CustomPacketPayload {
        public static final Type<AirshipInputPayload> TYPE =
                new Type<>(FrontierMod.id("airship_input"));
        public static final StreamCodec<ByteBuf, AirshipInputPayload> STREAM_CODEC =
                StreamCodec.composite(
                        ByteBufCodecs.FLOAT, AirshipInputPayload::forward,
                        ByteBufCodecs.FLOAT, AirshipInputPayload::turn,
                        ByteBufCodecs.BOOL, AirshipInputPayload::ascend,
                        ByteBufCodecs.BOOL, AirshipInputPayload::descend,
                        AirshipInputPayload::new);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public static void onRegisterPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(ForgeButtonPayload.TYPE, ForgeButtonPayload.STREAM_CODEC, FrontierPayloads::handleForge);
        registrar.playToServer(OpenRelicPayload.TYPE, OpenRelicPayload.STREAM_CODEC, FrontierPayloads::handleOpenRelics);
        registrar.playToServer(AirshipInputPayload.TYPE, AirshipInputPayload.STREAM_CODEC, FrontierPayloads::handleAirshipInput);
    }

    private static void handleForge(ForgeButtonPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player
                    && player.containerMenu instanceof com.frontier.frontier.menu.MasterGrindstoneMenu
                    && player.level().getBlockEntity(payload.pos()) instanceof MasterGrindstoneBlockEntity grindstone
                    && grindstone.stillValid(player)) {
                grindstone.tryForge(player);
            }
        });
    }

    private static void handleOpenRelics(OpenRelicPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                NetworkHooks.openScreen(player, new SimpleMenuProvider(
                        (id, inv, p) -> new RelicMenu(id, inv),
                        Component.translatable("container.frontier.relics")));
            }
        });
    }

    private static void handleAirshipInput(AirshipInputPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player && player.getVehicle() instanceof AirshipEntity airship) {
                airship.setInput(payload.forward(), payload.turn(), payload.ascend(), payload.descend());
            }
        });
    }
}
