package com.uzm.hylex.rankup.spigot.utils;

import java.text.NumberFormat;
import java.util.Locale;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.uzm.hylex.rankup.Core;
import com.uzm.hylex.rankup.controller.HylexPlayer;
import com.uzm.hylex.rankup.discordhook.TokenController;
import com.uzm.hylex.rankup.discordhook.DiscordAccount.Situation;
import com.uzm.hylex.rankup.ranks.RankupResponse;
import com.uzm.hylex.rankup.ranks.RankupResponse.Type;
import com.uzm.hylex.rankup.spigot.utils.json.JSONMessage;
import com.uzm.hylex.rankup.spigot.utils.json.NMSUtil;

import net.minecraft.server.v1_14_R1.ChatMessageType;
import net.minecraft.server.v1_14_R1.IChatBaseComponent;
import net.minecraft.server.v1_14_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_14_R1.PacketPlayOutChat;
import net.minecraft.server.v1_14_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_14_R1.PacketPlayOutTitle.EnumTitleAction;

public class HylexMethods {

	public static boolean isNumeric(String strNum) {
		return strNum.matches("-?\\d+(\\.\\d+)?");
	}

	public enum StaffAction {
		FORCERANKUP("§7[%player% forcou a evolução de %actionname% para §n%loc%]"),
		TELEPORTTO("§7[%player% teletransportou-se para §n%actionname%]"),
		TPALL("§7[%player% teletransportou todos os(as) %actionname% até ele]"), INVENTORYSEE(""), GAMEMODE("");

		private String message;

		private StaffAction(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}

	}

	public static String modifyBalance(double value) {

		NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.forLanguageTag("en-US"));
		if (value <= 1.0D) {
			if (numberFormat.format(value).concat(" ").concat("").replace(" ", "").split("\\.").length > 1) {
				if (numberFormat.format(value).concat(" ").concat("").replace(" ", "").split("\\.")[1].length() == 3) {
					return numberFormat.format(value).replace(" ", "").substring(0,
							numberFormat.format(value).replace(" ", "").length() - 1);
				}
			} else {
				return numberFormat.format(value).concat(" ").concat("").replace(" ", "");
			}

		} else if (numberFormat.format(value).concat(" ").concat("").replace(" ", "").split("\\.").length > 1) {
			if (numberFormat.format(value).concat(" ").concat("").replace(" ", "").split("\\.")[1].length() == 3) {
				return numberFormat.format(value).replace(" ", "").substring(0,
						numberFormat.format(value).replace(" ", "").length() - 1);
			}
		} else {
			return numberFormat.format(value).concat(" ").concat("").replace(" ", "");
		}
		return numberFormat.format(value).concat(" ").concat("").replace(" ", "");

	}

	public static void sendTitle(Player jogador, String mensagem) {
		PacketPlayOutTitle packet1 = new PacketPlayOutTitle(EnumTitleAction.TITLE,
				ChatSerializer.a("{\"text\": \"\"}").a(mensagem), 10, 100, 100);
		((CraftPlayer) jogador).getHandle().playerConnection.sendPacket(packet1);
	}

	public static void sendSubTitle(Player jogador, String mensagem) {
		PacketPlayOutTitle packet1 = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE,
				ChatSerializer.a("{\"text\": \"\"}").a(mensagem), 10, 100, 100);
		((CraftPlayer) jogador).getHandle().playerConnection.sendPacket(packet1);
	}

	public static void sendActionBar(Player player, String message) {
		CraftPlayer p = (CraftPlayer) player;

		IChatBaseComponent cbc = ChatSerializer.a("{\"text\": \"" + message + "\"}");
		PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, ChatMessageType.GAME_INFO);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(ppoc);
	}

	public static String getProgressBar(int paramInt) {
		if (paramInt >= 100) {
			return "§8[§a∎∎∎∎∎∎∎∎∎∎§8]";
		}
		int i = 0;
		if (paramInt > 0) {
			i = paramInt / 10;
		}
		StringBuilder localStringBuilder = new StringBuilder("§8[");
		if (i > 0) {
			localStringBuilder.append("§a");
		}
		for (int j = 0; j < 10; j++) {
			if (j == i) {
				localStringBuilder.append("§7");
			}
			localStringBuilder.append("∎");
		}
		localStringBuilder.append("§8]");

		return ChatColor.translateAlternateColorCodes('&', localStringBuilder.toString());
	}

	public static void externalRankup(Player p) {
		if (HylexPlayer.get(p) != null) {
			HylexPlayer player = HylexPlayer.get(p);
			double balance = Core.getVaultHook().getBalance(p);

			if (balance >= player.getNext().getCoust()) {

				RankupResponse r = player.rankup(balance);
				p.sendMessage(r.getMessage().replace("%rank%", r.getRank().getName()));
				if (r.getType() == Type.LASTRANK) {
					p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2, 2);
				}
				if (r.getType() == Type.NOFOUNDS) {
					p.playSound(p.getLocation(), Sound.ENTITY_IRON_GOLEM_HURT, 2, 2);
				}
				p.closeInventory();

			} else {

				RankupResponse r = new RankupResponse(player.getNext(), RankupResponse.Type.NOFOUNDS,
						"§cVocê não possui fundos suficientes para upar para o rank: §f%rank%§c!");
				p.sendMessage(r.getMessage().replace("%rank%", r.getRank().getName()));
				p.playSound(p.getLocation(), Sound.ENTITY_IRON_GOLEM_HURT, 2, 2);
				p.closeInventory();

			}
		}
	}

	public static void externalDiscordHook(Player p) {
		HylexPlayer hylex = HylexPlayer.get(p);
		String token = TokenController.buildToken(6);
		hylex.getDiscord().setUniqueToken(token);
		hylex.getDiscord().setSituation(Situation.PENDING);
		hylex.getDiscord().setAccountId(null);
		hylex.save(new String[] {});

		JSONMessage message = NMSUtil.buildMessage("   §e★ §aO seu token de vinculação é: §f§l§n");
		JSONMessage.ChatExtra accept = new JSONMessage.ChatExtra(token)
				.clickExtra(JSONMessage.ClickEventType.SUGGEST_COMMAND, token);

		message.addExtra(accept).addExtra(new JSONMessage.ChatExtra("§aa clique nele para §fcopiar§a."));

		String msg = message.toString();

		IChatBaseComponent chat = ChatSerializer.a(msg);
		p.sendMessage(" §f[Instruções] §eEntre no discord do servidor e digite -vincular " + token
				+ " §eaguarde alguns segundos, se você estiver online no servidor você irá receber uma §fmensagem §ee um §fsinal sonoro§e.");

		p.sendMessage("");
		((CraftPlayer) p).getHandle().sendMessage(chat);
		p.sendMessage("");
	}

}
