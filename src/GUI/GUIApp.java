package GUI;

import initiatives.SimulationInitiative;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import GUI.widget.earth.EarthPanel;

import javax.swing.JCheckBox;

import base.PausableStoppable;
import base.PresentationMethod;
import base.SimulationResult;

public class GUIApp extends JFrame implements PresentationMethod{

	private final static Logger LOGGER = Logger.getLogger(SimulationInitiative.class.getName());
	private static final long serialVersionUID = -1873444547097063288L;
	
	private JPanel contentPane;
	private JTextField gridSpacing;
	private JTextField timeStep;
	private JTextField displayRate;
	private JTextField iniativeEntry;
	private JTextField bufferSizeEntry;
	
	private EarthPanel presentation_panel;
	private PausableStoppable initiative;
	
	/**
	 * @wbp.nonvisual location=-141,459
	 */
	private final JCheckBox checkBox = new JCheckBox("New check box");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUIApp frame = new GUIApp();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	

	public void setInitiative(PausableStoppable initiative) {
		this.initiative = initiative;
	}


	/**
	 * Create the frame.
	 */
	public GUIApp() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(1, 1, 1006, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		/**
		 * Buttons to start/stop/pause/resume the application
		 */
		JButton startButton = new JButton("Start");
		startButton.setBounds(349, 543, 117, 29);
		startButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int degreeSeparation = 15;
					int simulationTimeStep = 1;
					try {
						degreeSeparation = Integer.parseInt(gridSpacing.getText());
					} catch (NumberFormatException ex) {
						gridSpacing.setText(Integer.toString(degreeSeparation));
					}
					try {
						simulationTimeStep = Integer.parseInt(timeStep.getText());
					} catch (NumberFormatException ex) {
						timeStep.setText(Integer.toString(simulationTimeStep));
					}
					presentation_panel.drawGrid(degreeSeparation);
					initiative.start(degreeSeparation, simulationTimeStep);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		contentPane.add(startButton);

		JButton resumeButton = new JButton("Resume");
		resumeButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					initiative.resume();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		resumeButton.setBounds(682, 543, 117, 29);
		contentPane.add(resumeButton);

		JButton stopButton = new JButton("Stop");
		stopButton.setBounds(459, 543, 117, 29);
		stopButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					initiative.stop();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		contentPane.add(stopButton);

		JButton pauseButton = new JButton("Pause");
		pauseButton.setBounds(570, 543, 117, 29);
		pauseButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					initiative.pause();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		contentPane.add(pauseButton);
		
		/**
		 * Jpanel for holding display contents
		 */
		JPanel displayPanel = new JPanel();
		displayPanel.setBorder(null);
		displayPanel.setBounds(160, 12, 834, 450);
		contentPane.add(displayPanel);
		displayPanel.setLayout(null);

		/**
		 * Jpanel for displaying the earth and simulation animation
		 */
		presentation_panel = new EarthPanel(new Dimension(30,100), new Dimension(30,100), new Dimension(30,100));
		presentation_panel.setBorder(new LineBorder(Color.DARK_GRAY, 2, true));
		presentation_panel.setBounds(12, 18, 800, 400);
		displayPanel.add(presentation_panel);
		
		JLabel lblZeroDegrees = new JLabel("0");
		lblZeroDegrees.setHorizontalAlignment(SwingConstants.CENTER);
		lblZeroDegrees.setBounds(393, 430, 35, 16);
		displayPanel.add(lblZeroDegrees);
		
		JLabel lbl180degE = new JLabel("180 E");
		lbl180degE.setHorizontalAlignment(SwingConstants.CENTER);
		lbl180degE.setBounds(793, 430, 35, 16);
		displayPanel.add(lbl180degE);
		
		JLabel lbl90degE = new JLabel("90 E");
		lbl90degE.setHorizontalAlignment(SwingConstants.CENTER);
		lbl90degE.setBounds(591, 430, 35, 16);
		displayPanel.add(lbl90degE);
		
		JLabel lbl180degW = new JLabel("180 W");
		lbl180degW.setHorizontalAlignment(SwingConstants.CENTER);
		lbl180degW.setBounds(0, 430, 39, 16);
		displayPanel.add(lbl180degW);
		
		JLabel lbl90degW = new JLabel("90 W");
		lbl90degW.setHorizontalAlignment(SwingConstants.CENTER);
		lbl90degW.setBounds(196, 430, 35, 16);
		displayPanel.add(lbl90degW);
		
		JLabel lbl45degE = new JLabel("45 E");
		lbl45degE.setHorizontalAlignment(SwingConstants.CENTER);
		lbl45degE.setBounds(493, 430, 35, 16);
		displayPanel.add(lbl45degE);
		
		JLabel lbl135degW = new JLabel("135 W");
		lbl135degW.setHorizontalAlignment(SwingConstants.CENTER);
		lbl135degW.setBounds(93, 430, 46, 16);
		displayPanel.add(lbl135degW);
		
		JLabel lbl135degE = new JLabel("135 E");
		lbl135degE.setHorizontalAlignment(SwingConstants.CENTER);
		lbl135degE.setBounds(685, 430, 46, 16);
		displayPanel.add(lbl135degE);
		
		JLabel lbl45degW = new JLabel("45 W");
		lbl45degW.setHorizontalAlignment(SwingConstants.CENTER);
		lbl45degW.setBounds(296, 430, 35, 16);
		displayPanel.add(lbl45degW);

		/**
		 * Jpanel in top left area of gui for first set of user input
		 */
		JPanel topLeftPanel = new JPanel();
		topLeftPanel.setBounds(6, 30, 155, 207);
		contentPane.add(topLeftPanel);
		topLeftPanel.setLayout(null);

		gridSpacing = new JTextField();
		gridSpacing.setBounds(40, 5, 74, 28);
		gridSpacing.setText("1 to 180");
		topLeftPanel.add(gridSpacing);
		gridSpacing.setColumns(5);

		JLabel lblNewLabel = new JLabel(" Grid Spacing (degrees)");
		lblNewLabel.setBounds(5, 38, 144, 16);
		topLeftPanel.add(lblNewLabel);

		/**
		 * text fields for user input of grid spacing, display rate and time step 
		 */
		timeStep = new JTextField();
		timeStep.setBounds(40, 59, 74, 28);
		timeStep.setText("1 to 1440");
		timeStep.setColumns(5);
		topLeftPanel.add(timeStep);

		JLabel lblTimeStepminutes = new JLabel("Time Step (minutes)");
		lblTimeStepminutes.setBounds(15, 92, 125, 16);
		topLeftPanel.add(lblTimeStepminutes);

		displayRate = new JTextField();
		displayRate.setBounds(40, 113, 74, 28);
		displayRate.setText("18 - 24");
		displayRate.setColumns(5);
		topLeftPanel.add(displayRate);

		JLabel lblDisplayRate = new JLabel("Display Rate");
		lblDisplayRate.setBounds(38, 146, 78, 16);
		topLeftPanel.add(lblDisplayRate);

		JLabel lblframesPerSecond = new JLabel("(frames per second)");
		lblframesPerSecond.setBounds(15, 167, 124, 16);
		topLeftPanel.add(lblframesPerSecond);
		
		/**
		 * Jpanel in bottom left area of gui for second set of user input
		 */
		JPanel botLeftPanel = new JPanel();
		botLeftPanel.setBounds(6, 249, 155, 213);
		contentPane.add(botLeftPanel);
		botLeftPanel.setLayout(null);

		JLabel lblInitiative = new JLabel("Initiative");
		lblInitiative.setBounds(39, 131, 78, 16);
		lblInitiative.setHorizontalAlignment(SwingConstants.CENTER);
		botLeftPanel.add(lblInitiative);

		iniativeEntry = new JTextField();
		iniativeEntry.setHorizontalAlignment(SwingConstants.CENTER);
		iniativeEntry.setBounds(20, 102, 114, 28);
		iniativeEntry.setText("Sim, Pre, GUI");
		iniativeEntry.setColumns(5);
		botLeftPanel.add(iniativeEntry);

		JLabel lblBufferSize = new JLabel("Buffer Size");
		lblBufferSize.setBounds(39, 191, 78, 16);
		lblBufferSize.setHorizontalAlignment(SwingConstants.CENTER);
		botLeftPanel.add(lblBufferSize);

		bufferSizeEntry = new JTextField();
		bufferSizeEntry.setHorizontalAlignment(SwingConstants.CENTER);
		bufferSizeEntry.setBounds(39, 159, 78, 28);
		bufferSizeEntry.setText(">= 1");
		bufferSizeEntry.setColumns(5);
		botLeftPanel.add(bufferSizeEntry);
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("Simulation");
		chckbxNewCheckBox.setBounds(20, 30, 114, 23);
		botLeftPanel.add(chckbxNewCheckBox);
		
		JCheckBox chckbxPresentationThread = new JCheckBox("Presentation");
		chckbxPresentationThread.setBounds(20, 56, 129, 23);
		botLeftPanel.add(chckbxPresentationThread);
		
		JLabel lblThreads = new JLabel("Thread(s)");
		lblThreads.setHorizontalAlignment(SwingConstants.CENTER);
		lblThreads.setBounds(20, 13, 114, 16);
		botLeftPanel.add(lblThreads);

		/**
		 * display the 
		 */
		TextArea elapsedTimeDisplay = new TextArea();
		elapsedTimeDisplay.setForeground(new Color(0, 0, 0));
		elapsedTimeDisplay.setBounds(764, 483, 128, 25);
		contentPane.add(elapsedTimeDisplay);

		JLabel elapsedTimeLabel = new JLabel("Time Elapsed");
		elapsedTimeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		elapsedTimeLabel.setBounds(655, 483, 111, 25);
		contentPane.add(elapsedTimeLabel);

		JLabel rotatePositionLabel = new JLabel("Rotational Position");
		rotatePositionLabel.setHorizontalAlignment(SwingConstants.CENTER);
		rotatePositionLabel.setBounds(262, 483, 134, 25);
		contentPane.add(rotatePositionLabel);
		
		TextArea rotatePositionDisplay = new TextArea();
		rotatePositionDisplay.setForeground(Color.BLACK);
		rotatePositionDisplay.setBounds(402, 483, 128, 25);
		contentPane.add(rotatePositionDisplay);
	}

	@Override
	public void present(SimulationResult result) throws InterruptedException {
		presentation_panel.updateGrid(result);
		presentation_panel.moveSunPosition(result.getSunPosition() + 180);
		System.out.println(result.getTemperature(1, 1));
		LOGGER.info("Temperature (1,1): " + result.getTemperature(1, 1));
		LOGGER.info("Sun position: " + result.getSunPosition());
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}
}
