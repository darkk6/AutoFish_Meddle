package tw.darkk6.mcmod.meddle.autofish;

import tw.darkk6.meddle.api.srg.SrgMap.FieldKey;
import tw.darkk6.meddle.api.srg.SrgMap.MethodKey;

public class NameMap {
	public static final MethodKey mGetDistance = MethodKey.get("getDistance", "(DDD)D","net/minecraft/entity/Entity");
	public static final FieldKey fFishEntity = FieldKey.get("fishEntity","Lnet/minecraft/entity/projectile/EntityFishHook;", "net/minecraft/entity/player/EntityPlayer");
}
