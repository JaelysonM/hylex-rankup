package com.uzm.hylex.rankup.commands;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import com.google.common.collect.Lists;
import com.uzm.hylex.rankup.Core;
import com.uzm.hylex.rankup.controller.HylexPlayer;
import com.uzm.hylex.rankup.discordhook.DiscordAccount.Situation;
import com.uzm.hylex.rankup.discordhook.TokenController;
import com.uzm.hylex.rankup.spigot.utils.json.JSONMessage;
import com.uzm.hylex.rankup.spigot.utils.json.NMSUtil;
import com.uzm.hylex.services.lan.WebSocket;

import net.minecraft.server.v1_14_R1.IChatBaseComponent;
import net.minecraft.server.v1_14_R1.IChatBaseComponent.ChatSerializer;

public class LoginStaffCommand implements CommandExecutor {

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
		HylexPlayer hylex = HylexPlayer.get(player);
		if (label.equalsIgnoreCase("loginstaff")) {

			switch (args.length) {
			case 0:
				if (hylex.getDiscord() != null) {
					if (hylex.isAuthenciated()) {
						player.sendMessage("§a[LoginStaff] §7Você já está autenticado.");
						return true;
					}
					if (hylex.isRequested()) {
						player.sendMessage("§a[LoginStaff] §cVocê já tem uma autenticação pendente.");
						return true;
					}
					if (hylex.getDiscord().getAccountSituation() == Situation.LINKED) {
						String token = TokenController.buildToken(6);

						try {
							JSONObject json = new JSONObject();
							json.put("account_id", hylex.getDiscord().getAccountId());
							json.put("nickname", player.getName());
							json.put("activation_token", token);
							json.put("uuid", player.getUniqueId().toString());
							hylex.request_authentiation();
							WebSocket.get("local_socket").getSocket().emit("loginstaff", json);

						} catch (Exception e) {
							player.sendMessage(
									"§c[LoginStaff] §cOcorreu um erro ao tentar gerar a requisição, tente novamente.");
							return true;
						}

						JSONMessage message = NMSUtil.buildMessage("   §e★ §aO seu token de autenticação é: §f§l§n");
						JSONMessage.ChatExtra accept = new JSONMessage.ChatExtra(token)
								.clickExtra(JSONMessage.ClickEventType.SUGGEST_COMMAND, token);

						message.addExtra(accept).addExtra(new JSONMessage.ChatExtra("§a clique nele para §fcopiar§a."));

						String msg = message.toString();
						player.sendMessage("§aSistema de segurança §f§nLoginStaff §b§lBETA");
						IChatBaseComponent chat = ChatSerializer.a(msg);
						player.sendMessage("");
						((CraftPlayer) player).getHandle().sendMessage(chat);
						player.sendMessage("");
						player.sendMessage(
								" §f[Instruções] §eEntre no discord do servidor da equipe e digite -loginstaff " + token
										+ " §eaguarde alguns segundos, se você estiver online no servidor você irá receber uma §fmensagem §ee um §fsinal sonoro§e.");

					} else {
						player.getPlayer().sendMessage("§b[Hylex] §cSua conta ainda não foi vinculada com o discord.");
					}

				} else {
					player.getPlayer().sendMessage("§b[Hylex] §cSua conta ainda não foi carregada aguarde um pouco.");
				}

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
		return Lists.newArrayList("loginstaff");
	}

}
