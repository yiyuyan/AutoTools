package cn.ksmcbrigade.ats;

import net.minecraft.world.entity.player.Inventory;


//old
public class Utils {
    public static void setSlot(Inventory inv,int slot){
        int d = slot-inv.selected;
        if(d>0){
            for(int i=0;i<d;i++){
                inv.selected++;
            }
            if(inv.selected!=slot){
                inv.selected--;
            }
        }
        else{
            for(int i=0;i<-d;i++){
                inv.selected--;
            }
            if(inv.selected!=slot){
                inv.selected++;
            }
        }
    }
}
