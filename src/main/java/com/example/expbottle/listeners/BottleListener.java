package com.example.expbottle.listeners;

import com.example.expbottle.managers.ConfigManager;
import com.example.expbottle.managers.ExpBottleManager;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class BottleListener implements Listener {
    
    private final ExpBottleManager expBottleManager;
    private final ConfigManager configManager;
    
    public BottleListener(ExpBottleManager expBottleManager, ConfigManager configManager) {
        this.expBottleManager = expBottleManager;
        this.configManager = configManager;
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (item == null) return;
        
        if (expBottleManager.consumeExpBottle(event.getPlayer(), item)) {
            item.setAmount(item.getAmount() - 1);
            event.getPlayer().sendMessage(ChatColor.GREEN + "[!] Đã dùng chai kinh nghiệm!");
            event.setCancelled(true);
        }
    }
}
