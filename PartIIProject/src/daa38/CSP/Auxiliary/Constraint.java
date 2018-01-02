package daa38.CSP.Auxiliary;

import java.util.ArrayList;
import java.util.Collection;

public class Constraint {
	public Variable mVariable1;
	public Variable mVariable2;
	public Collection<PairInts> mValues;
	
	public Constraint()
	{
		mVariable1 = new Variable();
		mVariable2 = new Variable();
		mValues = new ArrayList<PairInts>();
	}
	
}
