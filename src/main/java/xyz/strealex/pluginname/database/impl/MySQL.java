package xyz.strealex.pluginname.database.impl;

import xyz.strealex.pluginname.database.DatabaseProvider;

public class MySQL extends SQLImpl {

    public MySQL(DatabaseProvider provider) {
        super(provider);
    }

    @Override
    public boolean isExternal() {
        return true;
    }
}
