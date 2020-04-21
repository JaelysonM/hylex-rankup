package com.uzm.hylex.rankup.spigot.utils.json;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JSONMessage {
	private JSONObject chatObject;

	@SuppressWarnings("unchecked")
	public JSONMessage(String text) {
		chatObject = new JSONObject();
		chatObject.put("text", text);
	}

	@SuppressWarnings("unchecked")
	public JSONMessage addExtra(ChatExtra extraObject) {
		if (!chatObject.containsKey("extra")) {
			chatObject.put("extra", new JSONArray());
		}
		JSONArray extra = (JSONArray) chatObject.get("extra");
		extra.add(extraObject.toJSON());
		chatObject.put("extra", extra);
		return this;
	}

	public String toString() {
		return chatObject.toJSONString();
	}

	public static class ChatExtra {
		private JSONObject chatExtra;

		@SuppressWarnings("unchecked")
		public ChatExtra(String text) {
			chatExtra = new JSONObject();
			chatExtra.put("text", text);
		}

		@SuppressWarnings("unchecked")
		public ChatExtra clickExtra(ClickEventType action, String value) {
			JSONObject clickEvent = new JSONObject();
			clickEvent.put("action", action.getTypeString());
			clickEvent.put("value", value);
			chatExtra.put("clickEvent", clickEvent);
			return this;
		}

		@SuppressWarnings("unchecked")
		public ChatExtra hoverExtra(HoverEventType action, String value) {
			JSONObject hoverEvent = new JSONObject();
			hoverEvent.put("action", action.getTypeString());
			hoverEvent.put("value", value);
			chatExtra.put("hoverEvent", hoverEvent);
			return this;
		}

		public JSONObject toJSON() {
			return chatExtra;
		}

		public ChatExtra build() {
			return this;
		}
	}

	public static enum ClickEventType {
		RUN_COMMAND, SUGGEST_COMMAND, OPEN_URL, CHANGE_PAGE;

		public String getTypeString() {
			return name().toLowerCase();
		}
	}

	public static enum HoverEventType {
		SHOW_TEXT, SHOW_ITEM, SHOW_ACHIEVEMENT;

		public String getTypeString() {
			return name().toLowerCase();
		}
	}
}
