package kkd.common.index.my;

import java.io.File;
import java.io.RandomAccessFile;

public class BPlusUtil {
	
	public  static BPlusTtree toBPlusTree() {
		try {
			BPlusTtree<String,String> bpt=new BPlusTtree<String,String >();
			RandomAccessFile dis=new RandomAccessFile(new File("E:/test/metedata.txt"),"r");
			if(dis.length()>0){
				bpt.HT =dis.readInt();
				bpt.N=dis.readInt();
				bpt.rootPos=dis.readInt();
				dis.close();
			}else{
				RandomAccessFile dos=new RandomAccessFile(new File("E:/test/metedata.txt"),"rw");
				dos.writeInt(0);
				dos.writeInt(0);
				dos.writeInt(0);
				dos.close();
			}
			return bpt;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static void main(String[] args) {
		BPlusTtree bpt=BPlusUtil.toBPlusTree();
		bpt.put("1", "1");
//		System.out.println(bpt.get("1"));
		
		
	}
}
