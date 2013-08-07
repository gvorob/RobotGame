/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package robotgame;

/**
 *
 * @author George
 */
public interface MouseEventListener {
    public boolean mouseClicked(int x, int y, boolean  left, boolean down);
    public boolean mouseMoved(int oldX, int oldY, int x, int y, boolean left, boolean right);
}
