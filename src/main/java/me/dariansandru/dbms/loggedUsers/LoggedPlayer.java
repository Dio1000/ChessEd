package me.dariansandru.dbms.loggedUsers;

import me.dariansandru.domain.Player;
import me.dariansandru.utilities.observer.Observable;

public class LoggedPlayer {

    private static final Observable<Player> loggedPlayer = new Observable<>();

    private LoggedPlayer() {}

    public static Observable<Player> getLoggedPlayer() {
        return loggedPlayer;
    }

}
