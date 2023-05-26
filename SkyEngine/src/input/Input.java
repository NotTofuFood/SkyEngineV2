package input;

import java.awt.AWTException;
import java.awt.Frame;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import main.Window;
import renderer.Renderer;

public class Input implements MouseListener {
	
	public static boolean clicked = false;
	
	private static Robot mouse_mover; 
	
	private static boolean stopper = false;
	
	private static boolean reload = false;
	
	public Input() {
		try {
			mouse_mover = new Robot();
		} catch (AWTException e1) {
			e1.printStackTrace();
		}
	}
	
	public void setDefualts(JPanel panel, Renderer renderer, int key_up, int key_down, int key_left, int key_right) {
		
		initInput(panel, key_up, new AbstractAction() {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				renderer.setUp(true);
				renderer.setDown(false);
			}
		}, new AbstractAction() {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				renderer.setUp(false);
			}
		});
		initInput(panel, key_down, new AbstractAction() {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				renderer.setUp(false);
				renderer.setDown(true);
			}
		}, new AbstractAction() {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				renderer.setDown(false);
			}
		});
		initInput(panel, key_left, new AbstractAction() {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				renderer.setLeft(true);
			}
		}, new AbstractAction() {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				renderer.setLeft(false);
			}
		});
		initInput(panel, key_right, new AbstractAction() {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				renderer.setRight(true);
			}
		}, new AbstractAction() {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				renderer.setRight(false);
			}
		});
		initInput(panel, KeyEvent.VK_ESCAPE, new AbstractAction() {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				
			}
		}, new AbstractAction() {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				stopper = !stopper;
			}
		});
		
		
	}
	
	public void addReloadInput(JPanel panel) {
		initInput(panel, KeyEvent.VK_R, new AbstractAction() {

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				reload = true;
			}
			
		},new AbstractAction() {

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				reload = false;
			}
			
		});
	}
	
	private void initInput(JPanel panel, int key, AbstractAction action_pressed, AbstractAction action_released) {
		panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(key, 0, false), "pressed" + key);
		panel.getActionMap().put("pressed" + key, action_pressed);
		panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(key, 0, true), "released" + key);
		panel.getActionMap().put("released" + key, action_released);
	}
	

	public static boolean MouseUp() {
		boolean up = false;
		
		if(!stopper) {
		if(MouseInfo.getPointerInfo().getLocation().getY() < Window.HEIGHT) {
			up = true;
		}
		}
		
		return up;
	}
	
	public static boolean MouseDown() {
		boolean down = false;
		
		if(!stopper) {
		if(MouseInfo.getPointerInfo().getLocation().getY() > Window.HEIGHT) {
			down = true;
		}
		}
		
		return down;
	}
	
	public static boolean MouseLeft() {
		boolean up = false;

		if(!stopper) {
		if(MouseInfo.getPointerInfo().getLocation().getX() < Window.WIDTH) {
			up = true;
		}
		}
		
		return up;
	}
	
	public static boolean MouseRight() {
		boolean down = false;
		
		if(!stopper) {
		if(MouseInfo.getPointerInfo().getLocation().getX() > Window.WIDTH) {
			down = true;
		}
		}
		
		return down;
	}
	
	public static void resetMouse() {
		if(!stopper) {
			mouse_mover.mouseMove((int)Window.WIDTH, (int)Window.HEIGHT);
		} 
	}
	
	public static boolean getReload() {
		return reload;
	}
	
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		clicked = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		clicked = false;
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}
	
}
