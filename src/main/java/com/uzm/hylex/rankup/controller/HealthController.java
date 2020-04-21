package com.uzm.hylex.rankup.controller;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.uzm.hylex.rankup.Core;
import com.uzm.hylex.rankup.spigot.utils.HylexMethods;

public class HealthController {
	private Player player;

	public HealthController(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	@SuppressWarnings("deprecation")
	public void addHealth(double plus) {

		player.setHealthScaled(true);

		player.setHealthScale(20);
		player.setMaxHealth(player.getMaxHealth() + plus);
		player.setHealth(player.getMaxHealth());
	}

	@SuppressWarnings("deprecation")
	public void setMaxHealth(double max) {
		player.setMaxHealth(max);
	}

	public void setHealthScale(double x) {
		player.setHealthScaled(true);

		player.setHealthScale(x);
	}

	public static void task() {

		new BukkitRunnable() {

			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers()) {
					HylexMethods.sendActionBar(p, "§c" + (int) p.getHealth() + "/" + (int) p.getMaxHealth() + "§c§l ❤");
				}
			}
		}.runTaskTimerAsynchronously(Core.getInstance(), 0, 10);
	}
}
