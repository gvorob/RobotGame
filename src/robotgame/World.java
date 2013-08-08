/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package robotgame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author George Vorobyev <quaffle97@gmail.com>
 */
public class World {
    public static final int MODE_PLAY = 0;
    public static final int MODE_EDITOR = 1;
    
    
    public BufferedImage[] sprites;
    public ArrayList<Entity> entities;
    public Player player;
    public Tile[][][] tiles;//x,y,z
    public int mode;
    int[] xyz;//holds the map size
    
    public final int viewX = 100;
    public final int viewY = 200;
    
    private int editx,edity,editz;
    private Tile editTile,editAlt;//editAlt is the tile currently under the cursor, used for swapping-out reasons
    
    public World(String fileName)
    {
        mode = MODE_PLAY;
        editTile = new Tile(0,0);
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
    
    
    private void parseTiles(String[] input)
    {
        tiles = new Tile[xyz[0]][xyz[1]][xyz[2]];//create an int[][][] with size specified in xyz
        for(int iz = 0;iz < xyz[2];iz++)
        {
            int index = 0;
            for(int iy = 0;iy < xyz[1];iy++)
            {
                for(int ix = 0;ix < xyz[0];ix++)
                {
                    tiles[ix][iy][iz] = new Tile(input[iz].charAt(index * 2),input[iz].charAt(index * 2 + 1));
                    index++;                    
                }
            }
        }
    }
    
    public void update(float time, Keyboard keys)//per-frame game updates
    {
        if(mode == MODE_PLAY)
        {
            player.update(time, keys, this);
            if(keys.getKey(KeyEvent.VK_F1))
                mode = MODE_EDITOR;
        }
        else if(mode == MODE_EDITOR)
        {
            if(keys.getKeyPressed(KeyEvent.VK_SPACE))
                tiles[editx][edity][editz] = editTile.clone();
            
            
            if(keys.getKeyPressed(KeyEvent.VK_W))
                edity += 1;
            if(keys.getKeyPressed(KeyEvent.VK_S))
                edity -= 1;
            if(keys.getKeyPressed(KeyEvent.VK_A))
                editx -= 1;
            if(keys.getKeyPressed(KeyEvent.VK_D))
                editx += 1;
            if(keys.getKeyPressed(KeyEvent.VK_SHIFT))
                editz += 1;
            if(keys.getKeyPressed(KeyEvent.VK_CONTROL))
                editz -= 1;
            if(keys.getKeyPressed(KeyEvent.VK_MINUS))
                editTile.sprite -= 1;
            if(keys.getKeyPressed(KeyEvent.VK_EQUALS))
                editTile.sprite += 1;
            if(keys.getKeyPressed(KeyEvent.VK_OPEN_BRACKET))
                editTile.type -= 1;
            if(keys.getKeyPressed(KeyEvent.VK_CLOSE_BRACKET))
                editTile.type += 1;
            
            //fix any out-of-bounds errors
            if(editx < 0)
                editx = 0;
            if(editx >= xyz[0])
                editx = xyz[0] - 1;
            if(edity < 0)
                edity = 0;
            if(edity >= xyz[1])
                edity = xyz[1] - 1;
            if(editz < 0)
                editz = 0;
            if(editz >= xyz[2])
                editz = xyz[2] - 1;
            
            if(keys.getKey(KeyEvent.VK_ESCAPE))
                mode = MODE_PLAY;
        }
    }
    
    
    public void draw(BufferedImage b)
    {
        Graphics2D g = b.createGraphics();
        
        entities = new ArrayList<Entity>();
        if(mode == MODE_PLAY)
            entities.add(player.draw());
        if(mode == MODE_EDITOR)
        {
            editAlt = tiles[editx][edity][editz];
            tiles[editx][edity][editz] = editTile;
        }
        
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
                    int ay = y + j; 
                    //Misc.prln(String.valueOf(ax) + ' ' + String.valueOf(ay));

                    int xcor = viewX + 0 + 16 * ax + 16 * ay;
                    int ycor = viewY + -55 + (8 * ax) + (-8 * ay) + (-16 * z);
                    int tileSprite = tiles[ax][ay][z].sprite;
                    g.drawImage(sprites[0],  xcor, ycor, xcor + 32, ycor + 64,tileSprite * 32, 0, tileSprite * 32 + 32, 64, null);

                }
                
                drawEntities(z, x - y,g);//draws all entities at z-level z, x-y defines the depth so it occludes each other
                
                if(y > 0)
                    y--;
                else
                    x++;

            }
        }
        
        if(mode == MODE_EDITOR)
        {
            tiles[editx][edity][editz] = editAlt;
            g.setColor(Color.red);
            g.drawString("X,Y,Z: " + String.valueOf(editx) + ", " + String.valueOf(edity) + ", " + String.valueOf(editz), 0, 10);
            g.drawString("Tile Sprite/Type: " + String.valueOf(editTile.sprite) + ", " + String.valueOf(editTile.type), 0, 20);
        }
    }
    
    private void drawEntities(int z, int camDistance, Graphics2D g)//camdistance is the distance from the top-left of the grid, given by x - y
    {
        Iterator<Entity> i = entities.iterator();
        while(i.hasNext())
        {
            Entity current = i.next();
            
            if(z == Math.floor(current.zpos) && camDistance == current.getCamDistance())
            {
                //Misc.prln("--" + String.valueOf(current.getCamDistance()) + "--");
                int xcor = (int)(viewX - current.refx + 16 * current.xpos + 16 * current.ypos);
                int ycor = (int)(viewY - current.refy + (8 * current.xpos) + (-8 * current.ypos) + (-16 * current.zpos));
                int tileCor = current.spriteid * current.spriteWidth;
                g.drawImage(sprites[current.spritesheet],  xcor, ycor, xcor + current.spriteWidth, ycor + current.spriteHeight, tileCor, 0, tileCor + current.spriteWidth, current.spriteHeight, null);
                
            }
        }
    }
    
    public float getHeight(float x, float y, float z)
    {
        if(x < 0 || y < 0 || z < 0 || x >= xyz[0] || y >= xyz[1] || z >= xyz[2])
        {
            return 1;
        }
        int tile = tiles[(int)x][(int)y][(int)z].type;
        switch (tile)
        {
            case 0: //empty
                return -1;
            case 1://Solid block
                return 1;
            //case 2://Floor
            //    return 0;
            default:
                return -1;
        }
                
        
    }
}
