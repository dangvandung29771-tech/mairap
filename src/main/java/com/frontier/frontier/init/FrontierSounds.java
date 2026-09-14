package com.frontier.frontier.init;

import com.frontier.frontier.FrontierMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class FrontierSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(Registries.SOUND_EVENT, FrontierMod.MODID);

    public static final Supplier<SoundEvent> GRINDSTONE_GRIND = sound("block.master_grindstone.grind");
    public static final Supplier<SoundEvent> GRINDSTONE_IMPACT = sound("block.master_grindstone.impact");
    public static final Supplier<SoundEvent> FORGE_COMPLETE = sound("block.master_grindstone.complete");
    public static final Supplier<SoundEvent> CRYPT_AMBIENT = sound("ambient.crypt");
    public static final Supplier<SoundEvent> CRYPT_DOOR = sound("block.crypt_door.open");
    public static final Supplier<SoundEvent> RUNE_ACTIVATE = sound("block.rune.activate");
    public static final Supplier<SoundEvent> RAFT_CREAK = sound("entity.large_raft.creak");
    public static final Supplier<SoundEvent> RAFT_WATER = sound("entity.large_raft.water");
    public static final Supplier<SoundEvent> AIRSHIP_ENGINE = sound("entity.pocket_airship.engine");
    public static final Supplier<SoundEvent> AIRSHIP_STEAM = sound("entity.pocket_airship.steam");
    public static final Supplier<SoundEvent> RELIC_ACTIVATE = sound("item.relic.activate");
    public static final Supplier<SoundEvent> SPRINKLER_SPRAY = sound("block.sprinkler.spray");

    private static Supplier<SoundEvent> sound(String name) {
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(FrontierMod.id(name)));
    }
}
