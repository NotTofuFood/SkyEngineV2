package maths;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import obj.Positional_Vert;

public class ExtraMath {

	public static double distance(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
	}

	public static double clamp(double value, double min, double max) {
		return Math.min(Math.max(value, min), max);
	}
	
    public static double lerp(double a, double b, double f) {
        return a + f * (b - a);
    }

    public static double smoothstep(double edge0, double edge1, double x) {
    	x = clamp((x - edge0) / (edge1 - edge0), 0.0, 1.0); 
    	return x * x * (3 - 2 * x);
    }
    

    
	public static int blend_color(int color1, int color2, double ratio) {
	    ratio = clamp(ratio, 0, 1);
	    double iRatio = 1.0f - ratio;

	    int r1 = ((color1 & 0xff0000) >> 16);
	    int g1 = ((color1 & 0xff00) >> 8);
	    int b1 = (color1 & 0xff);

	    int r2 = ((color2 & 0xff0000) >> 16);
	    int g2 = ((color2 & 0xff00) >> 8);
	    int b2 = (color2 & 0xff);

	    int r = (int)((r1 * iRatio) + (r2 * ratio));
	    int g = (int)((g1 * iRatio) + (g2 * ratio));
	    int b = (int)((b1 * iRatio) + (b2 * ratio));

	    return ( r << 16 | g << 8 | b );
	}
	
    
    // Based off of Quake III's Fast Inverse Square Root Algorithm
    public static float fstsqrt(float number) {
    	float og = number;
        int i = Float.floatToIntBits(number);
        float x2 = 0.5f*number;
        i = 0x5f3759df  - (i>>1);
        number = Float.intBitsToFloat(i);
        number *= (1.5f - x2*number*number);
        number *= og+0.1;
        return number;
    }
	
	public static Point pointIntersection(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
		double t1 = (x1 - x3) * (y3 - y4) - (y1 - y3) * (x3 - x4);
		double t2 = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);

		double u1 = (x1 - x2) * (y1- y3) - (y1 - y2) * (x1 - x3);
		double u2 = t2;

		if(t2 == 0) {
			return null;
		}

		double t = t1/t2;

		double u = -(u1/u2);

		double x;
		double y;

		if((0 <= t && t <= 1) && (0 <= u && u <= 1)) {
			x = x1 + (t * (x2 - x1));
			y = y1 + (t * (y2 - y1));
			return new Point(x, y);
		} else {
			return null;
		}
	}
	
	public static Point gridSystem(int cells, int px, int py) {

		int x = (int) Math.round(px/cells);
		int y = (int) Math.round(py/cells);
		
		return new Point(x,y);
	}
	
	public static Point gridSystemEntity(int cells, int px, int py, int px2, int py2, int ex, int ey) {
		Point p = pointIntersection(px, py, px2, py2, ex, ey, ex+(cells), ey+(cells));
		
		if(p != null) {
			return gridSystem(cells, (int)p.getX(), (int)p.getY());
		} else {
			return null;
		}
	}
	
	public static double fast_mod(int divis_a, int divis_b) {
		return (divis_a & (divis_b - 1));
	}
	
	public static int choose(int i, int j, int k, Random r) {
		int result = k;
		if(r.nextInt(4) == 1) result = i;
		if(r.nextInt(4) == 2) result = j;
		if(r.nextInt(4) == 3) result = k;
		return result;
	}
	
	public static List<Positional_Vert> vert_compact(float[] verts) {
		List<Positional_Vert> vert_total = new ArrayList<>();
		for(int i = 0; i < verts.length; i++) {
			if(i % 3 == 0 || i == 0) {
				vert_total.add(new Positional_Vert(new ThreeValue(verts[i], verts[i+1], verts[i+2]), new ThreeValue(1,1,1)));
			}
		}
		return vert_total;
	}
	
}
