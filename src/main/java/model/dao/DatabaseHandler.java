package model.dao;

import com.mongodb.MongoClient;
import model.Clan;
import model.Rank;
import model.User;
import org.bukkit.entity.Player;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.util.List;
import java.util.Optional;

public class DatabaseHandler {
    private MongoClient mc;
    private Morphia morphia;
    private Datastore datastore;
    private UserDAO userDAO;

    private ClanDAO clanDAO;

    public DatabaseHandler() {
        mc = new MongoClient();
        morphia = new Morphia();
        morphia.map(User.class);
        morphia.map(Clan.class);

        datastore = morphia.createDatastore(mc, "server");
        datastore.ensureIndexes();

        userDAO = new UserDAO(User.class, datastore);
        clanDAO = new ClanDAO(Clan.class, datastore);

    }

    public void saveClan(Clan c) {
        clanDAO.save(c);
    }

    public boolean isUserInClan(String clan, String uuid) {
        Clan c = clanDAO.findOne("name", clan);

        if(c != null) {
            Optional<User> u = c.getMembers().stream().filter(e -> e.getUuid() == uuid).findAny();
            if(u.isPresent()) {
                return true;
            }
        }

        return false;
    }

    public Clan getClanByName(String clanname) {
        return clanDAO.findOne("name", clanname);
    }

    public boolean isInSameClan(User u1, User u2) {
        return u1.getClan().getId() == u2.getClan().getId();
    }

    public User getUserByPlayer(Player player) {
        User u = userDAO.findOne("uuid", player.getUniqueId().toString());
        if(u == null) {
            u = new User();
            u.setUuid(player.getUniqueId().toString());
            u.setRank(Rank.UNDERLING);
            u.setUsername(player.getName());
            userDAO.save(u);
        }

        return u;
    }

    public Rank getRankByPlayer(Player player) {
        User u = getUserByPlayer(player);
        return u.getRank();
    }

    public void updateRank(Player player, Rank rank) {
        User u = getUserByPlayer(player);
        u.setRank(rank);
        datastore.merge(u);
    }

    public void saveUser(User u) {
        userDAO.save(u);
    }

    public List<User> getAllUser() {
        return userDAO.find().asList();
    }
}
