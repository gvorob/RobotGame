/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package robotgame;

/**
 *
 * @author George Vorobyev <quaffle97@gmail.com>
 */
public class Tile {
    public int sprite;
    public int type;
    
    public Tile(int s, int t)
    {
        sprite = s;
        type = t;
    }
    
    public Tile clone()
    {
        return new Tile(sprite,type);
    }
}
