package de.flo56958.minetinker.utils.playerconfig;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GeneralPCOptions implements PlayerConfigurationInterface {

	public static final GeneralPCOptions INSTANCE = new GeneralPCOptions();

	public final PlayerConfigurationOption ACTIONBAR_MESSAGES =
			new PlayerConfigurationOption(this, "actionbar-messages", PlayerConfigurationOption.Type.BOOLEAN,
					"Actionbar Messages", true);
	public final PlayerConfigurationOption ACTIONBAR_ON_EXP_GAIN =
			new PlayerConfigurationOption(this, "actionbar-on-exp-gain", PlayerConfigurationOption.Type.BOOLEAN,
					"Actionbar on EXP Gain", false);

	@Override
	public String getPCIKey() {
		return "General";
	}

	@Override
	public String getPCIDisplayName() {
		return "MineTinker";
	}

	@Override
	public List<PlayerConfigurationOption> getPCIOptions() {
		final ArrayList<PlayerConfigurationOption> playerConfigurationOptions = new ArrayList<>(List.of(ACTIONBAR_MESSAGES, ACTIONBAR_ON_EXP_GAIN));
		playerConfigurationOptions.sort(Comparator.comparing(PlayerConfigurationOption::displayName));
		return playerConfigurationOptions;
	}
}
