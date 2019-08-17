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

public class Freezing extends Modifier {

    private static Freezing instance;

    public static Freezing instance() {
        synchronized (Freezing.class) {
            if (instance == null) instance = new Freezing();
        }
        return instance;
    }

    private Freezing() {
        super("Freezing", "Freezing.yml",
                new ArrayList<>(Collections.singletonList(ToolType.BOOTS)),
                Main.getPlugin());
    }

    @Override
    public List<Enchantment> getAppliedEnchantments() {
        List<Enchantment> enchantments = new ArrayList<>();
        enchantments.add(Enchantment.FROST_WALKER);

        return enchantments;
    }

    @Override
    public void reload() {
        FileConfiguration config = getConfig();
        config.options().copyDefaults(true);

        config.addDefault("Allowed", true);
        config.addDefault("Name", "Freezing");
        config.addDefault("ModifierItemName", "Icy Crystal");
        config.addDefault("Description", "It is freezing around you.");
        config.addDefault("DescriptionModifierItem", "%WHITE%Modifier-Item for the Freezing-Modifier");
        config.addDefault("Color", "%AQUA%");
        config.addDefault("MaxLevel", 3);
        config.addDefault("OverrideLanguagesystem", false);

        config.addDefault("Recipe.Enabled", true);
        config.addDefault("Recipe.Top", "BBB");
        config.addDefault("Recipe.Middle", "BDB");
        config.addDefault("Recipe.Bottom", "BBB");

        Map<String, String> recipeMaterials = new HashMap<>();
        recipeMaterials.put("B", "BLUE_ICE");
        recipeMaterials.put("D", "DIAMOND");

        config.addDefault("Recipe.Materials", recipeMaterials);

        ConfigurationManager.saveConfig(config);
        ConfigurationManager.loadConfig("Modifiers" + File.separator, getFileName());

        init(Material.DIAMOND, true);
    }

    @Override
    public boolean applyMod(Player p, ItemStack tool, boolean isCommand) {
        if (modManager.hasMod(tool, Aquaphilic.instance())) {
            pluginManager.callEvent(new ModifierFailEvent(p, tool, this, ModifierFailCause.INCOMPATIBLE_MODIFIERS, isCommand));
            return false;
        }

        if (!Modifier.checkAndAdd(p, tool, this, "freezing", isCommand)) return false;

        ItemMeta meta = tool.getItemMeta();

        if (meta != null) {
            meta.addEnchant(Enchantment.FROST_WALKER, modManager.getModLevel(tool, this), true);

            if (Main.getPlugin().getConfig().getBoolean("HideEnchants")) {
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            } else {
                meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
        }

        tool.setItemMeta(meta);
        return true;
    }

    @Override
    public void removeMod(ItemStack tool) {
        ItemMeta meta = tool.getItemMeta();

        if (meta != null) {
            meta.removeEnchant(Enchantment.FROST_WALKER);
            tool.setItemMeta(meta);
        }
    }
}
