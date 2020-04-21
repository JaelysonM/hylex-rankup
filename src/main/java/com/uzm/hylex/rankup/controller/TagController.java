package com.uzm.hylex.rankup.controller;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.google.common.collect.Maps;
import com.uzm.hylex.services.Core;


public class TagController
{
 
   public static HashMap<String, TagController> tags = Maps.newHashMap();
   
   private String uuid;
   private String prefix = "§7";
   private String suffix = "";
   private String order= "z";
   private Player player;
   
   
   public TagController(String uuid) {
	   this.uuid=uuid;
	   this.player = Bukkit.getPlayer(UUID.fromString(uuid));
   }
   
   
   public void setPrefix(String p) {
	   this.prefix=p;
   }
   public void setSuffix(String s) {
	   this.suffix=s;
   }
   public void setOrder(String s) {

	   this.order=s;
   }
   
   public String getSuffix() {
	return suffix;
	   
   }
   public String getPrefix() {
	return prefix;
   }
   public String getUUID() {
	   return uuid;
   }
   public String getOrder() {
		return order;		   
   }
   public Player getPlayer() {
 		return player;
 		   
 	   }
   
   
   public static TagController create(Player p) {
	   if (tags.containsKey(p)) {
	       return get(p);
	     }
	     return tags.put(p.getUniqueId().toString(), new TagController(p.getUniqueId().toString()));
   }
   
   public static TagController delete(Player p) {
	   if (tags.containsKey(p)) {
	       return tags.remove(p.getUniqueId().toString());
	     }else {
	        return null;
	     }
	   
   }
   
   
   public static TagController get(Player p) {
	   if (!tags.containsKey(p.getUniqueId().toString())) {
			  return null;
		  }else {
			   return (TagController)tags.get(p.getUniqueId().toString());		  
		  }
   }
   
   
   public static void task() {
	   new BukkitRunnable() {
			
			public void run() {
				for (TagController p : getDatas()) {
			          p.update();
					}	
			}
		}.runTaskTimerAsynchronously(Core.getInstance(), 20L, 60L);
   }
   
   
   
   public static ArrayList<TagController> getDatas() {
	   return new ArrayList<TagController>(tags.values());
   }
   
   public static Team getTeam(Scoreboard board, String name) {
	   if (board.getTeam(name) !=null) {
		   return board.getTeam(name);
	   }else {
		   return board.registerNewTeam(name);
	   }
   }
   @SuppressWarnings("deprecation")
public void update() {
	 
	   HylexPlayer hylex =HylexPlayer.get(player);
	    if (hylex !=null) {
	    	if (hylex.getTeam() != null) {
	 		   if (!hylex.getTeam().getName().equalsIgnoreCase(order + (player.getUniqueId().toString().length() > 15 ? player.getUniqueId().toString().substring(0, 15): player.getUniqueId().toString()))) {
	 			   hylex.getTeam().unregister();
	 			   Team team = getTeam(player.getScoreboard(
	 					   
	 					   ), order + (player.getUniqueId().toString().length() > 15 ? player.getUniqueId().toString().substring(0, 15): player.getUniqueId().toString()));
	 			   if (!team.hasPlayer(player)) {
	 		    	   HylexPlayer.get(player).setTeam(team);
	 		    	   team.addPlayer(player);   
	 		       } 
	 			   
	 		   }else {
	 			   if (! hylex.getTeam().hasPlayer(player)) hylex.getTeam().addPlayer(player);   
	 		        
	 			   hylex.getTeam().setPrefix(prefix);
	 			   hylex.getTeam().setSuffix(suffix); 
	 		   }
	 		   
	 	   }else {
	 		   Team team = getTeam(player.getScoreboard(
	 				   
	 				   ), order + (player.getUniqueId().toString().length() > 15 ? player.getUniqueId().toString().substring(0, 15): player.getUniqueId().toString()));
	 		   if (!team.hasPlayer(player)) {
	 	    	   HylexPlayer.get(player).setTeam(team);
	 	    	   team.addPlayer(player);   
	 	       } 
	 		   team.setPrefix(prefix);
	 	       team.setSuffix(suffix);
	 	   } 	
	    }
	   
	   
	 
     
 
	   
   }
   
 

}