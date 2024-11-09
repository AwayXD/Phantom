package fr.alexdoru.megawallsenhancementsmod.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BotListener {

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        String message = event.message.getUnformattedText();
        String username = Minecraft.getMinecraft().thePlayer.getName(); // Gets the player's current username

        // Check if the message contains "watchdogbot" followed by the player's username
        if (message.contains("watchdogbot " + username)) {
            BotSimulation.startSimulation();

            // Send a debug message to the player's chat
          //  Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Debug: Watchdog bot simulation started."));
        }
    }
}
