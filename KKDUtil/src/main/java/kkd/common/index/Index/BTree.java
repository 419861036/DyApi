package kkd.common.index.Index;
import java.io.*;

import javax.swing.JOptionPane;
public class BTree implements Serializable//索引文件采用二进制文件存储,且分成字文件进行存储
{
	public static final String indexfilename="indexfile";
	public static final String datafilename="datafile.txt";
	public static final String modefilename1="mode1.txt";
	public static final String modefilename2="mode2.txt";
	public static final String peizhifilename="peizhi";
	public static final int datablocksum=20;//最多存400个
	public static final int perdatablock=20;//每个数据块最多存取的记录数
	public static final int indexfilesum=111;//索引文件最多的数目
	public static final int max=3;//B树的阶数
	public static final long datablockbytes=4021;
	public static final int perrecord=200;//每个数据记录的大小
	public static final int studentdatawidth=8;
	public static final int moderecordwidth=7;
	public static int readtimes;
	public static int writetimes;
	String[] insdata;
	int pos; /* 查找时找到的键在节点中的位置 */
	static int flag; /* 节点增减标志,为1时节点需有增减 */
	static int count; /* 多路树的键总数 */
	//public static int curpos=0,parent=1;
	static int NewTree; /* 在节点分割的时候指向新建的节点的地址，即子文件的代号 */
	static long InsKey; /* 要插入的键 */
	static int InsAddr; /* 要插入的键相对应的数据指针 */
	static int InnerAddr;
    
	//构造方法
	public BTree()
	{
		readtimes=0;
		writetimes=0;
		flag=0;
        count=0;
		try
		{
			PrintWriter outputStream;
			String inString=null;
			File file;
			file=new File(modefilename1);
			if(!file.exists())
			{
			outputStream=new PrintWriter(new FileOutputStream(modefilename1));
			inString="0-datafile-"+perrecord+"-0-0-0-";
			outputStream.println(inString);
			//writetimes++;
			//System.out.println("&&&&&&一次写");
			outputStream.close();
			}
			file=new File(modefilename2);
			if(!file.exists())
			{
			outputStream=new PrintWriter(new FileOutputStream(modefilename2));
			inString="datafile-学号-string-20B-姓名-string-8B-出生日期-string-8B-家庭所在地-string-40B-院系-string-12B-年级-string-4B-班级－string-2B-课程及成绩列表-string-98B";
			outputStream.println(inString);
			//writetimes++;
			//System.out.println("&&&&&&一次写");
			outputStream.close();
			}
			file=new File(peizhifilename);
			if(!file.exists())
			{
				System.out.println("初始化时写配置文件");
			ObjectOutputStream outStream=new ObjectOutputStream(new FileOutputStream(peizhifilename));
			boolean[] bitmap=new boolean[datablocksum+indexfilesum];
			for(int i=0;i<bitmap.length;i++)
				bitmap[i]=false;
			BitMap bit=new BitMap(bitmap);
			outStream.writeObject(bit);
			//writetimes++;
			//System.out.println("&&&&&&一次写");
			outStream.close();
			}
		}
		catch(Exception e)
		{
			System.out.println("b-tree的构造方法出问题"+"\n"+e.getMessage());
		}
	}
	
	//关键值的检索
	public void modify(String[] data)
	{
		System.out.println("enter the method of modify");
		long k=Long.parseLong(data[0]);
		ObjectInputStream inputStream;
		BTNode t=null;
		int i,j,m=0;
		BufferedReader input;
		try
		{
			//读取模式文件，获得索引首地址
	    	input=new BufferedReader(new FileReader(modefilename1));
	    	//readtimes++;
	    	//System.out.println("读一次");
	    	String s=input.readLine();
	    	input.close();
	    	int firstsign=s.indexOf("-");
	    	s=s.substring(firstsign+1);
	    	int secondsign=s.indexOf("-");
	    	s=s.substring(secondsign+1);
	    	int thirdsign=s.indexOf("-");
	    	s=s.substring(thirdsign+1);
	    	int fourthsign=s.indexOf("-");
	    	int firstindex=Integer.parseInt(s.substring(0, fourthsign));
	    	if(firstindex==0)
	    		throw new NokeyException();
	    	int filenum=firstindex;			
			File file=new File(indexfilename+Integer.toString(firstindex));
			System.out.println("k:"+k);
			while(file.exists())
			{
				inputStream=new ObjectInputStream(new FileInputStream(indexfilename+Integer.toString(filenum)));
				t=(BTNode)inputStream.readObject();
				readtimes++;
				System.out.println("读一次");
				inputStream.close();			  
			        for(i=0, j=t.d-1; i<j; m=(j+i)/2)
			        {
			        	if(k>t.key[m]) i=m+1;
			        	else           j=m;
			        		
			        }
			        if (k == t.key[i])
			        {
			            pos=i;
			            RandomAccessFile ranfile=new RandomAccessFile(datafilename,"rw");
			            ranfile.seek(datablockbytes*(t.addr[i]-1)+perdatablock+1+perrecord*(t.inneraddr[i]-1));			            			        
			            byte[] getbyte=new byte[200];
			            ranfile.read(getbyte, 0, 200);
			            readtimes++;
			            System.out.println("读一次");
			            ranfile.close();
			            String snew=null;
			            snew=new String(getbyte);
			            
			            snew=snew.substring(0, snew.indexOf("\n"));
			            System.out.println("snew"+snew);
			            String[] olddata=stringtoarray(snew,studentdatawidth);
			            int z;
			            for(z=0;z<data.length;z++)
			            	if(!data[z].equals(""))//if(!data[z].equals(""))
			            	{
			            		olddata[z]=data[z];	
			            	}
			            snew=olddata[0];
			            for(z=1;z<olddata.length;z++)
			            	snew=snew+"-"+olddata[z];
			            snew=snew+"\n";
			            ranfile=new RandomAccessFile(datafilename,"rw");
			            ranfile.seek(datablockbytes*(t.addr[i]-1)+perdatablock+1+perrecord*(t.inneraddr[i]-1));	
			            ranfile.write(snew.getBytes());
			            writetimes++;
			            System.out.println("&&&&&&一次写");
			            ranfile.close();
			            JOptionPane.showMessageDialog(null,"修改完毕"+"\n"+"硬盘读次数："+readtimes+"\n"+"硬盘写次数"+writetimes);
			            return;
			        }
			        if (k > t.key[i]) /* i == t.d-1 时有可能出现 */
			            i++;
			        filenum=t.p[i];
			        System.out.println("t.p[i]"+t.p[i]);
			        file=new File(indexfilename+Integer.toString(filenum));
			}	
			throw new NokeyException();
		}	
		catch(NokeyException e1)
		{
			JOptionPane.showMessageDialog(null,e1.getMessage());
		}
		catch(Exception e)
		{
			String exceptionMessage="读索引文件发生错误"+e.getMessage();
			JOptionPane.showMessageDialog(null,exceptionMessage);
		}
		 
	}
	public String[] briefsearch(long former,long later)
	{
		BufferedReader input;
		ObjectInputStream inputStream;
		BTNode t=null;
		try
		{
			//读取模式文件，获得索引首地址
	    	input=new BufferedReader(new FileReader(modefilename1));
	    	String s=input.readLine();
	    	//readtimes++;
	    	//System.out.println("读一次");
	    	input.close();
	    	int firstsign=s.indexOf("-");
	    	s=s.substring(firstsign+1);
	    	int secondsign=s.indexOf("-");
	    	s=s.substring(secondsign+1);
	    	int thirdsign=s.indexOf("-");
	    	s=s.substring(thirdsign+1);
	    	int fourthsign=s.indexOf("-");
	    	int firstindex=Integer.parseInt(s.substring(0, fourthsign));
	    	if(firstindex==0)
	    		return null;
	    	inputStream=new ObjectInputStream(new FileInputStream(indexfilename+Integer.toString(firstindex)));
			t=(BTNode)inputStream.readObject();
		    String tab=Long.toString(later-former+1);
		    System.out.println("tab"+tab);
		    int len=Integer.parseInt(tab);
		    String[] info=new String[len];
		    for(int x=0;x<len;x++)
			   info[x]="";
		    int xiabiao=0;
		    System.out.println("enter the method of internalsearch");
		    internalsearch(info,t,former,later,xiabiao);
		    JOptionPane.showMessageDialog(null,"模糊查找完毕"+"\n"+"硬盘读次数："+readtimes+"\n"+"硬盘写次数"+writetimes);
		    return info;
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null,"无法读取"+e.getMessage());
		}
		return null;
	}
	public boolean internalsearch(String[] info,BTNode t,long former,long later,int xiabiao)
	{
		System.out.println("enter the method of internalsearch");
		RandomAccessFile ranfile;
		boolean hasfind=false;
		if(t==null)
			return false;
		int i,j,k;
		for(i=0;i<t.d&&t.key[i]<former;i++);
		System.out.println("i"+i);
		for(j=t.d-1;j>=0&&t.key[j]>later;j--);
		System.out.println("j"+j);
		
		for(k=i;k<=j;k++)
		{
			try{
				ranfile=new RandomAccessFile(datafilename,"rw");
	            ranfile.seek(datablockbytes*(t.addr[k]-1)+perdatablock+1+perrecord*(t.inneraddr[k]-1));			            			        
	            byte[] getbyte=new byte[200];
	            ranfile.read(getbyte, 0, 200);
	            readtimes++;
	            System.out.println("读一次");
	            ranfile.close();
	            System.out.println("读取完毕");
	            String snew=null;
	            snew=new String(getbyte);
	            snew=snew.substring(0, snew.indexOf("\n"));
	            info[xiabiao]=snew;
	            System.out.println("info["+xiabiao+"]"+snew);
	            xiabiao++;
	            k++;
	            hasfind=true;
				}
				catch(Exception e)
				{
					System.out.println("internalsearch方法出现错误");
				}
		}
		System.out.println("j"+j);
		System.out.println("former"+former);
		System.out.println("later"+later);
		System.out.println("xiabiao"+xiabiao);
		if(t.key[i]==former)
		    i++;
		
		if(t.key[j]==later)
		    j--;
		j++;
		System.out.println("i"+i+"j"+j);
		if(j<=i)
			return hasfind;
		for(int x=i;x<=j;x++)
		{
			BTNode childnode=null;
			System.out.println("t.p[x]"+t.p[x]);
			if(t.p[x]!=0)
			   childnode=GetChildNode(t,x);
			boolean find=internalsearch(info,childnode,former,later,xiabiao);
			if(find)
				xiabiao++;
		}
		return hasfind;
	}
	public String search(long k)
	{
		System.out.println("enter the method of search");
		ObjectInputStream inputStream;
		BufferedReader input;
		BTNode t=null;
		int i,j,m=0;
		try
		{
			//读取模式文件，获得索引首地址
	    	input=new BufferedReader(new FileReader(modefilename1));
	    	String s=input.readLine();
	    	//readtimes++;
	    	//System.out.println("读一次");
	    	input.close();
	    	int firstsign=s.indexOf("-");
	    	s=s.substring(firstsign+1);
	    	int secondsign=s.indexOf("-");
	    	s=s.substring(secondsign+1);
	    	int thirdsign=s.indexOf("-");
	    	s=s.substring(thirdsign+1);
	    	int fourthsign=s.indexOf("-");
	    	int firstindex=Integer.parseInt(s.substring(0, fourthsign));
	    	if(firstindex==0)
	    		return null;
	    	int filenum=firstindex;
	    	File file=new File(indexfilename+Integer.toString(filenum));
			
			while(file.exists())
			{
				inputStream=new ObjectInputStream(new FileInputStream(indexfilename+Integer.toString(filenum)));
				t=(BTNode)inputStream.readObject();
				readtimes++;
				System.out.println("读一次");
				inputStream.close();
				System.out.println("first node 不为空");
			    	System.out.println("t.d:"+t.d);
			        for(i=0, j=t.d-1; i<j; m=(j+i)/2)
			        {
			        	if(k>t.key[m]) i=m+1;
			        	else           j=m;
			        		
			        }
			        System.out.println("m:"+m);
			        if (k == t.key[i])
			        {
			        	System.out.println("关键值存在");
			            pos=i;
			            RandomAccessFile ranfile=new RandomAccessFile(datafilename,"rw");
			            ranfile.seek(datablockbytes*(t.addr[i]-1)+perdatablock+1+perrecord*(t.inneraddr[i]-1));			            			        
			            byte[] getbyte=new byte[200];
			            ranfile.read(getbyte, 0, 200);
			            readtimes++;
			            System.out.println("读一次");
			            ranfile.close();
			            System.out.println("读取完毕");
			            String snew=null;
			            snew=new String(getbyte);
			            snew=snew.substring(0, snew.indexOf("\n"));
						return snew;
			        }
			        if (k > t.key[i]) /* i == t.d-1 时有可能出现 */
			            i++;
			        filenum=t.p[i];
			        file=new File(indexfilename+Integer.toString(filenum));
			}
			
				
		}	
		catch(Exception e)
		{
			String exceptionMessage="读取学生信息时发生错误"+e.getMessage();
			JOptionPane.showMessageDialog(null,exceptionMessage);
		}
		JOptionPane.showMessageDialog(null,"查询完毕"+"\n"+"硬盘读次数："+readtimes+"\n"+"硬盘写次数"+writetimes);
	    return null;
	}
		
	public String changetostring(char[] c)
	{
		int i;
		String s=null;
		for(i=0;i<c.length;i++)
		{
			s=s+c[i];
		}
		return s;
	}
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@-----插入-----@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
	public void Insert(String[] data)//,String newaddr)//k为要插入的关键值，newaddr为关键值对应的数据指针
	{
		//$$$$$$$$$$$$$$$$$$$调试$$$$$$$$$$$$$$$
		insdata=data;
		System.out.println("enter the method of Insert");
		//insertintodatafile(data);
		long k=Long.parseLong(data[0]);
		ObjectInputStream inputStream;
		BTNode t=null;
		InsKey=k;
		NewTree=0;
	    BufferedReader input;
	    try
	    {
	    	//读取模式文件，获得索引首地址
	    	input=new BufferedReader(new FileReader(modefilename1));
	    	String modedata=input.readLine(),s;
	    	//readtimes++;
	    	//System.out.println("读一次");
	    	System.out.println("模式文件内容为："+modedata);
	    	input.close();
	    	int firstsign=modedata.indexOf("-");
	    	s=modedata.substring(firstsign+1);
	    	int secondsign=s.indexOf("-");
	    	s=s.substring(secondsign+1);
	    	int thirdsign=s.indexOf("-");
	    	s=s.substring(thirdsign+1);
	    	int fourthsign=s.indexOf("-");
	    	int firstindex=Integer.parseInt(s.substring(0, fourthsign));	    	
	    	System.out.println("插入时，firstindex："+firstindex);
	    if(firstindex!=0)
	    {
			inputStream=new ObjectInputStream(new FileInputStream(indexfilename+firstindex));
			t=(BTNode)inputStream.readObject();
			readtimes++;
			System.out.println("读一次");
			inputStream.close();
			try
			{
		    InternalInsert(k, t);
		    
			}
			catch(KeyhasexistException e)
			{				
				JOptionPane.showMessageDialog(null,e.getMessage());
			}
		    if (flag == 1)  /* 根节点满之后，它被分割成两个半满节点 */
		        NewRoot(t);    /* 树的高度增加 */
	    }
	    else
	    {
	    		
	    		insertintodatafile(data);
	    		System.out.println("root is null");
	    	    NewRoot(t);
	    }
	    	input=new BufferedReader(new FileReader(modefilename1));
		    String mode=input.readLine();
		    //readtimes++;
		    //System.out.println("读一次");
		    input.close();
		    int firsteof=mode.indexOf("-");
		    int keysum=Integer.parseInt(mode.substring(0,firsteof));
		    keysum=keysum+count;
		    mode=keysum+mode.substring(firsteof);
		    PrintWriter out=new PrintWriter(new FileOutputStream(modefilename1));
		    out.println(mode);
		    //writetimes++;
		    //System.out.println("&&&&&&一次写");
		    out.close();
		    JOptionPane.showMessageDialog(null,"插入完毕"+"\n"+"硬盘读次数："+readtimes+"\n"+"硬盘写次数"+writetimes);
	    }
	    catch(Exception e)
	    {
	    	System.out.println("索引文件插入发生错误"+e.getMessage());
	    }

	    //first=t;
	}
	public void insertintodatafile(String[] data)
	{
		count++;
		System.out.println("count:"+count);
		System.out.println("enter the method of insertiontodatafile;");
		BufferedReader inputStream;
		try
		{
			inputStream=new BufferedReader(new FileReader(modefilename1));//读表模式文件
			String mode=inputStream.readLine();
			//readtimes++;
			//System.out.println("读一次");
			inputStream.close();
			String subs,line;
			int i=mode.lastIndexOf("-");
			subs=mode.substring(i+1);
			String dataString; 
			dataString=data[0];
			for(int k=1;k<data.length;k++)
				dataString=dataString+"-"+data[k];
			dataString+="\n";
			byte[] stringarray=dataString.getBytes();
			System.out.println("要插入的记录："+dataString);
			if(subs.length()!=0)//数据空闲表存在
			{
				System.out.println("数据空闲表存在");
				int douhaopos=-1;//=subs.indexOf(",");
				for(int k=0;k<subs.length();k++)
					if(subs.charAt(k)==',')
					{
						douhaopos=k;
						break;
					}
				System.out.println("逗号的位置:"+douhaopos);
				if(douhaopos!=-1)
				 InsAddr=Integer.parseInt(subs.substring(0,douhaopos));
				else
					InsAddr=Integer.parseInt(subs.substring(0));
				System.out.println("数据插入的数据块："+InsAddr);
				inputStream=new BufferedReader(new FileReader(datafilename));//读数据文件
				inputStream.skip(datablockbytes*(InsAddr-1));
				line=inputStream.readLine();
				//readtimes++;
				//System.out.println("读一次");
				inputStream.close();				
				boolean insert=false;
				int recordnumber=0;
				for(int j=0;j<line.length();j++)
				{
					if(line.charAt(j)=='0'&&!insert)//此处无记录
					{
						line=line.substring(0, j)+"1"+line.substring(j+1);
						RandomAccessFile ranfile=new RandomAccessFile(datafilename,"rw");
						long skip=datablockbytes*(InsAddr-1);
						InnerAddr=j+1;
						System.out.println("记录插入的数据块位置"+InnerAddr);
						ranfile.seek(skip);
						ranfile.write(line.getBytes());
						//writetimes++;
						System.out.println("&&&&&&一次写");
						ranfile.close();
						ranfile=new RandomAccessFile(datafilename,"rw");
						System.out.println("插入第二个记录要跳过的字节数为"+perrecord*j);
						ranfile.seek(skip+perrecord*j+perdatablock+1);
						ranfile.write(stringarray);
						//writetimes++;
						System.out.println("&&&&&&一次写");
						insert=true;
						recordnumber++;
						ranfile.close();
					}
					else
						recordnumber++;
				}
				//更新模式文件
				if(recordnumber==line.length())//数据块内已存满记录
				{
					mode=mode.substring(0, i+1)+subs.substring(douhaopos+1);
					PrintWriter outputStream=new PrintWriter((new FileOutputStream(modefilename1)));
					//writetimes++;
					//System.out.println("&&&&&&一次写");
					outputStream.println(mode);
					outputStream.close();
				}	
			}
			else//空闲表不存在
			{
				System.out.println("空闲表不存在");
					ObjectInputStream inStream=new ObjectInputStream(new FileInputStream(peizhifilename));//读配置文件
					System.out.println("读配置文件");
					BitMap bitmap=(BitMap)inStream.readObject();
					//readtimes++;
					//System.out.println("读一次");
					inStream.close();
					System.out.println("读完配置文件");
					boolean[] bits=bitmap.getArray();					
					boolean avai=false;
					for(int j=0;j<datablocksum;j++)
					{
						if(bits[j]==false)
						{
							avai=true;
							InsAddr=j+1;
							InnerAddr=1;
							RandomAccessFile ranfile=new RandomAccessFile(datafilename,"rw");
							long skip=datablockbytes*j;
							ranfile.seek(skip);
							String smallrecord="1";
							for(int z=0;z<perdatablock-1;z++)
								smallrecord=smallrecord+"0";
							smallrecord+="\n"+dataString;
							ranfile.write(smallrecord.getBytes());
							writetimes++;
							System.out.println("&&&&&&一次写");
							//ranfile.write(stringarray);
							ranfile.close();
							System.out.println("数据文件写入完毕");
							//更新配置文件
							bits[j]=true;
							bitmap.reset(bits);
							ObjectOutputStream outStream=new ObjectOutputStream(new FileOutputStream(peizhifilename));
							outStream.writeObject(bitmap);
							//writetimes++;
							//System.out.println("&&&&&&一次写");
							outStream.close();
							System.out.println("配置文件更新完毕");
							//更新模式文件
							j++;
							String[] modedata=stringtoarray(mode,moderecordwidth);
							modedata[5]=Integer.toString(j);
							modedata[6]=Integer.toString(j);
							if(modedata[4].equals("0"))
								modedata[4]=Integer.toString(j);
							String newmode=modedata[0];
							for(int k=1;k<modedata.length;k++)
								newmode=newmode+"-"+modedata[k];
							System.out.println("newmode:"+newmode);
							PrintWriter outputStream=new PrintWriter((new FileOutputStream(modefilename1)));
							outputStream.println(newmode);
							//writetimes++;
							//System.out.println("&&&&&&一次写");
							System.out.println("模式文件更新完毕");
							outputStream.close();
							break;
						}
						
					}
					if(!avai)
						System.out.println("已存满");
			}	
		}
		catch(Exception e)
		{
			System.out.println("数据文件写数据时发生错误"+"\n"+e.getMessage());
		}
	}
	void InternalInsert(long k, BTNode t) throws KeyhasexistException
	{
		//$$$$$$$$$$$$$$$$$$$$调试$$$$$$$$$$$$$$$$$$$$$$
		System.out.println("enter the method of InternalInsert");
	    int i=0,j,m=0;  
	    //递归终止的条件
	   if(t==null)
       {
    		/* 到达了树的底部即没找到此关键值: 指出要做的插入 */	    
		   insertintodatafile(insdata);
    	        NewTree = 0; /* 这个键没有对应的子树 */
    	        InsKey = k; /* 导致底层的叶子节点增加键值+空子树对 */
    	        flag = 1; /* 指示上层节点把返回的键插入其中 */
    	        return;  
    	}
	  if(t.d!=0)
	  {
	    for(j=t.d-1; i<j; m=(j+i)/2)//折半查找
        {
        	if(k>t.key[m]) i=m+1;
        	else           j=m;		
        }
	    if (k==t.key[ i ]) /* 键已经在树中 */
	    {
	        throw new KeyhasexistException(); 
	        //flag = 0;
	        //return;
	    }
	    if (k>t.key[ i ]) /* i == t->d-1 时有可能出现 */
	        i++;
	    try
	    {
	    	File file=new File(indexfilename+Integer.toString(t.p[i]));
	    	if(file.exists())
	    	{
	    		System.out.println("file "+Integer.toString(t.p[i])+"exist");
	    		ObjectInputStream inputStream=new ObjectInputStream(new FileInputStream(indexfilename+t.p[i]));
		    	//inputStream.skip(sk*(t.p[i]-1));
		    	t=(BTNode)inputStream.readObject();
		    	readtimes++;
		    	System.out.println("读一次");
		    	inputStream.close();
		    	InternalInsert(k, t);//递归实现
	    	}
	    	else 
	    		InternalInsert(k,null);
	    }
	    catch(Exception e)
	    {
	    	System.out.println("internalinsert发生错误");
	    }
	  

	    if (flag == 0)
	        return;
	  }
	    /* 有新键要插入到当前节点中 */
	    if (t.d <max-1) 
	    {/* 当前节点未满 */
	        InsInNode(t, i); /* 把键值+子树对插入当前节点中 */
	        flag = 0; /* 指示上层节点没有需要插入的键值+子树，插入过程结束 */
	    }
	    else /* 当前节点已满，则分割这个页面并把键值+子树对插入当前节点中 */
	        SplitNode(t, i); /* 继续指示上层节点把返回的键值+子树插入其中 */
	}
//-------------------------------------------
	/*
	* 建立有两个子树和一个键的根节点
	*/
	void NewRoot(BTNode t)
	{
		System.out.println("enter the method of newroot");
		ObjectOutputStream outputStream;
		ObjectInputStream inputStream;
	    BTNode temp;
	    temp=new BTNode();
	    temp.d = 1;
	    if(t==null)
	    {
	    	temp.p[0]=0;
	    	System.out.println("t is null");
	    }
	    else
	    {
	        temp.p[0] = t.position;
	    System.out.println("t.position:"+t.position);
	    System.out.println("t.d"+t.d);}
	    temp.p[1] = NewTree;
	    temp.key[0] = InsKey;
	    temp.addr[0] = InsAddr;
	    System.out.println("InsAddr:"+InsAddr);
	    temp.inneraddr[0]=InnerAddr;
	    System.out.println("InnerAddr:"+InnerAddr);
	    int i;
	    try
	    {
	    	System.out.println("enter the try block");
	    	inputStream=new ObjectInputStream(new FileInputStream(peizhifilename));
	    	BitMap bitmap=(BitMap)inputStream.readObject();
	    	//readtimes++;
	    	//System.out.println("读一次");
	    	boolean[] bits=bitmap.getArray();
	    	inputStream.close();
	    	System.out.println("read the peizhi file to get its content");
	    	for(i=datablocksum;i<bits.length;i++)
	    	{
	    		System.out.println("enter the loop");
	    		if(bits[i]==false)
	    		{
	    			System.out.println("bits["+i+"] is false");
	    			bits[i]=true;
	    			i=i-datablocksum;
	    			outputStream=new ObjectOutputStream(new FileOutputStream(indexfilename+Integer.toString(++i)));
	    			System.out.println("new an indexfile");
	    			temp.position=i;
	    	    	outputStream.writeObject(temp);
	    	    	writetimes++;
	    	    	System.out.println("&&&&&&一次写");
	    	    	System.out.println("write a btnode into the indexfile");
	    	    	outputStream.close();
	    	    	break;
	    		}	
	    	}
	    	//更新配置文件
	    	System.out.println("更新配置文件");
	    	outputStream=new ObjectOutputStream(new FileOutputStream(peizhifilename));
	    	bitmap.reset(bits);
	    	System.out.println("bits[20"+bits[20]);
	    	outputStream.writeObject(bitmap);
	    	//writetimes++;
	    	//System.out.println("&&&&&&一次写");
	    	outputStream.close();
	    	//更新模式文件
	    	System.out.println("更新模式文件");
	    	BufferedReader inStream=new BufferedReader(new FileReader(modefilename1));
	    	String s=inStream.readLine();
	    	//readtimes++;
	    	//System.out.println("读一次");
	    	inStream.close();
	    	String[] sarray=stringtoarray(s,moderecordwidth);
	    	sarray[3]=Integer.toString(i);
	    	String newmode=sarray[0];	    	
	    	for(int x=1;x<sarray.length;x++)
	    		newmode=newmode+"-"+sarray[x];
	    	PrintWriter outStream=new PrintWriter(new FileOutputStream(modefilename1));
	    	outStream.println(newmode);
	    	//writetimes++;
	    	//System.out.println("&&&&&&一次写");
	    	System.out.println("经方法newroot后模式文件:"+newmode);
	    	outStream.close();
	    }
	    catch(Exception e)
	    {
	    	System.out.println("newroot时发生错误"+"\n"+e.getMessage());
	    }
	}
	//----------------------------------------
	/*
	* 把一个键和对应的右子树插入一个节点中
	*/
	void InsInNode(BTNode t, int pos)
	{
		System.out.println("enter the method of InsInNode");
	    int i = t.d;
	    /* 把所有大于要插入的键值的键和对应的右子树右移 */
	    for(; i >pos; i--)
	    {
	        t.key[ i ] = t.key[i-1];
	        t.addr[ i ] = t.addr[i-1];
	        t.inneraddr[i]=t.inneraddr[i-1];
	        t.p[i+1] = t.p[ i ];
	    }
	    /* 插入键和右子树 */
	    t.key[ i ] = InsKey;
	    t.p[i+1] = NewTree;
	    t.addr[ i ] = InsAddr;
	    System.out.println("t.addr["+i+"]:"+t.addr[i]);
	    t.inneraddr[i]=InnerAddr;
	    System.out.println("t.inneraddr["+i+"]:"+t.inneraddr[i]);
	    System.out.println("node curpos:"+t.position);
	    t.d++;
	    try
	    {
	    	ObjectOutputStream outStream=new ObjectOutputStream(new FileOutputStream(indexfilename+Integer.toString(t.position)));
			outStream.writeObject(t);
			writetimes++;
			System.out.println("&&&&&&一次写");
			outStream.close();
	    }
	    catch(Exception e)
	    {
	    	System.out.println("InsInNode方法发生错误");
	    }
	    System.out.println("退出InsInNode方法");
	}
	/*
	* 前件是要插入一个键和对应的右子树，并且本节点已经满
	* 导致分割这个节点，插入键和对应的右子树，
	* 并向上层返回一个要插入键和对应的右子树
	*/
	
	void SplitNode(BTNode t, int pos)//会导致索引首地址的改变
	{   
		System.out.println("enter the method of SplitNode");
		ObjectOutputStream outputStream;
		ObjectInputStream inputStream;
		int center=(max+1)/2-1;
	    int i,j;
	    BTNode temp;
	    long temp_k;
	    int temp_addr;
	    int temp_inneraddr;
	    /* 建立新节点 */
	    temp = new BTNode();
	    /*
	     *   +---+--------+-----+-----+--------+-----+
	     *   | 0 | ...... |  c  | M+1 | ...... |2*M-1|
	     *   +---+--------+-----+-----+--------+-----+
	     *   |<-      M+1     ->|<-        M-1     ->|  
	     */
	    /* 要插入当前节点的右半部分 */
	    if (pos > center) 
	    { 
	        /* 把从 2*M-1 到 M+1 的 M-1 个键值+子树对转移到新节点中,
	         * 并且为要插入的键值+子树空出位置 */
	        for(i=max-2,j=center-1; i>=pos; i--,j--)
	        {
	            temp.key[j] = t.key[ i ];
	            temp.addr[j] = t.addr[ i ];
	            temp.inneraddr[j]=t.inneraddr[i];
	            temp.p[j+1] = t.p[i+1];
	        }
	        for(i=pos-1,j=pos-center-2; j>=0; i--,j--) 
	        {
	            temp.key[j] = t.key[ i ];
	            temp.addr[j] = t.addr[ i ];
	            temp.inneraddr[j]=t.inneraddr[i];
	            temp.p[j+1] = t.p[i+1];
	        }
	        /* 把节点的最右子树转移成新节点的最左子树 */
	        temp.p[0] = t.p[center+1];
	        /* 在新节点中插入键和右子树 */
	        temp.key[pos-center-1] = InsKey;
	        temp.p[pos-center] = NewTree;
	        temp.addr[pos-center-1] = InsAddr;
	        temp.inneraddr[pos-center-1] = InnerAddr;
	        /* 设置要插入上层节点的键和值 */
	        InsKey = t.key[center];
	        InsAddr = t.addr[center];
	        InnerAddr=t.inneraddr[center];

	    }
	    else 
	    { 
	        /* 把从 2*M-1 到 M 的 M 个键值+子树对转移到新节点中 */
	        for(i=max-2,j=center-1; j>=0; i--,j--) 
	        {
	            temp.key[j] = t.key[ i ];
	            temp.addr[j] = t.addr[ i ];
	            temp.inneraddr[j]=t.inneraddr[i];
	            temp.p[j+1] = t.p[i+1];
	        }
	        if (pos == center) /* 要插入当前节点的正中间 */
	            /* 把要插入的子树作为新节点的最左子树 */
	            temp.p[0] = NewTree;
	            /* 直接把要插入的键和值返回给上层节点 */
	        else /* (d<M) 要插入当前节点的左半部分 */
	            /* 把节点当前的最右子树转移成新节点的最左子树 */
	        { 
	            temp.p[0] = t.p[center];
	            /* 保存要插入上层节点的键和值 */
	            temp_k = t.key[center-1];
	            temp_addr = t.addr[center-1];
	            temp_inneraddr=t.inneraddr[center-1];
	            /* 把所有大于要插入的键值的键和对应的右子树右移 */
	            for(i=center-1; i>pos; i--) 
	            {
	                t.key[ i ] = t.key[i-1];
	                t.addr[ i ] = t.addr[i-1];
	                t.inneraddr[i]=t.inneraddr[i-1];
	                t.p[i+1] = t.p[ i ];
	            }
	            /* 在节点中插入键和右子树 */
	            t.key[pos] = InsKey;
	            t.p[pos+1] = NewTree;
	            t.addr[pos] = InsAddr;
	            t.inneraddr[pos]=InnerAddr;
	            /* 设置要插入上层节点的键和值 */
	            InsKey = temp_k;
	            InsAddr = temp_addr;
	            InnerAddr=temp_inneraddr;
	        }
	    }
	    t.d =center;
	    temp.d = center;	    
	    try
	    {
	    	inputStream=new ObjectInputStream(new FileInputStream(peizhifilename));
	    	BitMap bitmap=(BitMap)inputStream.readObject();
	    	//readtimes++;
	    	//System.out.println("读一次");
	    	boolean[] bits=bitmap.getArray();
	    	inputStream.close();
	    	for(i=datablocksum;i<bits.length;i++)
	    		if(bits[i]==false)
	    		{
	    			bits[i]=true;
	    			i=i-datablocksum;
	    			i++;
	    			temp.position=i;
	    			System.out.println("5要插入的位置"+temp.position);
	    			System.out.println("方法splitnode读索引文件："+indexfilename+Integer.toString(i));
	    			NewTree=i;
	    			outputStream=new ObjectOutputStream(new FileOutputStream(indexfilename+i));
	    	    	outputStream.writeObject(temp);
	    	    	writetimes++;
	    	    	System.out.println("&&&&&&一次写");
	    	    	outputStream.close();
	    	    	outputStream=new ObjectOutputStream(new FileOutputStream(indexfilename+Integer.toString(t.position)));
	    	    	outputStream.writeObject(t);
	    	    	writetimes++;
	    	    	System.out.println("&&&&&&一次写");
	    	    	outputStream.close();
	    	    	break;
	    		}	
	    	outputStream=new ObjectOutputStream(new FileOutputStream(peizhifilename));
	    	bitmap.reset(bits);
	    	outputStream.writeObject(bitmap);
	    	//writetimes++;
	    	//System.out.println("&&&&&&一次写");
	    	outputStream.close();
	    	
	    }
	    catch(Exception e)
	    {
	    	System.out.println("节点裂变时发生错误");
	    }
	    
	}
	
	//#####################################-----删除-----###########################################
	public void delete(long k)
	{
		System.out.println("enter the method of delete");
		BTNode t=null;
		ObjectInputStream inputStream;
		BufferedReader input;
		try
	    {						
			//读取模式文件，获得索引首地址
	    	input=new BufferedReader(new FileReader(modefilename1));
	    	String s=input.readLine();
	    	//readtimes++;
	    	//System.out.println("读一次");
	    	System.out.println("模式文件内容为："+s);
	    	input.close();
	    	int firstsign=s.indexOf("-");
	    	s=s.substring(firstsign+1);
	    	int secondsign=s.indexOf("-");
	    	s=s.substring(secondsign+1);
	    	int thirdsign=s.indexOf("-");
	    	s=s.substring(thirdsign+1);
	    	int fourthsign=s.indexOf("-");
	    	int firstindex=Integer.parseInt(s.substring(0, fourthsign));
	    if(firstindex!=0)
	    {
			inputStream=new ObjectInputStream(new FileInputStream(indexfilename+firstindex));
			System.out.println("firstindex"+firstindex);
			t=(BTNode)inputStream.readObject();
			readtimes++;
			System.out.println("读一次");
			inputStream.close();
	    }
			try{				
		    InternalDelete(k, t);
		    input=new BufferedReader(new FileReader(modefilename1));
		    String mode=input.readLine();
		    //readtimes++;
		    //System.out.println("读一次");
		    input.close();
		    int firsteof=mode.indexOf("-");
		    int keysum=Integer.parseInt(mode.substring(0,firsteof));
		    keysum=keysum+count;
		    mode=keysum+mode.substring(firsteof);
		    PrintWriter out=new PrintWriter(new FileOutputStream(modefilename1));
		    out.println(mode);
		    //writetimes++;
		    //System.out.println("&&&&&&一次写");
		    out.close();
			}
			catch(NokeyException e)
			{
				JOptionPane.showMessageDialog(null,e.getMessage());
			}
		    if (t.d == 0)
		    /* 根节点的子节点合并导致根节点键的数目随之减少，
		     * 当根节点中没有键的时候，只有它的最左子树可能非空 */
		        FreeRoot();
		    JOptionPane.showMessageDialog(null,"删除完毕"+"\n"+"硬盘读次数："+readtimes+"\n"+"硬盘写次数"+writetimes);
	    }
	    catch(Exception e)
	    {
	    	System.out.println("索引文件删除时发生错误"+e.getMessage());
	    }
	}
	public void InternalDelete(long k,BTNode t) throws NokeyException//方法开始前首先把parent与curpos回归初始值
	{
		System.out.println("enter the method of InternalDelete");
		RandomAccessFile ranfile;
	    int i,j,m=0;
	    BTNode l,r;	  
	    if(t==null)
	    {
			        throw new NokeyException(); /* 在整个树中未找到要删除的键 */
			        //flag = 0;
			        //return;
	    }
	    for(i=0, j=t.d-1; i<j; m=(j+i)/2)
	    {
	    	if(k>t.key[m])
	    		i=m+1;
	    	else
	    		j=m;
	    }
	    if (k==t.key[ i ])/* 找到要删除的键 */
	    { 
	    	count--;
	        if (t.addr[ i ] != 0)/* 释放这个节点包含的值 */
	        {
	        	System.out.println("这个节点对应有值");
	          try
	          {
	        	ranfile=new RandomAccessFile(datafilename,"rw");
				ranfile.seek(datablockbytes*(t.addr[i]-1));
				String s=ranfile.readLine();
				readtimes++;
				System.out.println("读一次");
				ranfile.close();
                s=s.substring(0, t.inneraddr[i]-1)+"0"+s.substring(t.inneraddr[i])+"\n";
                System.out.println("if delete key "+k+"the new shujukuaijilubiao is"+s);
                ranfile=new RandomAccessFile(datafilename,"rw");
				ranfile.seek(datablockbytes*(t.addr[i]-1));
				ranfile.write(s.getBytes());
				writetimes++;
				System.out.println("&&&&&&一次写");
				ranfile.close();
				System.out.println("internaldelete方法中删除要删除的记录完毕");
                int numof1=0;
                for(int x=0;x<s.length()-1;x++)
                {
                	if(s.charAt(x)=='1')
                		numof1++;
                }
                if(numof1==0)//该数据块已空
                {
                	System.out.println("删除后数据块变空");
//                	更新配置文件，把数据块相应位置置0
    				ObjectInputStream input=new ObjectInputStream(new FileInputStream(peizhifilename));
    				BitMap bitmap=(BitMap)input.readObject();
    				//readtimes++;
    				//System.out.println("读一次");
    				input.close();
    				boolean[] bits=bitmap.getArray();
    				bits[t.addr[i]-1]=false;
    				bitmap.reset(bits);
    				ObjectOutputStream output=new ObjectOutputStream(new FileOutputStream(peizhifilename));
    				output.writeObject(bitmap);
    				//writetimes++;
    				//System.out.println("&&&&&&一次写");
    				output.close();
                	//更新模式文件，从空闲列表中删除
                	ranfile=new RandomAccessFile(modefilename1,"rw");
    				s=ranfile.readLine();
    				//readtimes++;
    				//System.out.println("读一次");
    				ranfile.close();
    				int lastsign=s.lastIndexOf("-");
    				String subs=s.substring(lastsign+1);
    				if(subs.indexOf(",")==-1)
    					subs="";
    				else
    				{
    					int x=0,y=0;
    					while(x<subs.length()&&x!=-1)
    					{
    						x=subs.indexOf(",");
    						if(Integer.parseInt(subs.substring(y, x))==t.addr[i])
    							subs=subs.substring(0, y)+subs.substring(x+1);
    						y=x;
    					}
    				}
    				s=s.substring(0, lastsign+1)+subs;
    				System.out.println("new modeinfo :"+s);
    				//更新数据首块地址和数据尾块地址
    				int firstdatablock=0,lastdatablock=0;
    				for(int pointer=t.addr[i];pointer<datablocksum;pointer++)
    					if(bits[pointer]==true)
    					{
    						firstdatablock=pointer+1;
    					}
    				for(int pointer=t.addr[i]-2;pointer>=0;pointer--)
    					if(bits[pointer]==true)
    					{
    						lastdatablock=pointer+1;
    					}
    				String[] modedata=stringtoarray(s,moderecordwidth);
    				if(Integer.parseInt(modedata[4])==t.addr[i])
    					modedata[4]=Integer.toString(firstdatablock);
    				if(Integer.parseInt(modedata[5])==t.addr[i])
    					modedata[5]=Integer.toString(lastdatablock);
    				s=modedata[0];
    				for(int arrayp=1;arrayp<modedata.length;arrayp++)
    					s+="-"+modedata[arrayp];
    				System.out.println("the next newmode info:"+s);
    				PrintWriter out=new PrintWriter(new FileOutputStream(modefilename1));
    				out.println(s);
    				//writetimes++;
    				//System.out.println("&&&&&&一次写");
    				out.close();
                	
                }
                else if(numof1==s.length()-1)//数据块原来满
                {
                	//更新模式文件，添加到空闲列表中
                	ranfile=new RandomAccessFile(modefilename1,"rw");
    				s=ranfile.readLine();
    				//readtimes++;
    				//System.out.println("读一次");
    				s=s+","+t.addr[i];
    				ranfile.writeBytes(s);
    				//writetimes++;
    				//System.out.println("&&&&&&一次写");
    				ranfile.close();
                }
	          }
	          catch(Exception e)
	          {
	        	  System.out.println("Internal 发生问题");
	          }
	        }
	        if(t.p[i]!=0)/* 这个键位于非叶节点,转成叶节点,用其左子树的最大值代替 */
	        {
	            /* 找到前驱节点 */
	        		r = GetChildNode(t,i);
		            while (r.p[r.d]!=0) 
		            {
		                r = GetChildNode(r,r.d);	  
		            }
		            t.key[ i ]=r.key[r.d-1];
		            t.addr[ i ]=r.addr[r.d-1];
		            t.inneraddr[i]=r.inneraddr[r.d-1];
		            r.addr[r.d-1]=0;
		            r.inneraddr[r.d-1]=0;
		            k = r.key[r.d-1];	            
	        }
	        else
	        {
	        	DelFromNode(t,i);
	        	flag=1;
	        	return;
	        }
	   }	       
	        
	    else if (k > t.key[ i ]) /* i == t->d-1 时有可能出现 */
	        i++;  
	    BTNode temp=null;
	    if(t.p[i]!=0)
	    {
	    temp=GetChildNode(t, i);
	    }
	    InternalDelete(k,temp);
	    /* 调整平衡 */
	    if (flag == 0)	   
	    	return;
	    if (temp.d <(max+1)/2) //在关键值key[i]的左子树上发生删除
	    {
	    	System.out.println("temp-d:"+temp.d);
	        if (i == t.d) /* 在最右子树中发生了删除 */
	            i--; /* 调整最右键的左右子树平衡 */
	        System.out.println("i=="+i);
	        l = GetChildNode(t, i);
	        System.out.println("l.k"+l.key[0]);
	        r = GetChildNode(t, i+1);
	        if (r.d >(max+1)/2)
	            MoveLeftNode(t,i);
	        else if(l.d > (max+1)/2)
	            MoveRightNode(t,i);
	        else 
	        {
	            JoinNode(t,i);
	            /* 继续指示上层节点本子树的键数量减少 */
	            return;
	        }
	        flag = 0;
	        /* 指示上层节点本子树的键数量没有减少，删除过程结束 */
	    }
	}
	BTNode GetChildNode(BTNode t,int i)
	{
		System.out.println("enter the method of getchildnode");
		ObjectInputStream input;
		BTNode temp=null;
		try
		{
			System.out.println("t.p[1]"+t.p[i]);
			input=new ObjectInputStream(new FileInputStream(indexfilename+t.p[i]));
			temp=(BTNode)input.readObject();
			readtimes++;
			System.out.println("读一次");
			input.close();
		}
		catch(Exception e)
		{
			System.out.println("获取子节点的方法出现错误"+e.getMessage());
		}
		System.out.println("exit the method of getchildnode");
		return temp;
	}

	/*
	* 合并一个节点的某个键对应的两个子树
	*/
	public void JoinNode(BTNode t, int d)//把节点t的左右子树及t合并成一个节点
	{
		
		System.out.println("enter the method of JoinNode");
		System.out.println("左子树的位置："+d);
	    BTNode l=null,r=null;
	    int i,j;
	    try
		{
	    l = GetChildNode(t, d);
	    r = GetChildNode(t, d+1);
        System.out.println("获得T的左右节点");
        System.out.println("t.key[d]"+t.key[d]+"\n"
        		+"t.addr[d]"+t.addr[d]+"\n"
        		+"t.inner[d]"+t.inneraddr[d]);
        System.out.println("l.key[d]"+l.key[0]+"\n"
        		+"l.addr[d]"+l.addr[0]+"\n"
        		+"l.inner[d]"+l.inneraddr[0]);
        System.out.println("l.d"+l.d);
        if(r!=null)
        System.out.println(r.d);
        else
        	System.out.println("r is null");
        System.out.println("r.key[d]"+r.key[0]+"\n"
        		+"r.addr[d]"+r.addr[0]+"\n"
        		+"r.inner[d]"+r.inneraddr[0]);
	    /* 把这个键下移到它的左子树 */
	    l.key[l.d] = t.key[d];
	    l.addr[l.d] = t.addr[d];
	    l.inneraddr[l.d]=t.inneraddr[d];
	    System.out.println("把这个键下移到它的左子树");
	    /* 把右子树中的所有键值和子树转移到左子树 */
	    for (j=r.d-1,i=l.d+r.d; j >= 0 ; j--,i--) 
	    {
	        l.key[ i ] = r.key[j];
	        l.addr[ i ] = r.addr[j];
	        l.inneraddr[i]=r.inneraddr[j];
	        l.p[ i ] = r.p[j];
	    }
	    System.out.println("把右子树中的所有键值和子树转移到左子树");
	    l.p[l.d+r.d+1] = r.p[r.d];
	    l.d += r.d+1;
	    /* 释放右子树的节点 */
	    System.out.println("释放右节点："+t.p[d+1]);
	    free(t.p[d+1]);
		}
		catch(Exception e1)
		{
			System.out.println("former part error"+e1.getMessage());
		}
	    try//左节点重新写入
	    {
	    	ObjectOutputStream outputStream=new ObjectOutputStream(new FileOutputStream(indexfilename+Integer.toString(t.p[d])));
	    	outputStream.writeObject(l);
	    	writetimes++;
	    	System.out.println("&&&&&&一次写");
	    	outputStream.close();
	    	/* 把这个键右边的键和对应的右子树左移 */
		    for (i=d; i < t.d-1; i++) 
		    {
		        t.key[ i ] = t.key[i+1];
		        t.addr[ i ] = t.addr[i+1];
		        t.inneraddr[i]=t.inneraddr[i+1];
		        t.p[i+1] = t.p[i+2];
		    }
		    t.d--;
		    System.out.println("t节点的位置："+t.position);
		    outputStream=new ObjectOutputStream(new FileOutputStream(indexfilename+Integer.toString(t.position)));
		    outputStream.writeObject(t);
		    writetimes++;
		    System.out.println("&&&&&&一次写");
		    outputStream.close();
		    System.out.println("joinnode method ends");
	    }
	    catch(Exception e)
	    {
	    	System.out.println("joinnode方法出现错误");
	    }
	    
	    
	}
	/*
	* 从一个键的右子树向左子树转移一些键，使两个子树平衡
	*/
	public void MoveLeftNode(BTNode t, int d)
	{
		System.out.println("enter the method of moveleftNode");
	    BTNode l,r;
	    int m; /* 应转移的键的数目 */
	    int i,j;
	    l = GetChildNode(t,d);
	    r = GetChildNode(t,d+1);
	    m = (r.d - l.d)/2;

	    /* 把这个键下移到它的左子树 */
	    l.key[l.d] = t.key[d];
	    l.addr[l.d] = t.addr[d];
	    l.inneraddr[l.d]=t.inneraddr[d];
	    /* 把右子树的最左子树转移成左子树的最右子树
	     * 从右子树向左子树移动 m-1 个键+子树对 */
	    for (j=m-2,i=l.d+m-1; j >= 0; j--,i--)
        {
	        l.key[ i ] = r.key[j];
	        l.addr[ i ] = r.addr[j];
	        l.inneraddr[i]=r.inneraddr[j];
	        l.p[ i ] = r.p[j];
	    }
	    l.p[l.d+m] = r.p[m-1];
	    /* 把右子树的最左键提升到这个键的位置上 */
	    t.key[d] = r.key[m-1];
	    t.addr[d] = r.addr[m-1];
	    t.inneraddr[d]=r.inneraddr[m-1];
	    /* 把右子树中的所有键值和子树左移 m 个位置 */
	    //r.p[0] = r.p[m];
	    for (i=0; i<r.d-m; i++) {
	        r.key[ i ] = r.key[i+m];
	        r.addr[ i ] = r.addr[i+m];
	        r.inneraddr[i]=r.inneraddr[i+m];
	        r.p[ i ] = r.p[i+m];
	    }
	    r.p[r.d-m] = r.p[r.d];
	    l.d+=m;
	    r.d-=m;
	    try
	    {
	    	ObjectOutputStream output=new ObjectOutputStream(new FileOutputStream(indexfilename+Integer.toString(t.position)));
	    	output.writeObject(t);
	    	writetimes++;
	    	System.out.println("&&&&&&一次写");
	    	output.close();
	    	output=new ObjectOutputStream(new FileOutputStream(indexfilename+t.p[d]));
	    	output.writeObject(l);
	    	writetimes++;
	    	System.out.println("&&&&&&一次写");
	    	output.close();
	    	output=new ObjectOutputStream(new FileOutputStream(indexfilename+t.p[d+1]));
	    	output.writeObject(r);
	    	writetimes++;
	    	System.out.println("&&&&&&一次写");
	    	output.close();
	    }
	    catch(Exception e)
	    {
	    	System.out.println("MOveLeftNode方法出现错误");
	    }
	}
	/*
	* 从一个键的左子树向右子树转移一些键，使两个子树平衡
	*/
	public void MoveRightNode(BTNode t, int d)
	{
		System.out.println("enter the method of MoveRightNode");
	    BTNode l,r;
	    int m; /* 应转移的键的数目 */
	    int i,j;
	    l = GetChildNode(t,d);
	    r = GetChildNode(t,d+1);

	    m = (l.d - r.d)/2;
	    /* 把右子树中的所有键值和子树右移 m 个位置 */
	    r.p[r.d+m]=r.p[r.d];
	    for (i=r.d-1; i>=0; i--)
	    {
	        r.key[i+m] = r.key[ i ];
	        r.addr[i+m] = r.addr[ i ];
	        r.inneraddr[i+m]=r.inneraddr[i];
	        r.p[i+m] = r.p[ i ];
	    }
	    /* 把这个键下移到它的右子树 */
	    r.key[m-1] = t.key[d];
	    r.addr[m-1] = t.addr[d];
	    r.inneraddr[m-1]=t.inneraddr[d];
	    /* 把左子树的最右子树转移成右子树的最左子树 */
	    r.p[m-1] = l.p[l.d];
	    /* 从左子树向右子树移动 m-1 个键+子树对 */
	    for (i=l.d-1,j=m-2; j>=0; j--,i--) {
	        r.key[j] = l.key[ i ];
	        r.addr[j] = l.addr[ i ];
	        r.inneraddr[j]=l.inneraddr[i];
	        r.p[j] = l.p[ i ];
	    }
	    /* 把左子树的最右键提升到这个键的位置上 */
	    t.key[d] = l.key[ i ];
	    t.addr[d] = l.addr[ i ];
	    t.inneraddr[d]=l.inneraddr[i];
	    l.d-=m;
	    r.d+=m;
	    try
	    {
	    	ObjectOutputStream output=new ObjectOutputStream(new FileOutputStream(indexfilename+Integer.toString(t.position)));
	    	output.writeObject(t);
	    	writetimes++;
	    	System.out.println("&&&&&&一次写");
	    	output.close();
	    	output=new ObjectOutputStream(new FileOutputStream(indexfilename+t.p[d]));
	    	output.writeObject(l);
	    	writetimes++;
	    	System.out.println("&&&&&&一次写");
	    	output.close();
	    	output=new ObjectOutputStream(new FileOutputStream(indexfilename+t.p[d+1]));
	    	output.writeObject(r);
	    	writetimes++;
	    	System.out.println("&&&&&&一次写");
	    	output.close();
	    }
	    catch(Exception e)
	    {
	    	System.out.println("MOveLeftNode方法出现错误");
	    }
	}
	/*
	* 把一个键和对应的右子树从一个节点中删除
	*/
	public void DelFromNode(BTNode t, int pos)
	{
		System.out.println("enter the method of DelFromNode");
	    int i;
	    /* 把所有大于要删除的键值的键左移 */
	    for(i=pos; i < t.d-1; i++)
	    {
	        t.key[ i ] = t.key[i+1];
	        t.addr[ i ] = t.addr[i+1];
	        t.inneraddr[i]=t.inneraddr[i+1];	        
	    }
	    if(t.p[pos]!=0)
	      free(t.p[pos]);
	    t.d--;
	    ObjectOutputStream outputStream;
	    try
	    {
	    	System.out.println("重写节点t");
	    	System.out.println("t.position"+t.position);
	    	outputStream=new ObjectOutputStream(new FileOutputStream(indexfilename+Integer.toString(t.position)));
	    	outputStream.writeObject(t);
	    	writetimes++;
	    	System.out.println("&&&&&&一次写");
	    	outputStream.close();
	    }
	    catch(Exception e)
	    {
	    	System.out.println("节点裂变时发生错误");
	    }
	}
	
	/*
	* 释放根节点，并保留它的最左子树
	*/
	public void FreeRoot()
	{
		System.out.println("enter the method of FreeRoot");
		ObjectInputStream inputStream;
		ObjectOutputStream outputStream;
		BufferedReader input;
		BTNode t;
		try
		{
//			读取模式文件，获得索引首地址
	    	input=new BufferedReader(new FileReader(modefilename1));
	    	String modeinfo=input.readLine(),s;
	    	//readtimes++;
	    	//System.out.println("读一次");
	    	System.out.println("模式文件内容为："+modeinfo);
	    	input.close();
	    	int firstsign=modeinfo.indexOf("-");
	    	s=modeinfo.substring(firstsign+1);
	    	int secondsign=s.indexOf("-");
	    	s=s.substring(secondsign+1);
	    	int thirdsign=s.indexOf("-");
	    	s=s.substring(thirdsign+1);
	    	int fourthsign=s.indexOf("-");
	    	int firstindex=Integer.parseInt(s.substring(0, fourthsign));
    	inputStream=new ObjectInputStream(new FileInputStream(indexfilename+Integer.toString(firstindex)));
		t=(BTNode)inputStream.readObject();
		readtimes++;
		System.out.println("读一次");
		inputStream.close();
		s=modeinfo.substring(0, firstsign+secondsign+thirdsign+3)+t.p[0]+modeinfo.substring(firstsign+secondsign+thirdsign+fourthsign+3);
		PrintWriter outStream=new PrintWriter(new FileOutputStream(modefilename1));
		outStream.println(s);
		//writetimes++;
		//System.out.println("&&&&&&一次写");
		outStream.close();
		free(t.position);
    	//修改配置文件
    	inputStream=new ObjectInputStream(new FileInputStream(peizhifilename));
    	BitMap bitmap=(BitMap)inputStream.readObject();
    	//readtimes++;
    	//System.out.println("读一次");
    	inputStream.close();
    	boolean[] bits=bitmap.getArray();
    	bits[datablocksum+firstindex-1]=false;
    	bitmap.reset(bits);
    	outputStream=new ObjectOutputStream(new FileOutputStream(peizhifilename));
    	outputStream.writeObject(bitmap);
    	//writetimes++;
    	//System.out.println("&&&&&&一次写");
    	outputStream.close();
		}
		catch(Exception e)
		{
			System.out.println("FreeRoot方法出现错误"+e.getMessage());
		}
	}

	public void Error(int f,long key)
	{
	    if (f==1)
	        System.out.println("Btrees error: Insert %d!\n"+key);
	    else
	    	System.out.println("Btrees error: delete %d!\n"+key);
	}

	//清空所有记录
	public void delall()
	{
		System.out.println("进入delall方法");
		File file;
		try
		{
			//删除所有索引文件
			ObjectInputStream input=new ObjectInputStream(new FileInputStream(peizhifilename));
			
			BitMap bitmap=(BitMap)input.readObject();
			input.close();
			boolean[] bits=new boolean[datablocksum+indexfilesum];
			bits=bitmap.getArray();
			int i;
			for(i=datablocksum;i<bits.length;i++)
			{
				System.out.println("i"+i);
				if(bits[i]==true)
				{
					System.out.println("bits[i]"+i);
					file=new File(indexfilename+Integer.toString(i-datablocksum+1));
					file.delete();
				}
			}
            //删除配置文件及模式文件及数据文件
			file=new File(peizhifilename);
			file.delete();
			file=new File(modefilename1);
			file.delete();
			file=new File(modefilename2);
			file.delete();
			file=new File(datafilename);
			file.delete();
		}
		catch(Exception e)
		{
			System.out.println("deall方法出现错误");
		}
	    
	}
	public void free(int filenum)
	{
		ObjectInputStream input;
		ObjectOutputStream output;
		try
		{
			File file=new File(indexfilename+Integer.toString(filenum));
			file.delete();
			input=new ObjectInputStream(new FileInputStream(peizhifilename));
			BitMap bitmap=(BitMap)input.readObject();
			//readtimes++;
			//System.out.println("读一次");
			input.close();
			boolean[] bits=bitmap.getArray();
			bits[datablocksum+filenum-1]=false;
			bitmap.reset(bits);
			output=new ObjectOutputStream(new FileOutputStream(peizhifilename));
			output.writeObject(bitmap);
			//writetimes++;
			//System.out.println("&&&&&&一次写");
			output.close();
			
		}
		catch(Exception e)
		{
			System.out.println("free方法出现问题");
		}
	}
	public String[] stringtoarray(String info,int length)
    {
    	int x=-1,y;
    	String[] studentinfo=new String[length];
    	int t;
    	for(t=0;t<length-1;t++)
    	{
    		y=x+1;
    		x=info.indexOf("-",y);
    		studentinfo[t]=info.substring(y, x);
    	}
    	studentinfo[length-1]=info.substring(x+1);
    	return studentinfo;
    }
	public int getkeysum()
	{
		int sum=0;
		try
		{
			BufferedReader in=new BufferedReader(new FileReader(modefilename1));
			String line=in.readLine();
			//readtimes++;
			//System.out.println("读一次");
			in.close();
			int firsteof=line.indexOf("-");
			sum=Integer.parseInt(line.substring(0, firsteof));
			return sum;
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		return sum;
		
		
	}
	private class BTNode implements Serializable
	{
		//static int max;//B-树的阶数即非根节点中键的最大数目
		private int d;    /* 节点中键的数目,最大为max-1个 */
		private long[] key;    /* 关键值 */
		private int[] addr;    /* 对应数据文件中的地址，即数据指针 */
		private int[] inneraddr;
		private int[] p;    /* 指向子树的指针 ，即树指针*/
		private int position;//记录节点所处的索引文件的序号
		public BTNode()
		{
			d=0;
			position=0;
			key=new long[max-1];
			addr=new int[max-1];
			inneraddr=new int[max-1];
			p=new int[max];
			for(int i=0;i<max-1;i++)
			{
				key[i]=0;
				addr[i]=0;
				inneraddr[i]=0;
				p[i]=0;
			}
			p[max-1]=0;
		}

	}

}
