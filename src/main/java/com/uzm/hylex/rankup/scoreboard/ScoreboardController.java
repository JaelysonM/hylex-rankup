package com.uzm.hylex.rankup.scoreboard;

import org.bukkit.Bukkit;

import com.uzm.hylex.rankup.Core;
import com.uzm.hylex.rankup.controller.HylexPlayer;
import com.uzm.hylex.rankup.controller.TagController;
import com.uzm.hylex.rankup.spigot.utils.HylexMethods;

public class ScoreboardController {

	public static void deploy() {

		Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(Core.getInstance(), () -> {
			for (FastBoard board : FastBoard.boards.values()) {
				updateBoard(board);
			}
			for (TagController p : TagController.getDatas()) {
		          p.update();
				}	
		}, 0, 25);
	}

	private static void updateBoard(FastBoard board) {
		double balance = Core.getVaultHook().getBalance(board.getPlayer());
		if (HylexPlayer.get(board.getPlayer()) != null) {
			HylexPlayer player = HylexPlayer.get(board.getPlayer());
			board.updateLines("           §8[Rankup]", "", " §fRank: §7" + player.getRank().getName(),
					"   " + player.scoreboardFormat(balance), "", "  §bEstatísticas:",
					"   §fVítimas: §c" + player.getKills(), "   §fBlocos quebrados: §a" + player.getBreakedBlocks(), "",
					" §FCarteira: §E$§f" + HylexMethods.modifyBalance(balance), "", "         §bhylex.net");
		}

	}
}
