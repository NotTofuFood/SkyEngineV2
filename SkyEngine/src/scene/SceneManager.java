package scene;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import entities.Entity;
import lighting.Light;
import main.Gameloop;
import main.Window;
import maths.AngleLookup;
import maths.ExtraMath;
import maths.Point;
import maths.ThreeValue;
import obj.ModelLoader;
import obj.OBJLoader;
import obj.Positional_Vert;
import obj.Wall;
import rays.Camera;
import rays.Portal;
import rays.Ray;
import renderer.Display;
import texture.ImageLoader;

public class SceneManager {

	public List<Wall> walls = new ArrayList<>();
	public List<Light> lights = new ArrayList<>();
	public List<Ray> rays = new ArrayList<>();
	public List<Portal> portal_rays = new ArrayList<>();
	private List<ModelLoader> models = new ArrayList<>();

	private List<Ray> entity_rays = new ArrayList<>();
	
	public final int wall_width = 64;
	private final int wall_height = 64;

	private int wall_hit_amount = 0;

	public int projection_width = (int) Window.WIDTH;

	public int[] projected_height_data;

	public static AngleLookup angles;

	public BufferedImage skyimage;

	public int distToProjectionPlane = 0;

	private double fishEyeCorrection[] = new double[projection_width * 15];

	private int changer = 2;

	private Color[] fogcolors = new Color[255];

	// private final Color shadow = new Color(0,0,0,0.5f);
	private final Font info_font = new Font("Times New Roman", Font.BOLD, 12);
	private final Font ent_font = new Font("Times New Roman", Font.BOLD, 9);

	private double fog_intensity;
	private ThreeValue fog_color;
	public int floor_height = (int) (Window.HEIGHT);

	private BufferedImage floor_buffer = new BufferedImage((int) Window.WIDTH, floor_height,
			BufferedImage.TYPE_INT_RGB);
	public BufferedImage floor_image;
	public BufferedImage ceiling_image;

	private int[] imagePixelData;
	private int[] imagePixelDataSky;
	private int[] imagePixelDataCeil;
	private int[] floor_buffer_pixels;

	private int lowest_x = 0;
	private int lowest_y = 0;

	public double camera_height;

	private boolean reverse = false;

	public int image_size = 0;

	private ModelLoader test_model;

	public boolean disableceil = false;

	public SceneManager(int player_height) {
		angles = new AngleLookup();
		List<Positional_Vert> verts = new ArrayList<>();
		float vertices[] = {
				// Vertices according to faces
				-1.0f, -1.0f, 1.0f, // Vertex 0
				1.0f, -1.0f, 1.0f, // v1
				-1.0f, 1.0f, 1.0f, // v2
				1.0f, 1.0f, 1.0f, // v3

				1.0f, -1.0f, 1.0f, // ...
				1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, -1.0f,

				1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, 1.0f, 1.0f, -1.0f, -1.0f, 1.0f, -1.0f,

				-1.0f, -1.0f, -1.0f, -1.0f, -1.0f, 1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f,

				-1.0f, -1.0f, -1.0f, 1.0f, -1.0f, -1.0f, -1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f,

				-1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, -1.0f, 1.0f, -1.0f, 1.0f, 1.0f, -1.0f,
//				0.0f, 1.0f, 0.0f, // triangle 1 : begin
//				-1.0f,-1.0f, 1.0f,
//				 1.0f,-1.0f, 1.0f, // triangle 1 : end
//				 0.0f, 1.0f, 0.0f, // triangle 2 : begin
//				 1.0f,-1.0f, 1.0f,
//				 1.0f,-1.0f,-1.0f, // triangle 2 : end
//				 0.0f, 1.0f, 0.0f,
//				 1.0f,-1.0f,-1.0f,
//				 -1.0f,-1.0f,-1.0f,
//				 0.0f, 1.0f, 0.0f,
//				 -1.0f,-1.0f,-1.0f,
//				 -1.0f,-1.0f, 1.0f

		};
	//	OBJLoader model = new OBJLoader("res/scientist1fear.obj");
		// verts = ExtraMath.vert_compact(vertices);
//		test_model = new ModelLoader(model.verts(), new ThreeValue(200, 1, 200), 0.5);
//		models.add(test_model);
	}

	public void safe_init() {

		for (int i = 0; i < 255; i++) {
			fogcolors[i] = new Color((int) fog_color.getX(), (int) fog_color.getY(), (int) fog_color.getZ(), i);
		}

		camera_height = Window.HEIGHT / 2;

		imagePixelData = ((DataBufferInt) floor_image.getRaster().getDataBuffer()).getData();

		imagePixelDataCeil = ((DataBufferInt) ceiling_image.getRaster().getDataBuffer()).getData();

		floor_buffer_pixels = ((DataBufferInt) floor_buffer.getRaster().getDataBuffer()).getData();

		distToProjectionPlane = (int) Math
				.floor((Window.WIDTH / changer) / Math.tan(Math.toRadians(Display.FOV / changer)));

		int lowest_x1 = (int) walls.get(0).getX1();
		int lowest_x2 = (int) walls.get(0).getX2();
		int lowest_y1 = (int) walls.get(0).getY1();
		int lowest_y2 = (int) walls.get(0).getY2();

		for (int z = 0; z < walls.size(); z++) {
			if (lowest_x1 < walls.get(z).getX1()) {
				lowest_x1 = (int) walls.get(z).getX1();
			}
			if (lowest_x2 < walls.get(z).getX2()) {
				lowest_x2 = (int) walls.get(z).getX2();
			}
			if (lowest_y1 < walls.get(z).getY1()) {
				lowest_y1 = (int) walls.get(z).getY1();
			}
			if (lowest_y2 < walls.get(z).getY2()) {
				lowest_y2 = (int) walls.get(z).getY2();
			}
		}

		if (lowest_x1 < lowest_x2) {
			lowest_x = lowest_x2;
		} else {
			lowest_x = lowest_x1;
		}

		if (lowest_y1 < lowest_y2) {
			lowest_y = lowest_y2;
		} else {
			lowest_y = lowest_y1;
		}

		for (int i = (int) -Math.floor(Window.WIDTH / 2); i <= Math.floor(Window.WIDTH / 2); i++) {
			fishEyeCorrection[(int) (i + Math.floor(Window.WIDTH / 2))] = (1.0
					/ Math.cos(((i * Math.PI) / Math.floor(Math.floor(Math.floor(Window.WIDTH / 2) * 3) * 2))));
		}
		
	}

	public void skyInit() {
		if (disableceil) {

			// imagePixelDataSky = ((DataBufferInt)skyimage.getSubimage(0, 0, 512,
			// 512).getRaster().getDataBuffer()).getData();

			// floor_buffer_pixels = skyimage.getSubimage(0, 0, 512, 512);
		}
	}

	public void renderScene(Graphics2D g, List<Entity> entites) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, (int) Window.WIDTH, (int) Window.HEIGHT);

		// Draw grid
		g.setColor(Color.GREEN);
		for (int x = 0; x < lowest_x; x += 64) {
			for (int y = 0; y < lowest_y; y += 64) {
				g.drawRect(x, y, 64, 64);
			}
		}

		for (int light = 0; light < lights.size(); light++) {
			g.fillOval(lights.get(light).getX(), lights.get(light).getY(), lights.get(light).getRadius(),
					lights.get(light).getRadius());
		}
		
		for (int ent = 0; ent < entites.size(); ent++) {
			g.setColor(Color.DARK_GRAY);
			System.out.println( entites.get(ent).BoundingBoxSizeX);
			g.fillRect(entites.get(ent).x, entites.get(ent).y, entites.get(ent).BoundingBoxSizeX, entites.get(ent).BoundingBoxSizeY);
		}

		int og = 0;

		if (!reverse) {
			for (int ray = 0; ray < rays.size(); ray++) {
				for(int ents = 0; ents < entites.size(); ents++) {

					entites.get(ents).clearFrame = true;
				
				}
				
				
					rays.get(ray).entity.clear();
					rays.get(ray).castingEntity = false;

					
					for(int ents = 0; ents < entites.size(); ents++) {
						
						
							rays.get(ray).raycastEntity(entites.get(ents));
						
					}
					
				for (int wall = 0; wall < walls.size(); wall++) {
					walls.get(wall).render(g);
					rays.get(ray).raycast(walls, wall);
					if (rays.get(ray).hitSomething()) {
						if (rays.get(ray).getWall().isPortal) {
							reverse = true;
						}
					}
				

				
				}
				rays.get(ray).render(g, true);
			}
		}
		
		
		
	}

	public void renderSceneUnTextured3D(Graphics2D g, Camera c) {
		g.setColor(Color.black);
		g.fillRect(0, (int) camera_height, (int) Window.WIDTH, (int) camera_height);
		for (int ray = 0; ray < rays.size(); ray++) {
			for (int wall = 0; wall < walls.size(); wall++) {
				rays.get(ray).raycast(walls, wall);
			}

			double distance = rays.get(ray).getDistance();
			int color = (int) ExtraMath.clamp(255 - rays.get(ray).getDistance() / 2, 0, 255);
			int projected_plane = (int) (wall_height / distance * distToProjectionPlane);
			g.setColor(new Color(color, color, color));
			g.fillRect(ray, (int) Window.HEIGHT / changer - projected_plane / changer, (int) 1,
					projected_plane + (int) wall_height);
			rays.get(ray).render(g, false);
		}
	}

	public void createPortal(Portal portal, ImageObserver io) {
		walls.add(new Wall(portal.getPortal_x(), portal.getPortal_y(), portal.getPortal_x() + wall_width,
				portal.getPortal_y()));
		walls.get(walls.size() - 1).isPortal = true;
		walls.get(walls.size() - 1).portal_id = portal;
		walls.get(walls.size() - 1).setTexture(ImageLoader.loadImage("res/textures/important/missing_texture.png"));
	}

	public void fog_settings(ThreeValue color, double intensity) {
		fog_intensity = intensity;
		fog_color = color;
	}

	public void renderInfo(Graphics2D g, List<Entity> entities, Camera c) {
		g.setColor(Color.WHITE);
		g.setFont(info_font);
		
		int changer = 64;
		int mod = 16;
		
		g.drawString("FPS: " + Gameloop.display_frames, 10, changer-mod);
		g.drawString("Global Position: " + c.getX() + " , " + c.getY(), 10, changer);
		g.drawString("Map Name: " + Display.current_map, 10, changer+mod*1);
		g.drawString("Walls In Session: " + walls.size(), 10, changer+mod*2);
		g.drawString("Columns Being Rendered: " + wall_hit_amount, 10, changer+mod*3);
		g.drawString("Portals In Session: " + portal_rays.size(), 10, changer+mod*4);
		
		g.setFont(ent_font);
		for(int i = 0; i < entities.size(); i++) {
			g.drawString("SPR: " + entities.get(i).sprite + " Dist: " + entities.get(i).distance + " Index: " + entities.get(i).rindex + " Entity Frame: " + entities.get(i).clearFrame, 10, (changer+mod*(5+i)));
		}
		wall_hit_amount = 0;
	}

	public double fixAngle(double d) {
		if (d > 3.14 * 2) {
			d -= 3.14 * 2;
		}
		if (d < 0) {
			d += 3.14 * 2;
		}
		return d;
	}
	
	int fc= 0 ;
	
	private Positional_Vert test = new Positional_Vert(new ThreeValue(620,0,623), new ThreeValue(0,0,0));
	private Point test_point;
	
	private void swap(List<Entity> dists, int i1, int i2) {
		Entity tempVal = dists.get(i1);
		dists.set(i1, dists.get(i2));
		dists.set(i2, tempVal);
	}
	
	private int partition(List<Entity> dists, int low, int high) {
		double pivot = dists.get(high).distance;
		int i = low;
		for(int j = low; j < high; j++) {
			if(dists.get(j).distance < pivot) { 
				swap(dists, i, j);
				i++;
			}
		}
		swap(dists, i, high);
		return i;
	}
	
	public void sort(List<Entity> ents, int low, int high) {
		if(low < high) {
			int p = partition(ents, low, high);
			sort(ents, low, p-1);
			sort(ents, p + 1, high);
		}
	}
	
	public void sortEnts(List<Entity> ents, Camera c, double[] dists) {

 		for(int i = 0; i < ents.size()-1; i++) {
 		
 			double d1 = ExtraMath.distance(c.getX(), c.getY(), ents.get(i).x, ents.get(i).y);
 			ents.get(i).distance = d1;
  			
		}
 		sort(ents, 0, ents.size()-1);
 		
	}
	
	public void renderScene3D(Graphics2D g, ImageObserver io, Camera c, Skybox sky, List<Entity> entities) {
		BufferedImage texture = null;
		projected_height_data = new int[rays.size()];

		g.drawImage(floor_buffer, 0, 0, io);
		
		// sky.render(io, g);
		for(int ents = 0; ents < entities.size(); ents++) {

			entities.get(ents).clearFrame = true;
			entities.get(ents).rindex = -1;
		
		}
		
		for (int ray = 0; ray < rays.size(); ray++) {
			
		
			
			
			for (int wall = 0; wall < walls.size(); wall++) {
					rays.get(ray).raycast(walls, wall);
				if (rays.get(ray).hitSomething()) {
					wall_hit_amount++;
				}

				c.checkCollisions(walls, wall);
			}
			
			rays.get(ray).entity.clear();
			rays.get(ray).castingEntity = false;

			
			for(int ents = 0; ents < entities.size(); ents++) {
				
				
					rays.get(ray).raycastEntity(entities.get(ents));
				
			}
			
			double wall_heights = (wall_height * distToProjectionPlane / rays.get(ray).getDistance());
			double wall_end = (camera_height + (wall_heights * 0.5));
			double wall_start = (camera_height - (wall_heights * 0.5));

			int projected_plane = (int) ((camera_height + (wall_heights * 0.5))
					- (camera_height - (wall_heights * 0.5)));

			// System.out.println(projected_plane);

			if (ray == rays.size() / 2) {
				if (projected_plane > 3000) {
					c.stop_up = true;
				} else {
					c.stop_up = false;
				}
			}

			int offset;

			if (rays.get(ray).getWallType()) {
				offset = (int) ExtraMath.fast_mod((int) (rays.get(ray).getX2()), wall_width);
			} else {
				offset = (int) ExtraMath.fast_mod((int) (rays.get(ray).getY2()), wall_width);
			}

			if (offset >= wall_width) {
				offset -= wall_width;
			}

			float height_map = 0.f;

			if ((int) (Window.WIDTH * rays.get(ray).getTextureID()) + offset + 16 < image_size) {
				// if(rays.get(ray).getWall().isEntity) texture =
				// rays.get(ray).getWall().entity.sprites.get((int)(Window.WIDTH*rays.get(ray).getEntityTextureID())+offset+16);
				// else
				texture = ImageLoader.wall_textures
						.get((int) (Window.WIDTH * rays.get(ray).getTextureID()) + offset + 16);
			}

			int offset_height = 1;
			int height_map_multiplyer = 16;

			// if using 512, use a different val. Default is 1 at 64 wall width.

			if (wall_width != 1) {
				offset_height = 8;
			}

			
			
			if (rays.get(ray).getWall().isHas_height()) {
				if ((int) (Window.WIDTH * rays.get(ray).getTextureID()) + offset + 16 < image_size)
					height_map = ImageLoader.height_values
							.get(((int) (Window.WIDTH * rays.get(ray).getTextureID()) + offset + 16) / offset_height)
							/ 255.f;
				height_map *= height_map_multiplyer;
				g.drawImage(texture, ray, (int) wall_start, 1, projected_plane, io);
				if (!rays.get(ray).getWallType()) {
					g.drawImage(texture, ray - (int) height_map, (int) wall_start, 1 * (int) height_map,
							projected_plane, io);
					// g.fillRect(ray - (int)height_map, (int)wall_start, 1*(int)height_map,
					// projected_plane);
				} else {
					g.drawImage(texture, ray - (int) height_map, (int) wall_start, -1 * (int) height_map,
							projected_plane, io);
					// g.fillRect(ray - (int)height_map, (int)wall_start, -1*(int)height_map,
					// projected_plane);
				}
			} else {
				g.drawImage(texture, ray, (int) wall_start, 1, projected_plane, io);
			}

			int tx, ty;
			
		
			int ray_rot;
			ray_rot = (int) (Math.toDegrees(rays.get(ray).getDegrees()) * 100);

			for (int i = (int) wall_end - 1; i < Window.HEIGHT - 1; i++) {

				double distance = ((float) (wall_height / changer) / (i - camera_height)) * distToProjectionPlane;

				tx = ((int) (distance * (angles.cos[Math.abs(ray_rot)]))) + c.getX();
				ty = ((int) (distance * (angles.sin[Math.abs(ray_rot)]))) + c.getY();

				int tex_x = (int) tx & 63;
				int tex_y = (int) ty & 63;

				int tex = tex_y * wall_width + tex_x;
				int pixel_index = (int) ExtraMath.fast_mod((i * projection_width + ray), 1);

				int red_color = 1;
				int green_color = 1;
				int blue_color = 1;

				for (int light_index = 0; light_index < lights.size(); light_index++) {
					float light = (lights.get(light_index).calculateLighting(tx, ty) + 1);

					int r = (int) (lights.get(light_index).getColor().getX() * light);
					int gg = (int) (lights.get(light_index).getColor().getY() * light);
					int b = (int) (lights.get(light_index).getColor().getZ() * light);

					if (light > 1.0) {
						red_color += r;
						green_color += gg;
						blue_color += b;
					}
				}

				// Reflection Effect

				int red_color_ref = red_color;
				int green_color_ref = green_color;
				int blue_color_ref = blue_color;
				if (lights.size() > 0) {
					red_color_ref /= lights.size();
					green_color_ref /= lights.size();
					blue_color_ref /= lights.size();
				}

				red_color_ref = (int) ExtraMath.clamp(red_color_ref, 0, 255);
				green_color_ref = (int) ExtraMath.clamp(green_color_ref, 0, 255);
				blue_color_ref = (int) ExtraMath.clamp(blue_color_ref, 0, 255);

				red_color = (int) ExtraMath.clamp(red_color, 0, 255);
				green_color = (int) ExtraMath.clamp(green_color, 0, 255);
				blue_color = (int) ExtraMath.clamp(blue_color, 0, 255);
				

				
				int final_color = (red_color << 16) | (green_color << 8) | blue_color;
				int final_color_ref = (red_color_ref << 16) | (green_color_ref << 8) | blue_color_ref;

				int final_color_2 = ExtraMath.blend_color(final_color, imagePixelData[tex], (i)/(700.));
		
				

				int final_color_reflection = ExtraMath.blend_color(imagePixelData[tex], final_color_ref, 0.9);

				floor_buffer_pixels[pixel_index++] = final_color_2 >> 16;
				floor_buffer_pixels[pixel_index++] = final_color_2 >> 8;
				floor_buffer_pixels[pixel_index++] = final_color_2;
				floor_buffer_pixels[pixel_index] = 255;

				floor_buffer.getRaster().setPixel(ray, i, floor_buffer_pixels);
				

				// floor_buffer.getRaster().setPixel(ray, i, floor_buffer_pixels);
			}

			if (!disableceil) {

				for (int i = (int) wall_start + 1; i > 0; i--) {

					double distance = ((float) (wall_height / changer) / (i - camera_height)) * distToProjectionPlane;

					tx = ((int) (-distance * (angles.cos[Math.abs(ray_rot)]))) + c.getX();
					ty = ((int) (-distance * (angles.sin[Math.abs(ray_rot)]))) + c.getY();

					int tex_x = (int) tx & 63;
					int tex_y = (int) ty & 63;

					int tex = tex_y * wall_width + tex_x;
					int pixel_index = (int) ExtraMath.fast_mod((i * projection_width + ray), 1);

					int red_color = 1;
					int green_color = 1;
					int blue_color = 1;

					for (int light_index = 0; light_index < lights.size(); light_index++) {
						float light = (lights.get(light_index).calculateLighting(tx, ty) + 1);

						int r = (int) (lights.get(light_index).getColor().getX() * light);
						int gg = (int) (lights.get(light_index).getColor().getY() * light);
						int b = (int) (lights.get(light_index).getColor().getZ() * light);

						if (light > 1.0) {
							red_color += r;
							green_color += gg;
							blue_color += b;
						}
					}

					// Reflection Effect

					int red_color_ref = red_color;
					int green_color_ref = green_color;
					int blue_color_ref = blue_color;

					if (lights.size() > 0) {
						red_color_ref /= lights.size();
						green_color_ref /= lights.size();
						blue_color_ref /= lights.size();
					}

					red_color_ref = (int) ExtraMath.clamp(red_color_ref, 0, 255);
					green_color_ref = (int) ExtraMath.clamp(green_color_ref, 0, 255);
					blue_color_ref = (int) ExtraMath.clamp(blue_color_ref, 0, 255);

					red_color = (int) ExtraMath.clamp(red_color, 0, 255);
					green_color = (int) ExtraMath.clamp(green_color, 0, 255);
					blue_color = (int) ExtraMath.clamp(blue_color, 0, 255);

					int final_color = (red_color << 16) | (green_color << 8) | blue_color;
					int final_color_ref = (red_color_ref << 16) | (green_color_ref << 8) | blue_color_ref;

					int final_color_2 = ExtraMath.blend_color(final_color, imagePixelDataCeil[tex], (i)/(700.));

					int final_color_reflection = ExtraMath.blend_color(imagePixelDataCeil[tex], final_color_ref, 0.9);

					floor_buffer_pixels[pixel_index++] = final_color_2 >> 16;
					floor_buffer_pixels[pixel_index++] = final_color_2 >> 8;
					floor_buffer_pixels[pixel_index++] = final_color_2;
					floor_buffer_pixels[pixel_index] = 255;

					floor_buffer.getRaster().setPixel(ray, i, floor_buffer_pixels);
				}
			} else {
				for (int i = (int) wall_start + 1; i > 0; i--) {
					int pixel_index = (int) ExtraMath.fast_mod((i * projection_width + ray), 1);

					double distance = ((float) (ceiling_image.getHeight() * 4 / changer) / (i - camera_height))
							* distToProjectionPlane;

					tx = ((int) (-distance * (angles.cos[Math.abs(ray_rot)]))) + c.getX();
					ty = ((int) (-distance * (angles.sin[Math.abs(ray_rot)]))) + c.getY();

					int tex_x = (int) tx;
					int tex_y = (int) ty;

					int tex = tex_y * wall_width + tex_x;

					int final_color_2 = imagePixelDataCeil[Math.abs((tex % 1048576) - 1)];

					floor_buffer_pixels[pixel_index++] = (final_color_2 >> 16);
					floor_buffer_pixels[pixel_index++] = (final_color_2 >> 8);
					floor_buffer_pixels[pixel_index++] = (final_color_2);
					floor_buffer_pixels[pixel_index] = 255;

					floor_buffer.getRaster().setPixel(ray, i, floor_buffer_pixels);
				}

			}
			

		
			 
			fc = (int) ExtraMath.clamp(rays.get(ray).getDistance() / fog_intensity, 1, 255);
			
			g.setColor(fogcolors[fc - 1]);
			g.fillRect(ray, (int) wall_start, 1, projected_plane);
			
			
			
			rays.get(ray).render(g, false);
			g.setColor(Color.white);
			

			
		}

	}
	

	
	//oh my lordy lord, it is NOT detecting entities behind something.
	
	
	public void renderEntities(List<Entity> entities, Graphics2D g, ImageObserver io) {
		//ENTITY START
		
		
		for(int entity = 0; entity < entities.size(); entity++) {
			if(entities.get(entity).onScreen && entities.get(entity).rindex > 0) {
		
		int sps = wall_width; //proportil reasoning from 512/64
		
		//double prop = wall_width/sps;
		
		double entity_heights = (sps * (distToProjectionPlane) / rays.get(entities.get(entity).rindex).getEntityDistance());
		double entity_end = ((camera_height) + (entity_heights*0.5));
		double entity_start = ((camera_height) - (entity_heights*0.5));
	
		double final_dis = (entity_end-entity_start);
		
		
		double size_changer = 1.5;
		
		BufferedImage spr = null;
		BufferedImage img = null;
		try {
			spr = ImageIO.read(new File(entities.get(entity).sprite));
			img = new BufferedImage(spr.getWidth(), spr.getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = img.createGraphics();
			g2.setXORMode(new Color(255,255,255));
			g2.setComposite(AlphaComposite.SrcOver.derive((float)entities.get(entity).distance/1000));
			g2.setColor(Color.BLACK);
			//g2.drawImage(spr, 0,  (int) ((entity_end)-final_dis/size_changer), spr.getWidth(), spr.getHeight(), io);
			g2.fillRect(0, (int) ((entity_end)-final_dis/size_changer), spr.getWidth(), spr.getHeight());
			g2.dispose();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//System.out.println(spr.getHeight());
		g.drawImage(spr, entities.get(entity).rindex, (int) ((entity_end)-final_dis/size_changer), (int)(final_dis/size_changer), (int)(final_dis/size_changer), io);
		
		//g.drawImage(img, entities.get(entity).rindex, (int) ((entity_end)-final_dis/size_changer), (int)(final_dis/size_changer), (int)(final_dis/size_changer), io);
		
		//g.fillRect(entities.get(entity).rindex, (int) ((entity_end)-final_dis/size_changer), (int)(final_dis/size_changer), (int)(final_dis/size_changer));
		
		//}
			}
			
		}
	}

	
	
}
