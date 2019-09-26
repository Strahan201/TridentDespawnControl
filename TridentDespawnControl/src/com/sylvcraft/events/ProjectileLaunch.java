package com.sylvcraft.events;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import com.sylvcraft.TridentDespawnControl;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ProjectileLaunch implements Listener {
  TridentDespawnControl plugin;
  
  public ProjectileLaunch(TridentDespawnControl instance) {
    plugin = instance;
  }

	@EventHandler
  public void onProjectileLaunch(ProjectileLaunchEvent e) {
		if (e.getEntityType() != EntityType.TRIDENT) return;

		Trident t = (Trident)e.getEntity();
		Player p = (t.getShooter() instanceof Player)?(Player)t.getShooter():null;

		if (plugin.getConfig().getBoolean("config.requirepermission") && p != null && !p.hasPermission("tdc.enabled")) return;
		
		new BukkitRunnable() {
			int loopCount = 0;
			
			@Override
			public void run() {
				loopCount++;
				if (loopCount >= plugin.totalLoops) this.cancel();
				((Trident)e.getEntity()).setTicksLived(1);
			}
		}.runTaskTimer(plugin, 0, plugin.loopLength);
  }
}