package daa38.JolTesting;

import java.util.ArrayList;

import org.openjdk.jol.info.GraphLayout;

public class JolTestMultipleRef {

	public static class Base
	{
		private ArrayList<Integer> mAL;
		public Base()
		{
			mAL = new ArrayList<Integer>();
			for (int i=0; i<100; i++)
				mAL.add(i);
		}
	}
	
	public static class Ext1
	{
		private Base mBase;
		private Integer mInt;
		
		public Ext1(Base pBase)
		{
			mBase = pBase;
			mInt = 111;
		}
	}
	
	public static class Ext2
	{
		private Base mBase;
		private Integer mInt;
		
		public Ext2(Base pBase)
		{
			mBase = pBase;
			mInt = 112;
		}
	}
	
	public static class Ext12
	{
		private Ext1 mExt1;
		private Ext2 mExt2;
		private Integer mInt;
		
		public Ext12(Ext1 pExt1, Ext2 pExt2)
		{
			mExt1 = pExt1;
			mExt2 = pExt2;
			mInt = 1112;
		}
	}
	
	public static void main(String[] args) {
		
		Base lCommon = new Base();
		
		Ext1 lExt1 = new Ext1(lCommon);
		Ext2 lExt2 = new Ext2(lCommon);
		
		Ext12 lExt12 = new Ext12(lExt1, lExt2);
		
		System.out.println(GraphLayout.parseInstance(lExt12).toPrintable());
		System.out.println(GraphLayout.parseInstance(lExt12).totalSize());
		
	}
}
