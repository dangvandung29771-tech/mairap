package com.frontier.frontier.init;

import com.frontier.frontier.FrontierMod;
import com.frontier.frontier.relic.RelicData;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class FrontierAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, FrontierMod.MODID);

    public static final Supplier<AttachmentType<RelicData>> RELICS = ATTACHMENT_TYPES.register("relics",
            () -> AttachmentType.builder(RelicData::new).serialize(RelicData.CODEC).build());
}
