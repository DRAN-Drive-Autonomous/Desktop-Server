package gui;
import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import java.awt.Font;
import java.awt.CardLayout;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import aiOps.FCar;
import aiOps.OCR;
import server.Server;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JTextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class GUI {
	Server ser = new Server();
	Dimension screensize;
	int mainFrameHeight, mainFrameWidth;
	boolean serverStarted = false;

	private JFrame MainFrame;
	private JTextField PortValueInput;
	private JLabel GrabbedImage;
	private JLabel LaneSegmentImage;
	private JLabel TrafficSegmentImage;
	private JLabel TrafficLightSegmentImage;
	private JButton ServerButton;
	private JLabel NetworkLabel;
	private JLabel ServerStatusLabel;
	
	private SwingWorker<String, Void> worker = new SwingWorker<String, Void>(){

		@Override
		protected String doInBackground() throws Exception {
			String clientHostData = ser.startServer2();
			System.out.println("Done1");
			return clientHostData;
		}

		@Override
		protected void done() {
			String clientHostData = "";
			try {
				clientHostData = get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ServerButton.setEnabled(true);
			try {
				NetworkLabel.setText(String.format("%s %s %s %s %s", "<html><body style='padding: 10px;'>", clientHostData, "<br><br>", ser.getINetworkDetailsHTML(), "</body></html>"));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			ServerStatusLabel.setText("Server connected");
			System.out.println("Done");
			return;
		}
		
	};

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		OCR tessocr = new OCR();
		float currentCarSpeed = tessocr.doOCR();
		FCar pidCar = new FCar();
		pidCar.initiatePID(60);
		System.out.println("Car Name: " + pidCar.name);
		System.out.println("Owner Name: " + pidCar.owner);
		System.out.println("Car Speed: " + currentCarSpeed);
		System.out.println("PID Action: " + pidCar.sendPIDAction(currentCarSpeed));
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.MainFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws Exception 
	 */
	public GUI() throws Exception {
		initialize();
		setGrabbedImage();
		setLaneSegmentImage();
		setTrafficSegmentImage();
		setTrafficLightSegmentImage();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws Exception 
	 */
	private void initialize() throws Exception {
		Color orangeColor = new Color(255, 136, 0);
		Color darkColor = new Color(34, 34, 44);
		
		MainFrame = new JFrame();
		MainFrame.setIconImage(Toolkit.getDefaultToolkit().getImage("./src/assets/images/logo.png"));
		MainFrame.setTitle("DRAN - Drive Autonomous");
		MainFrame.getContentPane().setBackground(orangeColor);
//		frame.setSize(screen);
		MainFrame.setBounds(0, 0, 1440, 800);
		MainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		MainFrame.setResizable(false);
		MainFrame.setVisible(true);
		MainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		MainFrame.getContentPane().setLayout(null);
		
		// Get Frame Sizes
		screensize = MainFrame.getSize();
		mainFrameHeight = (int) screensize.getHeight();
		mainFrameWidth = (int) screensize.getWidth();
		int mainPanelWidth = (mainFrameWidth - 40) / 2;
		int mainPanelHeight = (mainFrameHeight - 60) / 2;
		int outputSubPanelWidth = (mainPanelWidth - 8)/ 2;
		int outputSubPanelHeight = (mainPanelHeight - 8)/ 2;
		int nameLabelMajorHeight = (int)(0.8 * mainPanelHeight);
		int nameLabelMinorHeight = mainPanelHeight - nameLabelMajorHeight;
		
		int rightPanelX = (mainFrameWidth / 2) - 4;
		int lowerPanelY = (mainFrameHeight / 2) - 14; 
		int rightOutputSubPanelX = outputSubPanelWidth + 8;
		int lowerOutputSubPanelY = outputSubPanelHeight + 9;
		
		JPanel GrabImagePanel = new JPanel();
		GrabImagePanel.setBackground(darkColor);
		GrabImagePanel.setBounds(8, 8, mainPanelWidth, mainPanelHeight);
		MainFrame.getContentPane().add(GrabImagePanel);
		
		GrabbedImage = new JLabel();
		GrabbedImage.setBounds(0, 0, mainPanelWidth, mainPanelHeight);
		GrabImagePanel.setLayout(null);
		GrabImagePanel.add(GrabbedImage);
		
		JPanel OutputMainPanel = new JPanel();
		OutputMainPanel.setBackground(orangeColor);
		OutputMainPanel.setBounds(rightPanelX, 8, mainPanelWidth, mainPanelHeight);
		MainFrame.getContentPane().add(OutputMainPanel);
		OutputMainPanel.setLayout(null);
		
		JPanel LaneDetectionPanel = new JPanel();
		LaneDetectionPanel.setBackground(darkColor);
		LaneDetectionPanel.setBounds(0, 0, outputSubPanelWidth, outputSubPanelHeight);
		OutputMainPanel.add(LaneDetectionPanel);
		LaneDetectionPanel.setLayout(null);
		
		LaneSegmentImage = new JLabel();
		LaneSegmentImage.setBounds(0, 0, outputSubPanelWidth, outputSubPanelHeight);
		LaneDetectionPanel.add(LaneSegmentImage);
		
		JPanel TrafficDetectionPanel = new JPanel();
		TrafficDetectionPanel.setBackground(darkColor);
		TrafficDetectionPanel.setBounds(rightOutputSubPanelX, 0, outputSubPanelWidth, outputSubPanelHeight);
		OutputMainPanel.add(TrafficDetectionPanel);
		TrafficDetectionPanel.setLayout(null);
		
		TrafficSegmentImage = new JLabel();
		TrafficSegmentImage.setBounds(0, 0, outputSubPanelWidth, outputSubPanelHeight);
		TrafficDetectionPanel.add(TrafficSegmentImage);
		
		JPanel TrafficLightDetectionPanel = new JPanel();
		TrafficLightDetectionPanel.setBackground(darkColor);
		TrafficLightDetectionPanel.setBounds(0, lowerOutputSubPanelY, outputSubPanelWidth, outputSubPanelHeight);
		OutputMainPanel.add(TrafficLightDetectionPanel);
		TrafficLightDetectionPanel.setLayout(null);
		
		TrafficLightSegmentImage = new JLabel();
		TrafficLightSegmentImage.setBounds(0, 0, outputSubPanelWidth, outputSubPanelHeight);
		TrafficLightDetectionPanel.add(TrafficLightSegmentImage);
		
		JPanel FinalOutputPanel = new JPanel();
		FinalOutputPanel.setBackground(darkColor);
		FinalOutputPanel.setBounds(rightOutputSubPanelX, lowerOutputSubPanelY, outputSubPanelWidth, outputSubPanelHeight);
		OutputMainPanel.add(FinalOutputPanel);
		FinalOutputPanel.setLayout(null);
		
		JLabel SpeedLabel = new JLabel("Current Speed");
		SpeedLabel.setForeground(Color.WHITE);
		SpeedLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		SpeedLabel.setHorizontalAlignment(SwingConstants.CENTER);
		SpeedLabel.setBounds(0, 0, outputSubPanelWidth/2, outputSubPanelHeight/2);
		FinalOutputPanel.add(SpeedLabel);
		
		JLabel KeyLabel = new JLabel("Key Pressed");
		KeyLabel.setForeground(Color.WHITE);
		KeyLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		KeyLabel.setHorizontalAlignment(SwingConstants.CENTER);
		KeyLabel.setBounds(0, outputSubPanelHeight/2, outputSubPanelWidth/2, outputSubPanelHeight/2);
		FinalOutputPanel.add(KeyLabel);
		
		JLabel Colon1 = new JLabel(":");
		Colon1.setForeground(Color.WHITE);
		Colon1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		Colon1.setHorizontalAlignment(SwingConstants.CENTER);
		Colon1.setBounds(outputSubPanelWidth/2, 0, outputSubPanelWidth/4, outputSubPanelHeight/2);
		FinalOutputPanel.add(Colon1);
		
		JLabel Colon2 = new JLabel(":");
		Colon2.setForeground(Color.WHITE);
		Colon2.setFont(new Font("Tahoma", Font.PLAIN, 18));
		Colon2.setHorizontalAlignment(SwingConstants.CENTER);
		Colon2.setBounds(outputSubPanelWidth/2, outputSubPanelHeight/2, outputSubPanelWidth/4, outputSubPanelHeight/2);
		FinalOutputPanel.add(Colon2);
		
		JLabel SpeedValue = new JLabel("60");
		SpeedValue.setForeground(Color.WHITE);
		SpeedValue.setFont(new Font("Tahoma", Font.PLAIN, 18));
		SpeedValue.setHorizontalAlignment(SwingConstants.CENTER);
		SpeedValue.setBounds((3*outputSubPanelWidth)/4, 0, outputSubPanelWidth/4, outputSubPanelHeight/2);
		FinalOutputPanel.add(SpeedValue);
		
		JLabel KeyValue = new JLabel("WA");
		KeyValue.setForeground(Color.WHITE);
		KeyValue.setFont(new Font("Tahoma", Font.PLAIN, 18));
		KeyValue.setHorizontalAlignment(SwingConstants.CENTER);
		KeyValue.setBounds((3*outputSubPanelWidth)/4, outputSubPanelHeight/2, outputSubPanelWidth/4, outputSubPanelHeight/2);
		FinalOutputPanel.add(KeyValue);
		
		JPanel NetworkPanel = new JPanel();
		NetworkPanel.setBackground(darkColor);
		NetworkPanel.setBounds(8, lowerPanelY, mainPanelWidth, mainPanelHeight);
		MainFrame.getContentPane().add(NetworkPanel);
		NetworkPanel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(null);
		scrollPane.setEnabled(false);
		scrollPane.setBounds(0, 0, mainPanelWidth, mainPanelHeight - 100);
		NetworkPanel.add(scrollPane);
		
		JPanel NetworkSubPanel = new JPanel();
		NetworkSubPanel.setBackground(darkColor);
		scrollPane.setViewportView(NetworkSubPanel);
		NetworkSubPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		NetworkLabel = new JLabel(("<html><body style='padding: 10px;'>" + ser.getINetworkDetailsHTML() + "</body></html>"));
		NetworkLabel.setForeground(Color.WHITE);
		NetworkLabel.setBounds(0, 0, 700, 370);
		NetworkLabel.setVerticalAlignment(SwingConstants.TOP);
		NetworkLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		NetworkLabel.setHorizontalAlignment(SwingConstants.LEFT);
		NetworkSubPanel.add(NetworkLabel);
		
		ServerStatusLabel = new JLabel("Server not working");
		ServerStatusLabel.setForeground(Color.RED);
		ServerStatusLabel.setFont(new Font("Tahoma", Font.BOLD, 25));
		ServerStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);
		ServerStatusLabel.setBounds(0, mainPanelHeight-90, mainPanelWidth, 25);
		NetworkPanel.add(ServerStatusLabel);
		
		ServerButton = new JButton("Start Server");
		ServerButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				serverBtnAction();
			}
		});
		ServerButton.setBounds(((3*mainPanelWidth) / 4) + 10, mainPanelHeight-55, ((mainPanelWidth / 4) - 10), 50);
		NetworkPanel.add(ServerButton);
		
		PortValueInput = new JTextField();
		PortValueInput.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (PortValueInput.getText().equals("Enter Port Number (recommended 10,000 to 50,000)")) {
					PortValueInput.setText("");
					PortValueInput.setForeground(new Color(0, 0, 0));
				}
			}
			@Override
			public void focusLost(FocusEvent e) {
				if (PortValueInput.getText().equals("")) {
					PortValueInput.setText("Enter Port Number (recommended 10,000 to 50,000)");
					PortValueInput.setForeground(Color.LIGHT_GRAY);
				}
			}
		});
		PortValueInput.setForeground(Color.LIGHT_GRAY);
		PortValueInput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				
				if (!Character.isDigit(c)) {
					e.consume();
				}
			}
		});
		PortValueInput.setFont(new Font("Tahoma", Font.PLAIN, 18));
		PortValueInput.setHorizontalAlignment(SwingConstants.CENTER);
		PortValueInput.setText("Enter Port Number (recommended 10,000 to 50,000)");
		PortValueInput.setBounds(0, mainPanelHeight-55, ((3*mainPanelWidth) / 4), 50);
		NetworkPanel.add(PortValueInput);
		PortValueInput.setColumns(10);
		
		JPanel NamePanel = new JPanel();
		NamePanel.setBackground(darkColor);
		NamePanel.setBounds(rightPanelX, lowerPanelY, mainPanelWidth, mainPanelHeight);
		MainFrame.getContentPane().add(NamePanel);
		NamePanel.setLayout(null);
		
		JLabel ProjectLabel = new JLabel("<html><body style='text-align: center'>DRAN</body></html>");
		ProjectLabel.setForeground(Color.WHITE);
		ProjectLabel.setBounds(0, 0, mainPanelWidth, nameLabelMajorHeight);
		ProjectLabel.setFont(new Font("Tahoma", Font.BOLD, 38));
		ProjectLabel.setHorizontalAlignment(SwingConstants.CENTER);
		NamePanel.add(ProjectLabel);
		
		JLabel SubProjectLabel = new JLabel("<html><body style='text-align: center'>Drive Autonomous</body></html>");
		SubProjectLabel.setForeground(Color.WHITE);
		SubProjectLabel.setBounds(0, 0, mainPanelWidth, nameLabelMajorHeight+100);
		SubProjectLabel.setFont(new Font("Tahoma", Font.PLAIN, 28));
		SubProjectLabel.setHorizontalAlignment(SwingConstants.CENTER);
		NamePanel.add(SubProjectLabel);
		
		JLabel CreatorLabel = new JLabel("Made by Tushar Jain (20UCS211)");
		CreatorLabel.setForeground(Color.WHITE);
		CreatorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		CreatorLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		CreatorLabel.setBounds(0, nameLabelMajorHeight, mainPanelWidth, nameLabelMinorHeight);
		NamePanel.add(CreatorLabel);
	}
	
	public void setGrabbedImage() {
		ImageIcon imgicon = new ImageIcon("./src/assets/images/GrabImage.png");
		imgicon = new ImageIcon(imgicon.getImage().getScaledInstance(GrabbedImage.getWidth(), GrabbedImage.getHeight(), Image.SCALE_FAST));
		GrabbedImage.setIcon(imgicon);
	}
	
	public void setLaneSegmentImage() {
		ImageIcon imgicon = new ImageIcon("./src/assets/images/LaneImage.png");
		imgicon = new ImageIcon(imgicon.getImage().getScaledInstance(LaneSegmentImage.getWidth(), LaneSegmentImage.getHeight(), Image.SCALE_FAST));
		LaneSegmentImage.setIcon(imgicon);
	}
	
	public void setTrafficSegmentImage() {
		ImageIcon imgicon = new ImageIcon("./src/assets/images/TrafficImage.png");
		imgicon = new ImageIcon(imgicon.getImage().getScaledInstance(TrafficSegmentImage.getWidth(), TrafficSegmentImage.getHeight(), Image.SCALE_FAST));
		TrafficSegmentImage.setIcon(imgicon);
	}
	
	public void setTrafficLightSegmentImage() {
		ImageIcon imgicon = new ImageIcon("./src/assets/images/TrafficLightImage.png");
		imgicon = new ImageIcon(imgicon.getImage().getScaledInstance(TrafficLightSegmentImage.getWidth(), TrafficLightSegmentImage.getHeight(), Image.SCALE_FAST));
		TrafficLightSegmentImage.setIcon(imgicon);
	}
	
	public void serverBtnAction() {
		String portStr = PortValueInput.getText();
		Integer portNum;
		if (!(portStr.equals("") || portStr.equals("Enter Port Number (recommended 10,000 to 50,000)"))) {
			portNum = Integer.parseInt(PortValueInput.getText());
		} else {
			portNum = -1;
		}
		if (portNum > 1024 && portNum < 65000) {
			if (!serverStarted) {
				ser.startServer1(portNum);
				ServerButton.setEnabled(false);
				ServerButton.setText("Stop Server");
				ServerStatusLabel.setText("Server is waiting for clients...");
				worker.execute();
				serverStarted = true;
//				ServerButton.setEnabled(true);
			} else {
				ServerButton.setEnabled(false);
				ServerButton.setText("Start Server");
				if (worker.isDone()) {
					try {
						ser.stopServer();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					try {
						NetworkLabel.setText(("<html><body style='padding: 10px;'>" + ser.getINetworkDetailsHTML() + "</body></html>"));
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					serverStarted = false;
					ServerButton.setEnabled(true);
					ServerStatusLabel.setText("Server not working");
					
					// Reinitiating Swing Worker
					worker = new SwingWorker<String, Void>(){

						@Override
						protected String doInBackground() throws Exception {
							String clientHostData = ser.startServer2();
							return clientHostData;
						}

						@Override
						protected void done() {
							String clientHostData = "";
							try {
								clientHostData = get();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (ExecutionException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							ServerButton.setEnabled(true);
							try {
								NetworkLabel.setText(String.format("%s %s %s %s %s", "<html><body style='padding: 10px;'>", clientHostData, "<br><br>", ser.getINetworkDetailsHTML(), "</body></html>"));
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							ServerStatusLabel.setText("Server connected");
							return;
						}
						
					};
				}	
			}
		}
		
	}
}
