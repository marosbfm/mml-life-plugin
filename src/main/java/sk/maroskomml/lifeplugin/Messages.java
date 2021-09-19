package sk.maroskomml.lifeplugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;

public class Messages {
    public static TextComponent consolePluginEnabled() {
        return Component.text("MML Life Plugin je pripraveny.").color(NamedTextColor.GREEN);
    }

    public static TextComponent globalNewPlayer(Player player) {
        return Component.text("Na server sa prihlasil novy hrac ").color(NamedTextColor.GRAY)
                .append(
                        Component.text(player.getName())
                                .color(NamedTextColor.GREEN)
                                .decoration(TextDecoration.BOLD, true)
                );
    }

    public static TextComponent welcomePlayer(Config config, Player player) {
        int livesCount = config.getLivesCount();
        return Component.text("Ahoj na serveri ").color(NamedTextColor.GRAY)
                .append(
                        Component.text(player.getName())
                                .color(NamedTextColor.GREEN)
                                .decoration(TextDecoration.BOLD, true))
                .append(
                        Component.text(". Ziskavas ")
                                .color(NamedTextColor.GRAY))
                .append(
                        Component.text(livesCount)
                                .color(NamedTextColor.GREEN)
                                .decoration(TextDecoration.BOLD, true))
                .append(
                        Component.text(" " + wordLife(livesCount) + ".")
                                .color(NamedTextColor.GRAY));
    }

    public static TextComponent help(Config config) {
        int livesCount = config.getLivesCount();
        return Component.text(
                        "Hrac pri provom prihlaseni sa na server obdrzi " + livesCount + " " + wordLife(livesCount) + ". Po kazdej smrti sa hracovi odpocita jeden zivot. Ked hrac strati vsetky zivoty stava sa duchom.\n" +
                                "Duch je neviditelny a ziari. Nemoze nicit bloky ale moze zbierat veci, interagovat s objektami (napr. otvarat dvere, vyberat truhlice) a moze bojovat proti hracom aj inym mobom.\n" +
                                "Hrac obzivne ak znova bude mat viac zivotov ako 0. Zivot hrac moze ziskat pouzitim krystalu zivota alebo ak sa podeli s nim o svoj zivot iny hrac.\n" +
                                "Pocas hry plinie ozivovaci interval ( raz za " + config.getResetIntervalAmount() + " " + wordUnit(config) + "), kedy sa vsetkym hracom (aj tym ktori su momentalne offline) pripocita " + livesCount + " " + wordLife(livesCount) + ".\n" +
                                "OP moze nastavit pocet zivotov lubovolnemu hracovi. Ak nastavi nejakemu hracovi zivoty na 0 stava sa z daneho hraca duch.\n" +
                                "Pre napovedu pre prikazy tohoto pluginu: ").color(NamedTextColor.GRAY)
                .append(Component.text("/help mml-life-plugin\n\n").color(NamedTextColor.GOLD))
                .append(Component.text("Viac info: ").color(NamedTextColor.GRAY))
                .append(
                        Component.text("https://github.com/marosmasarovic/mml-life-plugin")
                                .color(NamedTextColor.BLUE)
                                .clickEvent(ClickEvent.openUrl("https://github.com/marosmasarovic/mml-life-plugin"))
                                .decoration(TextDecoration.UNDERLINED,true)
                );
    }

    public static TextComponent lives(int lifeCount) {
        return Component.text("Ostava ti ").color(NamedTextColor.GRAY)
                .append(Component.text(lifeCount).color(NamedTextColor.GREEN))
                .append(Component.text(" " + wordLife(lifeCount) + ".").color(NamedTextColor.GRAY));
    }

    public static TextComponent ghost() {
        return Component.text("SI DUCH!!!").color(NamedTextColor.DARK_RED).decoration(TextDecoration.BOLD,true);
    }

    public static TextComponent myLife(int lifeCount) {
        return Component.text("Ostava ti ").color(NamedTextColor.GRAY)
                .append(Component.text(lifeCount).color(NamedTextColor.GREEN))
                .append(Component.text(" " + wordLife(lifeCount) + ".").color(NamedTextColor.GRAY));
    }

    public static TextComponent errorCommandUsage(String commandName) {
        return Component.text("Zle pouzity prikaz /" + commandName)
                .color(NamedTextColor.RED)
                .append(Component.text(". Pouzi /help " + commandName + ".").color(NamedTextColor.GRAY));
    }

    public static TextComponent playerLife(String playerNick, int lifeCount) {
        if (lifeCount == 0) {
           return Component.text("Hrac ").color(NamedTextColor.GRAY)
                   .append(Component.text(playerNick).color(NamedTextColor.GREEN))
                   .append(Component.text(" je ").color(NamedTextColor.GRAY))
                   .append(Component.text("DUCH!!!")
                           .color(NamedTextColor.DARK_RED)
                           .decoration(TextDecoration.BOLD,true));

        }
        return Component.text("Hrac ").color(NamedTextColor.GRAY)
                .append(Component.text(playerNick).color(NamedTextColor.GREEN))
                .append(Component.text(" ma ").color(NamedTextColor.GRAY))
                .append(Component.text(lifeCount).color(NamedTextColor.GREEN))
                .append(Component.text(" " + wordLife(lifeCount) + ".")
                        .color(NamedTextColor.GRAY));
    }

    public static TextComponent unknownPlayer(String playerNick) {
        return Component.text("Neznamy hrac " + playerNick).color(NamedTextColor.RED);
    }

    public static TextComponent globalPlayerIsGhost(String playerNick) {
        return Component.text("Hrac ").color(NamedTextColor.GRAY)
                .append(Component.text(playerNick).color(NamedTextColor.GREEN))
                .append(Component.text(" je ").color(NamedTextColor.GRAY))
                .append(Component.text("DUCH!!!")
                        .color(NamedTextColor.DARK_RED)
                        .decoration(TextDecoration.BOLD,true));
    }

    public static TextComponent playerSentLife(String playerNick, int lifeCount) {
        return Component.text("Hrac ").color(NamedTextColor.GRAY)
                .append(Component.text(playerNick).color(NamedTextColor.GREEN))
                .append(Component.text(" ti poslal ").color(NamedTextColor.GRAY))
                .append(Component.text(lifeCount).color(NamedTextColor.GREEN))
                .append(Component.text(" " + wordLife(lifeCount) + ".").color(NamedTextColor.GRAY));
    }

    public static TextComponent globalPlayerSentLife(String playerNick, String receiverNick, int lifeCount) {
        return Component.text("Hrac ").color(NamedTextColor.GRAY)
                .append(Component.text(playerNick).color(NamedTextColor.GREEN))
                .append(Component.text(" poslal hracovi ").color(NamedTextColor.GRAY))
                .append(Component.text( receiverNick + " ").color(NamedTextColor.GREEN))
                .append(Component.text(lifeCount).color(NamedTextColor.GREEN))
                .append(Component.text(" " + wordLife(lifeCount) + ".").color(NamedTextColor.GRAY));
    }

    public static TextComponent globalAllPlayersLivesAdd(int lifeCount) {
        return Component.text("Vsetkym hracom bolo pridanych ").color(NamedTextColor.GRAY)
                .append(Component.text(lifeCount).color(NamedTextColor.GREEN))
                .append(Component.text(" " + wordLife(lifeCount) + ".").color(NamedTextColor.GRAY));
    }

    public static TextComponent cantSentLiveToHimSelf() {
        return Component.text("Nemozes poslat zivoty sam sebe!").color(NamedTextColor.RED);
    }

    public static TextComponent globalPlayerHasSetLife(String receiverNick, int lifeCount) {
        return Component.text("Hrac ").color(NamedTextColor.GRAY)
                .append(Component.text(receiverNick).color(NamedTextColor.GREEN))
                .append(Component.text(" ma nastavenych ").color(NamedTextColor.GRAY))
                .append(Component.text(lifeCount).color(NamedTextColor.GREEN))
                .append(Component.text(" " + wordLife(lifeCount) + ".").color(NamedTextColor.GRAY));
    }

    public static TextComponent useCrystal() {
        return Component.text("Pouzil si krtstal zivota.").color(NamedTextColor.DARK_RED);
    }

    public static TextComponent errorDoNotHavePermission(String commandName) {
        return Component.text("Nemas pravo pouzit prikaz /" + commandName)
                .color(NamedTextColor.RED);
    }

    private static String wordLife(int count) {
        return switch (count) {
            case 1 -> "zivot";
            case 2, 3, 4 -> "zivoty";
            default -> "zivotov";
        };
    }

    private static String wordUnit(Config config) {
        int count = config.getResetIntervalAmount();
        String unit = config.getResetIntervalUnit();

        return switch (count) {
            case 1 -> switch (unit) {
                case "MINUTE" -> "minutu";
                case "HOUR" -> "hodinu";
                case "DAYS" -> "den";
                case "WEEKS" -> "tyzden";
                case "YEARS" -> "rok";
                default -> "";
            };
            case 2, 3, 4 -> switch (unit) {
                case "MINUTE" -> "minuty";
                case "HOUR" -> "hodiny";
                case "DAYS" -> "dni";
                case "WEEKS" -> "tyzdne";
                case "YEARS" -> "roky";
                default -> "";
            };
            default -> switch (unit) {
                case "MINUTE" -> "minut";
                case "HOUR" -> "hodin";
                case "DAYS" -> "dni";
                case "WEEKS" -> "tyzdnov";
                case "YEARS" -> "rokov";
                default -> "";
            };
        };
    }

}
