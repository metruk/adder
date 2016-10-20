import java.io.BufferedReader;
import java.io.InputStreamReader;


public class Main {

	
	public static void main(String[] args) throws Exception {
		String id=null;
		String token = null;
		int groupId = 0;
			while(id==null){
				try{
					BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
					System.out.println("Enter an acess token");
					token=br.readLine();
					System.out.println(token);
					System.out.println("Enter group id");
					id=br.readLine();
					groupId=Integer.parseInt(id);
				}catch(java.lang.NumberFormatException ex){
					System.out.println("Invalid format");
					id=null;
				}
			}
		ParserService ps = new ParserService();
		Adder add= new Adder(token,groupId);
		add.addToFriend();
	}
}
