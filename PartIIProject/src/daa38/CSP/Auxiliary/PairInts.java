package daa38.CSP.Auxiliary;

public class PairInts {
	public final int first;
	public final int second;
	
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