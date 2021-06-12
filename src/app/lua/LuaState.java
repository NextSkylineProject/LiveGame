package app.lua;

import app.Debug;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

public class LuaState {
	public static final String LUA_PATH = "app/luaScripts/";
	private static Globals globals;
	
	public static void init() {
		globals = JsePlatform.standardGlobals();
	}
	
	public static Globals loadFile(String fileName) {
		String path = LUA_PATH + fileName;
		Debug.print("Lua file loaded:" + path);
		
		globals.loadfile(path).call();
		
		return globals;
	}
	
	public static void addLib(LuaValue lib) {
		globals.load(lib);
	}
	
	public static void addFunc(String funcName, LuaValue funcClass) {
		globals.set(funcName, funcClass);
	}
}
