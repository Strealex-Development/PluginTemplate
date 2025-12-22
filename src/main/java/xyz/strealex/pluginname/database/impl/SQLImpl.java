package xyz.strealex.pluginname.database.impl;

import xyz.strealex.pluginname.database.Database;
import xyz.strealex.pluginname.database.DatabaseProvider;
import xyz.strealex.pluginname.models.DatabasePlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

abstract class SQLImpl implements Database {

    private final DatabaseProvider provider;

    public SQLImpl(DatabaseProvider provider) {
        this.provider = provider;
    }

    @Override
    public void initialize() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS players (
                    uuid VARCHAR(36) PRIMARY KEY,
                    jsonData TEXT
                );
            """;
        try (Connection conn = provider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();
        }
    }

    @Override
    public DatabasePlayer getDatabasePlayer(String uuid) throws SQLException {
        String query = "SELECT * FROM players WHERE uuid = ?";
        try (Connection conn = provider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, uuid);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new DatabasePlayer(
                        UUID.fromString(rs.getString("uuid")),
                        rs.getString("jsonData")
                    );
                }
            }
        }

        return null;
    }

    @Override
    public void createDatabasePlayer(DatabasePlayer player) throws SQLException {
        String insert = "INSERT INTO players (uuid, jsonData) VALUES (?, ?)";
        try (Connection conn = provider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insert)) {

            stmt.setString(1, String.valueOf(player.getUuid()));
            stmt.setString(2, player.getData().toJson());
            stmt.executeUpdate();
        }
    }

    @Override
    public void updateDatabasePlayer(DatabasePlayer player) throws SQLException {
        String update = """
                UPDATE players
                SET jsonData = ?
                WHERE uuid = ?
            """;
        try (Connection conn = provider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(update)) {

            stmt.setString(1, player.getData().toJson());
            stmt.setString(2, String.valueOf(player.getUuid()));
            stmt.executeUpdate();
        }
    }

    @Override
    public void close() {
        provider.close();
    }

    public abstract boolean isExternal();
}
