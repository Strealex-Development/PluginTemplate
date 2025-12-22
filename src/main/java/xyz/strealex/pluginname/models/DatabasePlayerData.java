package xyz.strealex.pluginname.models;

import com.google.gson.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DatabasePlayerData {

    public static final Gson GSON = new GsonBuilder()
        .enableComplexMapKeySerialization()
        .create();

    private JsonObject json;

    public DatabasePlayerData(@Nullable String jsonString) {
        if (jsonString == null) {
            this.json = new JsonObject();
            return;
        }

        try {
            JsonElement elem = JsonParser.parseString(jsonString);
            this.json = elem.isJsonObject() ? elem.getAsJsonObject() : new JsonObject();
        } catch (Exception ignored) {
            this.json = new JsonObject();
        }
    }

    public void setInt(@NotNull String key, int value) {
        json.addProperty(key, value);
    }

    public int getInt(@NotNull String key) {
        JsonElement element = json.get(key);
        return (element != null && !element.isJsonNull()) ? element.getAsInt() : 0;
    }

    public void setString(@NotNull String key, @Nullable String value) {
        json.addProperty(key, value);
    }

    public @Nullable String getString(@NotNull String key) {
        JsonElement element = json.get(key);
        return (element != null && !element.isJsonNull()) ? element.getAsString() : null;
    }

    public void setBoolean(@NotNull String key, boolean value) {
        json.addProperty(key, value);
    }

    public boolean getBoolean(@NotNull String key) {
        JsonElement element = json.get(key);
        return (element != null && !element.isJsonNull()) && element.getAsBoolean();
    }


    public String toJson() {
        return GSON.toJson(json);
    }
}
