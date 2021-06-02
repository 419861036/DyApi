package kkd.common.util;

/**
 * Hash值相关的计算处理
 * 
 * @since fan houjun 2008-4-11
 */
public class HashUtil {

	/**
	 * 判断两个对象是否相等
	 */
	public static boolean isEqual(Object o1, Object o2) {
		if (o1 == null) {
			return o2 == null;
		}
		if (o1 instanceof Object[]) {
			if (o2 instanceof Object[]) {
				Object[] a1 = (Object[]) o1;
				Object[] a2 = (Object[]) o2;
				if (a1.length == a2.length) {
					for (int i = 0; i < a1.length; i++) {
						if (!HashUtil.isEqual(a1[i], a2[i])) {
							return false;
						}
					}
					return true;
				}
				return false;

			} else {
				return false;
			}
		}
		return o1.equals(o2);
	}

	/**
	 * 计算组合对象的hash值
	 */
	public static int calHash(Object... args) {
		int k = 23;
		if (args != null) {
			for (int i = 0; i < args.length; i++) {
				int c;
				if (args[i] instanceof Object[]) {
					c = calHash((Object[]) args[i]);
				} else {
					c = args[i] == null ? 0 : args[i].hashCode();
				}
				k = k * 31 + c;
			}
		}
		return k;
	}
	public long RSHash(String str) {
		int b = 378551;
		int a = 63689;
		long hash = 0;
		for (int i = 0; i < str.length(); i++) {
			hash = hash * a + str.charAt(i);
			a = a * b;
		}
		return hash;
	}

	public long JSHash(String str) {
		long hash = 1315423911;
		for (int i = 0; i < str.length(); i++) {
			hash ^= ((hash << 5) + str.charAt(i) + (hash >> 2));
		}
		return hash;
	}

	public long PJWHash(String str) {
		long BitsInUnsignedInt = (long) (4 * 8);
		long ThreeQuarters = (long) ((BitsInUnsignedInt * 3) / 4);
		long OneEighth = (long) (BitsInUnsignedInt / 8);
		long HighBits = (long) (0xFFFFFFFF) << (BitsInUnsignedInt - OneEighth);
		long hash = 0;
		long test = 0;
		for (int i = 0; i < str.length(); i++) {
			hash = (hash << OneEighth) + str.charAt(i);
			if ((test = hash & HighBits) != 0) {
				hash = ((hash ^ (test >> ThreeQuarters)) & (~HighBits));
			}
		}
		return hash;
	}

	public static long ELFHash(String str) {
		long hash = 0;
		long x = 0;
		for (int i = 0; i < str.length(); i++) {
			hash = (hash << 4) + str.charAt(i);
			if ((x = hash & 0xF0000000L) != 0) {
				hash ^= (x >> 24);
			}
			hash &= ~x;
		}
		return hash;
	}

	public static  long BKDRHash(String str) {
		long seed = 131; // 31 131 1313 13131 131313 etc..
		long hash = 0;
		for (int i = 0; i < str.length(); i++) {
			hash = (hash * seed) + str.charAt(i);
		}
		return hash;
	}

	public static  long SDBMHash(String str) {
		long hash = 0;
		for (int i = 0; i < str.length(); i++) {
			hash = str.charAt(i) + (hash << 6) + (hash << 16) - hash;
		}
		return hash;
	}

	public static  long DJBHash(String str) {
		long hash = 5381;
		for (int i = 0; i < str.length(); i++) {
			hash = ((hash << 5) + hash) + str.charAt(i);
		}
		return hash;
	}

	public static  long DEKHash(String str) {
		long hash = str.length();
		for (int i = 0; i < str.length(); i++) {
			hash = ((hash << 5) ^ (hash >> 27)) ^ str.charAt(i);
		}
		return hash;
	}

	public static  long BPHash(String str) {
		long hash = 0;
		for (int i = 0; i < str.length(); i++) {
			hash = hash << 7 ^ str.charAt(i);
		}
		return hash;
	}

	public static  long FNVHash(String str) {
		long fnv_prime = 0x811C9DC5;
		long hash = 0;
		for (int i = 0; i < str.length(); i++) {
			hash *= fnv_prime;
			hash ^= str.charAt(i);
		}
		return hash;
	}

	public static  long APHash(String str) {
		long hash = 0xAAAAAAAA;
		for (int i = 0; i < str.length(); i++) {
			if ((i & 1) == 0) {
				hash ^= ((hash << 7) ^ str.charAt(i) ^ (hash >> 3));
			} else {
				hash ^= (~((hash << 11) ^ str.charAt(i) ^ (hash >> 5)));
			}
		}
		return hash;
	}

}
