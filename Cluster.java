import jmetal.util.*;
import java.util.*;
import java.util.List;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.security.*;

public class Cluster	 {

  public static void main(String[] args){

		if(args.length != 3){
			System.out.println("Please call the program like this : java EBS_Distribution <k> <m>");
			System.exit(0);
		}

    Scanner scanner = new Scanner(System.in);

    //scheme parameters
    int number_Of_Nodes;
    int number_Of_Gateway;
    double max_Range_Node=50; //meters
    double max_Range_Gateway=200; //meters
    int xLength=145, yWidth=145; //meters
    int degre=4; //degre de chaque nœud
    int K; //number of keys for a cluster
    int k; //k is the number of keys by node
    int m; //
    int N; //Number of node by cluster, variable between clusters
    int Nmax; //Number maximum of unique subassembly of k keys
    int number_Of_Key_By_EBS;
    int key_Size=128; //size of each key
    int number_Nodes_Hacked;
    int number_Of_Compromise_Link;
    double coeffSizeTabKeys=0;
    double secureNumberCombination;

    SecureRandom random = new SecureRandom();
    MersenneTwisterFast mt = new MersenneTwisterFast();

    List<Boolean> bytes = new ArrayList<>();
    List<Node> nodeAlreadyAssign = new ArrayList<Node>();
    List<String> uniqueCombination = new ArrayList<String>();
    ArrayList<Link> arrayLinkNode = new ArrayList<Link>();
    ArrayList<Link> resArrayLinkNode = new ArrayList<Link>();
    ArrayList<Link> arrayLinkCompromise = new ArrayList<Link>();
    
    int[][] possibilities;

    try
    {

      //System.out.println("---------------------------\nPARAMETERS INITIALIZATION\n---------------------------\n");
      //Here is the choice for k and m
      number_Of_Gateway=1;
      k=Integer.parseInt(args[0]);
      m=Integer.parseInt(args[1]);
      number_Nodes_Hacked=Integer.parseInt(args[2]);
      
      number_Of_Key_By_EBS = k+m;
      K = k+m;

      //Calcul of the number total of node for the test
      number_Of_Nodes = (int)(degre*xLength*yWidth/(Math.PI*max_Range_Node*max_Range_Node));
      //System.out.println("number_Of_Nodes : "+number_Of_Nodes);

      //Condition to have an EBS system
			Nmax=(int)(binomial((long)(k+m), (long)(k)));
			N=(int)(binomial((long)(k), (long)(m)));
			
			//System.out.println("The maximum of nodes for this paramaters (k:"+k+",m:"+m+") is : "+Nmax);

      //calcul number keys
			coeffSizeTabKeys=3;

			//System.out.println("We need a max of 2^"+coeffSizeTabKeys+" keys\n");

			if(Nmax<number_Of_Nodes) {
				System.out.println("The condition with k and m are not good to have an EBS system\n");
				System.exit(0);
			}

			//Gateways' creation
      //System.out.println("---------------------------\nGateway creation\n---------------------------\n");
      Gateway myGateway = new Gateway(1, (xLength/2)+mt.nextInt(xLength+1)%100, (yWidth/2)+mt.nextInt(yWidth+1)%100); //around the center
      //System.out.println("Check point : Ok");


      //Nodes' creation
      //System.out.println("\n---------------------------\nNodes' creation\n---------------------------\n");
      Node[] sensors = new Node[number_Of_Nodes];
      double distance_With_Gateway;
      for(int i=0; i<number_Of_Nodes; i++) {
        sensors[i] = new Node(i, Math.abs(mt.nextInt(xLength+1)%145), Math.abs(mt.nextInt(yWidth+1)%145), number_Of_Nodes);
        distance_With_Gateway = sensors[i].distanceG(myGateway);
        sensors[i].changeDistance(distance_With_Gateway);
      }
      //System.out.println("Check point : Ok\n");


      //Nodes' discovery with myGgateway
      //System.out.println("---------------------------\nNODES' DISCOVERY WITH GATEWAY\n---------------------------");
      //There is only one cluster
      for(int i=0; i<number_Of_Nodes; i++) {
      	myGateway.addNodeCluster(sensors[i]);
      }

      //Keys' generation matrix
      //System.out.println("---------------------------\nKEYS'GENERATION FOR MYGATEWAY\n---------------------------\n");

			//calculate the number of permutations
			double numPermutations = Math.pow(2,coeffSizeTabKeys);
      //System.out.println((int)(numPermutations)+"\n");

			//Bunch of keys
			Key resBunchKey[] = new Key[(int)(numPermutations)];
      Key bunchKey[] = new Key[k+m];
			for(int i=0; i<resBunchKey.length; i++) {
				resBunchKey[i]= new Key((int)(coeffSizeTabKeys),i,true);
			}
      for(int i=0; i<bunchKey.length; i++) {                                    //Number exact of keys needed
				bunchKey[i]= new Key((int)(coeffSizeTabKeys),i,true);
			}

			//Generation of all binary key depend of k and m (K=k+m)
			for(int i=0; i<coeffSizeTabKeys; i++) {
				bytes.add(false);
			}

			// loop through all permutations
			for(int i=0; i<numPermutations; i++) {
				//increment the 2 bytes
				increment(bytes);
				//create the current permutation
				creation(i,bytes,resBunchKey);
			}

			//System.out.println("---------------------------Verification Test for the unique binary matrix\n---------------------------\n");
			int teub=0;
			for(int i=0; i<numPermutations; i++) {
				teub++;
			}
			//if(numPermutations==teub)	System.out.println("\nThere are "+teub+" different combinations and it's coherent : 2^"+coeffSizeTabKeys+"="+teub+"\n");

      //keys needed
      for(int i=0; i<bunchKey.length; i++) {
				bunchKey[i]=resBunchKey[i];
        //System.out.println(bunchKey[i].toString());
			}


			//Assignation of all keys for all clusters
			//System.out.println("---------------------------\nKEYS' ASSIGNATION FOR MYGATEWAY\n---------------------------\n");
			int counter=0;
			for(int i=0; i<bunchKey.length; i++) {
				myGateway.addKeyToKeybunch(bunchKey[i]);
			}
			//System.out.println("Check point : Ok and number of keys : "+bunchKey.length+"\n");



			//Assignation of all keys for all nodes			
			//System.out.println("---------------------------\nKEYS' ASSIGNATION FOR ONE CLUSTER\n---------------------------");			
			//initialization paramaters
			Key resKey;
			int counterNode=0, pointer, numKey;
			String combinationString;
			String[] cchar = new String[k];
			List<Integer> rresCombinationInt = new ArrayList<Integer>();
			char[] bufferC = new char[2*k+1];
			String[] realBuffer = new String[k];
			
			
			//differents unique combination
			String str="";
			/*for(int i=0; i<k+m; i++)	{
				str+=Integer.toString(i)+",";
			}*/
			//System.out.println(str+"\n");			
			String str0="0,1,2,";
			String str1="0,1,3,";
			String str2="0,1,4,";
			String str3="0,2,3,";
			String str4="0,2,4,";
			String str5="0,3,4,";
			String str6="1,2,3,";
			String str7="1,2,4,";
			String str8="1,3,4,";	
			String str9="2,3,4,";
			
			uniqueCombination.add(str0);
			uniqueCombination.add(str1);
			uniqueCombination.add(str2);
			uniqueCombination.add(str3);
			uniqueCombination.add(str4);
			uniqueCombination.add(str5);
			uniqueCombination.add(str6);
			uniqueCombination.add(str7);
			uniqueCombination.add(str8);
			uniqueCombination.add(str9);
			
			counter=0;
			int realCounter=0;
			for(int p=0; p<uniqueCombination.size(); p++)	{
				for(int i=0; i<uniqueCombination.size(); i++)	{
					if(i!=p)	{
						for(int j=0; j<uniqueCombination.get(p).length(); j++)	{
							for(int l=0; l<uniqueCombination.get(i).length(); l++)	{
								if(uniqueCombination.get(p).charAt(j)==uniqueCombination.get(i).charAt(l))	{
									counter++;
								}
							}
						}
					}
					if(counter==uniqueCombination.get(0).length())	{
						realCounter++;
					}
					counter=0;
				}
			}
			
			//if(realCounter==0) System.out.println("There is "+realCounter/2+" same combination\n");
			//else System.out.println("There is a BIG PROBLEM with the unique combination\n");
			
			//test if the combinations are good for 10
			/*System.out.println("SIZE : "+uniqueCombination.size()+"\n");
			for(int i=0; i<10; i++) {
				System.out.println(uniqueCombination.get(i));
			}*/
			//key assignation
			//initialisation du tableau pointeur des combinaisons
			for(int l=0; l<number_Of_Nodes; l++) {
				rresCombinationInt.add(l);
			}
			
			//assignation for each node
			pointer=0;
			for(Node resNode : myGateway.cluster_Node) {
				
				//on rend inutilisable les combinaisons déjà utilisées
				//rresCombinationInt.remove(pointer);
				
				//recupère le numéro de combinaison souhaité
				combinationString = uniqueCombination.get(pointer);  
				
				//get the combination wanted
				str="";
				counter=0;
				bufferC = uniqueCombination.get(pointer).toCharArray();
				for(int i=0; i<bufferC.length; i++) {
					if(bufferC[i]!=',')	{
						str+=bufferC[i];
					}
					else {
						realBuffer[counter]=str;
						counter++;
						str="";
					}
				}	
				
				for(int j=0; j<k; j++) {
					//Séparer le string en multiple string pour les parser en int
					if(resNode.keyBunch.size()<k) {
						numKey= Integer.parseInt(realBuffer[j]);
						resKey = myGateway.keyBunch.get(numKey);
						resNode.addKey(resKey);
					}
				}
				pointer++;
			}


			//test verification
			/*System.out.println("\n---------------------------\nVerification Test for the keys on 10 sensors\n---------------------------\n");
			for(int i=0; i<10; i++) {
				//display node's id
				str="";
				StringBuilder sb = new StringBuilder();
				sb.append("");
				sb.append(sensors[i].getId());
				str = sb.toString();
				System.out.println(str);
				//display node's keys
				System.out.println(sensors[i].keysToStringId());
				System.out.println();
			}


			//Display nodes with too much keys
			System.out.println("Display nodes with too much keys : \n");

			int wrong_Number_Of_Keys=0;
			for(int i=0; i<number_Of_Nodes; i++) {
				if(sensors[i].keyBunch.size()>k) {
					wrong_Number_Of_Keys++;
					System.out.println(sensors[i].getId());
					System.out.println(sensors[i].keysToString());
					System.out.println();
				}
			}

			System.out.println("There are : "+wrong_Number_Of_Keys+" nodes with too much keys\n");*/
			
			//add neighbor to each node		
			double distanceNN;
			for(int i=0; i<sensors.length; i++) {
				for(int j=0; j<sensors.length; j++) {
					distanceNN=0;
					if(i!=j) {
						distanceNN=sensors[i].distanceB(sensors[j]);
						if(distanceNN<max_Range_Node) {
							sensors[i].addNeighbor(sensors[j]);
						}
					}
				}
			}
			
			//add connexion to each node
			counter=0;
			Link link;
			boolean bool=false;
			for(int i=0; i<sensors.length; i++) {
				for(Node node : sensors[i].neighbors) {
					if(sensors[i].shareKeyNode(node)!=null) {
						sensors[i].addConnexion(node);
						link = new Link(sensors[i],node);
						arrayLinkNode.add(link);
						resArrayLinkNode.add(link);	
					}
					counter++;
				}
			}
			
			//display nodes without connexion
			counter=0;
			List<Node> orphanNode = new ArrayList<Node>();
			for(int i=0; i<sensors.length; i++) {
				if(sensors[i].connexion.size()==0) {
					counter++;
					orphanNode.add(sensors[i]);
				}
			}
			//System.out.println("There is/are : "+counter+" node(s) without any connexion with another node");
			
			counter=0;
			for(Node node : orphanNode) {
				if(node.neighbors.size()==0) {
					counter++;
				}
			}
			//System.out.println("But there is/are : "+counter+" node(s) without any neighbors with another node\n");
			
			for(Node node : orphanNode) {
				if(myGateway.cluster_Node.contains(node)) {
					myGateway.cluster_Node.remove(node);
				}
			}
			
			
			
			
			//Graph calcul
    	//System.out.println("---------------------------\n GRAPHS \n---------------------------\n");
    	double result=0;
	    int numberTotalOfConnexion=0;
	    int numberTotalOfNeighbors=0;
	    for(int t=0; t<number_Of_Nodes; t++) {
        numberTotalOfConnexion += sensors[t].numberOfConnexions();
        numberTotalOfNeighbors += sensors[t].numberOfNeighbors();
	    }
	    //System.out.println(numberTotalOfConnexion);
	    //System.out.println(numberTotalOfNeighbors+"\n");
	    double ratio = (double)(numberTotalOfConnexion)/(double)(numberTotalOfNeighbors);
	    //System.out.println("I have a result of : " + ratio+"\n");
	    
	    
	    
	    
	    //Display topography
    	//System.out.println("---------------------------\n DISPLAY NODES AND CONNECTIONS \n---------------------------\n");
			//Display displayConnexionNode1 = new Display(sensors, arrayLinkNode, null, myGateway);			
			
			
			
			//Resilience
    	//System.out.println("---------------------------\n RISILIENCE CALCUL \n---------------------------\n");
    	double resilience;
    	
    	//System.out.println("There is : "+arrayLinkNode.size()+" connexions\n");
    	
			Hacker hacker = new Hacker(number_Nodes_Hacked, sensors);
			/*for(int i=0; i<number_Nodes_Hacked; i++) {
				System.out.println("Node id : "+hacker.sensorsHacked.get(i).getId());
			}*/
			hacker.numberOfLinksCompromise(resArrayLinkNode, arrayLinkCompromise, arrayLinkNode);
			//System.out.println("I have a result of : " + number_Of_Compromise_Link+" for the number_Of_Compromise_Link\n");
			resilience = (double)(arrayLinkCompromise.size())/(double)(numberTotalOfConnexion);
			System.out.println(resilience);
			
			//Display displayConnexionNode2 = new Display(sensors, resArrayLinkNode, arrayLinkCompromise, myGateway);
			

			
		//end try
		}

    /* Exceptions Java */
		catch (NumberFormatException e1)
		{
			System.out.println("NumberFormatException: " + e1.getMessage());
		}
		catch (ArrayIndexOutOfBoundsException e2)
		{
			System.out.println("ArrayIndexOutOfBoundsException: " + e2.getMessage());
		}
		/* Exceptions personnalisées */
		catch (BinomialException1 | BinomialException2 | BinomialException3 e3){}
  }



  //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  //------------------------------------------------------------------------------FUNCTIONS---------------------------------------------------------------------------------------------------------------------
  //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------




	public static void shortSort1(double[] tableau, Node[] tabNode) {
		int i, j;
		double cle;
		Node resNode;

		for (i=1; i<tableau.length; i++) {
			cle = tableau[i];
			resNode = tabNode[i];
			j = i;
			while ((j>=1) && (tableau[j-1]>cle)) {
				tableau[j]=tableau[j-1];
				tabNode[j]=tabNode[j-1];
				j=j-1;
			}
			tableau[j]=cle;
			tabNode[j]=resNode;
		}
	}

	public static void shortSort2(double[] tableau, Gateway[] tabGateway) {
		int i, j;
		double cle;
		Gateway resGateway;

		for (i=1; i<tableau.length; i++) {
			cle = tableau[i];
			resGateway = tabGateway[i];
			j = i;
			while ((j>=1) && (tableau[j-1]>cle)) {
				tableau[j]=tableau[j-1];
				tabGateway[j]=tabGateway[j-1];
				j=j-1;
			}
			tableau[j]=cle;
			tabGateway[j]=resGateway;
		}
	}


  public static long binomial(long n, long p) throws BinomialException1, BinomialException2, BinomialException3
	{
		/* Si p>n ---> 0, cas particuliers */
		if((p < 0) || (n < 0))
		{
			throw new BinomialException1();
		}

		else if(p > n)
		{
			throw new BinomialException2();
		}
		else if((p == 0) || (p == n))
		{
			return 1 ;
		}
		else
			/* mise en place de la récursivité */
			return binomial (n-1, p-1) + binomial (n-1, p) ;
	}


	public static void creation(int i, List<Boolean> bytes, Key[] bunchKey) {
		// loop through the bytes
		int counter=0;
		for(Boolean bool : bytes) {
			// print 1 or 0
			if(bool) {
				bunchKey[i].changeValue(counter,'1');
			} else {
				bunchKey[i].changeValue(counter,'0');
			}
			counter++;
		}
	}

  public static void increment(List<Boolean> bytes) {
		// set increment position to the end of the list
		int position = bytes.size() - 1;

		// loop through changing next digit if necessary, stopping
		// if the front of the list is reached.
		do {
			bytes.set(position, !bytes.get(position));
		}
		while(!bytes.get(position--) && position >= 0);
	}
	
}

/* Exceptions Personnalisées */
class BinomialException1 extends Exception{
  public BinomialException1(){
   System.out.println("Attention les arguments passés ne sont pas positifs");
  }
}

class BinomialException2 extends Exception{
  public BinomialException2(){
   System.out.println("Attention p est supérieur à n");
  }
}

class BinomialException3 extends Exception{
  public BinomialException3(){
   System.out.println("The result is bigger than an int");
  }
}

//si entre deux noeuds je partage k1 et k2 j'ai besoin de k1 et k2 pour compromettre le lien
