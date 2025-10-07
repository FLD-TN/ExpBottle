package com.example.expbottle;

import com.example.expbottle.commands.ExpCommand;
import com.example.expbottle.listeners.BottleListener;
import com.example.expbottle.managers.ConfigManager;
import com.example.expbottle.managers.ExpBottleManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ExpBottlePlugin extends JavaPlugin {
    
    private ConfigManager configManager;
    private ExpBottleManager expBottleManager;
    
    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        expBottleManager = new ExpBottleManager(this, configManager);
        
        getCommand("expbottle").setExecutor(new ExpCommand(expBottleManager));
        getServer().getPluginManager().registerEvents(new BottleListener(expBottleManager, configManager), this);
        
        getLogger().info("ExpBottle plugin enabled!");
    }
    
    @Override
    public void onDisable() {
        if (expBottleManager != null) {
            expBottleManager.shutdown();
        }
        if (configManager != null) {
            configManager.shutdown();
        }
        getLogger().info("ExpBottle plugin disabled!");
    }
}
