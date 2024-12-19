package me.dariansandru.dbms.loggedUsers;

import me.dariansandru.domain.Admin;
import me.dariansandru.utilities.observer.Observable;

public class LoggedAdmin {

    private static final Observable<Admin> loggedAdmin = new Observable<>();

    private LoggedAdmin() {}

    public static Observable<Admin> getLoggedAdmin() {
        return loggedAdmin;
    }

}
