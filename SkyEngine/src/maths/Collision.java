package maths;

import java.util.List;

import obj.Wall;
import rays.Camera;

public class Collision {
	
//	private int collision_box = 2;
	
	public void detectPlayerCollisions(Camera camera, List<Wall> walls, int wall_index, int ray_index) {
//		Point check_vert = ExtraMath.pointIntersection(camera.getX(), camera.getY()+collision_box+10, camera.getX(), camera.getY()-collision_box-10, walls.get(wall_index).getX1(), walls.get(wall_index).getY1()+10, walls.get(wall_index).getX2(), walls.get(wall_index).getY2()+10);
//		Point check_horz = ExtraMath.pointIntersection(camera.getX()-collision_box, camera.getY(), camera.getX()+collision_box, camera.getY(), walls.get(wall_index).getX1(), walls.get(wall_index).getY1(), walls.get(wall_index).getX2(), walls.get(wall_index).getY2());
//	    if(walls.get(wall_index).isPortal) {
//			if(check_vert != null || check_horz != null) {
//				camera.setRotation((float) walls.get(wall_index).getPortalID().getRotation());
//				camera.setX((int) walls.get(wall_index).getPortalID().getX());
//				camera.setY((int) walls.get(wall_index).getPortalID().getY());
//		    }
//	    }
//	    else {
//	    	if(check_vert != null || check_horz != null) {
//	    		if((check_horz != null)) {
//		    		camera.setX((int) (camera.getX()-Math.signum(camera.getLastHSP())));
//		    	} else if((check_vert != null)) {
//		    		camera.setY((int) (camera.getY()-Math.signum(camera.getLastVSP())));
//		    	}
//		    	camera.setHSP(0);
//		    	camera.setVSP(0);
//		    }	
//	    }
	}
	
}
