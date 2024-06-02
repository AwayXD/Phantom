package fr.alexdoru.megawallsenhancementsmod.hackerdetector.checks;

import fr.alexdoru.megawallsenhancementsmod.config.ConfigHandler;
import fr.alexdoru.megawallsenhancementsmod.hackerdetector.data.PlayerDataSamples;
import fr.alexdoru.megawallsenhancementsmod.hackerdetector.data.SampleListD;
import fr.alexdoru.megawallsenhancementsmod.hackerdetector.utils.ViolationLevelTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ScaffoldCheck extends Check {
    public String getCheatName() {
        return "Scaffold";
    }

    public String getCheatDescription() {
        return "The player places blocks under their feet automatically while gaining height rapidly";
    }

    public boolean canSendReport() {
        return true;
    }

    public void performCheck(EntityPlayer player, PlayerDataSamples data) {
        checkViolationLevel(player, check(player, data), new ViolationLevelTracker[] { data.scaffoldVL });
    }

    public boolean check(EntityPlayer player, PlayerDataSamples data) {
        if (player.isRiding() || data.serverPosYList.size() < 4)
            return false;
        if (player.isSwingInProgress && player.hurtTime == 0 && data.serverPitchList.get(0) > 50.0F && data.getSpeedXZSq() > 9.0D) {
            ItemStack itemStack = player.getHeldItem();
            if (itemStack != null && itemStack.getItem() instanceof net.minecraft.item.ItemBlock) {
                double angleDiff = Math.abs(data.getMoveLookAngleDiff());
                double speedXZSq = data.getSpeedXZSq();
                if (angleDiff > 165.0D && speedXZSq < 100.0D) {
                    double speedY = data.speedYList.get(0);
                    double avgAccelY = avgAccel(data.serverPosYList);
                    if (isAlmostZero(avgAccelY))
                        return false;
                    if (speedY < 15.0D && speedY > 4.0D && avgAccelY > -25.0D) {
                        if (ConfigHandler.debugLogging) {
                            String msg = " | pitch " + String.format("%.2f", new Object[] { Float.valueOf(data.serverPitchList.get(0)) }) + " | speedXZ " + String.format("%.2f", new Object[] { Double.valueOf(data.getSpeedXZ()) }) + " | angleDiff " + String.format("%.2f", new Object[] { Double.valueOf(angleDiff) }) + " | speedY " + String.format("%.2f", new Object[] { Double.valueOf(speedY) }) + " | avgAccelY " + String.format("%.2f", new Object[] { Double.valueOf(avgAccelY) });
                            log(player, data, data.scaffoldVL, msg);
                        }
                        return true;
                    }
                    if (speedY < 4.0D && speedY > -1.0D && Math.abs(speedY) > 0.005D && speedXZSq > 25.0D) {
                        if (ConfigHandler.debugLogging) {
                            String msg = " | pitch " + String.format("%.2f", new Object[] { Float.valueOf(data.serverPitchList.get(0)) }) + " | speedXZ " + String.format("%.2f", new Object[] { Double.valueOf(data.getSpeedXZ()) }) + " | angleDiff " + String.format("%.2f", new Object[] { Double.valueOf(angleDiff) }) + " | speedY " + String.format("%.2f", new Object[] { Double.valueOf(speedY) }) + " | avgAccelY " + String.format("%.2f", new Object[] { Double.valueOf(avgAccelY) });
                            log(player, data, data.scaffoldVL, msg);
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static ViolationLevelTracker newVL() {
        return new ViolationLevelTracker(2, 1, 10);
    }

    private static double avgAccel(SampleListD list) {
        return 50.0D * (list.get(3) - list.get(2) - list.get(1) + list.get(0));
    }

    private static boolean isAlmostZero(double d) {
        return (Math.abs(d) < 0.001D);
    }
}
