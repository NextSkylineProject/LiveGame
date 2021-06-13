package app.lua;

import app.Debug;
import app.menu.TemplateList;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

import java.util.ArrayList;

public class TemplateLoader {
	private static final String FILE_NAME = "Templates.lua";
	private static final ArrayList<TemplateList.Template> templates = new ArrayList<>(10);
	
	
	public static ArrayList<TemplateList.Template> load() {
		LuaState.addLib(new TemplateLuaLib());
		LuaState.loadFile(FILE_NAME);
		
		return templates;
	}
	
	private static final class TemplateLuaLib extends TwoArgFunction {
		Globals globals;
		
		@Override
		public LuaValue call(LuaValue __, LuaValue globals) {
			this.globals = globals.checkglobals();
			this.globals.set("RegisterTemplate", new LF_RegisterTemplate());
			this.globals.set("cell", new LF_cell());
			return null;
		}
		
		private static final class LF_RegisterTemplate extends TwoArgFunction {
			@Override
			public LuaValue call(LuaValue regNameVal, LuaValue regArrayVal) {
				Debug.print("Register template:" + regNameVal.toString());
				String name = regNameVal.toString();
				LuaTable luaTable = regArrayVal.checktable();
				TemplateList.Template newTemplate = new TemplateList.Template(name);
				
				for (int i = 1; i <= luaTable.length(); i++) {
					int x = luaTable.get(i).get("x").toint();
					int y = luaTable.get(i).get("y").toint();
					newTemplate.addCell(x, y);
				}
				
				templates.add(newTemplate);
				
				return null;
			}
		}
		
		private static final class LF_cell extends TwoArgFunction {
			@Override
			public LuaValue call(LuaValue x, LuaValue y) {
				LuaTable lt = new LuaTable();
				lt.set("x", x);
				lt.set("y", y);
				
				return lt;
			}
		}
	}
}
