package cn.ksmcbrigade.ats.mixin;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.LastSeenMessagesTracker;
import net.minecraft.network.chat.SignedMessageChain;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientPacketListener.class)
public interface PacketListenerMixin {
    @Accessor("signedMessageEncoder")
    SignedMessageChain.Encoder getMessageEncoder();

    @Accessor("lastSeenMessages")
    LastSeenMessagesTracker getLastMessages();
}
