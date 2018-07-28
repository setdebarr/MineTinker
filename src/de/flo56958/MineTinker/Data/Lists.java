package de.flo56958.MineTinker.Data;

import de.flo56958.MineTinker.Main;

import java.util.List;

@SuppressWarnings("unchecked")
public class Lists {

    public static final List<String> SWORDS = (List<String>) Main.getPlugin().getConfig().getList("AllowedTools.Swords");
    public static final List<String> AXES = (List<String>) Main.getPlugin().getConfig().getList("AllowedTools.Axes");
    public static final List<String> PICKAXES = (List<String>) Main.getPlugin().getConfig().getList("AllowedTools.Pickaxes");
    public static final List<String> SHOVELS = (List<String>) Main.getPlugin().getConfig().getList("AllowedTools.Shovels");
    public static final List<String> HOES = (List<String>) Main.getPlugin().getConfig().getList("AllowedTools.Hoes");
    public static final List<String> BOWS = (List<String>) Main.getPlugin().getConfig().getList("AllowedTools.Bows");

    public static final List<String> WORLDS = (List<String>) Main.getPlugin().getConfig().getList("AllowedWorlds");
    public static final List<String> WORLDS_ELEVATOR = (List<String>) Main.getPlugin().getConfig().getList("Elevator.AllowedWorlds");
    public static final List<String> WORLDS_BUILDERSWANDS = (List<String>) Main.getPlugin().getConfig().getList("Builderswands.AllowedWorlds");

}
