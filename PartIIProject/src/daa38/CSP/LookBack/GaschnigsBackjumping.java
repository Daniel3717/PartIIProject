package daa38.CSP.LookBack;

import java.util.ArrayList;

import daa38.CSP.Auxiliary.StepFrame;
import daa38.CSP.Auxiliary.Variable;
import daa38.CSP.Main.Solver;

public class GaschnigsBackjumping extends Backtrack {

	public GaschnigsBackjumping(Solver pSolver)
	{
		super(pSolver);
	}
	
	@Override
	public int jump(ArrayList<StepFrame> pSteps, int pIndex) {
		
		StepFrame lNowFrame = pSteps.get(pIndex);
		Variable lVarEnd = lNowFrame.mVar;
		if (lVarEnd.mDomain.size()==0) //so, if it is a leaf dead-end
		{
			do
			{
				lNowFrame.resetFrame();
				pIndex--;
				lNowFrame = pSteps.get(pIndex);
			}
			while (!lNowFrame.restrictsVariable(lVarEnd));
			
			lNowFrame.removeValue();
			lNowFrame.mNowValIndex++;
			
			return pIndex;
		}
		
		//if it is an internal dead-end, we just backtrack
		return super.jump(pSteps,pIndex);
	}

}
