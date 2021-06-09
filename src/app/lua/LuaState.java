package app.lua;

import app.luaScripts.MyLuaLib;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.jse.JsePlatform;

public class LuaState {
	public static final String LUA_PATH = "app/luaScripts/";
	
	
	public static void loadConfig() {
		String path = LUA_PATH + "Config.lua";
		Globals globals = new Globals();
		LoadState.install(globals);
		LuaC.install(globals);
		
		globals.loadfile(path).call();
		
		LuaValue lv = globals.get("Config");
		System.out.println(lv);
	}
	
	
	// TEST
	public static void main(String[] args) {
		String path = "app/luaScripts/Config.lua";
		
		Globals globals = JsePlatform.standardGlobals();
		
		globals.load(new MyLuaLib());
		
		globals.loadfile(path).call();
		
		LuaValue lv = globals.get("Config");
		System.out.println(lv);
//		lv.call();
	}
	
	
}
