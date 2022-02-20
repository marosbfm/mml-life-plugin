package sk.maroskomml.lifeplugin;

import java.time.Instant;

public class PlayerEntity {
    private static final int SCORE_EXP_CHANGE = 1;
    private static final int SCORE_DAMAGE_TAKEN = 1;
    private static final int SCORE_MOB_KILL = 1;
    private static final int SCORE_HOSTILE_MOB_KILL = 5;
    private static final int SCORE_BOSS_KILL = 10;
    private static final int SCORE_PLAYER_KILL = 100;
    private String mcId;
    private String nick;
    private int lifeCount;
    private int score;
    private int deaths;
    private int killedPlayers;
    private int killedMobs;
    private int killedHostileMobs;
    private int gainedXP;
    private long spendTimeOnServer;
    private long lastLoginTs;
    private long createTs;

    private boolean cameToLife = false;

    public static PlayerEntity createNew(String playerMcId, String nick, int livesCount) {
        Instant now = Instant.now();
        return  new PlayerEntity(
                playerMcId, nick, livesCount, 0,0, 0, 0,0,0,0,
                now.toEpochMilli(), now.toEpochMilli()
        );
    }

    public PlayerEntity(
            String mcId,
            String nick,
            int lifeCount,
            int score,
            int deaths,
            int killedPlayers,
            int killedMobs,
            int killedHostileMobs,
            int gainedXP,
            long spendTimeOnServer,
            long lastLoginTs,
            long createTs
    ) {
        this.mcId = mcId;
        this.nick = nick;
        this.lifeCount = lifeCount;
        this.score = score;
        this.deaths = deaths;
        this.killedPlayers = killedPlayers;
        this.killedMobs = killedMobs;
        this.killedHostileMobs = killedHostileMobs;
        this.gainedXP = gainedXP;
        this.spendTimeOnServer = spendTimeOnServer;
        this.lastLoginTs = lastLoginTs;
        this.createTs = createTs;
    }

    public String getMcId() {
        return mcId;
    }

    public String getNick() {
        return nick;
    }

    public int getScore() {
        return score;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getKilledPlayers() {
        return killedPlayers;
    }

    public int getKilledMobs() {
        return killedMobs;
    }

    public int getKilledHostileMobs() {
        return killedHostileMobs;
    }

    public int getGainedXP() {
        return gainedXP;
    }

    public long getSpendTimeOnServer() {
        return spendTimeOnServer;
    }

    public long getLastLoginTs() {
        return lastLoginTs;
    }

    public void setLastLoginTs(long lastLoginTs) {
        this.lastLoginTs = lastLoginTs;
    }

    public long getCreateTs() {
        return createTs;
    }

    public void setLifeCount(int lifeCount) {
        this.lifeCount = lifeCount;
    }

    public int getLifeCount() {
        return lifeCount;
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

    public void addDeath() {
        this.deaths = deaths + 1;
    }

    public void addLife(int change) {
        this.lifeCount = this.lifeCount + change;
    }

    public void addTicks(int ticks) {
        this.spendTimeOnServer = this.spendTimeOnServer + ticks;
    }

    public void addKillPlayer() {
        this.score = this.score + SCORE_PLAYER_KILL;
        this.killedPlayers = this.killedPlayers + 1;
    }

    public void addKillBoss() {
        this.score = this.score + SCORE_BOSS_KILL;
        this.killedHostileMobs = this.killedHostileMobs + 1;
    }

    public void addKillHostileMob() {
        this.score = this.score + SCORE_HOSTILE_MOB_KILL;
        this.killedHostileMobs = this.killedHostileMobs + 1;
    }

    public void addKillOtherMob() {
        this.score = this.score + SCORE_MOB_KILL;
        this.killedMobs = this.killedMobs + 1;
    }

    public void addDamageTaken(int amount) {
        this.score = this.score + amount * SCORE_DAMAGE_TAKEN;
    }

    public void addExpChangeEvent(int amount) {
        if(amount > 0) {
            this.score = this.score + amount * SCORE_EXP_CHANGE;
            this.gainedXP = this.gainedXP + amount;
        }
    }
}