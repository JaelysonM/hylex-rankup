package com.uzm.hylex.rankup;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.uzm.hylex.rankup.controller.HealthController;
import com.uzm.hylex.rankup.ranks.Ranks;
import com.uzm.hylex.rankup.scoreboard.ScoreboardController;

import net.milkbowl.vault.economy.Economy;

/**
 * Hello world!
 *
 */
public class Core extends JavaPlugin {

	private static Core core;
	private static String CORE_PATH;
	public static Economy economy;
	public static PluginLoader loader;

	public void onEnable() {

		long aux = System.currentTimeMillis();

		getServer().getConsoleSender()
				.sendMessage("§b[Hylex Module: Rankup] §7Plugin §fessencialmente §7carregado com sucesso.");
		getServer().getConsoleSender().sendMessage(
				"§eVersão: §f" + getDescription().getVersion() + " e criado por §f" + getDescription().getAuthors());
		/*
		 * Declations
		 */

		core = this;
		CORE_PATH = getFile().getPath();
		loader = new PluginLoader(this);

		/*
		 * Setup
		 */
		HealthController.task();
		VaultHook();
		ScoreboardController.deploy();
		Ranks.load();

		getServer().getConsoleSender()
				.sendMessage("§b[Hylex Module: Rankup] §7Plugin §fdefinitivamente §7carregado com sucesso (§f"
						+ (System.currentTimeMillis() - aux + " milisegundos§7)"));

		/*
		 * Soon third-party
		 * 
		 * Bukkit.getMessenger().registerOutgoingPluginChannel(this, "FML|HS");
		 * 
		 * Bukkit.getMessenger().registerIncomingPluginChannel(this, "FML|HS", new
		 * ModMessageListener(this));
		 * 
		 * Bukkit.getMessenger().registerIncomingPluginChannel(this, "LABYMOD", new
		 * ModMessageListener(this));
		 * 
		 */

	}

	public void onDisable() {
		getServer().getConsoleSender().sendMessage(
				"§b[Hylex Module: Rankup] §7Plugin §bdesligado§7, juntamente todos os eventos e comandos também.");

	}

	public static Core getInstance() {
		return core;
	}

	public static String getPath() {
		return CORE_PATH;
	}

	public static PluginLoader getLoader() {
		return loader;
	}

	private boolean VaultHook() {

		RegisteredServiceProvider<Economy> localRegisteredServiceProvider = getServer().getServicesManager()
				.getRegistration(Economy.class);
		if (localRegisteredServiceProvider != null) {
			economy = (Economy) localRegisteredServiceProvider.getProvider();
		}
		return economy != null;
	}

	public static Economy getVaultHook() {
		return economy;

	}
}
