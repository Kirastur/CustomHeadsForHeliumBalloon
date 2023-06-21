package de.polarwolf.customheads.elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import de.polarwolf.heliumballoon.behavior.BehaviorDefinition;
import de.polarwolf.heliumballoon.config.rules.ConfigRule;
import de.polarwolf.heliumballoon.config.templates.ConfigElement;
import de.polarwolf.heliumballoon.elements.Element;
import de.polarwolf.heliumballoon.elements.ElementDefinition;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.tools.helium.HeliumParam;
import de.polarwolf.heliumballoon.tools.helium.HeliumSection;

public class HeadConfig implements ConfigElement {

	private final String name;
	private final String fullName;
	private final ElementDefinition elementDefinition;
	private Vector offset = new Vector(0, 0, 0);
	private String url;
	private String custom = null;

	public HeadConfig(String name, String fullName, ElementDefinition elementDefinition) {
		this.name = name;
		this.fullName = fullName;
		this.elementDefinition = elementDefinition;
	}

	public HeadConfig(ElementDefinition elementDefinition, ConfigurationSection fileSection) throws BalloonException {
		this.name = fileSection.getName();
		this.fullName = fileSection.getCurrentPath();
		this.elementDefinition = elementDefinition;
		loadConfigFromFile(fileSection);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getFullName() {
		return fullName;
	}

	@Override
	public ElementDefinition getElementDefinition() {
		return elementDefinition;
	}

	public String getURL() {
		return url;
	}

	protected void setURL(String url) {
		this.url = url;
	}

	public String getCustom() {
		return custom;
	}

	protected void setCustom(String custom) {
		this.custom = custom;
	}

	@Override
	public Element createElement(Player player, ConfigRule rule, BehaviorDefinition behaviorDefinition) {
		return new HeadElement(player, rule, this, behaviorDefinition);
	}

	@Override
	public double getMinYOffset() {
		return offset.getY();
	}

	@Override
	public double getMaxYOffset() {
		return offset.getY();
	}

	public Vector getOffset() {
		return offset;
	}

	protected void setOffset(Vector offset) {
		this.offset = offset;
	}

	protected List<HeliumParam> getValidParams() {
		return Arrays.asList(HeadParam.values());
	}

	protected void importHeliumSection(HeliumSection heliumSection) throws BalloonException {
		Double x = heliumSection.getDouble(HeadParam.X, getOffset().getX());
		Double y = heliumSection.getDouble(HeadParam.Y, getOffset().getY());
		Double z = heliumSection.getDouble(HeadParam.Z, getOffset().getZ());
		setOffset(new Vector(x, y, z));

		setURL(heliumSection.getString(HeadParam.URL));
		setCustom(heliumSection.getString(HeadParam.CUSTOM));
	}

	protected void loadConfigFromFile(ConfigurationSection fileSection) throws BalloonException {
		HeliumSection heliumSection = new HeliumSection(fileSection, getValidParams());
		importHeliumSection(heliumSection);
	}

	protected List<String> paramListAsDump() {
		String fs = "%s: \"%s\"";
		String fb = "%s: %s";
		List<String> newParamListDump = new ArrayList<>();
		if (offset.getX() != 0)
			newParamListDump.add(String.format(fb, HeadParam.X.getAttributeName(), Double.toString(offset.getX())));
		if (offset.getY() != 0)
			newParamListDump.add(String.format(fb, HeadParam.Y.getAttributeName(), Double.toString(offset.getY())));
		if (offset.getZ() != 0)
			newParamListDump.add(String.format(fb, HeadParam.Z.getAttributeName(), Double.toString(offset.getZ())));
		if ((url != null) && !url.isEmpty())
			newParamListDump.add(String.format(fs, HeadParam.URL.getAttributeName(), url));
		if ((custom != null) && !custom.isEmpty())
			newParamListDump.add(String.format(fs, HeadParam.CUSTOM.getAttributeName(), custom));
		return newParamListDump;
	}

	@Override
	public String toString() {
		return String.format("%s: { %s }", getName(), String.join(", ", paramListAsDump()));
	}

}
