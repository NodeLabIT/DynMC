package de.nodelab.dynmc.master.db;

import de.nodelab.dynmc.master.db.obj.*;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class AsyncDatabaseConnection {

    private DatabaseConnection connection;

    private ExecutorService exec;

    public AsyncDatabaseConnection(DatabaseConnection connection) {
        this.connection = connection;
        this.exec = Executors.newCachedThreadPool();
    }

    // Daemons

    public void addDaemon(DatabaseDaemon daemon, Runnable cb) {
        this.exec.submit(() -> {
            AsyncDatabaseConnection.this.connection.addDaemon(daemon);
            cb.run();
        });
    }

    public void updateDaemon(DatabaseDaemon daemon, Runnable cb, DatabaseDaemon.AccessRule... rules) {
        this.exec.submit(() -> {
            AsyncDatabaseConnection.this.connection.updateDaemon(daemon, rules);
            cb.run();
        });
    }

    public void removeDaemon(DatabaseDaemon daemon, Runnable cb) {
        this.removeDaemon(daemon.getName(), cb);
    }

    public void removeDaemon(String daemonName, Runnable cb) {
        this.exec.submit(() -> {
           AsyncDatabaseConnection.this.connection.removeDaemon(daemonName);
           cb.run();
        });
    }

    public void getDaemons(Consumer<ArrayList<DatabaseDaemon>> cb, DatabaseDaemon.AccessRule... rules) {
        this.exec.submit(() -> {
           cb.accept(AsyncDatabaseConnection.this.connection.getDaemons(rules));
        });
    }

    public void getDaemon(String daemon, Consumer<DatabaseDaemon> cb, DatabaseDaemon.AccessRule... rules) {
        this.exec.submit(() -> {
           cb.accept(AsyncDatabaseConnection.this.connection.getDaemon(daemon, rules));
        });
    }

    // Plugins

    public void addPlugin(DatabasePlugin plugin, Runnable cb) {
        this.exec.submit(() -> {
            AsyncDatabaseConnection.this.connection.addPlugin(plugin);
            cb.run();
        });
    }

    public void updatePlugin(DatabasePlugin plugin, Runnable cb, DatabasePlugin.AccessRule... rules) {
        this.exec.submit(() -> {
            AsyncDatabaseConnection.this.connection.updatePlugin(plugin, rules);
            cb.run();
        });
    }

    public void removePlugin(DatabasePlugin plugin, Runnable cb) {
        this.removePlugin(plugin.getName(), cb);
    }

    public void removePlugin(String pluginName, Runnable cb) {
        this.exec.submit(() -> {
            AsyncDatabaseConnection.this.connection.removePlugin(pluginName);
            cb.run();
        });
    }

    public void getPlugins(Consumer<ArrayList<DatabasePlugin>> cb, DatabasePlugin.AccessRule... rules) {
        this.exec.submit(() -> {
            cb.accept(AsyncDatabaseConnection.this.connection.getPlugins(rules));
        });
    }

    public void getPlugins(String plugin, Consumer<DatabasePlugin> cb, DatabasePlugin.AccessRule... rules) {
        this.exec.submit(() -> {
            cb.accept(AsyncDatabaseConnection.this.connection.getPlugin(plugin, rules));
        });
    }

    // Servers

    public void addServer(DatabaseServer server, Runnable cb) {
        this.exec.submit(() -> {
            AsyncDatabaseConnection.this.connection.addServer(server);
            cb.run();
        });
    }

    public void updateServer(DatabaseServer server, Runnable cb, DatabaseServer.AccessRule... rules) {
        this.exec.submit(() -> {
            AsyncDatabaseConnection.this.connection.updateServer(server, rules);
            cb.run();
        });
    }

    public void removeServer(DatabaseServer server, Runnable cb) {
        this.removeServer(server.getName(), cb);
    }

    public void removeServer(String serverName, Runnable cb) {
        this.exec.submit(() -> {
            AsyncDatabaseConnection.this.connection.removeServer(serverName);
            cb.run();
        });
    }

    public void getServers(Consumer<ArrayList<DatabaseServer>> cb, DatabaseServer.AccessRule... rules) {
        this.exec.submit(() -> {
            cb.accept(AsyncDatabaseConnection.this.connection.getServers(rules));
        });
    }

    public void getServer(String server, Consumer<DatabaseServer> cb, DatabaseServer.AccessRule... rules) {
        this.exec.submit(() -> {
            cb.accept(AsyncDatabaseConnection.this.connection.getServer(server, rules));
        });
    }

    // ServerTypes

    public void addServerType(DatabaseServerType serverType, Runnable cb) {
        this.exec.submit(() -> {
            AsyncDatabaseConnection.this.connection.addServerType(serverType);
            cb.run();
        });
    }

    public void updateServerType(DatabaseServerType serverType, Runnable cb, DatabaseServerType.AccessRule... rules) {
        this.exec.submit(() -> {
            AsyncDatabaseConnection.this.connection.updateServerType(serverType, rules);
            cb.run();
        });
    }

    public void removeServerType(DatabaseServerType serverType, Runnable cb) {
        this.removeServerType(serverType.getName(), cb);
    }

    public void removeServerType(String serverTypeName, Runnable cb) {
        this.exec.submit(() -> {
            AsyncDatabaseConnection.this.connection.removeServerType(serverTypeName);
            cb.run();
        });
    }

    public void getServerTypes(Consumer<ArrayList<DatabaseServerType>> cb, DatabaseServerType.AccessRule... rules) {
        this.exec.submit(() -> {
            cb.accept(AsyncDatabaseConnection.this.connection.getServerTypes(rules));
        });
    }

    public void getServerType(String serverType, Consumer<DatabaseServerType> cb, DatabaseServerType.AccessRule... rules) {
        this.exec.submit(() -> {
            cb.accept(AsyncDatabaseConnection.this.connection.getServerType(serverType, rules));
        });
    }

    // Worlds

    public void addWorld(DatabaseWorld world, Runnable cb) {
        this.exec.submit(() -> {
            AsyncDatabaseConnection.this.connection.addWorld(world);
            cb.run();
        });
    }

    public void updateWorld(DatabaseWorld world, Runnable cb, DatabaseWorld.AccessRule... rules) {
        this.exec.submit(() -> {
            AsyncDatabaseConnection.this.connection.updateWorld(world, rules);
            cb.run();
        });
    }

    public void removeWorld(DatabaseWorld world, Runnable cb) {
        this.removeServerType(world.getName(), cb);
    }

    public void removeWorld(String worldName, Runnable cb) {
        this.exec.submit(() -> {
            AsyncDatabaseConnection.this.connection.removeWorld(worldName);
            cb.run();
        });
    }

    public void getWorlds(Consumer<ArrayList<DatabaseWorld>> cb, DatabaseWorld.AccessRule... rules) {
        this.exec.submit(() -> {
            cb.accept(AsyncDatabaseConnection.this.connection.getWorlds(rules));
        });
    }

    public void getWorld(String world, Consumer<DatabaseWorld> cb, DatabaseWorld.AccessRule... rules) {
        this.exec.submit(() -> {
            cb.accept(AsyncDatabaseConnection.this.connection.getWorld(world, rules));
        });
    }

}
