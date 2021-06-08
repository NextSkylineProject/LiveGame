package app;

import java.awt.*;
import java.awt.geom.AffineTransform;


public class Camera {
	private Dimension screenSize;
	private double x = 0;
	private double y = 0;
	private double scale = 1.0f;
	private AffineTransform oldTransform;
	private static final double MIN_SCALE = 0.05d;
	private Point boundPos;
	private Dimension boundSize;
	
	public Camera(Dimension screenSize) {
		setScreenSize(screenSize);
	}
	
	public void transform(Graphics2D g) {
		oldTransform = g.getTransform();
		g.translate(screenSize.width * 0.5 - x * scale, screenSize.height * 0.5 - y * scale);
		g.scale(scale, scale);
		g.translate(0, 0);
	}
	
	public void restore(Graphics2D g) {
		if (oldTransform == null)
			return;
		g.setTransform(oldTransform);
	}
	
	public void setScreenSize(Dimension screenSize) {
		this.screenSize = screenSize;
	}
	
	public void setPos(double x, double y) {
		if (boundPos != null && boundSize != null) {
			if (screenSize.width < boundSize.width * scale) {
				x = Math.min(x, boundSize.width - screenSize.width * 0.5 / scale);
				x = Math.max(x, boundPos.x + screenSize.width * 0.5 / scale);
			} else {
				x = screenSize.width * 0.5 + (boundSize.width + boundPos.x - screenSize.width) * 0.5;
			}
			if (screenSize.height < boundSize.height * scale) {
				y = Math.min(y, boundSize.height - screenSize.height * 0.5 / scale);
				y = Math.max(y, boundPos.y + screenSize.height * 0.5 / scale);
			} else {
				y = screenSize.height * 0.5 + (boundSize.height + boundPos.y - screenSize.height) * 0.5;
			}
		}
		this.x = x;
		this.y = y;
	}
	
	public void setScale(double scale) {
		this.scale = Math.max(scale, MIN_SCALE);
	}
	
	public void updatePos() {
		setPos(x, y);
	}
	
	public void scale(double ds) {
		setScale(scale + ds);
	}
	
	public void move(double dx, double dy) {
		setPos(x + dx, y + dy);
	}
	
	public void moveTo(double targetX, double targetY, int speed) {
		double tx = approach(x, targetX, speed);
		double ty = approach(y, targetY, speed);
		setPos(tx, ty);
	}
	
	public void scaleTo(double tScale, double speed) {
		setScale(approach(scale, tScale, speed));
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
	
	public void setBounds(int x, int y, int w, int h) {
		this.boundPos = new Point(x, y);
		this.boundSize = new Dimension(w, h);
	}
	
	public double getScale() {
		return scale;
	}
	
	private double approach(double value, double targetValue, double incrementStep) {
		if (value == targetValue)
			return value;
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
	
	@Override
	public String toString() {
		return "Pos[" + x + "," + y + "], Scale[" + scale + "]";
	}
}
