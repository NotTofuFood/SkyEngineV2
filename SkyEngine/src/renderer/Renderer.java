package renderer;

import java.awt.Graphics2D;

import javax.swing.JPanel;

public abstract class Renderer extends JPanel {
	private static final long serialVersionUID = 2058718608284114485L;
	public abstract void update();
	public abstract void render(Graphics2D g);
	public abstract int getFOV();
	public abstract boolean isUp();
	public abstract void setUp(boolean up);
	public abstract boolean isDown();
	public abstract void setDown(boolean down);
	public abstract boolean isLeft();
	public abstract void setLeft(boolean left);
	public abstract boolean isRight();
	public abstract void setRight(boolean right);
}
