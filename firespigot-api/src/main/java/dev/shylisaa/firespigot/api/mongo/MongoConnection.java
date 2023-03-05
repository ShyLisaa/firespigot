package dev.shylisaa.firespigot.api.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import dev.shylisaa.firespigot.api.mongo.builder.MongoConnectionBuilder;
import org.bson.Document;
import org.jetbrains.annotations.ApiStatus;

import java.util.concurrent.CompletableFuture;

@ApiStatus.Experimental
public class MongoConnection {

    private MongoClient client;

    public MongoConnection(MongoConnectionBuilder connectionBuilder) {
        try {
            client = MongoClients.create(new ConnectionString(connectionBuilder.getConnectionString()));
            System.out.println("Connected to MongoDB");
        } catch (Exception exception) {
            System.out.println("Failed to connect to MongoDB");
            exception.printStackTrace();
        }
    }

    public MongoClient getClient() {
        return client;
    }

    public CompletableFuture<MongoDatabase> getDatabase(String name) {
        return CompletableFuture.supplyAsync(() -> client.getDatabase(name));
    }

    public CompletableFuture<InsertOneResult> insertOne(MongoCollection<Document> collection, Document insertDocument) {
        return CompletableFuture.supplyAsync(() -> collection.insertOne(insertDocument));
    }

    public void close() {
        client.close();
    }
}
