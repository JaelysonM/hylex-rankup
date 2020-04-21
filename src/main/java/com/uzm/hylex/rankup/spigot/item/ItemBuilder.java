package com.uzm.hylex.rankup.spigot.item;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public abstract interface ItemBuilder {

	public abstract ItemBuilder material(Material mat);

	public abstract ItemBuilder amount(int amount);

	public abstract ItemBuilder durability(int durability);

	public abstract ItemBuilder name(String dis);

	public abstract ItemBuilder lore(String... strings);

	public abstract ItemBuilder enchant(Enchantment ench, int level);

	public abstract ItemStack glow();

	public abstract ItemStack build();

	public abstract Enchantment getEnchant(String enchant);
}
