package fr.alexdoru.megawallsenhancementsmod.hackerdetector.checks;

import fr.alexdoru.megawallsenhancementsmod.config.ConfigHandler;
import fr.alexdoru.megawallsenhancementsmod.hackerdetector.data.PlayerDataSamples;
import fr.alexdoru.megawallsenhancementsmod.hackerdetector.utils.ViolationLevelTracker;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class NukerCheck extends Check {

    @Override
    public String getCheatName() {
        return "Nuker";
    }

    @Override
    public String getCheatDescription() {
        return "The Player has broken a bed through blocks";
    }

    @Override
    public boolean canSendReport() {
        return true;
    }

    @Override
    public void performCheck(EntityPlayer player, PlayerDataSamples data) {
        super.checkViolationLevel(player, this.check(player, data), data.nukerVL);
    }

    @Override
    public boolean check(EntityPlayer player, PlayerDataSamples data) {
        if (player.isRiding() || data.serverPosYList.size() < 4) return false;

        // Check if the player is swinging and not taking damage
        if (player.isSwingInProgress && player.hurtTime == 0) {
            ItemStack itemStack = player.getHeldItem();
            if (itemStack != null) {
                Block block = Block.getBlockFromItem(itemStack.getItem());

                // Check if the block being broken is a bed
                if (block instanceof BlockBed) {
                    // Here we should check if the bed is being broken through other blocks
                    // For simplicity, we assume `data` has a method to check block breaking through other blocks
                    if (data.isBreakingBlockThroughOtherBlocks()) {
                        if (ConfigHandler.debugLogging) {
                            final String msg = "Player: " + player.getName() + " broke a bed through blocks.";
                            this.log(player, data, data.nukerVL, msg);
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static ViolationLevelTracker newVL() {
        return new ViolationLevelTracker(1, 1, 1);
    }
}
