package view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import controller.MLFQ2;
//import controller.MultiLevelFeedbackQueueScheduling;
import controller.MultiLevelQueueScheduling;
import controller.Scheduler;
import controller.Utility;
import model.Process;

public class MainProgram extends JFrame implements ActionListener {
	/**
	 * GUI version of the CPU Scheduler Simulator
	 */
	private static final long serialVersionUID = 1L;
	private JMenuBar menuBar;
	private JMenu menu_file;
	private JMenu menu_view;
	private JMenu menu_about;
	private JPanel mainPanel;

	
	private DefaultTableModel model1;
	private JTable table;
	private JScrollPane tablePane;
	private JButton btn_add;
	private JButton btn_remove;
	private JLabel lb_status;
	private JButton btn_compute;
	private OneSchedulerPanel subPanel;
	private CompareSchedulersPanel comparePanel;
	private AnalysisPanel analysisPanel;
	private int state;
	private List<String> viewCommand = Arrays.asList(new String[] { "One", "Two", "All", "MLQ", "MLFQ" });
	private MultiQueueOptionPanel mlqPanel;

	public void GUI() {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		menuBar = new JMenuBar();
		menu_file = new JMenu("File");
		menu_view = new JMenu("View");
		menu_about = new JMenu("About");

		JMenuItem menuItem_new = new JMenuItem("  New", new ImageIcon("icon/new.png"));
		menuItem_new.setActionCommand("New");
		JMenuItem menuItem_open = new JMenuItem("  Open", new ImageIcon("icon/open.png"));
		menuItem_open.setActionCommand("Open");
		JMenuItem menuItem_exit = new JMenuItem("  Exit", new ImageIcon("icon/exit.png"));
		menuItem_exit.setActionCommand("Exit");
		menu_file.add(menuItem_new);
		menu_file.add(menuItem_open);
		menu_file.add(menuItem_exit);
		JMenuItem menuItem_one = new JMenuItem("One Scheduler");
		menuItem_one.setActionCommand("One");
		JMenuItem menuItem_two = new JMenuItem("Compare Two Schedulers");
		menuItem_two.setActionCommand("Two");
		JMenuItem menuItem_all = new JMenuItem("Analysis All Schedulers");
		menuItem_all.setActionCommand("All");
		JMenuItem menuItem_MLQ = new JMenuItem("MLQ Scheduler");
		menuItem_MLQ.setActionCommand("MLQ");
		JMenuItem menuItem_MLFQ = new JMenuItem("MLFQ Scheduler");
		menuItem_MLFQ.setActionCommand("MLFQ");
		menu_view.add(menuItem_one);
		menu_view.add(menuItem_two);
		menu_view.add(menuItem_all);
		menu_view.addSeparator();
		menu_view.add(menuItem_MLQ);
		menu_view.add(menuItem_MLFQ);

		JMenuItem menuItem_about = new JMenuItem("About");
		menuItem_about.setActionCommand("About");
		menu_about.add(menuItem_about);
		menuBar.add(menu_file);
		menuBar.add(menu_view);
		menuBar.add(menu_about);
		setJMenuBar(menuBar);

		menuItem_new.addActionListener(this);
		menuItem_open.addActionListener(this);
		menuItem_exit.addActionListener(this);
		menuItem_one.addActionListener(this);
		menuItem_two.addActionListener(this);
		menuItem_all.addActionListener(this);
		menuItem_about.addActionListener(this);
		menuItem_MLQ.addActionListener(this);
		menuItem_MLFQ.addActionListener(this);
		Utility.changeFont(menuBar, 13);
		model1 = new DefaultTableModel(
				new String[] { "Process", "Arrive Time", "Burst Time", "Priority", "Wait Time", "Turn Around Time" },
				0);

		table = new JTable(model1);
		table.setFillsViewportHeight(true);
		tablePane = new JScrollPane(table);
		Utility.setCellsAlignment(table, SwingConstants.CENTER);

		btn_add = new JButton("Add");
		btn_add.addActionListener(this);
		btn_remove = new JButton("Remove");
		btn_remove.addActionListener(this);
		lb_status = new JLabel("Status: Single Queue");
		btn_compute = new JButton("Compute");
		btn_compute.addActionListener(this);

		mainPanel = new JPanel(null);
		mainPanel.setPreferredSize(new Dimension(745, 560));
		tablePane.setBounds(25, 25, 700, 250);
		btn_add.setBounds(25, 280, 85, 25);
		btn_remove.setBounds(120, 280, 90, 25);
		lb_status.setBounds(480, 280, 130, 25);
		btn_compute.setBounds(630, 280, 95, 25);
		mainPanel.add(tablePane);
		mainPanel.add(btn_add);
		mainPanel.add(btn_remove);
		mainPanel.add(lb_status);
		mainPanel.add(btn_compute);

		subPanel = new OneSchedulerPanel(700, 250);
		subPanel.setLocation(25, 310);
		mainPanel.add(subPanel);
		subPanel.setVisible(true);

		comparePanel = new CompareSchedulersPanel(700, 250);
		comparePanel.setLocation(25, 310);
		comparePanel.setVisible(false);
		mainPanel.add(comparePanel);

		analysisPanel = new AnalysisPanel(700, 250);
		analysisPanel.setLocation(25, 350);
		analysisPanel.setVisible(false);
		mainPanel.add(analysisPanel);

		mlqPanel = new MultiQueueOptionPanel(250, 90);
		mlqPanel.setLocation(450, 140);
		mlqPanel.setVisible(false);
		subPanel.add(mlqPanel);

		setIconImage(new ImageIcon("icon/cpu2.png").getImage());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setResizable(false);
		getContentPane().add(mainPanel);
		pack();
		Utility.changeFont(mainPanel, 13);
		Utility.changeFont(subPanel, 12);
		Utility.changeFont(comparePanel, 12);
	}

	public MainProgram(String s) {
		super(s);
		state = 1;
		GUI();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btn_add) {
			model1.addRow(new String[] { "", "", "", "", "", "" });
		}
		if (e.getSource() == btn_remove) {
			onBtnRemoveClick();
		}

		if (e.getSource() == btn_compute) {
			onBtnComputeClick();
		}
		String action = e.getActionCommand();
		if (viewCommand.contains(action)) {
			onViewScheduleClick(action);
		}
		if (action.equals("New")) {
			model1.setRowCount(0);
			subPanel.clearPanel();
			comparePanel.clearPanel();
			analysisPanel.clearTable();
		}
		if (action.equals("Open")) {
			onOpenMenuClick();
		}
		if (action.equals("Exit")) {
			System.exit(0);
		}
		if (action.equals("About")) {
			String about = "This program is created by Quang Tinh, inspirated by Mr. Marvin Jason Sy"
					+ "\nThe main target is to simulate the CPU's processes scheduling";
			JOptionPane.showMessageDialog(null, about, "About", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private void onOpenMenuClick() {
		String filepath = Utility.loadFileFromComputer();
		if (filepath != null) {
			File inputFile = new File(filepath);
			try {
				model1.setRowCount(0);
				Scanner scan = new Scanner(new FileReader(inputFile));
				scan.nextLine();
				while (scan.hasNext()) {
					model1.addRow(new String[] { scan.next(), scan.nextInt() + "", scan.nextInt() + "",
							scan.nextInt() + "", scan.nextInt() + "", scan.nextInt() + "" });
				}
				scan.close();
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(null, "File not found", "Load File Error", JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(null, "Invalid File! Open a valid Job List.", "Invalid File",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void onViewScheduleClick(String s) {
		switch (s) {
		case "One":
			subPanel.setVisible(true);
			comparePanel.setVisible(false);
			analysisPanel.setVisible(false);
			changeToMLQTable(false);
			lb_status.setText("Status: Single Queue");
			subPanel.setOptionState(true);
			mlqPanel.setVisible(false);
			state = 1;
			break;
		case "Two":
			subPanel.setVisible(false);
			comparePanel.setVisible(true);
			analysisPanel.setVisible(false);
			lb_status.setText("Status: Single Queue");
			changeToMLQTable(false);
			state = 2;
			break;
		case "All":
			subPanel.setVisible(false);
			comparePanel.setVisible(false);
			analysisPanel.setVisible(true);
			lb_status.setText("Status: Single Queue");
			changeToMLQTable(false);
			state = 3;
			break;
		case "MLQ":
			changeToMLQTable(true);
			subPanel.setVisible(true);
			comparePanel.setVisible(false);
			analysisPanel.setVisible(false);
			lb_status.setText("Status: MLQ");
			subPanel.setOptionState(false);
			subPanel.clearPanel();
			mlqPanel.setVisible(true);
			state = 4;
			break;
		case "MLFQ":
			subPanel.setVisible(true);
			subPanel.setOptionState(false);
			mlqPanel.setVisible(true);
			subPanel.clearPanel();
			lb_status.setText("Status: MLFQ");
			state = 5;
			break;
		default:
			break;
		}

	}

	private void changeToMLQTable(boolean b) {
		if (b) {
			TableColumn tc = table.getTableHeader().getColumnModel().getColumn(3);
			tc.setHeaderValue("Queue No.");
			table.getTableHeader().repaint();
		} else {
			TableColumn tc = table.getTableHeader().getColumnModel().getColumn(3);
			tc.setHeaderValue("Priority");
			table.getTableHeader().repaint();
		}
	}

	private void onBtnRemoveClick() {
		int count = table.getSelectedRowCount();
		if (count > 0) {
			if (count == 1) {
				int row = table.getSelectedRow();
				if (row > -1) {
					model1.removeRow(row);
				}
			} else {
				int[] rows = table.getSelectedRows();
				Arrays.sort(rows);
				for (int i = rows.length - 1; i >= 0; i--) {
					model1.removeRow(rows[i]);
				}
			}
		}
	}

	public void onBtnComputeClick() {
		String selected = subPanel.getOption();
		List<Process> readyQueue = getReadyQueue();
		if (readyQueue != null) {
			if (state == 1) {
				Scheduler scheduler = Utility.selectScheduler(selected);
				scheduler.setProcessQueue(readyQueue);
				scheduler.process();
				fillWaitAndTurnAround(scheduler);
				subPanel.setAverageValue(scheduler.getAverageWaitingTime(), scheduler.getAverageTurnAroundTime());
				subPanel.setTimeLine(scheduler.getTimeline());
			}
			if (state == 2) {
				String selected1 = comparePanel.getOption1();
				String selected2 = comparePanel.getOption2();
				comparePanel.setSchedulers(readyQueue, selected1, selected2);
				Scheduler scheduler = comparePanel.getFirstScheduler();
				fillWaitAndTurnAround(scheduler);
			}
			if (state == 3) {
				analysisPanel.setReadyQueue(readyQueue);
			}
			if (state == 4) {
				MultiLevelQueueScheduling scheduler = new MultiLevelQueueScheduling();
				scheduler.setProcessQueue(readyQueue);
				scheduler.setSchedulerNames(mlqPanel.getOptions());
				try {
					scheduler.setQuantumTimes(mlqPanel.getQuantumTime());
					scheduler.process();
					fillWaitAndTurnAround(scheduler);
					subPanel.setAverageValue(scheduler.getAverageWaitingTime(), scheduler.getAverageTurnAroundTime());
					subPanel.setTimeLine(scheduler.getTimeline());
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e.getMessage(), "Error!", JOptionPane.WARNING_MESSAGE);
				}
			}
			if (state == 5) {
				// MultiLevelFeedbackQueueScheduling scheduler = new
				// MultiLevelFeedbackQueueScheduling();
				MLFQ2 scheduler = new MLFQ2();
				scheduler.setProcessQueue(readyQueue);
				scheduler.setSchedulerNames(mlqPanel.getOptions());
				try {
					scheduler.setQuantumTimes(mlqPanel.getQuantumTime());
					scheduler.process();
					fillWaitAndTurnAround(scheduler);
					subPanel.setAverageValue(scheduler.getAverageWaitingTime(), scheduler.getAverageTurnAroundTime());
					subPanel.setTimeLine(scheduler.getTimeline());
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, e.getMessage(), "Error!", JOptionPane.WARNING_MESSAGE);
				}
			}
		} else {
			JOptionPane.showMessageDialog(null, "No process to Schedule", "Warning!", JOptionPane.WARNING_MESSAGE);
		}
	}

	private void fillWaitAndTurnAround(Scheduler scheduler) {
		for (int i = 0; i < model1.getRowCount(); i++) {
			String process = (String) model1.getValueAt(i, 0);
			Process p = scheduler.getProcess(process);
			model1.setValueAt(p.getWaitingTime(), i, 4);
			model1.setValueAt(p.getTurnaroundTime(), i, 5);
		}
	}

	private List<Process> getReadyQueue() {
		List<Process> readyQueue = null;
		if (model1.getRowCount() > 0) {
			readyQueue = new ArrayList<>();
			for (int i = 0; i < model1.getRowCount(); i++) {
				String process = (String) model1.getValueAt(i, 0);
				try {
					int at = Integer.parseInt((String) model1.getValueAt(i, 1));
					int bt = Integer.parseInt((String) model1.getValueAt(i, 2));
					int pl;
					if (!model1.getValueAt(i, 3).equals("")) {
						pl = Integer.parseInt((String) model1.getValueAt(i, 3));
						if (pl == 0) {
							model1.setValueAt(1 + "", i, 3);
							JOptionPane.showMessageDialog(null, "Priority must be larger than 1.\nAuto change to 1",
									"Run error", JOptionPane.ERROR_MESSAGE);
							pl = 1;
						}
						if (pl > 3) {
							model1.setValueAt(3 + "", i, 3);
							JOptionPane.showMessageDialog(null,
									"Program can only handle Priority from 1 - 3.\nAuto change to 3", "Run error",
									JOptionPane.ERROR_MESSAGE);
							pl = 3;
						}
					} else {
						pl = 1;
					}
					readyQueue.add(new Process(process, at, bt, pl));
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Must enter data!", "Run error", JOptionPane.ERROR_MESSAGE);
					return null;
				}
			}
			return readyQueue;
		} else {
			return null;
		}
	}

	public static void main(String[] args) {
		new MainProgram("CPU Scheduler Simulator");
	}
}
