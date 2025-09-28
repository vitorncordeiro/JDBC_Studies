import com.mysql.cj.jdbc.MysqlDataSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
            String inputAlbum = sc.nextLine();
            String query = "SELECT * FROM albums WHERE album_name='" + inputAlbum + "'";


            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while(rs.next()){
                String name = rs.getString("album_name");
                System.out.printf("%s\n", name);
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }


    }
}
