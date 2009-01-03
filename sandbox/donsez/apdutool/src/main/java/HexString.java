import java.util.StringTokenizer;

/**
 * A utility class to print and to parse hexadecimal strings
 * @author  Didier Donsez (didier.donsez@imag.fr)
 **/

public class HexString {

	/**
	 * Parse a Hex string
	 * @param hexString a a hexadecimal-formatted string
	 * @note : java.lang.Integer.parseInt(String s, int radix) do not verify if symbol is correct !!
	 */
	public static byte[] parse(String hexString, String separator)
		throws java.lang.NumberFormatException {
		if(separator==null){
			separator="\n\t ";
		}
		StringTokenizer st = new StringTokenizer(hexString,separator);
		byte[] result = new byte[st.countTokens() ];
		for(int i=0; st.hasMoreTokens(); i++) {
			//result[i]=(byte)Integer.parseInt(st.nextToken(),16);
			char[] ca=(st.nextToken()).toCharArray();
			if(ca.length!=2) throw new java.lang.NumberFormatException();
			result[i]=(byte)(parse(ca[0])*16+parse(ca[1]));
		}
		return result;
	}

	/**
	 * Parse a radix 16 symbol
	 * @param c a symbol
	 * @note : java.lang.Integer.parseInt(String s, int radix) do not verify if symbol is correct !!
	 */
	public static byte parse(char c)
		throws java.lang.NumberFormatException {
		if((c>='0' && c<='9') || (c>='a' && c<='f') || (c>='A' && c<='F'))
			return (byte)(Character.digit(c,16));
		throw new java.lang.NumberFormatException();
	}

	/**
	 * Hexify a byte
	 * @param b a byte
	 */
	public static String hexify(byte b) {
		StringBuffer sb = new StringBuffer();
		String bs = Integer.toHexString(b & 0xFF);
		if (bs.length() == 1) {
			sb.append(0);
		}
		sb.append(bs);
		return sb.toString();
	}

	/**
	 * Hexify a byte array
	 * @param ba a byte array
	 */
	public static String hexify(byte[] ba, String separator) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < ba.length; i++) {
			if(i!=0 && separator!=null) {
				sb.append(separator);
			}
			String bs = Integer.toHexString(ba[i] & 0xFF);
			if (bs.length() == 1) {
				sb.append(0);
			}
			sb.append(bs);			
		}
		return sb.toString();
	}
}