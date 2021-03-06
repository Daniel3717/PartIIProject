package daa38.CSP.VariableOrdering;

import daa38.CSP.Auxiliary.Variable;

//Class used for comparing degrees of constraint-ment between variables
class VCV implements Comparable<VCV> //ValuesConstraintsVariables
{
	public int mValues;
	public int mConstraints;
	public Variable mVariable;
	
	public VCV(int pValues, int pConstraints, Variable pVariable)
	{
		mValues = pValues;
		mConstraints = pConstraints;
		mVariable = pVariable;
	}
	
	//Var1 Smaller than Var2 means Var1 is less constrained than Var2
	public int compareTo(VCV that) 
	{
		if (this.mValues>that.mValues)
			return -1;
		if (this.mValues<that.mValues)
			return 1;

		if (this.mConstraints<that.mConstraints)
			return -1;
		if (this.mConstraints>that.mConstraints)
			return 1;
		
		if (this.mVariable.mName<that.mVariable.mName)
			return -1;
		if (this.mVariable.mName>that.mVariable.mName)
			return 1;
		
		//Note: I could do random tie-breaking, but I'd like the algorithms to be 
		//as deterministic as possible for the comparison study
		
		return 0;
	}
}
