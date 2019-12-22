package controller;

import java.util.List;
import model.Event;
import model.Process;

public class RoundRobin extends Scheduler {
	/*
	 * Implementation of Round Robin Serve Scheduler
	 * Included Time Quantum
	 */
    @Override
    public void process() {
        Utility.sortByArrivalTime(this.getProcessQueue());
        
        List<Process> tempList = Utility.deepCopy(this.getProcessQueue());
        int time = tempList.get(0).getArrivalTime();
        int timeQuantum = this.getTimeQuantum();
        
        while (!tempList.isEmpty()) {
            Process row = tempList.get(0);
            int bt = (row.getBurstTime() < timeQuantum ? row.getBurstTime() : timeQuantum);
            this.getTimeline().add(new Event(row.getProcessName(), time, time + bt));
            time += bt;
            tempList.remove(0);
            
            if (row.getBurstTime() > timeQuantum) {
                row.setBurstTime(row.getBurstTime() - timeQuantum);
                if(tempList.size()==0){
                	tempList.add(row);
                }
                else {
                	for (int i = 0; i < tempList.size(); i++) {
                        if (tempList.get(i).getArrivalTime() > time) {
                            tempList.add(i, row);
                            break;
                        }
                        else if (i == tempList.size() - 1) {
                            tempList.add(row);
                            break;
                        }
                    }
                }
                
            }
        }
        
        setWTandTAT_P();
    }
}
