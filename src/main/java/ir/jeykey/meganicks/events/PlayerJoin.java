package ir.jeykey.meganicks.events;

import ir.jeykey.meganicks.MegaNicks;
import ir.jeykey.meganicks.database.models.Nick;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {
        @EventHandler
        protected void onPlayerJoin(PlayerJoinEvent e) {
                Player p = e.getPlayer();

                Bukkit.getScheduler().runTaskAsynchronously(MegaNicks.getInstance(), () -> {
                        if (Nick.exists(p)) {
                                Nick.apply(p, Nick.fetchNickName(p));
                        }
                });
        }
}
