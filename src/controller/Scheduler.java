package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Event;
import model.Process;

public abstract class Scheduler {
	private List<Process> processQueue;
    private final List<Event> timeline;
    private int timeQuantum;
    
    public Scheduler() {
    	processQueue = new ArrayList<>();
        timeline = new ArrayList<>();
        timeQuantum = 1;
    }
    
    public boolean add(Process row) {
        return processQueue.add(row);
    }
    
    public void setTimeQuantum(int timeQuantum) {
        this.timeQuantum = timeQuantum;
    }
    
    public int getTimeQuantum() {
        return timeQuantum;
    }
    
    public double getAverageWaitingTime() {
        double avg = 0.0;
        for (Process p : processQueue) {
            avg += p.getWaitingTime();
        }
        return avg / processQueue.size();
    }
    
    public double getAverageTurnAroundTime() {
        double avg = 0.0;
        for (Process p : processQueue) {
            avg += p.getTurnaroundTime();
        }
        return avg / processQueue.size();
    }
    
    public Event getEvent(Process row) {
        for (Event event : timeline) {
            if (row.getProcessName().equals(event.getProcessName())) {
                return event;
            }
        }
        return null;
    }
    
    public Process getProcess(String name) {
        for (Process row : processQueue) {
            if (row.getProcessName().equals(name)) {
                return row;
            }
        }
        return null;
    }
    public int getTotalBurstTime() {
		int total=0;
		for(Process p : processQueue) {
			total += p.getBurstTime();
		}
		return total;
	}
    public void setProcessQueue(List<Process> processQueue) {
		this.processQueue = processQueue;
	}
    
    public List<Process> getProcessQueue() {
        return processQueue;
    }
    
    public List<Event> getTimeline() {
        return timeline;
    }
    
    public abstract void process();
    
    public void setWTandTAT_NonP() {
    	for (Process p : this.getProcessQueue()) {
            p.setWaitingTime(this.getEvent(p).getStartTime() - p.getArrivalTime());
            p.setTurnaroundTime(p.getWaitingTime() + p.getBurstTime());
        }
    }
    
    public void setWTandTAT_P() {
    	Map<String,Integer> map = new HashMap<>();
        for (Process p : this.getProcessQueue()) {
            map.clear();
            for (Event event : this.getTimeline()) {
                if (event.getProcessName().equals(p.getProcessName())) {
                    if (map.containsKey(event.getProcessName())) {
                        int w = event.getStartTime() - (int) map.get(event.getProcessName());
                        p.setWaitingTime(p.getWaitingTime() + w);
                    }
                    else {
                        p.setWaitingTime(event.getStartTime() - p.getArrivalTime());
                    }
                    map.put(event.getProcessName(), event.getFinishTime());
                }
            }
            p.setTurnaroundTime(p.getWaitingTime() + p.getBurstTime());
        }
    }
    
    public void printTimeline(){ 
    	for (Event event : timeline)
    	{
    	  System.out.println(event.getStartTime());
    	  System.out.println("|  " + event.getProcessName());
    	}

    	System.out.print(timeline.get(timeline.size() - 1).getFinishTime());
    }
}
