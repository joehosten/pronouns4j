package games.negative.pronouns4j.data;

import games.negative.pronouns4j.pronouns.Pronouns;

import java.sql.SQLException;

public interface DataStorage {

    Pronouns loadPronouns(String uuid) throws SQLException;

    void savePronouns(String uuid, Pronouns pronouns); //todo: add pronouns parameter

    void init() throws SQLException;

}
