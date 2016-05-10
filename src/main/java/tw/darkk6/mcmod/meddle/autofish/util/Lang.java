package tw.darkk6.mcmod.meddle.autofish.util;

import java.util.HashMap;

import tw.darkk6.meddle.api.util.APIUtil;

public class Lang {
	private static HashMap<String,HashMap<String,String>> langMap=new HashMap<String, HashMap<String,String>>();
	static{
		HashMap<String, String> enLang=new HashMap<String, String>();
		HashMap<String, String> twLang=new HashMap<String, String>();
		langMap.put("en_US", enLang);
		langMap.put("zh_TW", twLang);
		
		String key="msg.reload.config.done";
		enLang.put(key,"Reload config finished.");
		twLang.put(key,"設定檔重新載入完成");
		
		key="config.enable";
		enLang.put(key,"Enable AutoFish");
		twLang.put(key,"啟用 AutoFish");
		
		key="config.prevent";
		enLang.put(key,"Stop fishing when the fishing rod is going to broken");
		twLang.put(key,"釣竿損毀前收起，防止釣竿損壞");
		
		key="config.switch";
		enLang.put(key,"Switch rod when broken or is going to broken (in hotbar only)");
		twLang.put(key,"釣竿損壞或收起時前嘗試更換釣竿(僅在快捷列上的)");
		
		key="config.breakValue";
		enLang.put(key,"The durability to switch fishing rod (when prevent is enabled)");
		twLang.put(key,"要更換釣竿的耐久度(防止釣竿損毀啟用時)");
		
		key="config.maxDistance";
		enLang.put(key,"The max distane between sound played and fishing hook location (for multi-player)");
		twLang.put(key,"浮標與播放聲音最大可起桿距離(適合於多人一起釣魚)");
		
		key="config.checkDistance";
		enLang.put(key,"Enable distance check (for multi-player)");
		twLang.put(key,"是否檢查浮標與聲音的距離(適合於多人一起釣魚)");
		
		key="config.soundName";
		enLang.put(key,"Sound event name when caught fish");
		twLang.put(key,"釣到魚時所撥放的音效名稱");
		
	}
	
	public static String get(String key){
		String lang=APIUtil.getLanguage();
		if(!langMap.containsKey(lang)) lang="en_US";
		if(!langMap.get(lang).containsKey(key)) return key;
		return langMap.get(lang).get(key);
	}
}
