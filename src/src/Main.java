import com.mysql.cj.jdbc.MysqlDataSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Properties prop = new Properties();
        try{
            prop.load(Files.newInputStream(Path.of("src/src/music.properties"),
                    StandardOpenOption.READ));

        }catch(IOException e){
            throw new RuntimeException(e);
        }
        var dataSource = new MysqlDataSource();
        dataSource.setServerName(prop.getProperty("serverName"));
        dataSource.setPort(Integer.parseInt(prop.getProperty("port")));
        dataSource.setPassword(prop.getProperty("password"));
        dataSource.setDatabaseName(prop.getProperty("databaseName"));
        try(Connection connection = dataSource.getConnection(
                prop.getProperty("user"),
                System.getenv("MYSQL_PASS")
        )){
            Scanner sc = new Scanner(System.in);
//            String inputAlbum = sc.nextLine();
//            String query = "SELECT * FROM albums WHERE album_name='" + inputAlbum + "'";

            connection.setAutoCommit(false);

            String insertUpdate = "INSERT INTO artists (artist_name) VALUES (?)";

            PreparedStatement statement = connection.prepareStatement(insertUpdate);
            statement.setString(1, sc.nextLine());
            int rs = statement.executeUpdate();

            System.out.printf("%d lines affected", rs);
            connection.commit();




            String deleteUpdate = "DELETE FROM artists WHERE artist_name='Linkin Park'";
            int lines = statement.executeUpdate(deleteUpdate);

            System.out.printf("%d lines affected", lines);
            connection.rollback();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }


    }
}
