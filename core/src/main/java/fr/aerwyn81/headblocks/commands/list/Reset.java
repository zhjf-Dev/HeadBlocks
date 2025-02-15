package fr.aerwyn81.headblocks.commands.list;

import fr.aerwyn81.headblocks.HeadBlocks;
import fr.aerwyn81.headblocks.commands.Cmd;
import fr.aerwyn81.headblocks.commands.HBAnnotations;
import fr.aerwyn81.headblocks.handlers.LanguageHandler;
import fr.aerwyn81.headblocks.handlers.StorageHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.stream.Collectors;

@HBAnnotations(command = "reset", permission = "headblocks.admin", args = { "player" })
public class Reset implements Cmd {
    private final LanguageHandler languageHandler;
    private final StorageHandler storageHandler;

    public Reset(HeadBlocks main) {
        this.languageHandler = main.getLanguageHandler();
        this.storageHandler = main.getStorageHandler();
    }

    @Override
    public boolean perform(CommandSender sender, String[] args) {
        Player pTemp = Bukkit.getOfflinePlayer(args[1]).getPlayer();

        if (pTemp == null) {
            sender.sendMessage(languageHandler.getMessage("Messages.PlayerNotFound"));
            return true;
        }

        if (!storageHandler.containsPlayer(pTemp.getUniqueId())) {
            sender.sendMessage(languageHandler.getMessage("Messages.NoHeadFound"));
            return true;
        }

        storageHandler.resetPlayer(pTemp.getUniqueId());
        sender.sendMessage(languageHandler.getMessage("Messages.PlayerReset")
                .replaceAll("%player%", pTemp.getName()));

        return true;
    }

    @Override
    public ArrayList<String> tabComplete(CommandSender sender, String[] args) {
        return args.length == 2 ? Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .collect(Collectors.toCollection(ArrayList::new)) : new ArrayList<>();
    }
}
