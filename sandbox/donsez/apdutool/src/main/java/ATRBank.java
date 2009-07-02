import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

/**
 * This class parses the SC list http://ludovic.rousseau.free.fr/softwares/pcsc-tools/smartcard_list.txt
 * The format is:
 * ATR in regular expression form
 * \t descriptive text
 * \t descriptive text
 * \t descriptive text
 *  empty line
 * @author Didier Donsez
 * TODO regular expression
 */
public class ATRBank {

	private Map<byte[], String> atrs=new HashMap<byte[], String>();
		
	public void load(Reader reader) throws IOException{
		LineNumberReader r=new LineNumberReader(reader);
		String line=null;
		byte[] atr=null;
		StringBuffer atrDescription=null;
		while((line=r.readLine())!=null){
			// comment
			if(line.indexOf("#")==0) continue;
			if(line.indexOf("\t")==0){
				if(atrDescription!=null){
					atrDescription.append(line);
				}
				continue;
			}
			if(line.length()==0){
				if(atr!=null) {
					atrs.put(atr,atrDescription.toString());
					atr=null;
					atrDescription=null;
				}
			} else {
				try {
					atr=HexString.parse(line," \t");
					atrDescription=new StringBuffer();
				} catch (java.lang.NumberFormatException e) {
					System.err.println("skip ATR "+line);
				}
			}
		}
		r.close();
	}
	
	public String getDescription(byte atr[]){
		return atrs.get(atr);
	}
}
