package de.polarwolf.customheads.behavior;

import org.bukkit.Location;

import de.polarwolf.heliumballoon.behavior.BehaviorDefinition;
import de.polarwolf.heliumballoon.behavior.observers.FixedObserver;
import de.polarwolf.heliumballoon.behavior.oscillators.Oscillator;
import de.polarwolf.heliumballoon.config.balloons.ConfigBalloon;
import de.polarwolf.heliumballoon.config.templates.ConfigElement;
import de.polarwolf.heliumballoon.exception.BalloonException;

public class HeadObserverFixed extends FixedObserver implements HeadObserver {

	public HeadObserverFixed(BehaviorDefinition behaviorDefinition, ConfigBalloon configBalloon,
			ConfigElement configElement, Oscillator oscillator, Location fixedLocation) throws BalloonException {
		super(behaviorDefinition, configBalloon, configElement, oscillator, fixedLocation);
	}

}
