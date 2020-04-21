package com.uzm.hylex.rankup.spigot.inventory;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;
import com.uzm.hylex.rankup.Core;
import com.uzm.hylex.rankup.spigot.item.ItemBuilder_1_14_R1;

public class InventoryPaged implements Listener {

	private Player viewer;
	private Inventory base;
	private String title;
	private int[] ocuped;
	private ItemStack ocuped_item = new ItemStack(Material.AIR);
	private List<Inventory> pages = Lists.newArrayList();
	private int now;
	private int avaliable;

	public InventoryPaged(Player viewer, Inventory base, String title) {
		this.viewer = viewer;
		this.base = base;
		this.title = title.replace("&", "§");
		Bukkit.getServer().getPluginManager().registerEvents(this, Core.getInstance());

	}

	public InventoryPaged ocupe(ItemStack base, int[] slots) {
		this.ocuped_item = base;
		this.ocuped = slots;
		this.avaliable = this.base.getSize() - slots.length;
		return this;
	}

	public InventoryPaged fill(ItemStack[] items, ItemStack[] pageitems, Object[][] extra, int[] slotpage, int end) {
		int total = getTotalPages(items);
		int inventorysize = (int) (Math.ceil(base.getSize() / 9.0D) * 9.0D);
		if (items.length == 0) {
			ItemStack[] i = new ItemStack[inventorysize];

			for (int ocupe : ocuped) {

				i[ocupe] = ocuped_item;
			}
			for (Object[] o : extra) {
				i[(Integer) o[1]] = (ItemStack) o[0];
			}
			Inventory inv = Bukkit.createInventory(null, i.length,
					title.replace("{c}", (pages.size() + 1) + "").replace("{m}", total + ""));
			inv.setContents(i);
			inv.setItem(22, new ItemBuilder_1_14_R1(
					"STAINED_GLASS_PANE:14 : 1 : name=&7Erro 404: §cNada encontrado : lore=/&7Não foi encontrado nada nessa categoria.")
							.build());
			pages.add(inv);

		} else {
			ItemStack[] i = new ItemStack[inventorysize];
			int slot = 0;
			int get = 0;

			for (int ocupe : ocuped) {
				i[ocupe] = ocuped_item;

			}
			for (Object[] o : extra) {
				i[(Integer) o[1]] = (ItemStack) o[0];
			}
			for (int in = 0; in < items.length; in++) {
				if (i == null) {
					i = new ItemStack[inventorysize];

					for (int ocupe : ocuped) {
						i[ocupe] = ocuped_item;

					}
					for (Object[] o : extra) {
						i[(Integer) o[1]] = (ItemStack) o[0];
					}
				}
				if (i[slot] == null) {
					i[slot] = items[in];
					get++;
				} else {
					in--;
				}
				slot++;

				if (slot == end || get == items.length) {
					Inventory inv = Bukkit.createInventory(null, i.length,
							title.replace("{c}", (this.pages.size() + 1) + "").replace("{m}", total + ""));
					inv.setContents(i);

					if (pages.size() > 0) {
						inv.setItem(slotpage[0], pageitems[0]);
						pages.get((pages.size() - 1)).setItem(slotpage[1], pageitems[1]);
					}
					this.pages.add(inv);
					i = null;
					slot = 0;
				}
				if (get == items.length) {
					break;
				}

			}
		}
		now = 1;
		return this;
	}

	public InventoryPaged fill(ItemStack[] items, ItemStack[] pageitems, Object[] empty, Object[][] extra,
			int[] slotpage, int end) {
		int total = getTotalPages(items);
		int inventorysize = (int) (Math.ceil(base.getSize() / 9.0D) * 9.0D);
		if (items.length == 0) {
			ItemStack[] i = new ItemStack[inventorysize];

			for (int ocupe : ocuped) {
				i[ocupe] = ocuped_item;
			}
			for (Object[] o : extra) {
				i[(Integer) o[1]] = (ItemStack) o[0];
			}

			Inventory inv = Bukkit.createInventory(null, i.length,
					title.replace("{c}", (pages.size() + 1) + "").replace("{m}", total + ""));
			inv.setContents(i);
			inv.setItem((int) empty[1], (ItemStack) empty[0]);

			pages.add(inv);

		} else {
			ItemStack[] i = new ItemStack[inventorysize];
			int slot = 0;
			int get = 0;

			for (int ocupe : ocuped) {
				i[ocupe] = ocuped_item;

			}
			for (Object[] o : extra) {
				i[(Integer) o[1]] = (ItemStack) o[0];
			}
			for (int in = 0; in < items.length; in++) {
				if (i == null) {
					i = new ItemStack[inventorysize];

					for (int ocupe : ocuped) {
						i[ocupe] = ocuped_item;

					}
					for (Object[] o : extra) {
						i[(Integer) o[1]] = (ItemStack) o[0];
					}
				}
				if (i[slot] == null) {
					i[slot] = items[in];
					get++;
				} else {
					in--;
				}
				slot++;

				if (slot == end || get == items.length) {
					Inventory inv = Bukkit.createInventory(null, i.length,
							title.replace("{c}", (this.pages.size() + 1) + "").replace("{m}", total + ""));
					inv.setContents(i);

					if (pages.size() > 0) {
						inv.setItem(slotpage[0], pageitems[0]);
						pages.get((pages.size() - 1)).setItem(slotpage[1], pageitems[1]);
					}
					this.pages.add(inv);
					i = null;
					slot = 0;
				}
				if (get == items.length) {
					break;
				}

			}
		}
		now = 1;
		return this;
	}

	public int getTotalPages(ItemStack[] items) {
		if (items.length < avaliable) {
			return 1;
		} else {
			return items.length % avaliable == 0
					? (Math.round(items.length / avaliable) == 0 ? 1 : Math.round(items.length / avaliable))
					: (Math.round(items.length / avaliable) + 1);
		}
	}

	public InventoryPaged open(Player player, int page) {
		if (page > 0 && page <= this.pages.size()) {
			if (player != null) {
				now = page;
				player.openInventory(this.pages.get(page - 1));
			}
		}
		return this;

	}

	public boolean exists(Inventory inv) {
		return ((Inventory) this.pages.get(this.now - 1)).equals(inv);
	}

	public InventoryPaged destroy() {
		pages.clear();
		return this;
	}

	public int getCurrent() {
		return now;
	}

	public Player getPlayer() {
		return viewer;
	}
}
