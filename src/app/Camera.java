package app;

import java.awt.*;
import java.awt.geom.AffineTransform;

/*
TODO сделать ограничители камеры
 написать документацию
*/

public class Camera {
	private final Dimension screenSize;
	private double x = 0;
	private double y = 0;
	private double scale = 1.0f;
	private AffineTransform oldTransform;
	private static final double MIN_SCALE = 0.1d;
	
	public Camera(Dimension screenSize) {
		this.screenSize = screenSize;
	}
	
	public void transform(Graphics2D g) {
		oldTransform = g.getTransform();;
		g.translate(screenSize.width * 0.5 - x * scale, screenSize.height * 0.5 - y * scale);
		g.scale(scale, scale);
		g.translate(0, 0);
	}
	
	public void restore(Graphics2D g) {
		if (oldTransform == null) return;
		g.setTransform(oldTransform);
	}
	
	
	public void setPos(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void setScale(double scale) {
		this.scale = Math.max(scale, MIN_SCALE);
	}
	
	public void scale(double ds) {
		setScale(scale + ds);
	}
	
	public void move(double dx, double dy) {
		x += dx;
		y += dy;
	}
	
	public void moveTo(double tX, double tY, int speed) {
		x = approach(x, tX, speed);
		y = approach(y, tY, speed);
	}
	
	public void scaleTo(double tScale, double speed) {
		scale = approach(scale, tScale, speed);
	}
	
	public Point worldToScreen(int wx, int wy) {
		wx = (int) ((wx - x) * scale + screenSize.width * 0.5);
		wy = (int) ((wy - y) * scale + screenSize.height * 0.5);
		
		return new Point(wx, wy);
	}
	
	public Point screenToWorld(int sx, int sy) {
		sx = (int) ((sx - screenSize.width * 0.5) / scale + x);
		sy = (int) ((sy - screenSize.height * 0.5) / scale + y);
		
		return new Point(sx, sy);
	}
	
	@Override
	public String toString() {
		return "Pos[" + x + "," + y + "], Scale[" + scale + "]";
	}
	
	public double getScale() {
		return scale;
	}
	
	private double approach(double value, double targetValue, double incrementStep) {
		if (value == targetValue) return value;
		if (value < targetValue) {
			if ((value + incrementStep) > targetValue) {
				value = approach(value, targetValue, incrementStep / 2);
			} else {
				value += incrementStep;
			}
		}
		if (value > targetValue) {
			if ((value - incrementStep) < targetValue) {
				value = approach(value, targetValue, incrementStep / 2);
			} else {
				value -= incrementStep;
			}
		}
		
		return value;
	}
}
