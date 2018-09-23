import java.util.*;

public class Node {

  protected int coordX, coordY;
  protected int idNode;
	protected double distance_With_Gateway;
  protected boolean marque;
  protected ArrayList<Integer> id_Every_Nodes;
  protected ArrayList<Node> neighbors;
  protected ArrayList<Node> connexion;
  protected ArrayList<Key> sharedKey;
  protected ArrayList<Key> keyBunch;

  public Node(int idNode, int coordX, int coordY, int numberNodes) {
    this.idNode=idNode;
    this.coordX=coordX;
    this.coordY=coordY;
    this.marque=false;
    this.distance_With_Gateway=0;
    id_Every_Nodes = new ArrayList<Integer>();
    for(int i=0; i<numberNodes; i++) {
    	if(i!=idNode) {
    		id_Every_Nodes.add(i); //EBS impose to each node to have the list of every id in the network
    	}
    }

    sharedKey = new ArrayList<Key>();
    keyBunch = new ArrayList<Key>();
    neighbors = new ArrayList<Node>();
    connexion = new ArrayList<Node>();
  }

  public void changeDistance(double d) {
  	this.distance_With_Gateway=d;
  }

  public void addConnexion(Node node){
    this.connexion.add(node);
  }

  public void addNeighbor(Node node){
    this.neighbors.add(node);
  }

  public void changeMarque(){
    this.marque = !this.marque;
  }

  public boolean getMarque() {
    return this.marque;
  }

  public int getCoordX() {
    return this.coordX;
  }

  public int getCoordY() {
    return this.coordY;
  }

  public int getId() {
  	return this.idNode;
  }

  public double distanceB(Node node){
    return Math.sqrt(Math.pow(Math.abs(node.coordX - this.coordX), 2) + Math.pow(Math.abs(node.coordY - this.coordY), 2));
  }

  public double distanceG(Gateway gateway){
    return Math.sqrt(Math.pow(Math.abs(gateway.coordX - this.coordX), 2) + Math.pow(Math.abs(gateway.coordY - this.coordY), 2));
  }

  public void addKey(Key newKey){
    keyBunch.add(newKey);
  }

	public boolean sameKeyBunchN(Node node){
		int counter=0;
	  for(Key resKey : this.keyBunch) {
			if(node.keyBunch.contains(resKey))	 {
				counter++;
			}
		}
		if(counter==this.keyBunch.size()) return true;
		else return false;
	}

  public List<Key> shareKeyNode(Node node){
  	List<Key> resList = new ArrayList<Key>();
    for(int i=0; i < this.keyBunch.size(); i++) {
      if(node.keyBunch.contains(this.keyBunch.get(i))) {
        resList.add(keyBunch.get(i));
      }
    }
    if(resList.size()==0)
    	return null;
    else
    	return resList;
  }

	public boolean hasThisKey(Key key){
    for(int i=0; i < this.keyBunch.size(); i++) {
      if(key.equals(this.keyBunch.get(i))) {
        return true;
      }
    }
    return false;
  }

  public int numberOfNeighbors() {
    return this.neighbors.size();
  }

  public int numberOfConnexions() {
    return this.connexion.size();
  }

  public String toString(){
    String str = "Node : " + this.idNode + " and his keys : ";
    for(int i=0; i<keyBunch.size(); i++)  str = str + "\n - " + keyBunch.get(i) + " shared with " + neighbors.get(i);
    return str;
  }

  public String keysToString() {
  	String str = "";
		for(int i=0; i<keyBunch.size(); i++) str+=keyBunch.get(i)+"\n";
		return str;
	}
	
	public String keysToStringId() {
  	String str = "";
		for(int i=0; i<keyBunch.size(); i++) str+=keyBunch.get(i).getId()+"\n";
		return str;
	}

}
