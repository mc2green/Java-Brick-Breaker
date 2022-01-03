import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GameBoard extends JPanel implements KeyListener, ActionListener 
{
	private static final long serialVersionUID = -67868281179892714L;
	
	/* scores */
	private int score = 0;
	private int scorePerBrick;
	
	/* bricks */
	private int numOfRows;
	private int numOfCols;
	private int totalBricks;
	
	/* paddle */
	private int paddleWidth;
	private int paddleHeight = 8;
	private int paddleYAboveBottom = 50;
	private int paddlePosX;
	private final int paddleMoveOffsetX = 30;
	private int paddleInitPosX;
	
	private int difficultyLevel;
	
	private final int DELAY = 8;
	
	/* ball related */
	private final int BALL_SIZE = 20;
	private final int BALL_STARTING_POS_X = 120;
	private final int BALL_STARTING_POS_Y = 350;
	/* ball 1 */
	private int ball1PosX = BALL_STARTING_POS_X;
	private int ball1PosY = BALL_STARTING_POS_Y;
	private int ball1XDir = -1;
	private int ball1YDir = -2;
	/* ball 2 */
	private int ball2PosX;
	private int ball2PosY = BALL_STARTING_POS_Y;
	private int ball2XDir = -1;
	private int ball2YDir = -2;
	
	private final int borderWidth = 4;
	private final int leftSpace = 80;
	private final int upSpace = 50;
	
	private int brickAreaWidth;
	private final int brickAreaHeight = 150;
	private int appWidth;
	private int appHeight;
	
	private final int DEFAULT_DIFFICULTY_LEVEL_VALUE = 4;
	
	private boolean hasBall2;
	
	/* variables for state */
	private boolean inPlayMode = false;
	private boolean isPaused = false;
	
	private BrickMapper brickMapper;
	private Timer timer;
	
	GamePreference gamePreference;
	SoundManager soundManager;
	
	public GameBoard(SoundManager sm, int appWidth, int appHeight,  GamePreference gp) {	
		this(sm, appWidth, appHeight, gp.getNumberOfBrickRows(),
				gp.getNumberOfBrickColumns(), gp.getPaddleWidhtPercentage(),
				// 8 - (gp.getDifficultyLevel() - 4) * 2,
				gp.getDifficultyLevel(),
				gp.getNumberOfBalls() == 1 ? false : true);
		
		gamePreference = gp;
	}
	
	public GameBoard(SoundManager sm, int appWidth, int appHeight, int numOfRows, int numOfCols, 
			int paddleWidthPercentage, int difficultyLevel, boolean twoBallMode)
	{
		this.soundManager = sm;
		this.appWidth = appWidth;
		this.appHeight = appHeight;
		
		init(numOfRows, numOfCols, paddleWidthPercentage, difficultyLevel, twoBallMode);
		
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		requestFocus();
		
		timer = new Timer(DELAY, this);
		timer.start();		
	}
	
	public void reset() {
		if (inPlayMode) {
			return;
		}
		
		timer.stop();
		init(gamePreference.getNumberOfBrickRows(),
				gamePreference.getNumberOfBrickColumns(), 
				gamePreference.getPaddleWidhtPercentage(),
				// 8 - ( gamePreference.getDifficultyLevel() - 4) * 2,
				gamePreference.getDifficultyLevel(),
				gamePreference.getNumberOfBalls() == 1 ? false : true);
		timer.start();
		
		repaint();
	}
	
	private void init(int numOfRows, int numOfCols, int paddleWidthPercentage, 
			int difficultyLevel, boolean twoBallMode)
	{
		this.numOfRows = numOfRows;
		this.numOfCols = numOfCols;
		this.difficultyLevel = difficultyLevel - 2;
		
		System.out.println("difficulty level: " + difficultyLevel);
		
		hasBall2 = twoBallMode;
		paddleWidth = (appWidth - 2 * borderWidth) / paddleWidthPercentage;
		// System.out.println("paddle width: " + paddleWidth);
		brickAreaWidth = appWidth - 2 * leftSpace;
		
		int bonus = (difficultyLevel - DEFAULT_DIFFICULTY_LEVEL_VALUE) > 0 
				? (difficultyLevel - DEFAULT_DIFFICULTY_LEVEL_VALUE) * 2 : 0;
		
		scorePerBrick = 5 + bonus;
		
		ball2PosX = appWidth - BALL_STARTING_POS_X;
		paddleInitPosX = (appWidth - paddleWidth) / 2;
		paddlePosX = paddleInitPosX;
		totalBricks = numOfRows * numOfCols;
		score = 0;
		ball1PosX = BALL_STARTING_POS_X;
		ball1PosY = BALL_STARTING_POS_Y;
		if (hasBall2) {
			ball2PosX = appWidth - BALL_STARTING_POS_X;
			ball2PosY = BALL_STARTING_POS_Y;			
		}
		
		brickMapper = new BrickMapper(numOfRows, numOfCols, 
				new Rectangle(leftSpace, upSpace, brickAreaWidth, brickAreaHeight));		
	}
	
	@Override
	public void paint(Graphics g)
	{	
		// draw background
		g.setColor(Color.CYAN);
		g.fillRect(0, 0, appWidth, appHeight);
		
		// draw borders
		g.setColor(Color.white);
		g.fillRect(0, 0, borderWidth, appHeight); // left vertical line
		g.fillRect(0, 0, appWidth, borderWidth);  // upper horizontal line
		g.fillRect(appWidth - borderWidth, 0, borderWidth, appHeight);  // right vertical line
				
		// draw brick area
		brickMapper.drawArea((Graphics2D) g);
		
		// display scores 		
		g.setColor(Color.white);
		g.setFont(new Font("serif",Font.BOLD, 25));
		g.drawString(""+score, appWidth - 120, 32 + borderWidth);
		
		// draw paddle
		g.setColor(Color.red);
		g.fillRect(paddlePosX, appHeight - paddleYAboveBottom, paddleWidth/3, paddleHeight);
		g.setColor(Color.orange);
		g.fillRect(paddlePosX + paddleWidth/3, appHeight - paddleYAboveBottom, paddleWidth/3, paddleHeight);
		g.setColor(Color.red);
		g.fillRect(paddlePosX + paddleWidth*2/3, appHeight - paddleYAboveBottom, paddleWidth/3, paddleHeight);
		
		
		// draw balls
		g.setColor(Color.red);
		g.fillOval(ball1PosX, ball1PosY, BALL_SIZE, BALL_SIZE);
		if (hasBall2) {
			g.setColor(Color.yellow);
			g.fillOval(ball2PosX, ball2PosY, BALL_SIZE, BALL_SIZE);		
		}
		
		if (! inPlayMode) {
            g.setColor(Color.RED);
            g.setFont(new Font("serif", Font.BOLD, 20));           
            g.drawString("Press (Enter) to Restart/Start", 230, 350);
            g.drawString("Press p to Pause and r to Resume", 230, 380);
            g.drawString("Press shift key with left/right key to move paddle faster", 230, 410);
		}
		
		if (isPaused) {
			g.setColor(Color.RED);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Paused", 300, 350);
            g.drawString("Press r to resume", 300, 380);
            // cancel the timer
            timer.stop();
			return;
		}
	
		// game over - you win
		if (totalBricks <= 0)
		{
			playHappyGameOverSound();
			
			 inPlayMode = false;
             ball1XDir = 0;
     		 ball1YDir = 0;
     		 if (hasBall2) {
                 ball2XDir = 0;
         		 ball2YDir = 0;     			 
     		 }
             g.setColor(Color.RED);
             g.setFont(new Font("serif", Font.BOLD, 30));
             g.drawString("You Won!", 260, 300);
             
             g.setColor(Color.RED);
             g.setFont(new Font("serif", Font.BOLD, 20));           
             g.drawString("Press (Enter) to Restart", 230, 350);
             g.drawString("Press p to pause and r to resume", 230, 380);
             g.drawString("Press shift key with left/right key to move paddle faster", 230, 410);
		}
		
		// you lose
		boolean ball1Die = ball1PosY > appHeight - paddleYAboveBottom;
		boolean ball2Die = hasBall2 ? hasBall2 && ball2PosY > appHeight - paddleYAboveBottom : true;
		if (ball1Die && ball2Die)
        {
			playSadGameOverSound();
			
			 inPlayMode = false;
             ball1XDir = 0;
     		 ball1YDir = 0;
     		 if (hasBall2) {
                 ball2XDir = 0;
         		 ball2YDir = 0;     			 
     		 }
             g.setColor(Color.RED);
             g.setFont(new Font("serif",Font.BOLD, 30));
             g.drawString("Game Over, Scores: "+score, 190, 300);
             
             g.setColor(Color.RED);
             g.setFont(new Font("serif",Font.BOLD, 20));           
             g.drawString("Press (Enter) to Restart", 230, 350); 
             g.drawString("Press p to pause and r to resume", 230, 380);
             g.drawString("Press shift key with left/right key to move paddle faster", 230, 410);
        }
		
		g.dispose();
	}	

	@Override
	public void keyPressed(KeyEvent e) 
	{
		if (e.getKeyChar() == 'p')
		{
			isPaused = true;
		} 
		else if (e.getKeyChar() == 'r')
		{
			isPaused = false;
			// resume the game - start timer again
			timer.start();
		} 
		else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
		{
			if (! inPlayMode)
				return;

			if (paddlePosX > appWidth - paddleWidth - borderWidth)
			{
				paddlePosX = appWidth - paddleWidth - borderWidth;
			}
			else
			{
				movePaddleToRight((e.getModifiersEx() & KeyEvent.SHIFT_DOWN_MASK) == KeyEvent.SHIFT_DOWN_MASK);
				if (paddlePosX > appWidth-paddleWidth - borderWidth)
				{
					paddlePosX = appWidth-paddleWidth - borderWidth;
				}
			}
        } 
		else if (e.getKeyCode() == KeyEvent.VK_LEFT)
		{          
			if (!inPlayMode)
				return;
			
			if (paddlePosX < borderWidth)
			{
				paddlePosX = borderWidth;
			}
			else
			{
				movePaddleToLeft((e.getModifiersEx() & KeyEvent.SHIFT_DOWN_MASK) == KeyEvent.SHIFT_DOWN_MASK);
				if (paddlePosX < borderWidth)
				{
					paddlePosX = borderWidth;
				}
			}
        } 
		else if (e.getKeyCode() == KeyEvent.VK_ENTER)
		{          
			if (! inPlayMode)
			{
				inPlayMode = true;
				
				int direction = randInt(-1, 1, false);
				int direction2 = randInt(-1, 1, false);
				
				ball1PosX = BALL_STARTING_POS_X;
				ball1PosY = BALL_STARTING_POS_Y;
				ball1XDir = direction * (difficultyLevel + randInt(0, 2, true)); // randInt(-1, 1);
				ball1YDir = -difficultyLevel + randInt(0, 1, true); // randInt(-3, -1); 
				
				paddlePosX = paddleInitPosX;
				
				if (hasBall2) {
					ball2PosX = appWidth - BALL_STARTING_POS_X;
					ball2PosY = BALL_STARTING_POS_Y;
					ball2XDir = direction2 * (difficultyLevel + randInt(0, 2, true)); // randInt(-1, 1);
					ball2YDir = -difficultyLevel + randInt(0, 1, true); // randInt(-3, -1);				
				}
				
				score = 0;
				totalBricks = numOfRows * numOfCols;
				brickMapper = new BrickMapper(numOfRows, numOfCols, 
						new Rectangle(leftSpace, upSpace, brickAreaWidth, brickAreaHeight));
				
				repaint();
			}
        }		
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
	}
	
	public void movePaddleToRight(boolean faster)
	{
		inPlayMode = true;
		paddlePosX += paddleMoveOffsetX * (faster ? 2 : 1);
	}
	
	public void movePaddleToLeft(boolean faster)
	{
		inPlayMode = true;
		paddlePosX -= paddleMoveOffsetX * (faster ? 2 : 1);
	}
	
	/* called by timer */
	@Override
	public void actionPerformed(ActionEvent event) 
	{
		timer.start();
		
		if (inPlayMode)
		{
			if (new Rectangle(ball1PosX, ball1PosY, BALL_SIZE, BALL_SIZE).intersects(new Rectangle(paddlePosX + paddleWidth/3, appHeight - paddleYAboveBottom, paddleWidth/3, paddleHeight)))
			{
				playHitPaddleSound();
				ball1YDir = -ball1YDir; // to bounce up vertically
			} 
			else if (new Rectangle(ball1PosX, ball1PosY, BALL_SIZE, BALL_SIZE).intersects(new Rectangle(paddlePosX + paddleWidth*2/3, appHeight - paddleYAboveBottom, paddleWidth/3, paddleHeight)))
			{
				playHitPaddleSound();
				ball1YDir = -ball1YDir;  // to bounce up 
				// ball1XDir += randInt(1,2);  // to bounce to right if hit the right part of 30/100
				ball1XDir = difficultyLevel - randInt(0, 2, true); // randInt(1,3);
			}
			else if (new Rectangle(ball1PosX, ball1PosY, BALL_SIZE, BALL_SIZE).intersects(new Rectangle(paddlePosX, appHeight - paddleYAboveBottom, paddleWidth/3, paddleHeight)))
			{
				playHitPaddleSound();
				ball1YDir = -ball1YDir;  // to bounce up
				ball1XDir = -difficultyLevel + randInt(0, 1, true); // randInt(-3, -1);
			}
			
			if (hasBall2)
			{
				if (new Rectangle(ball2PosX, ball2PosY, BALL_SIZE, BALL_SIZE).intersects(new Rectangle(paddlePosX + paddleWidth/3, appHeight - paddleYAboveBottom, paddleWidth/3, paddleHeight)))
				{
					playHitPaddleSound();
					ball2YDir = -ball2YDir;
				}
				else if (new Rectangle(ball2PosX, ball2PosY, BALL_SIZE, BALL_SIZE).intersects(new Rectangle(paddlePosX, appHeight - paddleYAboveBottom, paddleWidth/3, paddleHeight)))
				{
					playHitPaddleSound();
					ball2YDir = -ball2YDir;  // to bounce up
					ball2XDir = difficultyLevel - randInt(0, 1, true); // randInt(-3, -1);  // to bounce to the left if hit the 30/100 (the left part)
				} 
				else if (new Rectangle(ball2PosX, ball2PosY, BALL_SIZE, BALL_SIZE).intersects(new Rectangle(paddlePosX + paddleWidth*2/3, appHeight - paddleYAboveBottom, paddleWidth/3, paddleHeight)))
				{
					playHitPaddleSound();
					ball2YDir = -ball2YDir;  // to bounce up 
					// ball2XDir += randInt(1, 2);   // to bounce to right if hit the right part of 30/100
					ball2XDir = -difficultyLevel + randInt(0, 1, true); // randInt(1, 3);
				}		
			}
			
			boolean ballHitBrick = false;
			boolean ball2HitBrick = hasBall2 ? false : true;
			final int brickWidth = brickMapper.getBrickWidth();
			final int brickHeight = brickMapper.getBrickHeight();
			
			breakOutLabel: for (int ii = 0; ii < brickMapper.getRowSize(); ii++)
			{
				for (int jj =0; jj < brickMapper.getColumnSize(); jj++)
				{				
					if (brickMapper.getBrickValue(ii, jj) > 0)
					{
						int brickX = jj * brickWidth + leftSpace;
						int brickY = ii * brickHeight + upSpace;
						
						Rectangle brickRect = new Rectangle(brickX, brickY, brickWidth, brickHeight);					
						Rectangle ballRect = new Rectangle(ball1PosX, ball1PosY, BALL_SIZE, BALL_SIZE);
						
						if (! ballHitBrick && ballRect.intersects(brickRect))
						{	
							playHitBrickSound();
							brickMapper.setBrickValue(ii, jj, 0);
							score += scorePerBrick;	
							totalBricks--;
							
							// when ball hit right or left of brick
							if (ball1PosX + BALL_SIZE - 1 <= brickRect.x || ball1PosX + 1 >= brickRect.x + brickRect.width)	
							{
								ball1XDir = -ball1XDir;
							}
							// when ball hits top or bottom of brick
							else
							{
								ball1YDir = -ball1YDir;				
							}
							
							ballHitBrick = true;
						}
						if (hasBall2) {
							Rectangle ball2Rect = new Rectangle(ball2PosX, ball2PosY, BALL_SIZE, BALL_SIZE);
							if (!ball2HitBrick && brickMapper.getBrickValue(ii, jj) > 0 && 
									ball2Rect.intersects(brickRect))
							{		
								playHitBrickSound();
								brickMapper.setBrickValue(ii, jj, 0);
								score += scorePerBrick;	
								totalBricks--;
								
								// when ball hit right or left of brick
								if (ball2PosX + BALL_SIZE - 1 <= brickRect.x || ball2PosX + 1 >= brickRect.x + brickRect.width)	
								{
									ball2XDir = -ball2XDir;
								}
								else
								{
									ball2YDir = -ball2YDir;				
								}
								
								ball2HitBrick = true;
							}
							
						}
						if (ballHitBrick && ball2HitBrick) {
							break breakOutLabel;
						}
					}
				}
			}
			
			// now, calculate the X, Y of balls
			ball1PosX += ball1XDir;
			ball1PosY += ball1YDir;
			
			if (ball1PosX < borderWidth)
			{
				ball1XDir = - (ball1XDir != 0 ? ball1XDir : -1);
			}
			if (ball1PosY < borderWidth)
			{
				ball1YDir = - (ball1YDir != 0 ? ball1YDir : 1);
			}
			if (ball1PosX > appWidth - BALL_SIZE - borderWidth)
			{
				ball1XDir = - (ball1XDir != 0 ? ball1XDir : 1);
			}		
			
			if (hasBall2) {
				ball2PosX += ball2XDir;
				ball2PosY += ball2YDir;
				
				if (ball2PosX < borderWidth)
				{
					ball2XDir = - (ball2XDir != 0 ? ball2XDir : -1);
				}
				if (ball2PosY < borderWidth)
				{
					ball2YDir = - (ball2YDir != 0 ? ball2YDir : 1);
				}
				if (ball2PosX > appWidth - BALL_SIZE - borderWidth)
				{
					ball2XDir = - (ball2XDir != 0 ? ball2XDir : 1);
				}						
			}
			
			repaint();		
		}
	}
	
	private int randInt(int min, int max, boolean returingZero) {
		/*
		 * doesn't return 0
		 */
	    Random rand = new Random();

	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    
	    if (randomNum == 0 && ! returingZero) {
		    while (randomNum == 0) {
		    	randomNum = rand.nextInt((max - min) + 1) + min;
		    }
	    }

	    return randomNum;
	}
	
	private void playHitBrickSound() {
		try {
			soundManager.playSound(0);
		} catch (UnsupportedAudioFileException | LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void playHitPaddleSound() {
		try {
			soundManager.playSound(1);
		} catch (UnsupportedAudioFileException | LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void playSadGameOverSound() {
		try {
			soundManager.playSound(2);
		} catch (UnsupportedAudioFileException | LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void playHappyGameOverSound() {
		try {
			soundManager.playSound(3);
		} catch (UnsupportedAudioFileException | LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
