package quartzTest;

import org.neo4j.cypher.internal.compiler.v2_1.docbuilders.internalDocBuilder;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;

import java.util.ArrayList;

public class HandleNeo {
    private static final String DB_PATH = "/home/shanlihou/Downloads/neo4j-community-2.2.0/data/new.db";
    private static final String USER_NAME = "user_name";

    private static GraphDatabaseService graphDb;
    private static Index<Node> indexService;
    private ArrayList<String> information;

    private static enum RelTypes implements RelationshipType {
        KNOWS
    };

    private HandleNeo() {
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
        registerShutdownHook();
        try ( Transaction tx = graphDb.beginTx() ){
        	indexService = graphDb.index().forNodes( "nodes" );
            tx.success();
        }

    } 

    private static final HandleNeo single = new HandleNeo();  

    public static HandleNeo getInstance() {  
        return single;  
    }
    public void init(){
        information = new ArrayList<>();
        information.add("home");
        information.add("head portrait");
        information.add("sex");
        information.add("person status");
        information.add("update status");
        information.add("phone number");
        for (int i = 0; i < information.size(); i++){
            System.out.println(information.get(i));
        }
    }
    
    private static void registerShutdownHook(){
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running example before it's completed)
        Runtime.getRuntime().addShutdownHook( new Thread() {
            @Override
            public void run() {
                graphDb.shutdown();
            }
        } );
    }
    /*true:register
     *false:none
     * */
    private Node getRegisterUser(String user_name){
    	Node node = indexService.get(USER_NAME, user_name).getSingle();
    	if ( node == null ){
    		return null;
    	}
    	else {
			return node;
		}
    }

    private Node createNode(String name){
        System.out.println(getRegisterUser(name));
        System.out.println(name);
        if (getRegisterUser(name) != null)
        {
            System.out.println("has existed:" + name);
            return null;
        }
        else
        {
            System.out.println("add node:" + name);
            Label label;
            Node node = graphDb.createNode();
            node.setProperty(USER_NAME, name);
            indexService.add(node, USER_NAME, name);
            return node;
        }
    }
    private void makeFriends(Node user1, Node user2){
        Relationship rel = user1.createRelationshipTo(user2, RelTypes.KNOWS);
        rel = user2.createRelationshipTo(user1, RelTypes.KNOWS);
        System.out.println(user1.getRelationships().toString());
    }
    
    public void shutdownGraphDb(){
    	graphDb.shutdown();
    }

    public void testNeo(){
        Transaction tx =  graphDb.beginTx();
        try {
            createNode("liutong");
            createNode("chenkezheng");
            System.out.println(getRegisterUser("liutong"));
            System.out.println(getRegisterUser("chenkezheng"));
            makeFriends(getRegisterUser("liutong"), getRegisterUser("chenkezheng"));
            tx.success();
        }catch(Exception ex){
            ex.printStackTrace();
            tx.failure();
        }finally{
            tx.close();
        }
    }
}
