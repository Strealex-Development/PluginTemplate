package xyz.strealex.pluginname.models;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@Getter
public class DatabasePlayer {

    private final UUID uuid;
    private final DatabasePlayerData data;

    private transient boolean dirty;

    public DatabasePlayer(@NotNull UUID uuid, @Nullable String jsonData) {
        this(uuid, new DatabasePlayerData(jsonData));
    }

    public DatabasePlayer(@NotNull UUID uuid) {
        this(uuid, new DatabasePlayerData(null));
    }

    public DatabasePlayer(@NotNull UUID uuid, @NotNull DatabasePlayerData data) {
        this.uuid = uuid;
        this.data = data;
        this.dirty = false;
    }

    private void markDirty() {
        this.dirty = true;
    }

    public void markClean() {
        this.dirty = false;
    }

    public int getKills() {
        return data.getInt("kills");
    }

    public String getName() {
        return data.getString("name");
    }

    public void setName(String name) {
        data.setString("name", name);
        markDirty();
    }

    public String serialize() {
        return data.toJson();
    }
}
