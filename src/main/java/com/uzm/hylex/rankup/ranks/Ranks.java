package com.uzm.hylex.rankup.ranks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import com.uzm.hylex.rankup.java.util.ConfigurationBuilder;
import com.uzm.hylex.rankup.spigot.item.ItemBuilder_1_14_R1;
import com.uzm.hylex.rankup.spigot.item.ItemMethods;

public class Ranks {

	private String name;
	private double coust;
	private String prefix;
	private int id;
	private ItemStack stack;
	private ItemStack stack_mine;
	private String command;
	private List<String> permissions;
	private String time;
	private List<ItemStack> kits;
	public static HashMap<String, Ranks> ranks = new HashMap<String, Ranks>();
	public static ArrayList<String> ranks_organized;
	public static ArrayList<Ranks> ranks_organized_b;

	public Ranks(String name, double coust, String area, String prefix, List<String> perm, int id, String stack,
			String stack_mine, List<ItemStack> kit, String time) {
		if (stack.startsWith("HEAD:")) {
			this.stack = ItemMethods.buildHead(stack.split("HEAD:")[1]);
		} else {
			this.stack = new ItemBuilder_1_14_R1(stack).build();
		}
		if (stack_mine.startsWith("HEAD:")) {
			this.stack_mine = ItemMethods.buildHead(stack_mine.split("HEAD:")[1]);
		} else {
			this.stack_mine = new ItemBuilder_1_14_R1(stack_mine).build();
		}
		this.name = name;
		this.coust = coust;
		this.command = area;
		this.prefix = prefix;
		this.id = id;
		this.permissions = perm;
		this.command = area;
		this.kits = kit;
		this.time = time;

	}

	public List<ItemStack> getKitItems() {
		return kits;
	}

	public static List<Ranks> getOrganizedRanks() {

		return ranks_organized_b;
	}

	public String getName() {
		return name;
	}

	public double getCoust() {
		return coust;
	}

	public ItemStack getIcon() {
		return stack;
	}

	public ItemStack getIcon_Mine() {
		return stack_mine;
	}

	public String getCommand() {
		return command;
	}

	public int getID() {
		return id;
	}

	public String getPrefix() {
		return prefix;
	}

	public List<String> getPermissions() {
		return permissions;
	}

	public static ArrayList<String> getStringOrganizedRanks() {
		return ranks_organized;

	}

	public static ArrayList<String> organizeRanks() {

		List<String> mysqlzao = new ArrayList<>();

		for (Entry<String, Ranks> rs : ranks.entrySet()) {

			mysqlzao.add(rs.getKey() + " : " + rs.getValue().getID());

		}

		ArrayList<String> convert = new ArrayList<>();
		for (String str : mysqlzao)
			convert.add(str.split(" : ")[0] + " : " + str.split(" : ")[1]);
		Collections.sort(convert, new Comparator<String>() {

			@Override
			public int compare(String pt1, String pt2) {
				int killpt1 = Integer.parseInt(pt1.split(" : ")[1]);
				int killpt2 = Integer.parseInt(pt2.split(" : ")[1]);

				return Integer.compare(killpt2, killpt1);
			}

		});

		Collections.reverse(convert);

		return convert;

	}

	public static Ranks get(String p) {
		if (!ranks.containsKey(p.toLowerCase())) {
			return null;
		} else {
			return (Ranks) ranks.get(p.toLowerCase());
		}

	}

	public String getTimeFormat() {
		return time;
	}

	public static void load() {
		YamlConfiguration config = ConfigurationBuilder.find("ranks").get();
		if (config.getConfigurationSection("ranks") != null) {
			for (String key : config.getConfigurationSection("ranks").getKeys(false)) {
				List<String> items = config.getConfigurationSection("ranks").getStringList(key + ".kit");
				List<ItemStack> stacks = new ArrayList<>();
				for (String item : items) {

					stacks.add(new ItemBuilder_1_14_R1(item).build());

				}
				ranks.put(key.toLowerCase(), new Ranks(key, config.getDouble("ranks." + key + ".coust"),
						config.getString("ranks." + key + ".command"),
						config.getString("ranks." + key + ".prefix").replace("&", "§"),
						config.getStringList("ranks." + key + ".permissions"), (ranks.size() + 1),
						config.getString("ranks." + key + ".stack"), config.getString("ranks." + key + ".stack_mine"),
						stacks, config.getString("ranks." + key + ".kittime")));
			}
		} else {
			List<String> perm = new ArrayList<String>();
			perm.add("kit.unranked");
			ranks.put("Unranked".toLowerCase(), new Ranks("Unranked", 0, "/warp minaunranked", "§8Unranked", perm,
					(ranks.size() + 1), "STONE", "STONE", new ArrayList<ItemStack>(), "1 minutos"));
		}

		ranks_organized = organizeRanks();
		ArrayList<Ranks> r = new ArrayList<Ranks>();
		for (String s : getStringOrganizedRanks()) {
			r.add(Ranks.get(s.split(" : ")[0]));
		}
		ranks_organized_b = r;

		Bukkit.getConsoleSender().sendMessage("§b[Hylex] §7Prepare to build §f" + ranks.size() + " §7rankup ranks.");

	}

}
