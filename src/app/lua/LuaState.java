package app.lua;

import app.Debug;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.jse.JseBaseLib;

public class LuaState {
	public static final String LUA_PATH = "app/luaScripts/";
	private static Globals globals;
	
	public static void init() {
		globals = new Globals();
		globals.load(new JseBaseLib());
		LoadState.install(globals);
		LuaC.install(globals);
	}
	
	public static Globals loadFile(String fileName) {
		String path = LUA_PATH + fileName;
		Debug.print("Lua file loaded:" + path);
		
		globals.loadfile(path).call();
		
		return globals;
	}
	
}
