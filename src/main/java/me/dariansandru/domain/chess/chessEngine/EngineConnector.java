package me.dariansandru.domain.chess.chessEngine;

import java.io.*;
import java.net.Socket;

public class EngineConnector {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private final String host;
    private final int port;
    private final int timeoutMs;

    public EngineConnector(String host, int port, int timeoutMs) {
        this.host = host;
        this.port = port;
        this.timeoutMs = timeoutMs;
    }

    public void connect() throws IOException {
        socket = new Socket(host, port);
        socket.setSoTimeout(timeoutMs);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public String getBestMove(String fen) throws IOException {
        out.println("FEN " + fen);
        String response = in.readLine();
        if (response != null && response.startsWith("MOVE ")) {
            return response.substring(5).trim();
        }
        throw new IOException("Invalid engine response: " + response);
    }

    public void disconnect() {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            System.err.println("Error closing engine connection: " + e.getMessage());
        }
    }
}