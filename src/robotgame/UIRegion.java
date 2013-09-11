/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package robotgame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

/**
 *
 * @author George Vorobyev <quaffle97@gmail.com>
 */
public class UIRegion {
    public Rectangle region;
    public int uiid;
    public Color[] colors;//0 is default, 1 is mouseover, 2 is mousedown;
    public int state;//0 is default, 1 is mouseover, 
    //2 is mouse clicked and not yet released
    
    public UIListener parent;
    
    public UIRegion(Rectangle rect, int id, Color[] c)
    {
        region = rect;
        uiid = id;
        colors = c;
    }
    
    public UIRegion(Rectangle rect, int id, Color[] c, UIListener listener)
    {
        region = rect;
        uiid = id;
        colors = c;
        parent = listener;
    }
    
    public void update(Mouse m)
    {
        Point p = m.get();
        if(region.contains(p))
        {
            if(m.getL())
                state = 2;
            else 
            {
                if(state == 2)
                informClicked();
                state = 1;
            }
        }
        else
            state = 0;
    }
    
    public void draw(Graphics2D g)
    {
        g.setColor(colors[state]);
        g.fillRect(region.x, region.y, region.width, region.height);
    }
    
    public void informClicked()
    {
        if(parent != null)
            parent.informClicked(uiid);
    }
}
