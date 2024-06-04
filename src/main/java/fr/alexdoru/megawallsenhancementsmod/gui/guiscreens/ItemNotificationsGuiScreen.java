package fr.alexdoru.megawallsenhancementsmod.gui.guiscreens;

import fr.alexdoru.megawallsenhancementsmod.config.ConfigHandler;
import fr.alexdoru.megawallsenhancementsmod.gui.elements.OptionGuiButton;
import fr.alexdoru.megawallsenhancementsmod.gui.elements.TextElement;
import fr.alexdoru.megawallsenhancementsmod.gui.guiscreens.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static net.minecraft.util.EnumChatFormatting.*;

public class ItemNotificationsGuiScreen extends MyGuiScreen {

    public ItemNotificationsGuiScreen(GuiScreen parent) {
        this.parent = parent;
    }

    @Override
    public void initGui() {
        final int sideButtonWidth = 90;
        this.maxWidth = BUTTON_WIDTH + (10 + sideButtonWidth) * 2;
        this.maxHeight = (buttonsHeight + 4) * 12 + buttonsHeight;
        super.initGui();
        final int xPos = getxCenter() - BUTTON_WIDTH / 2;
        this.elementList.add(new TextElement(DARK_PURPLE + "Item Notifications (Beta)", getxCenter(), getButtonYPos(-1)).setSize(2).makeCentered());
        this.elementList.add(new TextElement(WHITE + "Disclaimer: this is not 100% accurate and can sometimes flag legit players,", getxCenter(), getButtonYPos(0)).makeCentered());
        this.buttonList.add(new OptionGuiButton(
                xPos, getButtonYPos(2),
                "Chainmail Armor",
                (b) -> ConfigHandler.showChainNotification = b,
                () -> ConfigHandler.showChainNotification,
                GRAY + "the player may be sniping (bought chainmail armor)."));
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            EntityPlayer player = event.player;
            if (player != null && player != Minecraft.getMinecraft().thePlayer) {
                for (ItemStack itemStack : player.getInventory()) {
                    if (ConfigHandler.showChainNotification && isChainmailArmor(itemStack)) {
                        notifyPlayer(Minecraft.getMinecraft().thePlayer, player);
                    }
                }
            }
        }
    }

    private boolean isChainmailArmor(ItemStack itemStack) {
        return itemStack != null && (itemStack.getItem() == Items.chainmail_helmet ||
                itemStack.getItem() == Items.chainmail_chestplate ||
                itemStack.getItem() == Items.chainmail_leggings ||
                itemStack.getItem() == Items.chainmail_boots);
    }

    private void notifyPlayer(EntityPlayerSP clientPlayer, EntityPlayer otherPlayer) {
        if (clientPlayer != null) {
            clientPlayer.addChatMessage(new ChatComponentText(GOLD + otherPlayer.getName() + " has equipped chainmail armor!"));
        }
    }
}
