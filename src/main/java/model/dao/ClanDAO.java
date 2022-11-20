package model.dao;

import model.Clan;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

public class ClanDAO extends BasicDAO<Clan, String> {
    public ClanDAO(Class<Clan> clan, Datastore ds) {
        super(clan, ds);
    }
}
