package me.dariansandru.domain;

public class Player implements Comparable<Player> {

    private String username;
    private int wins;
    private int losses;
    private float winLossRatio;

    public Player(){
        setWinLossRatio();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public float getWinLossRatio() {
        return winLossRatio;
    }

    public void setWinLossRatio() {
        if (this.wins + this.losses == 0) this.winLossRatio = 0;
        else this.winLossRatio = (float) ((100 * wins) / (wins + losses));
    }

    @Override
    public int compareTo(Player other) {
        if (this.winLossRatio != other.winLossRatio)
            return Float.compare(this.winLossRatio, other.winLossRatio);
        else {
            return Integer.compare(this.wins, other.wins);
        }
    }
}
