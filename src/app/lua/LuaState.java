package app.lua;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.jse.JseBaseLib;

public class LuaState {
	public static final String LUA_PATH = "app/luaScripts/";
	
	
	public static Globals loadFile(String fileName) {
		String path = LUA_PATH + fileName;
		System.out.println(path);
		Globals globals = new Globals();
		globals.load(new JseBaseLib());
		LoadState.install(globals);
		LuaC.install(globals);
		
		globals.loadfile(path).call();
		
		return globals;
	}
	
}
