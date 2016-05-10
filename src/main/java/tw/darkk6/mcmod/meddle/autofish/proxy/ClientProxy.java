package tw.darkk6.mcmod.meddle.autofish.proxy;

import java.io.File;

import net.fybertech.meddle.Meddle;
import net.fybertech.meddleapi.MeddleClient;
import tw.darkk6.mcmod.meddle.autofish.EventHandler;
import tw.darkk6.mcmod.meddle.autofish.util.Config;
import tw.darkk6.mcmod.meddle.autofish.util.Reference;
import tw.darkk6.meddle.api.EventRegister;


public class ClientProxy extends CommonProxy{
	
	public EventHandler eventhandler = new EventHandler();
	
	@Override
	public void init(){
		Config.instance=new Config(new File(Meddle.getConfigDir(),Reference.MODID+".cfg"));
		EventRegister.addTickListener(eventhandler);
		EventRegister.addSoundPlayListener(eventhandler);
		MeddleClient.registerKeyBindStateHandler(eventhandler);
	}
}
