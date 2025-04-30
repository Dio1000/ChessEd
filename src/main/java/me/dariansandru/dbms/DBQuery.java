package me.dariansandru.dbms;

import me.dariansandru.domain.Admin;
import me.dariansandru.domain.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBQuery {
    public static List<Player> fetchPlayers() {
        String querySQL = "SELECT * FROM players";
        List<Player> players = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(querySQL);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int playerId = resultSet.getInt("player_id");
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                int rating = resultSet.getInt("rating");

                List<String> data = new ArrayList<>();

                data.add(String.valueOf(playerId));
                data.add(username);
                data.add(email);
                data.add(String.valueOf(rating));

                Player player = new Player();
                DBQuery.setPlayerData(player, data);
                players.add(player);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return players;
    }

    public static List<String> getDataByUsername(String _username) {
        String querySQL = "SELECT * FROM players WHERE username = ?";
        List<String> resultList = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(querySQL)) {

            preparedStatement.setString(1, _username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String playerId = String.valueOf(resultSet.getInt("player_id"));
                    String username = resultSet.getString("username");
                    String email = resultSet.getString("email");
                    String password = resultSet.getString("password");
                    String rating = String.valueOf(resultSet.getInt("rating"));

                    resultList.add(playerId);
                    resultList.add(username);
                    resultList.add(email);
                    resultList.add(password);
                    resultList.add(rating);
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return resultList;
    }

    public static List<String> getAdminDataByUsername(String _username) {
        String querySQL = "SELECT * FROM admins WHERE username = ?";
        List<String> resultList = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(querySQL)) {

            preparedStatement.setString(1, _username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String adminID = String.valueOf(resultSet.getInt("admin_id"));
                    String username = resultSet.getString("username");
                    String email = resultSet.getString("email");
                    String password = resultSet.getString("password");

                    resultList.add(adminID);
                    resultList.add(username);
                    resultList.add(email);
                    resultList.add(password);
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return resultList;
    }

    public static boolean playerExists(String _username) {
        String querySQL = "SELECT * FROM players WHERE username = ?";

        try(Connection connection = DBConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(querySQL)
                ){

            preparedStatement.setString(1, _username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return true;
                }
            }
            return false;

        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    public static void setPlayerData(Player loggedPlayer, List<String> loggedPlayerData){
        loggedPlayer.setUsername(loggedPlayerData.get(1));
        loggedPlayer.setEmail(loggedPlayerData.get(2));
        loggedPlayer.setPassword(loggedPlayerData.get(3));
        loggedPlayer.setRating(Integer.parseInt(loggedPlayerData.get(4)));
    }

    public static void setAdminData(Admin loggedAdmin, List<String> loggedAdminData){
        loggedAdmin.setUsername(loggedAdminData.get(1));
        loggedAdmin.setEmail(loggedAdminData.get(2));
        loggedAdmin.setPassword(loggedAdminData.get(3));
    }

    public static List<String> getAllUsernames(){
        String querySQL = "SELECT username FROM players";
        List<String> resultList = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(querySQL);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                resultList.add(resultSet.getString("username"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return resultList;
    }

    public static List<String> getAllAdminUsernames(){
        String querySQL = "SELECT username FROM admins";
        List<String> resultList = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(querySQL);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                resultList.add(resultSet.getString("username"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return resultList;
    }

    public static boolean hasPermission(String adminUsername, String permission){
        String querySQL = "SELECT COUNT(?) FROM admins WHERE username = ?";

        try (Connection connection = DBConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(querySQL);
            ResultSet resultSet = preparedStatement.executeQuery()) {

            preparedStatement.setString(1, permission);
            preparedStatement.setString(2, adminUsername);

            return resultSet.next();

        } catch (SQLException e){
            System.out.println(e.getMessage());
        }

        return false;
    }

    public static boolean isBanned(String username) {
        String query = "SELECT isBanned FROM BannedPlayers WHERE username = ?";
        boolean isBanned = false;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int isBannedValue = resultSet.getInt("isBanned");
                    isBanned = (isBannedValue == 1);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error checking banned status: " + e.getMessage());
        }

        return isBanned;
    }

    public static void banPlayer(String username) {
        String insertOrUpdateSQL = """
        INSERT INTO BannedPlayers (username, isBanned)
        VALUES (?, TRUE)
        ON DUPLICATE KEY UPDATE isBanned = TRUE;
    """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertOrUpdateSQL)) {

            preparedStatement.setString(1, username);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Player " + username + " has been banned successfully.");
            } else {
                System.out.println("Failed to ban player " + username + ".");
            }

        } catch (SQLException e) {
            System.err.println("Error banning player: " + e.getMessage());
        }
    }

}
