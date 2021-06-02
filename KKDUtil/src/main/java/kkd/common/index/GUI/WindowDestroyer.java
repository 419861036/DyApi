package kkd.common.index.GUI;
import javax.swing.*;
import java.awt.event.*;
public class WindowDestroyer extends WindowAdapter
{
		public void windowClosing(WindowEvent e)
		{
			int result=JOptionPane.showConfirmDialog(null, "确实要退出","退出确认",0);
			if(result==0)
			   System.exit(0);
			
		}

}
