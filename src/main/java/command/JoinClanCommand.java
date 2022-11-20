package command;

import model.Clan;
import model.Rank;
import model.User;
import model.dao.DatabaseHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinClanCommand implements CommandExecutor {
    private final DatabaseHandler db = new DatabaseHandler();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (!player.isOp()) {
                return false;
            }

            User sender = db.getUserByPlayer(player);

            String clanName = strings[0];

            if (clanName != null) {
                Clan clan = db.getClanByName(clanName);

                if(clan == null) {
                    player.sendMessage("This clan does not exist!");
                    return false;
                }

                clan.getMembers().add(sender);

                sender.setClan(clan);

                Rank rank = db.getRankByPlayer(player);

                player.setDisplayName(rank.getPrefix() + clan.getTag() + player.getName());
                player.setPlayerListName(rank.getPrefix() + clan.getTag() + player.getName());

                db.saveUser(sender);
                db.saveClan(clan);

                return true;
            }


        }
        return false;
    }
}
