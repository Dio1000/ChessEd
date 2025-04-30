package me.dariansandru.dbms;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class DBCreator {

    public static void createTables(){
        createPlayersTable();
        createAdminsTable();
        createAdminPermissionsTable();
        createReportsTable();
        createGamesTable();
        createMovesTable();
        createBannedPlayersTable();
    }

    public static void truncateTables(){
        String truncatePlayers = "TRUNCATE TABLE players";
        String truncateAdmins = "TRUNCATE TABLE admins";
        String truncateAdminPermissions = "TRUNCATE TABLE admin_permissions";
        String truncateReports = "TRUNCATE TABLE reports";
        String truncateGames = "TRUNCATE TABLE games";
        String truncateMoves = "TRUNCATE TABLE moves";
        String truncateBannedPlayers = "TRUNCATE TABLE BannedPlayers";

        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(truncatePlayers);
            statement.execute(truncateAdmins);
            statement.execute(truncateAdminPermissions);
            statement.execute(truncateReports);
            statement.execute(truncateGames);
            statement.execute(truncateMoves);
            statement.execute(truncateBannedPlayers);
            System.out.println("Tables truncated successfully!.");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteTables() {
        String dropPlayers = "DROP TABLE IF EXISTS players";
        String dropAdmins = "DROP TABLE IF EXISTS admins";
        String dropAdminPermissions = "DROP TABLE IF EXISTS admin_permissions";
        String dropReports = "DROP TABLE IF EXISTS reports";
        String dropGames = "DROP TABLE IF EXISTS games";
        String dropMoves = "DROP TABLE IF EXISTS moves";
        String dropBannedPlayers = "DROP TABLE IF EXISTS BannedPlayers";

        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(dropPlayers);
            statement.execute(dropAdmins);
            statement.execute(dropAdminPermissions);
            statement.execute(dropReports);
            statement.execute(dropGames);
            statement.execute(dropMoves);
            statement.execute(dropBannedPlayers);
            System.out.println("All tables deleted successfully!");

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting tables: " + e.getMessage(), e);
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

    private static void createGamesTable() {
        String createTableSQL = """
                CREATE TABLE IF NOT EXISTS games (
                game_id INT AUTO_INCREMENT PRIMARY KEY,
                white_piece_player_id INT NOT NULL,
                black_piece_player_id INT NOT NULL,
                result VARCHAR(255) NOT NULL);
                """;

        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement()){

            statement.execute(createTableSQL);
            System.out.println("Table 'games' created successfully.");

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private static void createMovesTable() {
        String createTableSQL = """
                CREATE TABLE IF NOT EXISTS moves (
                move_id INT AUTO_INCREMENT PRIMARY KEY,
                game_id INT NOT NULL,
                player_id INT NOT NULL,
                move_number INT NOT NULL,
                notation VARCHAR(255) NOT NULL);""";

        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement()){

            statement.execute(createTableSQL);
            System.out.println("Table 'moves' created successfully.");

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static void createBannedPlayersTable() {
        String createTableSQL = """
        CREATE TABLE IF NOT EXISTS BannedPlayers (
            username VARCHAR(255) PRIMARY KEY,
            isBanned BOOLEAN DEFAULT FALSE
        );
        """;

        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(createTableSQL);
            System.out.println("BannedPlayers table created successfully.");

        } catch (SQLException e) {
            System.err.println("Error creating BannedPlayers table: " + e.getMessage());
        }
    }

    public static void showAllData() {
        String[] tableNames = {
                "players",
                "admins",
                "admin_permissions",
                "reports",
                "games",
                "moves",
                "BannedPlayers"
        };

        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement()) {

            for (String tableName : tableNames) {
                System.out.println("\nData from table: " + tableName);
                String query = "SELECT * FROM " + tableName;

                try (ResultSet resultSet = statement.executeQuery(query)) {
                    int columnCount = resultSet.getMetaData().getColumnCount();

                    for (int i = 1; i <= columnCount; i++) {
                        System.out.print(resultSet.getMetaData().getColumnName(i) + "\t");
                    }
                    System.out.println();

                    while (resultSet.next()) {
                        for (int i = 1; i <= columnCount; i++) {
                            System.out.print(resultSet.getString(i) + "\t");
                        }
                        System.out.println();
                    }
                } catch (SQLException e) {
                    System.out.println("Error fetching data from table " + tableName + ": " + e.getMessage());
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving data from tables: " + e.getMessage(), e);
        }
    }

}
