package view;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import model.Event;

public class OneSchedulerPanel extends JPanel {
	/**
	 * Panel to display one Scheduler Result
	 */
	private static final long serialVersionUID = 1L;
	private JScrollPane ganttChartPane;
	private CustomPanel ganttChartPanel;
    private JLabel lb_wait;
    private JLabel lb_waitResult;
    private JLabel lb_tat;
    private JLabel lb_tatResult;
    private JComboBox<String> option;
    
    public OneSchedulerPanel(int width, int height) {
    	setSize(width, height);
    	ganttChartPanel = new CustomPanel();
    	ganttChartPanel.setBackground(Color.WHITE);
    	ganttChartPane = new JScrollPane(ganttChartPanel);
    	lb_wait = new JLabel("<html>Average Waiting Time:</html>");
        lb_tat = new JLabel("<html>Average Turn Around Time:</html> ");
        lb_waitResult = new JLabel();
        lb_tatResult = new JLabel();
        String[] a = new String[] {"FCFS", "SJF", "SRT","HRRN", "PSN", "PSP", "RR"};
        option = new JComboBox<String>(a);
        setLayout(null);
        ganttChartPane.setBounds(0, 10, width, height/2);
        lb_wait.setBounds(0,height/2 + 15, width/3, 50);
        lb_tat.setBounds(0, height/2 + 70, width/3, 25);
        lb_waitResult.setBounds(width/3 + 10, height/2 + 15, width/3, 50);
        lb_tatResult.setBounds(width/3 + 10, height/2 + 70, width/3, 25);
        option.setBounds(width-95, height/2 + 15, 95, 25);
        add(ganttChartPane);
        add(lb_wait);
        add(lb_tat);
        add(lb_waitResult);
        add(lb_tatResult);
        add(option);
    }
    
    @Override
    protected void paintComponent(Graphics g)
    {
    	int width = this.getWidth();
    	int height = this.getHeight();
        super.paintComponent(g);
        ganttChartPane.setBounds(0, 10, width, height/2);
        lb_wait.setBounds(0,height/2 + 15, width/3, 25);
        lb_tat.setBounds(0, height/2 + 45, width/3, 25);
        lb_waitResult.setBounds(width/3 + 10, height/2 + 15, width/3, 25);
        lb_tatResult.setBounds(width/3 + 10, height/2 + 45, width/3, 25);
        option.setBounds(width-95, height/2 + 15, 95, 20);
    }
    public String getOption() {
    	return option.getSelectedItem().toString();
    }
    
    public void setTimeLine(List<Event> timeline) {
    	this.ganttChartPanel.setTimeline(timeline);
    }
    
    public void setAverageValue(double wait,double turn) {
    	lb_waitResult.setText(wait + "");
    	lb_tatResult.setText(turn + "");
    }

	public void setOptionState(boolean b) {
		option.setVisible(b);
	}

	public void clearPanel() {
		ganttChartPanel.setTimeline(null);
		lb_waitResult.setText("");
		lb_tatResult.setText("");
	}
}
