package gui;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import maths.ExtraMath;
import maths.Point;
import rays.Camera;

public class CustomParameter {
	
	private double parameterValue;
	
	private double min = Integer.MIN_VALUE;
	private double max = Integer.MAX_VALUE;
	
	private BufferedImage image;
	private Image gifimage;
	
	public int x = 0;
	public int y = 0;
	
	private String text;
	
	private String info;
	
	public int width = 1;
	public int height = 1;
	
	public boolean isAnimated = false;
	
	public ParametersValues type;
	
	public int original_y;
	
	public CustomParameter(ParametersValues type, String name, int x, int y) {
		this.type = type;
		this.x = x;
		this.y = y;
		this.original_y = y;
		switch(type) {
		case GUI_IMAGE :
			try {
				image = ImageIO.read(new File(name));
				info = name;
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case GUI_TEXT :
			text = name;
			break;
		default:
			break;
		}
	}
	
	public CustomParameter(ParametersValues type, String name, int x, int y, int width, int height) {
		this.type = type;
		this.x = x;
		this.y = y;
		this.original_y = y;
		this.width = width;
		this.height = height;
		switch(type) {
		case GUI_IMAGE :
			try {
				image = ImageIO.read(new File(name));
				info = name;
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case GUI_TEXT :
			text = name;
			break;
		default:
			break;
		}
	}
	
	
	public CustomParameter(ParametersValues type, String name, int x, int y, int width, int height, boolean gif) {
		this.type = type;
		this.x = x;
		this.y = y;
		this.width = width;
		this.original_y = y;
		this.height = height;
		switch(type) {
		case GUI_IMAGE :
			gifimage = new ImageIcon( this.getClass().getResource(name) ).getImage();
			info = name;
			isAnimated = true;
			break;
		case GUI_TEXT :
			text = name;
			break;
		default:
			break;
		}
	}
	
	public CustomParameter(ParametersValues type, double value, int x, int y) {
		this.type = type;
		this.x = x;
		this.y = y;
		this.original_y = y;
		switch(type) {
		case VAL_INF :
			parameterValue = value;
			min = Integer.MIN_VALUE;
			max = Integer.MAX_VALUE;
			break;
		case VAL_NEG :
			parameterValue = value;
			min = Integer.MIN_VALUE;
			max = 0;
			decrease_param(0);
			break;
		case VAL_POS :
			parameterValue = value;
			min = 0;
			max = Integer.MAX_VALUE;
			increase_param(0);
			break;
		default:
			break;
		}
	}
	
	public CustomParameter(ParametersValues type, int x, int y) {
		this.type = type;
		this.x = x;
		this.y = y;
		this.original_y = y;
		switch(type) {
		case GUI_POS :
			this.x = x;
			this.y = y;
			break;
		default:
			break;
		}
	}
	
	public void updateBob(Camera c) {
		if(c.getHSP() == 0) {
			y = original_y;
		} else {
			if(y > 20) {
				y += (int)((c.headbobs[c.current_head]/10)*100);
			}
		}
	}
	
	public void increase_param(double increase) {
		parameterValue+=increase;
		parameterValue = ExtraMath.clamp(parameterValue, min, max);
	}
	
	public void decrease_param(double decrease) {
		parameterValue-=decrease;
		parameterValue = ExtraMath.clamp(parameterValue, min, max);
	}
	
	public void setText(String new_text) {
		text = new_text;
	}
	
	public double getValue() {
		return parameterValue;
	}
	
	public Point getPos() {
		return new Point(x,y);
	}
	
	public String getValue(ParametersValues type) {
		switch(type) {
		case GUI_INFO :
			return info;
		case GUI_TEXT :
			return text;
		default:
			break;
		}
		return null;
	}
	
	public BufferedImage getImage() {
		return image;
	}
	
	public Image getGif() {
		return gifimage;
	}
	
	
	
}
