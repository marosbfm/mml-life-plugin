package sk.maroskomml.lifeplugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;
import org.jetbrains.annotations.Nullable;
import sk.maroskomml.lifeplugin.model.Storage;
import sk.maroskomml.lifeplugin.model.TopCategory;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PlayersHandler {

    private final Config config;
    private final Storage storage;
    private final Map<String, PlayerEntity> players = new HashMap<>();
    private final Map<String, String> nickPlayerIdMap = new HashMap<>();

    public PlayersHandler(Config config, Storage storage) {
        this.config = config;
        this.storage = storage;
    }

    public void handleJoinEvent(Player player) {
        String playerMcId = getPlayerId(player);
        loadPlayerEntity(player, playerMcId);

        reflectPlayerGameState(player);
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
        return getPlayerEntity(player).getLifeCount();
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
                        handleLifeChange(player, liveCountToAdd);
                        reflectPlayerGameState(player);
                    } else {
                        changePlayerLife(playerLife, liveCountToAdd);
                    }
                }
        );
    }

    public void setLifeByNick(String receiverNick, @Nullable Player receiver, int count) {
        String playerId = nickPlayerIdMap.get(receiverNick);

        if (Objects.nonNull(receiver)) {
            handleLifeChange(receiver, count, true);
            reflectPlayerGameState(receiver);
        } else if (players.containsKey(playerId)) {
            changePlayerLife(players.get(playerId), count, true);
        }
    }

    public void addLifeByNick(String receiverNick, Player receiver, int count) {
        String playerId = nickPlayerIdMap.get(receiverNick);

        if (Objects.nonNull(receiver)) {
            handleLifeChange(receiver, count);
            reflectPlayerGameState(receiver);
        } else if (players.containsKey(playerId)) {
            changePlayerLife(players.get(playerId), count);
        }
    }

    private PlayerEntity getPlayerEntity(Player player) {
        String playerMcId = getPlayerId(player);
        if (!players.containsKey(playerMcId)) {
            loadPlayerEntity(player, playerMcId);
        }
        return players.get(playerMcId);
    }

    private void loadPlayerEntity(Player player, String playerMcId) {
        PlayerEntity playerEntity = storage.getPlayer(playerMcId);

        if (Objects.isNull(playerEntity)) {
            playerEntity = PlayerEntity.createNew(playerMcId, player.getName(), config.getLivesCount());
            player.getServer().broadcast(Messages.globalNewPlayer(player));
            player.sendMessage(Messages.welcomePlayer(config, player));
            player.sendMessage(Messages.help(config));
        }

        playerEntity.setLastLoginTs(Instant.now().toEpochMilli());
        players.put(playerMcId, playerEntity);
        nickPlayerIdMap.put(playerEntity.getNick(), playerEntity.getMcId());
    }

    private void reflectPlayerGameState(Player player) {
        String playerId = getPlayerId(player);
        players.computeIfPresent(playerId, (s, playerEntity) -> {
            if (playerEntity.isGhost()) {
                player.setGameMode(GameMode.ADVENTURE);
                player.setGlowing(true);
                player.setInvisible(true);
            } else {
                player.setGameMode(GameMode.SURVIVAL);
                player.setGlowing(false);
                player.setInvisible(false);
            }
            return playerEntity;
        });
    }

    private void handleLifeChange(Player player, int change) {
        handleLifeChange(player, change, false);
    }

    private void handleLifeChange(Player player, int change, boolean setInsteadAdd) {
        PlayerEntity playerEntity = getPlayerEntity(player);
        changePlayerLife(playerEntity, change, setInsteadAdd);
        sendMessagesAboutChange(player, playerEntity);
    }

    private void changePlayerLife(PlayerEntity playerEntity, int change) {
        changePlayerLife(playerEntity, change, false);
    }

    private void changePlayerLife(PlayerEntity playerEntity, int change, boolean setInsteadAdd) {
        if (setInsteadAdd) {
            playerEntity.setLifeCount(change);
        } else {
            playerEntity.addLife(change);
        }
        store();
    }

    private void sendMessagesAboutChange(Player player, PlayerEntity playerEntity) {
        player.sendMessage(Messages.lives(playerEntity.getLifeCount()));
        if (playerEntity.isGhost()) {
            player.sendMessage(Messages.ghost());
            player.showTitle(Title.title(Messages.ghost(), Component.empty()));
            player.getServer().broadcast(Messages.globalPlayerIsGhost(player.getName()));
        } else if (playerEntity.isCameToLife()) {
            player.sendMessage(Messages.cameToLife(playerEntity.getLifeCount()));
            player.showTitle(Title.title(Messages.titleCameToLife(), Component.empty()));
            player.getServer().broadcast(Messages.globalCameToLife(player.getName()));
            playerEntity.setCameToLife(false);
            store();
        } else if (playerEntity.getLifeCount() == 1) {
            player.sendMessage(Messages.warningLife(playerEntity.getLifeCount()));
        }
    }

    private String getPlayerId(Player player) {
        return player.getUniqueId().toString();
    }

    public void addToSpendTimeOnServer(int ticks) {
        players.values().forEach(playerEntity -> playerEntity.addTicks(ticks));
    }

    public void store() {
        storage.store(players);
    }

    public void handleStatisticIncrementEvent(Player player, PlayerStatisticIncrementEvent event) {
        PlayerEntity playerEntity = getPlayerEntity(player);
        switch (event.getStatistic()) {
            case DAMAGE_TAKEN -> playerEntity.addDamageTaken(event.getNewValue() - event.getPreviousValue());
            case DEATHS -> playerEntity.addDeath();
            case KILL_ENTITY -> {
                switch (Objects.requireNonNull(event.getEntityType())) {
                    case PLAYER -> playerEntity.addKillPlayer();
                    case ENDER_DRAGON, WITHER -> playerEntity.addKillBoss();
                    case BLAZE, CREEPER, DROWNED, ELDER_GUARDIAN, ENDERMITE, EVOKER, GHAST, GUARDIAN, HOGLIN,
                            HUSK, MAGMA_CUBE, PHANTOM, PIGLIN_BRUTE, PILLAGER, RAVAGER, SHULKER, SILVERFISH, SKELETON, SKELETON_HORSE,
                            SLIME, SPIDER, STRAY, VEX, VINDICATOR, WITCH, WITHER_SKELETON, ZOGLIN, ZOMBIE, ZOMBIE_VILLAGER,
                            GIANT, ILLUSIONER, ZOMBIE_HORSE, ZOMBIFIED_PIGLIN -> playerEntity.addKillHostileMob();
                    default -> playerEntity.addKillOtherMob();
                }
            }
            default -> {
            }
        }
    }

    public void handleQuitEvent(Player player) {
        PlayerEntity playerEntity = getPlayerEntity(player);
        players.remove(playerEntity.getMcId());
    }

    public void handleKickEvent(Player player) {
        //TODO zvazit odmazanie z db
        PlayerEntity playerEntity = getPlayerEntity(player);
        players.remove(playerEntity.getMcId());
    }

    public void handleExpChangeEvent(Player player, PlayerExpChangeEvent event) {
        PlayerEntity playerEntity = getPlayerEntity(player);
        playerEntity.addExpChangeEvent(event.getAmount());
    }

    public List<PlayerEntity> getTopScore() {
        return storage.getTopPlayers(10, TopCategory.BY_SCORE);
    }
}


/*
- riesit odpojenie zo servera
- risit kill mobov
- riesit zisk xp
-
 */