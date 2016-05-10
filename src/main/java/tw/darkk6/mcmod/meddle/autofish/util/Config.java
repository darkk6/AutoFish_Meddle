package tw.darkk6.mcmod.meddle.autofish.util;

import java.io.File;

import net.fybertech.meddleapi.ConfigFile;

//不知道為何，會一直有奇怪的錯誤，會一直複製資料 , 只好改成每次 reload 都建立一個新的 ConfigFile 實體 
public class Config {
	//使用道具(預設為滑鼠右鍵)的  Key Name
	public static final String INTERACT_KEY_NAME = "key.use";
	
	public static boolean isEnable=true,isPrevent=true,isSwitch=true,isCheckDistance=false;
	public static int breakValue=5;
	public static double maxDistance=1.7D;
	public static String soundName="entity.bobber.splash";
	
	public static Config instance=null;
	
	private long lastModify=0L;
	private File file;
	
	public Config(File file){
		this.file=file;
		reload();
		lastModify = file.lastModified();
	}
	
	public boolean update(){
		if(lastModify != file.lastModified()){
			reload();
			lastModify = file.lastModified();
			return true;
		}
		return false;
	}
	
	private void reload(){
		ConfigFile cfg=new ConfigFile(file);
		cfg.load();
		
		
		
		isEnable=((Boolean)cfg.get(ConfigFile.key(
				"general", "enableAutofish", Boolean.valueOf(isEnable),
				Lang.get("config.enable")))).booleanValue();
		
		isPrevent=((Boolean)cfg.get(ConfigFile.key(
				"general", "preventRodBreak", Boolean.valueOf(isPrevent), 
				Lang.get("config.prevent")))).booleanValue();
		
		isSwitch=((Boolean)cfg.get(ConfigFile.key(
				"general", "switchRod", Boolean.valueOf(isSwitch),
				Lang.get("config.switch")))).booleanValue();
		
		breakValue=((Integer)cfg.get(ConfigFile.key(
				"general","preventBreakValue",Integer.valueOf(breakValue),
				Lang.get("config.breakValue")))).intValue();
		
		maxDistance=((Double)cfg.get(ConfigFile.key(
				"general","maxDistance",Double.valueOf(maxDistance),
				Lang.get("config.maxDistance")))).doubleValue();
		
		isCheckDistance=((Boolean)cfg.get(ConfigFile.key(
				"general", "checkDistanceEnable", Boolean.valueOf(isCheckDistance), 
				Lang.get("config.checkDistance")))).booleanValue();
		
		
		soundName=cfg.get(ConfigFile.key(
				"internal", "soundEventName", soundName, 
				Lang.get("config.soundName")));
		
		if(cfg.hasChanged()) cfg.save();
	}
}
