package quartzTest;

import org.neo4j.cypher.internal.compiler.v2_1.docbuilders.internalDocBuilder;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class HandleRequest {
	private enum ReqType{
		SIGN_UP,
		UPDATE
	}
	private HandleRequest(){
		
	}
    private static final String REQ_TYPE = "req_type";
    private static final String CONTENT = "content";
    private static final String NAME = "name";
    private static final String TELEPHONE = "telephone";

    private static final HandleRequest single = new HandleRequest();      
    
    public static HandleRequest getInstance() {  
        return single;  
    }  
    
	public String handleJson( JSONObject json ){
		JSONObject ret = new JSONObject();
		if (json.get( "req_type" ) == null){
			ret.put( "error", 10001 );
			return ret.toString();
		}
		int reqType = json.getInt( "req_type" );
		switch (ReqType.values()[reqType]) {
		case UPDATE:
			updateAllContacts(json, ret);
			break;
		case SIGN_UP:
			break;
		default:
			break;
		}
		return ret.toString();
	}
	
	private void signUpUser( JSONObject json, JSONObject ret){
		
	}
	
	private void updateAllContacts( JSONObject json, JSONObject ret ){
		JSONArray ja = json.getJSONArray( CONTENT );
		//TODO
		for (int i = 0; i < ja.size(); i++){
			System.out.println( ja.getJSONObject(i).getString(NAME) );
			System.out.println( ja.getJSONObject(i).getString(TELEPHONE) );
			
		}
		
	}
}
