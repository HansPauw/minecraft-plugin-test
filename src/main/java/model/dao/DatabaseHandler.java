package model.dao;

import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import model.Clan;
import model.Rank;
import model.User;
import org.bukkit.entity.Player;


import java.util.List;
import java.util.Optional;

import static dev.morphia.query.experimental.filters.Filters.eq;

public class DatabaseHandler {
    private final String uri = "mongodb://localhost:27017";
    private Morphia morphia;
    private final Datastore datastore;

    public DatabaseHandler() {

        datastore = Morphia.createDatastore(MongoClients.create(uri), "server");
        datastore.ensureIndexes();

        datastore.getMapper().map(User.class);
        datastore.getMapper().map(Clan.class);

    }

    public void saveClan(Clan c) {
        datastore.save(c);
    }

    public boolean isUserInClan(String clan, String uuid) {
        Clan c = datastore.find("Clans", Clan.class).filter(eq("name", clan)).first();

        if(c != null) {
            Optional<User> u = c.getMembers().stream().filter(e -> e.getUuid().equals(uuid)).findAny();
            if(u.isPresent()) {
                return true;
            }
        }

        return false;
    }

    public Clan getClanByName(String clanname) {
        return datastore.find(Clan.class).filter(eq("name", clanname)).first();
    }

    public boolean isInSameClan(User u1, User u2) {
        return u1.getClan().getId().equals(u2.getClan().getId());
    }

    public User getUserByPlayer(Player player) {
        User u = datastore.find(User.class).filter(eq("uuid", player.getUniqueId().toString())).first();
        if(u == null) {
            u = new User();
            u.setUuid(player.getUniqueId().toString());
            u.setRank(Rank.UNDERLING);
            u.setUsername(player.getName());
            datastore.save(u);
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
        datastore.save(u);
    }

    public List<User> getAllUser() {
        return datastore.find(User.class).iterator().toList();
    }
}
