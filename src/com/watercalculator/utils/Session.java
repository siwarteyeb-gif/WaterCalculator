package com.watercalculator.utils;

import com.watercalculator.models.User;

public class Session {
    private static User currentUser = null;

    public static User getCurrentUser()         { return currentUser; }
    public static void setCurrentUser(User u)   { currentUser = u; }
    public static void clear()                  { currentUser = null; }
    public static boolean isLoggedIn()          { return currentUser != null; }
}
