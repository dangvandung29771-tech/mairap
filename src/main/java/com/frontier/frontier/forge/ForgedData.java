package com.frontier.frontier.forge;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.ArrayList;
import java.util.List;

/**
 * Immutable list of forged properties stored on a weapon via the
 * {@code frontier:forged} data component.
 */
public record ForgedData(List<Entry> entries) {
    public static final ForgedData EMPTY = new ForgedData(List.of());

    public static final Codec<ForgedData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Entry.CODEC.listOf().fieldOf("properties").forGetter(ForgedData::entries)
    ).apply(instance, ForgedData::new));

    public static final StreamCodec<ByteBuf, ForgedData> STREAM_CODEC = StreamCodec.composite(
            Entry.STREAM_CODEC.apply(ByteBufCodecs.list()),
            ForgedData::entries,
            ForgedData::new
    );

    public record Entry(ForgedProperty property, int level) {
        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ForgedProperty.CODEC.fieldOf("property").forGetter(Entry::property),
                Codec.intRange(1, ForgedProperty.MAX_LEVEL).fieldOf("level").forGetter(Entry::level)
        ).apply(instance, Entry::new));

        public static final StreamCodec<ByteBuf, Entry> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, entry -> entry.property().ordinal(),
                ByteBufCodecs.VAR_INT, Entry::level,
                (ord, level) -> new Entry(ForgedProperty.values()[ord % ForgedProperty.values().length], level)
        );
    }

    public boolean isEmpty() {
        return entries.isEmpty();
    }

    public int levelOf(ForgedProperty property) {
        for (Entry entry : entries) {
            if (entry.property() == property) {
                return entry.level();
            }
        }
        return 0;
    }

    public ForgedProperty dominant() {
        return entries.isEmpty() ? null : entries.get(entries.size() - 1).property();
    }

    /** Returns a copy of this data with the given property raised to {@code level}. */
    public ForgedData withLevel(ForgedProperty property, int level) {
        List<Entry> list = new ArrayList<>();
        boolean replaced = false;
        for (Entry entry : entries) {
            if (entry.property() == property) {
                list.add(new Entry(property, level));
                replaced = true;
            } else {
                list.add(entry);
            }
        }
        if (!replaced) {
            list.add(new Entry(property, level));
        }
        return new ForgedData(List.copyOf(list));
    }
}
