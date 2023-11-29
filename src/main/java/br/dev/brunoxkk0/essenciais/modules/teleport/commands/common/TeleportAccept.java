package br.dev.brunoxkk0.essenciais.modules.teleport.commands.common;

import br.dev.brunoxkk0.essenciais.Essenciais;
import br.dev.brunoxkk0.essenciais.core.data.manager.ExpirableManager;
import br.dev.brunoxkk0.essenciais.modules.teleport.TeleportManager;
import br.dev.brunoxkk0.essenciais.modules.teleport.TeleportModule;
import br.dev.brunoxkk0.essenciais.modules.teleport.config.TeleportLang;
import br.dev.brunoxkk0.syrxmccore.core.commands.Command;
import br.dev.brunoxkk0.syrxmccore.core.commands.CommandContext;
import br.dev.brunoxkk0.syrxmccore.core.commands.CommandExecutable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@Command(
        plugin = Essenciais.class,
        command = "tpaccept",
        aliases = {"teleportaccept"},
        permission = "essenciais.command.teleport.teleporta",
        usage = "/tpaccept <tpa_requests>",
        playerOnly = true
)
public class TeleportAccept implements CommandExecutable {

    private final TeleportLang Lang = TeleportModule.getTeleportLang();

    @Override
    public void execute(CommandContext commandContext) {

        Player sender = (Player) commandContext.getSender();

        if(TeleportManager.isEmpty(sender.getUniqueId())){
            sender.sendMessage(Lang.TELEPORT_INVITE_EMPTY);
            return;
        }

        String[] args = commandContext.getArgs();
        Player player = null;

        if (args != null && args.length >= 1) {

            player = Bukkit.getPlayer(args[0]);

            if (player == null) {
                sender.sendMessage(Lang.PLAYER_NOT_FOUND);
                return;
            }

        }

        if (player != null){

            if (!TeleportManager.hasRequest(player, sender)) {
                sender.sendMessage(String.format(Lang.TELEPORT_INVITE_NOT_EXIST, player.getName()));
                return;
            }

            doTeleport(player, sender);

            return;
        }

        UUID first = TeleportManager.poolFirst(sender.getUniqueId());

        if(first != null){

            player = Bukkit.getPlayer(first);

            if (player == null) {
                sender.sendMessage(Lang.PLAYER_NOT_FOUND);
                return;
            }

            doTeleport(player, sender);

        }

    }

    private void doTeleport(Player player, Player sender) {

        String KEY = String.format("tpa_%s>%s", player.getName(), sender.getName());

        ExpirableManager.cancelExpiration(KEY);
        TeleportManager.removeRequest(player, sender);

        long time = 0;

        if(!player.hasPermission("teleport.warm_up.bypass"))
            time = TeleportModule.getTeleportConfig().warmUpTime;

        if(time > 0){
            player.sendMessage(String.format(Lang.TELEPORT_WARM_UP, " !some time! "));
        }

        sender.sendMessage(Lang.TELEPORT_INVITE_ACCEPTED);

        //TODO: on move event, check if have any of this type, if true, remove and notify
        ExpirableManager.scheduleExpiration(KEY, player, ExpirableManager.Reason.TELEPORT_ACCEPTED, (String key, Object ... context) -> {
            player.sendMessage(Lang.TELEPORTED_SUCCESSFUL);
            player.teleport(sender);
        }, time);

    }

}