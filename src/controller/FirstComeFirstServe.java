package controller;

import java.util.List;
import model.Event;
import model.Process;

public class FirstComeFirstServe extends Scheduler {
	/*
	 * Implementation of First Come First Serve Scheduler
	 */
	@Override
	public void process() {
		Utility.sortByArrivalTime(this.getProcessQueue());
		List<Event> timeline = this.getTimeline();
		for (Process p : this.getProcessQueue()) {
			if (timeline.isEmpty()) {
				timeline.add(new Event(p.getProcessName(), p.getArrivalTime(), p.getArrivalTime() + p.getBurstTime()));
			} else {
				Event event = timeline.get(timeline.size() - 1);
				timeline.add(new Event(p.getProcessName(), event.getFinishTime(), event.getFinishTime() + p.getBurstTime()));
			}
		}

		setWTandTAT_NonP();
	}
}
