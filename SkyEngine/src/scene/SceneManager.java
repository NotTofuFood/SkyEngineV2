package scene;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.List;

import main.Gameloop;
import main.Window;
import maths.AngleLookup;
import maths.ExtraMath;
import maths.ThreeValue;
import obj.Wall;
import rays.Camera;
import rays.Portal;
import rays.Ray;
import renderer.Display;
import texture.ImageLoader;

public class SceneManager {

	public List<Wall> walls = new ArrayList<>();
	public List<Ray> rays = new ArrayList<>();
	public List<Portal> portal_rays = new ArrayList<>();

	public final int wall_width = 64;
	private final int wall_height = 64;
	
	private int wall_hit_amount = 0;
	
	public int projection_width = (int) Window.WIDTH;
	
	public int[] projected_height_data;

	public static AngleLookup angles;
	
	public int distToProjectionPlane = 0;
	
	private double fishEyeCorrection[] = new double[projection_width*15];
	
	private int changer = 2;

	//private final Color shadow = new Color(0,0,0,0.5f);
	private final Font info_font = new Font("Times New Roman", Font.BOLD, 16);
	
	private double fog_intensity; 
	private ThreeValue fog_color;
	public int floor_height = (int) (Window.HEIGHT);

	private BufferedImage floor_buffer = new BufferedImage((int)Window.WIDTH, floor_height, BufferedImage.TYPE_INT_RGB);
	public BufferedImage floor_image;
	public BufferedImage ceiling_image;
	
	private int[] imagePixelData ;
	private int[] imagePixelDataCeil;
	private int[] floor_buffer_pixels;
		
	private int lowest_x = 0;
	private int lowest_y = 0;
	
	
	public int image_size = 0;
	
	public SceneManager(int player_height) {
		angles = new AngleLookup();
	}
	
	public void safe_init() {
		
		imagePixelData = ((DataBufferInt)floor_image.getRaster().getDataBuffer()).getData();
		
		imagePixelDataCeil = ((DataBufferInt)ceiling_image.getRaster().getDataBuffer()).getData();

		floor_buffer_pixels = ((DataBufferInt)floor_buffer.getRaster().getDataBuffer()).getData();
		distToProjectionPlane = (int) Math.floor((Window.WIDTH/changer) / Math.tan(Math.toRadians(Display.FOV/changer)));
		
		int lowest_x1 = (int) walls.get(0).getX1();
		int lowest_x2 = (int) walls.get(0).getX2();
		int lowest_y1 = (int) walls.get(0).getY1();
		int lowest_y2 = (int) walls.get(0).getY2();

		for(int z = 0; z < walls.size(); z++) {
			if(lowest_x1 < walls.get(z).getX1()) {
				lowest_x1 = (int) walls.get(z).getX1();
			}
			if(lowest_x2 < walls.get(z).getX2()) {
				lowest_x2 = (int) walls.get(z).getX2();
			}
			if(lowest_y1 < walls.get(z).getY1()) {
				lowest_y1 = (int) walls.get(z).getY1();
			}
			if(lowest_y2 < walls.get(z).getY2()) {
				lowest_y2 = (int) walls.get(z).getY2();
			}
		}
		
		if(lowest_x1 < lowest_x2) {
			lowest_x = lowest_x2;
		} else {
			lowest_x = lowest_x1;
		}
		
		if(lowest_y1 < lowest_y2) {
			lowest_y = lowest_y2;
		} else {
			lowest_y = lowest_y1;
		}

		for (int i=(int) -Math.floor(Window.WIDTH/2); i<=Math.floor(Window.WIDTH/2); i++)
		{
			fishEyeCorrection[(int) (i+Math.floor(Window.WIDTH/2))] = (1.0/Math.cos(((i*Math.PI)/Math.floor(Math.floor(Math.floor(Window.WIDTH/2)*3)*2))));
		}

	}
	
	public void renderScene(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, (int)Window.WIDTH, (int)Window.HEIGHT);

		//Draw grid
		g.setColor(Color.GREEN);
		for(int x = 0; x < lowest_x; x+=64) {
			for(int y = 0; y < lowest_y; y+=64) {
				g.drawRect(x, y, 64, 64);
			}
		}
		
		
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
			int projected_plane = (int) (wall_height/distance*distToProjectionPlane);
			g.setColor(new Color(color, color, color));
			g.fillRect(ray, (int) Window.HEIGHT/changer - projected_plane/changer, (int)1, projected_plane+(int)wall_height);
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

		
		int plus_y = 0;
		
		double projected_plane_portal = (int) (wall_height * distToProjectionPlane / portal.portal_rays.get(ray_index).getDistance());
		
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


		double wall_end = (Window.HEIGHT/2+(projected_plane_portal*0.5));
		double wall_start=(Window.HEIGHT/2-(projected_plane_portal*0.5));
		
		g.drawImage(texture, ray_index, (int)wall_start+plus_y, 1, (int)(wall_end-wall_start), io);

		g.setColor(new Color(fog_color.getX(),fog_color.getY(),fog_color.getZ(),color));
		g.fillRect(ray_index*(int)1, (int)wall_start+plus_y, 1, (int)(wall_end-wall_start));
		

		int tx,ty;

		int ray_rot;
		ray_rot = (int)(Math.toDegrees(portal.portal_rays.get(ray_index).getDegrees()) * 100);

		for (int i = (int) wall_end+1; i < Window.HEIGHT; i++) {

			double distance = ((float)(wall_height/changer) / (i-Window.HEIGHT/changer) ) * distToProjectionPlane;
			
			tx = ( (int) (distance * (angles.cos[Math.abs(ray_rot)]))) + c.getX();
			ty = ( (int) (distance * (angles.sin[Math.abs(ray_rot)]))) + c.getY();
			
			int tex_x = (int)tx&63;
			int tex_y = (int)ty&63;
		
			int tex = tex_y*wall_width+tex_x;
			int pixel_index = (int)ExtraMath.fast_mod((i*projection_width+ray_index), 1);
		
			floor_buffer_pixels[pixel_index] = imagePixelData[tex] >> 16;
			floor_buffer_pixels[pixel_index+1] = imagePixelData[tex] >> 8;
			floor_buffer_pixels[pixel_index+2] = imagePixelData[tex];
		
			floor_buffer.getRaster().setPixel(ray_index, i, floor_buffer_pixels);	
		}
		
		for (int i = (int) wall_start+plus_y; i > 0; i--) {

			double divsor = i-Window.HEIGHT/changer;
			
			double distance = ((float)(wall_height/changer) / (divsor) ) * distToProjectionPlane;
			
			tx = ( (int) (-distance * (angles.cos[Math.abs(ray_rot)]))) + c.getX();
			ty = ( (int) (-distance * (angles.sin[Math.abs(ray_rot)]))) + c.getY();
			
			int tex_x = (int)tx&63;
			int tex_y = (int)ty&63;
		
			int tex = tex_y*wall_width+tex_x;
			int pixel_index = (int)ExtraMath.fast_mod((i*projection_width+ray_index), 1);
		
			floor_buffer_pixels[pixel_index] = imagePixelDataCeil[tex] >> 16;
			floor_buffer_pixels[pixel_index+1] = imagePixelDataCeil[tex] >> 8;
			floor_buffer_pixels[pixel_index+2] = imagePixelDataCeil[tex];
		
			floor_buffer.getRaster().setPixel(ray_index, i, floor_buffer_pixels);	
		}
		
		rays.get(ray_index).render(g, false);
		portal.portal_rays.get(ray_index).render(g, false);
	}

	public void fog_settings(ThreeValue color, double intensity) {
		fog_intensity = intensity;
		fog_color = color;
	}
	
	public void renderInfo(Graphics2D g, Camera c) {
		g.setColor(Color.WHITE);
		g.setFont(info_font);
		g.drawString("FPS: " + Gameloop.display_frames, 10, 50);
		g.drawString("Global Rotation: " + c.getRotation(), 10, 100);
		g.drawString("Map Name: " + Display.current_map, 10, 150);
		g.drawString("Walls In Session: " + walls.size(), 10, 200);
		g.drawString("Columns Being Rendered: " + wall_hit_amount, 10, 250);
		g.drawString("Portals In Session: " + portal_rays.size(), 10, 300);
		wall_hit_amount = 0;
	}
	
	public double fixAngle(double d) {
	   if(d > 3.14*2) {
	            d -= 3.14*2;
	        }
	        if(d < 0) {
	            d += 3.14*2;
	        }
	        return d;
	    }

	
	public void renderScene3D(Graphics2D g, ImageObserver io, Camera c, Skybox sky) {
		BufferedImage texture = null;
		projected_height_data = new int[rays.size()];

		g.drawImage(floor_buffer, 0, 0, io);
		

		//sky.render(io, g);
		
		for(int ray = 0; ray < rays.size(); ray++) {
			for(int portal = 0; portal < portal_rays.size(); portal++) {
				portal_rays.get(portal).portal_rays.get(ray).setDegrees(rays.get(ray).getDegrees());
			}
			if(!rays.get(ray).hitPortal()) {
				for(int wall = 0; wall < walls.size(); wall++) {
					rays.get(ray).raycast(walls, wall);
					if(rays.get(ray).hitSomething()) { 
						wall_hit_amount++;
					}
					c.checkCollisions(walls, wall);
				}

				int color = (int) ExtraMath.clamp(rays.get(ray).getDistance()/fog_intensity, 0, 255);

				double wall_heights = (wall_height * distToProjectionPlane / rays.get(ray).getDistance());
				double wall_end = (Window.HEIGHT/2+(wall_heights*0.5));
				double wall_start=(Window.HEIGHT/2-(wall_heights*0.5));
				
				int projected_plane =  (int)((Window.HEIGHT/2 + (wall_heights*0.5)) - (Window.HEIGHT/2-(wall_heights*0.5)));

				if(ray == rays.size()/2) {
					if(projected_plane > 3000) {
						c.stop_up = true;
					} else {
						c.stop_up = false;
					}
				}

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

				g.drawImage(texture, ray, (int)wall_start, 1, projected_plane, io);
			
				g.setColor(new Color(fog_color.getX(),fog_color.getY(),fog_color.getZ(),color));
				g.fillRect(ray, (int)wall_start, 1, projected_plane);

				int tx,ty;

				int ray_rot;
				ray_rot = (int)(Math.toDegrees(rays.get(ray).getDegrees()) * 100);

				for (int i = (int) wall_end+1; i < Window.HEIGHT; i++) {
					
					double distance = ((float)(wall_height/changer) / (i-Window.HEIGHT/changer) ) * distToProjectionPlane;
					
					tx = ( (int) (distance * (angles.cos[Math.abs(ray_rot)]))) + c.getX();
					ty = ( (int) (distance * (angles.sin[Math.abs(ray_rot)]))) + c.getY();
					
					int tex_x = (int)tx&63;
					int tex_y = (int)ty&63;
				
					int tex = tex_y*wall_width+tex_x;
					int pixel_index = (int)ExtraMath.fast_mod((i*projection_width+ray), 1);
				
					floor_buffer_pixels[pixel_index] = imagePixelData[tex] >> 16;
					floor_buffer_pixels[pixel_index+1] = imagePixelData[tex] >> 8;
					floor_buffer_pixels[pixel_index+2] = imagePixelData[tex];
				
					floor_buffer.getRaster().setPixel(ray, i, floor_buffer_pixels);	
				}
				
				for (int i = (int) (Window.HEIGHT/changer - projected_plane/changer); i > 0; i--) {

					double divsor = i-Window.HEIGHT/changer;
					
					double distance = ((float)(wall_height/changer) / (divsor) ) * distToProjectionPlane;
					
					tx = ( (int) (-distance * (angles.cos[Math.abs(ray_rot)]))) + c.getX();
					ty = ( (int) (-distance * (angles.sin[Math.abs(ray_rot)]))) + c.getY();
					
					int tex_x = (int)tx&63;
					int tex_y = (int)ty&63;
				
					int tex = tex_y*wall_width+tex_x;
					int pixel_index = (int)ExtraMath.fast_mod((i*projection_width+ray), 1);
				
					floor_buffer_pixels[pixel_index] = imagePixelDataCeil[tex] >> 16;
					floor_buffer_pixels[pixel_index+1] = imagePixelDataCeil[tex] >> 8;
					floor_buffer_pixels[pixel_index+2] = imagePixelDataCeil[tex];
				
					floor_buffer.getRaster().setPixel(ray, i, floor_buffer_pixels);	
				}
				
				rays.get(ray).render(g, false);
			} else {
				renderPortal(g, rays.get(ray).getWall().getPortalID(), ray, c, (int) (wall_height * distToProjectionPlane / rays.get(ray).getDistance()), 2, io);
			}			
		}

	}
}
