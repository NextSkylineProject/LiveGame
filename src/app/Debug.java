package app;

import java.awt.*;
import java.util.*;

public class Debug {
	private static final Color color = new Color(0);
	private static boolean on = Config.DEBUG;
	private static final HashMap<String, String> varList = new HashMap<>(20);
	
	public static void On(boolean on) {
		Debug.on = on;
	}
	
	public static boolean isOn() {
		return on;
	}
	
	public static void print(String message) {
		if (!on)
			return;
		System.out.println(message);
	}
	
	public static void showString(String name, String var) {
		if (!on)
			return;
		if (varList.containsKey(name)) {
			varList.replace(name, var);
		} else {
			varList.put(name, var);
		}
	}
	
	public static void removeString(String name) {
		varList.remove(name);
	}
	
	public static void draw(Graphics2D g) {
		if (!on || varList.isEmpty())
			return;
		g.setColor(color);
		
		int n = 0;
		for (Map.Entry<String, String> entry : varList.entrySet()) {
			n++;
			g.drawString("" + entry.getKey() + ": " + entry.getValue(), 2, 12 * n);
		}
	}
}
