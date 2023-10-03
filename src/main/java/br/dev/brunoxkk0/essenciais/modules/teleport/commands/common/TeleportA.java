package br.dev.brunoxkk0.essenciais.modules.teleport.commands.common;

import br.com.finalcraft.evernifecore.fancytext.FancyText;
import br.com.finalcraft.evernifecore.fancytext.FancyTextManager;
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
        command = "teleporta",
        aliases = {"tpa", "tpi"},
        permission = "essenciais.command.teleport.teleporta",
        usage = "/teleporta <player>"
)
public class TeleportA implements CommandExecutable {

    @Override
    public void execute(CommandContext commandContext) {

        String[] args = commandContext.getArgs();

        if (args == null || args.length == 0) {
            commandContext.getSender().sendMessage(Lang.getString("WRONG_USAGE"));
            return;
        }

        if (args.length == 1 && (commandContext.getSender() instanceof Player playerSender)) {

            Player player = Bukkit.getPlayer(args[0]);

            if (player == null) {
                playerSender.sendMessage(Lang.getString("PLAYER_NOT_FOUND"));
                return;
            }

            if (playerSender.getUniqueId().equals(player.getUniqueId())) {
                commandContext.getSender().sendMessage(Lang.getString("MATRIX_FAIL_TELEPORT_ITSELF"));
                return;
            }

            User playerAsUser = UserModule.getUserDataStore().findByUUID(player.getUniqueId()).orElse(null);

            if (playerAsUser != null) {

                TeleportPrivacy teleportPrivacy = playerAsUser.getOrCreateExtension("teleportPrivacy", TeleportPrivacy.class);

                if (!teleportPrivacy.isEnabledTeleportInvites()) {
                    playerSender.sendMessage(Lang.getString("TELEPORT_INVITE_BLOCKED"));
                    return;
                }

            }

            String KEY = String.format("tpa_%s_%s", commandContext.getSender().getName(), player.getName());

            if (ExpirableManager.exists(KEY)) {
                playerSender.sendMessage(Lang.getString("TELEPORT_INVITE_PENDING"));
                return;
            }

            ExpirableManager.scheduleExpiration(KEY, playerSender, ExpirableManager.Reason.TELEPORT, ((key, context) -> {
                playerSender.sendMessage(Lang.getString("TELEPORT_INVITE_EXPIRED"));
            }), TeleportModule.getTeleportConfig().acceptTime);


            playerSender.sendMessage(Lang.getString("TELEPORT_INVITE_SENT"));

            player.sendMessage(String.format(Lang.getString("TELEPORT_INVITE_RECEIVED"), playerAsUser.getName()));

            FancyTextManager.send(
                    FancyText.of(Lang.getString("TELEPORT_BUTTON_ACCEPT"), Lang.getString("TELEPORT_BUTTON_ACCEPT_HOVER"), "tpaccept " + playerSender.getName()).append(
                            FancyText.of(Lang.getString("TELEPORT_BUTTON_DENY"), Lang.getString("TELEPORT_BUTTON_DENY_HOVER"), "tpadeny " + playerSender.getName())
                    ), player
            );

            return;
        }

        commandContext.getSender().sendMessage(Lang.getString("SENDER_CANNOT_BE_TELEPORTED"));
    }

}