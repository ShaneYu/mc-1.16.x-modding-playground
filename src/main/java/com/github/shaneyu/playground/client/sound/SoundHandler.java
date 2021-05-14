package com.github.shaneyu.playground.client.sound;

import com.github.shaneyu.playground.Playground;
import com.github.shaneyu.playground.common.config.PlaygroundConfig;
import com.github.shaneyu.playground.common.tile.interfaces.ITileSound;
import com.github.shaneyu.playground.common.util.WorldUtil;
import com.github.shaneyu.playground.lib.registration.registries.SoundEventRegistryObject;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.client.event.sound.SoundSetupEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.concurrent.ThreadLocalRandom;

public final class SoundHandler {
    private SoundHandler() {}

    private static final Long2ObjectMap<ISound> soundMap = new Long2ObjectOpenHashMap<>();
    private static SoundEngine soundEngine;

    public static void playSound(SoundEventRegistryObject<?> soundEventRO) {
        playSound(soundEventRO.get());
    }

    public static void playSound(SoundEvent sound) {
        playSound(SimpleSound.master(sound, 1, PlaygroundConfig.client.baseSoundVolume.get()));
    }

    public static void playSound(ISound sound) {
        Minecraft.getInstance().getSoundHandler().play(sound);
    }

    public static ISound startTileSound(SoundEvent soundEvent, SoundCategory category, float volume, BlockPos pos) {
        // First, check to see if there's already a sound playing at the desired location
        ISound s = soundMap.get(pos.toLong());

        if (s == null || !Minecraft.getInstance().getSoundHandler().isPlaying(s)) {
            // No sound playing, start one up - we assume that tile sounds will play until explicitly stopped
            // The TileTickableSound will then periodically poll to see if the volume should be adjusted
            s = new TileTickableSound(soundEvent, category, pos, volume);

            if (!isClientPlayerInRange(s)) {
                // If the player is not in range of the sound the tile would play, don't start it
                return null;
            }

            // Start the sound
            playSound(s);

            s = soundMap.get(pos.toLong());
        }

        return s;
    }

    public static void stopTileSound(BlockPos pos) {
        long posKey = pos.toLong();
        ISound s = soundMap.get(posKey);

        if (s != null) {
            Minecraft.getInstance().getSoundHandler().stop(s);
            soundMap.remove(posKey);
        }
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private static boolean isClientPlayerInRange(ISound sound) {
        if (sound.isGlobal() || sound.getAttenuationType() == ISound.AttenuationType.NONE) {
            // If the sound is global or has no attenuation, then return that the player is in range
            return true;
        }

        PlayerEntity player = Minecraft.getInstance().player;

        if (player == null) {
            return false;
        }

        Sound s = sound.getSound();

        // noinspection ConstantConditions
        if (s == null) {
            // If the sound hasn't been initialized yet for some reason try initializing it
            sound.createAccessor(Minecraft.getInstance().getSoundHandler());
            s = sound.getSound();
        }

        // Attenuation distance, defaults to 16 blocks
        int attenuationDistance = s.getAttenuationDistance();

        // Scale the distance based on the sound's volume
        float scaledDistance = Math.max(sound.getVolume(), 1) * attenuationDistance;

        // Check if the player is within range of hearing the sound
        return player.getPositionVec().squareDistanceTo(sound.getX(), sound.getY(), sound.getZ()) < scaledDistance * scaledDistance;
    }

    @SubscribeEvent
    public static void onSoundEngineSetup(SoundSetupEvent event) {
        if (soundEngine == null) {
            soundEngine = event.getManager();
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onTilePlaySound(PlaySoundEvent event) {
        ISound resultSound = event.getResultSound();

        if (resultSound == null) {
            return;
        }

        ResourceLocation soundLoc = event.getSound().getSoundLocation();

        if (!soundLoc.getNamespace().startsWith(Playground.MOD_ID)) {
            return;
        }

        if (event.getName().startsWith("tile.")) {
            BlockPos pos = new BlockPos(resultSound.getX() - 0.5, resultSound.getY() - 0.5, resultSound.getZ() - 0.5);
            soundMap.put(pos.toLong(), resultSound);
        }
    }

    private static class TileTickableSound extends TickableSound {

        private final float originalVolume;

        // Choose an interval between 60-80 ticks (3-4 seconds) to check for muffling changes. We do this
        // to ensure that not every tile sound tries to run on the same tick and thus create
        // uneven spikes of CPU usage
        private final int checkInterval = 20 + ThreadLocalRandom.current().nextInt(20);

        TileTickableSound(SoundEvent soundEvent, SoundCategory category, BlockPos pos, float volume) {
            super(soundEvent, category);
            // Keep track of our original volume
            this.originalVolume = volume * PlaygroundConfig.client.baseSoundVolume.get();
            this.x = pos.getX() + 0.5F;
            this.y = pos.getY() + 0.5F;
            this.z = pos.getZ() + 0.5F;

            // Hold off on setting volume until after we set the position
            this.volume = this.originalVolume * getTileVolumeFactor();
            this.repeat = true;
            this.repeatDelay = 0;
        }

        @Override
        public void tick() {
            // noinspection ConstantConditions
            if (Minecraft.getInstance().world.getGameTime() % checkInterval == 0) {
                if (!isClientPlayerInRange(this)) {
                    // If the player is not in range of hearing this sound any more; go ahead and shutdown
                    finishPlaying();
                    return;
                }

                // Make sure we set our volume back to what it actually would be for purposes of letting other mods know what volume to use
                volume = originalVolume;
                ISound s = ForgeHooksClient.playSound(soundEngine, this);

                if (s == this) {
                    // No filtering done, use the original sound's volume
                    volume = originalVolume * getTileVolumeFactor();
                } else if (s == null) {
                    // Full on mute; go ahead and shutdown
                    finishPlaying();
                } else {
                    // Altered sound returned; adjust volume
                    volume = s.getVolume() * getTileVolumeFactor();
                }
            }
        }

        private float getTileVolumeFactor() {
            TileEntity tile = WorldUtil.getTileEntity(Minecraft.getInstance().world, new BlockPos(getX(), getY(), getZ()));
            float retVolume = 1.0F;

            if (tile instanceof ITileSound) {
                retVolume *= ((ITileSound) tile).getVolume();
            }

            return retVolume;
        }

        @Override
        public float getVolume() {
            if (this.sound == null) {
                this.createAccessor(Minecraft.getInstance().getSoundHandler());
            }

            return super.getVolume();
        }

        @Override
        public boolean canBeSilent() {
            return true;
        }
    }
}
