package gg.desolve.bubble.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import gg.desolve.bubble.Bubble;
import lombok.Data;
import org.bson.Document;
import org.bson.UuidRepresentation;

@Data
public class MongoManager {

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    public MongoManager(String url, String database) {
        try {
            long start = System.currentTimeMillis();

            MongoClientSettings mongoSettings = MongoClientSettings.builder()
                    .applyConnectionString(new ConnectionString(url))
                    .uuidRepresentation(UuidRepresentation.STANDARD)
                    .build();

            mongoClient = MongoClients.create(mongoSettings);
            mongoDatabase = mongoClient.getDatabase(database);

            Bubble.getInstance().getLogger().info("Connected to MongoDB in " + (System.currentTimeMillis() - start) + "ms.");
        } catch (Exception e) {
            Bubble.getInstance().getLogger().warning("There was a problem connecting to MongoDB.");
            e.printStackTrace();
        }
    }

    public MongoCollection<Document> getCollection(String name) {
        return mongoDatabase.getCollection(name);
    }

    public void close() {
        mongoClient.close();
    }
}
