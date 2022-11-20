import org.bukkit.ChatColor;

public enum Rank {
    UNDERLING(ChatColor.DARK_GREEN + ""),
    BOSS(ChatColor.DARK_RED + ""),
    HAND(ChatColor.BLUE + "");

    private final String prefix;

    Rank(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
