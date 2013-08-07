/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package robotgame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author George Vorobyev <quaffle97@gmail.com>
 */
public class World {
    public BufferedImage sprites;
    public int[][][] tiles;//x,y,z
    int[] xyz;//holds the map size
    
    public World(String fileName)
    {
        String[] input;//holds lines of tile data, each line is one xy slice
        try {
            sprites = ImageIO.read(new File("tiles.png"));
        } catch (IOException ex) {
            Logger.getLogger(World.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
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
                    tiles[ix][iy][iz] = Integer.parseInt(String.valueOf(input[iz].charAt(index)));
                    index++;                    
                }
            }
        }
    }
    
    public void draw(BufferedImage b)
    {
        Graphics2D g = b.createGraphics();
        //g.setColor(Color.red);
        //g.drawRect(0, 0, 10, 10);
        //g.drawImage(sprites, 0, 0, null);
        
        
        for(int z = 0; z < xyz[2];z++)
        {
            int staggerHeight = xyz[0] + xyz[1] - 1;
            int x = 0;
            int y = xyz[1] - 1;
            for(int i = 1; i <= staggerHeight;i++)
            {
                int diff = staggerHeight-i + 1;
                int staggerWidth = diff>=i?i:diff;
                for(int j = 0; j < staggerWidth;j++)
                {
                    int ax = x + j;
                    int ay = Math.abs(y) + j; 
                    //Misc.prln(String.valueOf(ax) + ' ' + String.valueOf(ay));

                    int xcor = 60 + 16 * ax + 16 * ay;
                    int ycor = 60 + (8 * ax) + (-8 * ay) + (-16 * z);
                    int tileId = tiles[ax][ay][z];
                    //if(tiles[ax][ay][z] != 1)
                    g.drawImage(sprites,  xcor, ycor, xcor + 32, ycor + 64,tileId * 32, 0, tileId * 32 + 32, 64, null);
                    //else if(tiles[ax][ay][z] == 0)
                    //g.drawImage(sprites,  xcor, ycor, xcor + 32, ycor + 64,0, 0, 32, 64, null);

                }
                if(y != 0)
                    y--;
                else
                    x++;

            }
        }
        /*for(int x = 0; x < xyz[0];x++)
        {
            for(int y=0; y < xyz[1];y++)
            {
                
            }
        }*/
    }
}
