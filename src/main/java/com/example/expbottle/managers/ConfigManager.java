package com.example.expbottle.managers;

import com.example.expbottle.ExpBottlePlugin;
import org.bukkit.configuration.file.FileConfiguration;
import java.util.List;

public class ConfigManager {
    
    private final ExpBottlePlugin plugin;
    
    public ConfigManager(ExpBottlePlugin plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
    }
    
    public String getCachedValue(String key, String defaultValue) {
        FileConfiguration config = plugin.getConfig();
        return config.getString(key, defaultValue);
    }
    
    public int getCachedValue(String key, int defaultValue) {
        FileConfiguration config = plugin.getConfig();
        return config.getInt(key, defaultValue);
    }
    
    public List<String> getCachedStringList(String key) {
        FileConfiguration config = plugin.getConfig();
        return config.getStringList(key);
    }
    
    public void shutdown() {
        // Cleanup if needed
    }
}
