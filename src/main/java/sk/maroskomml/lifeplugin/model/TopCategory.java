package sk.maroskomml.lifeplugin.model;

public enum TopCategory {
    BY_LIFE_COUNT("lifeCount"),
    BY_SCORE("score"),
    BY_DEATHS("deaths"),
    BY_KILLED_PLAYERS("killedPlayers"),
    BY_KILLED_MOBS("killedMobs"),
    BY_KILLED_HOSTILE_MOBS("killedHostileMobs"),
    BY_GAINED_XP("gainedXP"),
    BY_SPEND_TIME_ON_SERVER("spendTimeOnServer");

    TopCategory(String categoryName) {
        this.categoryName = categoryName;
    }
    private final String categoryName;

    public String getCategoryName() {
        return categoryName;
    }
}
