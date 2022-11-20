package command;

import model.Clan;
import model.Rank;
import model.User;
import model.dao.DatabaseHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;

public class PlayerClanListener implements Listener, CommandExecutor {


    private final DatabaseHandler db = new DatabaseHandler();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (!player.isOp()) {
                return false;
            }

            User sender = db.getUserByPlayer(player);

            if(strings[0] == null) {
                return false;
            }
            String clanName = strings[0];
            String clanTag = "["+clanName.substring(0,3)+"]";
            clanTag = clanTag.toUpperCase();

            Clan clan = new Clan();

            clan.setName(clanName);
            clan.setDescription("");
            clan.setTag(clanTag);

            if(clan.getMembers() == null) {
                clan.setMembers(new ArrayList<>());
            }

            clan.getMembers().add(sender);


            db.saveClan(clan);

            sender.setClan(clan);

            db.saveUser(sender);

            Rank rank = db.getRankByPlayer(player);

            player.setDisplayName(rank.getPrefix() + clan.getTag() + player.getName());
            player.setPlayerListName(rank.getPrefix() + clan.getTag() + player.getName());

            return true;
        }
        return false;
    }
}
