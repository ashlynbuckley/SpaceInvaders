package SpaceInvaders;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Graphics;
import java.awt.Image;

public class Alien extends Sprite2D {
	//super inherits from Sprite2D and set the movement speed to 10 (they all have the same movement speed)
	public boolean isAlive = true;
	
	public Alien(Image i, Image i2) {
		super(i, i2);
		xSpeed = 5;
		//this is done in each constructor individually as if i did it in sprite2D's constructor, i was getting zeros in return and breaking collision detection
		width = myImage.getWidth(null);
        height = myImage.getHeight(null);
	}
	
	public boolean moveDown; //used when they've hit the edge
	public boolean move() {
			//adding the speed (how much they move by) to their horizontal position
			x += xSpeed;
			//after each x update, reset their position
			setPosition(x, y);
			
			//if moveDown is true, then add 30 to y and set itself to false, as it has moved down now and doesn't need to
			if (moveDown) {
	            y += 30;
	            moveDown = false;
	        }
			
			//if boundary is hit
			if (x >= 750 || x <= 0) { 
				moveDown = true; //moveDown is set to true
				return true; //return true to the run() method in main and go into the for loop that will change their direction and update their y
			}	
		return false;	
	}
	
	
	public void setPosition(double xx, double yy) {
		x = xx;
		y = yy;
	}
	
	public void reverseDirection() {
		xSpeed = xSpeed*-1;
		//e.g., 10*-1 = -10 and -10*-1 = 10
	}
	
	//setter method for moveDown boolean - used in run() - it will trigger y axis to update
	public void setMoveDown(boolean moveDown) {
        this.moveDown = moveDown;
    }
	
	public void increaseSpeed(Alien alien) {
		//add onto speed
		this.xSpeed += 5;
	}
	
	public boolean checkCollision(Sprite2D other) {
		//bullet and alien
		//or alien and player
		if (this.isAlive && other.getVisibility() ) {
			//alien's data
			int x1 = (int)this.x;
			int y1 = (int)this.y;
			int w1 = this.width;
			int h1 = this.height;
			
			//other potential sprite
			int x2 = (int)other.x;
			int y2 = (int)other.y;
			int w2 = other.width;
			int h2 = other.height;
		
	        if (
	        	( (x1<x2 && x1+w1>x2) || (x2<x1 && x2+w2>x1)
	        	)
	        	&&
	        	( (y1<y2 && y1+h1>y2) || (y2<y1 && y2+h2>y1) )
	        	) {
	        	return true;
	        }
	        		
		}
        
        return false;
	}
	
	//getters
	public int getY() {
		return (int)y;
	}
	
	public int getXSpeed() {
		return (int)xSpeed;
	}
	
	
	//paint the image
	@Override
	public void paint(Graphics g) {
		//if it isn't alive there is no need to paint it
	    if (isAlive) {
	        framesDrawn++;
	        if (framesDrawn % 10 < 5) {
	            g.drawImage(myImage, (int) x, (int) y, null);
	        } else {
	            g.drawImage(myImage2, (int) x, (int) y, null);
	        }
	    }
	}
}
