package sk.maroskomml.lifeplugin;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PlayersHandler {

    private final Config config;
    private Map<String, PlayerLife> players = new HashMap<>();
    private Map<String, String> nickPlayerIdMap = new HashMap<>();


    public PlayersHandler(Config config) {
        this.config = config;
    }

    public void store() {
        config.storePlayers(players);
    }

    public void reload() {
        players = config.reloadPlayers();
        nickPlayerIdMap = new HashMap<>();
        players.forEach((s, playerLife) -> {
            nickPlayerIdMap.put(playerLife.getNick(), s);
        });
    }

    public void handleJoin(@NotNull Player player) {
        String playerId = player.getUniqueId().toString();
        players.computeIfAbsent(playerId, s -> {
            LifePlugin.getPlugin(LifePlugin.class).getServer().getConsoleSender().sendMessage(
                    Messages.welcome(config, player)
            );
            player.sendMessage(Messages.welcomePlayer(config, player));
            player.sendMessage(Messages.help(config));
            PlayerLife playerLife = new PlayerLife(config, player);
            nickPlayerIdMap.put(playerLife.getNick(), playerLife.getId());
            return playerLife;
        });
        handleRespawn(player);
        store();
    }

    public void handleDeath(Player player) {
        String playerId = player.getUniqueId().toString();
        players.computeIfPresent(playerId, (s, playerLife) -> {
            playerLife.decreaseLife();
            store();
            player.sendMessage(Messages.lives(playerLife.getLifeCount()));
            if (playerLife.isGhost()) {
                player.sendMessage(Messages.ghost());
                player.getServer().broadcastMessage(
                        Messages.globalPlayerIsGhost(player.getName()));
            }
            return playerLife;
        });
    }


    public void handleRespawn(Player player) {
        String playerId = player.getUniqueId().toString();
        players.computeIfPresent(playerId, (s, playerLife) -> {
            if (playerLife.isGhost()) {
                player.setGameMode(GameMode.ADVENTURE);
                player.setGlowing(true);
                player.setInvisible(true);
            } else {
                player.setGameMode(GameMode.SURVIVAL);
                player.setVisualFire(false);
                player.setGlowing(false);
                player.setInvisible(false);
            }
            return playerLife;
        });
    }

    public int getLifeCount(Player player) {
        String playerId = player.getUniqueId().toString();
        return getLifeCountByPlayerId(playerId);
    }

    public int getLifeCountByNick(String playerName) {
        String playerId = nickPlayerIdMap.get(playerName);
        return getLifeCountByPlayerId(playerId);
    }

    public int getLifeCountByPlayerId(String playerId) {
        int result = 0;
        if (players.containsKey(playerId)) {
            result = players.get(playerId).getLifeCount();
        }
        return result;
    }

    public boolean knownPlayer(String playerNick) {
        return nickPlayerIdMap.containsKey(playerNick);
    }

    public void addLifeByNick(String receiverNick, int count) {
        String playerId = nickPlayerIdMap.get(receiverNick);
        if (players.containsKey(playerId)) {
            players.get(playerId).addLife(count);
            store();
        }
    }

    public void setLife(String playerId, int count) {
        if (players.containsKey(playerId)) {
            players.get(playerId).setLife(count);
            store();
        }
    }

    public boolean isGhost(Player player) {
        String playerId = player.getUniqueId().toString();
        if (!players.containsKey(playerId)) {
            return false;
        }
        return players.get(playerId).isGhost();
    }

    public void addLives() {
        players.forEach((s, playerLife) -> {
                    playerLife.addLife(config.getLivesCount());
                    Player player = LifePlugin.getPlugin(LifePlugin.class).getServer().getPlayer(playerLife.getNick());
                    if(Objects.nonNull(player)){
                        player.sendMessage(Messages.myLife(playerLife.getLifeCount()));
                        handleRespawn(player);
                    }
        }
        );
        store();
    }
}
