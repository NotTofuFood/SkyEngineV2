package maths;

public class AngleLookup {

	public double cos[];
	public double sin[];

	public AngleLookup() {
		cos = new double[40000];
		sin = new double[40000];

		double angle = 0;

		for (int i = 0; i < 40000; i++)
		{
			angle += 0.000174533;
			cos[i] = Math.cos(angle);
			sin[i] = Math.sin(angle);
		}
	}

}
