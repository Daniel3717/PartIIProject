package daa38.MapColoring;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class ForestMapGenerator {

	
	private static class Position
	{
		int row,col;
		
		public Position(int pRow, int pCol)
		{
			row=pRow;
			col=pCol;
		}
		
		public Position up()
		{
			return new Position(row-1,col);
		}

		public Position left()
		{
			return new Position(row,col-1);
		}

		public Position down()
		{
			return new Position(row+1,col);
		}

		public Position right()
		{
			return new Position(row,col+1);
		}
		
		public boolean isInsideMatrix(int[][] pMatrix)
		{
			if (row>=pMatrix.length)
				return false;
			if (row<0)
				return false;
			
			if (col>=pMatrix[row].length)
				return false;
			if (col<0)
				return false;
			
			return true;
		}
	}

	private static class Forest
	{
		public static int[][] mMap;
		public static HashMap<Integer, Forest> mIntToForest = new HashMap<Integer,Forest>();
		
		public ArrayList<Position> mNext;
		public int mIndex;
		
		public Forest(Position pStart, int pIndex)
		{
			mNext = new ArrayList<Position>();
			mNext.add(pStart);
			mIndex = pIndex;
			
			mIntToForest.put(mIndex, this);
		}
		
		public boolean grow()
		{
			Collections.shuffle(mNext);
			while (!mNext.isEmpty())
			{
				int lIndex = mNext.size()-1;
				Position lPos = mNext.get(lIndex);
				mNext.remove(lIndex);
				
				if (mMap[lPos.row][lPos.col]==0)
				{
					mMap[lPos.row][lPos.col] = mIndex;

					Position lUp = lPos.up();
					Position lLeft = lPos.left();
					Position lDown = lPos.down();
					Position lRight = lPos.right();
					
					if (lUp.isInsideMatrix(mMap))
						mNext.add(lUp);
					
					if (lLeft.isInsideMatrix(mMap))
						mNext.add(lLeft);
					
					if (lDown.isInsideMatrix(mMap))
						mNext.add(lDown);
					
					if (lRight.isInsideMatrix(mMap))
						mNext.add(lRight);
					
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
			
			Forest lF = new Forest(new Position(lRow,lCol),lIndex);
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

		Forest.mMap = lMap;
		
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
