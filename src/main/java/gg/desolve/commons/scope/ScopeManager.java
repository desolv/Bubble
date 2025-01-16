package gg.desolve.commons.scope;

import com.google.gson.Gson;
import com.mongodb.client.model.ReplaceOptions;
import gg.desolve.commons.Commons;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class ScopeManager {

    private static final Gson gson = new Gson();

    public ScopeManager() {
        load();
    }

    public void create(String name) {
        Scope scope = new Scope(name, new ArrayList<>());

        Commons.getInstance().getRedisManager().set("scope:" + scope.getName(), gson.toJson(scope));
        Commons.getInstance().getLogger().info("New scope @ " + scope.getName() + ".");
    }

    public void save(Scope scope) {
        Commons.getInstance().getRedisManager().set("scope:" + scope.getName(), gson.toJson(scope));
    }

    public void delete(Scope scope) {
        Commons.getInstance().getRedisManager().remove("scope:" + scope.getName());
        Commons.getInstance().getLogger().info("Removed scope @ " + scope.getName() + ".");
    }

    public void add(Scope scope, String instance) {
        scope.getInstances().add(instance);
        save(scope);
    }

    public void remove(Scope scope, String instance) {
        scope.getInstances().remove(instance);
        save(scope);
    }

    public Scope retrieve(String scopeName) {
        return Commons.getInstance().getRedisManager().keys("scope:*")
                .stream()
                .map(i -> gson.fromJson(i, Scope.class))
                .filter(scope -> scope.getName().equals(scopeName))
                .findFirst()
                .orElse(null);
    }

    public List<Scope> retrieve() {
        return Commons.getInstance().getRedisManager().keys("scope:*")
                .stream()
                .map(i -> gson.fromJson(i, Scope.class))
                .toList();
    }

    public void load() {
        Commons.getInstance().getMongoManager().getMongoDatabase().getCollection("scopes")
                .find()
                .forEach(scopeDocument -> {
                    Scope scope = gson.fromJson(scopeDocument.toJson(), Scope.class);
                    Commons.getInstance().getRedisManager().set("scope:" + scope.getName(), gson.toJson(scope));
        });
    }

    public void save() {
        Commons.getInstance().getRedisManager().keys("scope:*")
                .forEach(scopeKey -> {
                    Scope scope = gson.fromJson(Commons.getInstance().getRedisManager().get(scopeKey), Scope.class);
                    Commons.getInstance().getMongoManager().getMongoDatabase().getCollection("scopes")
                            .replaceOne(
                                    new Document("name", scope.getName()),
                                    Document.parse(gson.toJson(scope)),
                                    new ReplaceOptions().upsert(true));
                });
    }


}
