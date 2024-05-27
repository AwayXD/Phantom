package fr.alexdoru.megawallsenhancementsmod.hackerdetector.checks;

import fr.alexdoru.megawallsenhancementsmod.config.ConfigHandler;
import fr.alexdoru.megawallsenhancementsmod.hackerdetector.data.PlayerDataSamples;
import fr.alexdoru.megawallsenhancementsmod.hackerdetector.data.SampleListD;
import fr.alexdoru.megawallsenhancementsmod.hackerdetector.utils.ViolationLevelTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ScaffoldCheck extends Check {

    @Override
    public String getCheatName() {
        return "Scaffold";
    }

    @Override
    public String getCheatDescription() {
        return "The player places blocks under their feet automatically while gaining height rapidly";
    }

    @Override
    public boolean canSendReport() {
        return true;
    }

    @Override
    public void performCheck(EntityPlayer player, PlayerDataSamples data) {
        super.checkViolationLevel(player, this.check(player, data), data.scaffoldVL);
    }

    @Override
    public boolean check(EntityPlayer player, PlayerDataSamples data) {
        if (player.isRiding() || data.serverPosYList.size() < 4) return false;

        // Check if the player is using a block item and their hand is swinging
        final ItemStack itemStack = player.getHeldItem();
        if (itemStack != null && itemStack.getItem() instanceof ItemBlock && player.isSwingInProgress && player.hurtTime == 0) {
            // Check if the player's pitch is high (looking down) and they're moving fast on the XZ plane
            final double pitch = data.serverPitchList.get(0);
            final double speedXZSq = data.getSpeedXZSq();
            if (pitch > 50f && speedXZSq > 9d) {
                // Check the difference between the player's movement direction and looking direction
                final double angleDiff = Math.abs(data.getMoveLookAngleDiff());
                if (angleDiff > 165d && speedXZSq < 100d) {
                    // Analyze the player's vertical speed and average vertical acceleration
                    final double speedY = data.speedYList.get(0);
                    final double avgAccelY = avgAccel(data.serverPosYList);

                    // Avoid false flags when player is using stairs
                    if (isAlmostZero(avgAccelY)) return false;

                    // Condition for fast upward movement (scaffolding upwards)
                    if (speedY < 15d && speedY > 4d && avgAccelY > -25d) {
                        logDebug(player, data, pitch, speedXZSq, angleDiff, speedY, avgAccelY);
                        return true;
                    }

                    // Condition for slower upward movement with significant horizontal speed (common in scaffolding)
                    if (speedY < 4d && speedY > -1d && Math.abs(speedY) > 0.005d && speedXZSq > 25d) {
                        logDebug(player, data, pitch, speedXZSq, angleDiff, speedY, avgAccelY);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void logDebug(EntityPlayer player, PlayerDataSamples data, double pitch, double speedXZ, double angleDiff, double speedY, double avgAccelY) {
        if (ConfigHandler.debugLogging) {
            final String msg = String.format(" | pitch %.2f | speedXZ %.2f | angleDiff %.2f | speedY %.2f | avgAccelY %.2f",
                    pitch, speedXZ, angleDiff, speedY, avgAccelY);
            this.log(player, data, data.scaffoldVL, msg);
        }
    }

    public static ViolationLevelTracker newVL() {
        return new ViolationLevelTracker(2, 1, 24);
    }

    private static double avgAccel(SampleListD list) {
        return 50d * (list.get(3) - list.get(2) - list.get(1) + list.get(0));
    }

    private static boolean isAlmostZero(double d) {
        return Math.abs(d) < 0.001d;
    }

}
