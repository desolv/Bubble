package gg.desolve.api;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import gg.desolve.api.blueprint.Logger;
import lombok.Getter;
import org.bson.UuidRepresentation;

@Getter
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

            Logger.info("Connected to MongoDB in " + (System.currentTimeMillis() - start) + "ms.");
        } catch (Exception e) {
            Logger.error("There was a problem connecting to MongoDB.");
            e.printStackTrace();
        }
    }
}
