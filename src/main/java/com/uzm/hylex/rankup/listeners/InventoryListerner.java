package com.uzm.hylex.rankup.listeners;

import java.lang.reflect.Method;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.uzm.hylex.rankup.controller.HylexPlayer;
import com.uzm.hylex.rankup.java.util.Reflections;
import com.uzm.hylex.rankup.spigot.inventory.InventoryAccept;
import com.uzm.hylex.rankup.spigot.item.ItemBuilder_1_14_R1;
import com.uzm.hylex.rankup.spigot.utils.HylexMethods;
import com.uzm.hylex.rankup.spigot.utils.json.JSONMessage;
import com.uzm.hylex.rankup.spigot.utils.json.NMSUtil;

import net.minecraft.server.v1_14_R1.IChatBaseComponent;
import net.minecraft.server.v1_14_R1.IChatBaseComponent.ChatSerializer;

public class InventoryListerner implements Listener {

	@EventHandler
	private void click_painel(InventoryClickEvent e) {

		ItemStack item = e.getCurrentItem();
		Player p = (Player) e.getWhoClicked();
		if ((item != null) && (item.hasItemMeta())) {
			if (e.getView().getTitle().startsWith("§7Painel de ranks")) {
				e.setCancelled(true);

				if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aEvoluir de rank")) {
					try {
						Method handshake = Reflections.getMethod(HylexMethods.class, "externalRankup", Player.class);
						InventoryAccept accept = new InventoryAccept(p,
								new ItemBuilder_1_14_R1(Material.FIREWORK_ROCKET).name("§bDeseja upar de rank?")
										.build(),
								new String[] { "§7Ao clicar em aceitar você irá", "§7evoluir de rank." });
						accept.build(handshake);

					} catch (Exception ex) {
						System.err.println("Probally An error occurred while trying to register some commands.");
						ex.printStackTrace();
					}

				}

				if (e.getCurrentItem().getItemMeta().getDisplayName().startsWith("§dDar prestígio")) {
					if (HylexPlayer.get(p).prestige())
						;
				}

			}

		}

	}

	@EventHandler
	private void click_painel_discord(InventoryClickEvent e) {

		ItemStack item = e.getCurrentItem();
		Player p = (Player) e.getWhoClicked();
		if ((item != null) && (item.hasItemMeta())) {
			if (e.getView().getTitle().startsWith("§7Painel §8Discord")) {
				e.setCancelled(true);

				if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§bRealizar vinculação")) {
					try {
						Method handshake = Reflections.getMethod(HylexMethods.class, "externalDiscordHook",
								Player.class);
						InventoryAccept accept = new InventoryAccept(p,
								new ItemBuilder_1_14_R1(Material.BIRCH_DOOR)
										.name("§bDeseja realizar a vinculação com o discord?").build(),
								new String[] { "§7Ao clicar em aceitar você irá",
										"§7iniciar o processo de vinculação." });
						accept.build(handshake);

					} catch (Exception ex) {
						System.err.println("Probally An error occurred while trying to register some commands.");
						ex.printStackTrace();
					}

				}
				if (e.getCurrentItem().getItemMeta().getDisplayName().startsWith("§aO seu UDToken é: §f")) {
					p.closeInventory();
					p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2F, 2F);
					JSONMessage message = NMSUtil.buildMessage("   §e★ §aO seu token de vinculação é: §f§l§n");
					JSONMessage.ChatExtra accept = new JSONMessage.ChatExtra(
							HylexPlayer.get(p).getDiscord().getUniqueToken()).clickExtra(
									JSONMessage.ClickEventType.SUGGEST_COMMAND,
									HylexPlayer.get(p).getDiscord().getUniqueToken());

					message.addExtra(accept).addExtra(new JSONMessage.ChatExtra("§a clique nele para §fcopiar§a."));

					String msg = message.toString();

					IChatBaseComponent chat = ChatSerializer.a(msg);
					p.sendMessage("");
					((CraftPlayer) p).getHandle().sendMessage(chat);
					p.sendMessage("");
				}

			}

		}

	}

}
