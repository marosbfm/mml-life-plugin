package sk.maroskomml.lifeplugin;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PluginCommands implements CommandExecutor {

    public static final String COMMAND_HELP = "mmlhelp";
    public static final String COMMAND_MY_LIFE = "mmlmylife";
    public static final String COMMAND_LIFE = "mmllife";
    public static final String COMMAND_GIVE_LIFE = "mmlgivelife";
    public static final String COMMAND_SET_LIFE = "mmlsetlife";

    private final Config config;
    private final PlayersHandler playersHandler;

    public PluginCommands(Config config, PlayersHandler playersHandler) {
        this.config = config;
        this.playersHandler = playersHandler;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        String commandName = command.getName().toLowerCase();
        return switch (commandName) {
            case COMMAND_HELP -> mmlHelp(sender);
            case COMMAND_MY_LIFE -> mmlMyLife(sender, commandName);
            case COMMAND_LIFE -> mmlLife(sender, args, commandName);
            case COMMAND_GIVE_LIFE -> mmlGiveLive(sender, args, commandName);
            case COMMAND_SET_LIFE -> mmlSetLive(sender, args, commandName);
            default -> false;
        };
    }

    private boolean mmlHelp(CommandSender sender) {
        sender.sendMessage(Messages.help(config));
        return true;
    }

    private boolean mmlMyLife(CommandSender sender, String commandName) {
        boolean result = false;
        if (checkOnlyPlayerCommand(sender, commandName)) {
            Player player = (Player) sender;
            int lifeCount = playersHandler.getLifeCount(player);
            player.sendMessage(Messages.myLife(lifeCount));
            result = true;
        }
        return result;
    }

    private boolean mmlLife(CommandSender sender, String[] args, String commandName) {
        boolean result = false;
        if (checkArgSize(sender, args, 1, commandName) &&
                checkKnownPlayer(sender, args[0])
        ) {
            String playerNick = args[0];
            int lifeCount = playersHandler.getLifeCountByNick(playerNick);
            sender.sendMessage(Messages.playerLife(playerNick, lifeCount));
            result = true;
        }
        return result;
    }

    private boolean mmlGiveLive(CommandSender sender, String[] args, String commandName) {
        boolean result = false;
        if (checkOnlyPlayerCommand(sender, commandName) &&
                checkArgSize(sender, args, 2, commandName) &&
                checkKnownPlayer(sender, args[0]) &&
                checkLiveArgument(sender, args[1], commandName) &&
                checkSamePlayer(sender, args[0])
        ) {
            Player senderPlayer = (Player) sender;
            String receiverNick = args[0];
            int count = Integer.parseInt(args[1]);

            int senderLifeCount = playersHandler.getLifeCount(senderPlayer);
            if (senderLifeCount - count < 0) {
                count = senderLifeCount;
            }

            Server server = senderPlayer.getServer();
            Player receiver = server.getPlayer(receiverNick);

            playersHandler.addLifeByNick(receiverNick, receiver, count);
            playersHandler.setLife(senderPlayer, senderLifeCount - count);

            if (Objects.nonNull(receiver)) {
                receiver.sendMessage(Messages.playerSentLife(senderPlayer.getName(), count));
            }

            server.broadcast(Messages.globalPlayerSentLife(senderPlayer.getName(), receiverNick, count));
            result = true;
        }
        return result;
    }

    private boolean mmlSetLive(CommandSender sender, String[] args, String commandName) {
        boolean result = false;
        if (checkOP(sender, commandName) &&
                checkArgSize(sender, args, 2, commandName) &&
                checkKnownPlayer(sender, args[0]) &&
                checkLiveArgument(sender, args[1], commandName)
        ) {
            String receiverNick = args[0];
            int count = Integer.parseInt(args[1]);

            Server server = sender.getServer();
            Player receiver = server.getPlayer(receiverNick);

            playersHandler.setLifeByNick(receiverNick, receiver, count);

            server.broadcast(Messages.globalPlayerHasSetLife(receiverNick, count));
            result = true;
        }
        return result;
    }

    private boolean checkOnlyPlayerCommand(CommandSender sender, String commandName) {
        boolean result = true;
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.errorCommandUsage(commandName));
            result = false;
        }
        return result;
    }

    private boolean checkArgSize(CommandSender sender, String[] args, int size, String commandName) {
        boolean result = true;
        if (args.length != size) {
            sender.sendMessage(Messages.errorCommandUsage(commandName));
            result = false;
        }
        return result;
    }

    private boolean checkKnownPlayer(CommandSender sender, String playerNick) {
        boolean result = true;
        if (!playersHandler.knownPlayer(playerNick)) {
            sender.sendMessage(Messages.unknownPlayer(playerNick));
            result = false;
        }
        return result;
    }

    private boolean checkOP(CommandSender sender, String commandName) {
        boolean result = true;
        if ((sender instanceof Player) && !sender.hasPermission("op")) {
            sender.sendMessage(Messages.errorDoNotHavePermission(commandName));
            result = false;
        }
        return result;
    }

    private boolean checkLiveArgument(CommandSender sender, String argument, String commandName) {
        boolean result;
        try {
            int count = Integer.parseInt(argument);
            result = count >= 0;
        } catch (NumberFormatException e) {
            sender.sendMessage(Messages.errorCommandUsage(commandName));
            result = false;
        }
        return result;
    }

    private boolean checkSamePlayer(CommandSender sender, String playerNick) {
        boolean result = true;
        if (sender.getName().equals(playerNick)) {
            sender.sendMessage(Messages.cantSentLiveToHimSelf());
            result = false;
        }
        return result;
    }
}