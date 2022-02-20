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

    private static final String MML_LIFE_PLUGIN_LIVES_COUNT = "mml-life-plugin.lives-count";
    private static final String MML_LIFE_PLUGIN_LIFE_ADDING_CYCLE_AMOUNT = "mml-life-plugin.life-adding-cycle.amount";
    private static final String MML_LIFE_PLUGIN_LIFE_ADDING_CYCLE_UNIT = "mml-life-plugin.life-adding-cycle.unit";
    private static final String MML_LIFE_PLUGIN_LIFE_ADDING_CYCLE_TS = "mml-life-plugin.life-adding-cycle.ts";

    private final FileConfiguration fileConfiguration;

    private final int livesCount;
    private final int lifeAddingCycleAmount;
    private final String lifeAddingCycleUnit;

    private final long lastLifeAddingCycleTs;

    public Config(@NotNull FileConfiguration config) {
        fileConfiguration = config;
        livesCount = Integer.parseInt(
                Objects.requireNonNull(config.getString(MML_LIFE_PLUGIN_LIVES_COUNT))
        );
        lifeAddingCycleAmount = Integer.parseInt(
                Objects.requireNonNull(config.getString(MML_LIFE_PLUGIN_LIFE_ADDING_CYCLE_AMOUNT))
        );
        lifeAddingCycleUnit = Objects.requireNonNull(config.getString(MML_LIFE_PLUGIN_LIFE_ADDING_CYCLE_UNIT));
        long ts = config.getLong(MML_LIFE_PLUGIN_LIFE_ADDING_CYCLE_TS, 0L);
        if(ts == 0L){
            updateResetIntervalTs();
            ts = config.getLong(MML_LIFE_PLUGIN_LIFE_ADDING_CYCLE_TS, 0L);
        }
        lastLifeAddingCycleTs = ts;
    }

    public int getLivesCount() {
        return livesCount;
    }

    public int getLifeAddingCycleAmount() {
        return lifeAddingCycleAmount;
    }

    public String getLifeAddingCycleUnit() {
        return lifeAddingCycleUnit;
    }

    public long getLastLifeAddingCycleTs() {
        return lastLifeAddingCycleTs;
    }

    public void updateResetIntervalTs() {
        fileConfiguration.set(MML_LIFE_PLUGIN_LIFE_ADDING_CYCLE_TS, Instant.now().getEpochSecond());
        LifePlugin.getPlugin(LifePlugin.class).saveConfig();
    }

    @Override
    public String toString() {
        return "Config{" +
                "fileConfiguration=" + fileConfiguration +
                ", livesCount=" + livesCount +
                ", lifeAddingCycleAmount=" + lifeAddingCycleAmount +
                ", lifeAddingCycleUnit='" + lifeAddingCycleUnit + '\'' +
                ", lastLifeAddingCycleTs=" + lastLifeAddingCycleTs +
                '}';
    }
}
