package com.example.expbottle.utils;

import org.bukkit.entity.Player;

public class ExpUtils {
    
    public static int getTotalExp(Player player) {
        return Math.round(player.getExp() * getExpToNext(player.getLevel())) + getTotalExpFromLevel(player.getLevel());
    }
    
    public static boolean changeExp(Player player, int amount) {
        try {
            int currentExp = getTotalExp(player);
            int newExp = currentExp + amount;
            
            if (newExp < 0) newExp = 0;
            
            setTotalExp(player, newExp);
            return true;
            
        } catch (Exception e) {
            return false;
        }
    }
    
    public static void setTotalExp(Player player, int exp) {
        player.setExp(0);
        player.setLevel(0);
        
        if (exp <= 0) return;
        
        while (exp > 0) {
            int expToNext = getExpToNext(player.getLevel());
            if (exp >= expToNext) {
                exp -= expToNext;
                player.setLevel(player.getLevel() + 1);
            } else {
                player.setExp((float) exp / expToNext);
                break;
            }
        }
    }
    
    private static int getExpToNext(int level) {
        if (level <= 15) {
            return 2 * level + 7;
        } else if (level <= 30) {
            return 5 * level - 38;
        } else {
            return 9 * level - 158;
        }
    }
    
    private static int getTotalExpFromLevel(int level) {
        if (level <= 16) {
            return level * level + 6 * level;
        } else if (level <= 31) {
            return (int) (2.5 * level * level - 40.5 * level + 360);
        } else {
            return (int) (4.5 * level * level - 162.5 * level + 2220);
        }
    }
}
