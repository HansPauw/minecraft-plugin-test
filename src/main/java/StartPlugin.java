import command.JoinClanCommand;
import command.PlayerClanListener;
import command.PlayerRankListener;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class StartPlugin extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        getLogger().info("StartPlugin is enabled!");
        getServer().getPluginManager().registerEvents(new PlayerRankListener(), this);
        getCommand("mfr").setExecutor(new PlayerRankListener());
        getCommand("c").setExecutor(new PlayerClanListener());
        getCommand("cj").setExecutor(new JoinClanCommand());
    }

    @Override
    public void onDisable() {
        getLogger().info("StartPlugin is disabled");
    }

}
