package controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Event;
import model.Process;

public class MultiLevelQueueScheduling extends Scheduler {
	private List<Process> queue1;
	private List<Process> queue2;
	private List<Process> queue3;
	private String[] schedulerNames = new String[4];
	private int[] quantumTimes = new int[4];
	private int time;
	private int total;
	Map<Integer, List<Process>> map;

	public void setSchedulerNames(String[] name) {
		for (int i = 0; i < 4; i++) {
			schedulerNames[i] = name[i];
		}
	}

	public void setQuantumTimes(int[] time) {
		for (int i = 0; i < 4; i++) {
			if (schedulerNames[i].equals("RR")) {
				if (time[i] > 0) {
					quantumTimes[i] = time[i];
				} else {
					throw new NumberFormatException("Round Robin Scheudleing must have quantum time");
				}
			}
		}
	}

	@Override
	public void process() {
		total = this.getTotalBurstTime();
		Utility.sortByArrivalTime(this.getProcessQueue());
		queue1 = Utility.getProcessQueue(this.getProcessQueue(), 1);
		queue2 = Utility.getProcessQueue(this.getProcessQueue(), 2);
		queue3 = Utility.getProcessQueue(this.getProcessQueue(), 3);

		map = new HashMap<>();
		map.put(1, queue1);
		map.put(2, queue2);
		map.put(3, queue3);

		time = this.getProcessQueue().get(0).getArrivalTime();
		while (total != 0) {
			seekForNextEvent(map.get(1), 1);
			seekForNextEvent(map.get(2), 2);
			seekForNextEvent(map.get(3), 3);
		}
		setWTandTAT_P();
	}

	public void seekForNextEvent(List<Process> queue, int num) {
		while (queue.size() != 0) {
			if (schedulerNames[num].equals("FCFS")) {
				Process p = queue.get(0);
				if (time >= p.getArrivalTime()) {
					if (num == 1 || map.get(num - 1).size() == 0) {
						FCFS(p, p.getBurstTime());
						queue.remove(0);
					} else {
						Process p1 = map.get(num - 1).get(0);
						int bt = 0;
						if (time + p.getBurstTime() > p1.getArrivalTime()) {
							bt = p1.getArrivalTime() - time;
							FCFS(p, bt);
							p.setBurstTime(p.getBurstTime() - bt);
							queue.remove(0);
							queue.add(p);
							seekForNextEvent(map.get(num - 1), num - 1);
						}
					}
				} else {
					break;
				}
			} else if (schedulerNames[num].equals("RR")) {
				Process p = queue.get(0);
				if (time >= p.getArrivalTime()) {
					int bt;
					if (num == 1 || map.get(num - 1).size() == 0) {
						bt = (p.getBurstTime() < quantumTimes[num] ? p.getBurstTime() : quantumTimes[num]);
						RR(queue, p, bt, quantumTimes[num]);
					} else {
						Process p1 = map.get(num - 1).get(0);
						int temp = (p.getBurstTime() < quantumTimes[num] ? p.getBurstTime() : quantumTimes[num]);
						if (time + temp > p1.getArrivalTime()) {
							bt = p1.getArrivalTime() - time;
							RR(queue, p, bt, bt);
							seekForNextEvent(map.get(num - 1), num - 1);
						} else {
							bt = temp;
							RR(queue, p, bt, quantumTimes[num]);
						}
					}
				} else {
					break;
				}
			}
		}
	}

	private void FCFS(Process p, int burst) {
		if (this.getTimeline().isEmpty()) {
			this.getTimeline().add(new Event(p.getProcessName(), p.getArrivalTime(), p.getArrivalTime() + burst));
		} else {
			Event event = this.getTimeline().get(this.getTimeline().size() - 1);
			this.getTimeline().add(new Event(p.getProcessName(), event.getFinishTime(), event.getFinishTime() + burst));
		}
		time += burst;
		total -= burst;
	}

	private void RR(List<Process> queue, Process p, int burst, int bursted) {
		this.getTimeline().add(new Event(p.getProcessName(), time, time + burst));
		time += burst;
		total -= burst;
		queue.remove(0);

		if (p.getBurstTime() > bursted) {
			p.setBurstTime(p.getBurstTime() - bursted);
			if (queue.size() == 0) {
				queue.add(p);
			} else {
				for (int i = 0; i < queue.size(); i++) {
					if (queue.get(i).getArrivalTime() > time) {
						queue.add(i, p);
						break;
					} else if (i == queue.size() - 1) {
						queue.add(p);
						break;
					}
				}
			}
		}
	}
}
