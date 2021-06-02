package kkd.common.index.my;

import java.io.File;
import java.io.RandomAccessFile;



/**
 * 
 * @author tanbin
 *
 * @param <Key>
 * @param <Value>
 * 文件结构
 
struts{
	rootPos:long(64),根节点的位置
	height:int(32),高度
	nodeNum:int(32),节点数量
	M:int(32)节点最大能存多少数据
}

struts{
	isleaf:boolean(1)//是否是叶子节点
	nodeCount:int(32),//节点数量
	nextPos:int(32),//下个数据节点位置
	DataPoss[
	DataNodePos:int(32),
	DataNodePos:int(32),
	DataNodePos:int(32),
	DataNodePos:int(32),
	],
	keys:[{
		key:String(256),//查询关键字//
	},{
		key:String(256)
	},{
		key:String(256)
	}
	]
}

 */
@SuppressWarnings("rawtypes")
public class BPlusTtree<Key extends Comparable<Key>, Value>  {
	
	static final int M = 4;   //节点的最大长度
	long rootPos;		  //根节点位置
	Node root;            //根节点
	int HT;               //树的高度
	int N;                //节点个数

	private static final class Node {
		boolean isleaf=true;				//是否叶子
		int startPos;					//节点开始位置
		int m;						// 子节点的个数
		int nextPos;				//下个节点的位置
		int dataPos;				//数据指针
		Entry[] children = null;    // 子节点数组
		Node nextNode=null;			//下个节点

		private Node() {
		}
		//创建一个带k个孩子的节点
		private Node(int k) {
			m = k;
		}
	
	}
	
	
	
	private static class Entry {
		private int dataPos;
		private int lastPos;
		private Comparable key;
		private Object value;
		@SuppressWarnings("unused")
		private long nextPos;
		private Node next;     // helper field to iterate over array entries
		public Entry(Comparable key, Object value, Node next) {
			this.key   = key;
			this.value = value;
			this.next  = next;
		}
		public Entry() {
		}
		
	}
	public BPlusTtree() {  }
	public int size() { return N; }//节点个数

	public int height() { return HT; }//节点高度

	
	public static String toData(BPlusTtree bpt){
		StringBuilder sb=new StringBuilder();
		//		private static final int M = 4;   //节点的最大长度
		//		private int rootPos;		  //根节点位置
		//		private int HT;               //树的高度
		//		private int N;                //节点个数
		//		private Node root;            //根节点
		return null;
	}

	@SuppressWarnings("unchecked")
	private boolean less(Comparable k1, Comparable k2) {
		return k1.compareTo(k2) < 0;
	}

	@SuppressWarnings("unchecked")
	private boolean eq(Comparable k1, Comparable k2) {
		return k1.compareTo(k2) == 0;
	}

	public Value get(Key key) { 
		return search(this.root, key,this.HT); 
	}

	@SuppressWarnings("unchecked")
	private Value search(Node x, Key key, int ht) {
		Entry[] children = x.children;
		// external node
		if (ht == 0) {
			for (int j = 0; j < x.m; j++) {
				if (eq(key, children[j].key)) {
					return (Value) children[j].value;
				}
			}
		}
		// internal node
		else {
			for (int j = 0; j < x.m; j++) {
				if (j+1 == x.m || less(key, children[j+1].key)){
					Node node=this.getNode(children[j].dataPos);
					return search(node, key, ht-1);
				}
			}
		}
		return null;
	}
	
	public  Node getNode(long s){
		try {
			RandomAccessFile dis=new RandomAccessFile(new File("E:/node.txt"),"r");
			dis.seek(s);
			Node node= new Node();
			node.startPos=(int)s;
			if(dis.length()>0){
				int pos=-1;
				dis.readInt();
				pos=pos+4;
				node.isleaf =dis.readInt()==1?true:false;//是否是叶子节点
				pos=pos+4;
				node.m=dis.readInt();//节点数量
				pos=pos+4;
				node.nextPos=dis.readInt();//下个数据节点位置
				pos=pos+4;
				int m=node.m;
				node.children=new Entry[M];
				for(int i=0;i<m;i++){
					byte[] b=new byte[32];
					pos=pos+32;
					dis.read(b);
					Entry  e=new Entry();
					node.children[i]=e;
					e.key=byteToString(b);
					e.dataPos=dis.readInt();
					pos=pos+4;
					if(i==m-1){
						e.lastPos=dis.readInt();
						pos=pos+4;
					}
				}
			}
			return node;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String byteToString(byte[] b){
		StringBuilder sb=new StringBuilder();
		for (int i = 0; i < b.length; i++) {
			if(b[i]!=0){
				sb.append((char)b[i]);
			}
		}
		return sb.toString();
	}
	public byte[] stringToByte(String b){
		byte[] bt=b.getBytes();
		byte[] bt1 =new byte[32];
		for (int i = 0; i < bt1.length&& i<bt.length; i++) {
			bt1[i]=bt[i];
		}
		return bt1;
	}
	
	
	public void toBytes(Node node){
		try {
			RandomAccessFile raf=new RandomAccessFile(new File("E:/test/node.txt"),"rw");
			raf.seek(node.startPos);
			int pos=-1;
			raf.writeInt(node.startPos);
			pos=pos+4;
			raf.writeInt(node.isleaf?1:0);//是否是叶子节点
			pos=pos+4;
			raf.writeInt(node.m);
			pos=pos+4;
			raf.writeInt(node.nextPos);//下个数据节点位置
			pos=pos+4;
			int m=node.m;
			if(node.children==null){
				node.children=new Entry[M];
			}
			for(int i=0;i<m;i++){
				Entry en=node.children[i];
				pos=pos+32;
				raf.write(stringToByte((String)en.key));
				pos=pos+4;
				raf.writeInt(en.dataPos);
				if(i==m-1){
					pos=pos+4;
					raf.writeInt(en.lastPos);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public void put(Key key, Value value) {
		this.root=this.getNode(this.rootPos);
		if(this.root==null){
			
		}
		if(this.root.children==null){
			this.root.children=new Entry[M];
		}
		Node u = insert(this.root, key, value, this.HT); 
		N++;
		if (u == null) {
			return;
		}
		// need to split root
		Node t = new Node(2);
		t.children=new Entry[M];
		t.children[0] = new Entry(root.children[0].key, null, root);
		t.children[1] = new Entry(u.children[0].key, null, u);
		this.toBytes(t);
		root = t;
		this.HT++;
	}
	
	private Node insert(Node h, Key key, Value value, int ht) {
        int j;
        Entry t = new Entry(key, value, null);
        // external node
        if (ht == 0) {
            for (j = 0; j < h.m; j++) {
                if (less(key, h.children[j].key)) {
                	break;
                }
            }
         // internal node
        }else {
            for (j = 0; j < h.m; j++) {
                if ((j+1 == h.m) || less(key, h.children[j+1].key)) {
                    Node u = insert(h.children[j++].next, key, value, ht-1);
                    if (u == null) {
                    	return null;
                    }
                    t.key = u.children[0].key;
                    t.next = u;
                    break;
                }
            }
        }
        for (int i = h.m; i > j; i--) {
        	h.children[i] = h.children[i-1];
        }
        h.children[j] = t;
        h.m++;
        if (h.m < M) {
        	this.toBytes(h);
        	return null;
        }else{
        	return split(h);
        }
    }
	
	private Node split(Node h) {
		Node t = new Node();
		h.m = M/2;
		t.children=new Entry[M];
		for (int j = 0; j < M/2; j++){
			t.children[j] = h.children[M/2+j];
		}
		this.toBytes(h);
		this.toBytes(t);
		return t;
	}
	
}