package com.example.expbottle.commands;

import com.example.expbottle.managers.ExpBottleManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.text.DecimalFormat;

public class ExpCommand implements CommandExecutor {
    
    private final ExpBottleManager expBottleManager;
    private final DecimalFormat numberFormat = new DecimalFormat("#,###");
    
    public ExpCommand(ExpBottleManager expBottleManager) {
        this.expBottleManager = expBottleManager;
    }
    
    private String formatNumber(int number) {
        return numberFormat.format(number);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Lệnh này chỉ được dùng bởi người chơi!");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (args.length == 0) {
            int totalExp = expBottleManager.getPlayerExp(player);
            player.sendMessage(ChatColor.GREEN + "[!] Tổng số kinh nghiệm hiện có: " + ChatColor.AQUA + formatNumber(totalExp) + " XP");
            return true;
        }
        
        if (args[0].equalsIgnoreCase("withdraw") && args.length == 2) {
            try {
                int amount = Integer.parseInt(args[1]);
                if (amount <= 0) {
                    player.sendMessage(ChatColor.RED + "[!] Số EXP muốn rút phải lớn hơn 0!");
                    return true;
                }
                
                ItemStack bottle = expBottleManager.extractExp(player, amount);
                if (bottle == null) {
                    player.sendMessage(ChatColor.RED + "[!] Bạn không có đủ số exp để rút!");
                    return true;
                }
                
                player.getInventory().addItem(bottle);
                player.sendMessage(ChatColor.GREEN + "[!] Đã tạo chai kinh nghiệm với " + ChatColor.AQUA + formatNumber(amount) + " XP!");
                return true;
                
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.RED + "[!] Giá trị rút không hợp lệ!");
                return true;
            }
        }
        
        player.sendMessage(ChatColor.YELLOW + "Usage: /expbottle withdraw <Số Lượng>]");
        return true;
    }
}
