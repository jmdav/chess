package client;

public record SessionData(String authToken, String username, State state) {}
