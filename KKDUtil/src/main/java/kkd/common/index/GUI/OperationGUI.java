package kkd.common.index.GUI;

//import databaseOperationClass.*;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;

import kkd.common.index.Index.BTree;
public class OperationGUI extends JFrame //implements ActionListener
{
	    public final int WIDTH=800;
	    public final int HEIGHT=600;
	    
	    //图书信息输入
	    private JTextField studentID;
	    private JTextField name;
	    //private JTextField birth;
	    private JComboBox year;
	    private JComboBox month;
	    private JComboBox day;
	    private JTextField address;
	    private JTextField college;
	    //private JTextField grade;
	    //private JTextField cla;
	    private JComboBox grade;
	    private JComboBox cla;
	    private JTextArea course;
	    
	    //数据统计
	    private JTextField studentQuantity;
	    private JTextField maxQuantity;
	    //模糊查找
	    private JTextField former;
	    private JTextField later;
	    private JTextArea showdata;
	    //main方法
	    public static void main(String[]args)
	    {
	    	OperationGUI manager=new OperationGUI();
	    	manager.setVisible(true);
	    }
	    
	    //构造方法
	    public OperationGUI()
	    {
	    	JTabbedPane operatePane=new JTabbedPane(JTabbedPane.LEFT);
	    	
	    	//文件索引操作界面
	    	JPanel studentOperation=new JPanel();
	    	studentOperation.setLayout(new BorderLayout());
	    	JPanel messageInput=new JPanel();
	    	messageInput.setLayout(new BoxLayout(messageInput,BoxLayout.Y_AXIS));
	    	JPanel studentIDPanel=new JPanel();
	    	JLabel studentIDLabel=new JLabel("学号              ");
	    	studentIDPanel.add(studentIDLabel);
	    	studentID=new JTextField(15);
	    	studentIDPanel.add(studentID);
	    	JLabel sidlimit=new JLabel("最多输入15位                                                                      ");
	    	studentIDPanel.add(sidlimit);
	    	messageInput.add(studentIDPanel);
	    	JPanel namePanel=new JPanel();
	    	JLabel nameLabel=new JLabel("姓名                ");
	    	namePanel.add(nameLabel);
	    	name=new JTextField(4);
	    	namePanel.add(name);
	    	JLabel nameLimit=new JLabel("姓名最多四个字                                                                                                          ");
	    	namePanel.add(nameLimit);
	    	messageInput.add(namePanel);
	    	
	    	JPanel birthPanel=new JPanel();
	    	JLabel birthLabel=new JLabel("出生日期      ");
	    	birthPanel.add(birthLabel);
	    	Date date=new Date();
	    	String temp=date.toString();
	    	System.out.println(temp);
	    	int nowyear=Integer.parseInt(temp.substring(24));
	    	int[] yearchoice=new int[30];
	    	yearchoice[0]=nowyear-8;
	    	for(int i=1;i<30;i++)
	    		yearchoice[i]=yearchoice[i-1]-1;
	    	String[] years=new String[30];
	    	for(int i=0;i<years.length;i++)
	    		years[i]=Integer.toString(yearchoice[i]);
	    	year=new JComboBox(years);
	    	birthPanel.add(year);
	    	String[] months=new String[12];
	    	for(int i=0;i<months.length;i++)
	    		months[i]=Integer.toString(i+1);
	    	month=new JComboBox(months);
	    	birthPanel.add(month);
	    	String[] days=new String[31];
	    	for(int i=0;i<days.length;i++)
	    		days[i]=Integer.toString(i+1);
	    	day=new JComboBox(days);
	    	birthPanel.add(day);
	    	//birth=new JTextField(8);
	    	//birthPanel.add(birth);
	    	JLabel birthLimit=new JLabel("                                                                                                       ");
	    	birthPanel.add(birthLimit);
	    	messageInput.add(birthPanel);
	    	
	    	JPanel addressPanel=new JPanel();
	    	JLabel addressLabel=new JLabel("家庭所在地    ");
	    	addressPanel.add(addressLabel);
	    	address=new JTextField(20);
	    	addressPanel.add(address);
	    	JLabel addrlimit=new JLabel("最多不超过20个字                                           ");
	    	addressPanel.add(addrlimit);
	    	messageInput.add(addressPanel);
	    	
	    	JPanel collegePanel=new JPanel();
	    	JLabel collegeLabel=new JLabel("院系             ");
	    	collegePanel.add(collegeLabel);
	    	college=new JTextField(6);
	    	collegePanel.add(college);
	    	JLabel collimit=new JLabel("最多不超过6个字                                                                                                    ");
	    	collegePanel.add(collimit);
	    	messageInput.add(collegePanel);
	    	
	    	JPanel gradePanel=new JPanel();
	    	JLabel gradeLabel=new JLabel("年级            ");
	    	gradePanel.add(gradeLabel);
	    	String[] grades=new String[10];
	    	for(int i=0;i<grades.length;i++)
	    		grades[i]=Integer.toString(nowyear-i);
	    	grade=new JComboBox(grades);
	    	gradePanel.add(grade);
	    	//rade=new JTextField(4);
	    	//gradePanel.add(grade);
	    	JLabel gradelimit=new JLabel("                                                                                                                                            ");
	    	gradePanel.add(gradelimit);
	    	messageInput.add(gradePanel);
	    	
	    	JPanel classPanel=new JPanel();
	    	JLabel classLabel=new JLabel("班级              ");
	    	classPanel.add(classLabel);
	    	String[] clas=new String[10];
	    	for(int i=0;i<clas.length;i++)
	    		clas[i]=Integer.toString(i+1);
	    	cla=new JComboBox(clas);
	    	classPanel.add(cla);
	    	//cla=new JTextField(2);
	    	//classPanel.add(cla);
	    	JLabel classlimit=new JLabel("                                                                                                                                            ");
	    	classPanel.add(classlimit);
	    	messageInput.add(classPanel);
	    	
	    	JPanel coursePanel=new JPanel();
	    	JLabel courseLabel=new JLabel("课程与成绩  ");
	    	coursePanel.add(courseLabel);
	    	course=new JTextArea(4,40);
	  		Font label=new Font("宋体",Font.BOLD,20);
	  		course.setFont(label);
	  		JScrollPane courseScrollPane=new JScrollPane(course,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
	  		coursePanel.add(courseScrollPane);
	  		JLabel courselimit=new JLabel("所修课程不超过15门");
	  		coursePanel.add(courselimit);
	    	messageInput.add(coursePanel);
	    	
	    	studentOperation.add(messageInput,BorderLayout.CENTER);
	    	
	    	JPanel buttonPanel=new JPanel();
	    	buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.Y_AXIS));
	  	    buttonPanel.add(Box.createVerticalStrut(100));
	  	    JButton addIntoDatabase=new JButton("      添加      ");
	  	    buttonPanel.add(addIntoDatabase);
	  	    addIntoDatabase.addActionListener(new BookOperationListener());
	  	    JButton deleteFromDatabase=new JButton("   删除一条 ");
	  	    buttonPanel.add(deleteFromDatabase);
	  	    deleteFromDatabase.addActionListener(new BookOperationListener());
	  	    JButton deleteall=new JButton("    初始化    ");
	  	    deleteall.addActionListener(new BookOperationListener());
	  	    buttonPanel.add(deleteall);
	  	    JButton search=new JButton("       查询      ");
	  	    search.addActionListener(new BookOperationListener());
	  	    buttonPanel.add(search);
	  	    JButton motify=new JButton("       修改      ");
	  	    motify.addActionListener(new BookOperationListener());
	  	    buttonPanel.add(motify);
	  	    JButton clear=new JButton("清空文本域");
	  	    clear.addActionListener(new BookOperationListener());
	  	    buttonPanel.add(clear);
	  	    
	  	    studentOperation.add(buttonPanel,BorderLayout.EAST);
	  	    operatePane.add("学生信息操作",studentOperation);
	  	    
	  	    //数据统计
		    JPanel dataStatistics=new JPanel();
		    dataStatistics.setLayout(new BoxLayout(dataStatistics,BoxLayout.Y_AXIS));
		    
		    JPanel studentQuantityPanel=new JPanel();
		    JLabel studentQuantityLabel=new JLabel("学生总数");
		    studentQuantityPanel.add(studentQuantityLabel);
		    studentQuantity=new JTextField(10);
		    studentQuantity.setText("0");
		    studentQuantity.setEditable(false);
		    studentQuantityPanel.add(studentQuantity);
		    dataStatistics.add(studentQuantityPanel);
		    
		    JPanel maxQuantityPanel=new JPanel();
		    JLabel maxQuantityLabel=new JLabel("学生最大数");
		    maxQuantityPanel.add(maxQuantityLabel);
		    maxQuantity=new JTextField(10);
		    maxQuantity.setText("400");
		    maxQuantity.setEditable(false);
		    maxQuantityPanel.add(maxQuantity);
		    dataStatistics.add(maxQuantityPanel);
		    operatePane.add("学生信息统计",dataStatistics);
		    //模糊查找
		   JPanel briefsearchPanel=new JPanel();
		   briefsearchPanel.setLayout(new BorderLayout());
		   
		   JPanel inputPanel=new JPanel();
		   JLabel leftLabel=new JLabel("左界");
		   inputPanel.add(leftLabel);
		   former=new JTextField(15);
		   inputPanel.add(former);
		   JLabel rightLabel=new JLabel("右界");
		   inputPanel.add(rightLabel);
		   later=new JTextField(15);
		   inputPanel.add(later);
		   JButton searchbutton=new JButton("模糊查找");
		   searchbutton.addActionListener(new BookOperationListener());
		   inputPanel.add(searchbutton);
		   briefsearchPanel.add(inputPanel,BorderLayout.NORTH);
		   JPanel showdataPanel=new JPanel();
		   showdata=new JTextArea(20,40);
	  		JScrollPane courseScrollPane2=new JScrollPane(showdata,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
	  		showdataPanel.add(courseScrollPane2);
	  		briefsearchPanel.add(showdataPanel,BorderLayout.CENTER);
	  		operatePane.add("模糊查找",briefsearchPanel);
	  	    //整个界面处理
	  	    setTitle("图书管理系统");
	  	    setSize(WIDTH,HEIGHT);
	  	    Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
			int screenWidth=screenSize.width;
			int screenHeight=screenSize.height;
			Dimension frameSize=getSize();
			int x=(screenWidth-frameSize.width)/2;
			int y=(screenHeight-frameSize.height)/2;
			this.setLocation(x,y);
			this.setResizable(false);
			this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	  	    addWindowListener(new WindowDestroyer());
	  	    Container contentPane=getContentPane();
	  	    contentPane.setLayout(new BorderLayout());
	  	    contentPane.add(operatePane,BorderLayout.CENTER);
	    }
	    public String[] stringtoarray(String info)
	    {
	    	int x=-1,y;
	    	String[] studentinfo=new String[8];
	    	int t;
	    	for(t=0;t<7;t++)
	    	{
	    		y=x+1;
	    		x=info.indexOf("-",y);
	    		studentinfo[t]=info.substring(y, x);
	    	}
	    	studentinfo[7]=info.substring(x+1);
	    	return studentinfo;
	    }
	    public boolean stringalldigit(String s)
	    {
	    	for(int k=0;k<s.length();k++)
	    	{
	    		if(s.charAt(k)!='0'&&s.charAt(k)!='1'&&s.charAt(k)!='2'&&s.charAt(k)!='3'&&s.charAt(k)!='4'&&s.charAt(k)!='5'
	    			&&s.charAt(k)!='6'&&s.charAt(k)!='7'&&s.charAt(k)!='8'&&s.charAt(k)!='9')
	    			return false;	    		
	    	}
	    	return true;
	    }
	    public boolean checklength(String s,int limit)
	    {
	    	byte[] sbyte=s.getBytes();
	    	System.out.println("sbyte.length"+sbyte.length);
	    	if(sbyte.length>limit)
	    		return false;
	    	return true;
	    }
	    public boolean checkdaynum(String year,String month,String day)
	    {
	    	int yearnum=Integer.parseInt(year);
	    	int monthnum=Integer.parseInt(month);
	    	int daynum=Integer.parseInt(day);
	    	if(monthnum==2)
			{
	    		if(yearnum%4!=0||(yearnum%100==0&&yearnum%400!=0))
	    			if(daynum>28)
	    				return false;
			}
	    	return true;
	    }
	    private class BookOperationListener implements ActionListener
	    {
	    	public void actionPerformed(ActionEvent e)
	    	{
	    		String returnmes=null;
	    		String message=e.getActionCommand().trim();
	    		BTree btree=new BTree();	    						
			
				if(message.equals("添加"))
	    		{
					System.out.println("用户按下添加键");
					String[] studentInfo=new String[8];					
		    		studentInfo[0]=studentID.getText().trim();
		    		boolean check1=stringalldigit(studentInfo[0]);
		    		studentInfo[1]=name.getText().trim();//.getText();	
		    		boolean check2=checklength(studentInfo[1],8);
		    		studentInfo[2]=(String)year.getSelectedItem()+(String)month.getSelectedItem()+(String)day.getSelectedItem();
		    		boolean check6=checkdaynum((String)year.getSelectedItem(),(String)month.getSelectedItem(),(String)day.getSelectedItem());
		    		//studentInfo[2]=birth.getText().trim();
		    		studentInfo[3]=address.getText().trim();
		    		boolean check3=checklength(studentInfo[3],40);
		    		studentInfo[4]=college.getText().trim();
		    		boolean check4=checklength(studentInfo[4],12);
		    		studentInfo[5]=(String)grade.getSelectedItem();
		    		studentInfo[6]=(String)cla.getSelectedItem();
		    		//studentInfo[5]=grade.getText().trim();
		    		//studentInfo[6]=cla.getText().trim();
		    		studentInfo[7]=course.getText().trim();
		    		boolean check5=checklength(studentInfo[7],110);
		    	//if(studentInfo[7].length()>49)
		    		System.out.println("check1"+check1+"check2"+check2+"check3"+check3+"check4"+check4+"check5"+check5+"check6"+check6);
		    		if(!check1||!check2||!check3||!check4||!check5||!check6)
		    			JOptionPane.showMessageDialog(null,"记录不符合要求");
		    	  else
		    	  {
		    		boolean should=true;
		    		for(int i=0;i<studentInfo.length;i++)
		    			if(studentInfo[i].equals(""))
		    			{
		    				should=false;
		    				String showmessage="请把信息输全";
	    			        JOptionPane.showMessageDialog(null,showmessage);
	    			        break;
		    			}
		    		if(should)
		    		{
		    			    btree.Insert(studentInfo);
		    			    studentID.setText("");
			    			name.setText("");
			    			//birth.setText("");
			    			address.setText("");
			    			college.setText("");
			    			//grade.setText("");
			    			//cla.setText("");
			    			course.setText("");
			    			studentQuantity.setText(Integer.toString(btree.getkeysum()));
		    		}
		    	 }
	    		}	    			
	    		else if(message.equals("删除一条"))
	    		{
	    			System.out.println("用户按下删除一条键");
	    			if(studentID.getText().trim()==null)
					{
						String outmessage="学号不能为空";
						JOptionPane.showMessageDialog(null,outmessage);
					}
					else
					{
						long k=Long.parseLong(studentID.getText().trim());						
							int result=JOptionPane.showConfirmDialog(null, "确实要删除此项记录","退出确认",0);
							if(result==0)
							  btree.delete(k);
							studentQuantity.setText(Integer.toString(btree.getkeysum()));
					}
	    		}	
	    		else if(message.equals("初始化"))
	    		{
	    			System.out.println("用户按下删除全部键");
	    			int result=JOptionPane.showConfirmDialog(null, "确实要删除全部记录","退出确认",0);
					if(result==0)
					  btree.delall();
					studentQuantity.setText("0");
	    		}
	    		else if(message.equals("清空文本域"))
	    		{
	    			System.out.println("用户按下清除键");
	    			studentID.setText("");
	    			name.setText("");
	    			//birth.setText("");
	    			address.setText("");
	    			college.setText("");
	    			//grade.setText("");
	    			//cla.setText("");
	    			course.setText("");
	    			
	    		}
	    		else if(message.equals("修改"))
	    		{
	    			System.out.println("用户按下修改键");
	    			String[] studentInfo=new String[8];					
		    		studentInfo[0]=studentID.getText().trim();
		    		boolean check1=stringalldigit(studentInfo[0])&&checklength(studentInfo[0],15);
		    		studentInfo[1]=name.getText().trim();//.getText();	
		    		boolean check2=checklength(studentInfo[1],8);
		    		studentInfo[2]=(String)year.getSelectedItem()+(String)month.getSelectedItem()+(String)day.getSelectedItem();
		    		boolean check6=checkdaynum((String)year.getSelectedItem(),(String)month.getSelectedItem(),(String)day.getSelectedItem());
		    		//studentInfo[2]=birth.getText().trim();
		    		studentInfo[3]=address.getText().trim();
		    		boolean check3=checklength(studentInfo[3],40);
		    		studentInfo[4]=college.getText().trim();
		    		boolean check4=checklength(studentInfo[4],12);
		    		studentInfo[5]=(String)grade.getSelectedItem();
		    		studentInfo[6]=(String)cla.getSelectedItem();
		    		//studentInfo[5]=grade.getText().trim();
		    		//studentInfo[6]=cla.getText().trim();
		    		studentInfo[7]=course.getText().trim();
		    		boolean check5=checklength(studentInfo[7],110);
		    	//if(studentInfo[7].length()>49)
		    		System.out.println("check1"+check1+"check2"+check2+"check3"+check3+"check4"+check4+"check5"+check5+"check6"+check6);
		    		if(!check1||!check2||!check3||!check4||!check5||!check6)
		    			JOptionPane.showMessageDialog(null,"记录不符合要求");
		    	    else
	    			   btree.modify(studentInfo);
	    			
	    	    }
	    	else if(message.equals("查询"))
	    	{
	    		System.out.println("用户按下查询键");
	    		if(studentID.getText().equals(""))
				{
					String outmessage="学号不能为空";
					JOptionPane.showMessageDialog(null,outmessage);
				}
				else
				{
					long k=Long.parseLong(studentID.getText().trim());	
					returnmes=btree.search(k);
					System.out.println(returnmes);
					if(returnmes!=null){
					String[] getData=stringtoarray(returnmes);
					studentID.setText(getData[0]);
					name.setText(getData[1]);
					//birth.setText(getData[2]);
					address.setText(getData[3]);
					college.setText(getData[4]);
					//grade.setText(getData[5]);
					//cla.setText(getData[6]);
					course.setText(getData[7]);
					}
					else
					{
						JOptionPane.showMessageDialog(null,"此记录不存在");
					}
				}
	    	}
	    	else if(message.equals("模糊查找"))
	    	{
	    		String formerdata=former.getText();
	    		String laterdata=later.getText();
	    		boolean check1=stringalldigit(formerdata)&&checklength(formerdata,15);
	    		boolean check2=stringalldigit(laterdata)&&checklength(laterdata,15);
	    		if(!check1||!check2)
	    			JOptionPane.showMessageDialog(null,"输入格式不正确");
	    		else
	    		{
	    			long formernum=Long.parseLong(formerdata);
	    			long laternum=Long.parseLong(laterdata);
	    			String[] getdata;//=new String[laternum-form];
	    			getdata=btree.briefsearch(formernum, laternum);
	    			String datastring="";
	    			boolean found=false;
	    			for(int z=0;z<getdata.length;z++)
	    				if(!getdata[z].equals(""))
	    				{
	    				    datastring+=getdata[z]+"\n";
	    				    found=true;
	    				}
	    			System.out.println(getdata[0]+"\n"+getdata[1]+"\n"+getdata[2]);
	    			if(found)
	    			   showdata.setText(datastring);
	    			else
	    				showdata.setText("没有此段的记录");
	    		}
	    	}
	    }	    
	 
	}
}
