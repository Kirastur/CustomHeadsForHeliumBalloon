package de.polarwolf.customheads.elements;

import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.Vector;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import de.polarwolf.heliumballoon.behavior.BehaviorDefinition;
import de.polarwolf.heliumballoon.config.rules.ConfigRule;
import de.polarwolf.heliumballoon.elements.SimpleElement;
import de.polarwolf.heliumballoon.exception.BalloonException;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.FlagWatcher;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import me.libraryaddict.disguise.utilities.DisguiseValues;
import me.libraryaddict.disguise.utilities.reflection.FakeBoundingBox;

public class HeadElement extends SimpleElement {

	public static final float DISGUISE_Y_MODIFIER = -1.4f;

	private final HeadConfig config;
	protected FallingBlock fallingBlock = null;
	protected MobDisguise mobDisguise = null;

	private static Method metaSetProfileMethod;

	protected HeadElement(Player player, ConfigRule rule, HeadConfig config, BehaviorDefinition behaviorDefinition) {
		super(player, rule, behaviorDefinition);
		this.config = config;
	}

	// Idea taken from https://github.com/deanveloper/SkullCreator
	private static String urlToBase64(String url) throws BalloonException {
		URI actualUrl;
		try {
			actualUrl = new URI(url);
		} catch (URISyntaxException e) {
			throw new BalloonException(null, "malformed URL", url, e);
		}
		String toEncode = "{\"textures\":{\"SKIN\":{\"url\":\"" + actualUrl.toString() + "\"}}}";
		return Base64.getEncoder().encodeToString(toEncode.getBytes());
	}

	// Idea taken from https://github.com/deanveloper/SkullCreator
	private static GameProfile makeProfile(String b64) {
		// random uuid based on the b64 string
		UUID id = new UUID(b64.substring(b64.length() - 20).hashCode(), b64.substring(b64.length() - 10).hashCode());
		GameProfile profile = new GameProfile(id, "Player");
		profile.getProperties().put("textures", new Property("textures", b64));
		return profile;
	}

	protected BlockData createBaseBlockData() {
		return Bukkit.createBlockData(Material.OAK_WOOD);
	}

	protected void modifyBlockData(BlockData blockData) {
		// Do Nothing
	}

	public BlockData createBlockData() {
		BlockData blockData = createBaseBlockData();
		modifyBlockData(blockData);
		if (getBehaviorDefinition() != null) {
			getBehaviorDefinition().modifyBlockData(this, blockData);
		}
		return blockData;
	}

	public HeadConfig getConfig() {
		return config;
	}

	@Override
	public Vector getOffset() {
		return config.getOffset();
	}

	@Override
	public Entity getEntity() {
		return fallingBlock;
	}

	@Override
	public int getDelay() {
		return 0;
	}

	protected void spawnBaseFallingBlock(Location targetLocation) {
		fallingBlock = targetLocation.getWorld().spawnFallingBlock(targetLocation, createBlockData());
		fallingBlock.setPersistent(false);
		fallingBlock.setHurtEntities(false);
		fallingBlock.setGravity(false);
		fallingBlock.setDropItem(false);
		fallingBlock.setSilent(true);
		fallingBlock.setVelocity(new Vector());
	}

	protected void modifySpawn() throws BalloonException {
		String url = config.getURL();
		if (!url.matches("^(http|https)://textures.minecraft.net/texture/(.*)")) {
			throw new BalloonException(null, "URL not in minecraft textures scope", url);
		}
		String b64 = urlToBase64(url);
		GameProfile gameProfile = makeProfile(b64);

		ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD, 1);
		ItemMeta itemMeta = itemStack.getItemMeta();
		if (itemMeta instanceof SkullMeta)
			try {
				if (metaSetProfileMethod == null) {
					metaSetProfileMethod = itemMeta.getClass().getDeclaredMethod("setProfile", GameProfile.class); // NOSONAR
					metaSetProfileMethod.setAccessible(true); // NOSONAR
				}
				metaSetProfileMethod.invoke(itemMeta, gameProfile);
				itemStack.setItemMeta(itemMeta);
			} catch (Exception e) {
				throw new BalloonException(null, "Error while manipulating the SkullMeta", null, e);
			}

		FakeBoundingBox disguiseBox = new FakeBoundingBox(0, 0, 0);
		DisguiseValues disguiseValues = DisguiseValues.getDisguiseValues(DisguiseType.ARMOR_STAND);
		disguiseValues.setAdultBox(disguiseBox);

		mobDisguise = new MobDisguise(DisguiseType.ARMOR_STAND);
		mobDisguise.setEntity(fallingBlock);
		mobDisguise.setModifyBoundingBox(true);
		FlagWatcher watcher = mobDisguise.getWatcher();
		watcher.setHelmet(itemStack);
		watcher.setNoGravity(true);
		watcher.setInvisible(true);
		watcher.setYModifier(DISGUISE_Y_MODIFIER);
		mobDisguise.startDisguise();
	}

	@Override
	protected void spawn(Location targetLocation) throws BalloonException {
		spawnBaseFallingBlock(targetLocation);
		modifySpawn();
		if (getBehaviorDefinition() != null) {
			getBehaviorDefinition().modifyElement(this);
		}
	}

	@Override
	public void hide() {
		if (mobDisguise != null) {
			mobDisguise.stopDisguise();
			mobDisguise = null;
		}
		if (fallingBlock == null) {
			return;
		}
		fallingBlock.remove();
		fallingBlock = null;
	}

}
