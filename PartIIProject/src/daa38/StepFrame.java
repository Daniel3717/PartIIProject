package daa38;

import java.util.ArrayList;
import java.util.HashMap;

public class StepFrame {
	public HashMap<Variable, StepFrame> mExVars; //Variables assigned before this frame
	public ArrayList<Variable> mVarsToGo; //Variables to go through during this frame
	public int mNowVarIndex; //Current variable's index
	public ArrayList<Integer> mValsToGo; //Values that can be used for current variable
	public ArrayList<VariablesRestrictions> mRes; //Restrictions imposed on other variables due to value at same index
	public int mNowValIndex; //Current value's index

	public StepFrame()
	{
		mExVars = new HashMap<Variable, StepFrame>();
		mVarsToGo = new ArrayList<Variable>();
		mNowVarIndex = -1;
		mValsToGo = new ArrayList<Integer>();
		mRes = new ArrayList< VariablesRestrictions >();
		mNowValIndex = -1;
	}
	
	public void outputFrame()
	{
		System.out.println("Variables already assigned: ");
		for (Variable lV : this.mExVars.keySet())
			System.out.println("V"+lV.mIndex+"="+lV.mValue);
		System.out.println();
		
		
		System.out.print("Variables to assign: ");
		for (Variable lV : this.mVarsToGo)
			System.out.print(" " + lV.mIndex);
		System.out.println();
		System.out.println();
		
		
		System.out.println("Current variable index: "+this.mNowVarIndex);
		System.out.println();
		
		System.out.print("Values the variable can take:");
		for (Integer lInt : this.mValsToGo)
			System.out.print(" "+lInt);
		System.out.println();
		System.out.println();
		
		System.out.println("Current value index: "+this.mNowValIndex);
		System.out.println();
		
	}
}
