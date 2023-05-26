package obj;

import main.Window;
import maths.Point;
import maths.ThreeValue;

public class Positional_Vert {
	
	private ThreeValue pos;
	
	private ThreeValue theta;
	
	public Positional_Vert(ThreeValue pos, ThreeValue theta) {
		this.pos = pos;
		this.theta = theta;
	}
	
	public ThreeValue getPos() {
		return this.pos;
	}
	
	public Point update(ThreeValue camera_pos, ThreeValue theta, double projection_plane) {
		Point screen_space = new Point(-1,-1);
		
		double x = Math.cos(theta.getY()) * ((Math.sin(theta.getZ()) * pos.getX()-camera_pos.getX()) + (Math.cos(theta.getZ()) * pos.getY()-camera_pos.getY())) - (Math.sin(theta.getY())) * pos.getZ()-camera_pos.getZ();
		
		//wtf, i am sorry
		double y1 = Math.cos(theta.getY()) * pos.getZ()-camera_pos.getZ();
		double y2 = Math.sin(theta.getY()) * ((Math.sin(theta.getZ()) * pos.getY()-camera_pos.getY()) + (Math.cos(theta.getZ()) * pos.getX() - camera_pos.getX()));
		
		double y3 = (Math.cos(theta.getX()) * (Math.cos((theta.getZ()) * pos.getY()-camera_pos.getY())) - Math.sin(theta.getZ()) * pos.getX()-camera_pos.getX());
		
		double y = (Math.sin(theta.getX()) * (y1 + y2)) + y3; 

		// :(
		double z1 = Math.cos(theta.getY()) * pos.getZ()-camera_pos.getZ();
		double z2 = Math.sin(theta.getY()) * ((Math.sin(theta.getZ()) * pos.getY()-camera_pos.getY()) + (Math.cos(theta.getZ()) * pos.getX() - camera_pos.getX()));
		
		double z3 = (Math.sin(theta.getX()) * (Math.cos((theta.getZ()) * pos.getY()-camera_pos.getY())) - Math.sin(theta.getZ()) * pos.getX()-camera_pos.getX());
		
		double z = ((Math.cos(theta.getX())) * (z1 + z2)) - z3;
		
		double dx = (Math.cos(y) * ((Math.sin(z)*y) + (Math.cos(z)*x))) -(Math.sin(y)*z);
		double dy = (Math.sin(x) * ((Math.cos(y)*z) + (Math.sin(y) * (Math.sin(z)*y)+(Math.cos(z)*x)))) + Math.cos(x)*((Math.cos(z)*y)-(Math.sin(x)*x));
		double dz = Math.cos(x) * ((Math.cos(y)*z) + (Math.sin(y) * ((Math.sin(z)*y)+(Math.cos(z)*x)))) - Math.sin(x) * ((Math.cos(z)*y)-(Math.sin(z)*x));
		
		double bx = (dx*Math.sin(x))/((dz*Window.WIDTH)*projection_plane);
		double by = (dy*Math.sin(y))/((dz*Window.HEIGHT)*projection_plane);
		
		screen_space.setX(bx);
		screen_space.setY(by);
		
		return screen_space;
	}
	
}
