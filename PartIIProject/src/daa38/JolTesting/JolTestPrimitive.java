package daa38.JolTesting;

import org.openjdk.jol.info.GraphLayout;

public class JolTestPrimitive {

	public static class A
	{
		private Integer mInt;
		public String mS;
		
		public A(int pInt,String pS)
		{
			mInt=pInt;
			mS=pS;
		}
	}
	
	public static void main(String[] args) {
		A lA = new A(3,"4");
		System.out.println(GraphLayout.parseInstance(lA).toPrintable());
		System.out.println(GraphLayout.parseInstance(lA).totalSize());
		
	}

}
