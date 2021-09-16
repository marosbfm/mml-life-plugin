package sk.maroskomml.lifeplugin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

import java.util.Objects;
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

    @EventHandler
    public void onConsume(PlayerInteractEvent event){
        if(Objects.nonNull(event.getItem()) && Objects.nonNull(event.getItem().getItemMeta())) {
            if("LIFE CRYSTAL".equals(event.getItem().getItemMeta().getDisplayName())){
                event.getPlayer().sendMessage(Messages.useCrystal());

                playersHandler.addLifeByNick(event.getPlayer().getName(),1);
                playersHandler.handleRespawn(event.getPlayer());
                event.getPlayer().sendMessage(Messages.lives(playersHandler.getLifeCount(event.getPlayer())));

                event.getItem().setAmount(
                        event.getItem().getAmount() - 1
                );
            }
        }
    }
}
