import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.net.ssl.HttpsURLConnection;

import org.codehaus.jackson.map.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Adder {
	final static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36";
	int groupId;
	static String token;
	
	Adder(String token,int groupId){
		Adder.token=token;
		this.groupId=groupId;
	}
	
	public String getToken() {
		return token;
	}

	public static void main(String[] args) throws Exception {
		
	}
	
	void addToFriend() throws Exception{
		List<String> originSpamIds=mainListOfIds(groupId);
		for(int i=0;i<originSpamIds.size();i++){
		Thread.sleep(1000);
		addFriend(token,originSpamIds.get(i));	
		}
	}
	static List<String> mainListOfIds(int groupId) throws Exception{
		List<String> finalList=null;	
		List<String> secondList=null;
		
			List<String> list= idsForInvite(groupId);
			ParserService e = new ParserService("");		
			finalList=e.convertToOriginId(list);
			
			System.out.println(finalList);
			System.out.println(finalList.size());
			System.out.println(list);
			System.out.println(list.size());
			System.out.println(finalList.size());
			if(finalList.size()<=30){
				List<String> list1= idsForInvite(groupId);
				secondList=e.convertToOriginId(list1);
				finalList.addAll(secondList);	
			}
		
	
		System.out.println(finalList);
		System.out.println(finalList.size());
		return finalList;
		
	}

	static List<String> idsForInvite(int groupId) throws Exception {
		int responseCode;
		StringBuffer response;
		String url = "https://vk.com/al_search.php";
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		// add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		int offset=690 + (int)(Math.random() * ((999 - 690) + 1));
		System.out.println(offset);
		String urlParameters = "al=1&c%5Bage_from%5D=17&c%5Bage_to%5D=35&c%5Bcountry%5D=2&c%5Bgroup%5D="+groupId+"&c%5Bname%5D=1&c%5Bonline%5D=1&c%5Bphoto%5D=1&c%5Bsection%5D=people&c%5Bsex%5D=2&offset="+offset;
	
		System.out.println(urlParameters);
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + urlParameters);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		ParserService s= new ParserService(token);

		if(responseCode==200){
			return s.getUserIdsList(response.toString());	
		}else{
			List<String> list= new ArrayList<String>();
			list.add(response.toString());
			return list;
		}
		
		}
	
	 void addFriend(String token, String userId) throws IOException, InterruptedException{
		
		int responseCode;
		StringBuffer response = null;
	
		String url = "https://api.vk.com/method/friends.add?user_id="+userId+"&follow=0&text=&v=5.50&access_token="+token;
		
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		// add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		// Send post request
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
			System.out.println(response.toString());
			
		in.close();
		
		if(response.toString().contains("Captcha needed")){
			
			/*ObjectMapper mapper = new ObjectMapper();
			String f=response.toString().substring(9,response.toString().length());
			CaptchaJackson captAnswer = mapper.readValue(f, CaptchaJackson.class);
			System.out.println(captAnswer);*/
			Captcha cap= new Captcha();
			String forSidStr = response.toString();
			String captcha=response.toString();
			String captchaFindStr = "captcha_img";
			int index = captcha.indexOf(captchaFindStr)
					+ captchaFindStr.length() + 3;
			captcha = captcha.substring(index, captcha.length() - 3);
			captcha = captcha.replace("\\", "");
			forSidStr = forSidStr.substring(index, forSidStr.length() - 3);
			int indexSecondSid = forSidStr.indexOf("sid");
			forSidStr = forSidStr.substring(indexSecondSid + 4);
			System.out.println(forSidStr);
			System.out.println(captcha);
			URL url1 = new URL(captcha);
			String capchaText = null;
			while (capchaText == null) {
				try {
					capchaText = cap.sendPost(url1);
				
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			
			System.out.println(capchaText);
		addFriendWithCaptcha(token, userId, forSidStr, capchaText);
		}
		
}
	
	
  void addFriendWithCaptcha(String token,String userId,String sid,String captchaText) throws IOException, InterruptedException{
		
		int responseCode;
		StringBuffer response = null;
		String url = "https://api.vk.com/method/friends.add?user_id="+userId+"&follow=0&text=&v=5.50&access_token="+token+"&captcha_sid="+sid+"&captcha_key="+captchaText;
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		// add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		// Send post request
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
			System.out.println(response.toString());
			
		in.close();
}
	
}



