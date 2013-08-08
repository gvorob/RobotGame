/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package robotgame;

/**
 *
 * @author George Vorobyev <quaffle97@gmail.com>
 */
public class Entity {
    float zpos;
    float xpos;
    float ypos;
    int refx, refy;
    int spritesheet;
    int spriteid;
    int spriteWidth;
    int spriteHeight;
    
    public Entity(float x, float y, float z, int ry, int rx, int ss, int si, int sw, int sh)
    {
        xpos = x;
        ypos = y;
        zpos = z;
        spritesheet = ss;
        spriteid = si;
        refy = ry;
        refx = rx;
        spriteWidth = sw;
        spriteHeight = sh;
    }
    
    public int getCamDistance()
    {
        return  (int)( Math.floor(xpos) - Math.floor(ypos));
    }
}
