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
}
