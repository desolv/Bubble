package gg.desolve.bubble.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import gg.desolve.bubble.Bubble;
import lombok.Data;
import org.bson.UuidRepresentation;

@Data
public class MongoManager {

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    public MongoManager(String url, String database) {
        try {
            long now = System.currentTimeMillis();

            MongoClientSettings mongoSettings = MongoClientSettings.builder()
                    .applyConnectionString(new ConnectionString(url))
                    .uuidRepresentation(UuidRepresentation.STANDARD)
                    .build();

            mongoClient = MongoClients.create(mongoSettings);
            mongoDatabase = mongoClient.getDatabase(database);

            String timing = String.valueOf(System.currentTimeMillis() - now);
            Bubble.getInstance().getLogger().info("Merged Mongo @ " + timing + "ms.");
        } catch (Exception e) {
            Bubble.getInstance().getLogger().warning("There was a problem connecting to MongoDB.");
            e.printStackTrace();
        }
    }

    public void close() {
        mongoClient.close();
    }
}
