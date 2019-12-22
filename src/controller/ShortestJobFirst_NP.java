package controller;

import java.util.ArrayList;
import java.util.List;

import model.Event;
import model.Process;

public class ShortestJobFirst_NP extends Scheduler {
	/*
	 * Implementation of Shortest Job First Scheduler
	 * Version: Non-Preemptive 
	 */
	@Override
	public void process() {
        Utility.sortByArrivalTime(this.getProcessQueue());
        List<Process> listProcess = Utility.deepCopy(this.getProcessQueue());
        int time = listProcess.get(0).getArrivalTime();
        while (!listProcess.isEmpty()) {
            List<Process> availableRows = new ArrayList<>();
            for (Process row : listProcess) {
                if (row.getArrivalTime() <= time) {
                    availableRows.add(row);
                }
            }
            Utility.sortByBurstTime(availableRows);
            Process row = availableRows.get(0);
            this.getTimeline().add(new Event(row.getProcessName(), time, time + row.getBurstTime()));
            time += row.getBurstTime();
            
            for (int i = 0; i < listProcess.size(); i++) {
                if (listProcess.get(i).getProcessName().equals(row.getProcessName())) {
                	listProcess.remove(i);
                    break;
                }
            }
        }
        setWTandTAT_NonP();
    }
}
