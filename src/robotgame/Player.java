/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package robotgame;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 *
 * @author George Vorobyev <quaffle97@gmail.com>
 */
public class Player {
    final float gravity = -8;
    float anim;
    float speed;
    float jspeed;
    float zvel;
    float xpos;
    float ypos;
    float zpos;
    
    Entity sprite, shadow;
    ArrayList<Entity> spriteArray;
    
    public Player(float x, float y, float z)
    {
        speed = 3;//block/second
        jspeed = 5;
        zvel = 0;
        xpos = x;
        ypos = y;
        zpos = z;
        
        sprite = new Entity(xpos - 1, ypos - 1, zpos, 115, 30, 1, 1, 64, 128);
        shadow = new Entity(xpos - 1, ypos - 1, zpos, 110, 30, 1, 3, 64, 128);
        spriteArray = new ArrayList<>();
        spriteArray.add(shadow);
        spriteArray.add(sprite);
    }
    
    public void update(float time, Keyboard keys, World w)//time passed in seconds
    {
        anim += 1;
        if(anim == 1)
            sprite.spriteid = 0;
        else if(anim == 2)
            sprite.spriteid = 1;
        else if(anim == 3)
            sprite.spriteid = 2;
        else if(anim == 4)
            sprite.spriteid = 2;
        else if(anim == 5)
            sprite.spriteid = 1;
        else if(anim == 6)
        {
            sprite.spriteid = 0;
            anim = 0;
        }
        
        
        float dx = 0;
        float dy = 0;
        if(keys.getKey(KeyEvent.VK_W))
        {
            dx -= speed * time;
            dy += speed * time;
        }
        if(keys.getKey(KeyEvent.VK_A))
        {
            dx -= speed * time;
            dy -= speed * time;
        }
        if(keys.getKey(KeyEvent.VK_S))
        {
            dx += speed * time;
            dy -= speed * time;
        }
        if(keys.getKey(KeyEvent.VK_D))
        {
            dx += speed * time;
            dy += speed * time;
        }
        
        dx += xpos;//dx is now the predicted location after moving along the x axis
        dy += ypos;//ditto
        
        if(dx < 0)
            xpos = 0;
        else if(dx >= w.xyz[0])
            xpos = w.xyz[0] - .01f;
        else if(w.getHeight(dx,ypos,zpos) <= (zpos - (int)zpos))
            xpos = dx;
        
        if(dy < 0)
            ypos = 0;
        else if(dy >= w.xyz[1])
            ypos = w.xyz[1] - .01f;
        else if(w.getHeight(xpos,dy,zpos) <= (zpos - (int)zpos))
            ypos = dy;
        
        if(keys.getKeyPressed(KeyEvent.VK_SPACE))
        {
            zvel = jspeed;
        }
        
        zvel += gravity * time;
        zpos += zvel * time;
        //Misc.prln(String.valueOf(xpos) + ' ' + String.valueOf(ypos) + ' ' + String.valueOf(zpos));
        
        if(zpos < 0)
            zpos = 0;
        else if(zpos < w.xyz[2] && w.tiles[(int)xpos][(int)ypos][(int)zpos].type != 0)
        {
            zvel = 0;
            zpos = (float)Math.floor(zpos + 1);
        }
        
        
        sprite.xpos = xpos;
        sprite.ypos = ypos;
        sprite.zpos = zpos;
        shadow.xpos = xpos;
        shadow.ypos = ypos;
        shadow.zpos = zpos;
        
        boolean flag = true;
        while(flag)
        {
            float h = w.getHeight(xpos, ypos, shadow.zpos - 1);
            if(h == 1)
            {
                shadow.zpos = (int)shadow.zpos;
                flag = false;
            }
            else
                shadow.zpos = shadow.zpos - 1;
        }
        
    }
    
    public ArrayList<Entity> draw()
    {
        
        
        
        return spriteArray;
    }
}
