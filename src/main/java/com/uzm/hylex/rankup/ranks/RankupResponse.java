package com.uzm.hylex.rankup.ranks;

public class RankupResponse {
	public enum Type {
		NOFOUNDS, LASTRANK, ERROR, RANKUP
	}

	private String message;
	private Ranks rank;
	private Type type;

	public RankupResponse(Ranks rank, Type type, String message) {
		this.rank = rank;
		this.message = message;
		this.type = type;
	}

	public Type getType() {
		return type;

	}

	public Ranks getRank() {
		return rank;

	}

	public String getMessage() {
		return message;

	}

}
