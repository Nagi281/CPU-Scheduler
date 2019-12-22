package controller;

import java.awt.Component;
import java.awt.Container;
import java.awt.FileDialog;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

import model.Process;

public class Utility
{
	private static String key = "ProcessSchedulerFile";
	
    public static List<Process> deepCopy(List<Process> oldList) {
        List<Process> newList = new ArrayList<>();
        for (Process p : oldList) {
            newList.add(new Process(p.getProcessName(), p.getArrivalTime(),
            							p.getBurstTime(), p.getPriorityLevel()));
        }
        return newList;
    }
    
    public static void sortByArrivalTime(List<Process> listProcess) {
    	Collections.sort(listProcess, (Object o1, Object o2) -> {
            if (((Process) o1).getArrivalTime() == ((Process) o2).getArrivalTime()) {
                return 0;
            }
            else if (((Process) o1).getArrivalTime() < ((Process) o2).getArrivalTime()) {
                return -1;
            }
            else {
                return 1;
            }
        });
    }
    
    public static void sortByBurstTime(List<Process> listProcess) {
    	Collections.sort(listProcess, (Object o1, Object o2) -> {
            if (((Process) o1).getBurstTime() == ((Process) o2).getBurstTime()) {
                return 0;
            }
            else if (((Process) o1).getBurstTime() < ((Process) o2).getBurstTime()) {
                return -1;
            }
            else {
                return 1;
            }
        });
    }
    
    public static void sortByPriority(List<Process> listProcess) {
    	Collections.sort(listProcess, (Object o1, Object o2) -> {
            if (((Process) o1).getPriorityLevel() == ((Process) o2).getPriorityLevel()) {
                return 0;
            }
            else if (((Process) o1).getPriorityLevel() < ((Process) o2).getPriorityLevel()) {
                return -1;
            }
            else {
                return 1;
            }
        });
    }
    
    public static void sortByResponseRatio(List<Process> listProcess) {
    	Collections.sort(listProcess, (Object o1, Object o2) -> {
            if (((Process) o1).getResponseRatio()== ((Process) o2).getResponseRatio()) {
                return 0;
            }
            else if (((Process) o1).getResponseRatio() > ((Process) o2).getResponseRatio()) {
                return -1;
            }
            else {
                return 1;
            }
        });
    }
  
    public static List<Process> getProcessQueue(List<Process> input,int queueNum) {
    	List<Process> result = new ArrayList<>();
    	for (int i = 0; i < input.size(); i++) {
			if(input.get(i).getPriorityLevel() == queueNum) {
				Process a = input.get(i);
				result.add(new Process(a.getProcessName(),a.getArrivalTime(),a.getBurstTime(),a.getPriorityLevel()));
			}
		}
    	return result;
    }
    
    public static void setCellsAlignment(JTable table, int alignment)
    {
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(alignment);

        TableModel tableModel = table.getModel();

        for (int columnIndex = 0; columnIndex < tableModel.getColumnCount(); columnIndex++)
        {
            table.getColumnModel().getColumn(columnIndex).setCellRenderer(rightRenderer);
        }
    }
    
    public static void changeFont(Component component, int fontSize) {
	    Font f = component.getFont();
	    component.setFont(new Font(f.getName(),f.getStyle(),fontSize));
	    if (component instanceof Container) {
	        for (Component child : ((Container) component).getComponents()) {
	            changeFont(child, fontSize);
	        }
	    }
	}
    
    public static Scheduler selectScheduler(String selected) {
		switch (selected) {
         case "FCFS":
            return new FirstComeFirstServe();
         case "SJF":
        	 return new ShortestJobFirst_NP();
         case "SRT":
        	 return new ShortestRemainingTime();
         case "HRRN":
        	 return new HighestResponseRatioNext();
         case "PSN":
        	 return new PriorityNonPreemptive();
         case "PSP":
        	 return new PriorityPreemptive();
         case "RR":
        	 String tq;
        	 do{
            	 tq = JOptionPane.showInputDialog("Time Quantum");
             } while(tq.equals(""));
        	 
             Scheduler scheduler = new RoundRobin();
             scheduler.setTimeQuantum(Integer.parseInt(tq)); 
             return scheduler;
         default:
             return null;
		}
	}
    
    public static String loadFileFromComputer() {
    	FileDialog dialog = new FileDialog(new JFrame(),
				"Open an Existing Job List");
		File inputFile = null;
		dialog.setMode(FileDialog.LOAD);
		dialog.setVisible(true);
			
		String filepath = "";
		// start try-catch on inputFile opening
		try {
			if (!dialog.getFile().equals("")) {
				inputFile = new File(dialog.getDirectory() + dialog.getFile());
				filepath = new String(dialog.getDirectory() + dialog.getFile());
				if (isPISFile(inputFile)){
					return filepath;
				}
				else {
					return null;
				}
			}
		} catch (Exception ex) {
			return null;
		}
		return filepath;
    }
    
    private static boolean isPISFile(File inputFile)
			throws FileNotFoundException {
		Scanner fileTestRead = new Scanner(new FileReader(inputFile));

		if (!fileTestRead.next().equals(key)) {
			JOptionPane.showMessageDialog(null,
					"Invalid File! Open a valid Job List.");
			fileTestRead.close();
			return false;
		}
		fileTestRead.close();
		return true;
	}
}
