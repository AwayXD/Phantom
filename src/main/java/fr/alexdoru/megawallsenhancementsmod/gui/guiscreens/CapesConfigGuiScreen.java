package fr.alexdoru.megawallsenhancementsmod.gui.guiscreens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import java.util.List;
import java.util.ArrayList;
import fr.alexdoru.megawallsenhancementsmod.config.ConfigHandler;

public class CapesConfigGuiScreen extends GuiScreen {

    private static final ResourceLocation customFontLocation = new ResourceLocation("minecraft", "textures/font/custom_font.png"); // Replace with your custom font texture location
    private final List<GuiButton> buttonList = new ArrayList<>();
    private final int buttonsWidth = 150;
    private final int buttonsHeight = 20;
    private final int maxWidth;
    private final int maxHeight;

    public CapesConfigGuiScreen(GuiScreen parentScreen) {
        this.maxWidth = buttonsWidth + (buttonsWidth + 10) * 2;
        this.maxHeight = (buttonsHeight + 4) * 10 + buttonsHeight;
    }

    @Override
    public void initGui() {
        final int xPos = this.width / 2 - buttonsWidth / 2;

        this.buttonList.add(new GuiButton(0, xPos, getButtonYPos(1), buttonsWidth, buttonsHeight, EnumChatFormatting.DARK_PURPLE + "Enable Capes"));
        this.buttonList.add(new GuiButton(1, xPos, getButtonYPos(2), buttonsWidth, buttonsHeight, EnumChatFormatting.DARK_PURPLE + "Disable Capes"));

        // Add more buttons as needed

        super.initGui();
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                ConfigHandler.enableCape = true;
                break;
            case 1:
                ConfigHandler.enableCape = false;
                break;
            // Handle more button actions as needed
        }
    }

    private int getButtonYPos(int buttonIndex) {
        return this.height / 2 - 100 + (buttonsHeight + 4) * buttonIndex;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        drawCustomText(EnumChatFormatting.DARK_PURPLE + "Cape Configurations", this.width / 2, getButtonYPos(-1), 2, EnumChatFormatting.DARK_PURPLE.toString());
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawCustomText(String text, int x, int y, float scale, String color) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(customFontLocation);
        for (int i = 0; i < text.length(); i++) {
            char character = text.charAt(i);
            int posX = (int) (x + i * 8 * scale);
            this.drawTexturedModalRect(posX, y, character * 8, 0, 8, 8);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
