package sk.maroskomml.lifeplugin.model;

import sk.maroskomml.lifeplugin.PlayerEntity;

import java.util.List;
import java.util.Map;

public interface Storage {
    void store(Map<String, PlayerEntity> players);

    PlayerEntity getPlayer(String playerMcId);

    List<PlayerEntity> getTopPlayers(int top, TopCategory topCategory);
}
