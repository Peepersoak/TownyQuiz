package com.peepersoak.townyquiz.utils;

import com.peepersoak.townyquiz.TownyQuiz;
import com.peepersoak.townyquiz.questionsdata.QuestionsData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class EventListener implements Listener {

    private final HashMap<UUID, List<Boolean>> playerAnswer = new HashMap<>();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) return;
        if (!e.getView().getTitle().equalsIgnoreCase(StringPath.TOWNY_QUIZ_GUI_NAME)) return;
        if (e.getAction() == InventoryAction.HOTBAR_SWAP || e.getAction() == InventoryAction.HOTBAR_MOVE_AND_READD) {
            e.setCancelled(true);
            return;
        }

        e.setCancelled(true);

        Inventory inv = e.getClickedInventory();
        Player player = (Player) e.getWhoClicked();
        UUID uuid = player.getUniqueId();

        String category = TownyQuiz.getInstance().getCatergory(uuid);
        QuestionsData data = TownyQuiz.getInstance().getQuestions().getQuestionList().get(category);
        String question = data.getAllQuestion().get(TownyQuiz.getInstance().getCurrentQuestion(uuid));
        int maxChoice = data.getMaxChoice(question);

        int slot = e.getSlot();
        ItemStack item = null;
        switch (slot) {
            case 10, 12, 14, 16 -> item = inv.getItem(slot);
        }
        if (item == null) return;
        if (item.getType() == Material.BLACK_STAINED_GLASS_PANE) return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        if (maxChoice == 1) {
            if (meta.hasItemFlag(ItemFlag.HIDE_UNBREAKABLE)) {
                TownyQuiz.getInstance().addScore(uuid);
                Utils.playSound(player, TownyQuiz.getInstance().getConfig().getString(StringPath.CONFIG_SOUND_ON_CORRECT_ANSWER));
            } else {
                Utils.playSound(player, TownyQuiz.getInstance().getConfig().getString(StringPath.CONFIG_SOUND_ON_WRONG_ANSWER));
            }
            TownyQuiz.getInstance().nextQuestion(uuid);
            TownyQuiz.getInstance().getQuiz().nextQuestion(player);
            playerAnswer.remove(uuid);
        } else {
            if (playerAnswer.containsKey(uuid)) {
                List<Boolean> pAns = playerAnswer.get(uuid);
                pAns.add(meta.hasItemFlag(ItemFlag.HIDE_UNBREAKABLE));
                playerAnswer.put(uuid, pAns);
                inv.setItem(slot, Utils.createButton(" ", null, Material.BLACK_STAINED_GLASS_PANE, false, false));
            } else {
                List<Boolean> ans = new ArrayList<>();
                ans.add(meta.hasItemFlag(ItemFlag.HIDE_UNBREAKABLE));
                playerAnswer.put(uuid, ans);
                inv.setItem(slot, Utils.createButton(" ", null, Material.BLACK_STAINED_GLASS_PANE, false, false));
            }

            if (playerAnswer.get(uuid).size() >= maxChoice) {
                if (!playerAnswer.get(uuid).contains(false)) {
                    TownyQuiz.getInstance().addScore(uuid);
                    Utils.playSound(player, TownyQuiz.getInstance().getConfig().getString(StringPath.CONFIG_SOUND_ON_CORRECT_ANSWER));
                } else {
                    Utils.playSound(player, TownyQuiz.getInstance().getConfig().getString(StringPath.CONFIG_SOUND_ON_WRONG_ANSWER));
                }
                TownyQuiz.getInstance().nextQuestion(uuid);
                TownyQuiz.getInstance().getQuiz().nextQuestion(player);
                playerAnswer.remove(uuid);
            } else {
                Utils.sendSyncMessage(player, "&bChoose your next answer");
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        String message = e.getMessage().trim();
        checkBedrockAnswer(player, message);
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();
        String message = e.getMessage();
        if (checkBedrockAnswer(player, message)) {
            e.setCancelled(true);
        }
    }

    private boolean checkBedrockAnswer(Player player, String message) {
        UUID uuid = player.getUniqueId();

        if (TownyQuiz.getInstance().isTakingQuiz(uuid)) {
            String category = TownyQuiz.getInstance().getCatergory(uuid);
            QuestionsData data = TownyQuiz.getInstance().getQuestions().getQuestionList().get(category);
            String question = data.getAllQuestion().get(TownyQuiz.getInstance().getCurrentQuestion(uuid));
            List<String> answer = data.getAnswer(question);
            int maxChoice = data.getMaxChoice(question);

            List<Boolean> ans;
            if (playerAnswer.containsKey(uuid)) {
                ans = playerAnswer.get(uuid);
            } else {
                ans = new ArrayList<>();
            }
            ans.add(answer.contains(message));
            playerAnswer.put(uuid, ans);

            if (playerAnswer.get(uuid).size() >= maxChoice) {
                if (!ans.contains(false)) {
                    TownyQuiz.getInstance().addScore(uuid);
                    Utils.playSound(player, TownyQuiz.getInstance().getConfig().getString(StringPath.CONFIG_SOUND_ON_CORRECT_ANSWER));
                } else {
                    Utils.playSound(player, TownyQuiz.getInstance().getConfig().getString(StringPath.CONFIG_SOUND_ON_WRONG_ANSWER));
                }
                TownyQuiz.getInstance().nextQuestion(uuid);
                TownyQuiz.getInstance().getQuiz().nextQuestion(player);
                playerAnswer.remove(uuid);
            } else {
                Utils.sendSyncMessage(player, "&bChoose your next answer");
            }

            return true;
        }
        return false;
    }
}
