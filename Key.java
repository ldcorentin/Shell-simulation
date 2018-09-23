import java.util.*;

public class Key {

  protected char[] value;
  protected int id;
  protected boolean bool=true;

  public Key(int size, int id) {
    value = new char[size];
    this.id=id;
    randomKey();
  }
  
  public Key(int size, int id, boolean bool) {
  	value = new char[size];
    this.id=id;
    this.bool=bool;
   }

  //create a random key
  public void randomKey(){
    Random aleaNumber = new Random();
    int number;
    for (int i=0; i<value.length; i++){
      number = (int)aleaNumber.nextInt(94) + 33;
      //convert int into ascii caractere
      this.value[i]=(char)(number);
    }
  }  

	public void changeValue(int i, char c) {
		this.value[i]=c;
	}

  // Display the key
  public String toString(){
    String str = "";
    for(int i = 0; i<value.length; i++) str = str + value[i];
    return str;
	}
	
	public int getId() {
		return id;
	}

}
