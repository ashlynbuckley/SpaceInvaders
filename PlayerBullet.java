package SpaceInvaders;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Graphics;
import java.awt.Image;

public class PlayerBullet extends Sprite2D {
	
	public PlayerBullet(Image i) {
		super(i);
		//this is done in each constructor individually as if i did it in sprite2D's constructor, i was getting zeros in return and breaking collision detection
		width = myImage.getWidth(null);
        height = myImage.getHeight(null);
	}
	
	public boolean move() {
		//if it hasnt collided and hasnt gone out of bounds, y -= 10
		while (x != 0) {
			y -= 6;
			return false;
		}
		if (x == 0) { //this will be either it reaches the top (didnt hit)
			return true;
		}
		return false;
	}	
}
