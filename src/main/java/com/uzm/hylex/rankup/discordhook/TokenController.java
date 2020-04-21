package com.uzm.hylex.rankup.discordhook;

import java.util.Random;

public class TokenController {

	static public String buildToken(int chars) {
		String CharSet = "abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ234567890!@#$";
		String Token = "";
		for (int a = 1; a <= chars; a++) {
			Token += CharSet.charAt(new Random().nextInt(CharSet.length()));
		}
		return Token;
	}

}
