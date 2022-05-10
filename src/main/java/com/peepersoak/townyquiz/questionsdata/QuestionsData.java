package com.peepersoak.townyquiz.questionsdata;

import com.peepersoak.townyquiz.utils.Sender;
import com.peepersoak.townyquiz.TownyQuiz;
import com.peepersoak.townyquiz.utils.Data;
import com.peepersoak.townyquiz.utils.StringPath;
import com.peepersoak.townyquiz.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuestionsData {

    private final String category;
    private final FileConfiguration config;

    public QuestionsData(String category, Data data) {
        this.category = category;
        this.config = data.getConfig();
        init();
    }

    private String title;
    private boolean permission;

    private int passingScore;
    private int numberOfQuestions = 0;

    private List<String> preRequisiteCategory;

    private List<String> onStartReward;
    private List<String> onCompleteReward;
    private List<String> onFailReward;

    private HashMap<String, List<String>> choices;
    private HashMap<String, List<String>> answers;
    private HashMap<String, Integer> maxChoice;

    private List<String> allQuestion;

    private void init() {
        ConfigurationSection sectionCategory = config.getConfigurationSection(category);
        if (sectionCategory == null) {
            TownyQuiz.getInstance().getLogger().warning(category + " was not found, check the spelling and format");
            return;
        }

        title = sectionCategory.getString(StringPath.QUESTION_TITLE);
        permission = sectionCategory.getBoolean(StringPath.QUESTION_PERMISSION);
        passingScore = sectionCategory.getInt(StringPath.QUESTION_PASSING_SCORE);
        preRequisiteCategory = sectionCategory.getStringList(StringPath.QUESTION_PRE_REQUISITE_PERMISSION);

        ConfigurationSection questionSection = sectionCategory.getConfigurationSection(StringPath.QUESTION_QUESTIONS_WRAPPER);

        if (questionSection != null) {
            loadQuestions(questionSection);
        }

        onStartReward = sectionCategory.getStringList(StringPath.QUESTION_REWARD_WRAPPER + "." + StringPath.QUESTION_REWARD_ON_START);
        onCompleteReward = sectionCategory.getStringList(StringPath.QUESTION_REWARD_WRAPPER + "." + StringPath.QUESTION_REWARD_ON_COMPLETE);
        onFailReward = sectionCategory.getStringList(StringPath.QUESTION_REWARD_WRAPPER + "." + StringPath.QUESTION_REWARD_ON_FAILED);
    }

    private void loadQuestions(ConfigurationSection section) {
        choices = new HashMap<>();
        answers = new HashMap<>();
        allQuestion = new ArrayList<>();
        maxChoice = new HashMap<>();

        for (String key : section.getKeys(false)) {
            numberOfQuestions++;

            String question = section.getString(key + "." + StringPath.QUESTION_QUESTION);
            this.allQuestion.add(question);

            List<String> choicesData = section.getStringList(key + "." + StringPath.QUESTION_CHOICES);

            List<String> choices = new ArrayList<>();
            List<String> answers = new ArrayList<>();

            for (String str : choicesData) {
                String[] sp = str.split(";");
                if (sp.length >= 2) {
                    if (sp[1].equalsIgnoreCase("*")) {
                        answers.add(sp[0]);
                        choices.add(sp[0]);
                    }
                } else {
                    choices.add(str);
                }
            }

            int numberOfChoice = answers.size();
            this.maxChoice.put(question, numberOfChoice);

            this.choices.put(question, choices);
            this.answers.put(question, answers);
        }
    }

    public String getTitle() {
        return title;
    }

    public boolean checkPermission() {
        return permission;
    }

    public int getPassingScore() {
        return passingScore;
    }

    public int getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public List<String> getPreRequisiteCategory() {
        return preRequisiteCategory;
    }

    public List<String> getChoices(String question) {
        return choices.get(question);
    }

    public List<String> getAnswer(String question) {
        return answers.get(question);
    }

    public int getMaxChoice(String question) {
        return maxChoice.get(question);
    }

    public List<String> getAllQuestion() {
        return allQuestion;
    }

    public List<String> getOnStartReward() {
        return onStartReward;
    }

    public List<String> getOnCompleteReward() {
        return onCompleteReward;
    }

    public List<String> getOnFailReward() {
        return onFailReward;
    }

    public void runCommand(String sender, String commands, Player player) {
        if (sender.equalsIgnoreCase(Sender.CONSOLE)) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commands);
        }
        else if (sender.equalsIgnoreCase(Sender.PLAYER)) {
            Bukkit.dispatchCommand(player, commands);
        }
        else if (sender.equalsIgnoreCase(Sender.MESSAGE)) {
            Utils.sendSyncMessage(player, commands);
        }
    }
}
