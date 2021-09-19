package sk.maroskomml.lifeplugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

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
        players = config.readPlayers();
        nickPlayerIdMap = new HashMap<>();
        players.forEach((s, playerLife) -> nickPlayerIdMap.put(playerLife.getNick(), s));
    }

    public void handleJoinEvent(Player player) {
        String playerId = getPlayerId(player);

        players.computeIfAbsent(playerId, s -> {
            player.getServer().broadcast(Messages.globalNewPlayer(player));
            player.sendMessage(Messages.welcomePlayer(config, player));
            player.sendMessage(Messages.help(config));
            PlayerLife playerLife = new PlayerLife(config, player);
            nickPlayerIdMap.put(playerLife.getNick(), playerLife.getId());
            return playerLife;
        });
        reflectPlayerGameState(player);
        store();
    }

    public void handleDeathEvent(Player player) {
        handleLifeChange(player, -1);
    }


    public void handleRespawnEvent(Player player) {
        reflectPlayerGameState(player);
    }

    public void handleCrystalUsage(Player player) {
        player.sendMessage(Messages.useCrystal());
        handleLifeChange(player, 1);
        reflectPlayerGameState(player);
    }

    public int getLifeCount(Player player) {
        return getPlayerLife(player).getLifeCount();
    }

    public int getLifeCountByNick(String playerName) {
        int result = 0;
        String playerId = nickPlayerIdMap.get(playerName);
        if (players.containsKey(playerId)) {
            result = players.get(playerId).getLifeCount();
        }
        return result;
    }

    public boolean knownPlayer(String playerNick) {
        return nickPlayerIdMap.containsKey(playerNick);
    }


    public void setLife(Player player, int count) {
        handleLifeChange(player, count, true);
        reflectPlayerGameState(player);
    }

    public void addLiveForAll() {
        players.forEach((s, playerLife) -> {
                    int liveCountToAdd = config.getLivesCount();
                    Player player = LifePlugin.getPlugin(LifePlugin.class).getServer().getPlayer(playerLife.getNick());

                    if (Objects.nonNull(player)) {
                        handleLifeChange(player,liveCountToAdd);
                    }else {
                        changePlayerLife(playerLife, liveCountToAdd);
                    }
                }
        );
    }

    public void setLifeByNick(String receiverNick, @Nullable Player receiver, int count) {
        String playerId = nickPlayerIdMap.get(receiverNick);

        if (Objects.nonNull(receiver)) {
            handleLifeChange(receiver,count,true);
        }else if(players.containsKey(playerId)){
            changePlayerLife(players.get(playerId),count, true);
        }
    }

    public void addLifeByNick(String receiverNick, Player receiver, int count) {
        String playerId = nickPlayerIdMap.get(receiverNick);

        if (Objects.nonNull(receiver)) {
            handleLifeChange(receiver,count);
        }else if(players.containsKey(playerId)){
            changePlayerLife(players.get(playerId),count);
        }
    }

    private void reflectPlayerGameState(Player player) {
        String playerId = getPlayerId(player);
        players.computeIfPresent(playerId, (s, playerLife) -> {
            if (playerLife.isGhost()) {
                player.setGameMode(GameMode.ADVENTURE);
                player.setGlowing(true);
                player.setInvisible(true);
            } else {
                player.setGameMode(GameMode.SURVIVAL);
                player.setGlowing(false);
                player.setInvisible(false);
            }
            return playerLife;
        });
    }

    private void handleLifeChange(Player player, int change){
        handleLifeChange(player,change,false);
    }

    private void handleLifeChange(Player player, int change, boolean setInsteadAdd) {
        PlayerLife playerLife = getPlayerLife(player);
        changePlayerLife(playerLife, change, setInsteadAdd);
        sendMessagesAboutChange(player, playerLife);
    }
    private void changePlayerLife(PlayerLife playerLife, int change) {
        changePlayerLife(playerLife,change,false);
    }
    private void changePlayerLife(PlayerLife playerLife, int change, boolean setInsteadAdd) {
        if (setInsteadAdd) {
            playerLife.setLife(change);
        } else {
            playerLife.addLife(change);
        }
        store();
    }

    private void sendMessagesAboutChange(Player player,PlayerLife playerLife) {
        player.sendMessage(Messages.lives(playerLife.getLifeCount()));
        if (playerLife.isGhost()) {
            player.sendMessage(Messages.ghost());
            player.showTitle(Title.title(Messages.ghost(), Component.empty()));
            player.getServer().broadcast(Messages.globalPlayerIsGhost(player.getName()));
        }else if(playerLife.isCameToLife()){
            player.sendMessage(Messages.cameToLife(playerLife.getLifeCount()));
            player.showTitle(Title.title(Messages.titleCameToLife(), Component.empty()));
            player.getServer().broadcast(Messages.globalCameToLife(player.getName()));
        }else if(playerLife.getLifeCount() == 1){
            player.sendMessage(Messages.warningLife(playerLife.getLifeCount()));
        }
    }

    private String getPlayerId(Player player) {
        return player.getUniqueId().toString();
    }

    private PlayerLife getPlayerLife(Player player) {
        String playerId = getPlayerId(player);
        return players.computeIfAbsent(playerId, s -> {
            PlayerLife playerLife = new PlayerLife(config, player);
            nickPlayerIdMap.put(playerLife.getNick(), playerLife.getId());
            return playerLife;
        });
    }
}
