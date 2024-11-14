package me.dariansandru.utilities;

import me.dariansandru.domain.Player;
import me.dariansandru.round.ChessRound;
import me.dariansandru.domain.chess.Piece;
import me.dariansandru.domain.chess.PieceColour;
import me.dariansandru.domain.chess.piece.*;
import me.dariansandru.io.InputDevice;
import me.dariansandru.io.OutputDevice;
import me.dariansandru.io.exception.InputException;

import java.io.File;
import java.util.*;

public abstract class ChessUtils {

    public static InputDevice inputDevice = new InputDevice();
    public static OutputDevice outputDevice = new OutputDevice();

    public static int getNumber(char chr){
        return chr - 'a';
    }

    public static String getLetter(int num){
        return String.valueOf((char) ('a' + num));
    }

    public static boolean isValidPiece(char piece){
        char[] validPieces = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'P', 'K', 'Q', 'R', 'B', 'N', ' '};

        for (char p : validPieces){
            if (Objects.equals(p, piece)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isValidPiece(String piece){
        char[] validPieces = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'P', 'K', 'Q', 'R', 'B', 'N', ' '};
        char _piece = piece.charAt(0);

        for (char p : validPieces){
            if (Objects.equals(p, _piece)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isValidNonEmptyPiece(String piece){
        char[] validPieces = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'K', 'Q', 'R', 'B', 'N'};
        char _piece = piece.charAt(0);

        for (char p : validPieces){
            if (Objects.equals(p, _piece)) {
                return true;
            }
        }
        return false;
    }

    public static Pair<Integer, Integer> charsToMovePair(char col, char row){
        return new Pair<>(col - 'a', row - '1');
    }

    // Inshallah
    public static Pair<Integer, Integer> getColRow(String move){
        if (move.length() == 2){
            return charsToMovePair(move.charAt(0), move.charAt(1));
        }

        else if (move.length() == 3){
            if (move.charAt(0) == 'x' || isValidPiece(move.charAt(0))){
                return charsToMovePair(move.charAt(1), move.charAt(2));
            }

            if (move.charAt(2) == '+' || move.charAt(2) == '#'){
                return charsToMovePair(move.charAt(0), move.charAt(1));
            }
        }

        else if (move.length() == 4){
            if (move.charAt(1) == 'x'){
                return charsToMovePair(move.charAt(2), move.charAt(3));
            }

            if (move.charAt(3) == '+' || move.charAt(3) == '#'){
                return charsToMovePair(move.charAt(1), move.charAt(2));
            }
        }

        else if (move.length() == 5){

        }

        return new Pair<>(-1, -1);
    }

    public static Piece getPiece(String representation, PieceColour pieceColour) throws InputException {
        return switch (representation) {
            case "B" -> new Bishop(pieceColour);
            case "K" -> new King(pieceColour);
            case "N" -> new Knight(pieceColour);
            case "P" -> new Pawn(pieceColour);
            case "Q" -> new Queen(pieceColour);
            case "R" -> new Rook(pieceColour);
            default -> new EmptyPiece();
        };
    }

    public static String getPieceDisplay(String piece, PieceColour colour) throws InputException {
        String readFile = "files/chessPieceRepresentation.txt";
        List<String> list;

        if (new File(readFile).exists()){
            list = inputDevice.readFile(readFile);
        }
        else{
            return "Error";
        }

        for (String line : list){
            if (line.isEmpty()) continue;
            if (line.split(":").length != 2) continue;

            String details = line.split(":")[0].strip();
            String display = line.split(":")[1].strip();

            String pieceName = "EmptyPiece";
            String pieceColour = "None";

            if (!Objects.equals(details, "EmptyPiece")){
                pieceName = details.split("\\.")[1].strip();
                pieceColour = details.split("\\.")[0].strip();
            }

            if (Objects.equals(piece, "None") && Objects.equals(pieceColour, "None")) return display;
            if (Objects.equals(piece, pieceName) && Objects.equals(pieceColour, colour.toString())) return display;
        }

        return "Error";
    }

    public static Player getHighestRatedPlayer(Set<Player> playerSet){
        Player highestRatedPlayer = null;

        for (Player player : playerSet){
            if (highestRatedPlayer == null){
                highestRatedPlayer = player;
            }
            else if (player.compareTo(highestRatedPlayer) > 0){
                highestRatedPlayer = player;
            }
        }

        return highestRatedPlayer;
    }

    public static Player getLowestRatedPlayer(Set<Player> playerSet){
        Player lowestRatedPlayer = null;

        for (Player player : playerSet){
            if (lowestRatedPlayer == null){
                lowestRatedPlayer = player;
            }
            else if (player.compareTo(lowestRatedPlayer) < 0){
                lowestRatedPlayer = player;
            }
        }

        return lowestRatedPlayer;
    }

    public static void getPlayerRanking(List<Player> playerList){
        int length = playerList.size();

        for (int i = 0 ; i < length - 1 ; i++){
            int min_index = i;

            for (int j = i + 1; j < length ; j++){
                if (playerList.get(j).compareTo(playerList.get(min_index)) < 0) min_index = j;
            }

            Player swap = playerList.get(i);
            playerList.set(i, playerList.get(min_index));
            playerList.set(min_index, swap);
        }
    }

    private static int getColourMaterial(ChessRound chessRound, PieceColour colour){
        Piece[][] pieces = chessRound.getPieces();
        int totalPoints = 0;

        for (int row = 0 ; row < 8 ; row++){
            for (int col = 0 ; col < 8 ; col++){
                if (!Objects.equals(pieces[row][col].getName(), "King") &&
                    pieces[row][col].getColour() == colour)
                    totalPoints += pieces[row][col].getPoints();
            }
        }

        return totalPoints;
    }

    public static int getColourMaterialAdvantage(ChessRound chessRound, PieceColour colour){
        int whitePiecePlayerPoints = getColourMaterial(chessRound, PieceColour.WHITE);
        int blackPiecePlayerPoints = getColourMaterial(chessRound, PieceColour.BLACK);

        if (colour == PieceColour.WHITE){
            return Math.max(whitePiecePlayerPoints - blackPiecePlayerPoints, 0);
        }
        else if (colour == PieceColour.BLACK){
            return Math.max(blackPiecePlayerPoints - whitePiecePlayerPoints, 0);
        }
        else{
            throw new IllegalArgumentException(colour.toString() + " is not a valid colour!");
        }
    }

}
