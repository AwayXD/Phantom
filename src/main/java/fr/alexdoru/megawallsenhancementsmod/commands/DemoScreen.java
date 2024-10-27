package fr.alexdoru.megawallsenhancementsmod.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

public class DemoScreen extends GuiScreen {

    @Override
    public void initGui() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        this.buttonList.add(new GuiButton(0, centerX - 5, centerY + 5, 83, 20, "Purchase Now!"));
        this.buttonList.add(new GuiButton(1, centerX - 90, centerY + 5, 83, 20, "Continue Playing!"));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            Minecraft.getMinecraft().displayGuiScreen(null); // Close screen for now
        } else if (button.id == 1) {
            Minecraft.getMinecraft().displayGuiScreen(null); // Close the screen
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // Draw semi-transparent background behind the screen
        this.drawDefaultBackground();

        int boxWidth = 200;
        int boxHeight = 100;
        int boxX = (this.width - boxWidth) / 2;
        int boxY = (this.height - boxHeight) / 2 - 20;

        // Draw the border
        drawRect(boxX - 2, boxY - 2, boxX + boxWidth + 2, boxY + boxHeight + 2, 0xFF000000); // Black border

        // Draw the inner box
        drawRect(boxX, boxY, boxX + boxWidth, boxY + boxHeight, 0xFFAAAAAA); // Gray inner box

        drawCenteredString(this.fontRendererObj, "Minecraft Demo Mode", this.width / 2, boxY + 5, 0xFFFFFF);

        int textY = boxY + 20;
        drawCenteredString(this.fontRendererObj, "Move with W, A, S, D keys", this.width / 2, textY, 0xFFFFFF);
        drawCenteredString(this.fontRendererObj, "Jump: Space, Inventory: E", this.width / 2, textY + 10, 0xFFFFFF);

        drawCenteredString(this.fontRendererObj, "Demo lasts 5 in-game days.", this.width / 2, textY + 30, 0xFFFFFF);
        drawCenteredString(this.fontRendererObj, "Check advancements for hints!", this.width / 2, textY + 40, 0xFFFFFF);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
