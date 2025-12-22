package xyz.strealex.pluginname.database;

import xyz.strealex.pluginname.models.DatabasePlayer;

import java.sql.SQLException;

public interface Database {

    void initialize() throws SQLException;

    DatabasePlayer getDatabasePlayer(String uuid) throws SQLException;

    void createDatabasePlayer(DatabasePlayer player) throws SQLException;

    void updateDatabasePlayer(DatabasePlayer player) throws SQLException;

    boolean isExternal();

    void close();
}
