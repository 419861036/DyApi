package kkd.common.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * des加密算法
 * @author tanbin
 *
 */
public class DesUtil {

//	private static byte[] iv1 = { (byte) 0x12, (byte) 0x34, (byte) 0x56,  
//		(byte) 0x78, (byte) 0x90, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF };  
	public static void main(String[] args) {
		String s="sss";
		String s1=DesCbcEncode("12345678".getBytes(), s, "12345678", "UTF-8");
		try {
			String s2=DesCbcDecode("12345678".getBytes(), s1.getBytes(), "12345678",  "UTF-8");
			System.out.println(s2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 加密
	 * 
	 * @param  iv1 byte[]
	 *            向量
	 * @param datasource
	 *            byte[]
	 * @param password
	 *            String
	 * @return byte[]
	 */
	public static String DesCbcEncode(byte[] iv1,String datasource, String password,String charset) {
		try {
			
			IvParameterSpec iv = new IvParameterSpec(iv1);  
////			SecureRandom random = new SecureRandom();
			DESKeySpec desKey = new DESKeySpec(password.getBytes());
//			// 创建一个密匙工厂，然后用它把DESKeySpec转换成
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey securekey = keyFactory.generateSecret(desKey);
//			// Cipher对象实际完成加密操作
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
//			// 用密匙初始化Cipher对象
			cipher.init(Cipher.ENCRYPT_MODE, securekey, iv);
			
//			// 现在，获取数据并加密
//			// 正式执行加密操作
			return StringUtil.bytesToHexString(cipher.doFinal(datasource.getBytes(charset)));
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 解密
	 * 
	 * @param src
	 *            byte[]
	 * @param password
	 *            String
	 * @return byte[]
	 * @throws Exception
	 */
	public static String DesCbcDecode(byte[] iv1,byte[] src, String password,String charset) throws Exception {
		// DES算法要求有一个可信任的随机数源
//		SecureRandom random = new SecureRandom();
		IvParameterSpec iv = new IvParameterSpec(iv1);  
		// 创建一个DESKeySpec对象
		DESKeySpec desKey = new DESKeySpec(password.getBytes());
		// 创建一个密匙工厂
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		// 将DESKeySpec对象转换成SecretKey对象
		SecretKey securekey = keyFactory.generateSecret(desKey);
		// Cipher对象实际完成解密操作
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.DECRYPT_MODE, securekey, iv);
		// 真正开始解密操作
		return new String(cipher.doFinal(src),charset);
	}

//	 public static String DESDecode(String encodeStr)
//	  {
//	    String key = "dyytrl68";
//
//	    char[] kchar = new char[8];
//
//	    if (key.length() > 8)
//	    {
//	      key = key.substring(0, 8);
//	    }
//	    key.getChars(0, 8, kchar, 0);
//
//	    int[][] keys = makekey(kchar);
//
//	    char[] cchar = new char[encodeStr.length() / 2];
//	    for (int x = 0; x < encodeStr.length() / 2; x++)
//	    {
//	      String tempStr = encodeStr.substring(x * 2, x * 2 + 2);
//	      int i = Integer.parseInt(tempStr, 16);
//
//	      cchar[x] = (char)i;
//	    }
//	    char[] mchar = new char[cchar.length];
//	    for (int i = 0; i < mchar.length; i++)
//	    {
//	      mchar[i] = '\000';
//	    }
//
//	    int div = cchar.length / 8;
//	    if (cchar.length % 8 != 0) div++;
//	    char[][] divcchar = new char[div][];
//	    for (int i = 0; i < div; i++)
//	    {
//	      divcchar[i] = new char[8];
//	      divcchar[i][0] = cchar[(i * 8 + 0)];
//	      divcchar[i][1] = cchar[(i * 8 + 1)];
//	      divcchar[i][2] = cchar[(i * 8 + 2)];
//	      divcchar[i][3] = cchar[(i * 8 + 3)];
//	      divcchar[i][4] = cchar[(i * 8 + 4)];
//	      divcchar[i][5] = cchar[(i * 8 + 5)];
//	      divcchar[i][6] = cchar[(i * 8 + 6)];
//	      divcchar[i][7] = cchar[(i * 8 + 7)];
//	    }
//
//	    int[][] divmint = new int[div][];
//	    for (int i = 0; i < div; i++)
//	    {
//	      divmint[i] = new int[64];
//	      divmint[i] = DES_1(keys, divcchar[i]);
//	    }
//
//	    int[] mint = new int[div * 64];
//	    for (int i = 0; i < div; i++)
//	    {
//	      for (int j = 0; j < 64; j++)
//	      {
//	        mint[(i * 64 + j)] = divmint[i][j];
//	      }
//
//	    }
//
//	    for (int i = 0; i < mchar.length; i++)
//	    {
////	    	mchar[i];
//	       mchar[i] = (char)0;
//	    }
//	    for (int i = 0; i < mchar.length; i++)
//	    {
//	      if (mint[(i * 8 + 0)] == 1) mchar[i] = (char)(mchar[i] | 0x80);
//	      if (mint[(i * 8 + 1)] == 1) mchar[i] = (char)(mchar[i] | 0x40);
//	      if (mint[(i * 8 + 2)] == 1) mchar[i] = (char)(mchar[i] | 0x20);
//	      if (mint[(i * 8 + 3)] == 1) mchar[i] = (char)(mchar[i] | 0x10);
//	      if (mint[(i * 8 + 4)] == 1) mchar[i] = (char)(mchar[i] | 0x8);
//	      if (mint[(i * 8 + 5)] == 1) mchar[i] = (char)(mchar[i] | 0x4);
//	      if (mint[(i * 8 + 6)] == 1) mchar[i] = (char)(mchar[i] | 0x2);
//	      if (mint[(i * 8 + 7)] == 1) mchar[i] = (char)(mchar[i] | 0x1);
//
//	    }
//
//	    String source = new String(mchar).trim();
//
//	    return source;
//	  }
//
//	  public static String DESEncode(String source)
//	  {
//	    String key = "dyytrl68";
//
//	    char[] tempmchar = new char[source.length()];
//
//	    source.getChars(0, source.length(), tempmchar, 0);
//
//	    int mod = tempmchar.length % 8;
//	    char[] mchar;
//	    if (mod == 0)
//	      mchar = new char[tempmchar.length];
//	    else {
//	      mchar = new char[tempmchar.length + (8 - mod)];
//	    }
//	    Arrays.fill(mchar, ' ');
//	    for (int i = 0; i < tempmchar.length; i++)
//	    {
//	      mchar[i] = tempmchar[i];
//	    }
//
//	    char[] kchar = new char[8];
//	    char[] cchar = new char[mchar.length];
//
//	    if (key.length() > 8)
//	    {
//	      key = key.substring(0, 8);
//	    }
//	    key.getChars(0, key.length(), kchar, 0);
//
//	    int[][] keys = makekey(kchar);
//
//	    int div = mchar.length / 8;
//	    if (mchar.length % 8 != 0) div++;
//	    char[][] divmchar = new char[div][];
//	    for (int i = 0; i < div; i++)
//	    {
//	      divmchar[i] = new char[8];
//	      divmchar[i][0] = mchar[(i * 8 + 0)];
//	      divmchar[i][1] = mchar[(i * 8 + 1)];
//	      divmchar[i][2] = mchar[(i * 8 + 2)];
//	      divmchar[i][3] = mchar[(i * 8 + 3)];
//	      divmchar[i][4] = mchar[(i * 8 + 4)];
//	      divmchar[i][5] = mchar[(i * 8 + 5)];
//	      divmchar[i][6] = mchar[(i * 8 + 6)];
//	      divmchar[i][7] = mchar[(i * 8 + 7)];
//	    }
//
//	    int[][] divcint = new int[div][];
//	    for (int i = 0; i < div; i++)
//	    {
//	      divcint[i] = new int[64];
//	      divcint[i] = DES(keys, divmchar[i]);
//	    }
//
//	    int[] cint = new int[div * 64];
//	    for (int i = 0; i < div; i++)
//	    {
//	      for (int j = 0; j < 64; j++)
//	      {
//	        cint[(i * 64 + j)] = divcint[i][j];
//	      }
//
//	    }
//
//	    for (int i = 0; i < cchar.length; i++)
//	    {
////	    	cchar[i];
//	       cchar[i] = (char)0;
//	    }
//	    for (int i = 0; i < cchar.length; i++)
//	    {
//	      if (cint[(i * 8 + 0)] == 1) cchar[i] = (char)(cchar[i] | 0x80);
//	      if (cint[(i * 8 + 1)] == 1) cchar[i] = (char)(cchar[i] | 0x40);
//	      if (cint[(i * 8 + 2)] == 1) cchar[i] = (char)(cchar[i] | 0x20);
//	      if (cint[(i * 8 + 3)] == 1) cchar[i] = (char)(cchar[i] | 0x10);
//	      if (cint[(i * 8 + 4)] == 1) cchar[i] = (char)(cchar[i] | 0x8);
//	      if (cint[(i * 8 + 5)] == 1) cchar[i] = (char)(cchar[i] | 0x4);
//	      if (cint[(i * 8 + 6)] == 1) cchar[i] = (char)(cchar[i] | 0x2);
//	      if (cint[(i * 8 + 7)] == 1) cchar[i] = (char)(cchar[i] | 0x1);
//
//	    }
//
//	    StringBuilder ret = new StringBuilder();
//
//	    for (int n = 0; n < cchar.length; n++)
//	    {
//	      byte tempByte = (byte)cchar[n];
//	      ret.append(String.format("%1$02X", new Object[] { Byte.valueOf(tempByte) }));
//	    }
//
//	    String code = ret.toString();
//
//	    return code;
//	  }
//
//	  private static int[][] makekey(char[] kchar)
//	  {
//	    int[][] keyreturn = new int[16][];
//	    int[] temp = new int[64];
//	    int[] kint = new int[56];
//	    int[] c0 = new int[28];
//	    int[] d0 = new int[28];
//	    int[] c16 = new int[28];
//	    int[] d1 = new int[28];
//	    int[] c1 = new int[28];
//	    int[] d2 = new int[28];
//	    int[] c2 = new int[28];
//	    int[] d3 = new int[28];
//	    int[] c3 = new int[28];
//	    int[] d4 = new int[28];
//	    int[] c4 = new int[28];
//	    int[] d5 = new int[28];
//	    int[] c5 = new int[28];
//	    int[] d6 = new int[28];
//	    int[] c6 = new int[28];
//	    int[] d7 = new int[28];
//	    int[] c7 = new int[28];
//	    int[] d8 = new int[28];
//	    int[] c8 = new int[28];
//	    int[] d9 = new int[28];
//	    int[] c9 = new int[28];
//	    int[] d10 = new int[28];
//	    int[] c10 = new int[28];
//	    int[] d11 = new int[28];
//	    int[] c11 = new int[28];
//	    int[] d12 = new int[28];
//	    int[] c12 = new int[28];
//	    int[] d13 = new int[28];
//	    int[] c13 = new int[28];
//	    int[] d14 = new int[28];
//	    int[] c14 = new int[28];
//	    int[] d15 = new int[28];
//	    int[] c15 = new int[28];
//	    int[] d16 = new int[28];
//	    int[] k1 = new int[48];
//	    int[] k2 = new int[48];
//	    int[] k3 = new int[48];
//	    int[] k4 = new int[48];
//	    int[] k5 = new int[48];
//	    int[] k6 = new int[48];
//	    int[] k7 = new int[48];
//	    int[] k8 = new int[48];
//	    int[] k9 = new int[48];
//	    int[] k10 = new int[48];
//	    int[] k11 = new int[48];
//	    int[] k12 = new int[48];
//	    int[] k13 = new int[48];
//	    int[] k14 = new int[48];
//	    int[] k15 = new int[48];
//	    int[] k16 = new int[48];
//
//	    for (int i = 0; i < kchar.length; i++)
//	    {
//	      if ((kchar[i] & 0x80) != 0)
//	        temp[(i * 8 + 0)] = 1;
//	      if ((kchar[i] & 0x40) != 0)
//	        temp[(i * 8 + 1)] = 1;
//	      if ((kchar[i] & 0x20) != 0)
//	        temp[(i * 8 + 2)] = 1;
//	      if ((kchar[i] & 0x10) != 0)
//	        temp[(i * 8 + 3)] = 1;
//	      if ((kchar[i] & 0x8) != 0)
//	        temp[(i * 8 + 4)] = 1;
//	      if ((kchar[i] & 0x4) != 0)
//	        temp[(i * 8 + 5)] = 1;
//	      if ((kchar[i] & 0x2) != 0)
//	        temp[(i * 8 + 6)] = 1;
//	      if ((kchar[i] & 0x1) != 0) {
//	        temp[(i * 8 + 7)] = 1;
//	      }
//	    }
//
//	    kint[0] = temp[56];
//	    kint[1] = temp[48];
//	    kint[2] = temp[40];
//	    kint[3] = temp[32];
//	    kint[4] = temp[24];
//	    kint[5] = temp[16];
//	    kint[6] = temp[8];
//	    kint[7] = temp[0];
//	    kint[8] = temp[57];
//	    kint[9] = temp[49];
//	    kint[10] = temp[41];
//	    kint[11] = temp[33];
//	    kint[12] = temp[25];
//	    kint[13] = temp[17];
//	    kint[14] = temp[9];
//	    kint[15] = temp[1];
//	    kint[16] = temp[58];
//	    kint[17] = temp[50];
//	    kint[18] = temp[42];
//	    kint[19] = temp[34];
//	    kint[20] = temp[26];
//	    kint[21] = temp[18];
//	    kint[22] = temp[10];
//	    kint[23] = temp[2];
//	    kint[24] = temp[59];
//	    kint[25] = temp[51];
//	    kint[26] = temp[43];
//	    kint[27] = temp[35];
//	    kint[28] = temp[62];
//	    kint[29] = temp[54];
//	    kint[30] = temp[46];
//	    kint[31] = temp[38];
//	    kint[32] = temp[30];
//	    kint[33] = temp[22];
//	    kint[34] = temp[14];
//	    kint[35] = temp[6];
//	    kint[36] = temp[61];
//	    kint[37] = temp[53];
//	    kint[38] = temp[45];
//	    kint[39] = temp[37];
//	    kint[40] = temp[29];
//	    kint[41] = temp[21];
//	    kint[42] = temp[13];
//	    kint[43] = temp[5];
//	    kint[44] = temp[60];
//	    kint[45] = temp[52];
//	    kint[46] = temp[44];
//	    kint[47] = temp[36];
//	    kint[48] = temp[28];
//	    kint[49] = temp[20];
//	    kint[50] = temp[12];
//	    kint[51] = temp[4];
//	    kint[52] = temp[27];
//	    kint[53] = temp[19];
//	    kint[54] = temp[11];
//	    kint[55] = temp[3];
//
//	    for (int i = 0; i < 28; i++)
//	    {
//	      c0[i] = kint[i];
//	      d0[i] = kint[(i + 28)];
//	    }
//
//	    for (int i = 1; i < 28; i++)
//	    {
//	      c1[(i - 1)] = c0[i];
//	      d1[(i - 1)] = d0[i];
//	    }
//	    c1[27] = c0[0];
//	    d1[27] = d0[0];
//
//	    for (int i = 1; i < 28; i++)
//	    {
//	      c2[(i - 1)] = c1[i];
//	      d2[(i - 1)] = d1[i];
//	    }
//	    c2[27] = c1[0];
//	    d2[27] = d1[0];
//
//	    for (int i = 2; i < 28; i++)
//	    {
//	      c3[(i - 2)] = c2[i];
//	      d3[(i - 2)] = d2[i];
//	    }
//	    c3[26] = c2[0];
//	    c3[27] = c2[1];
//
//	    d3[26] = d2[0];
//	    d3[27] = d2[1];
//
//	    for (int i = 2; i < 28; i++)
//	    {
//	      c4[(i - 2)] = c3[i];
//	    }
//	    c4[26] = c3[0];
//	    c4[27] = c3[1];
//	    for (int i = 2; i < 28; i++)
//	    {
//	      d4[(i - 2)] = d3[i];
//	    }
//	    d4[26] = d3[0];
//	    d4[27] = d3[1];
//
//	    for (int i = 2; i < 28; i++)
//	    {
//	      c5[(i - 2)] = c4[i];
//	    }
//	    c5[26] = c4[0];
//	    c5[27] = c4[1];
//	    for (int i = 2; i < 28; i++)
//	    {
//	      d5[(i - 2)] = d4[i];
//	    }
//	    d5[26] = d4[0];
//	    d5[27] = d4[1];
//
//	    for (int i = 2; i < 28; i++)
//	    {
//	      c6[(i - 2)] = c5[i];
//	    }
//	    c6[26] = c5[0];
//	    c6[27] = c5[1];
//	    for (int i = 2; i < 28; i++)
//	    {
//	      d6[(i - 2)] = d5[i];
//	    }
//	    d6[26] = d5[0];
//	    d6[27] = d5[1];
//
//	    for (int i = 2; i < 28; i++)
//	    {
//	      c7[(i - 2)] = c6[i];
//	    }
//	    c7[26] = c6[0];
//	    c7[27] = c6[1];
//	    for (int i = 2; i < 28; i++)
//	    {
//	      d7[(i - 2)] = d6[i];
//	    }
//	    d7[26] = d6[0];
//	    d7[27] = d6[1];
//
//	    for (int i = 2; i < 28; i++)
//	    {
//	      c8[(i - 2)] = c7[i];
//	    }
//	    c8[26] = c7[0];
//	    c8[27] = c7[1];
//	    for (int i = 2; i < 28; i++)
//	    {
//	      d8[(i - 2)] = d7[i];
//	    }
//	    d8[26] = d7[0];
//	    d8[27] = d7[1];
//
//	    for (int i = 1; i < 28; i++)
//	    {
//	      c9[(i - 1)] = c8[i];
//	    }
//	    c9[27] = c8[0];
//	    for (int i = 1; i < 28; i++)
//	    {
//	      d9[(i - 1)] = d8[i];
//	    }
//	    d9[27] = d8[0];
//
//	    for (int i = 2; i < 28; i++)
//	    {
//	      c10[(i - 2)] = c9[i];
//	    }
//	    c10[26] = c9[0];
//	    c10[27] = c9[1];
//	    for (int i = 2; i < 28; i++)
//	    {
//	      d10[(i - 2)] = d9[i];
//	    }
//	    d10[26] = d9[0];
//	    d10[27] = d9[1];
//
//	    for (int i = 2; i < 28; i++)
//	    {
//	      c11[(i - 2)] = c10[i];
//	    }
//	    c11[26] = c10[0];
//	    c11[27] = c10[1];
//	    for (int i = 2; i < 28; i++)
//	    {
//	      d11[(i - 2)] = d10[i];
//	    }
//	    d11[26] = d10[0];
//	    d11[27] = d10[1];
//
//	    for (int i = 2; i < 28; i++)
//	    {
//	      c12[(i - 2)] = c11[i];
//	    }
//	    c12[26] = c11[0];
//	    c12[27] = c11[1];
//	    for (int i = 2; i < 28; i++)
//	    {
//	      d12[(i - 2)] = d11[i];
//	    }
//	    d12[26] = d11[0];
//	    d12[27] = d11[1];
//
//	    for (int i = 2; i < 28; i++)
//	    {
//	      c13[(i - 2)] = c12[i];
//	    }
//	    c13[26] = c12[0];
//	    c13[27] = c12[1];
//	    for (int i = 2; i < 28; i++)
//	    {
//	      d13[(i - 2)] = d12[i];
//	    }
//	    d13[26] = d12[0];
//	    d13[27] = d12[1];
//
//	    for (int i = 2; i < 28; i++)
//	    {
//	      c14[(i - 2)] = c13[i];
//	    }
//	    c14[26] = c13[0];
//	    c14[27] = c13[1];
//	    for (int i = 2; i < 28; i++)
//	    {
//	      d14[(i - 2)] = d13[i];
//	    }
//	    d14[26] = d13[0];
//	    d14[27] = d13[1];
//
//	    for (int i = 2; i < 28; i++)
//	    {
//	      c15[(i - 2)] = c14[i];
//	    }
//	    c15[26] = c14[0];
//	    c15[27] = c14[1];
//	    for (int i = 2; i < 28; i++)
//	    {
//	      d15[(i - 2)] = d14[i];
//	    }
//	    d15[26] = d14[0];
//	    d15[27] = d14[1];
//
//	    for (int i = 1; i < 28; i++)
//	    {
//	      c16[(i - 1)] = c15[i];
//	    }
//	    c16[27] = c15[0];
//	    for (int i = 1; i < 28; i++)
//	    {
//	      d16[(i - 1)] = d15[i];
//	    }
//	    d16[27] = d15[0];
//
//	    k1 = PC2(c1, d1);
//	    k2 = PC2(c2, d2);
//	    k3 = PC2(c3, d3);
//	    k4 = PC2(c4, d4);
//	    k5 = PC2(c5, d5);
//	    k6 = PC2(c6, d6);
//	    k7 = PC2(c7, d7);
//	    k8 = PC2(c8, d8);
//	    k9 = PC2(c9, d9);
//	    k10 = PC2(c10, d10);
//	    k11 = PC2(c11, d11);
//	    k12 = PC2(c12, d12);
//	    k13 = PC2(c13, d13);
//	    k14 = PC2(c14, d14);
//	    k15 = PC2(c15, d15);
//	    k16 = PC2(c16, d16);
//
//	    keyreturn[0] = ((int[])k1.clone());
//	    keyreturn[1] = ((int[])k2.clone());
//	    keyreturn[2] = ((int[])k3.clone());
//	    keyreturn[3] = ((int[])k4.clone());
//	    keyreturn[4] = ((int[])k5.clone());
//	    keyreturn[5] = ((int[])k6.clone());
//	    keyreturn[6] = ((int[])k7.clone());
//	    keyreturn[7] = ((int[])k8.clone());
//	    keyreturn[8] = ((int[])k9.clone());
//	    keyreturn[9] = ((int[])k10.clone());
//	    keyreturn[10] = ((int[])k11.clone());
//	    keyreturn[11] = ((int[])k12.clone());
//	    keyreturn[12] = ((int[])k13.clone());
//	    keyreturn[13] = ((int[])k14.clone());
//	    keyreturn[14] = ((int[])k15.clone());
//	    keyreturn[15] = ((int[])k16.clone());
//
//	    return keyreturn;
//	  }
//
//	  private static int[] DES_1(int[][] keys, char[] r)
//	  {
//	    int[] L0 = new int[32];
//	    int[] R0 = new int[32];
//	    int[] L1 = new int[32];
//	    int[] R1 = new int[32];
//	    int[] L2 = new int[32];
//	    int[] R2 = new int[32];
//	    int[] L3 = new int[32];
//	    int[] R3 = new int[32];
//	    int[] L4 = new int[32];
//	    int[] R4 = new int[32];
//	    int[] L5 = new int[32];
//	    int[] R5 = new int[32];
//	    int[] L6 = new int[32];
//	    int[] R6 = new int[32];
//	    int[] L7 = new int[32];
//	    int[] R7 = new int[32];
//	    int[] L8 = new int[32];
//	    int[] R8 = new int[32];
//	    int[] L9 = new int[32];
//	    int[] R9 = new int[32];
//	    int[] L10 = new int[32];
//	    int[] R10 = new int[32];
//	    int[] L11 = new int[32];
//	    int[] R11 = new int[32];
//	    int[] L12 = new int[32];
//	    int[] R12 = new int[32];
//	    int[] L13 = new int[32];
//	    int[] R13 = new int[32];
//	    int[] L14 = new int[32];
//	    int[] R14 = new int[32];
//	    int[] L15 = new int[32];
//	    int[] R15 = new int[32];
//	    int[] L16 = new int[32];
//	    int[] R16 = new int[32];
//	    int[] afterG = new int[32];
//	    int[] afterIP = IP(r);
//	    int[] Final = new int[64];
//	    int[] temp = new int[64];
//
//	    for (int i = 0; i < 32; i++)
//	    {
//	      L0[i] = afterIP[i];
//	      R0[i] = afterIP[(i + 32)];
//	    }
//
//	    afterG = g(R0, keys[15]);
//	    for (int i = 0; i < 32; i++)
//	    {
//	      if (L0[i] != afterG[i])
//	        R1[i] = 1;
//	    }
//	    L1 = R0;
//
//	    afterG = g(R1, keys[14]);
//	    for (int i = 0; i < 32; i++)
//	    {
//	      if (L1[i] != afterG[i])
//	        R2[i] = 1;
//	    }
//	    L2 = R1;
//
//	    afterG = g(R2, keys[13]);
//	    for (int i = 0; i < 32; i++)
//	    {
//	      if (L2[i] != afterG[i])
//	        R3[i] = 1;
//	    }
//	    L3 = R2;
//
//	    afterG = g(R3, keys[12]);
//	    for (int i = 0; i < 32; i++)
//	    {
//	      if (L3[i] != afterG[i])
//	        R4[i] = 1;
//	    }
//	    L4 = R3;
//
//	    afterG = g(R4, keys[11]);
//	    for (int i = 0; i < 32; i++)
//	    {
//	      if (L4[i] != afterG[i])
//	        R5[i] = 1;
//	    }
//	    L5 = R4;
//
//	    afterG = g(R5, keys[10]);
//	    for (int i = 0; i < 32; i++)
//	    {
//	      if (L5[i] != afterG[i])
//	        R6[i] = 1;
//	    }
//	    L6 = R5;
//
//	    afterG = g(R6, keys[9]);
//	    for (int i = 0; i < 32; i++)
//	    {
//	      if (L6[i] != afterG[i])
//	        R7[i] = 1;
//	    }
//	    L7 = R6;
//
//	    afterG = g(R7, keys[8]);
//	    for (int i = 0; i < 32; i++)
//	    {
//	      if (L7[i] != afterG[i])
//	        R8[i] = 1;
//	    }
//	    L8 = R7;
//
//	    afterG = g(R8, keys[7]);
//	    for (int i = 0; i < 32; i++)
//	    {
//	      if (L8[i] != afterG[i])
//	        R9[i] = 1;
//	    }
//	    L9 = R8;
//
//	    afterG = g(R9, keys[6]);
//	    for (int i = 0; i < 32; i++)
//	    {
//	      if (L9[i] != afterG[i])
//	        R10[i] = 1;
//	    }
//	    L10 = R9;
//
//	    afterG = g(R10, keys[5]);
//	    for (int i = 0; i < 32; i++)
//	    {
//	      if (L10[i] != afterG[i])
//	        R11[i] = 1;
//	    }
//	    L11 = R10;
//
//	    afterG = g(R11, keys[4]);
//	    for (int i = 0; i < 32; i++)
//	    {
//	      if (L11[i] != afterG[i])
//	        R12[i] = 1;
//	    }
//	    L12 = R11;
//
//	    afterG = g(R12, keys[3]);
//	    for (int i = 0; i < 32; i++)
//	    {
//	      if (L12[i] != afterG[i])
//	        R13[i] = 1;
//	    }
//	    L13 = R12;
//
//	    afterG = g(R13, keys[2]);
//	    for (int i = 0; i < 32; i++)
//	    {
//	      if (L13[i] != afterG[i])
//	        R14[i] = 1;
//	    }
//	    L14 = R13;
//
//	    afterG = g(R14, keys[1]);
//	    for (int i = 0; i < 32; i++)
//	    {
//	      if (L14[i] != afterG[i])
//	        R15[i] = 1;
//	    }
//	    L15 = R14;
//
//	    afterG = g(R15, keys[0]);
//	    for (int i = 0; i < 32; i++)
//	    {
//	      if (L15[i] != afterG[i])
//	        R16[i] = 1;
//	    }
//	    L16 = R15;
//
//	    for (int i = 0; i < 32; i++)
//	    {
//	      temp[i] = R16[i];
//	      temp[(i + 32)] = L16[i];
//	    }
//	    Final = IP_1(temp);
//	    return Final;
//	  }
//
//	  private static int[] DES(int[][] keys, char[] r)
//	  {
//	    int[] L0 = new int[32];
//	    int[] R0 = new int[32];
//	    int[] L1 = new int[32];
//	    int[] R1 = new int[32];
//	    int[] L2 = new int[32];
//	    int[] R2 = new int[32];
//	    int[] L3 = new int[32];
//	    int[] R3 = new int[32];
//	    int[] L4 = new int[32];
//	    int[] R4 = new int[32];
//	    int[] L5 = new int[32];
//	    int[] R5 = new int[32];
//	    int[] L6 = new int[32];
//	    int[] R6 = new int[32];
//	    int[] L7 = new int[32];
//	    int[] R7 = new int[32];
//	    int[] L8 = new int[32];
//	    int[] R8 = new int[32];
//	    int[] L9 = new int[32];
//	    int[] R9 = new int[32];
//	    int[] L10 = new int[32];
//	    int[] R10 = new int[32];
//	    int[] L11 = new int[32];
//	    int[] R11 = new int[32];
//	    int[] L12 = new int[32];
//	    int[] R12 = new int[32];
//	    int[] L13 = new int[32];
//	    int[] R13 = new int[32];
//	    int[] L14 = new int[32];
//	    int[] R14 = new int[32];
//	    int[] L15 = new int[32];
//	    int[] R15 = new int[32];
//	    int[] L16 = new int[32];
//	    int[] R16 = new int[32];
//	    int[] afterG = new int[32];
//
//	    int[] afterIP = IP(r);
//	    int[] Final = new int[64];
//	    int[] temp = new int[64];
//
//	    for (int i = 0; i < 32; i++)
//	    {
//	      L0[i] = afterIP[i];
//	      R0[i] = afterIP[(i + 32)];
//	    }
//
//	    afterG = g(R0, keys[0]);
//	    for (int i = 0; i < 32; i++)
//	    {
//	      if (L0[i] != afterG[i]) {
//	        R1[i] = 1;
//	      }
//	    }
//	    L1 = R0;
//
//	    afterG = g(R1, keys[1]);
//	    for (int i = 0; i < 32; i++)
//	    {
//	      if (L1[i] != afterG[i])
//	        R2[i] = 1;
//	    }
//	    L2 = R1;
//
//	    afterG = g(R2, keys[2]);
//	    for (int i = 0; i < 32; i++)
//	    {
//	      if (L2[i] != afterG[i])
//	        R3[i] = 1;
//	    }
//	    L3 = R2;
//
//	    afterG = g(R3, keys[3]);
//	    for (int i = 0; i < 32; i++)
//	    {
//	      if (L3[i] != afterG[i])
//	        R4[i] = 1;
//	    }
//	    L4 = R3;
//
//	    afterG = g(R4, keys[4]);
//	    for (int i = 0; i < 32; i++)
//	    {
//	      if (L4[i] != afterG[i])
//	        R5[i] = 1;
//	    }
//	    L5 = R4;
//
//	    afterG = g(R5, keys[5]);
//	    for (int i = 0; i < 32; i++)
//	    {
//	      if (L5[i] != afterG[i])
//	        R6[i] = 1;
//	    }
//	    L6 = R5;
//
//	    afterG = g(R6, keys[6]);
//	    for (int i = 0; i < 32; i++)
//	    {
//	      if (L6[i] != afterG[i])
//	        R7[i] = 1;
//	    }
//	    L7 = R6;
//
//	    afterG = g(R7, keys[7]);
//	    for (int i = 0; i < 32; i++)
//	    {
//	      if (L7[i] != afterG[i])
//	        R8[i] = 1;
//	    }
//	    L8 = R7;
//
//	    afterG = g(R8, keys[8]);
//	    for (int i = 0; i < 32; i++)
//	    {
//	      if (L8[i] != afterG[i])
//	        R9[i] = 1;
//	    }
//	    L9 = R8;
//
//	    afterG = g(R9, keys[9]);
//	    for (int i = 0; i < 32; i++)
//	    {
//	      if (L9[i] != afterG[i])
//	        R10[i] = 1;
//	    }
//	    L10 = R9;
//
//	    afterG = g(R10, keys[10]);
//	    for (int i = 0; i < 32; i++)
//	    {
//	      if (L10[i] != afterG[i])
//	        R11[i] = 1;
//	    }
//	    L11 = R10;
//
//	    afterG = g(R11, keys[11]);
//	    for (int i = 0; i < 32; i++)
//	    {
//	      if (L11[i] != afterG[i])
//	        R12[i] = 1;
//	    }
//	    L12 = R11;
//
//	    afterG = g(R12, keys[12]);
//	    for (int i = 0; i < 32; i++)
//	    {
//	      if (L12[i] != afterG[i])
//	        R13[i] = 1;
//	    }
//	    L13 = R12;
//
//	    afterG = g(R13, keys[13]);
//	    for (int i = 0; i < 32; i++)
//	    {
//	      if (L13[i] != afterG[i])
//	        R14[i] = 1;
//	    }
//	    L14 = R13;
//
//	    afterG = g(R14, keys[14]);
//	    for (int i = 0; i < 32; i++)
//	    {
//	      if (L14[i] != afterG[i])
//	        R15[i] = 1;
//	    }
//	    L15 = R14;
//
//	    afterG = g(R15, keys[15]);
//	    for (int i = 0; i < 32; i++)
//	    {
//	      if (L15[i] != afterG[i])
//	        R16[i] = 1;
//	    }
//	    L16 = R15;
//
//	    for (int i = 0; i < 32; i++)
//	    {
//	      temp[i] = R16[i];
//	      temp[(i + 32)] = L16[i];
//	    }
//
//	    Final = IP_1(temp);
//	    return Final;
//	  }
//
//	  private static  int[] PC2(int[] c, int[] d)
//	  {
//	    int[] temp = new int[56];
//	    int[] k = new int[48];
//
//	    for (int i = 0; i < 28; i++)
//	    {
//	      temp[i] = c[i];
//	      temp[(i + 28)] = d[i];
//	    }
//
//	    k[0] = temp[13];
//	    k[1] = temp[16];
//	    k[2] = temp[10];
//	    k[3] = temp[23];
//	    k[4] = temp[0];
//	    k[5] = temp[4];
//	    k[6] = temp[2];
//	    k[7] = temp[27];
//	    k[8] = temp[14];
//	    k[9] = temp[5];
//	    k[10] = temp[20];
//	    k[11] = temp[9];
//	    k[12] = temp[22];
//	    k[13] = temp[18];
//	    k[14] = temp[11];
//	    k[15] = temp[3];
//	    k[16] = temp[25];
//	    k[17] = temp[7];
//	    k[18] = temp[15];
//	    k[19] = temp[6];
//	    k[20] = temp[26];
//	    k[21] = temp[19];
//	    k[22] = temp[12];
//	    k[23] = temp[1];
//	    k[24] = temp[40];
//	    k[25] = temp[51];
//	    k[26] = temp[30];
//	    k[27] = temp[36];
//	    k[28] = temp[46];
//	    k[29] = temp[54];
//	    k[30] = temp[29];
//	    k[31] = temp[39];
//	    k[32] = temp[50];
//	    k[33] = temp[44];
//	    k[34] = temp[32];
//	    k[35] = temp[47];
//	    k[36] = temp[43];
//	    k[37] = temp[48];
//	    k[38] = temp[38];
//	    k[39] = temp[55];
//	    k[40] = temp[33];
//	    k[41] = temp[52];
//	    k[42] = temp[45];
//	    k[43] = temp[41];
//	    k[44] = temp[49];
//	    k[45] = temp[35];
//	    k[46] = temp[28];
//	    k[47] = temp[31];
//	    return k;
//	  }
//
//	  private static int[] IP(char[] mchar)
//	  {
//	    int[] mint = new int[64];
//	    int[] mreturn = new int[64];
//	    for (int i = 0; i < mint.length; i++)
//	    {
//	      mint[i] = 0;
//	    }
//
//	    for (int i = 0; i < mchar.length; i++)
//	    {
//	      if ((mchar[i] & 0x80) != 0)
//	        mint[(i * 8 + 0)] = 1;
//	      if ((mchar[i] & 0x40) != 0)
//	        mint[(i * 8 + 1)] = 1;
//	      if ((mchar[i] & 0x20) != 0)
//	        mint[(i * 8 + 2)] = 1;
//	      if ((mchar[i] & 0x10) != 0)
//	        mint[(i * 8 + 3)] = 1;
//	      if ((mchar[i] & 0x8) != 0)
//	        mint[(i * 8 + 4)] = 1;
//	      if ((mchar[i] & 0x4) != 0)
//	        mint[(i * 8 + 5)] = 1;
//	      if ((mchar[i] & 0x2) != 0)
//	        mint[(i * 8 + 6)] = 1;
//	      if ((mchar[i] & 0x1) != 0) {
//	        mint[(i * 8 + 7)] = 1;
//	      }
//	    }
//
//	    mreturn[0] = mint[57];
//	    mreturn[1] = mint[49];
//	    mreturn[2] = mint[41];
//	    mreturn[3] = mint[33];
//	    mreturn[4] = mint[25];
//	    mreturn[5] = mint[17];
//	    mreturn[6] = mint[9];
//	    mreturn[7] = mint[1];
//	    mreturn[8] = mint[59];
//	    mreturn[9] = mint[51];
//	    mreturn[10] = mint[43];
//	    mreturn[11] = mint[35];
//	    mreturn[12] = mint[27];
//	    mreturn[13] = mint[19];
//	    mreturn[14] = mint[11];
//	    mreturn[15] = mint[3];
//	    mreturn[16] = mint[61];
//	    mreturn[17] = mint[53];
//	    mreturn[18] = mint[45];
//	    mreturn[19] = mint[37];
//	    mreturn[20] = mint[29];
//	    mreturn[21] = mint[21];
//	    mreturn[22] = mint[13];
//	    mreturn[23] = mint[5];
//	    mreturn[24] = mint[63];
//	    mreturn[25] = mint[55];
//	    mreturn[26] = mint[47];
//	    mreturn[27] = mint[39];
//	    mreturn[28] = mint[31];
//	    mreturn[29] = mint[23];
//	    mreturn[30] = mint[15];
//	    mreturn[31] = mint[7];
//	    mreturn[32] = mint[56];
//	    mreturn[33] = mint[48];
//	    mreturn[34] = mint[40];
//	    mreturn[35] = mint[32];
//	    mreturn[36] = mint[24];
//	    mreturn[37] = mint[16];
//	    mreturn[38] = mint[8];
//	    mreturn[39] = mint[0];
//	    mreturn[40] = mint[58];
//	    mreturn[41] = mint[50];
//	    mreturn[42] = mint[42];
//	    mreturn[43] = mint[34];
//	    mreturn[44] = mint[26];
//	    mreturn[45] = mint[18];
//	    mreturn[46] = mint[10];
//	    mreturn[47] = mint[2];
//	    mreturn[48] = mint[60];
//	    mreturn[49] = mint[52];
//	    mreturn[50] = mint[44];
//	    mreturn[51] = mint[36];
//	    mreturn[52] = mint[28];
//	    mreturn[53] = mint[20];
//	    mreturn[54] = mint[12];
//	    mreturn[55] = mint[4];
//	    mreturn[56] = mint[62];
//	    mreturn[57] = mint[54];
//	    mreturn[58] = mint[46];
//	    mreturn[59] = mint[38];
//	    mreturn[60] = mint[30];
//	    mreturn[61] = mint[22];
//	    mreturn[62] = mint[14];
//	    mreturn[63] = mint[6];
//	    return mreturn;
//	  }
//
//	  private static int[] g(int[] r, int[] k)
//	  {
//	    int[] afterE = E(r);
//
//	    int[] m1 = new int[6];
//	    int[] m4 = new int[6];
//	    int[] m7 = new int[6];
//	    int[] m2 = new int[6];
//	    int[] m5 = new int[6];
//	    int[] m8 = new int[6];
//	    int[] m3 = new int[6];
//	    int[] m6 = new int[6];
//
//	    int[] afterS1 = new int[4];
//	    int[] afterS5 = new int[4];
//	    int[] afterS2 = new int[4];
//	    int[] afterS6 = new int[4];
//	    int[] afterS3 = new int[4];
//	    int[] afterS7 = new int[4];
//	    int[] afterS4 = new int[4];
//	    int[] afterS8 = new int[4];
//	    int[] afterS = new int[32];
//	    int[] greturn = new int[32];
//
//	    for (int i = 0; i < 48; i++)
//	    {
//	      if (afterE[i] != k[i])
//	        afterE[i] = 1;
//	      else {
//	        afterE[i] = 0;
//	      }
//	    }
//	    for (int i = 0; i < 6; i++)
//	    {
//	      m1[i] = afterE[i];
//	      m2[i] = afterE[(i + 6)];
//	      m3[i] = afterE[(i + 12)];
//	      m4[i] = afterE[(i + 18)];
//	      m5[i] = afterE[(i + 24)];
//	      m6[i] = afterE[(i + 30)];
//	      m7[i] = afterE[(i + 36)];
//	      m8[i] = afterE[(i + 42)];
//	    }
//
//	    int temp = getS(m1, 1);
//	    if ((temp & 0x8) != 0)
//	      afterS1[0] = 1;
//	    if ((temp & 0x4) != 0)
//	      afterS1[1] = 1;
//	    if ((temp & 0x2) != 0)
//	      afterS1[2] = 1;
//	    if ((temp & 0x1) != 0)
//	      afterS1[3] = 1;
//	    temp = getS(m2, 2);
//	    if ((temp & 0x8) != 0)
//	      afterS2[0] = 1;
//	    if ((temp & 0x4) != 0)
//	      afterS2[1] = 1;
//	    if ((temp & 0x2) != 0)
//	      afterS2[2] = 1;
//	    if ((temp & 0x1) != 0)
//	      afterS2[3] = 1;
//	    temp = getS(m3, 3);
//	    if ((temp & 0x8) != 0)
//	      afterS3[0] = 1;
//	    if ((temp & 0x4) != 0)
//	      afterS3[1] = 1;
//	    if ((temp & 0x2) != 0)
//	      afterS3[2] = 1;
//	    if ((temp & 0x1) != 0)
//	      afterS3[3] = 1;
//	    temp = getS(m4, 4);
//	    if ((temp & 0x8) != 0)
//	      afterS4[0] = 1;
//	    if ((temp & 0x4) != 0)
//	      afterS4[1] = 1;
//	    if ((temp & 0x2) != 0)
//	      afterS4[2] = 1;
//	    if ((temp & 0x1) != 0)
//	      afterS4[3] = 1;
//	    temp = getS(m5, 5);
//	    if ((temp & 0x8) != 0)
//	      afterS5[0] = 1;
//	    if ((temp & 0x4) != 0)
//	      afterS5[1] = 1;
//	    if ((temp & 0x2) != 0)
//	      afterS5[2] = 1;
//	    if ((temp & 0x1) != 0)
//	      afterS5[3] = 1;
//	    temp = getS(m6, 6);
//	    if ((temp & 0x8) != 0)
//	      afterS6[0] = 1;
//	    if ((temp & 0x4) != 0)
//	      afterS6[1] = 1;
//	    if ((temp & 0x2) != 0)
//	      afterS6[2] = 1;
//	    if ((temp & 0x1) != 0)
//	      afterS6[3] = 1;
//	    temp = getS(m7, 7);
//	    if ((temp & 0x8) != 0)
//	      afterS7[0] = 1;
//	    if ((temp & 0x4) != 0)
//	      afterS7[1] = 1;
//	    if ((temp & 0x2) != 0)
//	      afterS7[2] = 1;
//	    if ((temp & 0x1) != 0)
//	      afterS7[3] = 1;
//	    temp = getS(m8, 8);
//	    if ((temp & 0x8) != 0)
//	      afterS8[0] = 1;
//	    if ((temp & 0x4) != 0)
//	      afterS8[1] = 1;
//	    if ((temp & 0x2) != 0)
//	      afterS8[2] = 1;
//	    if ((temp & 0x1) != 0) {
//	      afterS8[3] = 1;
//	    }
//
//	    for (int i = 0; i < 4; i++)
//	    {
//	      afterS[i] = afterS1[i];
//	      afterS[(i + 4)] = afterS2[i];
//	      afterS[(i + 8)] = afterS3[i];
//	      afterS[(i + 12)] = afterS4[i];
//	      afterS[(i + 16)] = afterS5[i];
//	      afterS[(i + 20)] = afterS6[i];
//	      afterS[(i + 24)] = afterS7[i];
//	      afterS[(i + 28)] = afterS8[i];
//	    }
//
//	    greturn[0] = afterS[15];
//	    greturn[1] = afterS[6];
//	    greturn[2] = afterS[19];
//	    greturn[3] = afterS[20];
//	    greturn[4] = afterS[28];
//	    greturn[5] = afterS[11];
//	    greturn[6] = afterS[27];
//	    greturn[7] = afterS[16];
//	    greturn[8] = afterS[0];
//	    greturn[9] = afterS[14];
//	    greturn[10] = afterS[22];
//	    greturn[11] = afterS[25];
//	    greturn[12] = afterS[4];
//	    greturn[13] = afterS[17];
//	    greturn[14] = afterS[30];
//	    greturn[15] = afterS[9];
//	    greturn[16] = afterS[1];
//	    greturn[17] = afterS[7];
//	    greturn[18] = afterS[23];
//	    greturn[19] = afterS[13];
//	    greturn[20] = afterS[31];
//	    greturn[21] = afterS[26];
//	    greturn[22] = afterS[2];
//	    greturn[23] = afterS[8];
//	    greturn[24] = afterS[18];
//	    greturn[25] = afterS[12];
//	    greturn[26] = afterS[29];
//	    greturn[27] = afterS[5];
//	    greturn[28] = afterS[21];
//	    greturn[29] = afterS[10];
//	    greturn[30] = afterS[3];
//	    greturn[31] = afterS[24];
//	    return greturn;
//	  }
//
//	  private static int getS(int[] Mi, int i)
//	  {
//	    int[][] S1 = new int[4][16];
//	    int[][] S2 = new int[4][16];
//	    int[][] S3 = new int[4][16];
//	    int[][] S4 = new int[4][16];
//	    int[][] S5 = new int[4][16];
//	    int[][] S6 = new int[4][16];
//	    int[][] S7 = new int[4][16];
//	    int[][] S8 = new int[4][16];
//	    int[] output = new int[8];
//
//	    S1[0][0] = 14;
//	    S1[0][1] = 4;
//	    S1[0][2] = 13;
//	    S1[0][3] = 1;
//	    S1[0][4] = 2;
//	    S1[0][5] = 15;
//	    S1[0][6] = 11;
//	    S1[0][7] = 8;
//	    S1[0][8] = 3;
//	    S1[0][9] = 10;
//	    S1[0][10] = 6;
//	    S1[0][11] = 12;
//	    S1[0][12] = 5;
//	    S1[0][13] = 9;
//	    S1[0][14] = 0;
//	    S1[0][15] = 7;
//	    S1[1][0] = 0;
//	    S1[1][1] = 15;
//	    S1[1][2] = 7;
//	    S1[1][3] = 4;
//	    S1[1][4] = 14;
//	    S1[1][5] = 2;
//	    S1[1][6] = 13;
//	    S1[1][7] = 1;
//	    S1[1][8] = 10;
//	    S1[1][9] = 6;
//	    S1[1][10] = 12;
//	    S1[1][11] = 11;
//	    S1[1][12] = 9;
//	    S1[1][13] = 5;
//	    S1[1][14] = 3;
//	    S1[1][15] = 8;
//	    S1[2][0] = 4;
//	    S1[2][1] = 1;
//	    S1[2][2] = 14;
//	    S1[2][3] = 8;
//	    S1[2][4] = 13;
//	    S1[2][5] = 6;
//	    S1[2][6] = 2;
//	    S1[2][7] = 11;
//	    S1[2][8] = 15;
//	    S1[2][9] = 12;
//	    S1[2][10] = 9;
//	    S1[2][11] = 7;
//	    S1[2][12] = 3;
//	    S1[2][13] = 10;
//	    S1[2][14] = 5;
//	    S1[2][15] = 0;
//	    S1[3][0] = 15;
//	    S1[3][1] = 12;
//	    S1[3][2] = 8;
//	    S1[3][3] = 2;
//	    S1[3][4] = 4;
//	    S1[3][5] = 9;
//	    S1[3][6] = 1;
//	    S1[3][7] = 7;
//	    S1[3][8] = 5;
//	    S1[3][9] = 11;
//	    S1[3][10] = 3;
//	    S1[3][11] = 14;
//	    S1[3][12] = 10;
//	    S1[3][13] = 0;
//	    S1[3][14] = 6;
//	    S1[3][15] = 13;
//
//	    S2[0][0] = 15;
//	    S2[0][1] = 1;
//	    S2[0][2] = 8;
//	    S2[0][3] = 14;
//	    S2[0][4] = 6;
//	    S2[0][5] = 11;
//	    S2[0][6] = 3;
//	    S2[0][7] = 4;
//	    S2[0][8] = 9;
//	    S2[0][9] = 7;
//	    S2[0][10] = 2;
//	    S2[0][11] = 13;
//	    S2[0][12] = 12;
//	    S2[0][13] = 0;
//	    S2[0][14] = 5;
//	    S2[0][15] = 10;
//	    S2[1][0] = 3;
//	    S2[1][1] = 13;
//	    S2[1][2] = 4;
//	    S2[1][3] = 7;
//	    S2[1][4] = 15;
//	    S2[1][5] = 2;
//	    S2[1][6] = 8;
//	    S2[1][7] = 14;
//	    S2[1][8] = 12;
//	    S2[1][9] = 0;
//	    S2[1][10] = 1;
//	    S2[1][11] = 10;
//	    S2[1][12] = 6;
//	    S2[1][13] = 9;
//	    S2[1][14] = 11;
//	    S2[1][15] = 5;
//	    S2[2][0] = 0;
//	    S2[2][1] = 14;
//	    S2[2][2] = 7;
//	    S2[2][3] = 11;
//	    S2[2][4] = 10;
//	    S2[2][5] = 4;
//	    S2[2][6] = 13;
//	    S2[2][7] = 1;
//	    S2[2][8] = 5;
//	    S2[2][9] = 8;
//	    S2[2][10] = 12;
//	    S2[2][11] = 6;
//	    S2[2][12] = 9;
//	    S2[2][13] = 3;
//	    S2[2][14] = 2;
//	    S2[2][15] = 15;
//	    S2[3][0] = 13;
//	    S2[3][1] = 8;
//	    S2[3][2] = 10;
//	    S2[3][3] = 1;
//	    S2[3][4] = 3;
//	    S2[3][5] = 15;
//	    S2[3][6] = 4;
//	    S2[3][7] = 2;
//	    S2[3][8] = 11;
//	    S2[3][9] = 6;
//	    S2[3][10] = 7;
//	    S2[3][11] = 12;
//	    S2[3][12] = 0;
//	    S2[3][13] = 5;
//	    S2[3][14] = 14;
//	    S2[3][15] = 9;
//
//	    S3[0][0] = 10;
//	    S3[0][1] = 0;
//	    S3[0][2] = 9;
//	    S3[0][3] = 14;
//	    S3[0][4] = 6;
//	    S3[0][5] = 3;
//	    S3[0][6] = 15;
//	    S3[0][7] = 5;
//	    S3[0][8] = 1;
//	    S3[0][9] = 13;
//	    S3[0][10] = 12;
//	    S3[0][11] = 7;
//	    S3[0][12] = 11;
//	    S3[0][13] = 4;
//	    S3[0][14] = 2;
//	    S3[0][15] = 8;
//	    S3[1][0] = 13;
//	    S3[1][1] = 7;
//	    S3[1][2] = 0;
//	    S3[1][3] = 9;
//	    S3[1][4] = 3;
//	    S3[1][5] = 4;
//	    S3[1][6] = 6;
//	    S3[1][7] = 10;
//	    S3[1][8] = 2;
//	    S3[1][9] = 8;
//	    S3[1][10] = 5;
//	    S3[1][11] = 14;
//	    S3[1][12] = 12;
//	    S3[1][13] = 11;
//	    S3[1][14] = 15;
//	    S3[1][15] = 1;
//	    S3[2][0] = 13;
//	    S3[2][1] = 6;
//	    S3[2][2] = 4;
//	    S3[2][3] = 9;
//	    S3[2][4] = 8;
//	    S3[2][5] = 15;
//	    S3[2][6] = 3;
//	    S3[2][7] = 0;
//	    S3[2][8] = 11;
//	    S3[2][9] = 1;
//	    S3[2][10] = 2;
//	    S3[2][11] = 12;
//	    S3[2][12] = 5;
//	    S3[2][13] = 10;
//	    S3[2][14] = 14;
//	    S3[2][15] = 7;
//	    S3[3][0] = 1;
//	    S3[3][1] = 10;
//	    S3[3][2] = 13;
//	    S3[3][3] = 0;
//	    S3[3][4] = 6;
//	    S3[3][5] = 9;
//	    S3[3][6] = 8;
//	    S3[3][7] = 7;
//	    S3[3][8] = 4;
//	    S3[3][9] = 15;
//	    S3[3][10] = 14;
//	    S3[3][11] = 3;
//	    S3[3][12] = 11;
//	    S3[3][13] = 5;
//	    S3[3][14] = 2;
//	    S3[3][15] = 12;
//
//	    S4[0][0] = 7;
//	    S4[0][1] = 13;
//	    S4[0][2] = 14;
//	    S4[0][3] = 3;
//	    S4[0][4] = 0;
//	    S4[0][5] = 6;
//	    S4[0][6] = 9;
//	    S4[0][7] = 10;
//	    S4[0][8] = 1;
//	    S4[0][9] = 2;
//	    S4[0][10] = 8;
//	    S4[0][11] = 5;
//	    S4[0][12] = 11;
//	    S4[0][13] = 12;
//	    S4[0][14] = 4;
//	    S4[0][15] = 15;
//	    S4[1][0] = 13;
//	    S4[1][1] = 8;
//	    S4[1][2] = 11;
//	    S4[1][3] = 5;
//	    S4[1][4] = 6;
//	    S4[1][5] = 15;
//	    S4[1][6] = 0;
//	    S4[1][7] = 3;
//	    S4[1][8] = 4;
//	    S4[1][9] = 7;
//	    S4[1][10] = 2;
//	    S4[1][11] = 12;
//	    S4[1][12] = 1;
//	    S4[1][13] = 10;
//	    S4[1][14] = 14;
//	    S4[1][15] = 9;
//	    S4[2][0] = 10;
//	    S4[2][1] = 6;
//	    S4[2][2] = 9;
//	    S4[2][3] = 0;
//	    S4[2][4] = 12;
//	    S4[2][5] = 11;
//	    S4[2][6] = 7;
//	    S4[2][7] = 13;
//	    S4[2][8] = 15;
//	    S4[2][9] = 1;
//	    S4[2][10] = 3;
//	    S4[2][11] = 14;
//	    S4[2][12] = 5;
//	    S4[2][13] = 2;
//	    S4[2][14] = 8;
//	    S4[2][15] = 4;
//	    S4[3][0] = 3;
//	    S4[3][1] = 15;
//	    S4[3][2] = 0;
//	    S4[3][3] = 6;
//	    S4[3][4] = 10;
//	    S4[3][5] = 1;
//	    S4[3][6] = 13;
//	    S4[3][7] = 8;
//	    S4[3][8] = 9;
//	    S4[3][9] = 4;
//	    S4[3][10] = 5;
//	    S4[3][11] = 11;
//	    S4[3][12] = 12;
//	    S4[3][13] = 7;
//	    S4[3][14] = 2;
//	    S4[3][15] = 14;
//
//	    S5[0][0] = 2;
//	    S5[0][1] = 12;
//	    S5[0][2] = 4;
//	    S5[0][3] = 1;
//	    S5[0][4] = 7;
//	    S5[0][5] = 10;
//	    S5[0][6] = 11;
//	    S5[0][7] = 6;
//	    S5[0][8] = 8;
//	    S5[0][9] = 5;
//	    S5[0][10] = 3;
//	    S5[0][11] = 15;
//	    S5[0][12] = 13;
//	    S5[0][13] = 0;
//	    S5[0][14] = 14;
//	    S5[0][15] = 9;
//	    S5[1][0] = 14;
//	    S5[1][1] = 11;
//	    S5[1][2] = 2;
//	    S5[1][3] = 12;
//	    S5[1][4] = 4;
//	    S5[1][5] = 7;
//	    S5[1][6] = 13;
//	    S5[1][7] = 1;
//	    S5[1][8] = 5;
//	    S5[1][9] = 0;
//	    S5[1][10] = 15;
//	    S5[1][11] = 10;
//	    S5[1][12] = 3;
//	    S5[1][13] = 9;
//	    S5[1][14] = 8;
//	    S5[1][15] = 6;
//	    S5[2][0] = 4;
//	    S5[2][1] = 2;
//	    S5[2][2] = 1;
//	    S5[2][3] = 11;
//	    S5[2][4] = 10;
//	    S5[2][5] = 13;
//	    S5[2][6] = 7;
//	    S5[2][7] = 8;
//	    S5[2][8] = 15;
//	    S5[2][9] = 9;
//	    S5[2][10] = 12;
//	    S5[2][11] = 5;
//	    S5[2][12] = 6;
//	    S5[2][13] = 3;
//	    S5[2][14] = 0;
//	    S5[2][15] = 14;
//	    S5[3][0] = 11;
//	    S5[3][1] = 8;
//	    S5[3][2] = 12;
//	    S5[3][3] = 7;
//	    S5[3][4] = 1;
//	    S5[3][5] = 14;
//	    S5[3][6] = 2;
//	    S5[3][7] = 13;
//	    S5[3][8] = 6;
//	    S5[3][9] = 15;
//	    S5[3][10] = 0;
//	    S5[3][11] = 9;
//	    S5[3][12] = 10;
//	    S5[3][13] = 4;
//	    S5[3][14] = 5;
//	    S5[3][15] = 3;
//
//	    S6[0][0] = 12;
//	    S6[0][1] = 1;
//	    S6[0][2] = 10;
//	    S6[0][3] = 15;
//	    S6[0][4] = 9;
//	    S6[0][5] = 2;
//	    S6[0][6] = 6;
//	    S6[0][7] = 8;
//	    S6[0][8] = 0;
//	    S6[0][9] = 13;
//	    S6[0][10] = 3;
//	    S6[0][11] = 4;
//	    S6[0][12] = 14;
//	    S6[0][13] = 7;
//	    S6[0][14] = 5;
//	    S6[0][15] = 11;
//	    S6[1][0] = 10;
//	    S6[1][1] = 15;
//	    S6[1][2] = 4;
//	    S6[1][3] = 2;
//	    S6[1][4] = 7;
//	    S6[1][5] = 12;
//	    S6[1][6] = 9;
//	    S6[1][7] = 5;
//	    S6[1][8] = 6;
//	    S6[1][9] = 1;
//	    S6[1][10] = 13;
//	    S6[1][11] = 14;
//	    S6[1][12] = 0;
//	    S6[1][13] = 11;
//	    S6[1][14] = 3;
//	    S6[1][15] = 8;
//	    S6[2][0] = 9;
//	    S6[2][1] = 14;
//	    S6[2][2] = 15;
//	    S6[2][3] = 5;
//	    S6[2][4] = 2;
//	    S6[2][5] = 8;
//	    S6[2][6] = 12;
//	    S6[2][7] = 3;
//	    S6[2][8] = 7;
//	    S6[2][9] = 0;
//	    S6[2][10] = 4;
//	    S6[2][11] = 10;
//	    S6[2][12] = 1;
//	    S6[2][13] = 13;
//	    S6[2][14] = 11;
//	    S6[2][15] = 6;
//	    S6[3][0] = 4;
//	    S6[3][1] = 3;
//	    S6[3][2] = 2;
//	    S6[3][3] = 12;
//	    S6[3][4] = 9;
//	    S6[3][5] = 5;
//	    S6[3][6] = 15;
//	    S6[3][7] = 10;
//	    S6[3][8] = 11;
//	    S6[3][9] = 14;
//	    S6[3][10] = 1;
//	    S6[3][11] = 7;
//	    S6[3][12] = 6;
//	    S6[3][13] = 0;
//	    S6[3][14] = 8;
//	    S6[3][15] = 13;
//
//	    S7[0][0] = 4;
//	    S7[0][1] = 11;
//	    S7[0][2] = 2;
//	    S7[0][3] = 14;
//	    S7[0][4] = 15;
//	    S7[0][5] = 0;
//	    S7[0][6] = 8;
//	    S7[0][7] = 13;
//	    S7[0][8] = 3;
//	    S7[0][9] = 12;
//	    S7[0][10] = 9;
//	    S7[0][11] = 7;
//	    S7[0][12] = 5;
//	    S7[0][13] = 10;
//	    S7[0][14] = 6;
//	    S7[0][15] = 1;
//	    S7[1][0] = 13;
//	    S7[1][1] = 0;
//	    S7[1][2] = 11;
//	    S7[1][3] = 7;
//	    S7[1][4] = 4;
//	    S7[1][5] = 9;
//	    S7[1][6] = 1;
//	    S7[1][7] = 10;
//	    S7[1][8] = 14;
//	    S7[1][9] = 3;
//	    S7[1][10] = 5;
//	    S7[1][11] = 12;
//	    S7[1][12] = 2;
//	    S7[1][13] = 15;
//	    S7[1][14] = 8;
//	    S7[1][15] = 6;
//	    S7[2][0] = 1;
//	    S7[2][1] = 4;
//	    S7[2][2] = 11;
//	    S7[2][3] = 13;
//	    S7[2][4] = 12;
//	    S7[2][5] = 3;
//	    S7[2][6] = 7;
//	    S7[2][7] = 14;
//	    S7[2][8] = 10;
//	    S7[2][9] = 15;
//	    S7[2][10] = 6;
//	    S7[2][11] = 8;
//	    S7[2][12] = 0;
//	    S7[2][13] = 5;
//	    S7[2][14] = 9;
//	    S7[2][15] = 2;
//	    S7[3][0] = 6;
//	    S7[3][1] = 11;
//	    S7[3][2] = 13;
//	    S7[3][3] = 8;
//	    S7[3][4] = 1;
//	    S7[3][5] = 4;
//	    S7[3][6] = 10;
//	    S7[3][7] = 7;
//	    S7[3][8] = 9;
//	    S7[3][9] = 5;
//	    S7[3][10] = 0;
//	    S7[3][11] = 15;
//	    S7[3][12] = 14;
//	    S7[3][13] = 2;
//	    S7[3][14] = 3;
//	    S7[3][15] = 12;
//
//	    S8[0][0] = 13;
//	    S8[0][1] = 2;
//	    S8[0][2] = 8;
//	    S8[0][3] = 4;
//	    S8[0][4] = 6;
//	    S8[0][5] = 15;
//	    S8[0][6] = 11;
//	    S8[0][7] = 1;
//	    S8[0][8] = 10;
//	    S8[0][9] = 9;
//	    S8[0][10] = 3;
//	    S8[0][11] = 14;
//	    S8[0][12] = 5;
//	    S8[0][13] = 0;
//	    S8[0][14] = 12;
//	    S8[0][15] = 7;
//	    S8[1][0] = 1;
//	    S8[1][1] = 15;
//	    S8[1][2] = 13;
//	    S8[1][3] = 8;
//	    S8[1][4] = 10;
//	    S8[1][5] = 3;
//	    S8[1][6] = 7;
//	    S8[1][7] = 4;
//	    S8[1][8] = 12;
//	    S8[1][9] = 5;
//	    S8[1][10] = 6;
//	    S8[1][11] = 11;
//	    S8[1][12] = 0;
//	    S8[1][13] = 14;
//	    S8[1][14] = 9;
//	    S8[1][15] = 2;
//	    S8[2][0] = 7;
//	    S8[2][1] = 11;
//	    S8[2][2] = 4;
//	    S8[2][3] = 1;
//	    S8[2][4] = 9;
//	    S8[2][5] = 12;
//	    S8[2][6] = 14;
//	    S8[2][7] = 2;
//	    S8[2][8] = 0;
//	    S8[2][9] = 6;
//	    S8[2][10] = 10;
//	    S8[2][11] = 13;
//	    S8[2][12] = 15;
//	    S8[2][13] = 3;
//	    S8[2][14] = 5;
//	    S8[2][15] = 8;
//	    S8[3][0] = 2;
//	    S8[3][1] = 1;
//	    S8[3][2] = 14;
//	    S8[3][3] = 7;
//	    S8[3][4] = 4;
//	    S8[3][5] = 10;
//	    S8[3][6] = 8;
//	    S8[3][7] = 13;
//	    S8[3][8] = 15;
//	    S8[3][9] = 12;
//	    S8[3][10] = 9;
//	    S8[3][11] = 0;
//	    S8[3][12] = 3;
//	    S8[3][13] = 5;
//	    S8[3][14] = 6;
//	    S8[3][15] = 11;
//
//	    int j = Mi[0] * 2 + Mi[5];
//	    int k = Mi[1] * 8 + Mi[2] * 4 + Mi[3] * 2 + Mi[4];
//	    output[0] = S1[j][k];
//	    output[1] = S2[j][k];
//	    output[2] = S3[j][k];
//	    output[3] = S4[j][k];
//	    output[4] = S5[j][k];
//	    output[5] = S6[j][k];
//	    output[6] = S7[j][k];
//	    output[7] = S8[j][k];
//	    return output[(i - 1)];
//	  }
//
//	  private static int[] E(int[] r)
//	  {
//	    int[] AfterE = new int[48];
//	    AfterE[0] = r[31];
//	    int i=0;
//	    for ( i = 1; i <= 5; i++)
//	    {
//	      AfterE[i] = r[(i - 1)];
//	    }
//	    for (i = 6; i <= 11; i++)
//	    {
//	      AfterE[i] = r[(i - 3)];
//	    }
//	    for (i = 12; i <= 17; i++)
//	    {
//	      AfterE[i] = r[(i - 5)];
//	    }
//	    for (i = 18; i <= 23; i++)
//	    {
//	      AfterE[i] = r[(i - 7)];
//	    }
//	    for (i = 24; i <= 29; i++)
//	    {
//	      AfterE[i] = r[(i - 9)];
//	    }
//	    for (i = 30; i <= 35; i++)
//	    {
//	      AfterE[i] = r[(i - 11)];
//	    }
//	    for (i = 36; i <= 41; i++)
//	    {
//	      AfterE[i] = r[(i - 13)];
//	    }
//	    for (i = 42; i <= 46; i++)
//	    {
//	      AfterE[i] = r[(i - 15)];
//	    }
//	    AfterE[47] = r[0];
//	    return AfterE;
//	  }
//
//	  private static int[] IP_1(int[] mchar)
//	  {
//	    int[] IPreturn = new int[64];
//	    IPreturn[0] = mchar[39];
//	    IPreturn[1] = mchar[7];
//	    IPreturn[2] = mchar[47];
//	    IPreturn[3] = mchar[15];
//	    IPreturn[4] = mchar[55];
//	    IPreturn[5] = mchar[23];
//	    IPreturn[6] = mchar[63];
//	    IPreturn[7] = mchar[31];
//	    IPreturn[8] = mchar[38];
//	    IPreturn[9] = mchar[6];
//	    IPreturn[10] = mchar[46];
//	    IPreturn[11] = mchar[14];
//	    IPreturn[12] = mchar[54];
//	    IPreturn[13] = mchar[22];
//	    IPreturn[14] = mchar[62];
//	    IPreturn[15] = mchar[30];
//	    IPreturn[16] = mchar[37];
//	    IPreturn[17] = mchar[5];
//	    IPreturn[18] = mchar[45];
//	    IPreturn[19] = mchar[13];
//	    IPreturn[20] = mchar[53];
//	    IPreturn[21] = mchar[21];
//	    IPreturn[22] = mchar[61];
//	    IPreturn[23] = mchar[29];
//	    IPreturn[24] = mchar[36];
//	    IPreturn[25] = mchar[4];
//	    IPreturn[26] = mchar[44];
//	    IPreturn[27] = mchar[12];
//	    IPreturn[28] = mchar[52];
//	    IPreturn[29] = mchar[20];
//	    IPreturn[30] = mchar[60];
//	    IPreturn[31] = mchar[28];
//	    IPreturn[32] = mchar[35];
//	    IPreturn[33] = mchar[3];
//	    IPreturn[34] = mchar[43];
//	    IPreturn[35] = mchar[11];
//	    IPreturn[36] = mchar[51];
//	    IPreturn[37] = mchar[19];
//	    IPreturn[38] = mchar[59];
//	    IPreturn[39] = mchar[27];
//	    IPreturn[40] = mchar[34];
//	    IPreturn[41] = mchar[2];
//	    IPreturn[42] = mchar[42];
//	    IPreturn[43] = mchar[10];
//	    IPreturn[44] = mchar[50];
//	    IPreturn[45] = mchar[18];
//	    IPreturn[46] = mchar[58];
//	    IPreturn[47] = mchar[26];
//	    IPreturn[48] = mchar[33];
//	    IPreturn[49] = mchar[1];
//	    IPreturn[50] = mchar[41];
//	    IPreturn[51] = mchar[9];
//	    IPreturn[52] = mchar[49];
//	    IPreturn[53] = mchar[17];
//	    IPreturn[54] = mchar[57];
//	    IPreturn[55] = mchar[25];
//	    IPreturn[56] = mchar[32];
//	    IPreturn[57] = mchar[0];
//	    IPreturn[58] = mchar[40];
//	    IPreturn[59] = mchar[8];
//	    IPreturn[60] = mchar[48];
//	    IPreturn[61] = mchar[16];
//	    IPreturn[62] = mchar[56];
//	    IPreturn[63] = mchar[24];
//	    return IPreturn;
//	  }
}
