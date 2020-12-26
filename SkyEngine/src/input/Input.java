package input;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import renderer.Renderer;

public class Input {
	
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
	}
	
	public void initInput(JPanel panel, int key, AbstractAction action_pressed, AbstractAction action_released) {
		panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(key, 0, false), "pressed" + key);
		panel.getActionMap().put("pressed" + key, action_pressed);
		panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(key, 0, true), "released" + key);
		panel.getActionMap().put("released" + key, action_released);
	}
	
}
