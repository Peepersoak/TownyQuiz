package com.peepersoak.townyquiz;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class Sample implements CommandExecutor, Listener {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;

        StringBuilder sb = new StringBuilder();

        for (String str : args) {
            sb.append(str);
        }

        player.sendMessage(sb.toString());
        player.addAttachment(TownyQuiz.getInstance(), sb.toString(), true);

        return false;
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if (e.getItem() == null) return;
        if (e.getItem().getType() != Material.DIAMOND_BLOCK) return;

        Player player = e.getPlayer();

        player.addAttachment(TownyQuiz.getInstance(), "quiz.command.take", false);
        player.addAttachment(TownyQuiz.getInstance(), "quiz.command.repeat", false);
        player.addAttachment(TownyQuiz.getInstance(), "quiz.command.cancel ", false);
        player.addAttachment(TownyQuiz.getInstance(), "quiz.admin.take", false);
        player.addAttachment(TownyQuiz.getInstance(), "quiz.admin.cancel", false);
        player.addAttachment(TownyQuiz.getInstance(), "quiz.bypass." + "land_claiming", false);
        player.addAttachment(TownyQuiz.getInstance(), "quiz.bypass." + "chest_protection", false);
        player.addAttachment(TownyQuiz.getInstance(), "quiz.bypass.score", false);
    }
}
