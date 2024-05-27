package fr.alexdoru.megawallsenhancementsmod.chat;

import net.minecraft.util.ResourceLocation;

public class SkinChatHead {

    private ResourceLocation skin;

    public SkinChatHead(ResourceLocation skin) {
        this.skin = skin;
    }

    public ResourceLocation getSkine() {
        return skin;
    }

    public void setSkine(ResourceLocation skin) {
        this.skin = skin;
    }

}
