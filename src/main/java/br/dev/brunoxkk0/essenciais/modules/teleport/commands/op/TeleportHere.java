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
        command = "tphere",
        aliases = {"tph"},
        playerOnly = true,
        permission = "essenciais.command.teleport.tphere",
        usage = "/tphere <player>"
)
public class TeleportHere implements CommandExecutable {

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

            player.teleport(playerSender, PlayerTeleportEvent.TeleportCause.COMMAND);
            playerSender.sendMessage(Lang.TELEPORTED_SUCCESSFUL);
            return;

        }

        commandContext.getSender().sendMessage(Lang.SENDER_CANNOT_BE_TELEPORTED);
    }

}