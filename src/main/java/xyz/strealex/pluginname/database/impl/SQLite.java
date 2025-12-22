package xyz.strealex.pluginname.database.impl;

import xyz.strealex.pluginname.database.DatabaseProvider;

public class SQLite extends SQLImpl {

    public SQLite(DatabaseProvider provider) {
        super(provider);
    }

    @Override
    public boolean isExternal() {
        return false;
    }
}
