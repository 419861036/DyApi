package kkd.common.util;

/** 
 *  
 */  
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

  
  
  
/** 
 * RSA 工具类。提供加密，解密，生成密钥对等方法。 
 * 需要到http://www.bouncycastle.org下载bcprov-jdk14-123.jar。 
 *  
 */  
public class RSAUtil {  
	private static final char hexDigits[] = { '0', '1', '2', '3', '4', '5',
		'6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
    private static KeyFactory keyFac = null;
//    private static Cipher cipher = null;
    private static PublicKey publicKey = null;
    private static PrivateKey privateKey = null;
    private static BigInteger smodulus = null;
    private static BigInteger spublicExponent = null;
    private static BigInteger sprivateExponent = null;
    private static BouncyCastleProvider boundy = null;
	public static void init() {
		 try {  
			 	if(keyFac == null) {
	            	keyFac = KeyFactory.getInstance("RSA",  
		                    new org.bouncycastle.jce.provider.BouncyCastleProvider()); 
	            }
//	            if(cipher == null) {
//	            	cipher = Cipher.getInstance("RSA",  
//		                    new org.bouncycastle.jce.provider.BouncyCastleProvider());
//	            }
			 	if(boundy == null) {
			 		boundy = new org.bouncycastle.jce.provider.BouncyCastleProvider();
			 	}
	        } catch (Exception ex) {  
	            ex.printStackTrace();
	        }  
	}
	
	public static KeyPair getKeyPair() {
		  // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象  
        KeyPairGenerator keyPairGen = null;  
        try {  
            keyPairGen = KeyPairGenerator.getInstance("RSA");  
        } catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  
        }  
        // 初始化密钥对生成器，密钥大小为96-1024位  
        keyPairGen.initialize(1024,new SecureRandom());  
        // 生成一个密钥对，保存在keyPair中  
        KeyPair keyPair = keyPairGen.generateKeyPair();  
        return keyPair;
	}
	
	/** 
     * * 生成公钥 * 
     *  
     * @param modulus * 
     * @param publicExponent * 
     * @return RSAPublicKey * 
     * @throws Exception 
     */  
    public static PublicKey generateRSAPublicKey(BigInteger modulus,  
    		BigInteger publicExponent) throws Exception {  
    	 try { 
	    	if(smodulus == null || spublicExponent == null || 
	    			!modulus.equals(smodulus) || !publicExponent.equals(spublicExponent)) {
	    		RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(modulus, publicExponent); 
	    		 publicKey = keyFac.generatePublic(pubKeySpec);
	    	}
    		 return publicKey;
        } catch (InvalidKeySpecException ex) {  
            throw new Exception(ex.getMessage());  
        }  
    }  
  
    /** 
     * * 生成私钥 * 
     *  
     * @param modulus * 
     * @param privateExponent * 
     * @return RSAPrivateKey * 
     * @throws Exception 
     */  
    public static PrivateKey generateRSAPrivateKey(BigInteger modulus,  
    		BigInteger privateExponent) throws Exception {  
        try {  
        	if(smodulus == null || sprivateExponent == null || 
	    			!modulus.equals(smodulus) || !sprivateExponent.equals(privateExponent)) {
        		RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(modulus, privateExponent); 
        		privateKey = keyFac.generatePrivate(keySpec);
	    	}
            return privateKey;  
        } catch (InvalidKeySpecException ex) {  
            throw new Exception(ex.getMessage());  
        }  
    }  
  
    /** 
     * * 加密 * 
     *  
     * @param key 
     *            加密的密钥 * 
     * @param data 
     *            待加密的明文数据 * 
     * @return 加密后的数据 * 
     * @throws Exception 
     */  
    public static String encrypt(PublicKey pk, byte[] data) throws Exception {  
        try {  
        	final Cipher cipher = Cipher.getInstance("RSA", boundy);
            cipher.init(Cipher.ENCRYPT_MODE, pk);  
            int blockSize = cipher.getBlockSize();// 获得加密块大小，如：加密前数据为128个byte，而key_size=1024  
            // 加密块大小为127  
            // byte,加密后为128个byte;因此共有2个加密块，第一个127  
            // byte第二个为1个byte  
            int outputSize = cipher.getOutputSize(data.length);// 获得加密块加密后块大小  
            int leavedSize = data.length % blockSize;  
            int blocksSize = leavedSize != 0 ? data.length / blockSize + 1  
                    : data.length / blockSize;  
            byte[] raw = new byte[outputSize * blocksSize];  
            int i = 0;  
            while (data.length - i * blockSize > 0) {  
                if (data.length - i * blockSize > blockSize)  
                    cipher.doFinal(data, i * blockSize, blockSize, raw, i  
                            * outputSize);  
                else  
                    cipher.doFinal(data, i * blockSize, data.length - i  
                            * blockSize, raw, i * outputSize);  
                // 这里面doUpdate方法不可用，查看源代码后发现每次doUpdate后并没有什么实际动作除了把byte[]放到  
                // ByteArrayOutputStream中，而最后doFinal的时候才将所有的byte[]进行加密，可是到了此时加密块大小很可能已经超出了  
                // OutputSize所以只好用dofinal方法。  
  
                i++;  
            }  
            return toHex(raw);  
        } catch (Exception e) {  
            throw new Exception(e.getMessage());  
        }  
    }  
    
    /** 
     * * 解密 * 
     *  注意：js 解密会反转
     *  
     * @param key 
     *            解密的密钥 * 
     * @param raw 
     *            已经加密的数据 * 
     * @return 解密后的明文 * 
     * @throws Exception 
     */  
    public static String decrypt(PrivateKey pk, String hexstr) throws Exception {  
        try {  
        	final Cipher cipher = Cipher.getInstance("RSA", boundy);
        	byte[] raw = HexString2Bytes(hexstr);
            cipher.init(Cipher.DECRYPT_MODE, pk);  
            int blockSize = cipher.getBlockSize();  
            ByteArrayOutputStream bout = new ByteArrayOutputStream(64);  
            int j = 0;  
  
            while (raw.length - j * blockSize > 0) {  
                bout.write(cipher.doFinal(raw, j * blockSize, blockSize));  
                j++;  
            }  
            return new String(bout.toByteArray());  
        } catch (Exception e) {  
        	e.printStackTrace();
            throw new Exception(e.getMessage());  
        }  
    }  
    private static String toHex(byte[] bytes) {
		StringBuffer str = new StringBuffer(32);
		for (byte b : bytes) {
			str.append(hexDigits[(b & 0xf0) >> 4]);
			str.append(hexDigits[(b & 0x0f)]);
		}
		return str.toString();
	}
    
    private static int parse(char c) {  
        if (c >= 'a')  
            return (c - 'a' + 10) & 0x0f;  
        if (c >= 'A')  
            return (c - 'A' + 10) & 0x0f;  
        return (c - '0') & 0x0f;  
    }  
   
    public static byte[] HexString2Bytes(String hexstr) {  
        byte[] b = new byte[hexstr.length() / 2];  
        int j = 0;  
        for (int i = 0; i < b.length; i++) {  
            char c0 = hexstr.charAt(j++);  
            char c1 = hexstr.charAt(j++);  
            b[i] = (byte) ((parse(c0) << 4) | parse(c1));  
        }  
        return b;  
    } 
    
    /** 
     * * * 
     *  
     * @param args * 
     * @throws Exception 
     */  
    public static void main(String[] args) throws Exception { 
    	
    	String test = "123456789";
    	BigInteger publicKeyModule = new BigInteger("122263256388652243933419648259568285922405623676598054826217085078350680481848076450642812500234697188962627263904930464207598697843504356436090654275785223024928212014602039957098293549801180449314882695587852201779783925584202367127389974278906795756233724641293671439834809918623990053145910392629865805407");
    	BigInteger publicExponent = new BigInteger("65537");
    	PublicKey pk = RSAUtil.generateRSAPublicKey(publicKeyModule,
    			publicExponent);
    	BigInteger privateKeyModule = new BigInteger("122263256388652243933419648259568285922405623676598054826217085078350680481848076450642812500234697188962627263904930464207598697843504356436090654275785223024928212014602039957098293549801180449314882695587852201779783925584202367127389974278906795756233724641293671439834809918623990053145910392629865805407");
    	BigInteger privateExponent = new BigInteger("47560606350188447851421342336839249298957977478541752136038853883874334318083446923610904708500287931022372392175294829707013155970250393868832798878600910963167525879306446014098613251280339154034082916308051992037448884866418598315794747779637894228931353033521554944546313730606840041470288223499324528273");
    	PrivateKey privateKey = RSAUtil.generateRSAPrivateKey(privateKeyModule, privateExponent);
//    	
    	RSAUtil.init();
    	
    	String bPk = RSAUtil.encrypt(pk, test.getBytes());
    	System.out.println(bPk);
    	String kdp = RSAUtil.decrypt(privateKey, bPk);
//    	
    	System.out.println(kdp);

    	
//    	KeyPair pair = RSAUtil.generateKeyPair();
//    	RSAPrivateKey privateKey = (RSAPrivateKey )pair.getPrivate();
//    	System.out.println("---------------------");
//    	System.out.println(privateKey.getModulus());
//    	System.out.println("---------------------");
//    	System.out.println(privateKey.getPrivateExponent());
//    	
//    	RSAPublicKey publicKey = (RSAPublicKey )pair.getPublic();
//    	System.out.println("---------------------");
//    	System.out.println(publicKey.getModulus());
//    	System.out.println("---------------------");
//    	System.out.println(publicKey.getPublicExponent());
    	
    	
    	
    }  
}  



























