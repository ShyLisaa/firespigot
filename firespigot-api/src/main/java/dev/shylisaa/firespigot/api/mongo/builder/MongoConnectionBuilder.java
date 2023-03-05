package dev.shylisaa.firespigot.api.mongo.builder;

public class MongoConnectionBuilder {

    private String connectionString;

    private MongoConnectionBuilder(String connectionString) {
        this.connectionString = connectionString;
    }

    public static MongoConnectionBuilder builder(String connectionString) {
        return new MongoConnectionBuilder(connectionString);
    }

    public String getConnectionString() {
        return connectionString;
    }
}
