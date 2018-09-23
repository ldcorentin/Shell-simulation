import java.util.*;

public class Gateway {

  protected int coordX, coordY;
  protected int idGateway;
  protected ArrayList<Node> cluster_Node;
  protected ArrayList<Node> neighbors;
  protected ArrayList<Key> keyBunch;

  public Gateway(int idGateway, int coordX, int coordY) {
    this.idGateway=idGateway;
    this.coordX=coordX;
    this.coordY=coordY;
    keyBunch = new ArrayList<Key>();
    cluster_Node = new ArrayList<Node>();
    neighbors = new ArrayList<Node>();
  }

  public int getCoordX() {
    return this.coordX;
  }

  public int getCoordY() {
    return this.coordY;
  }
  
  public int getIdGateway() {
    return this.idGateway;
  }

  public double distanceN(Node node) {
    return Math.sqrt(Math.pow(Math.abs(node.coordX - this.coordX), 2) + Math.pow(Math.abs(node.coordY - this.coordY), 2));
  }

  public double distanceG(Gateway gateway) {
    return Math.sqrt(Math.pow(Math.abs(gateway.coordX - this.coordX), 2) + Math.pow(Math.abs(gateway.coordY - this.coordY), 2));
  }

	public void addKeyToKeybunch(Key key) {
		this.keyBunch.add(key);
	}
	
	public void addNeighbors(Node node) {
		this.neighbors.add(node);
	}

  public void addNodeCluster(Node node) {
    this.cluster_Node.add(node);
  }

  public Key shareKeyGateway(Gateway gateway){
    for(int i=0; i < this.keyBunch.size(); i++) {
      if(gateway.keyBunch.contains(this.keyBunch.get(i))) {
        return this.keyBunch.get(i);
      }
    }
    return null;
  }

	public boolean hasThisKey(Key key){
    for(int i=0; i < this.keyBunch.size(); i++) {
      if(key.equals(this.keyBunch.get(i))) {
        return true;
      }
    }
    return false;
  }
  
  public String toStringKeys() {
  	String str="";
  	for(Key resKey : keyBunch) {
  		str+=resKey.toString()+"\n";
  	}
  	return str;
  }
  
}
