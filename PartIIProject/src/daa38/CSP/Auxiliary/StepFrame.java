package daa38.CSP.Auxiliary;

import java.util.ArrayList;
import java.util.Collection;

public class StepFrame {
	public Variable mVar; //Variable to assign at this frame
	public ArrayList<Integer> mValsToGo; //Values that can be used for current variable
	public ArrayList<VariablesRestrictions> mRes; //Restrictions imposed on other variables due to value at same index
	public Integer mNowValIndex; //Current value's index

	public StepFrame()
	{
		mVar = null;
		mValsToGo = new ArrayList<Integer>();
		mRes = new ArrayList< VariablesRestrictions >();
		mNowValIndex = -1;
	}
	
	public void resetFrame()
	{
		if ((mVar!=null)&&(mVar.mValue != null))
		{
			removeValue();
		}
		
		mVar = null;
		mValsToGo.clear();
		mRes.clear();
		mNowValIndex = -1;
	}
	
	public void assignValue()
	{
		if (mVar.mValue!=null)
		{
			System.out.println("assignValue() was called when a value was already assigned; removing assigned value");
			removeValue();
		}
		
		mVar.mValue = mValsToGo.get(mNowValIndex);
		mRes.get(mNowValIndex).enforceRestrictions();
	}
	
	public boolean restrictsVariable(Variable pVar)
	{
		Collection<Integer> mRestrictedValues = mRes.get(mNowValIndex).getVarRestrictions(pVar); 
		
		if (mRestrictedValues==null)
			return false;
		
		//if (mRestrictedValues.size()==0)
		//	System.out.println("We restricted a variable by 0 values, which shouldn't happen (inefficiency)");
		
		return true;
	}
	
	public void removeValue()
	{
		if (mVar.mValue == null)
		{
			System.out.println("removeValue() was called with no value assigned");
			//it won't do any harm, but it's doing nothing so the call was probably erroneous
		}
		else
		{
			mVar.mValue = null;
			mRes.get(mNowValIndex).liftRestrictions();
		}
	}
	
	public void outputFrame()
	{
		System.out.print("Variable name to assign: ");
		if (mVar==null)
			System.out.print("null");
		else
			System.out.print(mVar.mName);
		System.out.println();
		
		System.out.print("Values the variable can take:");
		for (Integer lInt : mValsToGo)
			System.out.print(" "+lInt);
		System.out.println();
		
		System.out.println("Current value index: "+mNowValIndex);
		System.out.println();
		
	}
}
