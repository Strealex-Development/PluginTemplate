package xyz.strealex.pluginname.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import xyz.strealex.pluginname.PluginName;
import xyz.strealex.pluginname.config.ConfigManager;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseProvider {

    private final PluginName plugin;
    private final ConfigManager config;
    private final DatabaseType databaseType;
    private HikariDataSource dataSource;

    public DatabaseProvider(PluginName plugin, ConfigManager config) {
        this.plugin = plugin;
        this.config = config;
        this.databaseType = parseDatabaseType();
        initializeDataSource();
    }

    private DatabaseType parseDatabaseType() {
        String type = config.getSettings().getType().toLowerCase();
        return switch (type) {
            case "mysql" -> DatabaseType.MYSQL;
            case "sqlite" -> DatabaseType.SQLITE;
            default -> throw new IllegalArgumentException("Unsupported database type: " + type);
        };
    }

    private void initializeDataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        String jdbcUrl = getJdbcUrl();

        hikariConfig.setJdbcUrl(jdbcUrl);
        hikariConfig.setUsername(config.getSettings().getUser());
        hikariConfig.setPassword(config.getSettings().getPassword());

        hikariConfig.setKeepaliveTime(30000);
        hikariConfig.setConnectionTimeout(10000);
        hikariConfig.setMaxLifetime(6000000);

        this.dataSource = new HikariDataSource(hikariConfig);
    }

    private String getJdbcUrl() {
        return switch (databaseType) {
            case MYSQL ->
                "jdbc:mysql://" + config.getSettings().getUrl() + "/" + config.getSettings().getName() + "?serverTimezone=UTC";
            case SQLITE -> "jdbc:sqlite:" + getFilePath();
        };
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }

    public void reload() {
        close();
        initializeDataSource();
    }

    public String getFilePath() {
        return plugin.getDataFolder().getAbsolutePath() + "/database.db";
    }

    public DatabaseType getType() {
        return databaseType;
    }
}

