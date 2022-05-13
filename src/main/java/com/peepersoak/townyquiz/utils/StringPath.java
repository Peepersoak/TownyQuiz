package com.peepersoak.townyquiz.utils;

import com.peepersoak.townyquiz.TownyQuiz;
import org.bukkit.NamespacedKey;

public class StringPath {

    public static String QUESTION_YML = "Questions.yml";
    public static String MESSAGE_DATA_YML = "Message.yml";

    public static String QUESTION_TITLE = "TitleName";
    public static String QUESTION_PERMISSION = "Permission";
    public static String QUESTION_PASSING_SCORE = "MinimumScore";
    public static String QUESTION_PRE_REQUISITE_PERMISSION = "RequiredCategory";
    public static String QUESTION_QUESTIONS_WRAPPER = "Questions";
    public static String QUESTION_QUESTION = "Question";
    public static String QUESTION_CHOICES = "Choices";
    public static String QUESTION_REWARD_WRAPPER = "Rewards";
    public static String QUESTION_REWARD_ON_START = "OnStart";
    public static String QUESTION_REWARD_ON_COMPLETE = "OnCompleted";
    public static String QUESTION_REWARD_ON_FAILED = "OnFailed";

    public static String TOWNY_QUIZ_GUI_NAME = "Towny Quiz";
    public static String TOWNY_QUIZ_GUI_CATEGORY_NAME ="Quiz Categories";

    public static String MESSAGE_NO_PRE_REQUISITE_CATEGORY = "noPrerequisiteCategory";
    public static String MESSAGE_NO_PERMISSION = "noPermission";
    public static String MESSAGE_NO_PERMISSION_COMMAND = "noPermissionCommand";
    public static String MESSAGE_ON_START = "onStart";
    public static String MESSAGE_ON_FAILED = "onFailed";
    public static String MESSAGE_ON_COMPLETE = "onCompleted";
    public static String MESSAGE_NO_QUIZ = "noQuiz";
    public static String MESSAGE_REMINDER = "Reminder_Message";
    public static String MESSAGE_CANCEL_QUIZ = "onCancelQuiz";
    public static String MESSAGE_ON_NEXT_QUESTION = "onNextQuestion";
    public static String MESSAGE_QUIZ_STATUS_COMPLETE = "quizStatusComplete";
    public static String MESSAGE_QUIZ_STATUS_NO_PERMISSION = "quizStatusNoPermission";
    public static String MESSAGE_QUIZ_STATUS_REQ_NOT_MET = "quizStatusPreReqNotMet";

    public static String CONFIG_SHOULD_REMIND = "Remind_Player";
    public static String CONFIG_REMIND_INTERVAL = "Remind_Interval";
    public static String CONFIG_SOUND_ON_CORRECT_ANSWER = "soundOnCorrectAnswer";
    public static String CONFIG_SOUND_ON_WRONG_ANSWER = "soundOnWrongAnswer";
    public static String CONFIG_SOUND_ON_QUIZ_COMPLETE = "soundOnCompleted";
    public static String CONFIG_QUESTION_FORMAT = "Chat_Question_Format";

    public static String CONFIG_QUIZ_NOT_TAKEN_SETTING = "notTaken";
    public static String CONFIG_QUIZ_COMPLETED_SETTING = "Completed";
    public static String CONFIG_QUIZ_PRE_REQUISITE_SETTING = "prequisiteRequired";
    public static String CONFIG_QUIZ_NO_PERMISSION_SETTING = "noPermission";

    public static String CONFIG_QUIZ_CATEGORY_TITLE = "Title";
    public static String CONFIG_QUIZ_CATEGORY_ITEM = "Item";
    public static String CONFIG_QUIZ_CATEGORY_LORES = "Lore";

    public static NamespacedKey QUIZ_CATEGORY = new NamespacedKey(TownyQuiz.getInstance(), "QuizCategory");
    public static NamespacedKey QUIZ_CATEGORY_STATUS = new NamespacedKey(TownyQuiz.getInstance(), "QuizCategoryStatus");
}
