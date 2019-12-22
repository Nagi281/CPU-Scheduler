package controller;

import java.util.ArrayList;
import java.util.List;
import model.Process;
import model.Event;
public class ShortestRemainingTime extends Scheduler {	
	/*
	 * Implementation of Shortest Remaining Time First 
	 * Preemptive Version of Shortest Job First
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
            
            Utility.sortByBurstTime(availableRows);
            
            Process row = availableRows.get(0);
            this.getTimeline().add(new Event(row.getProcessName(), time, ++time));
            row.setBurstTime(row.getBurstTime() - 1);
            
            if (row.getBurstTime() == 0) {
                for (int i = 0; i < rows.size(); i++) {
                    if (rows.get(i).getProcessName().equals(row.getProcessName())) {
                        rows.remove(i);
                        break;
                    }
                }
            }
        }
        
        for (int i = this.getTimeline().size() - 1; i > 0; i--) {
            List<Event> timeline = this.getTimeline();
            if (timeline.get(i - 1).getProcessName().equals(timeline.get(i).getProcessName())) {
                timeline.get(i - 1).setFinishTime(timeline.get(i).getFinishTime());
                timeline.remove(i);
            }
        }
        
        setWTandTAT_P();
    }
}
