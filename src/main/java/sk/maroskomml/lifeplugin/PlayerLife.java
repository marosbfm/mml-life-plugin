package sk.maroskomml.lifeplugin;

import org.bukkit.entity.Player;

public class PlayerLife {

    private final String id;
    private final String nick;
    private int lifeCount;
    private boolean cameToLife = false;

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

    public void setLife(int count){
        if(count < 0){
            count = 0;
        }
        if(isGhost() && count > 0){
            cameToLife = true;
        }
        lifeCount = count;
    }

    public void addLife(int count) {
        if(isGhost() && count > 0){
            cameToLife = true;
        }
        lifeCount = lifeCount + count;
        if(lifeCount < 0){
            lifeCount = 0;
        }
    }

    public boolean isGhost() {
        return lifeCount == 0;
    }

    public boolean isCameToLife() {
        return cameToLife;
    }

    public void setCameToLife(boolean cameToLife) {
        this.cameToLife = cameToLife;
    }
}
