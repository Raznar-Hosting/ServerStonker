package id.raznar.serverstonker;

import org.bukkit.ChatColor;

public class Utils {
    public static String colorize(String messages){
        return ChatColor.translateAlternateColorCodes('&', messages);
    }
}
