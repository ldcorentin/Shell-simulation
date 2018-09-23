import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Display extends JFrame {
  JPanel panel;
  Node[] sensors;
  ArrayList<Link> links;
  ArrayList<Link> corruptLink;
  Gateway gateway;

  public Display(Node[] s, ArrayList<Link> l, ArrayList<Link> l2, Gateway myGateway){
    super("Representation of the sensor network");
    sensors = s;
    links = l;
    corruptLink = l2;
		gateway = myGateway;
		
    setLayout(null);
    panel = new JPanel();
    panel.setBounds(0, 0, 1000, 1000);
    setContentPane(panel);

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(1000, 1000);
    setResizable(false);
    setVisible(true);
	}

	public void paint(Graphics g){
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 1000, 1000);
		// Draw links between nodes
		g.setColor(Color.WHITE);
		for(int j=0; j<links.size(); j++){
			g.drawLine(links.get(j).a.coordX, links.get(j).a.coordY, links.get(j).b.coordX, links.get(j).b.coordY);
		}
		// Draw nodes
		g.setColor(Color.GREEN);
		for(int i=0; i<sensors.length; i++){
			g.drawLine(sensors[i].coordX, sensors[i].coordY, sensors[i].coordX, sensors[i].coordY);
		}
		g.setColor(Color.YELLOW);
		g.drawLine(gateway.coordX, gateway.coordY, gateway.coordX, gateway.coordY);
		// Draw corrupt links 
		g.setColor(Color.RED);
		for(int j=0; j<corruptLink.size(); j++){
			g.drawLine(corruptLink.get(j).a.coordX, corruptLink.get(j).a.coordY, corruptLink.get(j).b.coordX, corruptLink.get(j).b.coordY);
		}
	}
}
