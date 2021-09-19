package sk.maroskomml.lifeplugin;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

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
        PluginEventsListener pluginEventsListener = new PluginEventsListener(playersHandler);
        PluginCommands pluginCommands = new PluginCommands(config, playersHandler);

        ItemManager.init();
        getServer().getPluginManager().registerEvents(pluginEventsListener, this);
        registerCommands(pluginCommands);
        createSchedulerForRegularLifeAdding();

        getServer().getConsoleSender().sendMessage(Messages.consolePluginEnabled());
    }

    @Override
    public void onDisable() {
        playersHandler.store();
        super.onDisable();
    }

    private void registerCommands(PluginCommands pluginCommands) {
        setCommandExecutor(PluginCommands.COMMAND_HELP, pluginCommands);
        setCommandExecutor(PluginCommands.COMMAND_MY_LIFE, pluginCommands);
        setCommandExecutor(PluginCommands.COMMAND_LIFE, pluginCommands);
        setCommandExecutor(PluginCommands.COMMAND_GIVE_LIFE, pluginCommands);
        setCommandExecutor(PluginCommands.COMMAND_SET_LIFE, pluginCommands);
    }
    
    private void setCommandExecutor(String commandName, CommandExecutor commandExecutor){
        PluginCommand pluginCommand = getCommand(commandName);
        if(Objects.isNull(pluginCommand)){
            throw new RuntimeException("Unknown command: " + commandName);
        }
        pluginCommand.setExecutor(commandExecutor);
    }

    private void createSchedulerForRegularLifeAdding() {
        BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, () -> {
            if(shouldAddLives(config)){
                getServer().broadcast(Messages.globalAllPlayersLivesAdd(config.getLivesCount()));
                playersHandler.addLiveForAll();
                config.updateResetIntervalTs();
            }
        }, 60*20L, 60*20L); // every minute 1 second ~= 20ticks
    }

    private boolean shouldAddLives(Config config) {
        Instant reset = Instant.ofEpochSecond(config.getResetTs());
        Instant next = reset.plus(
                config.getResetIntervalAmount(),
                ChronoUnit.valueOf(config.getResetIntervalUnit())
        );
        return Instant.now().isAfter(next);
    }
}
