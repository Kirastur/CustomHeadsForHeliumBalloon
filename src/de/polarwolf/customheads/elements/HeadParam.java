package de.polarwolf.customheads.elements;

import static de.polarwolf.heliumballoon.tools.helium.HeliumParamType.STRING;

import de.polarwolf.heliumballoon.tools.helium.HeliumParam;
import de.polarwolf.heliumballoon.tools.helium.HeliumParamType;

public enum HeadParam implements HeliumParam {

	CUSTOM(STRING, "custom"),
	X(STRING, "x"),
	Y(STRING, "y"),
	Z(STRING, "z"),
	URL(STRING, "url");

	private final HeliumParamType paramType;
	private final String attributeName;

	private HeadParam(HeliumParamType paramType, String attributeName) {
		this.paramType = paramType;
		this.attributeName = attributeName;
	}

	@Override
	public boolean isType(HeliumParamType testParamType) {
		return testParamType == paramType;
	}

	@Override
	public String getAttributeName() {
		return attributeName;
	}

}