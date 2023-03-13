import java.util.*;

public class MazeGenerator {
    
    private static Random random = new Random();
    
    public static void main(String args[]) {
        
        int[][] map = new int[15][15];
        
        int num_lines = 3;
        
        for(int i = 0; i < num_lines; i++) {
            generateMap(map);
        }
        
        
        for(int x = 0; x < map.length; x++) {
            for(int y = 0; y < map[x].length; y++) {
                System.out.print(map[x][y]);
            }
            System.out.println();
        }
        
    }
    
    public static void generateMap(int[][] map) {
        
        int x_rand = random.nextInt(15);
        int y_rand = random.nextInt(15);
        
        int flip = random.nextInt(2);
        
        boolean f_;
        
        if(flip >=1) {
            f_ = true;
        } else {
            f_ = false;
        }
        
        int curr_y = y_rand;
        int max_y = 15;
        
        int curr_x = x_rand;
        int max_x = 15;
        
        
        if(f_) {
        while(curr_y < max_y) {
            map[x_rand][curr_y] = 3;
            curr_y++;
        } 
            
        } else {
            while(curr_x < max_x) {
            map[curr_x][y_rand] = 3;
            curr_x++;
        } 
        }

    }
}
