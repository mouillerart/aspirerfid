

/**
 * A utility class to print and to parse binary-formatted strings
 * @author  Didier Donsez (didier.donsez@imag.fr)
 **/

public class BinString {

	/**
	 * Parse a binary-formatted string
	 * @param binString a a binary-formatted string
	 * @throws NumberFormatException
	 */
	public static byte[] parse(String binString)
		throws java.lang.NumberFormatException {
		int len=binString.length();
		if(len%8!=0) throw new NumberFormatException("Incorrect length");
		byte[] res=new byte[len/8];
		int j=0;
		byte b=0;
		for(int i=0;i<len;i++) {
			b=(byte)(b<<1);
			char c=binString.charAt(i);
			if(c=='1') {
				b+=1;
			} else if(c!='0') {
				throw new NumberFormatException("Not a binary symbol");	
			}			
			if(i%8==7) {
				res[j++]=b;
				b=0;
			}
		}
		return res;
	}
	
	/**
	 * Binify a byte
	 * @param sb a string buffer
	 * @param b a byte
	 */
	public static void binify(StringBuffer sb, byte b) {
		for(int i=0; i<8; i++) {
			sb.append((b<0) ? '1' : '0');
			b <<= 1;
		}
	}	
	
	/**
	 * Binify a byte array
	 * @param sb a string buffer
	 * @param bs a byte array
	 */
	public static void binify(StringBuffer sb, byte[] bs) {
		for(int i=0; i<bs.length; i++) {
			binify(sb, bs[i]);
		}
	}	

}