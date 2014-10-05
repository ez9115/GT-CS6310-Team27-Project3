package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

import java.awt.Color;
import java.awt.SystemColor;
import java.awt.TextArea;

import javax.swing.JTextPane;

public class GUIApp extends JFrame {

	private JPanel contentPane;
	private JTextField txtEnterAValue;
	private JTextField txtTo;
	private JTextField textField;
	private JTextField txtSimOrPre;
	private JTextField txtSimPreGui;
	private JTextField txtPositiveInt;

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

	/**
	 * Create the frame.
	 */
	public GUIApp() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(1, 1, 825, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnStart = new JButton("Start");
		btnStart.setBounds(261, 543, 117, 29);
		contentPane.add(btnStart);
		
		JButton btnStop = new JButton("Resume");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnStop.setBounds(594, 543, 117, 29);
		contentPane.add(btnStop);
		
		JButton button = new JButton("Stop");
		button.setBounds(371, 543, 117, 29);
		contentPane.add(button);
		
		JButton btnPause = new JButton("Pause");
		btnPause.setBounds(482, 543, 117, 29);
		contentPane.add(btnPause);
		
		JPanel panel = new JPanel();
		panel.setBorder(null);
		panel.setBounds(160, 12, 640, 450);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JPanel presentation_panel = new JPanel();
		presentation_panel.setBorder(new LineBorder(Color.DARK_GRAY, 2, true));
		presentation_panel.setBounds(12, 18, 628, 426);
		panel.add(presentation_panel);
		JImageComponent ic = new JImageComponent(); //this is what I'm not getting
		presentation_panel.add(ic); //it should bring in the image???
				
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(6, 30, 155, 207);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		txtEnterAValue = new JTextField();
		txtEnterAValue.setBounds(40, 5, 74, 28);
		txtEnterAValue.setText("1 to 180");
		panel_1.add(txtEnterAValue);
		txtEnterAValue.setColumns(5);
		
		JLabel lblNewLabel = new JLabel(" Grid Spacing (degrees)");
		lblNewLabel.setBounds(5, 38, 144, 16);
		panel_1.add(lblNewLabel);
		
		txtTo = new JTextField();
		txtTo.setBounds(40, 59, 74, 28);
		txtTo.setText("1 to 1440");
		txtTo.setColumns(5);
		panel_1.add(txtTo);
		
		JLabel lblTimeStepminutes = new JLabel("Time Step (minutes)");
		lblTimeStepminutes.setBounds(15, 92, 125, 16);
		panel_1.add(lblTimeStepminutes);
		
		textField = new JTextField();
		textField.setBounds(40, 113, 74, 28);
		textField.setText("18 - 24");
		textField.setColumns(5);
		panel_1.add(textField);
		
		JLabel lblDisplayRate = new JLabel("Display Rate");
		lblDisplayRate.setBounds(38, 146, 78, 16);
		panel_1.add(lblDisplayRate);
		
		JLabel lblframesPerSecond = new JLabel("(frames per second)");
		lblframesPerSecond.setBounds(15, 167, 124, 16);
		panel_1.add(lblframesPerSecond);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(6, 249, 155, 213);
		contentPane.add(panel_2);
		panel_2.setLayout(null);
		
		JLabel lblThreads = new JLabel("Threads");
		lblThreads.setBounds(39, 51, 78, 16);
		lblThreads.setHorizontalAlignment(SwingConstants.CENTER);
		panel_2.add(lblThreads);
		
		txtSimOrPre = new JTextField();
		txtSimOrPre.setHorizontalAlignment(SwingConstants.CENTER);
		txtSimOrPre.setBounds(39, 22, 78, 28);
		txtSimOrPre.setText("s or p");
		txtSimOrPre.setColumns(5);
		panel_2.add(txtSimOrPre);
		
		JLabel lblInitiative = new JLabel("Initiative");
		lblInitiative.setBounds(39, 106, 78, 16);
		lblInitiative.setHorizontalAlignment(SwingConstants.CENTER);
		panel_2.add(lblInitiative);
		
		txtSimPreGui = new JTextField();
		txtSimPreGui.setHorizontalAlignment(SwingConstants.CENTER);
		txtSimPreGui.setBounds(20, 77, 114, 28);
		txtSimPreGui.setText("Sim, Pre, GUI");
		txtSimPreGui.setColumns(5);
		panel_2.add(txtSimPreGui);
		
		JLabel lblBufferSize = new JLabel("Buffer Size");
		lblBufferSize.setBounds(39, 166, 78, 16);
		lblBufferSize.setHorizontalAlignment(SwingConstants.CENTER);
		panel_2.add(lblBufferSize);
		
		txtPositiveInt = new JTextField();
		txtPositiveInt.setHorizontalAlignment(SwingConstants.CENTER);
		txtPositiveInt.setBounds(39, 134, 78, 28);
		txtPositiveInt.setText(">= 1");
		txtPositiveInt.setColumns(5);
		panel_2.add(txtPositiveInt);
		
		TextArea textArea = new TextArea();
		textArea.setForeground(new Color(0, 0, 0));
		textArea.setBounds(17, 512, 128, 25);
		contentPane.add(textArea);
		
		JLabel lblNewLabel_1 = new JLabel("Time Elapsed");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(6, 544, 157, 25);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblPositionOfThe = new JLabel("Rotational Position");
		lblPositionOfThe.setHorizontalAlignment(SwingConstants.CENTER);
		lblPositionOfThe.setBounds(27, 474, 134, 25);
		contentPane.add(lblPositionOfThe);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBounds(160, 478, 659, 21);
		contentPane.add(panel_3);
	}
}
