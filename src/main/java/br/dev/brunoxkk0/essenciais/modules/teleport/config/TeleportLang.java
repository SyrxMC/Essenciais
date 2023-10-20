package br.dev.brunoxkk0.essenciais.modules.teleport.config;

import br.dev.brunoxkk0.syrxmccore.core.config.Comment;
import br.dev.brunoxkk0.syrxmccore.core.config.ConfigFactory;
import br.dev.brunoxkk0.syrxmccore.libs.com.electronwill.nightconfig.core.conversion.Path;

public class TeleportLang extends ConfigFactory.PathConfig {

    @Path("lang.wrong_usage")
    @Comment("Wrong command usage.")
    public String WRONG_USAGE = "uso errado.";

    @Path("lang.player_not_found")
    @Comment("Player not found.")
    public String PLAYER_NOT_FOUND = "player não encontrado.";

    @Path("lang.matrix_fail_teleport_itself")
    @Comment("The player cannot teleport to itself.")
    public String MATRIX_FAIL_TELEPORT_ITSELF = "não pode teleportar a si mesmo.";

    @Path("lang.sender_cannot_be_teleported")
    @Comment("The command sender cannot be teleported.")
    public String SENDER_CANNOT_BE_TELEPORTED = "você não pode ser teleportado.";

    @Path("lang.teleport_invite_accept_time")
    @Comment("It took take long time to confirm the teleport invite.")
    public String TOOK_MUCH_TIME_TO_CONFIRM = "demorou muito para confirmar.";

    @Path("lang.need_confirmation_to_execute")
    @Comment("The command needs confirmation to be executed.")
    public String NEED_CONFIRMATION_TO_EXECUTE = "execute o comando novamente para confirmar a ação.";

    @Path("lang.teleport_invite_empty")
    @Comment("The teleport queue for this player, is empty.")
    public String TELEPORT_INVITE_EMPTY = "Você não possui nenhuma solicitação de teleporte.";

    @Path("lang.teleport_invite_expired")
    @Comment("The teleport invite was not accepted and has expired.")
    public String TELEPORT_INVITE_EXPIRED = "Pedido de teleporte expirou.";

    @Path("lang.teleport_invite_pending")
    @Comment("The teleport request is pending.")
    public String TELEPORT_INVITE_PENDING = "Pedido de teleporte pendente.";

    @Path("lang.teleport_invite_sent")
    @Comment("The teleport request was sent.")
    public String TELEPORT_INVITE_SENT = "Pedido de teleporte enviado.";

    @Path("lang.teleport_invite_accept")
    @Comment("The teleport request was accepted.")
    public String TELEPORT_INVITE_ACCEPTED = "Pedido de teleporte aceito.";

    @Path("lang.teleport_invite_received")
    @Comment("You have received a teleport request.")
    public String TELEPORT_INVITE_RECEIVED = "O jogador %s te enviou um pedido de teleporte.";

    @Path("lang.teleport_invite_blocked")
    @Comment("Time required to expire a teleport invite.")
    public String TELEPORT_INVITE_BLOCKED = "Não é possível enviar pedidos de teleporte para este jogador.";

    @Path("lang.teleport_invite_not_exist")
    @Comment("Teleport invite for some player not exists")
    public String TELEPORT_INVITE_NOT_EXIST = "Não é existe nenhum pedido de teleporte do jogador %s.";

    @Path("lang.teleport_warm_up")
    @Comment("Wait some time to be teleported.")
    public String TELEPORT_WARM_UP = "Não se mova, você será teleportado em %s.";

    @Path("lang.teleported_successful")
    @Comment("Successfully teleported.")
    public String TELEPORTED_SUCCESSFUL = "teleportado com sucesso.";

    @Path("lang.teleport_button_accept")
    @Comment("Teleport request accept button.")
    public String TELEPORT_BUTTON_ACCEPT = "[Aceitar]";

    @Path("lang.teleport_button_accept_hover")
    @Comment("Hover text for teleport request accept button.")
    public String TELEPORT_BUTTON_ACCEPT_HOVER = "Aceite o teleporte";

    @Path("lang.teleport_button_deny")
    @Comment("Teleport request deny button.")
    public String TELEPORT_BUTTON_DENY = "[Negar]";

    @Path("lang.teleport_button_deny_hover")
    @Comment("Hover text for teleport request deny button.")
    public String TELEPORT_BUTTON_DENY_HOVER = "Recuse o pedido de teleporte";

    @Override
    public long getFileVersion() {
        return 2;
    }
}
