import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class BrickMapper 
{
	private int bricks[][];
	private int brickWidth;
	private int brickHeight;
	private int leftSpace;
	private int upSpace;
	
	public BrickMapper (int row, int col, Rectangle brickArea)
	{
		bricks = new int[row][col];		
		for (int ii = 0; ii< bricks.length; ii++)
		{
			for (int jj =0; jj < bricks[0].length; jj++)
			{
				bricks[ii][jj] = 1;
			}			
		}
		
		brickWidth = brickArea.width / col;
		brickHeight = brickArea.height / row;
		leftSpace = brickArea.x;
		upSpace = brickArea.y;
	}	
	
	public void drawArea(Graphics2D g)
	{
		for (int ii = 0; ii < bricks.length; ii++)
		{
			for (int jj =0; jj < bricks[0].length; jj++)
			{
				if(bricks[ii][jj] > 0)
				{
					g.setColor((ii + jj) % 2 == 0 ? Color.white : Color.gray);
					g.fillRect(jj * brickWidth + leftSpace, ii * brickHeight + upSpace, brickWidth, brickHeight);
					
					g.setStroke(new BasicStroke(3));
					g.setColor(Color.black);
					g.drawRect(jj * brickWidth + leftSpace, ii * brickHeight + upSpace, brickWidth, brickHeight);				
				}
			}
		}
	}
	
	public int getRowSize() {
		return bricks.length;
	}
	
	public int getColumnSize() {
		return bricks[0].length;
	}
	
	public int getBrickWidth() {
		return brickWidth;
	}
	
	public int getBrickHeight() {
		return brickHeight;
	}
	
	public int getBrickValue(int row, int col) {
		return bricks[row][col];
	}
	
	public void setBrickValue(int row, int col, int value)
	{
		bricks[row][col] = value;
	}
}
