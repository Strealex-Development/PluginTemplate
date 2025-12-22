package xyz.strealex.pluginname.config;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import lombok.Getter;

@Configuration
@Getter
public final class Settings {
    @Comment("This is a simple test setting which holds a string")
    private String test = "I am a test!";

    @Comment("\nI am a sub configuration")
    private TestSubSettings subSettings = new TestSubSettings();

    @Configuration
    @Getter
    public static class TestSubSettings {
        @Comment("Sub configuration")
        private String testSubSetting = "I am a setting within a sub configuration";
    }

    @Comment("Database Type (Supported: MySQL, SQLite)")
    private String type = "SQLite";

    @Comment("URL to your Database")
    private String url = "localhost";

    @Comment("Name of your Database")
    private String name = "SimpleDuels";

    @Comment("Username for your Database")
    private String user = "root";

    @Comment("Password for your Database")
    private String password = "";

    @Comment("Enable or disable automatic saving of player data.")
    private boolean enableAutoSaveTask = true;

    @Comment("Initial delay (in ticks) before the auto-save task starts. (20 ticks = 1 second)")
    private long autoSaveDelayedTicks = 1200;

    @Comment("Interval (in ticks) between each auto-save execution. (20 ticks = 1 second)")
    private long autoSaveIntervalTicks = 6000;
}
