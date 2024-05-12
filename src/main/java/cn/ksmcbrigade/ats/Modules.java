package cn.ksmcbrigade.ats;

import cn.ksmcbrigade.ats.mixin.MinecraftMixin;
import cn.ksmcbrigade.vmr.module.Config;
import cn.ksmcbrigade.vmr.module.Module;
import cn.ksmcbrigade.vmr.uitls.JNAUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.jna.platform.win32.WinDef;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundPickItemPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Objects;
import java.util.Random;

public class Modules {

    public static Module AT = new Module("hack.name.at"){

        public int last = -1;

        @Override
        public void playerTick(Minecraft MC, @Nullable Player player) throws Exception{
            if(player!=null){
                if(last!=-1){
                    Objects.requireNonNull(MC.getConnection()).getConnection().send(new ServerboundPickItemPacket(last));
                    last = -1;
                }
                if(player.getOffhandItem().getItem().equals(Items.TOTEM_OF_UNDYING)){
                    return;
                }
                if(!player.getOffhandItem().isEmpty()){
                    return;
                }
                for(int i=0;i<player.getInventory().getMaxStackSize();i++){
                    if(player.getInventory().getItem(i).getItem().equals(Items.TOTEM_OF_UNDYING)){
                        if(i<9){
                            final int last = player.getInventory().selected;
                            player.getInventory().selected = i;
                            Objects.requireNonNull(MC.getConnection()).getConnection().send(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ZERO, Direction.DOWN));
                            player.getInventory().selected = last;
                            //MC.getConnection().getConnection().send(new ServerboundSetCarriedItemPacket(last));
                        }
                        else{
                            Objects.requireNonNull(MC.getConnection()).getConnection().send(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ZERO, Direction.DOWN));
                            MC.getConnection().getConnection().send(new ServerboundPickItemPacket(i));
                            MC.getConnection().getConnection().send(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ZERO, Direction.DOWN));
                        }
                        last = i;
                        break;
                    }
                }
            }
        }

        @Override
        public String getName() {
            int t = getTotem();
            return t==0?super.getName():super.getName()+" ["+t+"]";
        }

        public int getTotem(){
            int i =0;
            if(Minecraft.getInstance().player==null){
                return i;
            }
            for(ItemStack item:Minecraft.getInstance().player.getInventory().items){
                if(item.getItem().equals(Items.TOTEM_OF_UNDYING)){
                    i++;
                }
            }
            if(Minecraft.getInstance().player.getOffhandItem().getItem().equals(Items.TOTEM_OF_UNDYING)){
                i++;
            }
            return i;
        }
    };

    public static Module AC = new Module("hack.name.ac"){
       /*
        0: Left Click
        1: Right Click
        3: Medium Click       */
        public int mode = 0;

        /*
        true: Click
        false: Down           */
        public boolean click = true;

        public int sleepZ;
        public int sleep = 7;

        @Override
        public void enabled(Minecraft MC) throws Exception {
            if(getConfig()==null){
                JsonObject object = new JsonObject();
                object.addProperty("mode",0);
                object.addProperty("click",true);
                object.addProperty("sleep",7);
                setConfig(new Config(new File("AutoClick"),object));
            }
            Random rand = new Random();
            this.mode = Objects.requireNonNull(getConfig().get("mode")).getAsInt();
            this.click = Objects.requireNonNull(getConfig().get("click")).getAsBoolean();
            int r = rand.nextBoolean()?(Objects.requireNonNull(getConfig().get("sleep")).getAsInt()+rand.nextInt(4)):(Objects.requireNonNull(getConfig().get("sleep")).getAsInt()-rand.nextInt(4));
            this.sleep = r;
            this.sleepZ = r;
        }

        @Override
        public void playerTick(Minecraft MC, @Nullable Player player) {
            if(player!=null && MC.gameMode!=null){
                if(sleep!=0){
                    sleep--;
                    return;
                }
                switch (mode){
                    case 0:
                        if (click) {
                            if(JNAUtils.JNAInstance.INSTANCE!=null){
                                WinDef.POINT pos = new WinDef.POINT();
                                JNAUtils.JNAInstance.INSTANCE.GetCursorPos(pos);
                                JNAUtils.JNAInstance.INSTANCE.mouse_event(2,pos.x,pos.y,0,0);
                                JNAUtils.JNAInstance.INSTANCE.mouse_event(4,pos.x,pos.y,0,0);
                            }
                        } else {
                            KeyMapping.set(MC.options.keyAttack.getKey(),true);
                            MC.options.keyAttack.setDown(true);
                        }
                    case 1:
                        if (click) {
                            if(JNAUtils.JNAInstance.INSTANCE!=null){
                                WinDef.POINT pos = new WinDef.POINT();
                                JNAUtils.JNAInstance.INSTANCE.GetCursorPos(pos);
                                JNAUtils.JNAInstance.INSTANCE.mouse_event(8,pos.x,pos.y,0,0);
                                JNAUtils.JNAInstance.INSTANCE.mouse_event(10,pos.x,pos.y,0,0);
                            }
                        } else {
                            KeyMapping.set(MC.options.keyUse.getKey(),true);
                            MC.options.keyUse.setDown(true);
                        }
                    case 2:
                        if (click) {
                            if(JNAUtils.JNAInstance.INSTANCE!=null){
                                WinDef.POINT pos = new WinDef.POINT();
                                JNAUtils.JNAInstance.INSTANCE.GetCursorPos(pos);
                                JNAUtils.JNAInstance.INSTANCE.mouse_event(32,pos.x,pos.y,0,0);
                                JNAUtils.JNAInstance.INSTANCE.mouse_event(64,pos.x,pos.y,0,0);
                            }
                        } else {
                            KeyMapping.set(MC.options.keyPickItem.getKey(),true);
                            MC.options.keyPickItem.setDown(true);
                        }
                    default:
                        if (click) {
                            if(JNAUtils.JNAInstance.INSTANCE!=null){
                                WinDef.POINT pos = new WinDef.POINT();
                                JNAUtils.JNAInstance.INSTANCE.GetCursorPos(pos);
                                JNAUtils.JNAInstance.INSTANCE.mouse_event(2,pos.x,pos.y,2,0);
                                JNAUtils.JNAInstance.INSTANCE.mouse_event(4,pos.x,pos.y,2,0);
                            }
                        } else {
                            KeyMapping.set(MC.options.keyAttack.getKey(),true);
                            MC.options.keyAttack.setDown(true);
                        }
                }
                sleep = sleepZ;
            }
        }

        @Override
        public void disabled(Minecraft MC) {
            KeyMapping.releaseAll();
        }
    };

    public static Module AS = new Module("hack.name.as"){
        @Override
        public void playerTick(Minecraft MC, @Nullable Player player) {
            MC.options.keySprint.setDown(true);
        }

        @Override
        public void disabled(Minecraft MC) {
            MC.options.keySprint.setDown(false);
        }
    };

    public static Module AZ = new Module("hack.name.az",false, KeyEvent.VK_Z){
        @Override
        public void playerTick(Minecraft MC, @Nullable Player player) {
            MC.options.keyShift.setDown(true);
        }

        @Override
        public void disabled(Minecraft MC) {
            MC.options.keyShift.setDown(false);
        }
    };

    public static Module AM = new Module("hack.name.am"){
        @Override
        public void playerTick(Minecraft MC, @Nullable Player player) {
            KeyMapping.set(MC.options.keyAttack.getKey(),true);
            MC.options.keyAttack.setDown(true);
        }

        @Override
        public void disabled(Minecraft MC) {
            MC.options.keyAttack.setDown(false);
        }
    };

    public static Module ASS = new Module("hack.name.ass"){

        @Override
        public void playerTick(Minecraft MC, @Nullable Player player) {
            if(player!=null){
                if(player.getInventory().selected==8){
                    player.getInventory().selected=0;
                }
                else{
                    player.getInventory().selected++;
                }
            }
        }
    };

    public static Module ASW = new Module("hack.name.asw"){

        @Override
        public void playerTick(Minecraft MC, @Nullable Player player) {
            if(player==null){
                return;
            }
            if(player.horizontalCollision || MC.options.keyShift.isDown()){
                return;
            }
            if(!player.isInWater()){
                return;
            }
            if(player.getSwimAmount(0)>0){
                player.setSprinting(true);
            }
        }
    };

    public static Module AD = new Module("hack.name.ad"){

        public ServerData info = null;
        public int timer = 100; //5s

        @Override
        public void enabled(Minecraft MC) throws Exception {
            if(getConfig()==null){
                JsonObject object = new JsonObject();
                object.addProperty("tick",100);
                setConfig(new Config(new File("AutoReconnect"),object));
            }
            this.timer = Objects.requireNonNull(getConfig().get("tick")).getAsInt();
        }

        @Override
        public void clientTick(Minecraft MC) {
            if(Objects.requireNonNull(MC.getConnection()).getConnection().isConnected()){
                info = MC.getConnection().getServerData();
            }
        }

        @Override
        public void screenTick(Minecraft MC) {
            if(MC.screen instanceof DisconnectedScreen){
                if(timer==0){
                    if(info!=null){
                        ConnectScreen.startConnecting(new TitleScreen(),MC, ServerAddress.parseString(info.ip), info, false);
                        info = null;
                    }

                    timer = Objects.requireNonNull(getConfig().get("tick")).getAsInt();
                }
                else{
                    timer--;
                }
            }
        }
    };

    public static Module AL = new Module("hack.name.al"){

        @Override
        public void enabled(Minecraft MC) throws Exception {
            if(getConfig()==null){
                JsonObject object = new JsonObject();
                object.addProperty("hea",3.0F);
                setConfig(new Config(new File("AutoLeave"),object));
            }
        }

        @Override
        public void playerTick(Minecraft MC, @Nullable Player player) throws Exception {
            JsonElement e = getConfig().get("hea");
            float hea = e!=null?e.getAsFloat():3.0F;
            if(player!=null && player.getHealth()!=0 && player.getHealth()<=hea){
                player.level().disconnect();
                setEnabled(false);
            }
        }
    };

    public static Module AR = new Module("hack.name.ar"){

        @Override
        public void playerTick(Minecraft MC, @Nullable Player player) {
            if(player!=null && !player.isAlive()){
                player.respawn();
            }
        }
    };

    //other
    //for dev
    public static Module AP = new Module("hack.name.ap");

    public static Module FP = new Module("hack.name.fp"){
        @Override
        public void playerTick(Minecraft MC, @Nullable Player player) {
            ((MinecraftMixin)MC).setRightDelay(0);
        }
    };

}
