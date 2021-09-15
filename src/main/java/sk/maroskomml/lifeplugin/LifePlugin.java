package sk.maroskomml.lifeplugin;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

public class LifePlugin extends JavaPlugin {
    private Config config = null;
    private PlayersHandler playersHandler = null;

    @Override
    public void onEnable() {
        super.onEnable();
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        config = new Config(getConfig());
        playersHandler = new PlayersHandler(config);
        playersHandler.reload();
        PluginEventsListener pluginEventsListener = new PluginEventsListener(config, playersHandler);
        PluginCommandExecutor pluginCommandExecutor = new PluginCommandExecutor(config, playersHandler);

        getServer().getPluginManager().registerEvents(pluginEventsListener, this);
        getCommand("mmlmylife").setExecutor(pluginCommandExecutor);
        getCommand("mmllife").setExecutor(pluginCommandExecutor);
        getCommand("mmlgivelife").setExecutor(pluginCommandExecutor);


        BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                if(shouldAddLives(config)){
                    playersHandler.addLives();
                    config.livesWasAdded();
                }
            }
        }, 5*60*20L, 60*20L); // every minute 1 second ~= 20ticks

        getServer().getConsoleSender().sendMessage(Messages.pluginEnabled());
        getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "Config: " + config.toString());
    }

    private boolean shouldAddLives(Config config) {
        Instant reset = Instant.ofEpochSecond(config.getResetTs());
        Instant next = reset.plus(
                config.getResetIntervalAmount(),
                ChronoUnit.valueOf(config.getResetIntervalUnit())
        );
        return Instant.now().isAfter(next);
    }

    @Override
    public void onDisable() {
        playersHandler.store();
        super.onDisable();
    }
}
