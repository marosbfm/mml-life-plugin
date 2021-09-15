package sk.maroskomml.lifeplugin;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PluginCommandExecutor implements CommandExecutor {

    private final Config config;
    private final PlayersHandler playersHandler;

    public PluginCommandExecutor(Config config, PlayersHandler playersHandler) {
        this.config = config;
        this.playersHandler = playersHandler;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        String commandName = command.getName().toLowerCase();
        switch (commandName) {
            case "mmlmylife": {
                mmlMyLife(sender, commandName);
                break;
            }
            case "mmllife": {
                mmlLife(sender, args, commandName);
                break;
            }
            case "mmlgivelife": {
                mmlGiveLive(sender, args, commandName);
                break;
            }
            default:
        }
        return true;
    }

    private void mmlGiveLive(@NotNull CommandSender sender, @NotNull String[] args, String commandName) {
        try {
            if (sender instanceof Player) {
                Player senderPlayer = (Player) sender;
                if (args.length != 2) {
                    sender.sendMessage(Messages.errorCommandUsage(commandName));
                }
                String receiverNick = args[0];
                int count = Integer.parseInt(args[1]);
                if (playersHandler.knownPlayer(receiverNick)) {
                    int senderLifeCount = playersHandler.getLifeCount(senderPlayer);
                    if (senderLifeCount - count < 0) {
                        count = senderLifeCount;
                    }
                    playersHandler.addLifeByNick(receiverNick, count);
                    playersHandler.setLife(senderPlayer.getUniqueId().toString(), senderLifeCount - count);

                    Server server = senderPlayer.getServer();

                    playersHandler.handleRespawn(senderPlayer);
                    senderPlayer.sendMessage(Messages.lives(playersHandler.getLifeCount(senderPlayer)));
                    if (playersHandler.isGhost(senderPlayer)) {
                        senderPlayer.sendMessage(Messages.ghost());
                        server.broadcastMessage(Messages.globalPlayerIsGhost(senderPlayer.getName()));
                    }


                    Player receiver = server.getPlayer(receiverNick);
                    if (Objects.nonNull(receiver)) {
                        playersHandler.handleRespawn(receiver);
                        receiver.sendMessage(Messages.playerSentLife(senderPlayer.getName(), count));
                        receiver.sendMessage(Messages.lives(playersHandler.getLifeCount(receiver)));
                        if(playersHandler.isGhost(receiver)){
                            receiver.sendMessage(Messages.ghost());
                            server.broadcastMessage(Messages.globalPlayerIsGhost(receiver.getName()));
                        }
                    }

                    server.broadcastMessage(Messages.globalPlayerSentLife(senderPlayer.getName(), receiverNick, count));
                } else {
                    sender.sendMessage(Messages.unknownPlayer(receiverNick));

                }
            } else {
                sender.sendMessage(Messages.errorCommandUsage(commandName));
            }
        } catch (Exception e) {
            sender.sendMessage(Messages.errorCommandUsage(commandName));
        }
    }

    private void mmlLife(@NotNull CommandSender sender, @NotNull String[] args, String commandName) {
        if (args.length != 1) {
            sender.sendMessage(Messages.errorCommandUsage(commandName));
        } else {
            String playerNick = args[0];
            if (playersHandler.knownPlayer(playerNick)) {
                int lifeCount = playersHandler.getLifeCountByNick(playerNick);
                sender.sendMessage(Messages.playerLife(playerNick, lifeCount));
            } else {
                sender.sendMessage(Messages.unknownPlayer(playerNick));
            }
        }
    }

    private void mmlMyLife(@NotNull CommandSender sender, String commandName) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            int lifeCount = playersHandler.getLifeCount(player);
            player.sendMessage(Messages.myLife(lifeCount));
        } else {
            sender.sendMessage(Messages.errorCommandUsage(commandName));
        }
    }
}
