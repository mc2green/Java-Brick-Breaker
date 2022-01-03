import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;

public class ConfigurationDialog extends JFrame implements GamePreference
{
	private static final long serialVersionUID = 6991911125167356556L;
	
	private JSlider numOfRowsComp;
	private JSlider numOfColsComp;
	private ButtonGroup numOfBallsComp;
	private JSlider difficultyLevelComp;
	private JSlider paddleWidthComp;
	
	private ActionListener saveCallback;
	
	public ConfigurationDialog(int width, int height) {
		
        setTitle("Brick Breaker Preference");
        setSize(width, height);

        //Adding Panel With Layout in Default Window
        JPanel jPanel=new JPanel();
        EmptyBorder emptyBorder=new EmptyBorder(10,10,10,10);
        jPanel.setBorder(emptyBorder);
        BoxLayout boxLayout=new BoxLayout(jPanel,BoxLayout.Y_AXIS);
        jPanel.setLayout(boxLayout);
        add(jPanel);
        //End panel Code
        
        JPanel rowsOfBrickPanel = addNumberOfBrickRowsPref();
        JPanel colsOfBrcikPanel = addNumberOfBrickColumnsPref();
        JPanel numOfBallsPanel = addNumberOfBallsPref();
        JPanel paddleWidthPanel = addPaddleWidthPref();
        JPanel difficultLevePanel = addDifficulityPref();
        JLabel empty = new JLabel();
        JLabel empty2 = new JLabel();
        JPanel saveBtn = addSaveButton();
        
        jPanel.add(rowsOfBrickPanel);
        jPanel.add(colsOfBrcikPanel);
        jPanel.add(numOfBallsPanel);
        jPanel.add(paddleWidthPanel);
        jPanel.add(difficultLevePanel);
        jPanel.add(empty);
        jPanel.add(empty2);
        jPanel.add(saveBtn);
	}
	
	private JPanel addSaveButton() {
		JPanel panel = new JPanel();
		panel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		
		JButton btn = new JButton("Save/Reset");
		btn.addActionListener(new ActionListener() {  
			public void actionPerformed(ActionEvent e) {
				saveCallback.actionPerformed(e);
	        };
		});
		panel.add(btn);
		
		return panel;
	}
	
	private JPanel addPaddleWidthPref() {
		JPanel panel = new JPanel();
		panel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        BoxLayout horizontalLayout = new BoxLayout(panel, BoxLayout.X_AXIS);
        panel.setLayout(horizontalLayout);

        JLabel label = new JLabel("paddle width percentage : ");
        paddleWidthComp = new JSlider(JSlider.HORIZONTAL, 3, 9, 6);
        
        paddleWidthComp.setMajorTickSpacing(6);
        paddleWidthComp.setMinorTickSpacing(1);
        paddleWidthComp.setToolTipText("paddle width (% of play area) : ");
        paddleWidthComp.setPaintTicks(true);
        paddleWidthComp.setPaintLabels(true);
        paddleWidthComp.setBorder(
                BorderFactory.createEmptyBorder(0,0,4,0));
        Font font = new Font("Serif", Font.ITALIC, 10);
        paddleWidthComp.setFont(font);
        
        Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
        for (int ii=3; ii <= 9; ii++) {
        	labelTable.put(ii, new JLabel("1/" + ii));
        }        
        
        paddleWidthComp.setLabelTable(labelTable);
        
        panel.add(label);
        panel.add(paddleWidthComp);
        
		return panel;
	}

	private JPanel addDifficulityPref() {
		JPanel panel = new JPanel();
		panel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        BoxLayout horizontalLayout = new BoxLayout(panel, BoxLayout.X_AXIS);
        panel.setLayout(horizontalLayout);

        JLabel label = new JLabel("difficulity level : ");
        difficultyLevelComp = new JSlider(JSlider.HORIZONTAL, 2, 8, 3);
        
        difficultyLevelComp.setMajorTickSpacing(6);
        difficultyLevelComp.setMinorTickSpacing(1);
        difficultyLevelComp.setToolTipText("difficulity of game");
        difficultyLevelComp.setPaintTicks(true);
        difficultyLevelComp.setPaintLabels(true);
        difficultyLevelComp.setBorder(
                BorderFactory.createEmptyBorder(0,0,4,0));
        Font font = new Font("Serif", Font.ITALIC, 10);
        difficultyLevelComp.setFont(font);
        
        Hashtable<Integer, JLabel> labelTable = 
                new Hashtable<Integer, JLabel>();
        labelTable.put(2, new JLabel("easiest"));
        // labelTable.put(1, new JLabel("easiest"));
        labelTable.put(5, new JLabel("moderate"));
        // labelTable.put(1, new JLabel("easiest"));
        labelTable.put(8, new JLabel("hard"));        
        
        difficultyLevelComp.setLabelTable(labelTable);
        
        panel.add(label);
        panel.add(difficultyLevelComp);
        
		return panel;
	}

	private JPanel addNumberOfBallsPref() {
		JPanel panel = new JPanel();
		panel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        BoxLayout horizontalLayout = new BoxLayout(panel, BoxLayout.X_AXIS);
        panel.setLayout(horizontalLayout);

        JLabel label = new JLabel("# of balls : ");
        JRadioButton b1 = new JRadioButton("1", false);
        JRadioButton b2 = new JRadioButton("2", true);
        numOfBallsComp = new ButtonGroup();
        numOfBallsComp.add(b1);
        numOfBallsComp.add(b2);
        
        panel.add(label);
        panel.add(b1);
        panel.add(b2);
        
        return panel;
	}

	private JPanel addNumberOfBrickColumnsPref() {
		JPanel panel = new JPanel();
		panel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        BoxLayout horizontalLayout = new BoxLayout(panel, BoxLayout.X_AXIS);
        panel.setLayout(horizontalLayout);

        JLabel label = new JLabel("# of columns of brick : ");
        numOfColsComp = new JSlider(JSlider.HORIZONTAL, 4, 10, 7);
        
        numOfColsComp.setMajorTickSpacing(6);
        numOfColsComp.setMinorTickSpacing(1);
        numOfColsComp.setToolTipText("the numbrer of rows of bricks");
        numOfColsComp.setPaintTicks(true);
        numOfColsComp.setPaintLabels(true);
        numOfColsComp.setBorder(
                BorderFactory.createEmptyBorder(0,0,4,0));
        Font font = new Font("Serif", Font.ITALIC, 10);
        numOfColsComp.setFont(font);
        
        Hashtable<Integer, JLabel> labelTable = 
                new Hashtable<Integer, JLabel>();
        for (int ii=4; ii <=10; ii++) {
        	labelTable.put(ii,  new JLabel(""+ii));
        }
        
        numOfColsComp.setLabelTable(labelTable);
        
        panel.add(label);
        panel.add(numOfColsComp);
        
		return panel;
	}

	private JPanel addNumberOfBrickRowsPref() {
		JPanel panel = new JPanel();
		panel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        BoxLayout horizontalLayout = new BoxLayout(panel, BoxLayout.X_AXIS);
        panel.setLayout(horizontalLayout);

        JLabel label = new JLabel("# of rows of brick : ");
        numOfRowsComp = new JSlider(JSlider.HORIZONTAL, 2, 6, 4);
        
        numOfRowsComp.setMajorTickSpacing(4);
        numOfRowsComp.setMinorTickSpacing(1);
        numOfRowsComp.setToolTipText("the numbrer of rows of bricks");
        numOfRowsComp.setPaintTicks(true);
        numOfRowsComp.setPaintLabels(true);
        numOfRowsComp.setBorder(
                BorderFactory.createEmptyBorder(0,0,4,0));
        Font font = new Font("Serif", Font.ITALIC, 10);
        numOfRowsComp.setFont(font);
        
        Hashtable<Integer, JLabel> labelTable = 
                new Hashtable<Integer, JLabel>();
        for (int ii=2; ii <=6; ii++) {
        	labelTable.put(ii,  new JLabel(""+ii));
        }
        
        numOfRowsComp.setLabelTable(labelTable);
        
        panel.add(label);
        panel.add(numOfRowsComp);
        
		return panel;
	}
	
	@Override
	public int getNumberOfBrickRows() {
		return numOfRowsComp.getValue();
	}
	
	@Override
	public int getNumberOfBrickColumns() {
		return numOfColsComp.getValue();
	}
	
	@Override
	public int getNumberOfBalls() {
		int numOfBalls = 1;
		for (Enumeration<AbstractButton> buttons = numOfBallsComp.getElements(); 
				buttons.hasMoreElements();)
		{
            AbstractButton button = buttons.nextElement();

            if (button.isSelected()) {
            	numOfBalls = Integer.parseInt(button.getText());
            	break;
            }
        }
		return numOfBalls;
	}
	
	@Override
	public int getDifficultyLevel() {
		return difficultyLevelComp.getValue();
	}
	
	@Override
	public int getPaddleWidhtPercentage() {
		return paddleWidthComp.getValue();
	}
	
	public void registerSaveCallback(ActionListener al) {
		saveCallback = al;
	}
}
