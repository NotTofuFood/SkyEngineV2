package gui;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.List;

public class GUI_Segment {

	private List<CustomParameter> params = new ArrayList<>();

	public Font text_font;

	public GUI_Segment(List<CustomParameter> params) {
		this.params = params;
	}

	public void addParam(CustomParameter param) {
		params.add(param);
	}

	public void segment_render(Graphics2D renderer, ImageObserver io) {
		for (int i = 0; i < this.params.size(); i++) {
			switch (this.params.get(i).type) {
			case GUI_IMAGE:
				if (this.params.get(i).width != 1) {
					if (!this.params.get(i).isAnimated)
						renderer.drawImage(this.params.get(i).getImage(), this.params.get(i).x, this.params.get(i).y,
								this.params.get(i).width, this.params.get(i).height, io);
					else
						renderer.drawImage(this.params.get(i).getGif(), this.params.get(i).x, this.params.get(i).y,
								this.params.get(i).width, this.params.get(i).height, io);
				} else {
					if (!this.params.get(i).isAnimated)
						renderer.drawImage(this.params.get(i).getImage(), this.params.get(i).x, this.params.get(i).y,
								io);
					else
						renderer.drawImage(this.params.get(i).getGif(), this.params.get(i).x, this.params.get(i).y, io);
				}
				break;
			case GUI_INFO:
				renderer.drawString(this.params.get(i).getValue(ParametersValues.GUI_INFO), this.params.get(i).x,
						this.params.get(i).y);
				break;
			case GUI_POS:
				renderer.drawString(this.params.get(i).getPos().getX() + " " + this.params.get(i).getPos().getY(),
						this.params.get(i).x, this.params.get(i).y);
				break;
			case GUI_TEXT:
				renderer.drawString(this.params.get(i).getValue(ParametersValues.GUI_TEXT), this.params.get(i).x,
						this.params.get(i).y);
				break;
			case VAL_INF:
				renderer.drawString(Double.toString(this.params.get(i).getValue()), this.params.get(i).x,
						this.params.get(i).y);
				break;
			case VAL_NEG:
				renderer.drawString(Double.toString(this.params.get(i).getValue()), this.params.get(i).x,
						this.params.get(i).y);
				break;
			case VAL_POS:
				renderer.drawString(Double.toString(this.params.get(i).getValue()), this.params.get(i).x,
						this.params.get(i).y);
				break;
			default:
				break;
			}
		}
	}

}