package sk.maroskomml.lifeplugin;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Messages {
    public static String pluginEnabled() {
        return ChatColor.GREEN + "Maroskov Life Plugin je pripraveny.";
    }

    public static String welcome(Config config, Player player) {
        return ChatColor.GREEN + "Na server sa prihlasil " + player.getName();
    }

    public static String welcomePlayer(Config config, Player player) {
        return ChatColor.GREEN + "Ahoj na serveri " + player.getName() + ". Ziskavas " + config.getLivesCount() + " zivotov";
    }

    public static String help(Config config) {
        return ChatColor.RED + "TODO spravit napovedu ako funguje plugin";
    }

    public static String lives(int lifeCount) {
        return ChatColor.RED + "Ostava ti " +lifeCount + " zivota";
    }

    public static String ghost() {
        return ChatColor.RED + "SI DUCH!!!";
    }

    public static String myLife(int lifeCount) {
        return ChatColor.RED + "Mas " + lifeCount + " zivota";
    }

    public static String errorCommandUsage(String commandName) {
        return ChatColor.RED + "Zle pouzity prikaz /" + commandName + " Pouzi /help " + commandName;
    }

    public static String playerLife(String playerNick, int lifeCount) {
        if(lifeCount == 0){
            return ChatColor.RED + "Hrac " + playerNick + " je DUCH!!!";
        }
        return ChatColor.RED + "Hrac " + playerNick + " ma " + lifeCount + " zivota.";
    }

    public static String unknownPlayer(String playerNick) {
        return ChatColor.RED + "Neznamy hrac " + playerNick;
    }

    public static String globalPlayerIsGhost(String name) {
        return ChatColor.RED + "Hrac " + name + " je duch.";
    }

    public static String playerSentLife(String name, int count) {
        return ChatColor.RED+"Hrac " + name + " ti poslal " + count + " zivotov.";
    }

    public static String globalPlayerSentLife(String name, String receiverNick, int count) {
        return ChatColor.RED+"Hrac " + name + " poslal hracovi " + receiverNick + " " + count + " zivotov.";
    }
}
