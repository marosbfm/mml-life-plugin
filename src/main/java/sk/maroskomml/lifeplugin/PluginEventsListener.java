package sk.maroskomml.lifeplugin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.UUID;

public class PluginEventsListener implements Listener {
    private final Config config;
    private final PlayersHandler playersHandler;

    public PluginEventsListener(Config config, PlayersHandler playersHandler) {
        this.config = config;
        this.playersHandler = playersHandler;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        playersHandler.handleJoin(event.getPlayer());
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event){
        playersHandler.handleRespawn(event.getPlayer());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        playersHandler.handleDeath(event.getEntity());
    }
}
