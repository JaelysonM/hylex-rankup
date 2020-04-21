
package com.uzm.hylex.rankup.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.uzm.hylex.rankup.Core;
import com.uzm.hylex.rankup.discordhook.DiscordAccount;
import com.uzm.hylex.rankup.discordhook.DiscordAccount.Situation;
import com.uzm.hylex.rankup.ranks.Ranks;
import com.uzm.hylex.rankup.ranks.RankupResponse;
import com.uzm.hylex.rankup.ranks.RankupResponse.Type;
import com.uzm.hylex.rankup.spigot.SpigotFeatures;
import com.uzm.hylex.rankup.spigot.utils.HylexMethods;
import com.uzm.hylex.rankup.spigot.utils.HylexMethods.StaffAction;
import com.uzm.hylex.services.lan.WebSocket;

public class HylexPlayer {

	private String name;
	private Player player;
	private String uuid;
	private Group group;

	private Ranks rank = Ranks.getOrganizedRanks().get(0);
	private int kills = 0;
	private int breaked_blocks = 0;
	// private List<String> historic;
	private int prestige = 0;

	private Team team;

	private String id;
	private boolean authenciated = false;
	private boolean pending_authentication = false;
	public static ArrayList<Player> staff = Lists.newArrayList();
	public static HashMap<String, HylexPlayer> datas = new HashMap<String, HylexPlayer>();

	private DiscordAccount discord;

	public HylexPlayer(Player player) {
		this.name = player.getName();
		this.uuid = player.getUniqueId().toString();
		this.player = player;

		this.rank = Ranks.getOrganizedRanks().get(0);
		
		for (Group g : Group.values()) {
			if (player.hasPermission(g.getPermission())) {
				this.group = g;
				break;
			}
		}
		if (getGroup() == null) {
			this.group = Group.NORMAL;
		}
	}

	public void setupPlayer(JsonObject response) {
		
		JsonElement rank_e = response.get("stats").getAsJsonObject().get("rank");
		String rank_f = rank_e.isJsonNull() ? null : rank_e.getAsString();
		String rank = rank_f == null ? "" : rank_f;
		setName((String) response.get("nickname").getAsString());
		setID(response.get("_id").getAsString());

		setRank(Ranks.get(rank) == null ? Ranks.getOrganizedRanks().get(0) : Ranks.get(rank));
		setBreakedBlocks(
				response.get("stats").getAsJsonObject().get("break_actual_rank").getAsInt());
		setKills(response.get("stats").getAsJsonObject().get("kill_actual_rank").getAsInt());

		setPrestige(response.get("stats").getAsJsonObject().get("prestige").getAsInt());

		Situation situation = Situation.valueOf(response.get("discord").getAsJsonObject()
				.get("account_situation").getAsString().toUpperCase());

		setDiscord(new DiscordAccount(this, situation,
				situation == Situation.UNLINKED ? null : response.get("token").getAsString()));
		getDiscord().setAccountId(situation == Situation.UNLINKED ? null
				: response.get("discord").getAsJsonObject().get("account_id").getAsString());

		player.getPlayer().sendMessage("§aGochaa!! §7Conta carregada com sucesso.");
		TagController.create(player);
		TagController tag = TagController.get(player);

		tag.setPrefix(group.getDisplay());
		tag.setOrder(group.getOrder());
		tag.setSuffix(" §f" +getPrestige() + "§e✰");
		tag.update();
		player.setDisplayName(group.getDisplay() + player.getName());
		
		SpigotFeatures features = new SpigotFeatures(player);

		features.tabColor("\n §b§lHYLEX \n    §7Seja bem-vindo §E" + player.getName() + "§7." + "\n",
				"\n §7Seu grupo é: " + group.getName() + "\n§7Você está em: §fRankup #"
						+ Core.getLoader().getLobbyNumber() + "\n\n§b§nhylex.net§r ");

	}

	
	public String getUUID() {
		return uuid;
	}

	public int getPrestige() {
		return prestige;
	}

	public void addPrestige() {
		this.prestige += 1;
	}

	public String getName() {
		return name;
	}

	public Player getPlayer() {
		return player;
	}

	public Ranks getRank() {
		return rank;
	}

	public int getKills() {
		return kills;
	}

	public int getBreakedBlocks() {
		return breaked_blocks;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public void setKills(int x) {
		kills = x;
	}

	public void setBreakedBlocks(int x) {
		breaked_blocks = x;
	}

	public void setRank(Ranks rank) {
		this.rank = rank;
	}

	public String getID() {
		return id;
	}

	public void setID(String id) {
		this.id = id;
	}

	public void setName(String nick) {
		this.name = nick;
	}

	public void authenciate() {
		authenciated = true;
	}

	public boolean isAuthenciated() {
		return authenciated;
	}

	public void request_authentiation() {
		pending_authentication = true;
	}

	public boolean isRequested() {
		return pending_authentication;
	}

	public void setDiscord(DiscordAccount account) {
		discord = account;
	}

	public DiscordAccount getDiscord() {
		return discord;
	}

	public void setPrestige(int x) {
		prestige = x;
	}

	public void aut(Player p) {
		player = p;
	}

	public void setPlayer(Player p) {
		player = p;
	}
    public Group getGroup() {
    	return group;
    }
	public Ranks getNext() {
		int indexof = Ranks.getOrganizedRanks().indexOf(getRank());
		if ((indexof + 1) >= Ranks.getOrganizedRanks().size()) {
			return null;
		} else {
			return Ranks.getOrganizedRanks().get(indexof + 1);
		}
	}

	public String scoreboardFormat(double balance) {
		return (Ranks.getOrganizedRanks().indexOf(getRank()) + 1) >= Ranks.getOrganizedRanks().size()
				? "§8[§a✰  §e+1 §8/prestigio]"
				: getProgessInt(balance) + "% §7- §r"
						+ HylexMethods.getProgressBar((int) ((balance / getNext().getCoust()) * 100));
	}

	public String getProgessBar(double balance) {
		return (Ranks.getOrganizedRanks().indexOf(getRank()) + 1) >= Ranks.getOrganizedRanks().size()
				? "§8[Você pode dar prestígio]"
				: HylexMethods.getProgressBar((int) ((balance / getNext().getCoust()) * 100));

	}

	public String getProgessInt(double balance) {
		return (Ranks.getOrganizedRanks().indexOf(getRank()) + 1) >= Ranks.getOrganizedRanks().size() ? "100" :

				(balance / getNext().getCoust()) * 100 > 100 ? "100"
						: HylexMethods.modifyBalance(((balance / getNext().getCoust()) * 100));

	}

	public void broadcastAction(StaffAction action, String actionname, String alias2) {

		for (Player staff : staff) {

			if (staff.getPlayer() != getPlayer()) {
				staff.getPlayer().sendMessage(action.getMessage().replace("%player%", getPlayer().getName())
						.replace("%found%", alias2).replace("%actionname%", actionname));

			}
		}
		/*
		 * Implements soon
		 */
		// socket.emit("staff_broadcast", msg);
	}

	/*
	 * Rankup methods
	 */

	public boolean prestige() {

		if ((Ranks.getOrganizedRanks().indexOf(getRank()) + 1) >= Ranks.getOrganizedRanks().size()) {

			setRank(Ranks.getOrganizedRanks().get(0));
			addPrestige();
			TagController tag = TagController.get(player);
			tag.setPrefix("§c[CEO] ");
			tag.setOrder("a");
			tag.setSuffix(" §f" + getPrestige() + "§e✰");
			int toadd = (getPrestige() * 3) + 2;
			HealthController h = new HealthController(getPlayer());
			h.addHealth((getPrestige() * 3) + 2);

			for (Player pls : Bukkit.getOnlinePlayers()) {
				HylexMethods.sendTitle(pls, "§e♕ §f" + getName() + " §e♕ ");
				HylexMethods.sendSubTitle(pls, "§a✰  §e+1 §fprestígio (§A" + getPrestige() + "§F)");
				pls.playSound(pls.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);

			}

			player.sendMessage("");
			player.sendMessage(
					"§f* §7Parabéns você deu §eprestígio, §7agora você retornou para o primeiro rank do servidor, porém você não voltou de §a§nmãos vazias§7.");
			player.sendMessage("");
			player.sendMessage("§a[Confira abaixo os seus prêmios]");

			player.sendMessage("");
			player.sendMessage(" §f- §a+ §c" + toadd + " §fde vida");
			player.sendMessage(" §f- §a+ §eUm item aleatório");
			player.sendMessage(" §f- §a+ §e1 ✰ (prestígio)");
			player.sendMessage("");
			player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);

			player.closeInventory();

			save(new String[] {});
			return true;
		} else {
			return false;
		}

	}

	public RankupResponse rankup(double balance) {

		if (getRank().getID() < Ranks.ranks.size()) {

			Ranks nextrank = getNext();
			if (balance >= getNext().getCoust()) {

				HylexMethods.sendActionBar(player, "§e! §aVocê upou para o rank: " + nextrank.getPrefix() + "§f.");
				player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);

				if (nextrank.getID() == Ranks.ranks.size()) {
					for (Player pls : Bukkit.getOnlinePlayers()) {
						HylexMethods.sendTitle(pls, "§e♕ §f" + getName() + " §e♕ ");
						HylexMethods.sendSubTitle(pls, "§aChegou ao último rank.");
						pls.playSound(pls.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
						if (pls != player)
							HylexMethods.sendActionBar(pls,
									"§e! §f" + getName() + " §aupou para o rank: " + nextrank.getPrefix() + "§a.");
					}

				}

				Core.getVaultHook().withdrawPlayer(player, nextrank.getCoust());
				setRank(nextrank);
				save(new String[] {});

				/*
				 * for (String perms : nextrank.getPermissions()) {
				 * //Manager.addPermisson(getName(), perms);
				 * 
				 * }
				 */

				return new RankupResponse(nextrank, Type.RANKUP,
						"§a* Você upou para o rank: §f" + nextrank.getName() + "§a.");
			} else {
				return new RankupResponse(nextrank, Type.NOFOUNDS,
						"§cVocê não possui fundos suficientes para upar para o rank: §f%rank%§c!");
			}

		} else {
			return new RankupResponse(getRank(), Type.LASTRANK, "§e* O rank: §f%rank% §eé o último rank, parabéns!");
		}
	}

	public RankupResponse forcerankup() {

		if (getRank().getID() < Ranks.ranks.size()) {

			Ranks nextrank = getNext();

			HylexMethods.sendActionBar(player, "§e! §aVocê upou para o rank: " + nextrank.getPrefix() + "§f.");
			player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);

			if (nextrank.getID() == Ranks.ranks.size()) {
				for (Player pls : Bukkit.getOnlinePlayers()) {
					HylexMethods.sendTitle(pls, "§e♕ §f" + getName() + " §e♕ ");
					HylexMethods.sendSubTitle(pls, "§aChegou ao último rank.");
					pls.playSound(pls.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
					if (pls != player)
						HylexMethods.sendActionBar(pls,
								"§e! §f" + getName() + " §aupou para o rank: " + nextrank.getPrefix() + "§a.");
				}

			}

			setRank(nextrank);
			save(new String[] {});

			/*
			 * for (String perms : nextrank.getPermissions()) {
			 * //Manager.addPermisson(getName(), perms);
			 * 
			 * }
			 */

			return new RankupResponse(nextrank, Type.RANKUP,
					"§a* Você upou para o rank: §f" + nextrank.getName() + "§a.");

		} else {
			return new RankupResponse(getRank(), Type.LASTRANK, "§e* O rank: §f%rank% §eé o último rank, parabéns!");
		}
	}

	/*
	 * Account controller
	 */

	public static HylexPlayer create(Player player) {
		if (datas.containsKey(player)) {
			return get(player);
		}
		datas.put(player.getUniqueId().toString(), new HylexPlayer(player));
		if (!staff.contains(player) && player.hasPermission("hylex.staff")) {
			staff.add(player);
		}
		return get(player);
	}

	public static void remove(Player p) {
		if (datas.containsKey(p)) {
			if (staff.contains(p)) {
				staff.remove(p);
			}
			datas.remove(p);

		}
	}

	public static HylexPlayer get(Player p) {
		if (!datas.containsKey(p.getUniqueId().toString())) {
			return null;
		} else {
			return (HylexPlayer) datas.get(p.getUniqueId().toString());
		}
	}

	public static HylexPlayer get(String uuid) {
		if (!datas.containsKey(uuid)) {
			return null;
		} else {
			return (HylexPlayer) datas.get(uuid);
		}

	}

	public void requestLoad() {
		try {
			JSONObject json = new JSONObject();
			json.put("require", "user-info");
			JSONObject array = new JSONObject();
			array.put("uuid", getPlayer().getUniqueId());
			array.put("nickname", getPlayer().getName());
			json.put("bodyDefault", array);

			WebSocket.get("local_socket").getSocket().emit("require-info", json);

		} catch (JSONException err) {
			err.printStackTrace();
		}
	}

	public void save(String[] toupdate) {
		try {
			if (toupdate.length == 0) {
				JSONObject json = new JSONObject();
				json.put("id", getID());
				JSONObject finalarray = new JSONObject();

				JSONObject stats = new JSONObject();
				stats.put("rank", getRank().getName() != null ? getRank().getName() : null);
				stats.put("prestige", getPrestige());
				stats.put("kill_actual_rank", getKills());
				stats.put("break_actual_rank", getBreakedBlocks());

				JSONObject discord = new JSONObject();
				discord.put("account_id", getDiscord().getAccountId());
				discord.put("account_situation", getDiscord().getAccountSituation().toString());

				finalarray.put("stats", stats);
				finalarray.put("discord", discord);
				finalarray.put("token", getDiscord().getUniqueToken());
				json.put("body", finalarray);

				/*
				 * Send socket.io request
				 */
				WebSocket.get("local_socket").getSocket().emit("save-account", json);

			} else {

				List<String> list = Lists.newArrayList(toupdate);
				JSONObject json = new JSONObject();
				json.put("id", getID());
				JSONObject finalarray = new JSONObject();

				JSONObject array = new JSONObject();

				if (list.contains("rank")) {
					array.put("rank", getRank().getName() != null ? getRank().getName() : null);
				}
				if (list.contains("prestige")) {
					array.put("prestige", getPrestige());

				}
				if (list.contains("kill_actual_rank")) {
					array.put("kill_actual_rank", getKills());

				}

				if (list.contains("break_actual_rank")) {
					array.put("break_actual_rank", getBreakedBlocks());

				}

				finalarray.put("stats", array);
				json.put("body", finalarray);

				WebSocket.get("local_socket").getSocket().emit("save-account", json);

			}
		} catch (Exception err) {
			System.err.println(
					"[Hylex - Socket.io (Event)] Ocorreu um erro ao gerar o JSON | Code: " + err.getLocalizedMessage());

		}

	}

	public static List<HylexPlayer> getDatas() {
		return new ArrayList<HylexPlayer>(datas.values());
	}

	public enum Group {

		HYLEX("§bHylex", "§b[Hylex] ", "*", "a", true),
		GERENTE("§4Gerente", "§4[Gerente] ", "hylex.group.gerente", "b", true),
		ADMIN("§9Admin", "§9[Admin] ", "hylex.group.admin", "c", true),
		DESENVOLVEDOR("§6Desenvolvedor", "§6[Desenvolvedor] ", "hylex.group.dev", "d", true),
		MODERADOR("§2Moderador", "§2[Moderador]", "hylex.group.mod", "e", true),
		AJUDANTE("§eAjudante", "§e[Ajudante] ", "hylex.group.ajd", "f", true),
		STREAMER("§5Streamer", "§5[Streamer] ", "hylex.group.streamer", "h", true),
		MINIYT("§cMiniYT", "§c[MiniYT] ", "hylex.group.miniyt", "i", true),
		NORMAL("§7Normal", "§7", "hylex.group.normal", "j");

		private String display;
		private String permission;
		private String order;
		private String name;
		private boolean alwaysVisible;

		private Group(String name, String display, String permission, String order, boolean visible) {
			this.display = display;
			this.order = order;
			this.permission = permission;
			this.name = name;
			this.alwaysVisible = visible;
		}

		private Group(String name, String display, String permission, String order) {
			this.display = display;
			this.order = order;
			this.permission = permission;
			this.name = name;
			this.alwaysVisible = false;
		}

		public String getDisplay() {
			return display;
		}

		public boolean isAlwaysVisible() {
			return alwaysVisible;
		}

		public String getPermission() {
			return permission;
		}

		public String getOrder() {
			return order;
		}

		public String getName() {
			return name;
		}

	}
}
