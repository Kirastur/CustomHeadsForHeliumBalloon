package de.polarwolf.customheads;

import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import de.polarwolf.customheads.behavior.HeadBehavior;
import de.polarwolf.customheads.behavior.HeadObserver;
import de.polarwolf.customheads.elements.HeadDefinition;
import de.polarwolf.heliumballoon.api.HeliumBalloonAPI;
import de.polarwolf.heliumballoon.api.HeliumBalloonProvider;
import de.polarwolf.heliumballoon.behavior.observers.Observer;

public class Main extends JavaPlugin {

	public static final int PLUGINID_CUSTOMHEADS = 18787;

	@Override
	public void onEnable() {
		saveDefaultConfig();

		// Enable bStats Metrics
		new Metrics(this, PLUGINID_CUSTOMHEADS);

		HeliumBalloonAPI api = HeliumBalloonProvider.getAPI();
		api.addReloadRegistration(this, null, null);

		HeadDefinition headElementDefinition = new HeadDefinition();
		api.addElementDefinition(headElementDefinition);

		HeadBehavior headBehavior = new HeadBehavior(api.getBehaviorHelper());
		api.addBehaviorDefinition(headBehavior);

		api.addCompatibility(headElementDefinition, headBehavior, api.findBalloonDefinition("pets"));
		api.addCompatibility(headElementDefinition, headBehavior, api.findBalloonDefinition("walls"));

		this.getLogger().info("Custom Heads can now be used as HeliumBalloon pets.");
	}

	@Override
	public void onDisable() {
		int count = 0;
		HeliumBalloonAPI api = HeliumBalloonProvider.getAPI();
		api.removeReloadRegistrations(this);
		for (Observer myObserver : api.getAllObservers()) {
			if (myObserver instanceof HeadObserver) {
				myObserver.cancel();
				count = count + 1;
			}
		}
		if (count > 0) {
			String s = String.format("%d custom heads removed", count);
			this.getLogger().info(s);
		}
	}

}
