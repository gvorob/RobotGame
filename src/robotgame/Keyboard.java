


package robotgame;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.ListIterator;

/**
 *
 * @author George
 */
public class Keyboard extends KeyAdapter {
    private boolean[] keys;
    private boolean[] keyPolled;//used to test for keypresses, if a key has been polled and not realeased, it will be true here
    private ArrayList<KeyEventListener> listeners;
    
    public Keyboard(){
        listeners = new ArrayList<KeyEventListener>();
        keys=new boolean[66000];
        keyPolled = new boolean[66000];
    }
    
    public void addListener(KeyEventListener e){listeners.add(e);}    
    public void removeListener(KeyEventListener e){listeners.remove(e);}
    
    public void keyPressed(KeyEvent e){
        keys[e.getKeyCode()]=true;
        inform(e.getKeyCode(), true);
    }
    
    public void keyReleased(KeyEvent e){
        keys[e.getKeyCode()]=false;
        keyPolled[e.getKeyCode()] = false;
        inform(e.getKeyCode(), false);
    }
    
    public boolean getKey(int k){
        return keys[k];
    }
    
    public boolean getKeyPressed(int k){
        if(keys[k] && ! keyPolled[k])
        {
            keyPolled[k] = true;
            return true;
        }
        else
            return false;
    }
    
    private void inform(int index, boolean down)
    {
         ListIterator<KeyEventListener> i = listeners.listIterator();
         while(i.hasNext())
         {
             i.next().KeyChange(index, down);
         }
    }
}
