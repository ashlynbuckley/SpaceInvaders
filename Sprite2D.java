package SpaceInvaders;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

abstract public class Sprite2D extends JFrame {
	protected double x,y;
	protected int defaultAlienSpeed = 6;
	protected double xSpeed=0; //so the speed indicates how far it moves each time it updates its position
	protected Image myImage, myImage2;
	protected int framesDrawn = 0;
	protected int width, height;
	protected boolean isAlive;
	
	//constructors
	 // Constructor for classes needing one image
    public Sprite2D(Image i) {
        myImage = i;
        x = (Math.random() * 600);
        y = (Math.random() * 200);
        isAlive = true;
    }
    
	public Sprite2D(Image i, Image i2) {
		//randomise position and set image of sprite
		myImage = i;
		myImage2 = i2;
		x = (Math.random()*600);
		y = (Math.random()*200);
	}
	
	//setting playership's position - starts at the same point every time
	public void setPosition(double xx, double yy) {
		 x = xx;
	     y = yy;	
	}
		
	//setting the speed value, can be changed depending on keypressed or keyreleased
	public void setXSpeed(double dx) {
		xSpeed = dx;
	}
	
	//these are my setter and getter for the visibility boolean isAlive
	public void setVisibility(boolean state) {
		this.isAlive = state;
	}
	public boolean getVisibility() {
		return isAlive;
	}
	//paint the image
		public void paint(Graphics g) {
			g.drawImage(myImage, (int)x, (int)y, null);
		}
}
