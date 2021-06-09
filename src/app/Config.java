package app;

import app.lua.LuaState;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;


public class Config {
	public static int CELL_SIZE = 5;
	public static int WIDTH = 200;
	public static int HEIGHT = 150;
	public static int FRAME_WIDTH = 500;
	public static int FRAME_HEIGHT = 500;
	public static int FRAME_PER_SECOND = 5;
	public static boolean DEBUG = false;
	public static int MAX_FRAME_PER_SECOND = 200;
	
	public static final String FILE_NAME = "Config.lua";
	public static final String TABLE_NAME = "Config";
	public static Globals globals;
	
	public static void load() {
		globals = LuaState.loadFile(FILE_NAME);
		
		FRAME_PER_SECOND = getInt("FRAME_PER_SECOND");
		CELL_SIZE = getInt("CELL_SIZE");
		WIDTH = getInt("WIDTH");
		HEIGHT = getInt("HEIGHT");
		FRAME_WIDTH = getInt("FRAME_WIDTH");
		FRAME_HEIGHT = getInt("FRAME_HEIGHT");
		FRAME_PER_SECOND = getInt("FRAME_PER_SECOND");
		DEBUG = getBoolean("DEBUG");
		MAX_FRAME_PER_SECOND = getInt("MAX_FRAME_PER_SECOND");
	}
	
	private static LuaValue getVal(String name) {
		return globals.get(TABLE_NAME).get(name);
	}
	
	public static int getInt(String name) {
		return getVal(name).toint();
	}
	
	public static boolean getBoolean(String name) {
		return getVal(name).toboolean();
	}
	
	public static String getString(String name) {
		return getVal(name).toString();
	}
}
