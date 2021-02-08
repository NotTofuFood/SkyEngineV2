package scene;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.List;
import main.Window;
import maths.ExtraMath;
import maths.ThreeValue;
import obj.Wall;
import rays.Camera;
import rays.Portal;
import rays.Ray;
import texture.ImageLoader;

public class SceneManager {

	public List<Wall> walls = new ArrayList<>();
	public List<Ray> rays = new ArrayList<>();
	public List<Portal> portal_rays = new ArrayList<>();

	public final double wall_width = 64;
	private final double wall_height = 128;
	
	private final int multiplier = (int) (Window.WIDTH/2);

	public int projection_width = (int) Window.WIDTH;
	
	public int[] projected_height_data;
	
	private double[] ray_angle_x, ray_angle_y;
	private double[] floor_offset_divider;

	private int height;
	
	private double fog_intensity; 
	private ThreeValue fog_color;
	public int floor_height = (int) (Window.HEIGHT);

	private BufferedImage floor_buffer = new BufferedImage((int)Window.WIDTH, floor_height, BufferedImage.TYPE_INT_RGB);
	
	public int image_size = 0;
	
	public SceneManager(int player_height) {
		height = floor_height*player_height;
		ray_angle_x = new double[projection_width];
		ray_angle_y = new double[projection_width];
		floor_offset_divider = new double[projection_width];
		for(int j = 0; j < projection_width; j++) {
			floor_offset_divider[j] = ((player_height / (j-Window.HEIGHT/2)));
			for(int rot = 0; rot < 360; rot++) {
				double column_angle = Math.atan( ( j - (Window.WIDTH/2) ) / floor_height);
				double rayangle = Math.toRadians(rot)+column_angle;
				ray_angle_x[j] = Math.cos(rayangle);
				ray_angle_y[j] = Math.sin(rayangle);
			}
		}
	}
	
	public void renderScene(Graphics2D g) {
		for(int ray = 0; ray < rays.size(); ray++) {
			for(int wall = 0; wall < walls.size(); wall++) {
				walls.get(wall).render(g);
				rays.get(ray).raycast(walls, wall); 
			}
			rays.get(ray).render(g, true);
		}
		for(int portal = 0; portal < portal_rays.size(); portal++) {
			for(int ray = 0; ray < portal_rays.get(portal).portal_rays.size(); ray++) {
				for(int wall = 0; wall < walls.size(); wall++) {
					portal_rays.get(portal).portal_rays.get(ray).raycast(walls, wall); 
				}
				portal_rays.get(portal).portal_rays.get(ray).render(g, true);
			}
		}
	}


	public void renderSceneUnTextured3D(Graphics2D g, Camera c) {
		g.setColor(Color.black);
		g.fillRect(0, (int)Window.HEIGHT/2, (int)Window.WIDTH, (int)Window.HEIGHT/2);
		for(int ray = 0; ray < rays.size(); ray++) {
			for(int wall = 0; wall < walls.size(); wall++) {
				rays.get(ray).raycast(walls, wall);
			}
			
			double distance = rays.get(ray).getDistance();

			int color = (int) ExtraMath.clamp(255 - rays.get(ray).getDistance()/2, 0, 255);
			int projected_plane = (int) (wall_height/distance*multiplier);
			g.setColor(new Color(color, color, color));
			g.fillRect(ray, (int) Window.HEIGHT/4 - projected_plane/4, (int)1, projected_plane+(int)wall_height);
			rays.get(ray).render(g, false);
		}
	}

	public void createPortal(Portal portal, ImageObserver io) {
		walls.add(new Wall(portal.getPortal_x(), portal.getPortal_y(), portal.getPortal_x()+wall_width, portal.getPortal_y()));
		walls.get(walls.size()-1).isPortal = true;
		walls.get(walls.size()-1).portal_id = portal;
		walls.get(walls.size()-1).setTexture(ImageLoader.loadImage("res/textures/important/missing_texture.png"));
	}

	private void renderPortal(Graphics2D g, Portal portal, int ray_index, Camera c, int projected_plane, int recursion, ImageObserver io) {
		BufferedImage texture = null;
		for(int wall = 0; wall < walls.size(); wall++) {
			portal.portal_rays.get(ray_index).raycast(walls, wall);
			rays.get(ray_index).raycast(walls, wall); 
		}

		int color = (int) ExtraMath.clamp(portal.portal_rays.get(ray_index).getDistance()/fog_intensity, 0, 255);

		double projected_plane_portal = (int) (wall_height * multiplier / portal.portal_rays.get(ray_index).getDistance());
		
		if(projected_plane_portal > projected_plane) {
			projected_plane_portal = projected_plane;
		}
		
		int offset;
		
		if(portal.portal_rays.get(ray_index).getWallType()) {
			offset = (int) ((portal.portal_rays.get(ray_index).getX2())%wall_width);
		} else {
			offset = (int) ((portal.portal_rays.get(ray_index).getY2())%wall_width);
		}

		if(offset >= wall_width) {
			offset-=wall_width;
		}
		if((int)(Window.WIDTH*portal.portal_rays.get(ray_index).getTextureID())+offset < image_size) texture = ImageLoader.wall_textures.get((int)(Window.WIDTH*portal.portal_rays.get(ray_index).getTextureID())+offset);

		g.drawImage(texture, ray_index, (int) Window.HEIGHT/4 - (int)projected_plane_portal/4, 1, (int)projected_plane_portal+(int)wall_height, io);

		g.setColor(new Color(fog_color.getX(),fog_color.getY(),fog_color.getZ(),color));
		g.fillRect(ray_index*(int)1, (int) Window.HEIGHT/4 - (int)projected_plane_portal/4, (int)1, (int)projected_plane_portal+(int)wall_height);
	
		rays.get(ray_index).render(g, false);
		portal.portal_rays.get(ray_index).render(g, false);

	}

	public void fog_settings(ThreeValue color, double intensity) {
		fog_intensity = intensity;
		fog_color = color;
	}
	
	BufferedImage floor_image = ImageLoader.loadFloorImage("res/textures/wolf/greystone.png");

	public void renderFloor(Graphics2D g, ImageObserver io, Camera c) {
		floor_buffer.setAccelerationPriority(1);
		int[] imagePixelData = ((DataBufferInt)floor_image.getRaster().getDataBuffer()).getData();
		int[] floor_buffer_pixels = ((DataBufferInt)floor_buffer.getRaster().getDataBuffer()).getData();

		double x, y;
		double floor_off;
		for(int i = (int)floor_height>>1; i < floor_height; i++) {	
			floor_off = floor_offset_divider[i] * height;
			for(int j = 0; j < rays.size(); j++) {
				
				x = floor_off * ray_angle_x[j] + c.getRotation();
				y = floor_off * ray_angle_y[j] + c.getRotation();
				
				x+=c.getX();
				y+=c.getY();

				int tex_x = (int) x&63;
				int tex_y = (int) y&63;
				
				int tex = tex_y*64+tex_x;
				
				int pixel_index = (int) ExtraMath.fast_mod(i*projection_width+j, 1);

				floor_buffer_pixels[pixel_index] = imagePixelData[tex] >> 16;
				floor_buffer_pixels[pixel_index+1] = imagePixelData[tex] >> 8;
				floor_buffer_pixels[pixel_index+2] = imagePixelData[tex];

				floor_buffer.getRaster().setPixel(j, i, floor_buffer_pixels);	
				//floor_buffer.getRaster().setPixel(j, floor_buffer.getHeight()-i-1, floor_buffer_pixels);	
			}
		}
		g.drawImage(floor_buffer, 0, (int) 0, io);
		//g.drawImage(floor_buffer, 0, (int) floor_height*2, projection_width, -floor_height, io);
	}
	
	public void renderScene3D(Graphics2D g, ImageObserver io, Camera c) {
		BufferedImage texture = null;
		projected_height_data = new int[rays.size()];
		for(int ray = 0; ray < rays.size(); ray++) {
			for(int portal = 0; portal < portal_rays.size(); portal++) {
				portal_rays.get(portal).portal_rays.get(ray).setDegrees(rays.get(ray).getDegrees());
			}
			if(!rays.get(ray).hitPortal()) {
				for(int wall = 0; wall < walls.size(); wall++) {
					rays.get(ray).raycast(walls, wall);
					c.checkCollisions(walls, wall);
				}

				int color = (int) ExtraMath.clamp(rays.get(ray).getDistance()/fog_intensity, 0, 255);

				int projected_plane = (int) (wall_height * multiplier / rays.get(ray).getDistance());

				int offset;

				if(rays.get(ray).getWallType()) {
					offset = (int) ExtraMath.fast_mod((int) (rays.get(ray).getX2()), 64);
				} else {
					offset = (int) ExtraMath.fast_mod((int) (rays.get(ray).getY2()), 64);
				}

				if(offset >= wall_width) {
					offset-=wall_width; 
				}
				
				if((int)(Window.WIDTH*rays.get(ray).getTextureID())+offset+16 < image_size) texture = ImageLoader.wall_textures.get((int)(Window.WIDTH*rays.get(ray).getTextureID())+offset+16);

				g.drawImage(texture, ray, (int) Window.HEIGHT/4 - (int)projected_plane/4, 1, (int)projected_plane+(int)wall_height, io);

				g.setColor(new Color(fog_color.getX(),fog_color.getY(),fog_color.getZ(),color));
				g.fillRect(ray, (int) Window.HEIGHT/4 - (int)projected_plane/4, 1, (int)projected_plane+(int)wall_height);

				rays.get(ray).render(g, false);
			} else {
				renderPortal(g, rays.get(ray).getWall().getPortalID(), ray, c, (int) (wall_height * multiplier / rays.get(ray).getDistance()), 2, io);
			}
		}
	}
}
