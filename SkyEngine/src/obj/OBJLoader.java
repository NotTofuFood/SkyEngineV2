package obj;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import maths.ThreeValue;

public class OBJLoader {
	
	private String filename;
	
	private String content;
	
	private List<Positional_Vert> vert_list = new ArrayList<>();
	private List<Positional_Vert> vert_total = new ArrayList<>();
	
	public OBJLoader(String filename) {
		//Seriously, if anyone has a cleaner solution than this please reach out to me.
		this.filename = filename;
		File model_file = new File(filename);
		try {
			Scanner reader = new Scanner(model_file);
			while(reader.hasNext()) {
				content = reader.nextLine();
				if(content.charAt(0) == 'v' && content.charAt(1) == ' ') {
					String one = "";
					String two = "";
					String three = "";
					String compiled = content.replace("v ", "");
					//System.out.println(compiled);
					int curr = 0;
					for(int i = 0; i < compiled.length(); i++) {
						if(compiled.charAt(i) != ' ') {
							switch(curr) {
							case 0:
								one+=compiled.charAt(i);
								if(compiled.charAt(i+1) == ' ') curr++;
							case 1:
								two+=compiled.charAt(i);
								if(compiled.charAt(i+1) == ' ') curr++;
							case 2:
								three+=compiled.charAt(i);
								if(compiled.charAt(i+1) == ' ') curr++;
							default:
								break;
							}
						}
					}
					vert_list.add(new Positional_Vert(new ThreeValue(Double.parseDouble(one), Double.parseDouble(two) ,Double.parseDouble(three)), new ThreeValue(1,1,1)));
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public List<Positional_Vert> verts() {
		return vert_total;
	}
	
}
