package me.hammale.koth;

import org.bukkit.ChatColor;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class kPlayer {
	
	Player player;
	koth plugin;
	int id;
	boolean active;
	
	public kPlayer(koth plugin, Player p){
		this.plugin = plugin;
		this.player = p;
	}
	
	public void start(){
		active = true;
		run();
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public void stop(){
		active = false;
		plugin.getServer().getScheduler().cancelTask(id);
		plugin.active.remove(player.getName());
	}
	
	private void run(){
		id = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			   public void run() {
			       if(active
			    		    && player.getLocation().getBlock().getRelative(BlockFace.DOWN, 1).getX() == plugin.blockLoc.getBlockX()
							&& player.getLocation().getBlock().getRelative(BlockFace.DOWN, 1).getY() == plugin.blockLoc.getBlockY()
							&& player.getLocation().getBlock().getRelative(BlockFace.DOWN, 1).getZ() == plugin.blockLoc.getBlockZ()){
			    	   plugin.getServer().dispatchCommand(
								plugin.getServer().getConsoleSender(),
								"eco give " + player.getName() + " 2000");
			    	   player.sendMessage(ChatColor.GREEN + "[DeathPVP] You earned $2,000!");
			       }else{
			    	   stop();
			       }
			   }
		}, 300L, 300L);
	}
	
	
	
}
