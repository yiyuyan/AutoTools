package cn.ksmcbrigade.ats;

import cn.ksmcbrigade.ats.mixin.PacketListenerMixin;
import cn.ksmcbrigade.vmr.command.Command;
import cn.ksmcbrigade.vmr.module.Module;
import cn.ksmcbrigade.vmr.uitls.CommandUtils;
import cn.ksmcbrigade.vmr.uitls.ModuleUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.LastSeenMessagesTracker;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.network.chat.SignedMessageBody;
import net.minecraft.network.protocol.game.ServerboundChatPacket;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.util.Crypt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.time.Instant;
import java.util.Arrays;
import java.util.Objects;

@Mod(AutoTools.MODID)
@Mod.EventBusSubscriber
public class AutoTools {

    public static final String MODID = "ats";

    public AutoTools() {
        MinecraftForge.EVENT_BUS.register(this);

        Arrays.stream(Modules.class.getDeclaredFields())
                .filter(f -> f.getType().equals(Module.class))
                        .toList()
                                .forEach(m -> {
                                    try {
                                        System.out.println(m.getName());
                                        ModuleUtils.add((Module) m.get(null));
                                    } catch (Exception e) {
                                        System.out.println("11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111112222222222222222222222222222222222222222222222222222222222222222223333333333333333333333333333333333333333333333333333333333333333333333344444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444466666666666666666666666666666666677777777777777777777777777777777777777778888888888888888888888888888889999999999999999999999999999999999999999999999999999999999999900000000000000000000000000000000000:error: "+e.getMessage());
                                        e.printStackTrace();
                                    }
                                });
        /*ModuleUtils.add(Modules.AC);
        ModuleUtils.add(Modules.AM);
        ModuleUtils.add(Modules.AP);
        ModuleUtils.add(Modules.AZ);
        ModuleUtils.add(Modules.AS);
        ModuleUtils.add(Modules.AT);*/

        //fix bug
        CommandUtils.add(new Command("say",1) {
            @Override
            public void onCommand(Minecraft MC, Player player, String[] args) throws Exception{
                StringBuilder chat = new StringBuilder();
                for(String arg:args){
                    if(chat.toString().equals("")){
                        chat = new StringBuilder(arg);
                    }
                    else{
                        chat.append(" ").append(arg);
                    }
                }
                if (chat.toString().isEmpty()) return;
                ClientPacketListener connection = MC.getConnection();
                if(connection==null) return;
                Instant instant = Instant.now();
                long i = Crypt.SaltSupplier.getLong();
                LastSeenMessagesTracker.Update lastseenmessagestracker$update = ((PacketListenerMixin)connection).getLastMessages().generateAndApplyUpdate();
                MessageSignature messagesignature = ((PacketListenerMixin)connection).getMessageEncoder().pack(new SignedMessageBody(chat.toString(), instant, i, lastseenmessagestracker$update.lastSeen()));
                connection.send(new ServerboundChatPacket(chat.toString(), instant, i, messagesignature, lastseenmessagestracker$update.update()));
            }
        });
    }

    @SubscribeEvent
    public static void onRightInput(PlayerInteractEvent.RightClickEmpty event){
        if(Minecraft.getInstance().options.keyUse.isDown()){
            /*System.out.println("1111111111111111111111");
            System.out.println(ModuleUtils.enabled("hack.name.ap"));*/
            if(ModuleUtils.enabled("hack.name.ap")){
                Minecraft MC =Minecraft.getInstance();
                Player player = MC.player;
                /*System.out.println(player!=null);*/
                    if (player != null) {
                        /*System.out.println(Minecraft.getInstance().hitResult != null);*/
                        if (Minecraft.getInstance().hitResult != null) {
                            Objects.requireNonNull(MC.getConnection()).getConnection().send(new ServerboundUseItemOnPacket(player.getUsedItemHand(), (BlockHitResult) Minecraft.getInstance().hitResult,3));
                            MC.getConnection().getConnection().send(new ServerboundSwingPacket(player.getUsedItemHand()));
                            /*System.out.println("2222222222222222222222222");*/
                        }
                    }
            }
        }
    }
}
