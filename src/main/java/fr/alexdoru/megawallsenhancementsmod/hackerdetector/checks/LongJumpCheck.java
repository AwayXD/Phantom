package fr.alexdoru.megawallsenhancementsmod.hackerdetector.checks;
import fr.alexdoru.megawallsenhancementsmod.config.ConfigHandler;
import fr.alexdoru.megawallsenhancementsmod.hackerdetector.data.PlayerDataSamples;
import fr.alexdoru.megawallsenhancementsmod.hackerdetector.utils.ViolationLevelTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class LongJumpCheck extends Check {

    @Override
    public String getCheatName() {
        return "LongJump";
    }

    @Override
    public String getCheatDescription() {
        return "The player may be flying)";
    }

    @Override
    public boolean canSendReport() {
        return true;
    }

    @Override
    public void performCheck(EntityPlayer player, PlayerDataSamples data) {
        super.checkViolationLevel(player, this.check(player, data), data.longjumpVL);

    }

    @Override
    public boolean check(EntityPlayer player, PlayerDataSamples data) {
        // If the player is not moving or is riding an entity, skip the check
        if (data.isNotMovingXZ() || player.isRiding()) return false;

        // Threshold for maximum horizontal distance covered in a single jump
        final double maxJumpDistance = 4.0; // Adjust this value based on your needs

        // Calculate the horizontal distance covered during the jump
        double jumpDistance = data.getJumpDistance();

        // Check if the player's jump distance exceeds the maximum allowed distance
        if (jumpDistance > maxJumpDistance) {
            // Log the violation and increment the violation level
            data.longjumpVL.add(1);
            if (ConfigHandler.debugLogging) {
                this.log(player, data, data.longjumpVL, null);
            }
            return true;
        }

        return false;
    }

    @Override
    protected void log(EntityPlayer player, PlayerDataSamples data, ViolationLevelTracker vl, String extramsg) {
        final ItemStack itemStack = player.getHeldItem();
        final Item item = itemStack == null ? null : itemStack.getItem();
        super.log(player, data, vl,
                " | jumpDistance " + String.format("%.2f", data.getJumpDistance())
                        + " | speedXZ " + String.format("%.2f", data.getSpeedXZ())
                        + (item != null ? " | item held " + item.getRegistryName() : "")
                        + " | moveDiff " + String.format("%.2f", Math.abs(data.getMoveLookAngleDiff())));
    }

    public static ViolationLevelTracker newVL() {
        return new ViolationLevelTracker(1);
    }
}
