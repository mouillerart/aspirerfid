package jpachube.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Pachube.Data;
import Pachube.Domain;
import Pachube.Exposure;
import Pachube.Feed;
import Pachube.Location;
import Pachube.PachubeException;
import Pachube.httpClient.HttpClient;
import Pachube.httpClient.HttpMethod;
import Pachube.httpClient.HttpRequest;
import Pachube.httpClient.HttpResponse;
import Pachube2.Pachube2;

public class Main {

	public static void main(String argv[]) {
		try {
			String key;
			int argpt = 0;
			if (argv.length > 2 && argv[0].equals("-k")) {
				key = argv[1];
				argpt = 2;
			} else {
				Properties properties = new Properties();
				try {
					properties.load(new FileInputStream("pachube.properties"));
				} catch (IOException e) {
					System.err.println("can't load pachube.properties");
					return;
				}
				key = properties.getProperty("key");
			}

			System.out.println("Pachube2 key is "+key);
			
			// Creates a Pachube2 object authenticated using the provided API KEY
			Pachube2 p = new Pachube2(key);

			if (argv.length < argpt) {
				printUsage(System.out);
				return;
			}
			
			String option = argv[argpt];
			if (option.equals("create")) {
				create(p);

			} else if (option.equals("update")) {
				if (argv.length != argpt+2) {
					printUsage(System.out);
					return;
				}

				update(p,Integer.parseInt(argv[argpt+1]));

				
			} else if (option.equals("feeds")) {
				if (argv.length != argpt+2) {
					printUsage(System.out);
					return;
				}
				feeds(argv[argpt+1]);
				

				
			} else {
				printUsage(System.out);
				return;
			}

		} catch (PachubeException e) {
			// If an exception occurs it will print the error message from the
			// failed HTTP command
			System.err.println(e.errorMessage);
		} catch (IOException e) {
			System.err.println(e);
		} catch (JSONException e) {
			System.err.println(e);
		}
	}
	/**
	 * @link http://json-lib.sourceforge.net/index.html
	 * @param username
	 * @throws PachubeException
	 * @throws JSONException
	 */
	public static void feeds(String username) throws PachubeException, JSONException {
		
		HttpClient client = new HttpClient("www.pachube.com");
		
		HttpRequest hr = new HttpRequest("http://www.pachube.com/users/"+username+"/feeds.json");
		hr.setMethod(HttpMethod.GET);
		HttpResponse g = client.send(hr);

		if (g.getHeaderItem("Status").equals("HTTP/1.1 200 OK")) {
//			System.out.println(g.getBody());
			JSONObject jsonObject=new JSONObject(g.getBody()+"}"); // add a bracket since it is missing in the returned JSON
//			System.out.println(jsonObject.toString(2));
			
			JSONArray results=jsonObject.getJSONArray("results");
			int len=results.length();
			System.out.print(len); System.out.print(" feeds :");
			for(int i=0;i<len;i++){
				if(i!=0) System.out.print(", ");
				JSONObject f=results.getJSONObject(i);
				System.out.print(f.getInt("id")+" ("+f.getString("title")+")");
			}
			System.out.println();
			
		} else {
			throw new PachubeException(g.getHeaderItem("Status"));
		}
	}

	private static Data inputData(ArrayList<Data> datas, BufferedReader in, PrintStream out, boolean flagOptTag) throws NumberFormatException, IOException, PachubeException{
		String line = null;

		out.println("Data ident:");
		do {
			line = in.readLine();
		} while (line.trim().length() == 0);
		int id=Integer.parseInt(line.trim());
		
		Data data=null;
		if(datas!=null) {
			for (Data d : datas) {
				if(d.getId()==id) {
					data=d;
					break;
				}
			}
		}
		if(data==null) data = new Data();
		data.setId(id);

		if (flagOptTag) {
			out.println("Data tag (opt):");
			if ((line = in.readLine()).length() != 0) {
				data.setTag(line.trim());
			}

		} else {
			out.println("Data tag:");
			do {
				line = in.readLine();
			} while (line.trim().length() == 0);
			data.setTag(line.trim());
		}
		out.println("Data value:");
		do {
			line = in.readLine();
		} while (line.trim().length() == 0);
		data.setValue(line.trim());

		out.println("Data min value (opt):");
		if ((line = in.readLine()).length() != 0) {
			data.setMinValue(Double.parseDouble(line.trim()));
		}

		out.println("Data max value (opt):");
		if ((line = in.readLine()).length() != 0) {
			data.setMaxValue(Double.parseDouble(line.trim()));
		}

		return data;
	}
	
	
	private static Location inputLocation(Location location,BufferedReader in, PrintStream out) throws NumberFormatException, IOException, PachubeException{
		String line = null;
		
		out.println("Location name (opt):");
		if ((line = in.readLine()).length() != 0) {
			if(location==null) location = new Location();

			location.setName(line.trim());

			out.println("Location lattitude:");
			do {
				line = in.readLine();
			} while (line.trim().length() == 0);
			location.setLat(Float.parseFloat(line.trim()));

			out.println("Location longitude:");
			do {
				line = in.readLine();
			} while (line.trim().length() == 0);
			location.setLon(Float.parseFloat(line.trim()));

			out.println("Location elevation:");
			do {
				line = in.readLine();
			} while (line.trim().length() != 0);
			location.setElevation(Float.parseFloat(line.trim()));

			
			out.println("Location domain (opt):");
			if ((line = in.readLine()).length() != 0) {
				line=line.trim();
				if(line.equals("physical")) {
					location.setDomain(Domain.physical);
				} else if(line.equals("virtual")) {
					location.setDomain(Domain.virtual);
				}
			} else {
				location.setDomain(Domain.physical);
			}

			
			out.println("Location Exposure (opt):");
			if ((line = in.readLine()).length() != 0) {
				line=line.trim();
				if(line.equals("indoor")) {
					location.setExposure(Exposure.indoor);
				} else if(line.equals("outdoor")) {
					location.setExposure(Exposure.outdoor);
				}
			} else {
				location.setExposure(Exposure.outdoor);
			}
			return location;
		}
		return null;

	}
	
	
	private static void update(Pachube2 p, int feedid) throws PachubeException, IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in,
		"UTF-8"));
		PrintStream out = System.out;
		Feed f = p.getFeed(feedid);

		//System.out.println(f.toXML());
		
		String line = null;
		
		Location location=inputLocation(f.getLocation(),in, out);
		if(location!=null) f.setLocation(location);
	
		System.out.println("XML is :"+f.toXML());
		
		System.out.println("updating ...");
		f.updateDatastream(inputData(f.getData(), in, out, true));
		System.out.println("updated");
	
	}
	
	private static void create(Pachube2 p) throws IOException, PachubeException {

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in,
				"UTF-8"));
		PrintStream out = System.out;
		Feed f = new Feed();

		String line = null;

		out.println("Title:");
		do {
			line = in.readLine();
		} while (line.trim().length() != 0);
		f.setTitle(line.trim());

		out.println("Description:");
		do {
			line = in.readLine();
		} while (line.trim().length() != 0);
		f.setDescription(line.trim());

		out.println("Web site (opt):");
		if ((line = in.readLine()).length() != 0) {
			f.setWebsite(new URL(line.trim()));
		}

		Location location=inputLocation(null, in, out);
		if(location!=null) f.setLocation(location);

		f.addData(inputData(null, in, out, false));
		
		System.out.println("XML is :"+f.toXML());
		
		System.out.println("creating ...");
		Feed g = p.createFeed(f);
		System.out.println("created");

		// The Feed 'f' is does not represent the feed on pachube any
		// Changes made to this object will not alter the online feed.

		out.println("The id of the new feed is:");
		out.println(g.getId());

	}

	private static void printUsage(PrintStream out) {
		out.print("Usage:\n"
				+ "java -jar jpachubeutil [-k key] create\n"
				+ "java -jar jpachubeutil [-k key] update <feedid>\n"
				+ "java -jar jpachubeutil [-k key] feeds  <username>\n"
				
		);
	}
}