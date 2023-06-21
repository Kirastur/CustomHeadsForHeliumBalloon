package de.polarwolf.customheads.behavior;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import de.polarwolf.heliumballoon.behavior.BehaviorDefinition;
import de.polarwolf.heliumballoon.behavior.BehaviorHelper;
import de.polarwolf.heliumballoon.behavior.observers.Observer;
import de.polarwolf.heliumballoon.behavior.oscillators.Oscillator;
import de.polarwolf.heliumballoon.config.balloons.ConfigBalloon;
import de.polarwolf.heliumballoon.config.rules.ConfigRule;
import de.polarwolf.heliumballoon.config.templates.ConfigElement;
import de.polarwolf.heliumballoon.elements.Element;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.tools.helium.HeliumParam;

public class HeadBehavior implements BehaviorDefinition {

	public static final String BEHAVIOR_NAME = "head";

	protected final BehaviorHelper behaviorHelper;

	public HeadBehavior(BehaviorHelper behaviorHelper) {
		this.behaviorHelper = behaviorHelper;
	}

	@Override
	public String getName() {
		return BEHAVIOR_NAME;
	}

	@Override
	public String getFullName() {
		return BEHAVIOR_NAME;
	}

	@Override
	public List<HeliumParam> getAdditionalRuleParams() {
		return new ArrayList<>();
	}

	@Override
	public Oscillator createOscillator(ConfigRule rule) {
		return new HeadOscillator(rule);
	}

	@Override
	public Observer createObserver(ConfigBalloon configBalloon, ConfigElement configElement, Oscillator oscillator,
			Object target) throws BalloonException {
		if (target instanceof Player player) {
			return new HeadObserverPlayer(this, configBalloon, configElement, oscillator, player);
		}
		if (target instanceof Location location) {
			return new HeadObserverFixed(this, configBalloon, configElement, oscillator, location);
		}
		throw new BalloonException(getName(), "Target is not a player or a location", target.getClass().getName());
	}

	@Override
	public void modifyBlockData(Element element, BlockData blockData) {
		behaviorHelper.modifyBlockDataDefault(element, blockData);
	}

	@Override
	public void modifyElement(Element element) {
		behaviorHelper.modifyElementDefault(element);
	}

}
