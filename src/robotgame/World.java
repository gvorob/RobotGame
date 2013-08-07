/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package robotgame;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author George Vorobyev <quaffle97@gmail.com>
 */
public class World {
    public int[][][] tiles;//x,y,z
    int[] xyz;//holds the map size
    
    public World(String fileName)
    {
        String[] input;//holds lines of tile data, each line is one xy slice
        
        BufferedReader b = null;
        try {
            File f = new File(fileName);
            b = new BufferedReader(new FileReader(f));
            
            String[] params = b.readLine().split(",");//the first line has the size of the map in "x,y,z," form
            xyz = new int[3];
            for(int i = 0;i<3;i++)
            {
                xyz[i] = Integer.parseInt(params[i]);//parse map size to ints
            }
            
            input = new String[xyz[2]];//Array to hold each line of input, which will correspond to one xy slice
            
            for(int i = 0;i < xyz[2]; i++)
            {
                input[i] = b.readLine();
            }
            
            parseTiles(input);
            
        }
        catch(Exception ex)
        {
            Logger.getLogger(World.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                b.close();
            } catch (IOException ex) {
                Logger.getLogger(World.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void parseTiles(String[] input)
    {
        tiles = new int[xyz[0]][xyz[1]][xyz[2]];//create an int[][][] with size specified in xyz
        for(int iz = 0;iz < xyz[2];iz++)
        {
            int index = 0;
            for(int iy = 0;iy < xyz[1];iy++)
            {
                for(int ix = 0;ix < xyz[0];ix++)
                {
                    tiles[ix][iy][iz] = input[iz].charAt(index);
                    index++;                    
                }
            }
        }
    }
}
