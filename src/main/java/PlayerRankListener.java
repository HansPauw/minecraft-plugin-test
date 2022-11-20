import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;

public class PlayerRankListener implements Listener, CommandExecutor {
    private final DatabaseHandler db = new DatabaseHandler();

    private static final Map<Player, Rank> ranks = new HashMap<>();

    public void setRank(Player player, Rank rank) {
        ranks.put(player, rank);
        db.updateRank(player, rank);
        User u = db.getUserByPlayer(player);

        Clan clan =  u.getClan();

        if(clan != null) {
            player.setDisplayName(rank.getPrefix() + clan.getTag() + player.getName());
            player.setPlayerListName(rank.getPrefix() + clan.getTag() + player.getName());
            ranks.put(player, rank);
        } else {
            player.setDisplayName(rank.getPrefix() + player.getName());
            player.setPlayerListName(rank.getPrefix() + player.getName());
            ranks.put(player, rank);
        }
    }

    public void updateRank(Player player, Rank rank) {
        this.setRank(player, rank);
    }

    public static Rank getRank(Player player) {
        return ranks.get(player);
    }

    public void loadRank(Player player) {
        Rank rank = db.getRankByPlayer(player);

        User u = db.getUserByPlayer(player);

        Clan clan =  u.getClan();

        if(clan != null) {
            player.setDisplayName(rank.getPrefix() + clan.getTag() + player.getName());
            player.setPlayerListName(rank.getPrefix() + clan.getTag() + player.getName());
            ranks.put(player, rank);
        } else {
            player.setDisplayName(rank.getPrefix() + player.getName());
            player.setPlayerListName(rank.getPrefix() + player.getName());
            ranks.put(player, rank);
        }

    }

    public void saveRank(Player player) {
        ranks.remove(player);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        loadRank(event.getPlayer());
        Bukkit.broadcastMessage(event.getPlayer().getDisplayName() + " has joined the server!");
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        saveRank(event.getPlayer());
        Bukkit.broadcastMessage(event.getPlayer().getDisplayName() + " has quit the server!");
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if(!player.isOp()) {
                return false;
            }

            User sender = db.getUserByPlayer(player);


            String targetPlayerName = strings[0];
            String targetRank = strings[1].toUpperCase();

            if(Bukkit.getPlayer(targetPlayerName) != null && targetRank != null) {
                try {
                    Player tPlayer = Bukkit.getPlayer(targetPlayerName);
                    User target = db.getUserByPlayer(tPlayer);

                    if(db.isInSameClan(sender, target)) {
                        setRank(tPlayer, Rank.valueOf(targetRank));
                        return true;
                    }


                } catch (IllegalArgumentException e) {
                    player.sendMessage("The specified rank does not exist!");
                    return false;
                }
            }
        }
        return false;
    }
}
