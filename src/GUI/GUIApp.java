package GUI;

import initiatives.SimulationInitiative;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import GUI.widget.earth.EarthPanel;

import javax.swing.JCheckBox;

import base.InitiativeType;
import base.PausableStoppable;
import base.PresentationMethod;
import base.SimulationResult;
import base.ObjectFactory;
import base.Utils;

public class GUIApp extends JFrame implements PresentationMethod{

	private final static Logger LOGGER = Logger.getLogger(SimulationInitiative.class.getName());
	private static final long serialVersionUID = -1873444547097063288L;
	
	private JPanel contentPane;
	private JTextField gridSpacing;
	private JTextField timeStep;
	private JTextField displayRate;
	private JComboBox initiativeEntry;
	private JTextField bufferSizeEntry;
	private JCheckBox chckbxNewCheckBox;
	private JCheckBox chckbxPresentationThread;
	
	private JButton pauseButton;
	private JButton resumeButton;
	private JButton stopButton;
	private JButton startButton;
	
	private EarthPanel presentation_panel;
	private PausableStoppable initiative;
	private float secondsElapsed;
	private float previousSunPosition = 180;
	private JTextField elapsedTimeDisp;
	private JTextField rotatePositionDisp;
	
	/**
	 * Create the frame.
	 */
	public GUIApp(int bufferSize, boolean presentationThread, boolean simulationThread, InitiativeType initiativeType) {
		final PresentationMethod presentationMethod = this;
		
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
		startButton = new JButton("Start");
		startButton.setBounds(349, 543, 117, 29);
		startButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					setInputsEnabled(false);
					startButton.setEnabled(false);
					
					// Retrieve buffer size
					int bufferSize = 1;
					try {
						bufferSize = Integer.parseInt(bufferSizeEntry.getText());
					} catch (NumberFormatException ex) {
						bufferSizeEntry.setText("1");
					}
					
					// Retrieve initiative type
					InitiativeType initiativeType;
					switch(initiativeEntry.getSelectedIndex()) {
					case 1:
						initiativeType = InitiativeType.Presentation;
						break;
					case 2:
						initiativeType = InitiativeType.Simulation;
						break;
					default:
						initiativeType = InitiativeType.MasterController;
						break;
					}
					
					boolean presentationThreaded = chckbxNewCheckBox.isSelected();
					boolean simulationThreaded = chckbxPresentationThread.isSelected();
					if (!presentationThreaded || !simulationThreaded) {
						switch (JOptionPane.showConfirmDialog(null, "Warning: Executing the presentation or simulation on the main thread will cause the application to become unresponsive"))
						{
						case JOptionPane.OK_OPTION:
							break;
						default:
							return;
						}
					}
					initiative = ObjectFactory.getInitiative(initiativeType, bufferSize, presentationThreaded, simulationThreaded, presentationMethod);
					
					// Retrieve degree separation
					int degreeSeparation = 15;
					int simulationTimeStep = 1;
					try {
						degreeSeparation = Integer.parseInt(gridSpacing.getText());
					} catch (NumberFormatException ex) {
						gridSpacing.setText(Integer.toString(degreeSeparation));
					}
					
					// Retrieve simulation time step
					try {
						simulationTimeStep = Integer.parseInt(timeStep.getText());
					} catch (NumberFormatException ex) {
						timeStep.setText(Integer.toString(simulationTimeStep));
					}
					
					int presentationTimeStep = 1;
					try {
						presentationTimeStep = Integer.parseInt(displayRate.getText());
					} catch (NumberFormatException ex) {
						displayRate.setText(Integer.toString(presentationTimeStep));
					}
					// TODO: Retrieve presentation time step
					
					presentation_panel.drawGrid(degreeSeparation);
					initiative.start(degreeSeparation, simulationTimeStep, presentationTimeStep);
					stopButton.setEnabled(true);
					pauseButton.setEnabled(true);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		contentPane.add(startButton);

		resumeButton = new JButton("Resume");
		resumeButton.setEnabled(false);
		resumeButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					initiative.resume();
					resumeButton.setEnabled(false);
					pauseButton.setEnabled(true);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		resumeButton.setBounds(682, 543, 117, 29);
		contentPane.add(resumeButton);

		stopButton = new JButton("Stop");
		stopButton.setBounds(459, 543, 117, 29);
		stopButton.setEnabled(false);
		stopButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					initiative.stop();
					presentation_panel.reset();
					setInputsEnabled(true);
					stopButton.setEnabled(false);
					resumeButton.setEnabled(false);
					pauseButton.setEnabled(false);
					startButton.setEnabled(true);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		contentPane.add(stopButton);

		pauseButton = new JButton("Pause");
		pauseButton.setBounds(570, 543, 117, 29);
		pauseButton.setEnabled(false);
		pauseButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					pauseButton.setEnabled(false);
					resumeButton.setEnabled(true);
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

		initiativeEntry = new JComboBox(new String[] { "Master", "Presentation", "Simulation" });
		//iniativeEntry.setHorizontalAlignment(SwingConstants.CENTER);
		initiativeEntry.setBounds(20, 102, 114, 28);
		//iniativeEntry.setColumns(5);
		switch (initiativeType) {
		case MasterController:
			initiativeEntry.setSelectedItem("Master");
			break;
		case Presentation:
			initiativeEntry.setSelectedItem("Presentation");
			break;
		case Simulation:
			initiativeEntry.setSelectedItem("Simulation");
			break;
		}
		botLeftPanel.add(initiativeEntry);

		JLabel lblBufferSize = new JLabel("Buffer Size");
		lblBufferSize.setBounds(39, 191, 78, 16);
		lblBufferSize.setHorizontalAlignment(SwingConstants.CENTER);
		botLeftPanel.add(lblBufferSize);

		bufferSizeEntry = new JTextField();
		bufferSizeEntry.setHorizontalAlignment(SwingConstants.CENTER);
		bufferSizeEntry.setBounds(39, 159, 78, 28);
		bufferSizeEntry.setText(Integer.toString(bufferSize));
		bufferSizeEntry.setColumns(5);
		botLeftPanel.add(bufferSizeEntry);
		
		chckbxNewCheckBox = new JCheckBox("Simulation");
		chckbxNewCheckBox.setBounds(20, 30, 114, 23);
		chckbxNewCheckBox.setSelected(simulationThread);
		botLeftPanel.add(chckbxNewCheckBox);
		
		chckbxPresentationThread = new JCheckBox("Presentation");
		chckbxPresentationThread.setBounds(20, 56, 129, 23);
		chckbxPresentationThread.setSelected(presentationThread);
		botLeftPanel.add(chckbxPresentationThread);
		
		JLabel lblThreads = new JLabel("Thread(s)");
		lblThreads.setHorizontalAlignment(SwingConstants.CENTER);
		lblThreads.setBounds(20, 13, 114, 16);
		botLeftPanel.add(lblThreads);

		JLabel elapsedTimeLabel = new JLabel("Time Elapsed");
		elapsedTimeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		elapsedTimeLabel.setBounds(655, 483, 111, 25);
		contentPane.add(elapsedTimeLabel);

		JLabel rotatePositionLabel = new JLabel("Rotational Position");
		rotatePositionLabel.setHorizontalAlignment(SwingConstants.CENTER);
		rotatePositionLabel.setBounds(262, 483, 134, 25);
		contentPane.add(rotatePositionLabel);
		
		elapsedTimeDisp = new JTextField();
		elapsedTimeDisp.setColumns(5);
		elapsedTimeDisp.setBounds(759, 481, 216, 28);
		contentPane.add(elapsedTimeDisp);
		
		rotatePositionDisp = new JTextField();
		rotatePositionDisp.setColumns(5);
		rotatePositionDisp.setBounds(395, 481, 216, 28);
		contentPane.add(rotatePositionDisp);
	}

	@Override
	public void present(SimulationResult result) throws InterruptedException {
		presentation_panel.updateGrid(result);
		
		// Shift sun position from -180 - 180 to 0 - 360
		float sunPositionShifted = result.getSunPosition() + 180;
		presentation_panel.moveSunPosition(sunPositionShifted);
		incrementTimeElapsed(sunPositionShifted);
		previousSunPosition = sunPositionShifted;
		
		rotatePositionDisp.setText(String.format("%f", result.getSunPosition()));
		
		System.out.println(result.getTemperature(1, 1));
		LOGGER.info("Temperature (1,1): " + result.getTemperature(1, 1));
		LOGGER.info("Sun position: " + result.getSunPosition());
	}
	
	private void incrementTimeElapsed(float sunPosition) {
		elapsedTimeDisp.setText(Utils.convertSecondsToTimeString(secondsElapsed));
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}
	
	private void setInputsEnabled(boolean value) {
		gridSpacing.setEnabled(value);
		timeStep.setEnabled(value);
		displayRate.setEnabled(value);
		initiativeEntry.setEnabled(value);
		bufferSizeEntry.setEditable(value);
		chckbxNewCheckBox.setEnabled(value);
		chckbxPresentationThread.setEnabled(value);
	}
}
