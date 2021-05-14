package com.github.shaneyu.playground.lib.datagen.sound;

import com.github.shaneyu.playground.lib.datagen.DataConstants;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.util.ResourceLocation;

public class SoundBuilder {
    private final ResourceLocation location;
    private float volume = 1;
    private float pitch = 1;
    private int weight = 1;
    private boolean stream;
    private int attenuationDistance = 16;
    private boolean preload;
    private SoundType type = SoundType.SOUND;

    SoundBuilder(ResourceLocation location) {
        this.location = location;
    }

    public ResourceLocation getLocation() {
        return location;
    }

    public SoundBuilder volume(float volume) {
        if (volume < 0 || volume > 1) {
            throw new RuntimeException("Error volume for sound: '" + serializeLoc() + "' must be between 0.0 and 1.0 inclusive.");
        }

        this.volume = volume;

        return this;
    }

    public SoundBuilder pitch(float pitch) {
        this.pitch = pitch;

        return this;
    }

    public SoundBuilder weight(int weight) {
        if (weight < 1) {
            throw new RuntimeException("Error weight for sound: '" + serializeLoc() + "' must be at least 1.");
        }

        this.weight = weight;

        return this;
    }

    public SoundBuilder stream() {
        this.stream = true;

        return this;
    }

    public SoundBuilder attenuationDistance(int attenuationDistance) {
        if (attenuationDistance < 1) {
            throw new RuntimeException("Error attenuation distance for sound: '" + serializeLoc() + "' must be at least 1.");
        }

        this.attenuationDistance = attenuationDistance;

        return this;
    }

    public SoundBuilder preload() {
        this.preload = true;

        return this;
    }

    public SoundBuilder type(SoundType type) {
        this.type = type;

        return this;
    }

    public JsonElement toJson() {
        JsonObject jsonObject = new JsonObject();
        boolean hasSettings = false;

        if (volume != 1) {
            jsonObject.addProperty(DataConstants.VOLUME, volume);
            hasSettings = true;
        }

        if (pitch != 1) {
            jsonObject.addProperty(DataConstants.PITCH, pitch);
            hasSettings = true;
        }

        if (weight != 1) {
            jsonObject.addProperty(DataConstants.WEIGHT, weight);
            hasSettings = true;
        }

        if (stream) {
            jsonObject.addProperty(DataConstants.STREAM, true);
            hasSettings = true;
        }

        if (attenuationDistance != 16) {
            jsonObject.addProperty(DataConstants.ATTENUATION_DISTANCE, attenuationDistance);
            hasSettings = true;
        }

        if (preload) {
            jsonObject.addProperty(DataConstants.PRELOAD, true);
            hasSettings = true;
        }

        if (type != SoundType.SOUND) {
            jsonObject.addProperty(DataConstants.TYPE, type.value);
            hasSettings = true;
        }

        if (hasSettings) {
            jsonObject.addProperty(DataConstants.NAME, serializeLoc());

            return jsonObject;
        }

        return new JsonPrimitive(serializeLoc());
    }

    private String serializeLoc() {
        if (location.getNamespace().equals("minecraft")) {
            return location.getPath();
        }

        return location.toString();
    }

    public enum SoundType {
        SOUND("sound"),
        EVENT("event");

        private final String value;

        SoundType(String value) {
            this.value = value;
        }
    }
}
