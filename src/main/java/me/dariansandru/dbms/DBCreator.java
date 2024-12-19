package me.dariansandru.dbms;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class DBCreator {

    public static void createTables(){
        createPlayersTable();
        createAdminsTable();
        createAdminPermissionsTable();
        createReportsTable();
    }

    public static void truncateTables(){
        String truncatePlayers = "TRUNCATE TABLE players";
        String truncateAdmins = "TRUNCATE TABLE admins";
        String truncateAdminPermissions = "TRUNCATE TABLE admin_permissions";

        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(truncatePlayers);
            statement.execute(truncateAdmins);
            statement.execute(truncateAdminPermissions);
            System.out.println("Tables truncated successfully!.");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void createPlayersTable() {
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS players (
                player_id INT AUTO_INCREMENT PRIMARY KEY,
                username VARCHAR(100) NOT NULL UNIQUE,
                email VARCHAR(100) NOT NULL UNIQUE,
                password VARCHAR(50) NOT NULL,
                rating INT NOT NULL
            );
        """;

        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(createTableSQL);
            System.out.println("Table 'players' created successfully.");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void createAdminsTable() {
        String createTableSQL = """
                CREATE TABLE IF NOT EXISTS admins (
                admin_id INT AUTO_INCREMENT PRIMARY KEY,
                username VARCHAR(100) NOT NULL UNIQUE,
                email VARCHAR(100) NOT NULL UNIQUE,
                password VARCHAR(50) NOT NULL
                );""";

        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(createTableSQL);
            System.out.println("Table 'admins' created successfully.");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void createAdminPermissionsTable() {
        String createTableSQL = """
                CREATE TABLE IF NOT EXISTS admin_permissions (
                permission_id INT AUTO_INCREMENT PRIMARY KEY,
                admin_id INT,
                permission_name VARCHAR(100) NOT NULL
                );
                """;

        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(createTableSQL);
            System.out.println("Table 'AdminPermissions' created successfully.");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void createReportsTable() {
        String createTableSQL = """
                CREATE TABLE IF NOT EXISTS reports (
                report_id INT AUTO_INCREMENT PRIMARY KEY,
                report_text VARCHAR(255) NOT NULL,
                admin_id INT NOT NULL,
                player_id INT NOT NULL);""";

        try (Connection connection = DBConnection.getConnection();
            Statement statement = connection.createStatement()){

            statement.execute(createTableSQL);
            System.out.println("Table 'reports' created successfully.");

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
