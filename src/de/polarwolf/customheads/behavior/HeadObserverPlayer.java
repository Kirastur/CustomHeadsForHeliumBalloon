package de.polarwolf.customheads.behavior;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import de.polarwolf.heliumballoon.behavior.BehaviorDefinition;
import de.polarwolf.heliumballoon.behavior.observers.PlayerFollowingObserver;
import de.polarwolf.heliumballoon.behavior.oscillators.Oscillator;
import de.polarwolf.heliumballoon.config.balloons.ConfigBalloon;
import de.polarwolf.heliumballoon.config.templates.ConfigElement;
import de.polarwolf.heliumballoon.exception.BalloonException;

public class HeadObserverPlayer extends PlayerFollowingObserver implements HeadObserver {

	public HeadObserverPlayer(BehaviorDefinition behaviorDefinition, ConfigBalloon configBalloon,
			ConfigElement configElement, Oscillator oscillator, Player player) throws BalloonException {
		super(behaviorDefinition, configBalloon, configElement, oscillator, player);
	}

	@Override
	protected Vector getVelocity(Vector currentPosition, Vector nextPosition) {
		return nextPosition.clone().subtract(currentPosition);
	}

}
