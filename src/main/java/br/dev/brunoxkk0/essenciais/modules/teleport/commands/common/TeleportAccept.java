package br.dev.brunoxkk0.essenciais.modules.teleport.commands.common;

import br.dev.brunoxkk0.essenciais.Essenciais;
import br.dev.brunoxkk0.essenciais.core.data.ExpirableManager;
import br.dev.brunoxkk0.essenciais.modules.teleport.TeleportModule;
import br.dev.brunoxkk0.essenciais.modules.teleport.data.model.TeleportPrivacy;
import br.dev.brunoxkk0.essenciais.modules.user.UserModule;
import br.dev.brunoxkk0.essenciais.modules.user.data.model.User;
import br.dev.brunoxkk0.syrxmccore.core.commands.Command;
import br.dev.brunoxkk0.syrxmccore.core.commands.CommandContext;
import br.dev.brunoxkk0.syrxmccore.core.commands.CommandExecutable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static br.dev.brunoxkk0.essenciais.modules.teleport.TeleportModule.Lang;

@Command(
        plugin = Essenciais.class,
        command = "tpaccept",
        aliases = {"teleportaccept"},
        permission = "essenciais.command.teleport.teleporta",
        usage = "/tpaccept <player>",
        consoleEnable = false
)
public class TeleportAccept implements CommandExecutable {

    @Override
    public void execute(CommandContext commandContext) {

        String[] args = commandContext.getArgs();
        Player player = null;

        if (args != null && args.length >= 1 && (commandContext.getSender() instanceof Player playerSender)) {

            player = Bukkit.getPlayer(args[0]);

            if (player == null) {
                playerSender.sendMessage(Lang.getString("PLAYER_NOT_FOUND"));
                return;
            }

        }

        if(player != null)

        commandContext.getSender().sendMessage(Lang.getString("SENDER_CANNOT_BE_TELEPORTED"));
    }

}