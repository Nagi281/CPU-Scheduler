package view;

import java.awt.Font;
import java.awt.Graphics;
import java.util.List;
import javax.swing.JPanel;
import model.Event;

class CustomPanel extends JPanel {	
	/**
	 * Panel to draw Gantt Chart
	 */
	private static final long serialVersionUID = 1L;
	private List<Event> timeline;
	
	public CustomPanel() { }   
	
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        if (timeline != null)
        {
            int pos = 1;
            int width = (int) this.getWidth() / (getTotalTime()+2);
            for (int i = 0; i < timeline.size(); i++)
            {
                int x = width * pos;
                int y = 20;
                Event event = timeline.get(i);
                int duration = event.getFinishTime()- event.getStartTime();
                pos += duration;
                g.drawRect(x, y, width * duration , 30);
                g.setFont(new Font("Segoe UI", Font.BOLD, 11));
                float xP = x +  ((float) duration) * width* ((float) 1/4) ;
                g.drawString(event.getProcessName(), (int) xP, y + 20);
                g.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                g.drawString(Integer.toString(event.getStartTime()), x, y + 45);
                x = width * pos;
                if (i == timeline.size() - 1) {
                	g.drawString(Integer.toString(event.getFinishTime()), x, y + 45);
                }
            }
        }
    }
    
    public void setTimeline(List<Event> timeline) {
        this.timeline = timeline;
        repaint();
    }
    
    public int getTotalTime() {
    	int lastIndex = timeline.size()-1;
    	return timeline.get(lastIndex).getFinishTime() - timeline.get(0).getStartTime();
    }
}
