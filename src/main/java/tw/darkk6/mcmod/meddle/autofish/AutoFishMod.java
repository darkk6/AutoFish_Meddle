package tw.darkk6.mcmod.meddle.autofish;

import net.fybertech.meddle.MeddleMod;
import net.fybertech.meddleapi.MeddleAPI;
import net.fybertech.meddleapi.side.ClientOnly;
import tw.darkk6.mcmod.meddle.autofish.proxy.ClientProxy;
import tw.darkk6.mcmod.meddle.autofish.proxy.CommonProxy;
import tw.darkk6.mcmod.meddle.autofish.util.Reference;
import tw.darkk6.meddle.api.ClientEventAPI;

@ClientOnly
@MeddleMod(depends={"dynamicmappings", "meddleapi","clienteventapi"},id=Reference.MODID, name=Reference.MOD_NAME, version=Reference.MOD_VER, author="darkk6")
public class AutoFishMod {
	public static CommonProxy proxy = (CommonProxy) MeddleAPI.createProxyInstance(CommonProxy.class.getName(), ClientProxy.class.getName());
	
	public void init(){
		ClientEventAPI.checkApiVersionWithException("1.1");
		proxy.init();
	}
}
