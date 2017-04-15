package de.nodelab.dynmc.master.db;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import de.nodelab.dynmc.master.db.obj.*;
import org.bson.Document;

import java.util.ArrayList;

public class DatabaseConnection {

    private MongoClient client;
    private MongoDatabase database;

    private AsyncDatabaseConnection asyncDatabaseConnection;

    public DatabaseConnection() {
        this.client = new MongoClient();
        this.database = this.client.getDatabase("dynmc");
        this.asyncDatabaseConnection = new AsyncDatabaseConnection(this);
    }

    public AsyncDatabaseConnection async() {
        return this.asyncDatabaseConnection;
    }

    // Daemons

    public void addDaemon(DatabaseDaemon daemon) {
        MongoCollection<Document> col = this.database.getCollection("daemons");
        col.insertOne(daemon.convertToBson());
    }

    public void updateDaemon(DatabaseDaemon daemon, DatabaseDaemon.AccessRule... rules) {
        this.updateDaemon(daemon.getName(), daemon, rules);
    }

    public void updateDaemon(String name, DatabaseDaemon daemon, DatabaseDaemon.AccessRule... rules) {
        MongoCollection<Document> col = this.database.getCollection("daemons");
        col.replaceOne(Filters.eq("name", name), daemon.getUpdateDocument(rules));
    }

    public void removeDaemon(DatabaseDaemon daemon) {
        this.removeDaemon(daemon.getName());
    }

    public void removeDaemon(String daemonName) {
        MongoCollection<Document> col = this.database.getCollection("daemons");
        col.deleteOne(Filters.eq("name", daemonName));
    }

    public ArrayList<DatabaseDaemon> getDaemons(DatabaseDaemon.AccessRule... rules) {
        MongoCollection<Document> col = this.database.getCollection("daemons");
        FindIterable<Document> i = col.find();

        if(!DatabaseDaemon.AccessRule.containsAll(rules)) {
            i.projection(Projections.include(DatabaseDaemon.AccessRule.toList(rules)));
        }

        ArrayList<DatabaseDaemon> daemons = new ArrayList<>();
        i.forEach((Block<? super Document>) (doc) -> {
           DatabaseDaemon daemon = new DatabaseDaemon();
           daemon.fillFromBson(doc);
           daemons.add(daemon);
        });

        return daemons;
    }

    public DatabaseDaemon getDaemon(String daemonName, DatabaseDaemon.AccessRule... rules) {
        MongoCollection<Document> col = this.database.getCollection("daemons");

        DatabaseDaemon daemon = new DatabaseDaemon();
        FindIterable<Document> iterable = col.find(Filters.eq("name", daemonName));

        if(!DatabaseDaemon.AccessRule.containsAll(rules)) {
            iterable.projection(Projections.include(DatabaseDaemon.AccessRule.toList(rules)));
        }

        Document doc = iterable.first();
        if(doc == null) return null;

        daemon.fillFromBson(doc);
        return daemon;
    }

    // Plugins

    public void addPlugin(DatabasePlugin plugin) {
        MongoCollection<Document> col = this.database.getCollection("plugins");
        col.insertOne(plugin.convertToBson());
    }

    public void updatePlugin(DatabasePlugin plugin, DatabasePlugin.AccessRule... rules) {
        MongoCollection<Document> col = this.database.getCollection("plugins");
        col.replaceOne(Filters.eq("name", plugin.getName()), plugin.getUpdateDocument(rules));
    }

    public void removePlugin(DatabasePlugin plugin) {
        this.removePlugin(plugin.getName());
    }

    public void removePlugin(String pluginName) {
        MongoCollection<Document> col = this.database.getCollection("plugins");
        col.deleteOne(Filters.eq("name", pluginName));
    }

    public ArrayList<DatabasePlugin> getPlugins(DatabasePlugin.AccessRule... rules) {
        MongoCollection<Document> col = this.database.getCollection("plugins");
        FindIterable<Document> i = col.find();

        if(!DatabasePlugin.AccessRule.containsAll(rules)) {
            i.projection(Projections.include(DatabasePlugin.AccessRule.toList(rules)));
        }

        ArrayList<DatabasePlugin> plugins = new ArrayList<>();
        i.forEach((Block<? super Document>) (doc) -> {
            DatabasePlugin plugin = new DatabasePlugin();
            plugin.fillFromBson(doc);
            plugins.add(plugin);
        });

        return plugins;
    }

    public DatabasePlugin getPlugin(String pluginName, DatabasePlugin.AccessRule... rules) {
        MongoCollection<Document> col = this.database.getCollection("plugins");

        DatabasePlugin plugin = new DatabasePlugin();
        FindIterable<Document> iterable = col.find(Filters.eq("name", pluginName));

        if(!DatabasePlugin.AccessRule.containsAll(rules)) {
            iterable.projection(Projections.include(DatabasePlugin.AccessRule.toList(rules)));
        }

        Document doc = iterable.first();
        if(doc == null) return null;

        plugin.fillFromBson(doc);
        return plugin;
    }

    // Servers

    public void addServer(DatabaseServer server) {
        MongoCollection<Document> col = this.database.getCollection("servers");
        col.insertOne(server.convertToBson());
    }

    public void updateServer(DatabaseServer server, DatabaseServer.AccessRule... rules) {
        MongoCollection<Document> col = this.database.getCollection("servers");
        col.updateOne(Filters.eq("name", server.getName()), server.getUpdateDocument(rules));
    }

    public void removeServer(DatabaseServer plugin) {
        this.removeServer(plugin.getName());
    }

    public void removeServer(String serverName) {
        MongoCollection<Document> col = this.database.getCollection("servers");
        col.deleteOne(Filters.eq("name", serverName));
    }

    public ArrayList<DatabaseServer> getServers(DatabaseServer.AccessRule... rules) {
        MongoCollection<Document> col = this.database.getCollection("servers");
        FindIterable<Document> i = col.find();

        if(!DatabaseServer.AccessRule.containsAll(rules)) {
            i.projection(Projections.include(DatabaseServer.AccessRule.toList(rules)));
        }

        ArrayList<DatabaseServer> servers = new ArrayList<>();
        i.forEach((Block<? super Document>) (doc) -> {
            DatabaseServer server = new DatabaseServer();
            server.fillFromBson(doc);
            servers.add(server);
        });

        return servers;
    }

    public DatabaseServer getServer(String serverName, DatabaseServer.AccessRule... rules) {
        MongoCollection<Document> col = this.database.getCollection("servers");

        DatabaseServer server = new DatabaseServer();
        FindIterable<Document> iterable = col.find(Filters.eq("name", serverName));

        if(!DatabaseServer.AccessRule.containsAll(rules)) {
            iterable.projection(Projections.include(DatabaseServer.AccessRule.toList(rules)));
        }

        Document doc = iterable.first();
        if(doc == null) return null;

        server.fillFromBson(doc);
        return server;
    }

    // ServerTypes

    public void addServerType(DatabaseServerType serverType) {
        MongoCollection<Document> col = this.database.getCollection("servertypes");
        col.insertOne(serverType.convertToBson());
    }

    public void updateServerType(DatabaseServerType serverType, DatabaseServerType.AccessRule... rules) {
        this.updateServerType(serverType.getName(), serverType, rules);
    }

    public void updateServerType(String name, DatabaseServerType serverType, DatabaseServerType.AccessRule... rules) {
        MongoCollection<Document> col = this.database.getCollection("servertypes");
        col.updateOne(Filters.eq("name", name), serverType.getUpdateDocument(rules));
    }

    public void removeServerType(DatabaseServerType serverType) {
        this.removeServerType(serverType.getName());
    }

    public void removeServerType(String serverTypeName) {
        MongoCollection<Document> col = this.database.getCollection("servertypes");
        col.deleteOne(Filters.eq("name", serverTypeName));
    }

    public ArrayList<DatabaseServerType> getServerTypes(DatabaseServerType.AccessRule... rules) {
        MongoCollection<Document> col = this.database.getCollection("servertypes");
        FindIterable<Document> i = col.find();

        if(!DatabaseServerType.AccessRule.containsAll(rules)) {
            i.projection(Projections.include(DatabaseServerType.AccessRule.toList(rules)));
        }

        ArrayList<DatabaseServerType> serverTypes = new ArrayList<>();
        i.forEach((Block<? super Document>) (doc) -> {
            DatabaseServerType serverType = new DatabaseServerType();
            serverType.fillFromBson(doc);
            serverTypes.add(serverType);
        });

        return serverTypes;
    }

    public DatabaseServerType getServerType(String serverTypeName, DatabaseServerType.AccessRule... rules) {
        MongoCollection<Document> col = this.database.getCollection("servertypes");

        DatabaseServerType serverType = new DatabaseServerType();
        FindIterable<Document> iterable = col.find(Filters.eq("name", serverTypeName));

        if(!DatabaseServerType.AccessRule.containsAll(rules)) {
            iterable.projection(Projections.include(DatabaseServerType.AccessRule.toList(rules)));
        }

        Document doc = iterable.first();
        if(doc == null) return null;

        serverType.fillFromBson(doc);
        return serverType;
    }

    // Worlds

    public void addWorld(DatabaseWorld world) {
        MongoCollection<Document> col = this.database.getCollection("worlds");
        col.insertOne(world.convertToBson());
    }

    public void updateWorld(DatabaseWorld world, DatabaseWorld.AccessRule... rules) {
        this.updateWorld(world.getName(), world, rules);
    }

    public void updateWorld(String name, DatabaseWorld world, DatabaseWorld.AccessRule... rules) {
        MongoCollection<Document> col = this.database.getCollection("worlds");
        col.updateOne(Filters.eq("name", name), world.getUpdateDocument(rules));
    }

    public void removeWorld(DatabaseWorld world) {
        this.removeWorld(world.getName());
    }

    public void removeWorld(String worldName) {
        MongoCollection<Document> col = this.database.getCollection("worlds");
        col.deleteOne(Filters.eq("name", worldName));
    }

    public ArrayList<DatabaseWorld> getWorlds(DatabaseWorld.AccessRule... rules) {
        MongoCollection<Document> col = this.database.getCollection("worlds");
        FindIterable<Document> i = col.find();

        if(!DatabaseWorld.AccessRule.containsAll(rules)) {
            i.projection(Projections.include(DatabaseWorld.AccessRule.toList(rules)));
        }

        ArrayList<DatabaseWorld> worlds = new ArrayList<>();
        i.forEach((Block<? super Document>) (doc) -> {
            DatabaseWorld world = new DatabaseWorld();
            world.fillFromBson(doc);
            worlds.add(world);
        });

        return worlds;
    }

    public DatabaseWorld getWorld(String worldName, DatabaseWorld.AccessRule... rules) {
        MongoCollection<Document> col = this.database.getCollection("worlds");

        DatabaseWorld world = new DatabaseWorld();
        FindIterable<Document> iterable = col.find(Filters.eq("name", worldName));

        if(!DatabaseWorld.AccessRule.containsAll(rules)) {
            iterable.projection(Projections.include(DatabaseWorld.AccessRule.toList(rules)));
        }

        Document doc = iterable.first();
        if(doc == null) return null;

        world.fillFromBson(doc);
        return world;
    }

}
