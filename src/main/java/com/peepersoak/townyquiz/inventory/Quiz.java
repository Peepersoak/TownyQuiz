package com.peepersoak.townyquiz.inventory;

import com.peepersoak.townyquiz.TownyQuiz;
import com.peepersoak.townyquiz.questionsdata.QuestionsData;
import com.peepersoak.townyquiz.utils.StringPath;
import com.peepersoak.townyquiz.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Quiz {

    public void startQuiz(String category, Player player) {
        QuestionsData data = TownyQuiz.getInstance().getQuestions().getQuestionList().get(category);
        openQuestionGUI(player, data, 0);

        List<String> command = data.getOnStartReward();

        for (String str : command) {
            String[] sp = str.split(":");
            String type = sp[0].trim();
            String cmd = sp[1].trim();

            data.runCommand(type, changePlaceholders(cmd, data.getTitle(), TownyQuiz.getInstance().getScore(player.getUniqueId()), data.getNumberOfQuestions(), player), player);
        }
    }

    public void nextQuestion(Player player) {
        UUID uuid = player.getUniqueId();
        String category = TownyQuiz.getInstance().getCatergory(uuid);
        QuestionsData data = TownyQuiz.getInstance().getQuestions().getQuestionList().get(category);

        int nextQuestion = TownyQuiz.getInstance().getCurrentQuestion(uuid);

        if (nextQuestion >= data.getNumberOfQuestions()) {

            int score = TownyQuiz.getInstance().getScore(uuid);

            List<String> command;

            if (score >= data.getPassingScore() || player.hasPermission("quiz.bypass.score")) {
                command = data.getOnCompleteReward();
                Utils.sendSyncMessage(player, TownyQuiz.getInstance().getMessageData().getConfig().getString(StringPath.MESSAGE_ON_COMPLETE));
            } else {
                command = data.getOnFailReward();
                Utils.sendSyncMessage(player, TownyQuiz.getInstance().getMessageData().getConfig().getString(StringPath.MESSAGE_ON_FAILED));
            }

            for (String str : command) {
                String[] sp = str.split(":");
                String type = sp[0].trim();
                String cmd = sp[1].trim();

                data.runCommand(type, changePlaceholders(cmd, data.getTitle(), score, data.getNumberOfQuestions(), player), player);
            }

            player.closeInventory();
            TownyQuiz.getInstance().removePlayer(uuid);
            return;
        }

        openQuestionGUI(player, data, nextQuestion);
        Utils.sendSyncMessage(player, "&eNext Question... &6or enter &2/quiz cancel &6to cancel and exit the current quiz");
    }

    public void openQuestionGUI(Player player, QuestionsData data, int questionNumber) {
        String question = data.getAllQuestion().get(questionNumber);
        List<String> choices = data.getChoices(question);
        List<String> answer = data.getAnswer(question);

        Collections.shuffle(choices);

        Inventory inv = Utils.createInventory(StringPath.TOWNY_QUIZ_GUI_NAME, 18);

        for (int i = 0; i < 18; i++) {
            switch (i) {
                case 4 -> inv.setItem(i, Utils.createButton("&6Question",convertLongString(question), Material.BOOK, true, false));
                case 10 -> inv.setItem(i, Utils.createButton("&bA.", convertLongString(choices.get(0)), Material.PAPER, false, answer.contains(choices.get(0))));
                case 12 -> inv.setItem(i, Utils.createButton("&bB.", convertLongString(choices.get(1)), Material.PAPER, false, answer.contains(choices.get(1))));
                case 14 -> inv.setItem(i, Utils.createButton("&bC.", convertLongString(choices.get(2)), Material.PAPER, false, answer.contains(choices.get(2))));
                case 16 -> inv.setItem(i, Utils.createButton("&bD.", convertLongString(choices.get(3)), Material.PAPER, false, answer.contains(choices.get(3))));
                default -> inv.setItem(i, Utils.createButton(" ", null, Material.BLACK_STAINED_GLASS_PANE, false, false));
            }
        }

        player.openInventory(inv);
    }

    private String changePlaceholders(String string, String categoryTitle, int score, int numberOfQuestion, Player player) {
        return string.replace("{categoryTitle}", categoryTitle)
                .replace("{score}", score + "")
                .replace("{total}", numberOfQuestion + "")
                .replace("%player%", player.getName())
                .replace("_", " ");
    }

    private List<String> convertLongString(String str) {
        List<String> list = new ArrayList<>();
        String[] sp = str.split(" ");
        int count = 0;
        StringBuilder sb = new StringBuilder();
        for (String s : sp) {
            sb.append(s).append(" ");
            count++;
            if (count > 5) {
                count = 0;
                list.add(sb.toString());
                sb = new StringBuilder();
            }
        }
        list.add(sb.toString());
        return list;
    }
}
