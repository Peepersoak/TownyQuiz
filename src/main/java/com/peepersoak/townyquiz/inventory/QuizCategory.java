package com.peepersoak.townyquiz.inventory;

import com.peepersoak.townyquiz.TownyQuiz;
import com.peepersoak.townyquiz.questionsdata.QuestionsData;
import com.peepersoak.townyquiz.utils.QuizStatus;
import com.peepersoak.townyquiz.utils.StringPath;
import com.peepersoak.townyquiz.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class QuizCategory {

    public void openCategory(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, StringPath.TOWNY_QUIZ_GUI_CATEGORY_NAME);

        int slot = 0;
        for (String category : TownyQuiz.getInstance().getQuizCategories()) {
            boolean hasTaken = player.hasPermission("quiz.category.complete." + category);
            QuestionsData data = TownyQuiz.getInstance().getQuestions().getQuestionList().get(category);
            String title = data.getTitle();
            int numberOfQuestion = data.getNumberOfQuestions();
            int passingScore = data.getPassingScore();

            if (hasTaken) {
                String path = StringPath.CONFIG_QUIZ_COMPLETED_SETTING + ".";
                inv.setItem(slot, getButton(player, path, title, numberOfQuestion, passingScore, data, QuizStatus.COMPLETE, category));
            } else {
                if (data.checkPermission() && !player.hasPermission("quiz.category." + category)) {
                    String path = StringPath.CONFIG_QUIZ_NO_PERMISSION_SETTING + ".";
                    inv.setItem(slot, getButton(player, path, title, numberOfQuestion, passingScore, data, QuizStatus.NO_PERMISSION, category));
                } else {
                    if (data.getPreRequisiteCategory() == null || data.getPreRequisiteCategory().isEmpty()) {
                        String path = StringPath.CONFIG_QUIZ_NOT_TAKEN_SETTING + ".";
                        inv.setItem(slot, getButton(player, path, title, numberOfQuestion, passingScore, data, QuizStatus.AVAILABLE, category));
                    } else {
                        String path = StringPath.CONFIG_QUIZ_PRE_REQUISITE_SETTING + ".";
                        inv.setItem(slot, getButton(player, path, title, numberOfQuestion, passingScore, data, QuizStatus.PRE_REQUISITE, category));
                    }
                }
            }
            slot++;
        }

        player.openInventory(inv);
    }

    private ItemStack getButton(Player player, String path, String categoryName, int numberOfQuestion, int passingScore, QuestionsData data, String status, String categoryKey) {
        Material material = Material.valueOf(TownyQuiz.getInstance().getConfig().getString(path + StringPath.CONFIG_QUIZ_CATEGORY_ITEM));
        String configTitle = TownyQuiz.getInstance().getConfig().getString(path + StringPath.CONFIG_QUIZ_CATEGORY_TITLE);
        List<String> lore = TownyQuiz.getInstance().getConfig().getStringList(path + StringPath.CONFIG_QUIZ_CATEGORY_LORES);

        if (configTitle == null) return null;

        String title = configTitle.replace("{categoryTitle}", categoryName);
        List<String> newLore = new ArrayList<>();

        for (String str : lore) {
            String s = str.replace("{total}", numberOfQuestion + "").replace("{passing}", passingScore + "");
            if (s.contains("{requiredCategoryTitle}")) {
                List<String> requiredCategory = data.getPreRequisiteCategory();
                for (String reqCategory : requiredCategory) {
                    boolean hasTaken = player.hasPermission("quiz.category.complete." + reqCategory);

                    String categoryTitle = TownyQuiz.getInstance().getQuestions().getQuestionList().get(reqCategory).getTitle();

                    if (hasTaken) {
                        newLore.add("- " + s.replace("{requiredCategoryTitle}","&m" + categoryTitle) + "&a ✔");
                    } else {
                        newLore.add( "- " + s.replace("{requiredCategoryTitle}", categoryTitle));
                    }
                }
            } else {
                newLore.add(s);
            }
        }

        ItemStack item = Utils.createButton(title, newLore, material, false, false);
        assert item != null;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        meta.getPersistentDataContainer().set(StringPath.QUIZ_CATEGORY_STATUS, PersistentDataType.STRING, status);
        meta.getPersistentDataContainer().set(StringPath.QUIZ_CATEGORY, PersistentDataType.STRING, categoryKey);

        item.setItemMeta(meta);
        return item;
    }
}
