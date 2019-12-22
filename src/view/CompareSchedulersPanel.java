package view;

import java.util.List;
import javax.swing.JPanel;
import controller.Scheduler;
import controller.Utility;
import model.Process;

public class CompareSchedulersPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private OneSchedulerPanel schedulerPanel1;
	private OneSchedulerPanel schedulerPanel2;
	private Scheduler scheduler1;
	private Scheduler scheduler2;
	
    public CompareSchedulersPanel(int width, int height) {
    	setSize(width, height);
    	setLayout(null);
    	schedulerPanel1 = new OneSchedulerPanel((width - 20)/2, height);
    	schedulerPanel1.setLocation(0, 0);
    	schedulerPanel2 = new OneSchedulerPanel((width - 20)/2, height);
    	schedulerPanel2.setLocation(width/2 + 10, 0);
    	add(schedulerPanel1);
    	add(schedulerPanel2);
    }
    
    public void setSchedulers(List<Process> list,String s1, String s2) {
    	scheduler1 = Utility.selectScheduler(s1);
        scheduler1.setProcessQueue(list);
        scheduler1.process();
        schedulerPanel1.setAverageValue(scheduler1.getAverageWaitingTime(),
        									scheduler1.getAverageTurnAroundTime());
        schedulerPanel1.setTimeLine(scheduler1.getTimeline());
        
        scheduler2 = Utility.selectScheduler(s2);
        scheduler2.setProcessQueue(list);
        scheduler2.process();
        schedulerPanel2.setAverageValue(scheduler2.getAverageWaitingTime(),
        									scheduler1.getAverageTurnAroundTime());
        schedulerPanel2.setTimeLine(scheduler2.getTimeline());
    }
    
    public String getOption1() {
    	return schedulerPanel1.getOption();
    }
    
    public String getOption2() {
    	return schedulerPanel2.getOption();
    }
    
    public Scheduler getFirstScheduler(){
    	return scheduler1;
    }

	public void clearPanel() {
		schedulerPanel1.clearPanel();
		schedulerPanel2.clearPanel();
	}
}
