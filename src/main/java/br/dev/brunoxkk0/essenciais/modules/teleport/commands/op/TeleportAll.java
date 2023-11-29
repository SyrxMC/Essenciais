package br.dev.brunoxkk0.essenciais.modules.teleport.commands.op;

import br.dev.brunoxkk0.essenciais.Essenciais;
import br.dev.brunoxkk0.essenciais.core.data.manager.ExpirableManager;
import br.dev.brunoxkk0.essenciais.modules.teleport.TeleportModule;
import br.dev.brunoxkk0.essenciais.modules.teleport.config.TeleportLang;
import br.dev.brunoxkk0.syrxmccore.core.commands.Command;
import br.dev.brunoxkk0.syrxmccore.core.commands.CommandContext;
import br.dev.brunoxkk0.syrxmccore.core.commands.CommandExecutable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import static br.dev.brunoxkk0.essenciais.core.data.manager.ExpirableManager.Reason.TELEPORT_ALL;


@Command(
        plugin = Essenciais.class,
        command = "tpall",
        aliases = {"teleportall"},
        playerOnly = true,
        permission = "essenciais.command.teleport.tpall",
        usage = "/tpall"
)
public class TeleportAll implements CommandExecutable {

    private final TeleportLang Lang = TeleportModule.getTeleportLang();

    @Override
    public void execute(CommandContext commandContext) {

        if (commandContext.getSender() instanceof Player player) {

            if (ExpirableManager.exists(player, TELEPORT_ALL)) {
                Bukkit.getOnlinePlayers().forEach(pl -> pl.teleport(player, PlayerTeleportEvent.TeleportCause.COMMAND));
                player.sendMessage(Lang.TELEPORTED_SUCCESSFUL);
                ExpirableManager.cancelExpirationEvery(player, TELEPORT_ALL);
                return;
            }

            ExpirableManager.scheduleExpiration("INVOKE_ALL_" + player.getName(), player, TELEPORT_ALL, (key, context) -> {
                commandContext.getSender().sendMessage(Lang.TOOK_MUCH_TIME_TO_CONFIRM);
            }, 5000);

            player.sendMessage(Lang.NEED_CONFIRMATION_TO_EXECUTE);
            return;
        }

        commandContext.getSender().sendMessage(Lang.SENDER_CANNOT_BE_TELEPORTED);
    }

}