import java.util.*;

public class Link {

	protected Node a;
	protected Node b;
	protected List<Key> sharedKeyLink;
	

	public Link(Node a, Node b) {
		this.a=a;
		this.b=b;
		sharedKeyLink = a.shareKeyNode(b);
	}
	
	public boolean doesLinkExist(Link link) {
		if(this.a==link.a && this.b==link.b)
			return true;
		else if(this.a==link.b && this.b==link.a)
			return true;
		else
			return false;
	}
}
