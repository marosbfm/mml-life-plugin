package sk.maroskomml.lifeplugin;

import net.kyori.adventure.text.TextComponent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

import java.util.Objects;

public class PluginEventsListener implements Listener {
    private final PlayersHandler playersHandler;

    public PluginEventsListener(PlayersHandler playersHandler) {
        this.playersHandler = playersHandler;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        playersHandler.handleJoinEvent(event.getPlayer());
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        playersHandler.handleRespawnEvent(event.getPlayer());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        playersHandler.handleDeathEvent(event.getEntity());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (Objects.nonNull(event.getItem()) &&
                Objects.nonNull(event.getItem().getItemMeta()) &&
                Objects.nonNull(event.getItem().getItemMeta().displayName())) {

            TextComponent textComponent = (TextComponent) event.getItem().getItemMeta().displayName();
            assert textComponent != null;

            if (ItemManager.LIFE_CRYSTAL_NAME.equals(textComponent.content())) {

                playersHandler.handleCrystalUsage(event.getPlayer());

                event.getItem().setAmount(
                        event.getItem().getAmount() - 1
                );
            }

        }
    }

    @EventHandler
    public void onPlayerStatisticIncrementEvent(PlayerStatisticIncrementEvent event) {
        playersHandler.handleStatisticIncrementEvent(event.getPlayer(),
                event);
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event){
        playersHandler.handleQuitEvent(event.getPlayer());
    }

    @EventHandler
    public void onPlayerKickEvent(PlayerKickEvent event){
        playersHandler.handleKickEvent(event.getPlayer());
    }

    @EventHandler
    public void onPlayerExpChangeEvent(PlayerExpChangeEvent event){
        playersHandler.handleExpChangeEvent(event.getPlayer(),event);
    }
}
