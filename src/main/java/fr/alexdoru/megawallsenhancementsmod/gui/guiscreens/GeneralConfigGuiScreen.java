package fr.alexdoru.megawallsenhancementsmod.gui.guiscreens;

import fr.alexdoru.megawallsenhancementsmod.asm.hooks.RendererLivingEntityHook_AprilFun;
import fr.alexdoru.megawallsenhancementsmod.config.ConfigHandler;
import fr.alexdoru.megawallsenhancementsmod.gui.elements.OptionGuiButton;
import fr.alexdoru.megawallsenhancementsmod.gui.elements.SimpleGuiButton;
import fr.alexdoru.megawallsenhancementsmod.gui.elements.TextElement;

import java.text.SimpleDateFormat;
import java.util.Date;

import static net.minecraft.util.EnumChatFormatting.*;

public class GeneralConfigGuiScreen extends MyGuiScreen {

    @Override
    public void initGui() {
        final int buttonsWidth = 150;
        this.maxWidth = buttonsWidth + (buttonsWidth + 10) * 2;
        this.maxHeight = (buttonsHeight + 4) * 10 + buttonsHeight;
        super.initGui();
        final int xPos = this.getxCenter() - buttonsWidth / 2;
        this.elementList.add(new TextElement(DARK_PURPLE + "Phantom Anticheat", getxCenter(), getButtonYPos(-1)).setSize(2).makeCentered());
        this.buttonList.add(new SimpleGuiButton(xPos, getButtonYPos(1), buttonsWidth, buttonsHeight, DARK_PURPLE + "Config", () -> mc.displayGuiScreen(new HackerDetectorConfigGuiScreen(this))));
        this.buttonList.add(new SimpleGuiButton(xPos, getButtonYPos(2), buttonsWidth, buttonsHeight, DARK_PURPLE + "Fun stuff :)", () -> mc.displayGuiScreen(new NoCheatersConfigGuiScreen(this))));
        this.buttonList.add(new SimpleGuiButton(xPos, getButtonYPos(3), buttonsWidth, buttonsHeight, DARK_PURPLE + "Item Notifications", () -> mc.displayGuiScreen(new ItemNotificationsGuiScreen(this))));
        this.buttonList.add(new SimpleGuiButton(xPos, getButtonYPos(9), buttonsWidth, buttonsHeight, "Done", () -> mc.displayGuiScreen(null)));
        if ("01/04".equals(new SimpleDateFormat("dd/MM").format(new Date().getTime()))) {
            this.buttonList.add(new OptionGuiButton(
                    getxCenter() + buttonsWidth / 2 + 10,
                    getButtonYPos(4),
                    130, buttonsHeight,
                    "April fun ",
                    (b) -> RendererLivingEntityHook_AprilFun.active = b,
                    () -> RendererLivingEntityHook_AprilFun.active, GRAY + "haha got u"));
        }
    }

}
