package de.flo56958.MineTinker.Modifiers.Types;

import de.flo56958.MineTinker.Data.ToolType;
import de.flo56958.MineTinker.Main;
import de.flo56958.MineTinker.Modifiers.Enchantable;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LightWeight extends Modifier implements Enchantable {

    private static LightWeight instance;

    public static LightWeight instance() {
        synchronized (LightWeight.class) {
            if (instance == null) instance = new LightWeight();
        }
        return instance;
    }

    private LightWeight() {
        super("Light-Weight", "Light-Weight.yml",
                new ArrayList<>(Collections.singletonList(ToolType.BOOTS)),
                Main.getPlugin());
    }

    @Override
    public List<Enchantment> getAppliedEnchantments() {
        List<Enchantment> enchantments = new ArrayList<>();
        enchantments.add(Enchantment.PROTECTION_FALL);

        return enchantments;
    }

    @Override
    public void reload() {
    	FileConfiguration config = getConfig();
    	config.options().copyDefaults(true);
    	
    	config.addDefault("Allowed", true);
    	config.addDefault("Name", "Light-Weight");
    	config.addDefault("ModifierItemName", "Enhanced Feather");
        config.addDefault("Description", "You fall like a feather - sort of...");
        config.addDefault("DescriptionModifierItem", "%WHITE%Modifier-Item for the Light-Weight-Modifier");
        config.addDefault("Color", "%GRAY%");
        config.addDefault("MaxLevel", 3);
    	config.addDefault("EnchantCost", 10);
    	config.addDefault("Recipe.Enabled", false);
    	
    	ConfigurationManager.saveConfig(config);
        ConfigurationManager.loadConfig("Modifiers" + File.separator, getFileName());
    	
        init(Material.FEATHER, true);
    }

    @Override
    public boolean applyMod(Player p, ItemStack tool, boolean isCommand) {
        if (!Modifier.checkAndAdd(p, tool, this, "lightweight", isCommand)) return false;

        ItemMeta meta = tool.getItemMeta();

        if (meta != null) {
            meta.addEnchant(Enchantment.PROTECTION_FALL, modManager.getModLevel(tool, this), true);
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
            meta.removeEnchant(Enchantment.PROTECTION_FALL);
            tool.setItemMeta(meta);
        }
    }

    @Override
    public void enchantItem(Player p, ItemStack item) {
        if (!p.hasPermission("minetinker.modifiers.lightweight.craft")) return;
        _createModifierItem(getConfig(), p, this);
    }
}
