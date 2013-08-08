/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package robotgame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import sun.util.BuddhistCalendar;

/**
 *
 * @author George Vorobyev <quaffle97@gmail.com>
 */
public class World {
    public BufferedImage[] sprites;
    public ArrayList<Entity> entities;
    public Player player;
    public int[][][] tiles;//x,y,z
    int[] xyz;//holds the map size
    
    public final int viewX = 100;
    public final int viewY = 200;
    
    public World(String fileName)
    {
        String[] input;//holds lines of tile data, each line is one xy slice
        try {
            sprites = new BufferedImage[2];
            sprites[0] = ImageIO.read(new File("tiles.png"));
            sprites[1] = ImageIO.read(new File("entities.png"));
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
        
        entities = new ArrayList<>();
        player = new Player(2.5f, 2.5f, 1);
        
    }
    
    public void update(float time, Keyboard keys)
    {
        player.update(time, keys, this);
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
        entities.add(player.draw());
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

                    int xcor = viewX + 0 + 16 * ax + 16 * ay;
                    int ycor = viewY + -55 + (8 * ax) + (-8 * ay) + (-16 * z);
                    int tileId = tiles[ax][ay][z];
                    g.drawImage(sprites[0],  xcor, ycor, xcor + 32, ycor + 64,tileId * 32, 0, tileId * 32 + 32, 64, null);

                }
                
                //drawEntities(z,i,g);//draws all entities at z-level z, height i from top of screen
                
                if(y != 0)
                    y--;
                else
                    x++;

            }
        }
        drawEntities(g);//z,i,g);//draws all entities at z-level z, height i from top of screen
    }
    
    private void drawEntities(Graphics2D g)//int depth, int scrHeight, Graphics2D g)//scrHeight is the distance from the top-left of the grid
    {
        Iterator<Entity> i = entities.iterator();
        while(i.hasNext())
        {
            Entity current = i.next();
            
            if(true)//Math.floor(current.zpos) == depth)
            {
                int xcor = (int)(viewX - current.refx + 16 * current.xpos + 16 * current.ypos);
                int ycor = (int)(viewY - current.refy + (8 * current.xpos) + (-8 * current.ypos) + (-16 * current.zpos));
                int tileCor = current.spriteid * current.spriteWidth;
                //if(tiles[ax][ay][z] != 1)
                g.drawImage(sprites[current.spritesheet],  xcor, ycor, xcor + current.spriteWidth, ycor + current.spriteHeight, tileCor, 0, tileCor + current.spriteWidth, current.spriteHeight, null);
                
            }
        }
    }
}
