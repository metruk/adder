import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GroupGetId {

	Document groupConnector(String href){
		Document doc = null;
		while(doc==null){
			try {
				doc = Jsoup.connect(href).get();
			}catch(java.net.UnknownHostException ex){
				ex.printStackTrace();
				System.out.println("UnknownHostException");
			}catch(java.lang.IllegalArgumentException ex){
				ex.printStackTrace();
				System.out.println("Argument");
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return doc;
	}
	
	String regexAlbum(String pattern,String matcher,int group){
		Pattern patt = Pattern.compile(pattern);
		Matcher match = patt.matcher(matcher);
		String albumHref="";
		if (match.find()) {
			albumHref += match.group(group);
		}	
	return albumHref;
	}
	
	String getGroupId(String vkHref) {

		Document doc = groupConnector(vkHref);
		Element id = doc.body();
			//class="profile_menu
		Elements profileMenu =id.select("ul[class=profile_menu]");
		Elements links = profileMenu.select("a[href]");
		
		String albumPattern="(a href=\"\\/videos)(-[0-9]+)";
		
		String ownerId=regexAlbum(albumPattern, links.toString(),2);	
		
		return ownerId;
		}
}
