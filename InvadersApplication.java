package SpaceInvaders;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Image;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Iterator;

//used to switch between states
enum GameState {
	IN_PROGRESS,
	GAME_OVER
}

public class InvadersApplication extends JFrame implements Runnable, KeyListener {

private static final Dimension WindowSize = new Dimension(800,600);
private BufferStrategy strategy;
protected static final int NUMALIENS = 25;

//for game state
protected boolean isGameInProgress; //set to false => "game over"
protected GameState gameState;
protected boolean anyKeyPressed = false; //anyKeyPressed is to get out of game over state "press any key to continue"
protected GameOver gameOverPanel; //my game over state visually is a JPanel that is visible upon the game_over state being active

//aliens and player
protected Alien[] AliensArray = new Alien[NUMALIENS];
protected Spaceship PlayerShip;
private ArrayList bulletsList = new ArrayList();

//images
private static String workingDirectory;
protected Image alienImage;
protected Image playerImage;
protected Image alienImage2;
protected Image bulletImage;

//score
protected int score = 0;
protected int highScore = 0;

//constructor
	public InvadersApplication() {
		//needed for user interactivity
		addKeyListener(this);
		
		//Create and set up the window.
		this.setTitle("Space Invaders");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
		//Display the window, centred on the screen
		Dimension screensize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		int xDim = screensize.width/2 - WindowSize.width/2;
		int yDim = screensize.height/2 - WindowSize.height/2;
		setBounds(100, 100, WindowSize.width, WindowSize.height);
		setVisible(true);
		
		//buffer
		createBufferStrategy(2);
		strategy = getBufferStrategy();
		
		//setting game over
		gameOverPanel = new GameOver();
        gameOverPanel.setBounds(0, 0, 800, 600);
        add(gameOverPanel);
        gameOverPanel.setVisible(false);
		
		//setting the icon
		ImageIcon alienIcon = new ImageIcon(workingDirectory + "\\alien_ship_1.png");
		ImageIcon alienIcon2 = new ImageIcon(workingDirectory + "\\alien_ship_2.png");
		ImageIcon playerIcon = new ImageIcon(workingDirectory + "\\player_ship.png");
		ImageIcon bulletIcon = new ImageIcon(workingDirectory + "\\bullet.png");
		
		//getting the image with the method and setting it to the variables, this will be used when making a sprite2D object
		alienImage = alienIcon.getImage(); 
		alienImage2 = alienIcon2.getImage();
		playerImage = playerIcon.getImage();
		bulletImage = bulletIcon.getImage();
		
		//setting up playership image and position
		PlayerShip = new Spaceship(playerImage);
		PlayerShip.setPosition(370, 550);
		PlayerShip.setVisibility(true);
		
		//fill array
		for (int i = 0; i<NUMALIENS; i++) {
			AliensArray[i] = new Alien(alienImage, alienImage2); //2 images here to alternate between
			AliensArray[i].setVisibility(true); //if i dont set their visibility also here, they're always hidden, even on run
		}
		
		//grid for aliens
		int y = 80;
		int alienCount = 0;
		for (int i = 0; i < 5; i++) {
			int x = 40;
			for (int j = 0; j < 5; j++) {
				AliensArray[alienCount].setPosition(x, y);
				
				alienCount++;
				x += 60;
			}
			y += 60;
		
		}
        
		//set game state
		gameState = GameState.IN_PROGRESS;
        isGameInProgress = true;
        
		//start a thread
		Thread t = new Thread(this);
		t.start();
				
		//repaint();
	}

	public void run() {
		while(true) {
			try {
				Thread.sleep(50);//wait to update screen again
				
				//game states 
				switch (gameState) {
					//if game is in progress
					case IN_PROGRESS:
						//check if the game state has changed since last loop
						updateGameState();
						
						//if game is still progressing, do the normal operations
						if (isGameInProgress) {
							boolean alienHitEdge = false; //used to determine whether y axis needs to be altered and direction reversed
							PlayerShip.move(); //updates the x position of the player as the keys are pressed
							
							//moves the aliens and listens for alienHitEdge to be true
							for (int j = 0; j<NUMALIENS; j++) {  
								if(AliensArray[j].move()) { //so if move returns true, you change alienHitEdge to true and move into the if statement
									alienHitEdge = true;
								} 
								
							}
							if (alienHitEdge) {
								//if they've reached the edge, you need to move them down and reverse the direction of each individual alien
								for (int i = 0; i<NUMALIENS; i++) {
									AliensArray[i].setMoveDown(true);
									AliensArray[i].reverseDirection();
								}
								//once they've all updated, they're not at the edge anymore and are moving the other way - set to false
								alienHitEdge = false; //reset the alienHitEdge boolean after shifting them all to the opposite direction
							}
							//move bullets
							for (Iterator<PlayerBullet> iterator = bulletsList.iterator(); iterator.hasNext(); ) {
							    PlayerBullet bullet = iterator.next();
							    bullet.move();
							    if (bullet.move()) { 
							        iterator.remove(); //remove bullets that have gone out of bounds or collided
							    }
							    //colliding with aliens
							    for (int a = 0; a < NUMALIENS; a++) {
						            if (AliensArray[a].checkCollision(bullet) && AliensArray[a].getVisibility()) {
						                iterator.remove(); //del bullet
						                AliensArray[a].setVisibility(false); //hide
						                score += 10;
						                break;  //break after collision with an alien
						            }
						        }
							}
							
							//colliding with player
							for (int j = 0; j < NUMALIENS; j++) {
							    if (AliensArray[j].checkCollision(PlayerShip) && AliensArray[j].getVisibility()) {
							        PlayerShip.setVisibility(false); //hide
							        isGameInProgress = false; //game over
							        break;
							    }
							}
							//no more aliens, you defeated them all without dying, new wave
							if (areAllDead()) {
							    newWave();
							    break;
							}
							this.repaint(); //repainting them to reflect their movement
							break;
						}//end of in game progress
					
					//if the case is game over, then check if state needs updating and if not continue to show game over panel
					case GAME_OVER:
						updateGameState();
						if (!isGameInProgress) {
							gameOverPanel.setVisible(true);
							if (score > highScore) {
					            highScore = score; //update high score if current score is higher
					            //i'm not going to save the highscore when you exit the game and rerun
					        }
							break;
						}
						
				}
			}
			//exception handling for threads interrupting each other
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void keyPressed(KeyEvent e) {
		//if e is left then do that
		//if e is right then do this
		
		if (e.getKeyCode() == KeyEvent.VK_LEFT) { //if let arrow key is pressed
			//altering the x value of player sprite
			PlayerShip.setXSpeed(-5); //-5 will go left, speed will be direction of left
		}
		else if (e.getKeyCode() == KeyEvent.VK_RIGHT) { //if right arrow key is pressed
			PlayerShip.setXSpeed(5); //move further right by setting speed positive
		}
		
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			shootBullet();
		}
		
		anyKeyPressed = true;
	}
	 
	public void keyReleased(KeyEvent e) {
		//if e is left then do that
		//if e is right then do this
		if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) {
		PlayerShip.setXSpeed(0); //when the key is released set the speed to 0, making it so
		}
		//clear any key pressed events
		//without this, if i killed some but not all and died, it wouldn't correctly enter the game over state, because of key pressing
		//so i need to reset the key press on release
	    anyKeyPressed = false;
	}
	
	//empty because not needed
	public void keyTyped(KeyEvent e) {
		
	}
	
	public void shootBullet() {
		PlayerBullet b = new PlayerBullet(bulletImage);
		//shoots out of player
		b.setPosition(PlayerShip.x+54/2, PlayerShip.y);
		//add to my arraylist
		bulletsList.add(b);
		b.setVisibility(true);
	}
	
	private void drawScore(Graphics g) {
		//"score" and "high score" 
	    g.setColor(Color.white); //set the color of the score 
	    g.setFont(new Font("Arial", Font.PLAIN, 20)); //set font

	   //display string
	    g.drawString("Score: " + score, 270, 60);
	    g.drawString("High score: " + highScore, 380, 60);
	}

	public void paint(Graphics g) {
		g = strategy.getDrawGraphics();
		Color c = Color.black; //background, needs to be repainted every time to avoid ghosting
		g.setColor(c); // Set the colour of g, which is the box
        g.fillRect(0, 0, 800, 800); //size of the window starting from the top corner
			
        drawScore(g); 
        //paint all the aliens - need to be looped, only 1 player ship
	        for (int i = 0; i<NUMALIENS; i++) {
	        	if (AliensArray[i].getVisibility()) {
	        		AliensArray[i].paint(g);
	        	}
	        }
	    //player
	        if (PlayerShip.getVisibility()) {
	        	PlayerShip.paint(g);
	        }
	    //bullets
	        for (Iterator<PlayerBullet> iterator = bulletsList.iterator(); iterator.hasNext();) {
	        	PlayerBullet bullet = iterator.next();
	        	if (bullet.getVisibility()) {
	        		bullet.paint(g);
	        	}
	        }
	    g.dispose();
	    strategy.show();
	    
	}
	
	public void updateGameState() {
		switch (gameState) {
		case IN_PROGRESS:
			//if the game is no longer in progress, you need to switch it to game_over
			if (!isGameInProgress) {
				gameState = GameState.GAME_OVER;
				gameOverPanel.setVisible(true);
			}
			break;
		case GAME_OVER:
			//if you are in game over state and you press a key, it's confirming you wish to continue
			if (anyKeyPressed) {
				resetGame(); 
				isGameInProgress = true; //this boolean will be checked at the next run() loop
				gameState = GameState.IN_PROGRESS;
				anyKeyPressed = false; //resetting the flag
				gameOverPanel.setVisible(false); //hide the game over panel again
			}
			break;
		}
	}
	
	public boolean areAllDead() {
		for (int j = 0; j < NUMALIENS; j++) {
			if (AliensArray[j].getVisibility()) {
				return false; //a visible alien was found, game continues
			}
		}
		return true; //none were visible
	}
	
	public void resetGame() {
	    for (int j = 0; j < NUMALIENS; j++) {
	    	//set position
	        AliensArray[j].setVisibility(true);
	        //grid for aliens
	        int y = 80;
	        int alienCount = 0;
	        for (int i = 0; i < 5; i++) {
	            int x = 40;
	            for (int k = 0; k < 5; k++) {
	                AliensArray[alienCount].setPosition(x, y);
	                x += 60;
	                alienCount++;
	            }
	            y += 60;
	        }
	    }
	    //set the player's visibility and reset score
	    PlayerShip.setVisibility(true);
	    PlayerShip.setPosition(370, 550);
	    score = 0;
	}

	
	public void newWave() {
		for (int i = 0; i < NUMALIENS; i++) {
			//increase every alien's speed - making game harder
			AliensArray[i].increaseSpeed(AliensArray[i]);
		}
		//then once their speed is increased you can reset and start a new wave
		resetGame();
	}
	
	public static void main(String[] args) {
		//used to get path of image in folder
		workingDirectory = System.getProperty("user.dir");
		System.out.println("Working Directory = " + workingDirectory);
		InvadersApplication test = new InvadersApplication();
	}
}
