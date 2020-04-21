package com.uzm.hylex.rankup.spigot.inventory;

import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.uzm.hylex.rankup.Core;
import com.uzm.hylex.rankup.spigot.item.ItemBuilder_1_14_R1;

public class InventoryAccept implements Listener {

	private String[] lore;
	private Player player;
	private Inventory inv;
	private ItemStack core;
	private Method method;

	public InventoryAccept(Player p, ItemStack core, String[] lore) {
		this.player = p;
		this.core = core;
		this.lore = lore;
		setup();
		Bukkit.getServer().getPluginManager().registerEvents(this, Core.getInstance());

	}

	public void setup() {
		inv = Bukkit.createInventory(player, 9 * 4, "§7Confirmação");

		inv.setItem(11, new ItemBuilder_1_14_R1(Material.RED_WOOL).name("§cRecusar")
				.lore("§7Deseja cancelar essa ação?").build());

		inv.setItem(13, new ItemBuilder_1_14_R1(core.clone()).lore(lore).build());

		inv.setItem(15, new ItemBuilder_1_14_R1(Material.GREEN_WOOL).name("§aConfirmar")
				.lore("§7Deseja aceitar fazer essa ação?").build());

		inv.setItem(31, new ItemBuilder_1_14_R1(Material.SEA_LANTERN).amount(30).name("§3Tempo").build());
	}

	public void build(Method m) {
		method = m;
		new BukkitRunnable() {
			float x = 30.0F;

			@Override
			public void run() {
				if (!inv.getViewers().contains(player)) {
					cancel();
					HandlerList.unregisterAll(InventoryAccept.this);

				} else {
					if (x > 0) {

						x -= 0.5F;
						inv.setItem(31, new ItemBuilder_1_14_R1(inv.getItem(31).clone())
								.amount(Math.round(x) == 0 ? 1 : Math.round(x)).build());
						if (x >= 1 && x <= 5 && x == Math.round(x)) {
							if (inv.getViewers().contains(player)) {
								player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
							}
						}

					} else {
						try {
							m.invoke(new String(), player);
						} catch (Exception e) {
							e.printStackTrace();
						}
						cancel();
						HandlerList.unregisterAll(InventoryAccept.this);

					}
				}

			}
		}.runTaskTimer(Core.getInstance(), 1L, 10L);
		player.openInventory(inv);
	}

	@EventHandler
	void click(InventoryClickEvent e) {
		ItemStack item = e.getCurrentItem();
		Player p = (Player) e.getWhoClicked();
		if ((item != null) && (item.hasItemMeta())) {
			if (e.getView().getTitle().startsWith("§7Confirmação")) {
				e.setCancelled(true);
				if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aConfirmar")) {

					try {
						method.invoke(new String(), player);
					} catch (Exception err) {
						err.printStackTrace();
					}
					p.closeInventory();

				}
				if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cRecusar")) {
					p.sendMessage("§c* Você recusou essa confirmação");
					p.closeInventory();

				}

			}

		}
	}

}
