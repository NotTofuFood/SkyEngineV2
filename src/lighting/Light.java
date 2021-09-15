package lighting;

import maths.ExtraMath;
import maths.ThreeValue;

public class Light {
	
	private int x;
	private int y;
	private int radius;
	
	private ThreeValue color;
	
	public float b;
	public float a;
	
	public Light(int x, int y, int radius, ThreeValue color) {
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.color = color;
		b = 1.0f / (radius*radius * 0.01f);
		a  = 0.f;
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getRadius() {
		return radius;
	}
	public void setRadius(int radius) {
		this.radius = radius;
	}
	public ThreeValue getColor() {
		return color;
	}
	public void setColor(ThreeValue color) {
		this.color = color;
	}
	
	public float calculateLighting(int px, int py) {
		float distance = (float) ExtraMath.fstsqrt(((x-px)*(x-px))+((y-py)*(y-py)));
		//System.out.println(((x-px)*(x-px))+((y-py)*(y-py)));
		//float attenuation = (1.0f / (1.0f + a*distance + b*distance*distance));
		float attenuation = (float) ExtraMath.clamp(1.0 - distance*distance/(radius*radius), 0.0, 1.0);
		attenuation*=attenuation;
		return attenuation;
	}
	
}
