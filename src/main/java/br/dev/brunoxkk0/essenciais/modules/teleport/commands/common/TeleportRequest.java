package br.dev.brunoxkk0.essenciais.modules.teleport.commands.common;

import br.com.finalcraft.evernifecore.fancytext.FancyText;
import br.com.finalcraft.evernifecore.fancytext.FancyTextManager;
import br.dev.brunoxkk0.essenciais.Essenciais;
import br.dev.brunoxkk0.essenciais.core.data.manager.ExpirableManager;
import br.dev.brunoxkk0.essenciais.modules.teleport.TeleportManager;
import br.dev.brunoxkk0.essenciais.modules.teleport.TeleportModule;
import br.dev.brunoxkk0.essenciais.modules.teleport.config.TeleportLang;
import br.dev.brunoxkk0.essenciais.modules.teleport.data.model.TeleportPrivacy;
import br.dev.brunoxkk0.essenciais.modules.user.UserModule;
import br.dev.brunoxkk0.essenciais.modules.user.data.model.User;
import br.dev.brunoxkk0.syrxmccore.core.commands.Command;
import br.dev.brunoxkk0.syrxmccore.core.commands.CommandContext;
import br.dev.brunoxkk0.syrxmccore.core.commands.CommandExecutable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Command(
        plugin = Essenciais.class,
        command = "teleporta",
        aliases = {"tpa", "tpi"},
        permission = "essenciais.command.teleport.tpa",
        usage = "/teleporta <player>",
        playerOnly = true
)
public class TeleportRequest implements CommandExecutable {

    private final TeleportLang Lang = TeleportModule.getTeleportLang();

    @Override
    public void execute(CommandContext commandContext) {

        String[] args = commandContext.getArgs();

        Player sender = (Player) commandContext.getSender();

        if (args == null || args.length == 0) {
            commandContext.getSender().sendMessage(Lang.WRONG_USAGE);
            return;
        }

        Player player = Bukkit.getPlayer(args[0]);

        if (player == null) {
            sender.sendMessage(Lang.PLAYER_NOT_FOUND);
            return;
        }

        if (sender.getUniqueId().equals(player.getUniqueId())) {
            commandContext.getSender().sendMessage(Lang.MATRIX_FAIL_TELEPORT_ITSELF);
            return;
        }

        User playerAsUser = UserModule.getUserDataStore().findByUUID(player.getUniqueId()).orElse(null);

        if (playerAsUser != null) {

            TeleportPrivacy teleportPrivacy = playerAsUser.getOrCreateExtension("teleportPrivacy", TeleportPrivacy.class);

            if (!teleportPrivacy.isEnabledTeleportInvites()) {
                sender.sendMessage(Lang.TELEPORT_INVITE_BLOCKED);
                return;
            }

        }

        String KEY = String.format("tpa_%s>%s", player.getName(), sender.getName());

        if (TeleportManager.hasRequest(sender, player)) {
            sender.sendMessage(Lang.TELEPORT_INVITE_PENDING);
            return;
        }

        registerRequest(sender, player, KEY);
        onTeleportRequest(player, sender);

    }

    private void registerRequest(Player sender, Player player, String KEY) {

        TeleportManager.registerRequest(sender, player);

        ExpirableManager.scheduleExpiration(KEY, sender, ExpirableManager.Reason.TELEPORT, ((key, context) -> {
            sender.sendMessage(Lang.TELEPORT_INVITE_EXPIRED);
            TeleportManager.removeRequest(sender, player);
        }), TeleportModule.getTeleportConfig().acceptTime);

        sender.sendMessage(Lang.TELEPORT_INVITE_SENT);

    }

    private void onTeleportRequest(Player player, Player sender) {

        player.sendMessage(String.format(Lang.TELEPORT_INVITE_RECEIVED, player.getName()));

        FancyTextManager.send(
                FancyText.of(Lang.TELEPORT_BUTTON_ACCEPT, Lang.TELEPORT_BUTTON_ACCEPT_HOVER, "/tpaccept " + sender.getName()).append(
                        FancyText.of(Lang.TELEPORT_BUTTON_DENY, Lang.TELEPORT_BUTTON_DENY_HOVER, "/tpadeny " + sender.getName())
                ), player
        );

    }

}