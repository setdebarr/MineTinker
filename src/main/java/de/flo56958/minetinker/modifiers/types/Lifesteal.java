package de.flo56958.minetinker.modifiers.types;

import de.flo56958.minetinker.MineTinker;
import de.flo56958.minetinker.data.ToolType;
import de.flo56958.minetinker.events.MTEntityDamageByEntityEvent;
import de.flo56958.minetinker.modifiers.Modifier;
import de.flo56958.minetinker.utils.ChatWriter;
import de.flo56958.minetinker.utils.ConfigurationManager;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;

public class Lifesteal extends Modifier implements Listener {

	private static Lifesteal instance;
	private int percentPerLevel;
	private int percentToTrigger;

	private Lifesteal() {
		super(MineTinker.getPlugin());
		customModelData = 10_018;
	}

	public static Lifesteal instance() {
		synchronized (Lifesteal.class) {
			if (instance == null) {
				instance = new Lifesteal();
			}
		}

		return instance;
	}

	@Override
	public String getKey() {
		return "Lifesteal";
	}

	@Override
	public List<ToolType> getAllowedTools() {
		return Arrays.asList(ToolType.AXE, ToolType.BOW, ToolType.CROSSBOW, ToolType.SWORD, ToolType.TRIDENT);
	}

	@Override
	public void reload() {
		FileConfiguration config = getConfig();
		config.options().copyDefaults(true);

		config.addDefault("Allowed", true);
		config.addDefault("Color", "%DARK_RED%");
		config.addDefault("MaxLevel", 3);
		config.addDefault("SlotCost", 1);
		config.addDefault("PercentToTrigger", 50);
		config.addDefault("PercentOfDamagePerLevel", 10);

		config.addDefault("EnchantCost", 10);
		config.addDefault("Enchantable", false);
		config.addDefault("MinimumToolLevelRequirement", 1);

		config.addDefault("Recipe.Enabled", true);
		config.addDefault("Recipe.Top", "SRS");
		config.addDefault("Recipe.Middle", "RNR");
		config.addDefault("Recipe.Bottom", "SRS");

		Map<String, String> recipeMaterials = new HashMap<>();
		recipeMaterials.put("N", Material.NETHERRACK.name());
		recipeMaterials.put("R", Material.ROTTEN_FLESH.name());
		recipeMaterials.put("S", Material.SOUL_SAND.name());

		config.addDefault("Recipe.Materials", recipeMaterials);

		ConfigurationManager.saveConfig(config);
		ConfigurationManager.loadConfig("Modifiers" + File.separator, getFileName());

		init(Material.NETHERRACK);

		this.percentPerLevel = config.getInt("PercentOfDamagePerLevel", 10);
		this.percentToTrigger = config.getInt("PercentToTrigger", 50);

		this.description = this.description.replace("%amount", String.valueOf(this.percentPerLevel))
				.replace("%chance", String.valueOf(this.percentToTrigger));
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH) //because of Melting
	public void effect(MTEntityDamageByEntityEvent event) {
		if (event.getPlayer().equals(event.getEvent().getEntity())) {
			return; //when event was triggered by the armor
		}

		Player player = event.getPlayer();
		ItemStack tool = event.getTool();

		if (!player.hasPermission("minetinker.modifiers.lifesteal.use")) {
			return;
		}

		if (!modManager.hasMod(tool, this)) {
			return;
		}

		Random rand = new Random();
		int n = rand.nextInt(100);

		if (n > this.percentToTrigger) {
			ChatWriter.logModifier(player, event, this, tool, String.format("Chance(%d/%d)", n, this.percentToTrigger));
			return;
		}

		int level = modManager.getModLevel(tool, this);
		double damage = event.getEvent().getDamage();
		double recovery = damage * ((percentPerLevel * level) / 100.0);
		double health = player.getHealth() + recovery;

		AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);

		if (attribute != null) {
			// for IllegalArgumentExeption if Health is biggen than MaxHealth
			if (health > attribute.getValue()) {
				health = attribute.getValue();
			}

			player.setHealth(health);
		}

		ChatWriter.logModifier(player, event, this, tool, String.format("Chance(%d/%d)", n, this.percentToTrigger),
				String.format("HealthGain(%.2f [%.2f/%.2f = %.4f])", recovery, recovery, damage, recovery/damage));
	}
}
