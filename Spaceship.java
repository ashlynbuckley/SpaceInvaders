package SpaceInvaders;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Image;

public class Spaceship extends Sprite2D {
	
	public Spaceship(Image i) {
		super(i);
		//this is done in each constructor individually as if i did it in sprite2D's constructor, i was getting zeros in return and breaking collision detection
		width = myImage.getWidth(null);
        height = myImage.getHeight(null);
	}
	
	//continue to update x axis
	public void move() {
		x += xSpeed;
		
		// stop movement at screen edge
		if (x < 0) {
	        x = 0;
	        xSpeed = 0;
	    } else if (x > 790 - myImage.getWidth(null)) {
	        x = 790 - myImage.getWidth(null);
	        xSpeed = 0;
	    }
	}
	
	//setting playership's position - starts at the same point every time
	public void setPosition(double xx, double yy) {
		x = xx;
		y = yy;
	}
	
}
