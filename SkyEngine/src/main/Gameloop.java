package main;

import renderer.Renderer;

public class Gameloop {

	private boolean isRunning;

	public static int display_frames = 0;
	
	public Gameloop(boolean isRunning) {
		this.isRunning = isRunning;
	}
	//renderer.update();
//	renderer.repaint();
//add entity chunking
	public void loop(Renderer renderer, Renderer gui_renderer) { 
		
		double startTime = System.currentTimeMillis();
		
		int Target_FPS = 20;
		
		double ticks = 0;
		
		while(isRunning) {
			
			double currentTime = System.currentTimeMillis();
			double deltaTime = currentTime-startTime;
		
			ticks+=deltaTime;
			
		
			if(ticks >= Target_FPS) {
				
				renderer.update();
				
				ticks-=Target_FPS;
			}
			startTime = currentTime;
			renderer.repaint();
		}
		
	}
}
