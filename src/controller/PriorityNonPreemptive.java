package controller;

import java.util.ArrayList;
import java.util.List;
import model.Event;
import model.Process;

public class PriorityNonPreemptive extends Scheduler {
	/*
	 * Implementation of Priority Non-preemptive Scheduler
	 */
    @Override
    public void process() {
    	Utility.sortByArrivalTime(this.getProcessQueue());
        
        List<Process> rows = Utility.deepCopy(this.getProcessQueue());
        int time = rows.get(0).getArrivalTime();
        
        while (!rows.isEmpty()) {
            List<Process> availableRows = new ArrayList<>();
            
            for (Process row : rows) {
                if (row.getArrivalTime() <= time) {
                    availableRows.add(row);
                }
            }
            
            Utility.sortByPriority(availableRows);
            Process row = availableRows.get(0);
            
            this.getTimeline().add(new Event(row.getProcessName(), time, time + row.getBurstTime()));
            time += row.getBurstTime();
            
            for (int i = 0; i < rows.size(); i++)
            {
                if (rows.get(i).getProcessName().equals(row.getProcessName())) {
                    rows.remove(i);
                    break;
                }
            }
        }
        
        setWTandTAT_NonP();
    }
}
