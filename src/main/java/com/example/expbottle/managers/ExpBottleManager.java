package com.example.expbottle.managers;

import com.example.expbottle.ExpBottlePlugin;
import com.example.expbottle.utils.ExpUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import java.text.DecimalFormat;
import java.util.List;

public class ExpBottleManager {
    
    private final ExpBottlePlugin plugin;
    private final ConfigManager configManager;
    private final NamespacedKey expAmountKey;
    private final DecimalFormat numberFormat = new DecimalFormat("#,###");
    
    public ExpBottleManager(ExpBottlePlugin plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.expAmountKey = new NamespacedKey(plugin, "exp_amount");
    }
    
    private String formatNumber(int number) {
        return numberFormat.format(number);
    }
    
    public ItemStack createExpBottle(int expAmount) {
        if (expAmount <= 0) return null;
        
        ItemStack bottle = new ItemStack(Material.EXPERIENCE_BOTTLE);
        ItemMeta meta = bottle.getItemMeta();
        if (meta == null) return null;
        
        String displayName = configManager.getCachedValue("bottle-display-name", "&aChai Kinh Nghiệm &8[&b{amount} XP&8]")
                .replace("{amount}", formatNumber(expAmount));
        meta.setDisplayName(displayName);
        
        List<String> lore = configManager.getCachedStringList("bottle-lore");
        if (lore != null && !lore.isEmpty()) {
            for (int i = 0; i < lore.size(); i++) {
                lore.set(i, lore.get(i).replace("{amount}", formatNumber(expAmount)));
            }
            meta.setLore(lore);
        }
        
        meta.getPersistentDataContainer().set(expAmountKey, PersistentDataType.INTEGER, expAmount);
        bottle.setItemMeta(meta);
        return bottle;
    }
    
    public boolean consumeExpBottle(Player player, ItemStack item) {
        if (player == null || item == null || item.getType() != Material.EXPERIENCE_BOTTLE) {
            return false;
        }
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        
        if (!meta.getPersistentDataContainer().has(expAmountKey, PersistentDataType.INTEGER)) {
            return false;
        }
        
        Integer expAmount = meta.getPersistentDataContainer().get(expAmountKey, PersistentDataType.INTEGER);
        if (expAmount == null || expAmount <= 0) return false;
        
        return ExpUtils.changeExp(player, expAmount);
    }
    
    public ItemStack extractExp(Player player, int amount) {
        if (player == null || amount <= 0) return null;
        
        int playerExp = ExpUtils.getTotalExp(player);
        if (playerExp < amount) return null;
        
        boolean success = ExpUtils.changeExp(player, -amount);
        if (!success) return null;
        
        ItemStack bottle = createExpBottle(amount);
        if (bottle == null) {
            ExpUtils.changeExp(player, amount);
            return null;
        }
        
        return bottle;
    }
    
    public int getPlayerExp(Player player) {
        if (player == null) return 0;
        return ExpUtils.getTotalExp(player);
    }
    
    public void shutdown() {
        plugin.getLogger().info("ExpBottleManager shutdown completed");
    }
}
