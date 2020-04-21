package com.uzm.hylex.rankup.spigot.item;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemBuilder_1_14_R1 implements ItemBuilder {
	private ItemStack item;
	private ItemMeta meta;
	private List<String> lore;
	private boolean glow = false;

	public ItemBuilder_1_14_R1(ItemStack item) {
		this.item = item;
		this.meta = item.getItemMeta();
		this.lore = ((this.meta != null) && (this.meta.hasLore()) ? this.meta.getLore() : new ArrayList<String>());
	}

	public ItemBuilder_1_14_R1(Material mat) {
		this(new ItemStack(mat));
	}

	public ItemBuilder_1_14_R1(String string) {
		if ((string == null) || (string.isEmpty())) {
			return;
		}
		String[] split = string.split(" : ");

		if (split[0].split(":").length > 1) {
			this.item = new ItemStack(Material.matchMaterial(split[0].split(":")[0].toUpperCase()));
		} else {
			this.item = new ItemStack(Material.matchMaterial(split[0].toUpperCase()));
		}
		this.meta = this.item.getItemMeta();
		this.lore = new ArrayList<>();
		if (split.length > 1) {
			try {
				amount(Integer.parseInt(split[1]));
			} catch (Exception localException1) {
			}
			for (int i = 2; i < split.length; i++) {
				String splitted = split[i];
				if (splitted.startsWith("name=")) {
					name(splitted.split("=")[1].replace("&", "§"));
				}
				if (splitted.startsWith("glow=")) {
					if (Boolean.valueOf(splitted.split("glow=")[1]) == true) {
						glow = true;
					}
				}
				if (splitted.startsWith("enchant=")) {
					for (int j = 0; j < splitted.split("=")[1].split("/").length; j++) {
						if (splitted.split("=")[1].split("/")[j].split(":").length > 1) {
							try {
								enchant(getEnchant(splitted.split("=")[1].split("/")[j].split(":")[0]),
										Integer.parseInt(splitted.split("=")[1].split("/")[j].split(":")[1]));

							} catch (Exception localException2) {
								localException2.printStackTrace();
							}
						}
					}
				}
				List<String> lore = new ArrayList<>();
				if (splitted.startsWith("lore=")) {
					for (int j = 0; j < splitted.split("=")[1].split("/").length; j++) {
						lore.add(splitted.split("=")[1].split("/")[j].replace("&", "§"));
					}
				}
				if (lore.size() >= 1) {
					lore((String[]) lore.toArray(new String[lore.size()]));
				}
			}
		}
	}

	public ItemBuilder_1_14_R1 material(Material mat) {
		if (this.item == null) {
			this.item = new ItemStack(mat);
		}
		this.item.setType(mat);
		return this;
	}

	public ItemBuilder_1_14_R1 amount(int amount) {
		if (this.item != null) {
			this.item.setAmount(amount);
		}
		return this;
	}

	@SuppressWarnings("deprecation")
	public ItemBuilder_1_14_R1 durability(int durability) {
		if (this.item != null) {
			this.item.setDurability((short) durability);
		}
		return this;
	}

	public ItemBuilder_1_14_R1 name(String dis) {
		if (this.item == null) {
			return this;
		}
		this.meta.setDisplayName(dis);
		return this;
	}

	public ItemBuilder_1_14_R1 lore(String... strings) {
		if ((this.item == null) || (this.meta == null)) {
			return this;
		}
		String[] arrayOfString;
		int j = (arrayOfString = strings).length;
		for (int i = 0; i < j; i++) {
			String string = arrayOfString[i];
			this.lore.add(string);
		}
		return this;
	}

	public ItemBuilder_1_14_R1 enchant(Enchantment ench, int level) {
		if (this.item == null) {
			return this;
		}
		this.meta.addEnchant(ench, level, true);
		return this;
	}

	/*
	 * public CraftItemStack glow(){ net.minecraft.server.v1_14_R1.ItemStack
	 * nmsStack = CraftItemStack.asNMSCopy(this.item); NBTTagCompound tag = null; if
	 * (!nmsStack.hasTag()) { tag = new NBTTagCompound(); nmsStack.setTag(tag); } if
	 * (tag == null) tag = nmsStack.getTag(); NBTTagList ench = new NBTTagList();
	 * tag.set("Enchantments", ench); nmsStack.setTag(tag); return
	 * CraftItemStack.asCraftMirror(nmsStack); }
	 */
	public ItemStack glow() {
		if (item == null)
			return null;
		ItemMeta met = item.getItemMeta();
		met.addEnchant(Enchantment.BINDING_CURSE, 1, false);
		met.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(met);
		return item;
	}

	public ItemStack build(boolean glow) {
		this.glow = glow;
		if (!this.lore.isEmpty()) {
			this.meta.setLore(this.lore);
			this.lore.clear();
		}
		if (this.meta != null) {
			this.item.setItemMeta(this.meta);
		}
		return glow ? (ItemStack) glow() : this.item;
	}

	public ItemStack build() {
		this.glow = false;
		if (!this.lore.isEmpty()) {
			this.meta.setLore(this.lore);
			this.lore.clear();
		}
		if (this.meta != null) {
			this.item.setItemMeta(this.meta);
		}
		return glow ? (ItemStack) glow() : this.item;
	}

	@SuppressWarnings("deprecation")
	public Enchantment getEnchant(String enchant) {
		switch (enchant.toLowerCase()) {
		case "protection":
			return Enchantment.PROTECTION_ENVIRONMENTAL;
		case "projectileprotection":
			return Enchantment.PROTECTION_PROJECTILE;
		case "fireprotection":
			return Enchantment.PROTECTION_FIRE;
		case "featherfall":
			return Enchantment.PROTECTION_FALL;
		case "blastprotection":
			return Enchantment.PROTECTION_EXPLOSIONS;
		case "respiration":
			return Enchantment.OXYGEN;
		case "aquaaffinity":
			return Enchantment.WATER_WORKER;
		case "sharpness":
			return Enchantment.DAMAGE_ALL;
		case "smite":
			return Enchantment.DAMAGE_UNDEAD;
		case "baneofarthropods":
			return Enchantment.DAMAGE_ARTHROPODS;
		case "knockback":
			return Enchantment.KNOCKBACK;
		case "fireaspect":
			return Enchantment.FIRE_ASPECT;
		case "depthstrider":
			return Enchantment.DEPTH_STRIDER;
		case "looting":
			return Enchantment.LOOT_BONUS_MOBS;
		case "power":
			return Enchantment.ARROW_DAMAGE;
		case "punch":
			return Enchantment.ARROW_KNOCKBACK;
		case "fire":
			return Enchantment.ARROW_FIRE;
		case "infinity":
			return Enchantment.ARROW_INFINITE;
		case "efficiency":
			return Enchantment.DIG_SPEED;
		case "silktouch":
			return Enchantment.SILK_TOUCH;
		case "unbreaking":
			return Enchantment.DURABILITY;
		case "fortune":
			return Enchantment.LOOT_BONUS_BLOCKS;
		case "luckofthesea":
			return Enchantment.LUCK;
		case "luck":
			return Enchantment.LUCK;
		case "lure":
			return Enchantment.LURE;
		case "thorns":
			return Enchantment.THORNS;

		}
		return Enchantment.getByName(enchant.toUpperCase());
	}

}
