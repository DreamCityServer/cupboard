package com.mics.spigotPlugin.cupboard;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Data {
	private YamlConfiguration cupboards;
	private File cupboardsFile;
	private YamlConfiguration limitblocks;
	private File limitblocksFile;
	static private int ZONE_SIZE = 7;
	static private int BUTTON_SIZE = 2;
	static private int TOP_SIZE = 12;

    Data(File dataFolder){

        cupboardsFile = new File(dataFolder, "cupboards.yml");
        this.cupboards = YamlConfiguration.loadConfiguration(cupboardsFile);
        
        limitblocksFile = new File(dataFolder, "limitblocks.yml");
        this.limitblocks = YamlConfiguration.loadConfiguration(limitblocksFile);
    }
    
    public boolean putCupboard(Block b, Player p){

    	Location l = b.getLocation();
    	String loc_str = Util.LocToString(l);
    	if(limitblocks.getString(loc_str) != null)return false;
    	for(int x = -ZONE_SIZE; x <= ZONE_SIZE; x++)
    		for(int y = -BUTTON_SIZE; y <= TOP_SIZE; y++)
    			for(int z = -ZONE_SIZE; z <= ZONE_SIZE; z++){
    				String nloc_str = Util.LocToString(l.clone().add(x,y,z));
    				
    				@SuppressWarnings("unchecked")
					List<String> nloc_list = (List<String>)limitblocks.getList(nloc_str);
    				if(nloc_list==null)nloc_list = new ArrayList<String>();
    				
    				nloc_list.add(loc_str);
    				limitblocks.set(nloc_str, nloc_list);
    			}
    	List<String> uuid_list = new ArrayList<String>();
    	cupboards.set(loc_str, uuid_list);
    	this.save();
    	return true;
    }
    public void removeCupboard(Block b){
    	Location l = b.getLocation();
    	String loc_str = Util.LocToString(l);
    	for(int x = -ZONE_SIZE; x <= ZONE_SIZE; x++)
    		for(int y = -BUTTON_SIZE; y <= TOP_SIZE; y++)
    			for(int z = -ZONE_SIZE; z <= ZONE_SIZE; z++){
					String nloc_str = Util.LocToString(l.clone().add(x,y,z));
    				
    				@SuppressWarnings("unchecked")
					List<String> nloc_list = (List<String>)limitblocks.getList(nloc_str);
    				if(nloc_list==null)nloc_list = new ArrayList<String>();
    				
    				nloc_list.remove(loc_str);
    				if(nloc_list.isEmpty()){
    					limitblocks.set(nloc_str, null);
    					continue;
    				}
    				limitblocks.set(nloc_str, nloc_list);
    			}
    	cupboards.set(loc_str, null);
    	this.save();
    }
    
    @SuppressWarnings("unchecked")
	public boolean toggleBoardAccess(Player p, Block b){
    	String loc_str = Util.LocToString(b.getLocation());
    	String uuid_str = p.getUniqueId().toString();
    	List<String> uuid_list;
    	boolean rtn;
    	uuid_list = (List<String>) cupboards.getList(loc_str);
    	if(uuid_list == null)uuid_list = new ArrayList<String>();
    	
    	if(uuid_list.contains(uuid_str)){
    		uuid_list.remove(uuid_str);
    		rtn = false;
    	} else {
    		uuid_list.add(uuid_str);
    		rtn = true;
    	}
    	cupboards.set(loc_str, uuid_list);
    	this.save();
    	return rtn;
	}
    
    private void save(){
    	try {
			cupboards.save(cupboardsFile);
			limitblocks.save(limitblocksFile);
		} catch (IOException e) {
			//should not happen
			e.printStackTrace();
		}
    }

	public boolean checkIsLimit(Block b) {
		String cupboard_str = limitblocks.getString(Util.LocToString(b.getLocation()));
		if(cupboard_str != null)return true;
		return false;
	}

	@SuppressWarnings("unchecked")
	public boolean checkIsLimit(Block b, Player p) {
		boolean flag = false;
		
		List<String> cups_str = (List<String>)limitblocks.getList(Util.LocToString(b.getLocation()));
		if(cups_str == null) return false;
		if(cups_str.isEmpty()) return false;
		
		for( String str : cups_str ){
			StringTokenizer st = new StringTokenizer(str,",");
			World world = Bukkit.getWorld((st.nextToken()));
			double x = Double.parseDouble(st.nextToken());
			double y = Double.parseDouble(st.nextToken());
			double z = Double.parseDouble(st.nextToken());
			Location cup_l = new Location(world,x,y,z);
			List<String> accessAbleUsers = (List<String>) cupboards.getList(Util.LocToString(cup_l));
			
			if(!accessAbleUsers.contains(p.getUniqueId().toString())) {
				flag = true;
				break; //假如有找不到的 直接中斷跳出
			}
		}
		return flag;
	}
}
