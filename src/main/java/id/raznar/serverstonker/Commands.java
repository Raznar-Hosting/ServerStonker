package id.raznar.serverstonker;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class Commands implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (label.equalsIgnoreCase("serverstonker")) {
            if (!(args.length == 0)) {
                if (args[0].equalsIgnoreCase("reload")) {
                    Config.reload();
                    sender.sendMessage(Utils.colorize("&aSuccessfully Reloaded Config!"));
                } else if (args[0].equalsIgnoreCase("clear")) {
                    new EntityUtils().clearEntityList();
                    sender.sendMessage(Utils.colorize("&aSuccessfully Cleared entities"));
                } else if (args[0].equalsIgnoreCase("author")) {
                    sender.sendMessage(Utils.colorize("&eThis plugin is made by &dRaznar Lab, &ahttps://raznar.id/"));
                } else {
                    sender.sendMessage(Utils.colorize("&cUnknown argument!"));
                }
            } else {
                sender.sendMessage(Utils.colorize("&eThis plugin is made by &dRaznar Lab, &ahttps://raznar.id/"));
                sender.sendMessage(Utils.colorize("&m&e---------------------------------------------"));
                sender.sendMessage(Utils.colorize("&b/serverstonker reload - Reload configuration"));
                sender.sendMessage(Utils.colorize("&b/serverstonker clear - Clear entities on the world list"));
                sender.sendMessage(Utils.colorize("&b/serverstonker author - Shows Plugin's author"));
                sender.sendMessage(Utils.colorize("&m&e---------------------------------------------"));
            }
        }
        return true;
    }
}
