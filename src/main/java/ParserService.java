import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class ParserService {
	
	private final static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36";
	private String token;
	public ParserService(String token) {
		this.token=token;
	}
	int getId(String userId){
	
		int id=0;
		ObjectMapper mapper = new ObjectMapper();
		
		try {
		String	wallJsonAnswer=wallConnectorByDomain(userId,token);
		String subJ=wallJsonAnswer.substring(12, wallJsonAnswer.length());
		Wall wall = mapper.readValue(subJ, Wall.class);
		id=wall.getOriginalId();
		
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	return id;		
	}
	
	 List<String> getUserIdsList(String postBody) {
		 
		Pattern patt = Pattern.compile("<a href=\"\\/(.+?)\"");	
		Matcher match = patt.matcher(postBody);
		
		List<String> list= new ArrayList<String>();
		
			while (match.find()) {
				list.add(match.group(1));
			}
			
			for(int i=0;i<list.size();i++){
				list.remove(i);
			}
			
	return list;		
	
	}
		
	String wallConnectorByDomain(String id,String token) throws IOException{
		int responseCode;
		StringBuffer response = null;
		String url = "https://api.vk.com/method/wall.get?domain="+id+"&v=5.50&access_token="+token;
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		con.setDoOutput(true);
		
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(url);
		wr.flush();
		wr.close();
		responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);
			
		BufferedReader in = new BufferedReader(new InputStreamReader(
		con.getInputStream()));
		String inputLine;
		response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
			}
			
		in.close();
			
	return response.toString();

	}
	
	List<String> convertToOriginId(List<String> ids ) throws InterruptedException{
		List<String> originalids = new ArrayList<String>();
			for(int i=0;i<ids.size();i++){
				String firstLetters=ids.get(i).substring(0,2);
				boolean bLast=Character.isDigit(ids.get(i).charAt(ids.get(i).length()-1));
					if(firstLetters.equals("id")&&bLast==true){
						String id=ids.get(i);
						originalids.add(id.substring(2,id.length()));
					}else{		
						int id=getId(ids.get(i));
						originalids.add(Integer.toString(id));
						Thread.sleep(500);
					}
			}
			
	return originalids;
	}
	
}
