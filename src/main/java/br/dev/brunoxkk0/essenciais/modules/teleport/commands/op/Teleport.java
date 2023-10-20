package br.dev.brunoxkk0.essenciais.modules.teleport.commands.op;

import br.dev.brunoxkk0.essenciais.Essenciais;
import br.dev.brunoxkk0.essenciais.modules.teleport.TeleportModule;
import br.dev.brunoxkk0.essenciais.modules.teleport.config.TeleportLang;
import br.dev.brunoxkk0.syrxmccore.core.commands.Command;
import br.dev.brunoxkk0.syrxmccore.core.commands.CommandContext;
import br.dev.brunoxkk0.syrxmccore.core.commands.CommandExecutable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

@Command(
        plugin = Essenciais.class,
        command = "teleport",
        aliases = {"tp"},
        permission = "essenciais.command.teleport.teleport",
        usage = "/teleport <player> <player>"
)
public class Teleport implements CommandExecutable {

    private final TeleportLang Lang = TeleportModule.getTeleportLang();

    @Override
    public void execute(CommandContext commandContext) {

        String[] args = commandContext.getArgs();

        if (args == null || args.length == 0) {
            commandContext.getSender().sendMessage(Lang.WRONG_USAGE);
            return;
        }

        if (args.length == 1 && (commandContext.getSender() instanceof Player playerSender)) {

            Player player = Bukkit.getPlayer(args[0]);

            if (player == null) {
                playerSender.sendMessage(Lang.PLAYER_NOT_FOUND);
                return;
            }

            if (playerSender.getUniqueId().equals(player.getUniqueId())) {
                commandContext.getSender().sendMessage(Lang.MATRIX_FAIL_TELEPORT_ITSELF);
                return;
            }

            playerSender.teleport(player, PlayerTeleportEvent.TeleportCause.COMMAND);
            playerSender.sendMessage(Lang.TELEPORTED_SUCCESSFUL);
            return;

        }

        if (args.length == 2) {

            Player from = Bukkit.getPlayer(args[0]);
            Player to = Bukkit.getPlayer(args[1]);

            if (from == null) {
                commandContext.getSender().sendMessage(Lang.PLAYER_NOT_FOUND);
                return;
            }

            if (to == null) {
                commandContext.getSender().sendMessage(Lang.PLAYER_NOT_FOUND);
                return;
            }

            if (from.getUniqueId().equals(to.getUniqueId())) {
                commandContext.getSender().sendMessage(Lang.MATRIX_FAIL_TELEPORT_ITSELF);
                return;
            }

            from.teleport(to, PlayerTeleportEvent.TeleportCause.COMMAND);
            commandContext.getSender().sendMessage(Lang.TELEPORTED_SUCCESSFUL);
            return;

        }

        commandContext.getSender().sendMessage(Lang.SENDER_CANNOT_BE_TELEPORTED);
    }

}