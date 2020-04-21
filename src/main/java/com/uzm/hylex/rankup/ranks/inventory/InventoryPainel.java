package com.uzm.hylex.rankup.ranks.inventory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.uzm.hylex.rankup.Core;
import com.uzm.hylex.rankup.controller.HylexPlayer;
import com.uzm.hylex.rankup.ranks.Ranks;
import com.uzm.hylex.rankup.spigot.inventory.InventoryProgressBar;
import com.uzm.hylex.rankup.spigot.item.ItemBuilder_1_14_R1;
import com.uzm.hylex.rankup.spigot.utils.HylexMethods;

public class InventoryPainel {

	public static void inventoryPainel(Player p) {
		HylexPlayer player = HylexPlayer.get(p);

		Inventory inv = Bukkit.createInventory(player.getPlayer(), 5 * 9, "§7Painel de ranks");
		inv.clear();
		inv.setItem(0,
				new ItemBuilder_1_14_R1(Material.ARMOR_STAND).name("§cClassificações")
						.lore("", "§7Nessa aba você terá acesso as §aclassificações",
								"§7do servidor seja: '§bTop Prestígio§7', §7'§aTop Estatísticas§7'",
								"§7ou §7'§cTop Ranks§7'", "", " §fClique §aaqui §fpara ver!")
						.build());
		int indexof = Ranks.getOrganizedRanks().indexOf(player.getRank());
		double balance = Core.getVaultHook().getBalance(p);
		String progress = player.getProgessInt(balance);
		String progress_bar = player.getProgessBar(balance);
		String scorebaord_format = player.scoreboardFormat(balance);
		if (player.getPrestige() > 0) {
			inv.setItem(18, new ItemBuilder_1_14_R1(Material.NETHER_STAR).amount(player.getPrestige())
					.name("§bVocê tem §f" + player.getPrestige() + " §e✰ §7(prestígio)").build());
		}
		if ((indexof + 1) >= Ranks.getOrganizedRanks().size()) {
			ItemStack stack = new ItemBuilder_1_14_R1(player.getRank().getIcon().clone())
					.name("§eO seu rank atual é " + ChatColor.getLastColors(player.getRank().getPrefix())
							+ player.getRank().getName())
					.lore("", "§eEstatísticas:", "", "    §f* §7Você está no rank de número: §a"
							+ player.getRank().getID() + "/" + Ranks.getStringOrganizedRanks().size(),
							"    " + scorebaord_format)
					.build();
			inv.setItem(4, stack);

			inv.setItem(19,
					new ItemBuilder_1_14_R1(Material.BARRIER).name("§cRank máximo antigido")
							.lore("", "§aParabéns§7, você foi capaz de chegar no",
									"§7último rank do servidor saiba não é pra qualquer",
									"§7um chegar a esse marco, por isso vamos lhe ", "§7recompensar §b§nvocê merece!",
									"", "§8§n(Veja as instruções no Pó de Blaze)", "")
							.build());
			inv.setItem(26,
					new ItemBuilder_1_14_R1(Material.BLAZE_POWDER).name("§dDar prestígio").lore("", "").build(true));
			new InventoryProgressBar(inv, 10, 10, 5, new ItemStack[] {
					new ItemBuilder_1_14_R1(Material.GREEN_STAINED_GLASS_PANE).name("§aN/A")
							.lore("", "§8Você chegou ao último rank.").build(true),
					new ItemBuilder_1_14_R1(Material.RED_STAINED_GLASS_PANE).name("§cNão alcançado")
							.lore("", "§7Você ainda não chegou a esse §cprogresso§7.", "", progress_bar).build() },
					new int[] { 20, }).build();

		} else {

			Ranks next = Ranks.getOrganizedRanks().get((indexof + 1));
			ItemStack stack = new ItemBuilder_1_14_R1(player.getRank().getIcon().clone())
					.name("§eO seu rank atual é " + player.getRank().getPrefix())
					.lore("", "§eEstatísticas:", "",
							"    §f* §7Você está no rank de número: §a" + player.getRank().getID() + "/"
									+ Ranks.getStringOrganizedRanks().size(),
							"    §f* §7O próximo rank é: " + ChatColor.getLastColors(next.getPrefix())
									+ ChatColor.stripColor(next.getName()),
							"    §f* §7O seu progresso para o próximo rank: " + scorebaord_format)
					.build();
			inv.setItem(4, stack);

			inv.setItem(19,
					new ItemBuilder_1_14_R1(next.getIcon_Mine().clone())
							.name("§7Progresso para o rank: " + next.getPrefix())
							.lore("", " §7Custo para evoluir: §c" + HylexMethods.modifyBalance(next.getCoust()),
									" §7O seu saldo: §e" + HylexMethods.modifyBalance(balance), "",
									"§a[§f!§a] Você está a §f§n" + progress + "§a do próximo rank.")
							.build(true));
			new InventoryProgressBar(inv, balance, player.getNext().getCoust(), 5, new ItemStack[] {
					new ItemBuilder_1_14_R1(Material.GREEN_STAINED_GLASS_PANE).name("§aAlcançado")
							.lore("", "§7Você chegou à esse §bprogresso§7.", "", progress_bar).build(true),
					new ItemBuilder_1_14_R1(Material.RED_STAINED_GLASS_PANE).name("§cNão alcançado")
							.lore("", "§7Você ainda não chegou a esse §cprogresso§7.", "", progress_bar).build() },
					new int[] { 20, }).build();
			if (progress == "100") {
				inv.setItem(26, new ItemBuilder_1_14_R1(Material.FIREWORK_ROCKET).name("§aEvoluir de rank")
						.lore("", "§7Gostaria de evoluir de rank?", "§7Ao fazer isso você está ciente de",
								"§7que você perderá §c"
										+ HylexMethods.modifyBalance(next.getCoust()) + " §7ao fazer essa",
								"§7ação.", "",
								"§7Fazendo isso você irá para o rank: " + ChatColor.getLastColors(next.getPrefix())
										+ ChatColor.stripColor(next.getPrefix()),
								"", "§aClique para evoluir! §f⇈")
						.build(true));
			}

		}

		player.getPlayer().openInventory(inv);
	}

	/*
	 * public ProductsInventory(Player viewer, Inventory base) { super(viewer,
	 * base); ocupe(new ItemStack(Material.AIR), new int[]{});
	 * 
	 * ArrayList<ItemStack> i = new ArrayList<ItemStack>();
	 * 
	 * ItemBuilder choose = null;
	 * 
	 * fill(new ItemStack[], pageitems, extra, slotpage, end) fill(i.toArray(new
	 * ItemStack[i.size()]) ,new ItemStack[] {Methods.buildHead(
	 * "bb0f6e8af46ac6faf88914191ab66f261d6726a7999c637cf2e4159fe1fc477",
	 * "§9Página anterior", new String[]{}, 1) ,Methods.buildHead(
	 * "f2f3a2dfce0c3dab7ee10db385e5229f1a39534a8ba2646178e37c4fa93b",
	 * "§9Próxima página", new String[]{}, 1)},new Object[] {Methods.buildHead(
	 * "16c60da414bf037159c8be8d09a8ecb919bf89a1a21501b5b2ea75963918b7b",
	 * "§cErro 404: Not Found", new
	 * String[]{"§7Não foi encontrado nenhum resultado"}, 1),22}, new
	 * Object[][]{{choose.build(),45}} , new int[]{52,53},35); open(getPlayer(), 1);
	 * } public void click(ItemStack item) { if (item.getType() ==
	 * Material.SKULL_ITEM &&
	 * item.getItemMeta().getDisplayName().startsWith("§9Página anterior")) {
	 * open(getPlayer(), getCurrent() - 1); } if (item.getType() ==
	 * Material.SKULL_ITEM &&
	 * item.getItemMeta().getDisplayName().startsWith("§9Próxima página")) {
	 * open(getPlayer(), getCurrent() + 1); } }
	 * 
	 * @EventHandler private void onClick(InventoryClickEvent e) { if
	 * (exists(e.getInventory())) { e.setCancelled(true); ItemStack item =
	 * e.getCurrentItem();
	 * 
	 * Player p = (Player)e.getWhoClicked(); p.updateInventory(); if ((item != null)
	 * && (!item.hasItemMeta()) && (p == getPlayer())) { click(item); } if ((item !=
	 * null) && (item.hasItemMeta()) && (p == getPlayer())) { click(item); }
	 * 
	 * } }
	 */
}
