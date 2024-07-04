package fr.alexdoru.megawallsenhancementsmod.gui.guiscreens;

import fr.alexdoru.megawallsenhancementsmod.asm.hooks.RendererLivingEntityHook_AprilFun;
import fr.alexdoru.megawallsenhancementsmod.config.ConfigHandler;
import fr.alexdoru.megawallsenhancementsmod.gui.elements.OptionGuiButton;
import fr.alexdoru.megawallsenhancementsmod.gui.elements.SimpleGuiButton;
import fr.alexdoru.megawallsenhancementsmod.gui.elements.TextElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

import java.text.SimpleDateFormat;
import java.util.Date;

import static net.minecraft.util.EnumChatFormatting.*;

public class GeneralConfigGuiScreen extends MyGuiScreen {

    private static final ResourceLocation customFontLocation = new ResourceLocation("minecraft", "textures/font/custom_font.png"); // Replace with your custom font texture location

    @Override
    public void initGui() {
        final int buttonsWidth = 150;
        this.maxWidth = buttonsWidth + (buttonsWidth + 10) * 2;
        this.maxHeight = (buttonsHeight + 4) * 10 + buttonsHeight;
        super.initGui();

        // Calculate the center position for the first button
        int xPos = this.width / 2 - (3 * buttonsWidth + 2 * 10) / 2; // Adjust the multiplier as needed for the number of buttons

        // Use custom texture font rendering
        int posY = getButtonYPos(-1);
        this.elementList.add(new TextElement(DARK_PURPLE + "" + BOLD + "Phantom", getxCenter(), getButtonYPos(-1)).setSize(2).makeCentered());
        // Add buttons with adjusted positions for horizontal alignment
        this.buttonList.add(new SimpleGuiButton(xPos, getButtonYPos(1), buttonsWidth, buttonsHeight, DARK_PURPLE + "Config", () -> mc.displayGuiScreen(new HackerDetectorConfigGuiScreen(this))));
        this.buttonList.add(new SimpleGuiButton(xPos + buttonsWidth + 10, getButtonYPos(1), buttonsWidth, buttonsHeight, DARK_PURPLE + "Fun stuff :)", () -> mc.displayGuiScreen(new NoCheatersConfigGuiScreen(this))));
        this.buttonList.add(new SimpleGuiButton(xPos + 2 * (buttonsWidth + 10), getButtonYPos(1), buttonsWidth, buttonsHeight, DARK_PURPLE + "Item Notifications", () -> mc.displayGuiScreen(new ItemNotificationsGuiScreen(this))));
        if ("01/04".equals(new SimpleDateFormat("dd/MM").format(new Date().getTime()))) {
            this.buttonList.add(new OptionGuiButton(
                    xPos + 3 * (buttonsWidth + 10),
                    getButtonYPos(1),
                    130, buttonsHeight,
                    "April fun ",
                    (b) -> RendererLivingEntityHook_AprilFun.active = b,
                    () -> RendererLivingEntityHook_AprilFun.active, GRAY + "haha got u"));
        }
    }

    // Method to draw text using a custom texture font
    private void drawCustomText(String text, int x, int y, float scale, String color) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(customFontLocation);
        for (int i = 0; i < text.length(); i++) {
            char character = text.charAt(i);
            int posX = (int) (x + i * 8 * scale);
            Gui.drawModalRectWithCustomSizedTexture(posX, y, character * 8, 0, 8, 8, 256, 256);
        }
    }
}
