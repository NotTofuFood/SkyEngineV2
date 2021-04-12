package main;

import maths.ExtraMath;
import renderer.Renderer;

public class Gameloop {

	private boolean isRunning;

	public static int display_frames = 0;
	
	public Gameloop(boolean isRunning) {
		this.isRunning = isRunning;
		ExtraMath.init();
	}

	public void loop(Renderer renderer) { 
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while(isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1) {
				renderer.update();
				renderer.repaint();
				delta--;
			}
			frames++;

			if(System.currentTimeMillis() - timer > 1000) {
				display_frames = frames/1000000;
				timer += 1000;
				frames = 0;
			}
		}
		isRunning = false;
	}
}
