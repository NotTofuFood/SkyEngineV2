package main;

import java.awt.event.KeyEvent;

import javax.swing.JFrame;

import input.Input;
import renderer.Display;
import renderer.GUIRenderer;
import renderer.Renderer;

public class Window {

	public static final double WIDTH = 426;
	public static final double HEIGHT = 240;
	
	public static final double ACTUAL_WIDTH = 1980;
	public static final double ACTUAL_HEIGHT = 1080;

	private static Renderer renderer = new Display();
	private static Renderer gui_renderer = new GUIRenderer();
	
	public static void main(String[] args) {
		JFrame window = new JFrame("Sky Engine - Aiden Thakurdial (Version 2.7)");
		Gameloop gameLoop = new Gameloop(true);
		Input inputs = new Input();
		window.setSize((int)ACTUAL_WIDTH, (int)ACTUAL_HEIGHT);
//		window.setExtendedState(window.getExtendedState() | JFrame.MAXIMIZED_BOTH);
//		window.setUndecorated(true);
		window.setVisible(true);
		window.setLocationRelativeTo(null);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(true);
		window.add(renderer);
		inputs.setDefualts(renderer, renderer, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D);
		inputs.setDefualts(renderer, renderer, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT);

		gameLoop.loop(renderer,gui_renderer);
		window.pack();
	}

}
