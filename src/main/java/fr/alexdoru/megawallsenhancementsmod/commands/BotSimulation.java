package fr.alexdoru.megawallsenhancementsmod.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.util.MathHelper;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import com.mojang.authlib.GameProfile;
import java.util.UUID;

public class BotSimulation {
    private static EntityOtherPlayerMP botEntity;
    private static final int SIMULATION_DURATION = 90; // duration in ticks
    private static int tickCounter = 0;
    private static boolean isActive = false;
    private static final Random random = new Random();
    private static double baseRadius = 0.0; // Start close and gradually increase
    private static double currentAngle = 0;
    private static double speed = .8; // Faster orbit

    // Head movement delay
    private static int headMovementCounter = 0;
    private static final int HEAD_MOVEMENT_DELAY = 3; // Delay in ticks between head movement updates

    // Expanded list of words to generate random usernames
    private static final List<String> randomWords = new ArrayList<String>() {{
        add("Dragon");
        add("Phoenix");
        add("Titan");
        add("Ghost");
        add("Shadow");
        add("Storm");
        add("Raven");
        add("Thunder");
        add("Blaze");
        add("Knight");
        add("Night");
        add("King");
        add("Queen");
        add("Warlord");
        add("Master");
        add("Viking");
        add("Gladiator");
        add("Wanderer");
        add("Mage");
        add("Sorcerer");
        add("Destroyer");
        add("Hunter");
        add("Slayer");
        add("Lancer");
        add("Baron");
        add("Warrior");
        add("Monarch");
        add("Defender");
        add("Assassin");
        add("Champion");
    }};

    // Expanded list of numbers for more variety
    private static final List<String> randomNumbers = new ArrayList<String>() {{
        add("007");
        add("1337");
        add("123");
        add("9000");
        add("404");
        add("555");
        add("256");
        add("666");
        add("999");
        add("321");
        add("101");
        add("202");
        add("303");
        add("404");
        add("505");
        add("696");
        add("808");
        add("1234");
        add("5678");
        add("1111");
        add("2222");
        add("3333");
        add("8888");
    }};

    public static void startSimulation() {
        if (isActive) return; // Prevent multiple activations

        Minecraft mc = Minecraft.getMinecraft();
        String randomUsername = generateRandomUsername(); // Generate a random username

        // Create a new UUID for the bot (since it's required by GameProfile)
        UUID randomUUID = UUID.randomUUID();

        // Create a new GameProfile with the random UUID and username
        GameProfile randomGameProfile = new GameProfile(randomUUID, randomUsername);
        botEntity = new EntityOtherPlayerMP(mc.theWorld, randomGameProfile); // Simulate a player

        botEntity.setCustomNameTag(randomUsername); // Set the bot's username
        botEntity.copyLocationAndAnglesFrom(mc.thePlayer); // Start at player's position
        botEntity.setInvisible(false); // Make visible for debugging

        mc.theWorld.spawnEntityInWorld(botEntity);
        isActive = true;
        tickCounter = 0;
    }

    // Random username generator (2 words and a number)
    private static String generateRandomUsername() {
        String randomWord1 = randomWords.get(random.nextInt(randomWords.size()));
        String randomWord2 = randomWords.get(random.nextInt(randomWords.size()));
        String randomNumber = randomNumbers.get(random.nextInt(randomNumbers.size())); // Add a random number
        return randomWord1 + randomWord2 + randomNumber; // Combine two words and number
    }

    public static void onTick() {
        if (!isActive) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || botEntity == null || tickCounter > SIMULATION_DURATION) {
            stopSimulation();
            return;
        }

        // Gradually increase the radius as the bot moves away from the player
        baseRadius = Math.min(1.0, baseRadius + 0.05); // Max distance of 3.0 blocks

        // Randomize speed and calculate circular position
        speed = 0.2 + random.nextDouble() * 0.2; // Speed varies between 0.2 and 0.4 radians per tick
        currentAngle += speed; // Increment angle for smooth circular motion

        // Calculate new position for the bot in a growing circular motion
        double offsetX = mc.thePlayer.posX + baseRadius * MathHelper.cos((float) currentAngle);
        double offsetZ = mc.thePlayer.posZ + baseRadius * MathHelper.sin((float) currentAngle);
        double offsetY = mc.thePlayer.posY; // Keep at player height

        botEntity.setPosition(offsetX, offsetY, offsetZ);

        // Make the bot move its legs like it's walking
        botEntity.setSprinting(true);
        botEntity.motionX = -MathHelper.sin((float) currentAngle) * 0.1;
        botEntity.motionZ = MathHelper.cos((float) currentAngle) * 0.1;

        // Simulate random head movement with a delay
        if (headMovementCounter >= HEAD_MOVEMENT_DELAY) {
            botEntity.rotationYawHead = random.nextFloat() * 360 - 180; // Random yaw between -180 and 180
            botEntity.rotationPitch = random.nextFloat() * 60 - 30; // Random pitch between -30 and 30 (includes looking up)
            headMovementCounter = 0; // Reset the counter
        } else {
            headMovementCounter++; // Increment the counter to delay the next head movement
        }

        // Simulate swinging as if it's attacking
        botEntity.swingItem();

        tickCounter++;
    }

    public static void stopSimulation() {
        if (botEntity != null) {
            botEntity.setDead();
            botEntity = null;
        }
        isActive = false;
    }
}
