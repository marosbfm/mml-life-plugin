package sk.maroskomml.lifeplugin;

import org.bukkit.entity.Player;

public class PlayerLife {

    private final String id;
    private final String nick;
    private int lifeCount;

    public PlayerLife(Config config, Player player) {
        lifeCount = config.getLivesCount();
        nick = player.getName();
        id = player.getUniqueId().toString();
    }

    public String getId() {
        return id;
    }

    public String getNick() {
        return nick;
    }

    public int getLifeCount() {
        return lifeCount;
    }

    public void setLife(int count) {
        lifeCount = count;
    }

    public void addLife(int count) {
        lifeCount = lifeCount + count;
    }

    public void decreaseLife() {
        if(lifeCount > 0){
            lifeCount--;
        }
    }

    public boolean isGhost() {
        return lifeCount == 0;
    }

}
