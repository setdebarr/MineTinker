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

public class MultiShot extends Modifier {

    private boolean compatibleWithPiercing;

    private static MultiShot instance;

    public static MultiShot instance() {
        synchronized (MultiShot.class) {
            if (instance == null) instance = new MultiShot();
        }
        return instance;
    }

    private MultiShot() {
        super("Multishot", "Multishot.yml",
                new ArrayList<>(Collections.singletonList(ToolType.CROSSBOW)),
                Main.getPlugin());
    }

    @Override
    public List<Enchantment> getAppliedEnchantments() {
        List<Enchantment> enchantments = new ArrayList<>();
        enchantments.add(Enchantment.MULTISHOT);

        return enchantments;
    }

    @Override
    public void reload() {
        FileConfiguration config = getConfig();
        config.options().copyDefaults(true);

        config.addDefault("Allowed", true);
        config.addDefault("Name", "Multishot");
        config.addDefault("ModifierItemName", "Multi-Arrow");
        config.addDefault("Description", "Shoot more Arrows per shot!");
        config.addDefault("DescriptionModifierItem", "%WHITE%Modifier-Item for the Multishot-Modifier");
        config.addDefault("Color", "%YELLOW%");
        config.addDefault("MaxLevel", 1);

        config.addDefault("CompatibleWithPiercing", false);

        config.addDefault("Recipe.Enabled", true);
        config.addDefault("Recipe.Top", "QQQ");
        config.addDefault("Recipe.Middle", "AAA");
        config.addDefault("Recipe.Bottom", "QQQ");

        Map<String, String> recipeMaterials = new HashMap<>();
        recipeMaterials.put("Q", "QUARTZ_BLOCK");
        recipeMaterials.put("A", "ARROW");

        config.addDefault("Recipe.Materials", recipeMaterials);

        ConfigurationManager.saveConfig(config);
        ConfigurationManager.loadConfig("Modifiers" + File.separator, getFileName());

        init(Material.ARROW, true);

        this.compatibleWithPiercing = config.getBoolean("CompatibleWithPiercing", false);
    }

    @Override
    public boolean applyMod(Player p, ItemStack tool, boolean isCommand) {
        if (!Modifier.checkAndAdd(p, tool, this, "multishot", isCommand)) return false;

        ItemMeta meta = tool.getItemMeta();

        if (meta != null) {
            if (ToolType.CROSSBOW.contains(tool.getType())) {
                if (!this.compatibleWithPiercing) {
                    if (modManager.hasMod(tool, Piercing.instance()) || meta.hasEnchant(Enchantment.PIERCING)) {
                        pluginManager.callEvent(new ModifierFailEvent(p, tool, this, ModifierFailCause.INCOMPATIBLE_MODIFIERS, isCommand));
                        return false;
                    }
                }

                meta.addEnchant(Enchantment.MULTISHOT, modManager.getModLevel(tool, this), true);
            }

            if (Main.getPlugin().getConfig().getBoolean("HideEnchants")) {
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            } else {
                meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
            }

            tool.setItemMeta(meta);
        }

        return false;
    }

    @Override
    public void removeMod(ItemStack tool) {
        ItemMeta meta = tool.getItemMeta();

        if (meta != null) {
            meta.removeEnchant(Enchantment.MULTISHOT);
            tool.setItemMeta(meta);
        }
    }
}
