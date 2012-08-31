package me.hammale.koth;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class koth extends JavaPlugin {
	
	public Logger logger = Logger.getLogger("Minecraft");
	
	public HashSet<String> active = new HashSet<String>();
	public HashSet<kPlayer> players = new HashSet<kPlayer>();
	public HashSet<String> fire = new HashSet<String>();	
	
	Location blockLoc;
	
	@Override
	public void onEnable()
	{
		PluginDescriptionFile pdfFile = getDescription();
		this.logger.info(pdfFile.getName() + ", Version "
				+ pdfFile.getVersion() + ", Has Been Enabled!");
		PluginManager pm = getServer().getPluginManager();
		for(Player p : getServer().getOnlinePlayers()){
			players.add(new kPlayer(this, p));
		}
		pm.registerEvents(new listener(this), this);
		read();
	}
	
	@Override
	public void onDisable(){
		PluginDescriptionFile pdfFile = getDescription();
		this.logger.info(pdfFile.getName() + ", Version "
				+ pdfFile.getVersion() + ", Has Been Enabled!");
	}
	
	public String Colorize(String s) {
	    if (s == null) return null;
	    return s.replaceAll("&([0-9a-f])", "§$1");
	}
	
	public void read(){
		File tmp = new File("plugins/DeathPVP/block.dat");
		if(!tmp.exists()){
			return;
		}
		String arenaloc = null;
		try{
			FileInputStream fstream = new FileInputStream("plugins/DeathPVP/block.dat");
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String strLine;
			  while ((strLine = br.readLine()) != null)   {
				  arenaloc = strLine;
			  }
			  in.close();
		}catch (Exception e){
			  System.err.println("Error: " + e.getMessage());
		}
		String[] arenalocarr = arenaloc.split(",");
		if (arenalocarr.length == 3)
		{
			int x = Integer.parseInt(arenalocarr[0]);
			int y = Integer.parseInt(arenalocarr[1]);
			int z = Integer.parseInt(arenalocarr[2]);
			blockLoc = new Location(getServer().getWorld("world"),x, y, z);
		}	
	}
	
	public void write(Location l){
		File folder = new File("plugins/DeathPVP");
		if(!folder.exists()){
			folder.mkdir();
		}
		File tmp = new File("plugins/DeathPVP/block.dat");
		if(tmp.exists()){
			tmp.delete();
		}
		 try{
			  FileWriter fstream = new FileWriter("plugins/DeathPVP/block.dat");
			  BufferedWriter out = new BufferedWriter(fstream);
			  out.write(l.getBlockX() + "," + l.getBlockY() + "," + l.getBlockZ());
			  out.close();
			  fstream.close();
		 }catch (Exception e){
			  System.err.println("Error: " + e.getMessage());
		 }
	}
	
	public int playersOnline(){
		int i = 0;
		for(@SuppressWarnings("unused") Player p : getServer().getOnlinePlayers()){
			i++;
		}
		return i;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(!(sender instanceof Player)){
			sender.sendMessage("Players only please!");
			return true;
		}
		Player p = (Player) sender;
		if(cmd.getName().equalsIgnoreCase("pvp")){
			if(args.length > 0){
				if(args[0].equalsIgnoreCase("set")
						&& p.isOp()){
					if(blockLoc != null){
						blockLoc.getBlock().setType(Material.AIR);
					}
					Location loc = p.getLocation();
					int blockX = loc.getBlockX();
					int blockY = loc.getBlockY();
					int blockZ = loc.getBlockZ();
					write(loc);
					p.sendMessage(ChatColor.GOLD + "KOTH block has been set succesfully at: " + blockX
							+ ", " + blockY + ", " + blockZ);
					loc.getBlock().setType(Material.DIAMOND_BLOCK);
					blockLoc = loc;
				}
			}
			/*
			archer:
			pyro:
			boxer:
			*/
		}else if(cmd.getName().equalsIgnoreCase("soldier")){
			p.sendMessage(ChatColor.GREEN + "[DeathPVP] Giving soldier kit.");
			p.getInventory().clear();
			
			for (int i=0;i<p.getInventory().getSize();i++){
				p.getInventory().addItem(new ItemStack(Material.MUSHROOM_SOUP, 1));
			}
			
			p.getInventory().setChestplate(
					new ItemStack(Material.IRON_CHESTPLATE));
			p.getInventory().setLeggings(
					new ItemStack(Material.IRON_LEGGINGS));
			p.getInventory().setHelmet(
					new ItemStack(Material.IRON_HELMET));
			p.getInventory().setBoots(
					new ItemStack(Material.IRON_BOOTS));
			ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
			sword.addEnchantment(Enchantment.getById(16), 1);
			p.getInventory().setItem(0, sword);
		}else if(cmd.getName().equalsIgnoreCase("archer")){
			p.sendMessage(ChatColor.GREEN + "[DeathPVP] Giving archer kit.");
			p.getInventory().clear();
			
			for (int i=0;i<p.getInventory().getSize();i++){
				p.getInventory().addItem(new ItemStack(Material.MUSHROOM_SOUP, 1));
			}
			
			p.getInventory().setChestplate(
					new ItemStack(Material.IRON_CHESTPLATE));
			p.getInventory().setLeggings(
					new ItemStack(Material.IRON_LEGGINGS));
			p.getInventory().setHelmet(
					new ItemStack(Material.IRON_HELMET));
			p.getInventory().setBoots(
					new ItemStack(Material.IRON_BOOTS));
			ItemStack sword = new ItemStack(Material.BOW);
			sword.addEnchantment(Enchantment.getById(49), 1);
			ItemStack arrows = new ItemStack(Material.ARROW, 64);
			p.getInventory().setItem(0, sword);
			p.getInventory().setItem(1, new ItemStack(Material.STONE_SWORD, 1));
			p.getInventory().setItem(2, arrows);
			p.getInventory().setItem(3, arrows);
			p.getInventory().setItem(4, arrows);
			p.getInventory().setItem(5, arrows);
			p.getInventory().setItem(6, arrows);
		}else if(cmd.getName().equalsIgnoreCase("pyro")){
			p.sendMessage(ChatColor.GREEN + "[DeathPVP] Giving pyro kit.");
			p.getInventory().clear();
			
			for (int i=0;i<p.getInventory().getSize();i++){
				p.getInventory().addItem(new ItemStack(Material.MUSHROOM_SOUP, 1));
			}
			
			p.getInventory().setChestplate(
					new ItemStack(Material.CHAINMAIL_CHESTPLATE));
			p.getInventory().setLeggings(
					new ItemStack(Material.CHAINMAIL_LEGGINGS));
			p.getInventory().setHelmet(
					new ItemStack(Material.CHAINMAIL_HELMET));
			p.getInventory().setBoots(
					new ItemStack(Material.CHAINMAIL_BOOTS));
			ItemStack axe = new ItemStack(Material.DIAMOND_AXE);
			fire.add(p.getName());
			p.getInventory().setItem(0, axe);
		}else if(cmd.getName().equalsIgnoreCase("boxer")){
			p.sendMessage(ChatColor.GREEN + "[DeathPVP] Giving boxer kit.");
			p.getInventory().clear();
			
			for (int i=0;i<p.getInventory().getSize();i++){
				p.getInventory().addItem(new ItemStack(Material.MUSHROOM_SOUP, 1));
			}
			
			p.getInventory().setChestplate(
					new ItemStack(Material.GOLD_CHESTPLATE));
			p.getInventory().setLeggings(
					new ItemStack(Material.GOLD_LEGGINGS));
			p.getInventory().setHelmet(
					new ItemStack(Material.GOLD_HELMET));
			p.getInventory().setBoots(
					new ItemStack(Material.GOLD_BOOTS));
			ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
			Map<Enchantment, Integer> ench = new HashMap<Enchantment, Integer>();
			ench.put(Enchantment.getById(16), 1);
			ench.put(Enchantment.getById(19), 1);
			sword.addEnchantments(ench);
			p.getInventory().setItem(0, sword);
		}
		return true;
	}
	
	public kPlayer getPlayer(Player p){
		for(kPlayer kp : players){
			if(kp.getPlayer().getName().equalsIgnoreCase(p.getName())){
				return kp;
			}
		}
		return null;
	}
	
}