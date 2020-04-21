package com.uzm.hylex.rankup.discordhook;

import com.uzm.hylex.rankup.controller.HylexPlayer;

public class DiscordAccount {

	private HylexPlayer player;
	private String id = null;
	private Situation situation;
	private String u_token;

	public enum Situation {
		UNLINKED("§cDesvinculado"), PENDING("§eConfirmação pedente"), LINKED("§aVinculado");
		private String name;

		private Situation(String name) {
			this.name = name;
		}

		public String getDisplay() {
			return name;
		}
	}

	public DiscordAccount(HylexPlayer player, Situation situation, String u_token) {
		this.u_token = u_token;
		this.situation = situation;
		this.player = player;

	}

	public void setAccountId(String id) {
		this.id = id;
	}

	public void setSituation(Situation situation) {
		this.situation = situation;
	}

	public void setUniqueToken(String uniquetoken) {
		this.u_token = uniquetoken;
	}

	public String getUniqueToken() {
		return u_token;

	}

	public Situation getAccountSituation() {
		return situation;

	}

	public HylexPlayer getHylexPlayer() {
		return player;
	}

	public String getAccountId() {
		return id;
	}

}
