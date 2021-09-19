package sk.maroskomml.lifeplugin;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {

    public static final String LIFE_CRYSTAL_NAME = "LIFE CRYSTAL";
    public static final String LIFE_CRYSTAL_KEY = "life_crystal";

    public static ItemStack lifeCrystal;

    public static void init(){
        ItemStack item = new ItemStack(Material.END_CRYSTAL);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(LIFE_CRYSTAL_NAME));

        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("Tento krystal navrati strateny zivot."));
        lore.add(Component.text("Vaz si ho."));

        meta.lore(lore);

        meta.addEnchant(Enchantment.LUCK, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        
        item.setItemMeta(meta);

        lifeCrystal = item;

        ShapedRecipe sr = createShapedRecipe(item);
        Bukkit.addRecipe(sr);
    }

    @NotNull
    private static ShapedRecipe createShapedRecipe(ItemStack item) {
        ShapedRecipe sr = new ShapedRecipe(NamespacedKey.minecraft(LIFE_CRYSTAL_KEY), item);
        sr.shape(
                "ACA",
                "CBC",
                "ACA"
        );
        sr.setIngredient('A', Material.AMETHYST_SHARD);
        sr.setIngredient('B', Material.NETHERITE_INGOT);
        sr.setIngredient('C', Material.DIAMOND);
        return sr;
    }

}
