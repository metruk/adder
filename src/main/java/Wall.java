import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true)

public class Wall {

	
	String count;
	List items;
	String ownerId;
	

	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public List getItems() {
		return items;
	}
	public int getOriginalId() {
		int id = 0;
		int postAmount=items.size();
		Map<String,String> postDetails= new LinkedHashMap<String,String>();
			if(postAmount>0){
				postDetails=(Map<String, String>) items.get(0);
				for (Entry entry : postDetails.entrySet()) {
				     if(entry.getKey().equals("owner_id")){
				    	 id=(Integer) entry.getValue();
				     }
				}
			}
		return id;
	}
	public void setItems(List items) {
		this.items = items;
	}
	public String getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	@Override
	public String toString() {
		return "User [count=" + count + ", items="
				+ items + ", ownerId=" + ownerId + "]";
	}

}
