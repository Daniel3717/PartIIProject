package daa38.CSP.VariableOrdering;

import java.util.ArrayList;
import java.util.Collections;

import daa38.CSP.Auxiliary.StepFrame;
import daa38.CSP.Auxiliary.Variable;

public class MostConstrainedVariableOrdering implements VariableOrdering {

	//Most constrained = least values in domain
	//Theoretically, tie breakers should be on the most constraints with remaining variables
	//However, it is more efficient to not maintain the constraints up-to-date
	//Thus, tie-breakers will be decided based on the most original constraints
	//(Further tie-breakers will be decided based on Variable index, which should be unique)
	@Override
	public void process(StepFrame pSF) {
		
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
			
			public int compareTo(VCV that) //Var1 Smaller than Var2 means Var1 gets checked before Var2
			{
				if (this.mValues<that.mValues)
					return -1;
				if (this.mValues>that.mValues)
					return 1;

				if (this.mConstraints>that.mConstraints)
					return -1;
				if (this.mConstraints<that.mConstraints)
					return 1;
				
				if (this.mVariable.mIndex<that.mVariable.mIndex)
					return -1;
				if (this.mVariable.mIndex>that.mVariable.mIndex)
					return 1;
				
				return 0;
			}
		}
		
		ArrayList<VCV> mOrder = new ArrayList<VCV>();
		for (Variable lV : pSF.mVarsToGo)
		{
			mOrder.add(new VCV(lV.mDomain.size(),lV.mConstraints.size(),lV));
		}
		
		Collections.sort(mOrder);
		
		//DEBUG:System.out.println("Ordering is:");
		pSF.mVarsToGo.clear();
		for (VCV lVCV : mOrder)
		{
			pSF.mVarsToGo.add(lVCV.mVariable);
			//DEBUG:System.out.println("Variable "+lVCV.mVariable.mIndex+" with "+lVCV.mValues+" values and "+lVCV.mConstraints+" constraints");
		}
		
		pSF.mNowVarIndex=0;
	}

}