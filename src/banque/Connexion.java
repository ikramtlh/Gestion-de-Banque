package banque;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connexion {
    private static final String URL = "jdbc:mysql://localhost:3306/gestion_banque";
    private static final String USER = "root";
    private static final String PASSWORD = "ikram"; 
    
    private static Connection connection = null;
    
    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Connexion a la base de donnees reussie");
            } catch (ClassNotFoundException | SQLException e) {
                System.out.println("Erreur de connexion : " + e.getMessage());
            }
        }
        return connection;
    }
}