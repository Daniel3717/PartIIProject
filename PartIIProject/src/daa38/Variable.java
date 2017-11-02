package daa38;

import java.util.ArrayList;

public class Variable {
	public int mName;
	public ArrayList<Integer> mDomain;
	public int mValue;
	public ArrayList<Constraint> mConstraints;
	
	public Variable()
	{
		mName = 0;
		mDomain = new ArrayList<Integer>();
		mValue = 0;
		mConstraints = new ArrayList<Constraint>();
	}
}