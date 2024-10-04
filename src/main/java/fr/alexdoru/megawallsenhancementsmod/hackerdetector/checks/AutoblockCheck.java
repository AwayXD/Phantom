package fr.alexdoru.megawallsenhancementsmod.hackerdetector.checks;

import fr.alexdoru.megawallsenhancementsmod.config.ConfigHandler;
import fr.alexdoru.megawallsenhancementsmod.hackerdetector.data.PlayerDataSamples;
import fr.alexdoru.megawallsenhancementsmod.hackerdetector.utils.ViolationLevelTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class AutoblockCheck extends Check {

    @Override
    public String getCheatName() {
        return "Autoblock";
    }

    @Override
    public String getCheatDescription() {
        return "The player can attack while their sword is blocked";
    }

    @Override
    public boolean canSendReport() {
        return true;
    }

    @Override
    public void performCheck(EntityPlayer player, PlayerDataSamples data) {
        super.checkViolationLevel(player, this.check(player, data), data.autoblockAVL);
    }

    @Override
    public boolean check(EntityPlayer player, PlayerDataSamples data) {
        if (data.hasSwung) {
            final ItemStack itemStack = player.getHeldItem();
            if (itemStack != null && itemStack.getItem() instanceof ItemSword) {
                // breaking blocks exemption
                if (isPlayerBreakingBlock(player)) {
                    return false;
                }

                if (data.useItemTime > 5) {
                    data.autoblockAVL.add(2);
                    if (ConfigHandler.debugLogging) {
                        this.log(player, data, data.autoblockAVL, " | useItemTime " + data.useItemTime);
                    }
                    return true;
                } else if (data.useItemTime == 0) {
                    data.autoblockAVL.substract(1);
                }
            }
        }
        return false;
    }

    private boolean isPlayerBreakingBlock(EntityPlayer player) {
        World world = player.worldObj;
        BlockPos pos = player.getPosition();
        return player.isSwingInProgress && world.getBlockState(pos).getBlock().getBlockHardness(world, pos) > 0;
    }

    public static ViolationLevelTracker newVL() {
        return new ViolationLevelTracker(2);
    }
}
