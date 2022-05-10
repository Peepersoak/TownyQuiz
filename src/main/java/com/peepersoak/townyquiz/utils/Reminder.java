package com.peepersoak.townyquiz.utils;

import com.peepersoak.townyquiz.TownyQuiz;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.UUID;

public class Reminder extends BukkitRunnable {


    @Override
    public void run() {
        List<UUID> uuids = TownyQuiz.getInstance().getPlayerTakingQuizUUID();
        if (uuids.isEmpty()) return;
        for (UUID uuid : uuids) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null || !player.isOnline()) continue;
            Utils.sendSyncMessage(player, TownyQuiz.getInstance().getMessageData().getConfig().getString(StringPath.MESSAGE_REMINDER));
        }
    }
}
