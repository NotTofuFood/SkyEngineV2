package map;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import obj.Wall;
import renderer.Display;

public class Loader {

	private String map = null;
	
	public void loadMap(String filename) {
		Display.current_map = filename;
		File map_file = new File(filename);
		try {
			Scanner reader = new Scanner(map_file);
			while(reader.hasNext()) {
				map = reader.nextLine();
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void createMap() {
		for(int i = 0; i < map.split(" ").length; i++) {
			if(i % 4 == 0 || i == 0) {
				Display.manager.walls.add(
						new Wall(Double.parseDouble(map.split(" ")[i]), Double.parseDouble(map.split(" ")[i+1]), Double.parseDouble(map.split(" ")[i+2]), 
								Double.parseDouble(map.split(" ")[i+3])));
			}
		}
	}
	
}
