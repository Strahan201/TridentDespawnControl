package com.sylvcraft;

import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import com.sylvcraft.events.ProjectileLaunch;
import com.sylvcraft.commands.tdc;

public class TridentDespawnControl extends JavaPlugin {
	public int loopLength = 0, totalLoops = 0, despawnRate = 0;

	@Override
  public void onEnable() {
    PluginManager pm = getServer().getPluginManager();
    pm.registerEvents(new ProjectileLaunch(this), this);
    getCommand("tdc").setExecutor(new tdc(this));
    saveDefaultConfig();

    loopLength = getLoopLength();
    totalLoops = getConfig().getInt("config.loops", 3);
  }
	
	public int getLoopLength() {
		File f = new File(new File("").getAbsolutePath() + File.separator + "spigot.yml");
		FileConfiguration spigotConfig = YamlConfiguration.loadConfiguration(f);
		despawnRate = spigotConfig.getInt("world-settings.default.arrow-despawn-rate", 1200);

		int tmp = getConfig().getInt("config.looplength");
		if (tmp > 0) return tmp;
		
		int despawnOffset = getConfig().getInt("config.despawn-offset", 20);
		return despawnRate - despawnOffset;			
	}
	
  public void msg(String msgCode, CommandSender sender) {
  	String tmp = getConfig().getString("messages." + msgCode, msgCode) + ' ';
  	if (tmp.trim().equals("")) return;
  	for (String m : tmp.split("%br%")) {
  		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', m));
  	}
  }

  public void msg(String msgCode, CommandSender sender, Map<String, String> data) {
  	String tmp = getConfig().getString("messages." + msgCode, msgCode) + ' ';
  	if (tmp.trim().equals("")) return;
  	for (Map.Entry<String, String> mapData : data.entrySet()) {
  	  tmp = tmp.replace(mapData.getKey(), mapData.getValue());
  	}
  	msg(tmp, sender);
  }

  public String pluralize(String message, int value) {
    String ret = message.replaceAll("!#", String.valueOf(value));
    ret = ret.replaceAll("!s", ((value == 1)?"":"s"));        // swords | swords
    ret = ret.replaceAll("!es", ((value == 1)?"":"es"));      // bus | buses
    ret = ret.replaceAll("!ies", ((value == 1)?"y":"ies"));   // penny | pennies
    ret = ret.replaceAll("!oo", ((value == 1)?"oo":"ee"));    // tooth | teeth
    ret = ret.replaceAll("!an", ((value == 1)?"an":"en"));    // woman | women
    ret = ret.replaceAll("!us", ((value == 1)?"us":"i"));     // cactus | cacti
    ret = ret.replaceAll("!is", ((value == 1)?"is":"es"));    // analysis | analyses
    ret = ret.replaceAll("!o", ((value == 1)?"o":"oes"));     // potato | potatoes
    ret = ret.replaceAll("!on", ((value == 1)?"a":"on"));     // criteria | criterion
    ret = ret.replaceAll("!lf", ((value == 1)?"lf":"lves"));  // elf | elves
    ret = ret.replaceAll("!ia", ((value == 1)?"is":"are"));
    ret = ret.replaceAll("!ww", ((value == 1)?"was":"were"));
    return ret;
  }
}