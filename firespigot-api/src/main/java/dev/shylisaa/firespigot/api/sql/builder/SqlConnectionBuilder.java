package dev.shylisaa.firespigot.api.sql.builder;

public class SqlConnectionBuilder {

    private final String host;
    private String username, password, database;
    private final int port;

    private SqlConnectionBuilder(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static SqlConnectionBuilder builder(String host, int port) {
        return new SqlConnectionBuilder(host, port);
    }


    public String getHost() {
        return host;
    }

    public String getUsername() {
        return username;
    }

    public String getDatabase() {
        return database;
    }

    public String getPassword() {
        return password;
    }

    public int getPort() {
        return port;
    }

    public SqlConnectionBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public SqlConnectionBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public SqlConnectionBuilder setDatabase(String database) {
        this.database = database;
        return this;
    }
}
