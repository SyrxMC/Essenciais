package br.dev.brunoxkk0.essenciais.modules.teleport;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

public class TeleportManager {

    private static final HashMap<UUID, LinkedList<UUID>> TELEPORT_QUEUE = new HashMap<>();

    public static LinkedList<UUID> getList(UUID uuid){
        if(!TELEPORT_QUEUE.containsKey(uuid))
            TELEPORT_QUEUE.put(uuid, new LinkedList<>());

        return TELEPORT_QUEUE.get(uuid);
    }

    public static Queue<UUID> getQueue(UUID uuid){
        return getList(uuid);
    }

    public static UUID poolFirst(UUID queue){
        return getQueue(queue).poll();
    }

    public static UUID pool(UUID queue, UUID target){
        return getList(queue).remove(target) ? target : null;
    }

    public static boolean isPresent(UUID queue, UUID target){
        return getList(queue).contains(target);
    }

    public static boolean queue(UUID queue, UUID target){
        return getQueue(queue).offer(target);
    }

    public static boolean isEmpty(UUID queue){
        return getList(queue).isEmpty();
    }

    public static boolean registerRequest(Player from, Player to){
        return queue(to.getUniqueId(), from.getUniqueId());
    }

    public static UUID removeRequest(Player from, Player to){
        return pool(to.getUniqueId(), from.getUniqueId());
    }

    public static boolean hasRequest(Player from, Player to){
        return isPresent(to.getUniqueId(), from.getUniqueId());
    }

}
