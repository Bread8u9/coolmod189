package com.github.Bread8u9.coolmod189;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

@Mod(modid = "autosoup", version = "1.2")
public class ExampleMod {

    // Настройки AutoSoup
    public static boolean isSoupEnabled = true;
    public static float triggerHealth = 10.0F;

    // Настройки TriggerBot
    public static boolean isTriggerEnabled = false;
    public static int triggerCps = 10;
    public static boolean targetPlayers = true;
    public static boolean targetMobs = false;

    // Таймер для контроля КПС
    private long lastHitTime = 0L;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.thePlayer != null && mc.currentScreen == null) {
                mc.displayGuiScreen(new GuiModSettings());
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.theWorld == null || mc.currentScreen != null) return;

        // 1. ЛОГИКА АВТОСУПА
        if (isSoupEnabled && mc.thePlayer.getHealth() = delay) {
                            // Машем рукой визуально
                            mc.thePlayer.swingItem();
                            // Отправляем пакет атаки на сервер
                            mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
                            lastHitTime = currentTime;
                        }
                    }
                }
            }
        }
    }

    private int findSoupInHotbar(InventoryPlayer inventory) {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (stack != null && stack.getItem() == Items.mushroom_stew) {
                return i;
            }
        }
        return -1;
    }

    // КЛАСС МЕНЮ НАСТРОЕК
    public static class GuiModSettings extends GuiScreen {
        
        @Override
        public void initGui() {
            this.buttonList.clear();
            int centerX = this.width / 2;
            int centerY = this.height / 2;

            // ЛЕВАЯ КОЛОНКА: Настройки AutoSoup
            this.buttonList.add(new GuiButton(1, centerX - 155, centerY - 50, 150, 20, getSoupStatusText()));
            this.buttonList.add(new GuiButton(2, centerX - 155, centerY - 25, 73, 20, "Суп ХП: -1"));
            this.buttonList.add(new GuiButton(3, centerX - 78, centerY - 25, 73, 20, "Суп ХП: +1"));

            // ПРАВАЯ КОЛОНКА: Настройки TriggerBot
            this.buttonList.add(new GuiButton(5, centerX + 5, centerY - 50, 150, 20, getTriggerStatusText()));
            this.buttonList.add(new GuiButton(6, centerX + 5, centerY - 25, 73, 20, "КПС: -1"));
            this.buttonList.add(new GuiButton(7, centerX + 82, centerY - 25, 73, 20, "КПС: +1"));
            this.buttonList.add(new GuiButton(8, centerX + 5, centerY, 150, 20, getTargetPlayersText()));
            this.buttonList.add(new GuiButton(9, centerX + 5, centerY + 25, 150, 20, getTargetMobsText()));

            // Кнопка закрытия меню по центру снизу
            this.buttonList.add(new GuiButton(4, centerX - 75, centerY + 65, 150, 20, "Готово"));
        }

        private String getSoupStatusText() { return "AutoSoup: " + (ExampleMod.isSoupEnabled ? "§aВКЛ" : "§cВЫКЛ"); }
        private String getTriggerStatusText() { return "TriggerBot: " + (ExampleMod.isTriggerEnabled ? "§aВКЛ" : "§cВЫКЛ"); }
        private String getTargetPlayersText() { return "Игроки: " + (ExampleMod.targetPlayers ? "§aДА" : "§cНЕТ"); }
        private String getTargetMobsText() { return "Мобы: " + (ExampleMod.targetMobs ? "§aДА" : "§cНЕТ"); }

        @Override
        protected void actionPerformed(GuiButton button) throws IOException {
            if (button.id == 1) {
                ExampleMod.isSoupEnabled = !ExampleMod.isSoupEnabled;
                button.displayString = getSoupStatusText();
            } else if (button.id == 2) {
                if (ExampleMod.triggerHealth > 1.0F) ExampleMod.triggerHealth -= 1.0F;
            } else if (button.id == 3) {
                if (ExampleMod.triggerHealth < 19.0F) ExampleMod.triggerHealth += 1.0F;
            } else if (button.id == 5) {
                ExampleMod.isTriggerEnabled = !ExampleMod.isTriggerEnabled;
                button.displayString = getTriggerStatusText();
            } else if (button.id == 6) {
                if (ExampleMod.triggerCps > 1) ExampleMod.triggerCps -= 1;
            } else if (button.id == 7) {
                if (ExampleMod.triggerCps < 20) ExampleMod.triggerCps += 1;
            } else if (button.id == 8) {
                ExampleMod.targetPlayers = !ExampleMod.targetPlayers;
                button.displayString = getTargetPlayersText();
            } else if (button.id == 9) {
                ExampleMod.targetMobs = !ExampleMod.targetMobs;
                button.displayString = getTargetMobsText();
            } else if (button.id == 4) {
                this.mc.displayGuiScreen(null);
            }
        }

        @Override
        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            this.drawDefaultBackground();
            int centerX = this.width / 2;
            int centerY = this.height / 2;

            this.drawCenteredString(this.fontRendererObj, "§b★ Настройки Мода ★", centerX, centerY - 85, 16777215);

            // Текстовая информация для AutoSoup
            String soupHpText = "ХП для супа: §e" + (ExampleMod.triggerHealth / 2.0F) + " §rсерд.";
            this.drawCenteredString(this.fontRendererObj, soupHpText, centerX - 80, centerY + 5, 16777215);

            // Текстовая информация для TriggerBot
            String cpsText = "Текущий КПС: §e" + ExampleMod.triggerCps;
            this.drawCenteredString(this.fontRendererObj, cpsText, centerX + 80, centerY - 7, 16777215);

            super.drawScreen(mouseX, mouseY, partialTicks);
        }

        @Override
        public boolean doesGuiPauseGame() { return false; }
    }
}
