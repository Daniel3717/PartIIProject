package daa38.Auxiliary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class Variable {
	public int mIndex;
	public Collection<Integer> mDomain;
	public int mValue;
	public Collection<Constraint> mConstraints;
	
	public Variable()
	{
		mIndex = 0;
		mDomain = new HashSet<Integer>();
		mValue = 0;
		mConstraints = new ArrayList<Constraint>();
	}
	
}