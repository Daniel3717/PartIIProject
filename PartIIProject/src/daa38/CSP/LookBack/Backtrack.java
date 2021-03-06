package daa38.CSP.LookBack;

import java.util.ArrayList;

import daa38.CSP.Auxiliary.StepFrame;
import daa38.CSP.Main.Solver;

public class Backtrack extends LookBack {

	public Backtrack(Solver pSolver)
	{
		super(pSolver);
	}
	
	@Override
	public int jump(ArrayList<StepFrame> pSteps, int pIndex) {
		
		pSteps.get(pIndex).resetFrame();
		pIndex--;
		
		if (pIndex==-1) //Failed to find a solution
			return pIndex;
		
		StepFrame currentFrame = pSteps.get(pIndex);
		
		//DEBUG:
		//System.out.println("Backtrack to:");
		//currentFrame.outputFrame();
		
		currentFrame.removeValue();
		currentFrame.mNowValIndex++;
		
		return pIndex;
	}

}
