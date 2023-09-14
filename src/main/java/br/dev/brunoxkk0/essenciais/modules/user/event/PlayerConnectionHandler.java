package br.dev.brunoxkk0.essenciais.modules.user.event;

import br.dev.brunoxkk0.essenciais.modules.user.UserModule;
import br.dev.brunoxkk0.essenciais.modules.user.data.UserDataStore;
import br.dev.brunoxkk0.essenciais.modules.user.data.model.Position;
import br.dev.brunoxkk0.essenciais.modules.user.data.model.User;
import lombok.SneakyThrows;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectionHandler implements Listener {

    private static final UserDataStore userDataStore = UserModule.getUserDataStore();

    @SneakyThrows
    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        User user = userDataStore.findByUUID(event.getPlayer().getUniqueId()).orElse(null);

        if (user == null) {

            user = User
                    .builder()
                    .uuid(player.getUniqueId())
                    .name(player.getName())
                    .displayName(player.getName())
                    .fistSeen(System.currentTimeMillis())
                    .lastSeen(System.currentTimeMillis())
                    .ip(player.getAddress().getAddress().getHostAddress())
                    .build();

            UserModule.getInstance().handleNewPlayer(player);

            userDataStore.registerAndSave(user);

        }

        user.setLastSeen(System.currentTimeMillis());

    }

    @SneakyThrows
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDisconnect(PlayerQuitEvent event){

        Player player = event.getPlayer();

        User user = userDataStore.findByUUID(event.getPlayer().getUniqueId()).orElse(null);

        if(user != null){

            user.setLastSeen(System.currentTimeMillis());

            Position position = user.getOrCreateExtension("lastPosition", Position.class);

            Location location = player.getLocation();

            position.setX(location.getX());
            position.setY(location.getY());
            position.setZ(location.getZ());

            position.setYaw(location.getYaw());
            position.setPitch(location.getPitch());

            position.setWorld(location.getWorld().getUID().toString());

            userDataStore.save(user);

        }
    }

}
