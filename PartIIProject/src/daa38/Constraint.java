package daa38;

import java.util.ArrayList;

public class Constraint {
	public Variable mVariable1;
	public Variable mVariable2;
	public ArrayList<PairInts> mValues;
	
	public Constraint()
	{
		mVariable1 = new Variable();
		mVariable2 = new Variable();
		mValues = new ArrayList<PairInts>();
	}
}
