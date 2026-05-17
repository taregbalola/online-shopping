package com.tareg.onlineshopping.model;

public class User {
    private long id;
    private String username;
    private String passwordHash;
    private String fullName;
    private String role;

    public User(long id, String username, String passwordHash, String fullName, String role) {
        this.id           = id;
        this.username     = username;
        this.passwordHash = passwordHash;
        this.fullName     = fullName;
        this.role         = role;
    }

    public long   getId()           { return id; }
    public String getUsername()     { return username; }
    public String getPasswordHash() { return passwordHash; }
    public String getFullName()     { return fullName; }
    public String getRole()         { return role; }
    public boolean isAdmin()        { return "ADMIN".equals(role); }
}

