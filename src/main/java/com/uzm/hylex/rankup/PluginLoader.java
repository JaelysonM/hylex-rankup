package com.uzm.hylex.rankup;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.json.JSONObject;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.uzm.hylex.rankup.controller.HylexPlayer;
import com.uzm.hylex.rankup.discordhook.DiscordAccount.Situation;
import com.uzm.hylex.rankup.java.util.ConfigurationBuilder;
import com.uzm.hylex.rankup.java.util.Reflections;
import com.uzm.hylex.services.lan.WebSocket;

import io.socket.emitter.Emitter;

public class PluginLoader {

	private PluginManager pm = Bukkit.getServer().getPluginManager();
	private Core core;
	private String spigot_version;
	private int lobbyNumber;
	public HashMap<String, String> permissions = Maps.newHashMap();

	public PluginLoader(Core core) {

		this.core = core;
		spigot_version = Bukkit.getServer().getClass().getPackage().getName()
				.substring(Bukkit.getServer().getClass().getPackage().getName().lastIndexOf('.') + 1);

		listeners();
		commands();
		command_permissions();
		config();
		websocket_events();


	}

	public void listeners() {

		ArrayList<String> registred = Lists.newArrayList();

		List<Class<?>> classes = Reflections.getClasses("com.uzm.hylex.rankup.listeners");

		try {

			for (Class<?> c : classes) {
				pm.registerEvents((Listener) c.newInstance(), getCore());
				registred.add("");
			}

		} catch (Exception e) {
			System.err.println("Probally An error occurred while trying to register some listeners  ");
			e.printStackTrace();
		}

		Bukkit.getConsoleSender().sendMessage("§b[Hylex - Rankup] §7We're registered §f(" + registred.size() + "/"
				+ classes.size() + ") §7listeners.");

	}

	@SuppressWarnings("unchecked")
	public void commands() {

		ArrayList<String> registred = Lists.newArrayList();

		List<Class<?>> classes = Reflections.getClasses("com.uzm.hylex.rankup.commands");

		try {

			for (Class<?> c : classes) {
				Method handshake = Reflections.getMethod(c, "getInvoke", new Class[0]);
				ArrayList<String> list = (ArrayList<String>) handshake.invoke(new ArrayList<String>());

				for (String r : list) {
					getCore().getCommand(r).setExecutor((CommandExecutor) c.newInstance());
				}

				registred.add("");
			}

		} catch (Exception e) {
			System.err.println("Probally An error occurred while trying to register some commands.");
			e.printStackTrace();
		}

		Bukkit.getConsoleSender().sendMessage("§b[Hylex - Rankup] §7We're registered §f(" + registred.size() + "/"
				+ classes.size() + ") §7commands.");

	}

	public void command_permissions() {

		permissions.put("rankup", "hylex.rankupstaff");

		permissions.put("evoluir", "hylex.rankupstaff");

		permissions.put("vincular", "hylex.vincular");

		permissions.put("loginstaff", "hylex.loginstaff");

	}

	public void websocket_events() {
		WebSocket socket = WebSocket.get("local_socket");

		socket.getSocket().on("discord-callback",new Emitter.Listener() {

			@Override
			public void call(Object... args) {
				JsonObject response = new JsonParser().parse(((JSONObject) args[0]).toString()).getAsJsonObject();
				String uuid = response.get("uuid").getAsString();
				String action = response.get("action").getAsString();

				HylexPlayer player = HylexPlayer.get(uuid);

				if (player != null) {

					if (action.equalsIgnoreCase("link")) {
						String account_id = response.get("account_id").getAsString();
						String name = response.get("name_discord").getAsString();

						player.getPlayer()
								.sendMessage("§aGochaa!! §7A sua conta foi vinculada com o discord §a" + name);
						player.getPlayer().sendMessage("§7");
						player.getPlayer().sendMessage(
								"§7Agora você terá disponível todas as funcionalidades exclusivas de contas §9Discord ♺");
						player.getPlayer().sendMessage("§7");
						player.getPlayer().sendMessage("§a✔ §7Cargos sincronizados com o seu discord.");
						if (player.getPlayer().hasPermission("hylex.staffload"))
							player.getPlayer().sendMessage("§a✔ §7Acesso ao sistema de segurança 'LoginStaff'.");

						player.getDiscord().setAccountId(account_id);
						player.getDiscord().setSituation(Situation.LINKED);
						player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F,
								1F);
					} else {
						String name = response.get("name_discord").getAsString();

						player.getPlayer()
								.sendMessage("§aGochaa!! §7A sua conta foi §cdesvinculada §7com o discord §a" + name);
						player.getPlayer().sendMessage("§7");
						player.getPlayer().sendMessage(
								"§7Agora você §f§nperderá§7 todas aS funcionalidades exclusivas de contas §9Discord ♺");
						player.getPlayer().sendMessage("§7");
						player.getPlayer().sendMessage("§c✖ §7Cargos sincronizados com o seu discord.");
						if (player.getPlayer().hasPermission("hylex.staffload"))
							player.getPlayer().sendMessage("§c✖ §7Acesso ao sistema de segurança 'LoginStaff'.");

						player.getDiscord().setAccountId(null);
						player.getDiscord().setSituation(Situation.UNLINKED);
						player.getDiscord().setUniqueToken(null);
						player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_VILLAGER_HURT, 1F,
								1F);
					}

				} else {
					System.err.println(
							"[Hylex - Socket.io] A conta enviada com o UUID não está registrada ou não foi carregada | Code: "
									+ "Invalid Account");
				}

			}
		});

		socket.getSocket().on("callback-info",new Emitter.Listener() {

			@Override
			public void call(Object... args) {
				JsonObject response = new JsonParser().parse(((JSONObject) args[0]).toString()).getAsJsonObject();

				HylexPlayer player = HylexPlayer.get(response.get("uuid").getAsString());

				if (player != null) {
				   player.setupPlayer(response);

				} else {
					System.err.println(
							"[Hylex - Socket.io] A conta enviada com o UUID não está registrada ou não foi carregada | Code: "
									+ "Invalid Account");
				}

			}
		});

		socket.getSocket().on("loginstaff",new Emitter.Listener() {

			@Override
			public void call(Object... args) {
				JsonObject response = new JsonParser().parse(((JSONObject) args[0]).toString()).getAsJsonObject();

				HylexPlayer player = HylexPlayer.get(response.get("uuid").getAsString());

				if (player != null) {
					String name = response.get("account_name").getAsString();
					String token = response.get("activation_token").getAsString();

					player.getPlayer().sendMessage("");
					player.getPlayer().sendMessage("               §a§lSISTEMA 2FA [LOGINSTAFF] §8(Protótipo 1.0.2)");
					player.getPlayer().sendMessage("");
					player.getPlayer().sendMessage("   §7Conta §a§nautenticada §7com sucesso pela conta do discord §9"
							+ name + " §7com o código §f" + token);
					player.getPlayer().sendMessage("");
					player.getPlayer().sendMessage("   §aYAY!!! §7Suas permissões foram carregadas com sucesso.");

					player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2F, 2F);

					player.authenciate();
				}

			}
		});
	}

	public void config() {
		new ConfigurationBuilder("ranks").load();
	}

	public String getSpigotVersion() {
		return spigot_version;
	}

	public HashMap<String, String> getPermissions() {
		return permissions;
	}

	public Core getCore() {
		return core;
	}
	public int getLobbyNumber() {
		return lobbyNumber;
	}
}
