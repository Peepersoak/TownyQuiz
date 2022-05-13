package com.peepersoak.townyquiz.commands;

import com.peepersoak.townyquiz.TownyQuiz;
import com.peepersoak.townyquiz.inventory.QuizCategory;
import com.peepersoak.townyquiz.questionsdata.QuestionsData;
import com.peepersoak.townyquiz.utils.StringPath;
import com.peepersoak.townyquiz.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class QuizCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player player) {
            if (args.length == 3) {
                String cmd = args[0];
                String category = args[1];
                String targetName = args[2];
                Player target = Bukkit.getPlayer(targetName);
                if (target == null || !target.isOnline()) {
                    Utils.sendSyncMessage(player, "&cPlayer was not FOUND!");
                    return false;
                }
                if (cmd.equalsIgnoreCase("take")) {
                    if (player.hasPermission("quiz.admin.take")) {
                        takeQuiz(target, category, true);
                    } else {
                        Utils.sendSyncMessage(player, TownyQuiz.getInstance().getMessageData().getConfig().getString(StringPath.MESSAGE_NO_PERMISSION_COMMAND));
                    }
                }
            }
            else if (args.length == 2) {
                String cmd = args[0];

                if (cmd.equalsIgnoreCase("take")) {
                    String category = args[1];

                    if (player.hasPermission("quiz.command.take") || player.hasPermission("quiz.admin.take")) {
                        takeQuiz(player, category, false);
                    } else {
                        Utils.sendSyncMessage(player, TownyQuiz.getInstance().getMessageData().getConfig().getString(StringPath.MESSAGE_NO_PERMISSION_COMMAND));
                    }
                }

                else if (cmd.equalsIgnoreCase("cancel")) {
                    String targetName = args[1];
                    Player target = Bukkit.getPlayer(targetName);
                    if (target == null || !target.isOnline()) {
                        Utils.sendSyncMessage(player, "&cPlayer was not FOUND!");
                        return false;
                    }
                    target.closeInventory();
                    TownyQuiz.getInstance().removePlayer(target.getUniqueId());
                }
            }
            else if (args.length == 1) {
                String cmd = args[0];
                if (cmd.equalsIgnoreCase("reload")) {
                    if (player.isOp()) {
                        TownyQuiz.getInstance().reloadDataYML();
                        Utils.sendSyncMessage(player, "&6YML Data has been reloaded");
                    }
                }
                else if (cmd.equalsIgnoreCase("cancel")) {
                    if (player.hasPermission("quiz.command.cancel") || player.hasPermission("quiz.admin.cancel")) {
                        if (TownyQuiz.getInstance().isTakingQuiz(player.getUniqueId())) {
                            player.closeInventory();
                            TownyQuiz.getInstance().removePlayer(player.getUniqueId());
                            Utils.sendSyncMessage(player, TownyQuiz.getInstance().getMessageData().getConfig().getString(StringPath.MESSAGE_CANCEL_QUIZ));
                        } else {
                            Utils.sendSyncMessage(player, TownyQuiz.getInstance().getMessageData().getConfig().getString(StringPath.MESSAGE_NO_QUIZ));
                        }
                    } else {
                        Utils.sendSyncMessage(player, TownyQuiz.getInstance().getMessageData().getConfig().getString(StringPath.MESSAGE_NO_PERMISSION_COMMAND));
                    }
                }
                else if (cmd.equalsIgnoreCase("repeat")) {
                    if (player.hasPermission("quiz.command.repeat")) {
                        UUID uuid = player.getUniqueId();
                        if (TownyQuiz.getInstance().isTakingQuiz(uuid)) {
                            String category = TownyQuiz.getInstance().getCatergory(uuid);
                            QuestionsData data = TownyQuiz.getInstance().getQuestions().getQuestionList().get(category);
                            int questionNumber = TownyQuiz.getInstance().getCurrentQuestion(uuid);
                            TownyQuiz.getInstance().getQuiz().openQuestionGUI(player, data, questionNumber);
                        } else {
                            Utils.sendSyncMessage(player, TownyQuiz.getInstance().getMessageData().getConfig().getString(StringPath.MESSAGE_NO_QUIZ));
                        }
                    } else {
                        Utils.sendSyncMessage(player, TownyQuiz.getInstance().getMessageData().getConfig().getString(StringPath.MESSAGE_NO_PERMISSION_COMMAND));
                    }
                }
                else if (cmd.equalsIgnoreCase("take")) {
                    Utils.sendSyncMessage(player, "&cPlase enter the category");
                }
            }
            else if (args.length == 0) {
                QuizCategory category = new QuizCategory();
                category.openCategory(player);
            }
        } else {
            // Will run by console
            if (args.length == 3) {
                String cmd = args[0];
                String category = args[1];
                String playerName = args[2];

                Player player = Bukkit.getPlayer(playerName);
                if (player == null || !player.isOnline()) {
                    TownyQuiz.getInstance().getLogger().warning("Player was not FOUND!");
                    return false;
                }

                if (cmd.equalsIgnoreCase("take")) {
                    Utils.startQuiz(player, category);
                }
            }
            else if (args.length == 2) {
                String cmd = args[0];
                String playerName = args[1];

                Player player = Bukkit.getPlayer(playerName);
                if (player == null || !player.isOnline()) {
                    TownyQuiz.getInstance().getLogger().warning("Player was not FOUND!");
                    return false;
                }

                if (cmd.equalsIgnoreCase("cancel")) {
                    player.closeInventory();
                    TownyQuiz.getInstance().removePlayer(player.getUniqueId());
                }
            }
        }

        return false;
    }

    private void takeQuiz(Player player, String category, boolean ignorePerms) {
        if (ignorePerms) {
            Utils.startQuiz(player, category);
            return;
        }
        if (TownyQuiz.getInstance().getQuestions().getQuestionList().get(category).checkPermission()) {
            if (!player.hasPermission("quiz.category." + category)) {
                Utils.sendSyncMessage(player, TownyQuiz.getInstance().getMessageData().getConfig().getString(StringPath.MESSAGE_NO_PERMISSION));
                return;
            }
        }
        if (player.hasPermission("quiz.category.complete." + category)) {
            Utils.sendSyncMessage(player, TownyQuiz.getInstance().getMessageData().getConfig().getString(StringPath.MESSAGE_QUIZ_STATUS_COMPLETE));
            return;
        }
        List<String> requiredCategory = TownyQuiz.getInstance().getQuestions().getQuestionList().get(category).getPreRequisiteCategory();
        for (String reqCategory : requiredCategory) {
            if (!player.hasPermission("quiz.category.complete." + reqCategory) && !player.hasPermission("quiz.bypass." + reqCategory) && !player.hasPermission("quiz.bypass.*")) {
                String msg = TownyQuiz.getInstance().getMessageData().getConfig().getString(StringPath.MESSAGE_NO_PRE_REQUISITE_CATEGORY);
                if (msg == null) continue;
                String updated = msg.replace("{requiredCategoryTitle}", reqCategory.replace("_", " "));
                Utils.sendSyncMessage(player, updated);
                return;
            }
        }
        Utils.startQuiz(player, category);
        Utils.sendSyncMessage(player, TownyQuiz.getInstance().getMessageData().getConfig().getString(StringPath.MESSAGE_ON_START));
    }
}
