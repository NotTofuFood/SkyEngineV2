package obj;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import maths.ExtraMath;
import maths.Point;
import maths.ThreeValue;
import rays.Camera;

public class ModelLoader {
	
	private List<Positional_Vert> verts = new ArrayList<>();
	
	private ThreeValue pos;
	
	private double size;

	public ModelLoader(List<Positional_Vert> verts, ThreeValue pos, double size) {
		this.verts = verts;
		this.pos = pos;
		this.size = size;
	}
	
	public Point getPosition() {
		return new Point(pos.getX(), pos.getZ());
	}
	
	public void setSize(double size) {
		this.size = size;
	}
	
	public double getSize() {
		return size;
	}
	
	public void ModelRender(Graphics2D g, Camera c, double ray_distance, double projection_plane) {
		for(int i = 0; i < verts.size(); i++) {
			int color = (int) ExtraMath.clamp(Math.abs((int)ray_distance-255),0,255);
			g.setColor(new Color(color,color,color));
			pos = new ThreeValue(pos.getX(),0,pos.getZ());
			if(i % 3 == 0 && i != 0) {
				double vert_x = (this.verts.get(i).getPos().getX()*size) + this.pos.getX();
				double vert_y = (this.verts.get(i).getPos().getY()*size) + this.pos.getY();//(this.pos.getX()-this.pos.getZ());
				double vert_z = (this.verts.get(i).getPos().getZ()*size) + this.pos.getZ();
				// Fix projection plane and ray_distance, make it relevant to ray hitting the object itself
				//Point d = this.verts.get(i).update(new ThreeValue(c.getX(), 1, c.getY()), new ThreeValue(vert_x, vert_y, vert_z), new ThreeValue(1,1,1), projection_plane, ray_distance);
				double vert_x1 = (this.verts.get(i-1).getPos().getX()*size) + this.pos.getX();
				double vert_y1 = (this.verts.get(i-1).getPos().getY()*size) + this.pos.getY();//(this.pos.getX()-this.pos.getZ());
				double vert_z1 = (this.verts.get(i-1).getPos().getZ()*size) + this.pos.getZ();
				
				// Fix projection plane and ray_distance, make it relevant to ray hitting the object itself
			//	Point d1 = this.verts.get(i).update(new ThreeValue(c.getX(), 1, c.getY()), new ThreeValue(vert_x1, vert_y1, vert_z1), new ThreeValue(1,1,1), projection_plane, ray_distance);
				double vert_x2 = (this.verts.get(i-2).getPos().getX()*size) + this.pos.getX();
				double vert_y2 = (this.verts.get(i-2).getPos().getY()*size) + this.pos.getY();//(this.pos.getX()-this.pos.getZ());
				double vert_z2 = (this.verts.get(i-2).getPos().getZ()*size) + this.pos.getZ();
		
				// Fix projection plane and ray_distance, make it relevant to ray hitting the object itself
				//Point d2 = this.verts.get(i).update(new ThreeValue(c.getX(), 1, c.getY()), new ThreeValue(vert_x2, vert_y2, vert_z2), new ThreeValue(1,1,1), projection_plane, ray_distance);

				int y_int = (int)230;
				int y_multiply = 8;
				int x_multiply = 3;
			//	g.drawLine((int)d.getX()*x_multiply, (int)(d.getY()*y_multiply)+y_int, (int)d1.getX()*x_multiply, (int)(d1.getY()*y_multiply)+y_int);
			//	g.drawLine((int)d.getX()*x_multiply, (int)(d.getY()*y_multiply)+y_int, (int)d2.getX()*x_multiply, (int)(d2.getY()*y_multiply)+y_int);
			///	g.drawLine((int)d2.getX()*x_multiply, (int)(d2.getY()*y_multiply)+y_int, (int)d1.getX()*x_multiply, (int)(d1.getY()*y_multiply)+y_int);
			}
		}
	}
	
}
