package de.flo56958.MineTinker.Modifiers.Types;

import de.flo56958.MineTinker.Data.ModifierFailCause;
import de.flo56958.MineTinker.Data.ToolType;
import de.flo56958.MineTinker.Events.ModifierFailEvent;
import de.flo56958.MineTinker.Main;
import de.flo56958.MineTinker.Modifiers.Modifier;
import de.flo56958.MineTinker.Utilities.ConfigurationManager;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.*;

public class Luck extends Modifier {

    private static Luck instance;

    public static Luck instance() {
        synchronized (Luck.class) {
            if (instance == null) instance = new Luck();
        }
        return instance;
    }

    private Luck() {
        super("Luck", "Luck.yml",
                new ArrayList<>(Arrays.asList(ToolType.AXE, ToolType.BOW, ToolType.CROSSBOW, ToolType.HOE, ToolType.PICKAXE, ToolType.SHEARS,
                        ToolType.FISHINGROD, ToolType.SHOVEL, ToolType.SWORD, ToolType.TRIDENT)),
                Main.getPlugin());
    }

    @Override
    public List<Enchantment> getAppliedEnchantments() {
        List<Enchantment> enchantments = new ArrayList<>();
        enchantments.add(Enchantment.LOOT_BONUS_BLOCKS);
        enchantments.add(Enchantment.LOOT_BONUS_MOBS);
        enchantments.add(Enchantment.LUCK);

        return enchantments;
    }

    @Override
    public void reload() {
    	FileConfiguration config = getConfig();
    	config.options().copyDefaults(true);

    	config.addDefault("Allowed", true);
    	config.addDefault("Name", "Luck");
    	config.addDefault("ModifierItemName", "Compressed Lapis-Block");
        config.addDefault("Description", "Get more loot from mobs and blocks!");
        config.addDefault("DescriptionModifierItem", "%WHITE%Modifier-Item for the Luck-Modifier");
        config.addDefault("Color", "%BLUE%");
        config.addDefault("MaxLevel", 3);
        config.addDefault("OverrideLanguagesystem", false);

    	config.addDefault("Recipe.Enabled", true);
    	config.addDefault("Recipe.Top", "LLL");
    	config.addDefault("Recipe.Middle", "LLL");
    	config.addDefault("Recipe.Bottom", "LLL");

        Map<String, String> recipeMaterials = new HashMap<>();
        recipeMaterials.put("L", "LAPIS_BLOCK");

        config.addDefault("Recipe.Materials", recipeMaterials);

    	ConfigurationManager.saveConfig(config);
        ConfigurationManager.loadConfig("Modifiers" + File.separator, getFileName());

        init(Material.LAPIS_BLOCK, true);
    }

    @Override
    public boolean applyMod(Player p, ItemStack tool, boolean isCommand) {
        if (modManager.hasMod(tool, SilkTouch.instance())) {
            pluginManager.callEvent(new ModifierFailEvent(p, tool, this, ModifierFailCause.INCOMPATIBLE_MODIFIERS, isCommand));
            return false;
        }
        if (!Modifier.checkAndAdd(p, tool, this, "luck", isCommand)) return false;

        ItemMeta meta = tool.getItemMeta();

        if (meta != null) {
            if (ToolType.AXE.contains(tool.getType())) {
                meta.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, modManager.getModLevel(tool, this), true);
                meta.addEnchant(Enchantment.LOOT_BONUS_MOBS, modManager.getModLevel(tool, this), true);
            } else if (ToolType.BOW.contains(tool.getType())) {
                meta.addEnchant(Enchantment.LOOT_BONUS_MOBS, modManager.getModLevel(tool, this), true);
            } else if (ToolType.HOE.contains(tool.getType())) {
                meta.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, modManager.getModLevel(tool, this), true);
            } else if (ToolType.PICKAXE.contains(tool.getType())) {
                meta.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, modManager.getModLevel(tool, this), true);
            } else if (ToolType.SHOVEL.contains(tool.getType())) {
                meta.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, modManager.getModLevel(tool, this), true);
            } else if (ToolType.SWORD.contains(tool.getType())) {
                meta.addEnchant(Enchantment.LOOT_BONUS_MOBS, modManager.getModLevel(tool, this), true);
            } else if (ToolType.SHEARS.contains(tool.getType())) {
                meta.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, modManager.getModLevel(tool, this), true);
            } else if (ToolType.FISHINGROD.contains(tool.getType())) {
                meta.addEnchant(Enchantment.LUCK, modManager.getModLevel(tool, this), true);
            }

            if (Main.getPlugin().getConfig().getBoolean("HideEnchants")) {
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            } else {
                meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
            }

            tool.setItemMeta(meta);
        }

        return true;
    }

    @Override
    public void removeMod(ItemStack tool) {
        ItemMeta meta = tool.getItemMeta();

        if (meta != null) {
            meta.removeEnchant(Enchantment.LOOT_BONUS_BLOCKS);
            meta.removeEnchant(Enchantment.LOOT_BONUS_MOBS);
            meta.removeEnchant(Enchantment.LUCK);
            tool.setItemMeta(meta);
        }
    }
}
