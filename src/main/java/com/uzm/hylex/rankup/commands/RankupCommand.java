package com.uzm.hylex.rankup.commands;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;
import com.uzm.hylex.rankup.Core;
import com.uzm.hylex.rankup.ranks.inventory.InventoryPainel;

public class RankupCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage("§fHey brother, stop do it! You cannot execute commands.");
			return true;
		}

		Player player = (Player) sender;
		if (!player.getPlayer().hasPermission(Core.getLoader().getPermissions().get(label.toLowerCase()))) {
			player.getPlayer().sendMessage("§b[Hylex] §cSem §c§npermissão §cpara executar esse comando.");
			return true;
		}
		if (label.equalsIgnoreCase("rankup") || label.equalsIgnoreCase("evoluir")) {

			switch (args.length) {
			case 0:
				InventoryPainel.inventoryPainel(player);
				break;

			default:
				help(player.getPlayer(), label);
				break;
			}
		}
		return false;
	}

	public void help(Player player, String label) {

		player.sendMessage("");
		player.sendMessage("   §eAjuda do comando §f'" + label + "'");
		player.sendMessage("");
		player.sendMessage("  §e- §f/" + label + " §7Evolua de rank.");

		player.sendMessage("");
	}

	public static ArrayList<String> getInvoke() {
		return Lists.newArrayList("rankup", "evoluir");
	}

}
