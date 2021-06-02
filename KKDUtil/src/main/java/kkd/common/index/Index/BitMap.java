package kkd.common.index.Index;
import java.io.*;
class BitMap implements Serializable
{
   public boolean[] bitmap;
   
   public BitMap(boolean[] bits)
   {
	   bitmap=bits;
   }
   public boolean[] getArray()
   {
	   return bitmap;
   }
   public void reset(boolean[] bits)
   {
	   bitmap=bits;
   }
}
