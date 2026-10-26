package com.github.Bread8u9.coolmod189;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

@Mod(modid = "autosoup", version = "1.1")
public class ExampleMod {

    public static boolean isEnabled = true;
    public static float triggerHealth = 10.0F;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.thePlayer != null && mc.currentScreen == null) {
                mc.displayGuiScreen(new GuiAutosoupSettings());
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (!isEnabled) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.theWorld == null || mc.currentScreen != null) return;
        
        if (mc.thePlayer.getHealth() < triggerHealth) {
            int soupSlot = findSoupInHotbar(mc.thePlayer.inventory);
            if (soupSlot != -1) {
                int oldSlot = mc.thePlayer.inventory.currentItem;
                mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(soupSlot));
                mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getStackInSlot(soupSlot)));
                mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(oldSlot));
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

    public static class GuiAutosoupSettings extends GuiScreen {
        
        @Override
        public void initGui() {
            this.buttonList.clear();
            this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 2 - 40, 200, 20, getStatusText()));
            this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 2, 95, 20, "ХП для супа: -1"));
            this.buttonList.add(new GuiButton(3, this.width / 2 + 5, this.height / 2, 95, 20, "ХП для супа: +1"));
            this.buttonList.add(new GuiButton(4, this.width / 2 - 100, this.height / 2 + 40, 200, 20, "Готово"));
        }

        private String getStatusText() {
            return "Автосуп: " + (ExampleMod.isEnabled ? "§aВКЛ" : "§cВЫКЛ");
        }

        @Override
        protected void actionPerformed(GuiButton button) throws IOException {
            if (button.id == 1) {
                ExampleMod.isEnabled = !ExampleMod.isEnabled;
                button.displayString = getStatusText();
            } else if (button.id == 2) {
                if (ExampleMod.triggerHealth > 1.0F) ExampleMod.triggerHealth -= 1.0F;
            } else if (button.id == 3) {
                if (ExampleMod.triggerHealth < 19.0F) ExampleMod.triggerHealth += 1.0F;
            } else if (button.id == 4) {
                this.mc.displayGuiScreen(null);
            }
        }

        @Override
        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            this.drawDefaultBackground();
            
            String title = "Настройки AutoSoup";
            String currentHpText = "Суп сработает при: §e" + (ExampleMod.triggerHealth / 2.0F) + " §rсердечках (" + (int)ExampleMod.triggerHealth + " HP)";
            
            this.drawCenteredString(this.fontRendererObj, title, this.width / 2, this.height / 2 - 80, 16777215);
            this.drawCenteredString(this.fontRendererObj, currentHpText, this.width / 2, this.height / 2 + 25, 16777215);
            
            super.drawScreen(mouseX, mouseY, partialTicks);
        }

        @Override
        public boolean doesGuiPauseGame() {
            return false;
        }
    }
}
