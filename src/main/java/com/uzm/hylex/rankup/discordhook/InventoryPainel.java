package com.uzm.hylex.rankup.discordhook;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.uzm.hylex.rankup.controller.HylexPlayer;
import com.uzm.hylex.rankup.spigot.item.ItemBuilder_1_14_R1;

public class InventoryPainel {

	public static void inventoryPainel(Player p) {
		HylexPlayer player = HylexPlayer.get(p);

		Inventory inv = Bukkit.createInventory(player.getPlayer(), 5 * 9, "§7Painel §8Discord");
		inv.clear();

		inv.setItem(4, new ItemBuilder_1_14_R1(Material.NAME_TAG).name("§aSua conta").lore("", "§eInformações:", "",
				"§7Situação do hook com o Discord: " + player.getDiscord().getAccountSituation().getDisplay(),
				"§7Seu código (UDToken): §a" + (player.getDiscord().getUniqueToken() == null ? "§cAinda não foi gerado"
						: player.getDiscord().getUniqueToken()),
				"§7Id da conta Discord vinculada: §b" + (player.getDiscord().getAccountId() == null ? "§cNenhuma ainda"
						: player.getDiscord().getAccountId()),
				"").build());
		if (player.getDiscord().getUniqueToken() == null) {
			inv.setItem(25,
					new ItemBuilder_1_14_R1(Material.BARRIER).name("§cVocê ainda não tem um UDToken gerado").build());
		} else {
			inv.setItem(25, new ItemBuilder_1_14_R1(Material.TRIPWIRE_HOOK)
					.name("§aO seu UDToken é: §f" + player.getDiscord().getUniqueToken()).build(true));
		}
		switch (player.getDiscord().getAccountSituation()) {
		case UNLINKED:
			inv.setItem(21, new ItemBuilder_1_14_R1(Material.GREEN_STAINED_GLASS_PANE).name("§7Conta desviculada §a✔")
					.lore("").build());
			inv.setItem(22, new ItemBuilder_1_14_R1(Material.RED_STAINED_GLASS_PANE).name("§7Vinculação pedente §c✖")
					.lore("").build());

			inv.setItem(23, new ItemBuilder_1_14_R1(Material.RED_STAINED_GLASS_PANE).name("§7Conta vinculada §c✖")
					.lore("").build());
			inv.setItem(44,
					new ItemBuilder_1_14_R1(Material.BIRCH_DOOR).name("§bRealizar vinculação")
							.lore("", "§7Clicando aqui você irá gerar um UDToken",
									"§7, o qual você deve digitar no nosso servidor", "§7do Discord.", "",
									"§eClique §faqui §einicie agora a vinculação.")
							.build());
			break;
		case PENDING:
			inv.setItem(21, new ItemBuilder_1_14_R1(Material.GREEN_STAINED_GLASS_PANE).name("§7Conta desviculada §a✔")
					.lore("").build());
			inv.setItem(22, new ItemBuilder_1_14_R1(Material.GREEN_STAINED_GLASS_PANE).name("§7Vinculação pedente §a✔")
					.lore("").build());

			inv.setItem(23, new ItemBuilder_1_14_R1(Material.RED_STAINED_GLASS_PANE).name("§7Conta vinculada §c✖")
					.lore("").build());
			break;
		case LINKED:
			inv.setItem(21, new ItemBuilder_1_14_R1(Material.GREEN_STAINED_GLASS_PANE).name("§7Conta desviculada §a✔")
					.lore("").build(true));
			inv.setItem(22, new ItemBuilder_1_14_R1(Material.GREEN_STAINED_GLASS_PANE).name("§7Vinculação pedente §a✔")
					.lore("").build(true));

			inv.setItem(23, new ItemBuilder_1_14_R1(Material.GREEN_STAINED_GLASS_PANE).name("§7Conta vinculada §a✔")
					.lore("").build(true));
			break;

		default:
			break;
		}

		player.getPlayer().openInventory(inv);
	}

}
