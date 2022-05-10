package com.peepersoak.townyquiz.commands;

import com.peepersoak.townyquiz.TownyQuiz;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class QuizCompleter implements TabCompleter {

    private final List<String> commands;
    private final List<String> opCommands;
    private final List<String> categories;

    public QuizCompleter() {
        commands = new ArrayList<>();
        commands.add("take");
        commands.add("repeat");
        commands.add("cancel");

        opCommands = new ArrayList<>();
        opCommands.add("take");
        opCommands.add("repeat");
        opCommands.add("cancel");
        opCommands.add("reload");

        categories = new ArrayList<>();
        categories.addAll(TownyQuiz.getInstance().getQuestions().getQuestionList().keySet());
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player player) {
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("take")) {
                    if (player.hasPermission("quiz.command.take") || player.hasPermission("quiz.admin.take")) {
                        return categories;
                    }
                }
            }

            if (args.length == 1) {
                if (player.isOp()) {
                    return opCommands;
                }
                return commands;
            }
        }

        return null;
    }
}
