package sk.maroskomml.lifeplugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {

    public static ItemStack lifeCrystal;

    public static void init(){
        ItemStack item = new ItemStack(Material.END_CRYSTAL);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("LIFE CRYSTAL");

        List<String> lore = new ArrayList<>();
        lore.add("Tento krystal navrati strateny zivot.");
        lore.add("Vaz si ho.");

        meta.setLore(lore);

        meta.addEnchant(Enchantment.LUCK, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        
        item.setItemMeta(meta);

        lifeCrystal = item;

        ShapedRecipe sr = new ShapedRecipe(NamespacedKey.minecraft("life_crystal"), item);
        sr.shape(
                "ACA",
                "CBC",
                "ACA"
        );
        sr.setIngredient('A', Material.AMETHYST_SHARD);
        sr.setIngredient('B', Material.NETHERITE_INGOT);
        sr.setIngredient('C', Material.DIAMOND);

        Bukkit.addRecipe(sr);
    }

}
