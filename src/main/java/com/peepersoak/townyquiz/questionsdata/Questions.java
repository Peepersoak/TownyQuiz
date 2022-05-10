package com.peepersoak.townyquiz.questionsdata;

import com.peepersoak.townyquiz.utils.Data;

import java.util.HashMap;

public class Questions {

    private final HashMap<String, QuestionsData> questionList;

    public Questions(Data data) {
        questionList = new HashMap<>();
        if (data == null || data.getConfig() == null) return;
        for (String key : data.getConfig().getKeys(false)) {
            questionList.put(key, new QuestionsData(key, data));
        }
    }

    public HashMap<String, QuestionsData> getQuestionList() {
        return questionList;
    }
}
