package view;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MultiQueueOptionPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JComboBox<String> option1;
	private JComboBox<String> option2;
	private JComboBox<String> option3;
	private JTextField timeQt1;
	private JTextField timeQt2;
	private JTextField timeQt3;

	public MultiQueueOptionPanel(int width, int height) {
		super();
		this.setSize(width, height);
		setLayout(null);
		option1 = new JComboBox<String>(new String[] { "FCFS", "RR" });
		option2 = new JComboBox<String>(new String[] { "FCFS", "RR" });
		option3 = new JComboBox<String>(new String[] { "FCFS", "RR" });
		timeQt1 = new JTextField();
		timeQt2 = new JTextField();
		timeQt3 = new JTextField();
		timeQt1.setText("0");
		timeQt2.setText("0");
		timeQt3.setText("0");
		option1.setBounds(0, 0, 80, 25);
		option2.setBounds(85, 0, 80, 25);
		option3.setBounds(170, 0, 80, 25);
		timeQt1.setBounds(0, 30, 80, 25);
		timeQt2.setBounds(85, 30, 80, 25);
		timeQt3.setBounds(170, 30, 80, 25);
		JLabel lb1 = new JLabel("Queue 1");
		JLabel lb2 = new JLabel("Queue 2");
		JLabel lb3 = new JLabel("Queue 3");
		lb1.setBounds(5, 60, 80, 25);
		lb2.setBounds(90, 60, 80, 25);
		lb3.setBounds(175, 60, 80, 25);
		add(option1);
		add(option2);
		add(option3);
		add(timeQt1);
		add(timeQt2);
		add(timeQt3);
		add(lb1);
		add(lb2);
		add(lb3);
	}

	public String[] getOptions() {
		return new String[] { "", option1.getSelectedItem().toString(), option2.getSelectedItem().toString(),
				option3.getSelectedItem().toString() };
	}

	public int[] getQuantumTime() {
		try {
			int[] timeQts = new int[] { 0, Integer.parseInt(timeQt1.getText().toString()),
					Integer.parseInt(timeQt2.getText().toString()), Integer.parseInt(timeQt3.getText().toString()) };
			return timeQts;
		} catch (Exception e) {
			return null;
		}
	}
}
