package com.peepersoak.townyquiz.utils;

import com.peepersoak.townyquiz.TownyQuiz;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Data {

    public Data(String fileName) {
        this.fileName = fileName;
        init();
    }

    private final String fileName;
    private YamlConfiguration config;

    private void init() {
        File file = new File(TownyQuiz.getInstance().getDataFolder(), fileName);

        if (!file.exists()) {
            if (TownyQuiz.getInstance().getResource(fileName) != null) {
                TownyQuiz.getInstance().saveResource(fileName, false);
                file = new File(TownyQuiz.getInstance().getDataFolder(), fileName);
            } else {
                TownyQuiz.getInstance().getLogger().warning("Failed To Create File: " + fileName);
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
        config.options().copyDefaults(true);
    }

    public YamlConfiguration getConfig() {
        return config;
    }
}
