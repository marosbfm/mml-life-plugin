package sk.maroskomml.lifeplugin;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Config {

    private final FileConfiguration fileConfiguration;

    private final int livesCount;
    private final int resetIntervalAmount;
    private final String resetIntervalUnit;

    private final Gson gson = new Gson();
    private final Type playersMapType = new TypeToken<HashMap<String, PlayerLife>>() {
    }.getType();

    private final long resetTs;

    public Config(@NotNull FileConfiguration config) {
        fileConfiguration = config;
        livesCount = Integer.parseInt(
                Objects.requireNonNull(config.getString("mml-life-plugin.lives-count"))
        );
        resetIntervalAmount = Integer.parseInt(
                Objects.requireNonNull(config.getString("mml-life-plugin.reset-interval.amount"))
        );
        resetIntervalUnit = Objects.requireNonNull(config.getString("mml-life-plugin.reset-interval.unit"));
        long ts = config.getLong("mml-life-plugin.reset-interval.ts", 0L);
        if(ts == 0L){
            ts = Instant.now().getEpochSecond();
        }
        resetTs = ts;
    }

    public FileConfiguration getFileConfiguration() {
        return fileConfiguration;
    }

    public int getLivesCount() {
        return livesCount;
    }

    public int getResetIntervalAmount() {
        return resetIntervalAmount;
    }

    public String getResetIntervalUnit() {
        return resetIntervalUnit;
    }

    public long getResetTs() {
        return resetTs;
    }

    public void storePlayers(Map<String, PlayerLife> players) {
        fileConfiguration.set("mml-life-plugin.players", gson.toJson(players));
        LifePlugin.getPlugin(LifePlugin.class).saveConfig();
    }

    public Map<String, PlayerLife> reloadPlayers() {
        String json = fileConfiguration.getString("mml-life-plugin.players");
        if (Objects.nonNull(json)) {
            return gson.fromJson(
                    json,
                    playersMapType
            );
        } else {
            return new HashMap<>();
        }
    }

    @Override
    public String toString() {
        return "Config{" +
                "fileConfiguration=" + fileConfiguration +
                ", livesCount=" + livesCount +
                ", resetIntervalAmount=" + resetIntervalAmount +
                ", resetIntervalUnit='" + resetIntervalUnit + '\'' +
                ", playersMapType=" + playersMapType +
                ", tsLastReset=" + resetTs +
                '}';
    }

    public void livesWasAdded() {
        fileConfiguration.set("mml-life-plugin.reset-interval.ts", Instant.now().getEpochSecond());
        LifePlugin.getPlugin(LifePlugin.class).saveConfig();
    }
}
