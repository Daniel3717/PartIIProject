package daa38.CSP.Auxiliary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Constraint {
	public Variable mVariable1;
	public Variable mVariable2;
	
	//I need to make this public so one can iterate over it
	//But for any addition, addPair should be used
	//Since addPair also updates the Maps
	public Collection<PairInts> mValues;
	
	private Map<Integer, Collection<Integer> > mVal1ToVals2;
	private Map<Integer, Collection<Integer> > mVal2ToVals1;
	
	public Constraint()
	{
		mVariable1 = new Variable();
		mVariable2 = new Variable();
		mValues = new ArrayList<PairInts>();
		mVal1ToVals2 = new HashMap<Integer, Collection<Integer> >();
		mVal2ToVals1 = new HashMap<Integer, Collection<Integer> >();
	}
	
	public Variable otherVar(Variable pVar)
	{
		if (pVar==mVariable1)
		{
			return mVariable2;
		}
		
		if (pVar==mVariable2)
		{
			return mVariable1;
		}
		
		//ERROR:
		System.out.println("Constraint.otherVar was called with an invalid argument, returning null");
		return null;
	}
	
	public int getIndex(Variable pVar)
	{
		if (pVar==mVariable1)
		{
			return 1;
		}
		if (pVar==mVariable2)
		{
			return 2;	
		}
		
		//ERROR:
		System.out.println("Constraint.getIndex was called with an invalid argument, returning 0");
		return 0;
	}
	
	public void addPair(PairInts lPI)
	{
		mValues.add(lPI);
		
		int lFirst = lPI.getAtIndex(1);
		int lSecond = lPI.getAtIndex(2);
		
		Collection<Integer> lValsOfFirst;
		lValsOfFirst = mVal1ToVals2.get(lFirst);
		if (lValsOfFirst == null)
		{
			lValsOfFirst = new HashSet<Integer>();
			mVal1ToVals2.put(lFirst, lValsOfFirst);
		}
		lValsOfFirst.add(lSecond);

		Collection<Integer> lValsOfSecond;
		lValsOfSecond = mVal2ToVals1.get(lSecond);
		if (lValsOfSecond == null)
		{
			lValsOfSecond = new HashSet<Integer>();
			mVal2ToVals1.put(lFirst, lValsOfSecond);
		}
		lValsOfSecond.add(lFirst);
	}
	
	public Collection<Integer> getValsOfFirst(Integer pFirst)
	{
		return mVal1ToVals2.get(pFirst);
	}
	
	public Collection<Integer> getValsOfSecond(Integer pSecond)
	{
		return mVal2ToVals1.get(pSecond);
	}
	
	public boolean consistent(Integer pVal1, Integer pVal2)
	{
		Collection<Integer> lVals2 = mVal1ToVals2.get(pVal1);
		if (lVals2==null)
			return true;
		
		if (lVals2.contains(pVal2))
			return false;
		else
			return true;
	}
}
