package view;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import controller.FirstComeFirstServe;
import controller.HighestResponseRatioNext;
import controller.PriorityNonPreemptive;
import controller.PriorityPreemptive;
import controller.RoundRobin;
import controller.Scheduler;
import controller.ShortestJobFirst_NP;
import controller.ShortestRemainingTime;
import controller.Utility;
import model.Process;

public class OneScheduler extends JFrame implements ActionListener
{
    /**
	 * GUI version of the CPU Scheduler Simulator
	 */
	private static final long serialVersionUID = 1L;
    private JPanel mainPanel;
    private JScrollPane tablePane;
    private JScrollPane ganttChartPane;
    private JTable table;
    private JButton btn_add;
    private JButton btn_remove;
    private JButton btn_compute;
    private JPanel subPanel;
    private CustomPanel ganttChartPanel;
    private JLabel lb_wait;
    private JLabel lb_waitResult;
    private JLabel lb_tat;
    private JLabel lb_tatResult;
    private JComboBox<String> option;
    private DefaultTableModel model;
    private boolean computeFlag;
    
    private final static int width = 750;
    private final static int height = 550;
    public void GUI() {
    	model = new DefaultTableModel(new String[]{"Process", "Arrive Time", "Burst Time", 
    			"Priority", "Wait Time", "Turn Around Time"}, 0);
        	
    	table = new JTable(model);
        table.setFillsViewportHeight(true);
        tablePane = new JScrollPane(table);
        Utility.setCellsAlignment(table, SwingConstants.CENTER);
        btn_add = new JButton("Add");
        btn_add.addActionListener(this);
        btn_remove = new JButton("Remove");
        btn_remove.addActionListener(this);
        
        
        ganttChartPanel = new CustomPanel();
        ganttChartPanel.setBackground(Color.WHITE);
        ganttChartPane = new JScrollPane(ganttChartPanel);
        
        lb_wait = new JLabel("Average Waiting Time:");
        lb_tat = new JLabel("Average Turn Around Time:");
        lb_waitResult = new JLabel();
        lb_tatResult = new JLabel();
        String[] a = new String[] {"FCFS", "SJF", "SRT","HRRN","PSN", "PSP", "RR"};
        option = new JComboBox<String>(a);
        
        btn_compute = new JButton("Compute");
        btn_compute.addActionListener(this);
        
        mainPanel = new JPanel(null);
        mainPanel.setPreferredSize(new Dimension(width, height));
        tablePane.setBounds(25, 25, 700, 250);
        btn_add.setBounds(25, 280, 85, 25);
        btn_remove.setBounds(120, 280, 90 , 25);
        mainPanel.add(tablePane);
        mainPanel.add(btn_add);
        mainPanel.add(btn_remove);
  
        subPanel = new JPanel(null);
        subPanel.setBounds(25, 310, 700, 200);
        subPanel.setVisible(computeFlag);
        mainPanel.add(subPanel);
        
        ganttChartPane.setBounds(0, 10, 700,100);
        lb_wait.setBounds(0, 115, 180, 25);
        lb_tat.setBounds(0, 145, 180, 25);
        lb_waitResult.setBounds(360, 115, 170, 25);
        lb_tatResult.setBounds(360, 145, 170, 25);
        
        option.setBounds(605, 120, 95, 20);
        btn_compute.setBounds(605, 150, 95, 25);
        subPanel.add(ganttChartPane);
        subPanel.add(lb_wait);
        subPanel.add(lb_tat);
        subPanel.add(lb_waitResult);
        subPanel.add(lb_tatResult);
        subPanel.add(option);
        subPanel.add(btn_compute);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        add(mainPanel);
        pack();
        changeFont(mainPanel, 13);
    }
    public OneScheduler(String s)
    {
    	super(s);
    	computeFlag = true;
        GUI();
    }
    
    public static void main(String[] args)
    {
        new OneScheduler("CPU Scheduler Simulator");
    }
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btn_add) {
			 model.addRow(new String[]{"", "", "", "", "", ""});
		}
		if(e.getSource() == btn_remove) {
			onBtnRemoveClick();
		}
		if(e.getSource() == btn_compute){
			onBtnComputeClick();
		}
	}
	
	
	private void onBtnRemoveClick() {
		int count = table.getSelectedRowCount();
		if(count > 0 ){
			if(count == 1) {
				int row = table.getSelectedRow();
	            if (row > -1) {
	                 model.removeRow(row);
	            }
			}
			else {
				int[] rows = table.getSelectedRows();
				Arrays.sort(rows);
				for (int i = rows.length -1 ; i >= 0 ; i--) {
					model.removeRow(rows[i]);
				}
			}
		}
	}
	
	public void onBtnComputeClick() {
		String selected = (String) option.getSelectedItem();
        Scheduler scheduler;
        switch (selected) {
            case "FCFS":
                scheduler = new FirstComeFirstServe();
                break;
            case "SJF":
                scheduler = new ShortestJobFirst_NP();
                break;
            case "SRT":
                scheduler = new ShortestRemainingTime();
                break;
            case "HRRN":
                scheduler = new HighestResponseRatioNext();
                break;
            case "PSN":
                scheduler = new PriorityNonPreemptive();
                break;
            case "PSP":
                scheduler = new PriorityPreemptive();
                break;
            case "RR":
                String tq = JOptionPane.showInputDialog("Time Quantum");
                if (tq == null) {
                    return;
                }
                scheduler = new RoundRobin();
                scheduler.setTimeQuantum(Integer.parseInt(tq)); 
                break;
            default:
                return;
        }
        
        for (int i = 0; i < model.getRowCount(); i++) {
            String process = (String) model.getValueAt(i, 0);
            int at = Integer.parseInt((String) model.getValueAt(i, 1));
            int bt = Integer.parseInt((String) model.getValueAt(i, 2));
            int pl;
            
            if (selected.equals("PSN") || selected.equals("PSP")) {
                if (!model.getValueAt(i, 3).equals("")) {
                    pl = Integer.parseInt((String) model.getValueAt(i, 3));
                }
                else {
                    pl = 1;
                }
            }
            else {
                pl = 1;
            }
            scheduler.add(new Process(process, at, bt, pl));
        }
        
        scheduler.process();
        
        for (int i = 0; i < model.getRowCount(); i++) {
            String process = (String) model.getValueAt(i, 0);
            Process p = scheduler.getProcess(process);
            model.setValueAt(p.getWaitingTime(), i, 4);
            model.setValueAt(p.getTurnaroundTime(), i, 5);
        }
        
        lb_waitResult.setText(Double.toString(scheduler.getAverageWaitingTime()));
        lb_tatResult.setText(Double.toString(scheduler.getAverageTurnAroundTime()));
        
        ganttChartPanel.setTimeline(scheduler.getTimeline());
	}
	
	public static void changeFont(Component component, int fontSize) {
	    Font f = component.getFont();
	    component.setFont(new Font(f.getName(),f.getStyle(),fontSize));
	    if (component instanceof Container) {
	        for (Component child : ((Container) component).getComponents()) {
	            changeFont(child, fontSize);
	        }
	    }
	}
}

