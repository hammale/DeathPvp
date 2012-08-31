package me.hammale.koth;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class listener implements Listener {
	
	koth plugin;
	
	public listener(koth plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerLogout(PlayerQuitEvent e){
		kPlayer tp = plugin.getPlayer(e.getPlayer());
		tp.stop();
		plugin.players.remove(tp);
		if(plugin.fire.contains(e.getPlayer().getName())){
			plugin.fire.remove(e.getPlayer().getName());
		}
	}
	
	@EventHandler
	public void onPlayerLogin(PlayerJoinEvent e){
		plugin.players.add(new kPlayer(plugin, e.getPlayer()));
	}
	
	@EventHandler
	public void PlayerDamage(EntityDamageEvent event) {
	    if ((event.getEntity() instanceof Player)) {
	      Player player = (Player)event.getEntity();
	      if (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
	        EntityDamageEvent damageEvent = player.getLastDamageCause();
	        Entity arrow = ((EntityDamageByEntityEvent)damageEvent).getDamager();
	        Block headblock = player.getWorld().getBlockAt(player.getEyeLocation());
	        Block arrowblock = player.getWorld().getBlockAt(arrow.getLocation());
	        Projectile projectile = (Projectile)((EntityDamageByEntityEvent)damageEvent).getDamager();
	        if (((projectile.getShooter() instanceof Player)) && 
	          (arrowblock == headblock)) {
	          ((Player) event.getEntity()).damage(1000);
	        }
	      }else if (event instanceof EntityDamageByEntityEvent) {
	    	  EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) event;
	    	  if(damageEvent.getDamager() instanceof Player){
	    		  Player killer = (Player) damageEvent.getDamager();
	    		  if(killer.getItemInHand().getType() == Material.DIAMOND_AXE
	    				  && plugin.fire.contains(killer.getName())){
	    		  player.setFireTicks(player.getFireTicks()+200);
	    		  }
	    	  }
		  }
	    }
	 }
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e){
		e.getDrops().clear();
		Player p = e.getEntity();
		if (p.getKiller() instanceof Player){
			Player killer = p.getKiller();
			plugin.getServer().dispatchCommand(
					plugin.getServer().getConsoleSender(),
					"eco give " + killer.getName() + " 50");
			killer.sendMessage(ChatColor.GREEN + "[DeathPVP] You earned $50!");
		}
		if(plugin.fire.contains(p.getName())){
			plugin.fire.remove(p.getName());
		}
	}
	
	@EventHandler
	public void onItemDrop(PlayerDropItemEvent e){
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e){
		if(plugin.blockLoc == null){
			return;
		}
		if(e.getBlock().getX() == plugin.blockLoc.getBlockX()
				&& e.getBlock().getY() == plugin.blockLoc.getBlockY()
				&& e.getBlock().getZ() == plugin.blockLoc.getBlockZ()
				&& e.getPlayer().isOp()){
			e.setCancelled(true);
			e.getPlayer().sendMessage(ChatColor.RED + "[DeathPVP] You can't break this block. Use '/pvp set' instead.");
			return;
		}
		if(!e.getPlayer().isOp()){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e){
		if(!e.getPlayer().isOp()){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e){
		if(plugin.blockLoc == null){
			return;
		}
		if(plugin.playersOnline() >= 2 
				&& e.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN, 1).getX() == plugin.blockLoc.getBlockX()
				&& e.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN, 1).getY() == plugin.blockLoc.getBlockY()
				&& e.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN, 1).getZ() == plugin.blockLoc.getBlockZ()){
			if(!plugin.active.contains(e.getPlayer().getName())){
				plugin.active.add(e.getPlayer().getName());
				plugin.getPlayer(e.getPlayer()).start();
			}
		}
	}
	
}
