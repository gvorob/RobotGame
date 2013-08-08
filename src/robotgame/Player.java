/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package robotgame;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 *
 * @author George Vorobyev <quaffle97@gmail.com>
 */
public class Player {
    final float gravity = -3;
    
    float speed;
    float jspeed;
    float zvel;
    float xpos;
    float ypos;
    float zpos;
    
    Entity sprite;
    
    public Player(float x, float y, float z)
    {
        speed = 1;//block/second
        jspeed = 2;
        zvel = 0;
        xpos = x;
        ypos = y;
        zpos = z;
        
        sprite = new Entity(xpos - 1, ypos - 1, zpos, 110, 32, 1, 0, 64, 128);
    }
    
    public void update(float time, Keyboard keys, World w)//time passed in seconds
    {
        
        if(keys.getKey(KeyEvent.VK_W))
        {
            xpos -= speed * time;
            ypos += speed * time;
        }
        if(keys.getKey(KeyEvent.VK_A))
        {
            xpos -= speed * time;
            ypos -= speed * time;
        }
        if(keys.getKey(KeyEvent.VK_S))
        {
            xpos += speed * time;
            ypos -= speed * time;
        }
        if(keys.getKey(KeyEvent.VK_D))
        {
            xpos += speed * time;
            ypos += speed * time;
        }
        if(keys.getKey(KeyEvent.VK_SPACE))
        {
            zvel = jspeed;
        }
        
        zvel += gravity * time;
        Misc.prln(String.valueOf(xpos) + ' ' + String.valueOf(ypos));
        if(w.tiles[(int)xpos][(int)ypos][(int)zpos] != 0)
        {
            zvel = 0;
            zpos = (float)Math.floor(zpos + 1);
        }
        zpos += zvel * time;
        
    }
    
    public Entity draw()
    {
        sprite.xpos = xpos;// + 1;
        sprite.ypos = ypos;// - 1;
        sprite.zpos = zpos;
        return sprite;
    }
}
