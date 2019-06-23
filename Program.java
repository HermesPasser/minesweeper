import java.awt.*; 
import javax.swing.*; 
import java.awt.event.*; 
import java.util.Random;

public class Program implements MouseListener
{
	class Button extends JButton {
		int x, y;
		public boolean isMine = false;
		Button(int x, int y)
		{
			this.x = x;
			this.y = y;
		}
		
		boolean flagIsRaised()
		{
			return getIcon() != null;
		}
	}
	
	final int SIZE = 10;
	final int MINESCOUNT = 20;
	boolean firstClick = true;
	
	Button[][] buttons;
	ImageIcon icon;
	JFrame frame;

	public Program()
	{
		createUI();
	}
	
	void createUI()
	{
		icon = new ImageIcon("flag.png");
		
		frame = new JFrame("Mine");
        frame.setLayout(new GridLayout(SIZE,SIZE));

		buttons = new Button[SIZE][SIZE];
		for (int i = 0; i < SIZE; i++)
		for (int j = 0; j < SIZE; j++)
		{
			Button btn = new Button(i, j);
			buttons[i][j] = btn;
			btn.addMouseListener(this);

			frame.add(btn);
		}
		
		frame.pack();
		
		frame.setSize(new Dimension(400, 400));
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public void mouseClicked(MouseEvent e)
	{
		Button b = (Button)e.getSource();
		
		if (!b.isEnabled()) // desable onclick if the is desabled
			return;
		
		if (firstClick)
		{
			populateGrid(b.x, b.y);
			b.setEnabled(false);
		} else 
		{
			if (e.getButton() == MouseEvent.BUTTON1 && !b.flagIsRaised()) // prevent to click with mouse 0 if the flag is raised
				checkMine(b);
			else if (e.getButton() == MouseEvent.BUTTON3)
				raiseFlag(b);
		}
		firstClick = false;
	}
	
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mousePressed(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
	
	void populateGrid(int originX, int originY)
	{
		Random rand = new Random();
		int i = 0;
		while(i < MINESCOUNT)
		{
			int x = rand.nextInt(SIZE);
			int y = rand.nextInt(SIZE);
			
			if (x == originX && y == originY)	
				continue;
			
			buttons[x][y].setText("b");
			buttons[x][y].isMine = true;
			i++;
		}
	}
	
	void raiseFlag(Button btn)
	{
		if (btn.getIcon() == null)
			btn.setIcon(icon);
		else
			btn.setIcon(null);
	}
	
	boolean hasMine(Point pos)
	{
		if (pos.x < 0 || pos.y < 0 || pos.x >= buttons.length || pos.y >= buttons[0].length)
			return false;
		return buttons[pos.x][pos.y].isMine;
	}
	
	int countNeighboors(Button btn)
	{
		int count = 0;
		Point[] neighboors = new Point[]{
			new Point(btn.x - 1, btn.y - 1),
			new Point(btn.x,     btn.y - 1),
			new Point(btn.x + 1, btn.y - 1),
			
			new Point(btn.x - 1, btn.y),
			new Point(btn.x + 1, btn.y),
			
			new Point(btn.x - 1, btn.y + 1),
			new Point(btn.x,     btn.y + 1),
			new Point(btn.x + 1, btn.y + 1)
		};
		for (int i = 0; i < neighboors.length; i++)
			if (hasMine(neighboors[i]))
				count++;	
		return count;
	}
	
	void checkMine(Button btn)
	{
		if (btn.isMine)
		{
			btn.setBackground(Color.RED);
			gameOver();
			
		} else 
		{			
			int mineCount = countNeighboors(btn);
			btn.setText(mineCount + "");
		}
		btn.setEnabled(false);
	}
	
	void gameOver()
	{
		SetEnableGrid(false);
		JOptionPane.showMessageDialog(frame, "Game Over.");
	}
	
	void SetEnableGrid(boolean enabled)
	{
		for (int i = 0; i < buttons.length; i++)
		for (int j = 0; j < buttons[0].length; j++)
			buttons[i][j].setEnabled(enabled);
	}
	
	public static void main(String[] args)
	{
		new Program();
	}
}