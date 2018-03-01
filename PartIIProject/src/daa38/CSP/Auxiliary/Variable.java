package daa38.CSP.Auxiliary;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Variable {
	public Integer mName;
	public Collection<Integer> mDomain;
	public Integer mValue;
	public Map<Variable, Collection<Constraint> > mConstraints;
	
	public Variable()
	{
		mName = null;
		mDomain = new HashSet<Integer>();
		mValue = null;
		mConstraints = new HashMap<Variable, Collection<Constraint> >();
	}
	
}
