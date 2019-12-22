package view;

import java.util.Arrays;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import controller.Scheduler;
import controller.Utility;
import model.Process;

public class AnalysisPanel extends JPanel {
	/**
	 * Implement of Analysis Panel: Contains results of all 
	 * Scheduling Policy 
	 */
	private static final long serialVersionUID = 1L;
	private DefaultTableModel model;
	private JScrollPane tablePane;
    private JTable table;
    private List<Process> readyQueue;
    private List<String> schedulerList = Arrays.asList(new String[]
    								{"FCFS","SJF","SRT","PSN","PSP","RR"});
    public AnalysisPanel(int width,int height) {
    	setSize(width,height/2 + 20);
    	setLayout(null);
    	model = new DefaultTableModel(new String[]{"Scheduler",
    							"Avg. Waiting Time", "Avg. Turn Around Time"}, 0);
    	model.addRow(new String[]{"First Come First Serve", "", ""});
    	model.addRow(new String[]{"Shortest Job First", "", ""});
    	model.addRow(new String[]{"Shortest Remaining Time First", "", ""});
    	model.addRow(new String[]{"Priority Non-Preemptive", "", ""});
    	model.addRow(new String[]{"Priority Preemptive", "", ""});
    	model.addRow(new String[]{"Round Robin", "", ""});
    	
    	table = new JTable(model);
        table.setFillsViewportHeight(true);
        tablePane = new JScrollPane(table);
        tablePane.setBounds(0,0,width,height/2 + 20);
        Utility.changeFont(tablePane, 14);
        Utility.setCellsAlignment(table, SwingConstants.CENTER);
        add(tablePane);
    }
    public void setReadyQueue(List<Process> queue) {
    	this.readyQueue = queue;
		Scheduler scheduler;
		for(int i=0;i<schedulerList.size();i++) {
			scheduler = Utility.selectScheduler(schedulerList.get(i));
		 	scheduler.setProcessQueue(readyQueue);
		 	scheduler.process();
		 	model.setValueAt(scheduler.getAverageWaitingTime(), i, 1);
		 	model.setValueAt(scheduler.getAverageTurnAroundTime(), i, 2);
		}
    }
	public void clearTable() {
		model.setRowCount(0);
	}  
}
