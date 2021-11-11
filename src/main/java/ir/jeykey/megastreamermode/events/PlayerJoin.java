package ir.jeykey.megastreamermode.events;

import ir.jeykey.megastreamermode.MegaStreamermode;
import ir.jeykey.megastreamermode.database.models.StreamerMode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {
        @EventHandler
        protected void onPlayerJoin(PlayerJoinEvent e) {
                Player p = e.getPlayer();

                Bukkit.getScheduler().runTaskAsynchronously(MegaStreamermode.getInstance(), () -> {
                        if (StreamerMode.exists(p)) {
                                StreamerMode.apply(p, StreamerMode.fetchNickName(p));
                        }
                });
        }
}
