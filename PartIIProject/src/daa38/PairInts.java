package daa38;

public class PairInts {
	public int first;
	public int second;
	
	public PairInts(int pF,int pS)
	{
		first=pF;
		second=pS;
	}
	
	public int getAtIndex(int pIndex)
	{
		if (pIndex==1)
			return first;
		if (pIndex==2)
			return second;
		System.out.println("A getAtIndex was called with parameter:"+pIndex);
		return -1;
	}
}
