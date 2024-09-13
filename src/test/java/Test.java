import games.negative.alumina.sql.SQLCredentials;
import games.negative.alumina.sql.SQLDatabases;
import games.negative.pronouns4j.data.DataStorage;
import games.negative.pronouns4j.data.SqlStorage;
import games.negative.pronouns4j.pronouns.Pronouns;
import lombok.SneakyThrows;

public class Test {
    @SneakyThrows
    public static void main(String[] args) {
        System.out.println("Hello, World!");

        DataStorage storage = new SqlStorage(SQLDatabases.mysql(new SQLCredentials(""
                , 0, "", "", "")));

        Pronouns pronouns = new Pronouns("he", "him", "him", "himself");

        storage.savePronouns("123abc", pronouns);
        storage.loadPronouns("123abc");
    }
}
