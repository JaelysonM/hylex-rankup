package com.uzm.hylex.rankup.spigot.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationMethods {
	public static String serializeLocation(Location loc) {
		String toString = loc.getWorld().getName() + " : " + loc.getX() + " : " + loc.getY() + " : " + loc.getZ()
				+ " : " + loc.getPitch() + " : " + loc.getYaw();
		return toString;
	}

	public static Location unserializeLocation(String path) {
		String[] sp = path.split(" : ");
		Location loc = new Location(Bukkit.getWorld(sp[0]), Double.parseDouble(sp[1]), Double.parseDouble(sp[2]),
				Double.parseDouble(sp[3]));
		loc.setPitch(Float.parseFloat(sp[4]));
		loc.setYaw(Float.parseFloat(sp[5]));
		return loc;
	}
}
