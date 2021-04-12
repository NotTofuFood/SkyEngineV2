package maths;

public class ExtraMath {
	
	private static double[] rotation_cos = new double[400];
	private static double[] rotation_sin = new double[rotation_cos.length];
	
	public static void init() {
		for(int i = 0; i < rotation_cos.length; i++) {
			rotation_cos[i] = Math.cos(Math.toRadians(i));
			rotation_sin[i] = Math.sin(Math.toRadians(i));
		}
	}
	
	public static double getRotationCos(int deg) {
		double d;
		if(deg <= 0) {
			return -rotation_cos[Math.abs(deg)];
		} else {
			d = rotation_cos[deg];
		}
		return d;
	}
	
	public static double getRotationSin(int deg) {
		double d;
		if(deg <= 0) {
			return -rotation_sin[Math.abs(deg)];
		} else {
			d = rotation_sin[deg];
		}
		return d;
	}

	public static double distance(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
	}

	public static double clamp(double value, double min, double max) {
		return Math.min(Math.max(value, min), max);
	}
	
    public static double lerp(double a, double b, double f) {
        return a + f * (b - a);
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

	public static double fast_mod(int divis_a, int divis_b) {
		return (divis_a & (divis_b - 1));
	}
	
}
