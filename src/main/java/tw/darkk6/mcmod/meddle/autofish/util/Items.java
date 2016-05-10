package tw.darkk6.mcmod.meddle.autofish.util;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;


public class Items {
	//似乎無法順利取得 fishingRod 的 Items 物件 , 這邊改由 name 來判定
	public static boolean isFishingRod(Item item){
		if(item==null) return false;
		ResourceLocation res=Item.itemRegistry.getNameForObject(item);
		if(res==null) return false;
		return "fishing_rod".equals(res.a());
	}
}
