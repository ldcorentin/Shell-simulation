import java.util.*;

public class Hacker {

	protected List<Node> sensorsHacked;
	
	public Hacker(int number_Nodes_Hacked, Node[] sensors) {
		sensorsHacked = new ArrayList<Node>(number_Nodes_Hacked);
		fill_SensorsHacked(sensors,number_Nodes_Hacked);
	}
	
	public void fill_SensorsHacked(Node[] sensors, int number_Nodes_Hacked) {
		int pointer;
		int counter=0;
		while(counter<number_Nodes_Hacked) {
			pointer = (int)(Math.random()*(sensors.length));
			if(!sensorsHacked.contains(sensors[pointer])) {
				sensorsHacked.add(sensors[pointer]);
				counter++;
			}
		}
	}
	
	public void numberOfLinksCompromise(ArrayList<Link> resArrayLinkNode, ArrayList<Link> arrayLinkCompromise, ArrayList<Link> arrayLinkNode) {
		int counter;
		int number_Links_Compromise=0;
		List<Key> knowedKeys = new ArrayList<Key>();
		
		//fill the list of knowed keys for all the captured nodes
		for(Node node : sensorsHacked) {
			for(Key key : node.keyBunch) {
				if(!knowedKeys.contains(key)) {
					knowedKeys.add(key);
				}
			}
		}

		//check each link if it's compromise or not
		for(Link link : arrayLinkNode) {
			counter=0;
			for(Key key : knowedKeys) {
				if(link.sharedKeyLink.contains(key)) {
					counter++;
				}
			}
			if(counter==link.sharedKeyLink.size())	{
				number_Links_Compromise++;
				arrayLinkCompromise.add(link);
				resArrayLinkNode.remove(link);
			}
		}
	}
}
			
		
