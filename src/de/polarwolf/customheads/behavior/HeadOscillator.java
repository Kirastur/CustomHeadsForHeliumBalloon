package de.polarwolf.customheads.behavior;

import de.polarwolf.heliumballoon.behavior.oscillators.DefaultOscillator;
import de.polarwolf.heliumballoon.config.rules.ConfigRule;

public class HeadOscillator extends DefaultOscillator {

	public HeadOscillator(ConfigRule rule) {
		super(rule);
	}

	@Override
	public void setDeflectionState(boolean newDeflectionState) {
		// Do nothing, Deflection is not allowed here
	}

	@Override
	public void setSpinState(boolean newSpinState) {
		// Do nothing, Spin is not allowed here
	}

}
