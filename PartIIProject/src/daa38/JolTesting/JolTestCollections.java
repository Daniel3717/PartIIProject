package daa38.JolTesting;

import java.util.ArrayList;
import java.util.HashMap;

import org.openjdk.jol.info.GraphLayout;

public class JolTestCollections {

	public static class A
	{
		private ArrayList<Integer> mALInt;
		private HashMap<Integer, Integer> mHMInt;
		
		public A(int pCount)
		{
			mALInt = new ArrayList<Integer>();
			mHMInt = new HashMap<Integer, Integer>();
			while (pCount>0)
			{
				mALInt.add(1);
				mHMInt.put(pCount,1);
				pCount--;
			}
		}
	}
	
	public static void main(String[] args) {
		A lA = new A(100);
		System.out.println(GraphLayout.parseInstance(lA).toPrintable());
		System.out.println(GraphLayout.parseInstance(lA).totalSize());
		
	}

}
