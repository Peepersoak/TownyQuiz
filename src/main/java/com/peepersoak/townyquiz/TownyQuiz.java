package com.peepersoak.townyquiz;

import com.peepersoak.townyquiz.commands.QuizCommand;
import com.peepersoak.townyquiz.commands.QuizCompleter;
import com.peepersoak.townyquiz.inventory.Quiz;
import com.peepersoak.townyquiz.questionsdata.Questions;
import com.peepersoak.townyquiz.utils.Data;
import com.peepersoak.townyquiz.utils.EventListener;
import com.peepersoak.townyquiz.utils.Reminder;
import com.peepersoak.townyquiz.utils.StringPath;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class TownyQuiz extends JavaPlugin {

    private final HashMap<UUID, String> playerCurrentCategory = new HashMap<>();
    private final HashMap<UUID, Integer> playerCurrentQuestion = new HashMap<>();
    private final HashMap<UUID, Integer> playerQuizScore = new HashMap<>();
    private final HashMap<UUID, Boolean> isjavaPlayer = new HashMap<>();

    private final Quiz quiz = new Quiz();
    private final Reminder reminder = new Reminder();

    private static TownyQuiz instance;
    private Data questionData;
    private Data messageData;
    private Questions questions;

    @Override
    public void onEnable() {
        instance = this;
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        reloadDataYML();
        questions = new Questions(questionData);

        Bukkit.getPluginManager().registerEvents(new EventListener(), instance);

        Objects.requireNonNull(getCommand("quiz")).setExecutor(new QuizCommand());
        Objects.requireNonNull(getCommand("quiz")).setTabCompleter(new QuizCompleter());

        if (getConfig().getBoolean(StringPath.CONFIG_SHOULD_REMIND)) {
            int minute = getConfig().getInt(StringPath.CONFIG_REMIND_INTERVAL);
            int interval = 20 * 60 * minute;
            reminder.runTaskTimer(this, 0, interval);
        }
    }

    public static TownyQuiz getInstance() {
        return instance;
    }

    public Questions getQuestions() {
        return questions;
    }

    public Data getMessageData() {
        return messageData;
    }

    public void reloadDataYML() {
        questionData = new Data(StringPath.QUESTION_YML);
        messageData = new Data(StringPath.MESSAGE_DATA_YML);
    }

    public void addReplacePlayerCategory(UUID uuid, String category) {
        playerCurrentCategory.put(uuid, category);
    }

    public void removePlayer(UUID uuid) {
        playerCurrentCategory.remove(uuid);
        playerQuizScore.remove(uuid);
        playerCurrentQuestion.remove(uuid);
        isjavaPlayer.remove(uuid);
    }

    public void addScore(UUID uuid) {
        int score = 0;
        if (playerQuizScore.containsKey(uuid)) {
            score = playerQuizScore.get(uuid);
        }
        score++;
        playerQuizScore.put(uuid, score);
    }

    public void addPlayerToScoreData(UUID uuid) {
        playerQuizScore.put(uuid, 0);
    }

    public int getScore(UUID uuid) {
        return playerQuizScore.get(uuid);
    }

    public String getCatergory(UUID uuid) {
        return playerCurrentCategory.get(uuid);
    }

    public boolean isTakingQuiz(UUID uuid) {
        return playerCurrentCategory.containsKey(uuid);
    }

    public List<UUID> getPlayerTakingQuizUUID () {
        return new ArrayList<>(playerCurrentCategory.keySet());
    }

    public void addQuestionNumber(UUID uuid) {
        playerCurrentQuestion.put(uuid, 0);
    }

    public void nextQuestion(UUID uuid) {
        int count = 0;
        if (playerCurrentQuestion.containsKey(uuid)) {
            count = playerCurrentQuestion.get(uuid);
        }
        count++;
        playerCurrentQuestion.put(uuid, count);
    }

    public int getCurrentQuestion(UUID uuid) {
        return playerCurrentQuestion.get(uuid);
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public HashMap<UUID, Boolean> getIsjavaPlayer() {
        return isjavaPlayer;
    }
}
