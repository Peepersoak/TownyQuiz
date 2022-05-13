package com.peepersoak.townyquiz.utils;

import com.peepersoak.townyquiz.TownyQuiz;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static void sendSyncMessage(Player player,String msg) {
        Bukkit.getScheduler().runTask(TownyQuiz.getInstance(), () -> player.sendMessage(Utils.color(msg)));
    }

    public static Inventory createInventory(String title, int slot) {
        return Bukkit.createInventory(null, slot, color(title));
    }

    public static ItemStack createButton(String title, List<String> lore, Material material, boolean isGlowing, boolean isAnswer) {
        ItemStack item = new ItemStack(material);
        if (isGlowing) {
            item.addUnsafeEnchantment(Enchantment.DURABILITY, 0);
        }
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        if (isAnswer) {
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        }
        meta.setDisplayName(color(title));
        meta.setLore(colorizeList(lore));
        item.setItemMeta(meta);
        return item;
    }

    public static List<String> colorizeList(List<String> list) {
        if (list == null) return null;
        List<String> str = new ArrayList<>();
        for (String sting : list) {
            str.add(color("&e" + sting));
        }
        return str;
    }

    public static void playSound(Player player, String sound) {
        if (sound == null) return;
        Sound s;
        try {
            s = Sound.valueOf(sound);
        } catch (IllegalArgumentException exception) {
            TownyQuiz.getInstance().getLogger().warning(sound + " does not exist!");
            return;
        }
        player.playSound(player.getLocation(), s, 5.0F, 1.0F);
    }
}
