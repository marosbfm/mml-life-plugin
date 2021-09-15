package sk.maroskomml.lifeplugin;

import org.bukkit.entity.Player;

public class PlayerLife {
    private int lifeCount;
    private String nick;
    private String playerId;
    public PlayerLife(Config config, Player player) {
        lifeCount = config.getLivesCount();
        nick = player.getName();
        playerId = player.getUniqueId().toString();
    }

    public int getLifeCount() {
        return lifeCount;
    }


    public void decreaseLife() {
        if(lifeCount > 0){
            lifeCount--;
        }
    }

    public boolean isGhost() {
        return lifeCount == 0;
    }

    public String getNick() {
        return nick;
    }

    public String getId() {
        return playerId;
    }

    public void addLife(int count) {
        lifeCount = lifeCount + count;
    }

    public void setLife(int count) {
        lifeCount = count;
    }
}
