package com.sylvcraft.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import com.sylvcraft.TridentDespawnControl;

public class tdc implements TabExecutor {
  TridentDespawnControl plugin;
  
  public tdc(TridentDespawnControl instance) {
    plugin = instance;
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
  	List<String> tabs = new ArrayList<String>();
  	if (args.length == 1) {
  		tabs.add("loops");
  		tabs.add("perm");
  		tabs.add("status");
  		tabs.add("reload");
  	}
  	return tabs;
  }
  
	@Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    try {
    	if (!sender.hasPermission("tdc.admin")) {
    		plugin.msg("access-denied", sender);
    		return true;
    	}
    	
      if (args.length == 0) {
        showHelp(sender);
        return true;
      }

      Map<String, String> data = new HashMap<String, String>();
      switch (args[0].toLowerCase()) {
      case "reload":
      	plugin.reloadConfig();
      	plugin.msg("reloaded", sender);
      	return true;
      	
      case "status":
      	int tridentDuration = Math.round(((plugin.totalLoops == 0)?plugin.despawnRate:((plugin.totalLoops+1) * plugin.loopLength)) / 20);
      	data.put("%loops%", plugin.pluralize("!# loop!s (", plugin.totalLoops) + plugin.pluralize("!# sec!s)", tridentDuration));
      	data.put("%looplength%", plugin.pluralize("!# tick!s (", plugin.loopLength) + plugin.pluralize("!# sec!s)", Math.round(plugin.loopLength / 20)));
      	data.put("%despawnrate%", plugin.pluralize("!# tick!s (", plugin.despawnRate) + plugin.pluralize("!# sec!s)", Math.round(plugin.despawnRate / 20)));
      	plugin.msg("status", sender, data);
        break;
      
      case "loops":
      	try {
      		int loops = Integer.parseInt(args[1]);
      		if (loops < 1) {
        		plugin.msg("invalid-value", sender);
      			return true;
      		}
      		plugin.getConfig().set("config.loops", loops);
      		data.put("%loops%", String.valueOf(loops));
      		plugin.totalLoops = loops;
      		plugin.getConfig().set("loops", plugin.totalLoops);
      		plugin.saveConfig();
      		plugin.msg("loops-set", sender, data);
      	} catch (NumberFormatException ex) {
      		plugin.msg("invalid-value", sender);
      	}
      	break;
      	
      case "perm":
      	plugin.getConfig().set("config.requirepermission", !plugin.getConfig().getBoolean("config.requirepermission"));
      	plugin.saveConfig();
      	
      	data.put("%status%", plugin.getConfig().getBoolean("config.requirepermission")?"true":"false");
      	plugin.msg("perm-set", sender, data);
      	break;
      }

      return true;
    } catch (Exception ex) {
      return false;
    }
  }

	void showHelp(CommandSender sender) {
		if (sender.hasPermission("tdc.admin")) { 
			plugin.msg("help-status", sender);
			plugin.msg("help-loops", sender);
			plugin.msg("help-perm", sender);
		}
  }
}
