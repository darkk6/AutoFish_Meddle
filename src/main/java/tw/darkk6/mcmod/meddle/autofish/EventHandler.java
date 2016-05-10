package tw.darkk6.mcmod.meddle.autofish;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.fybertech.meddleapi.MeddleClient.IKeyBindingState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MainOrOffHand;
import tw.darkk6.mcmod.meddle.autofish.util.Config;
import tw.darkk6.mcmod.meddle.autofish.util.Items;
import tw.darkk6.mcmod.meddle.autofish.util.Lang;
import tw.darkk6.mcmod.meddle.autofish.util.Reference;
import tw.darkk6.meddle.api.event.SoundEvent;
import tw.darkk6.meddle.api.listener.ISoundListener;
import tw.darkk6.meddle.api.listener.ITickListener;
import tw.darkk6.meddle.api.util.APILog;
import tw.darkk6.meddle.api.util.APIUtil;

// 處理透過 transformer 轉換的事件
public class EventHandler implements ITickListener,ISoundListener,IKeyBindingState{
	
	@Override
	public void onsetKeyBindState(int keycode, boolean state, KeyBinding keybinding) {
		//當拿著釣竿按下滑鼠右鍵的時候(使用道具)，重新載入設定檔
		if( (!state) || keybinding==null) return;
		// keybinding.h() => getDescription
		if(Config.INTERACT_KEY_NAME.equals(keybinding.h())){
			try{
				EntityPlayerSP player=Minecraft.getMinecraft().thePlayer;
				ItemStack mHand=player.getHeldMainHandItem();
				ItemStack oHand=player.getHeldOffHandItem();
				
				if(	( mHand!=null && Items.isFishingRod(mHand.getItem())) ||
					( mHand==null && oHand!=null && Items.isFishingRod(oHand.getItem()))
				){
					if(Config.instance.update())
						APILog.infoChat(Lang.get("msg.reload.config.done"),Reference.LOG_TAG);
				}
			}catch(Exception e){
				APILog.error(e.getMessage(),Reference.LOG_TAG);
			}
		}
	}
	
	
	private boolean iGotFish=false;
	private static final int TICK_LEN_BETWEEN_RIGHT_CLICK=25;
	private boolean isFishing=false;
	private long lastSendms=-1;
	
	@Override
	public void onTickEnd(){
		Minecraft minecraft = Minecraft.getMinecraft();
		//mc.isGamePaused() => mc.T()
		if (!minecraft.T() && minecraft.thePlayer != null) {
			EntityPlayer player = minecraft.thePlayer;
			if (Config.isEnable) {
				if (Config.isSwitch && isFishing && hasSentRightClick() && !canSendRightClick(player)) {
					switchFishingRod(player);
				}
				if (canSendRightClick(player)) {
					isFishing = true;
					if (getPlayerFishEntity(player) != null) {
						if (iGotFish) {
							if(APIUtil.getPlayerController()!=null){
								APIUtil.getPlayerController().processRightClick(player, minecraft.theWorld, player.getHeldMainHandItem(),MainOrOffHand.MAIN_HAND);
								//P() => getTotalWorldTime()
								lastSendms = minecraft.theWorld.P();
								iGotFish=false;
							}
						}
					//==== 這邊底下都是 fishEntity = null ====
					} else if (hasSentRightClick() && minecraft.theWorld.P() > lastSendms + TICK_LEN_BETWEEN_RIGHT_CLICK) {
						//沒有揮桿出去，但之前 autofish 有送出過拉桿訊息，且和上次送出訊息時間差距 QUEUE_TICK_LENGTH 個 tick 以上
						//再把竿子丟出去
						if(APIUtil.getPlayerController()!=null){
							APIUtil.getPlayerController().processRightClick(player, minecraft.theWorld, player.getHeldMainHandItem(),MainOrOffHand.MAIN_HAND);
							lastSendms = -1;
							iGotFish=false;
						}
					} else if( !hasSentRightClick() && minecraft.theWorld.P() > lastSendms + TICK_LEN_BETWEEN_RIGHT_CLICK ){
						//如果已經超過間隔時間，而且之前沒有透過 mod 送出 right Click , 應該是使用者自行取消了
						// Nothing for now
					}
					//其他如等到魚上鉤的時間... 都沒做任何事情
				} else if (isFishing) {
					//手上不是拿釣竿，或者設定不再使用時，重設所有資料
					isFishing = false;
					lastSendms = -1;
					iGotFish=false;
				}
			}
		}
	}
	
	@Override
	public void onSoundPlay(SoundEvent evt){
		if(!Config.soundName.equals(evt.name)) return;
		//確認一下是自己抓到的，理論上沒問題
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.T() || mc.thePlayer == null) return;
		EntityPlayer player = mc.thePlayer;
		Object fishEntity=getPlayerFishEntity(player);
		if(fishEntity==null) return;
		if(Config.isCheckDistance){
			//player.fishEntity.getDistance()
			double dist=entityGetDistance(fishEntity,
						evt.sound.getXPosF(),
						evt.sound.getYPosF(),
						evt.sound.getZPosF()
					);
			iGotFish = ( dist <= Config.maxDistance );
		}else{
			iGotFish=true;
		}
	}

	private boolean hasSentRightClick(){
		return lastSendms > 0L;
	}
	
	private void switchFishingRod(EntityPlayer player){
		InventoryPlayer inventory = player.inventory;
		//只搜尋 hotbar
		for (int i = 0; i < 9; i++) {
			// mainInventory => a , 陣列元素為 36 的那個
			ItemStack item = inventory.a[i];
			if (item != null && Items.isFishingRod(item.getItem()) && canUseThisRod(item)){
				//這個道具是釣竿，且可以使用 , currentItem => d , 是 int 的那個
				inventory.d = i;
				break;
			}
		}
	}
	
	private boolean canSendRightClick(EntityPlayer player){
		if(!isHoldingRod(player)) return false;
		ItemStack item=player.getHeldMainHandItem();
		return Config.isEnable
				&& item.getItemDamage() <= item.getMaxDamage()
				&& canUseThisRod(item);
	}
	
	private boolean canUseThisRod(ItemStack item){
		int durability=item.getMaxDamage() - item.getItemDamage();
		return (!Config.isPrevent) || durability> Config.breakValue;
	}
	
	private boolean isHoldingRod(EntityPlayer player){
		ItemStack item=player.getHeldMainHandItem();
		if(item==null) return false;
		return Items.isFishingRod(item.getItem());
	}
	
	@Override
	public void onTickStart(){}
	
	@SuppressWarnings("rawtypes")
	private Object getPlayerFishEntity(EntityPlayer player){
		try{
			Class cls=EntityPlayer.class;
			Field fishEntity=cls.getField("bP");//fishEntity
			Object result=fishEntity.get(player);
			return result;
		}catch(Exception e){
			APILog.error("Can NOT get fishEntity",Reference.LOG_TAG);
			return null;
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private double entityGetDistance(Object entity,double x,double y,double z){
		try{
			Class cls=Entity.class;
			// getDistance(DDD)D
			Method getDistence=cls.getMethod("f",double.class,double.class,double.class);
			double result=(double)getDistence.invoke(entity,x,y,z);
			return result;
		}catch(Exception e){
			APILog.error("Can NOT get distance",Reference.LOG_TAG);
			return 0.0D;
		}
	}
}
