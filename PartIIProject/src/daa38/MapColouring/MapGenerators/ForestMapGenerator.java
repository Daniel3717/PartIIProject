package daa38.MapColouring.MapGenerators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import daa38.MapColouring.Auxiliary.IntMatrixElement;
import daa38.MapColouring.Auxiliary.MatrixPosition;

public class ForestMapGenerator {


	private static class Forest
	{
		public static HashMap<Integer, Forest> mIntToForest = new HashMap<Integer,Forest>();
		
		public ArrayList<IntMatrixElement> mBorder;
		public int mIndex;
		
		public Forest(IntMatrixElement pStart, int pIndex)
		{
			mBorder = new ArrayList<IntMatrixElement>();
			mBorder.add(pStart);
			mIndex = pIndex;
			
			mIntToForest.put(mIndex, this);
		}
		
		public boolean grow()
		{
			Collections.shuffle(mBorder);
			while (!mBorder.isEmpty())
			{
				int lIndex = mBorder.size()-1;
				IntMatrixElement lNow = mBorder.get(lIndex);
				mBorder.remove(lIndex);
				
				if (lNow.get()==0)
				{
					lNow.set(mIndex);

					Collection<IntMatrixElement> lNexts = new ArrayList<IntMatrixElement>();
					lNexts.add(lNow.up());
					lNexts.add(lNow.left());
					lNexts.add(lNow.down());
					lNexts.add(lNow.right());
					
					for (IntMatrixElement lNext : lNexts)
					{
						if (lNext.isInsideMatrix())
						{
							mBorder.add(lNext);
						}
					}
					
					return true;
				}
			}
			return false;
		}
	}
	
	private static Collection<Forest> fullRandomInitializeForests(int pNrForests, int[][] pMap)
	{	
		Collection<Forest> lForests = new ArrayList<Forest>();
		
		for (int lIndex=1; lIndex<=pNrForests; lIndex++)
		{
			int lRow,lCol;
			
			do
			{
				lRow = (int) Math.floor(Math.random()*pMap.length);
				lCol = (int) Math.floor(Math.random()*pMap[lRow].length);
			}
			while (pMap[lRow][lCol]!=0);
			
			Forest lF = new Forest(new IntMatrixElement(pMap,new MatrixPosition(lRow,lCol)),lIndex);
			lF.grow();
			lForests.add(lF);
		}
		
		return lForests;
	}
	
	public static int[][] generateMap(int pNrZones, int pSize)
	{
		
		int[][] lMap = new int[pSize][pSize];
		
		for (int lRow = 0; lRow<pSize; lRow++)
		{
			for (int lCol = 0; lCol<pSize; lCol++)
			{
				lMap[lRow][lCol]=0;
			}
		}
		
		Collection<Forest> lForests = fullRandomInitializeForests(pNrZones,lMap);
		
		Collection<Forest> lToRemove = new ArrayList<Forest>();
		while (!lForests.isEmpty())
		{
			for (Forest lF : lForests)
			{
				if (!lF.grow())
					lToRemove.add(lF);
			}
			
			for (Forest lF : lToRemove)
			{
				lForests.remove(lF);
			}
			
			lToRemove.clear();
		}
		
		return lMap;
	}
}
