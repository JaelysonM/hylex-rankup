package com.uzm.hylex.rankup.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.help.HelpTopic;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.JSONObject;

import com.uzm.hylex.rankup.controller.HylexPlayer;
import com.uzm.hylex.rankup.scoreboard.FastBoard;
import com.uzm.hylex.services.lan.WebSocket;

public class PlayerListener implements Listener {
	@EventHandler
	public void onSetupPlayer(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		p.setHealthScaled(true);
		p.setHealthScale(20);

		FastBoard board = new FastBoard(p);

		board.updateTitle("§f§l* §b§l§nHYLEX§r §f§l*");

	}

	@EventHandler
	public void onRegister(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		HylexPlayer player = HylexPlayer.create(p.getPlayer());

		player.requestLoad();

	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();

		FastBoard.destroy(p);
		HylexPlayer hylex = HylexPlayer.get(p);
		if (!hylex.isAuthenciated() && hylex.isRequested()) {
			try {
				JSONObject json = new JSONObject();
				json.put("account_id", hylex.getDiscord().getAccountId());

				WebSocket.get("local_socket").getSocket().emit("loginstaff-purge", json);

			} catch (Exception err) {
				err.printStackTrace();
			}
		}

		HylexPlayer.remove(e.getPlayer());

	}

	@EventHandler
	private void respawar(PlayerRespawnEvent e) {
		Player p = e.getPlayer();
		p.teleport(p.getWorld().getSpawnLocation());

	}

	@EventHandler
	private void mark_enchant(EnchantItemEvent e) {

		Player jogador = (Player) e.getEnchanter();
		final ItemStack ItemResult = e.getItem();
		ItemMeta ItemResultMeta = ItemResult.getItemMeta();
		final ArrayList<String> ItemResultDescricao = new ArrayList<>();
		ItemResultDescricao.add("");
		ItemResultDescricao.add("§a* Este item foi encantado: " + jogador.getName());

		ItemResultMeta.setLore(ItemResultDescricao);
		ItemResult.setItemMeta(ItemResultMeta);

	}

	Map<String, Long> cooldowncomando = new HashMap<String, Long>();
	public static String[] todisable = { "me", "whisper", "msg", "w" };

	@EventHandler
	private void comandoerrado(org.bukkit.event.player.PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();

		Long cooldown = Long.valueOf(System.currentTimeMillis() / 1000L);
		Long tempoDeEspera = (Long) cooldowncomando.get(p.getName());
		if (cooldowncomando.containsKey(p.getName()) && (!e.getMessage().toLowerCase().startsWith("/g"))) {
			if (cooldown.longValue() - tempoDeEspera.longValue() < 3L && (!p.hasPermission("hylex.commanddelay"))) {
				p.sendMessage("§cVocê está digitando comandos §f§nmuito rápido§c, aguarde um pouco.");
				e.setCancelled(true);
				return;
			}
			cooldowncomando.remove(p.getName());
		}
		cooldowncomando.put(p.getName(), cooldown);
		if (!e.isCancelled()) {
			String cmd = e.getMessage().split(" ")[0];

			HelpTopic help = Bukkit.getServer().getHelpMap().getHelpTopic(cmd);

			if (help == null) {

				e.setCancelled(true);
				e.getPlayer().sendMessage("§f[!] §cO comando §n'" + e.getMessage().split(" ")[0].replace("/", "")
						+ "'§c não está acessível ou não existe.");
			}
		}
		String command = e.getMessage().split("/")[1].replace("minecraft:", "").split(" ")[0];
		if (command.startsWith("bukkit:") && !e.getPlayer().hasPermission("*")) {
			e.setCancelled(true);
			e.getPlayer().sendMessage("§c[AVISO] §c§nO acesso a esse comando foi negado.");
		}

		List<String> lists = Arrays.asList(todisable);
		if (lists.contains(command)) {
			e.setCancelled(true);
			e.getPlayer().sendMessage("§c[AVISO] §c§nO acesso a esse comando foi negado.");
		}
	}
}