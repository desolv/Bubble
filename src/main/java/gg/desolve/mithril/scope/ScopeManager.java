package gg.desolve.mithril.scope;

import com.google.gson.Gson;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import gg.desolve.mithril.Mithril;
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

        Mithril.getInstance().getRedisManager().set("scope:" + scope.getName(), gson.toJson(scope));
        Mithril.getInstance().getLogger().info("New scope @ " + scope.getName() + ".");
    }

    public void save(Scope scope) {
        Mithril.getInstance().getRedisManager().set("scope:" + scope.getName(), gson.toJson(scope));

        Mithril.getInstance().getMongoManager().getMongoDatabase().getCollection("scopes")
                .replaceOne(Filters.eq("name", scope.getName()), Document.parse(gson.toJson(scope)),
                        new ReplaceOptions().upsert(true));
    }

    public void delete(Scope scope) {
        Mithril.getInstance().getRedisManager().remove("scope:" + scope.getName());
        Mithril.getInstance().getMongoManager().getMongoDatabase().getCollection("scopes")
                .deleteOne(Filters.eq("name", scope.getName()));
        Mithril.getInstance().getLogger().info("Removed scope @ " + scope.getName() + ".");
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
        return Mithril.getInstance().getRedisManager().keys("scope:*")
                .stream()
                .map(i -> gson.fromJson(i, Scope.class))
                .filter(scope -> scope.getName().equals(scopeName))
                .findFirst()
                .orElse(null);
    }

    public List<Scope> retrieve() {
        return Mithril.getInstance().getRedisManager().keys("scope:*")
                .stream()
                .map(i -> gson.fromJson(i, Scope.class))
                .toList();
    }

    public void load() {
        Mithril.getInstance().getMongoManager().getMongoDatabase().getCollection("scopes")
                .find()
                .forEach(scopeDocument -> {
                    Scope scope = gson.fromJson(scopeDocument.toJson(), Scope.class);
                    Mithril.getInstance().getRedisManager().set("scope:" + scope.getName(), gson.toJson(scope));
        });
    }
}
