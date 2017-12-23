package infosys_group2_4;

import com.google.api.server.spi.config.*;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.apphosting.api.ApiProxy;
import infosys_group2_4.variables.Request;
import infosys_group2_4.variables.User;

import javax.servlet.ServletException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.*;

import static java.lang.System.currentTimeMillis;

/**
  * Add your first API methods in this class, or you may create another class. In that case, please
  * update your web.xml accordingly.
 **/


@Api(name = "sos",
     version = "v1",
        namespace =
        @ApiNamespace(
        ownerDomain = "echo.example.com",
        ownerName = "echo.example.com",
        packagePath = ""
),
        // [START_EXCLUDE]
        issuers = {
                @ApiIssuer(
                        name = "firebase",
                        issuer = "https://securetoken.google.com/YOUR-PROJECT-ID",
                        jwksUri = "https://www.googleapis.com/robot/v1/metadata/x509/securetoken@system.gserviceaccount.com")
        }
        // [END_EXCLUDE]
)
public class YourFirstAPI {

    @ApiMethod(name = "new_user",path = "user/new", description = "POST new user details, returns userID if true, 0 if false")
    public List<Integer> newUser(User user){
        String insertNew = "INSERT INTO User (Username,PassHash,PassSalt,Token,displayName) VALUES (?,?,?,?,?)";
        String getID = "SELECT UserID FROM User WHERE Username = ?";
        int ID = 0;
        List<Integer> l = new LinkedList<>();
        try {
            Connection c = connectToDB();

            PreparedStatement s = c.prepareStatement(getID);
            s.setString(1,user.getUsername());
            boolean r = s.execute();
            if (!r){
                l.add(0);
                return l;
            }

            Long val = System.currentTimeMillis();
            String userSalt = sha256(val.toString());
            user.setPassHash(sha256(user.getPassHash()+userSalt));

            PreparedStatement statement = c.prepareStatement(insertNew);
            statement.setString(1,user.getUsername());
            statement.setString(2,user.getPassHash());
            statement.setString(3,userSalt);
            statement.setString(4,user.getToken());
            statement.setString(5,user.getDisplayName());
            statement.executeUpdate();

            PreparedStatement statement2 = c.prepareStatement(getID);
            statement2.setString(1,user.getUsername());
            r = statement2.execute();
            int e = 0;
            if (r){
                e = 1;
            }
            l.add(e);
            return l;

        } catch (SQLException e){
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        }
        l.add(0);
        return l;
    }




    @ApiMethod(name = "authenticate_user",path = "user/authenticate", description = "POST username and passHash, return UserID if pass, 0 if fail ")
    public List<Integer> authen_user(User user){

        String getSaltPass = "SELECT PassSalt,PassHash FROM User WHERE Username = ?";
        String getID = "SELECT UserID FROM User WHERE Username = ?";
        List<Integer> l = new LinkedList<>();

        try {
            Connection c = connectToDB();
            PreparedStatement statement = c.prepareStatement(getSaltPass);
            statement.setString(1,user.getUsername());
            ResultSet r = statement.executeQuery();
            String[] array = new String[2];
            while (r.next()) {
                array[0] = r.getString("PassHash");
                array[1] = r.getString("PassSalt");
            }
            if (array[0] == null||array[0].isEmpty()){
                l.add(0);
                return l;
            }

            if (array[0].equals(sha256(user.getPassHash()+array[1]))){
                statement = c.prepareStatement(getID);
                statement.setString(1,user.getUsername());
                ResultSet r2 = statement.executeQuery();
                while (r2.next()){
                    l.add(r2.getInt("UserID"));
                }

                return l;

            } else {
                l.add(0);
                return l;
            }


        } catch (ServletException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        l.add(-1);
        return l;
    }


    private String sha256(String s){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(s.getBytes());
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return s;
    }

    @ApiMethod(name = "delete_request",path = "request/delete",description = "DELETE a request")
    public boolean[] deleteRequest(@Named("RQID") int RQID){
        String deleteRequest = "UPDATE Request SET IsDeleted = ? WHERE RQID = ?";
        boolean[] result = new boolean[1];
        try{
            Connection c = connectToDB();
            PreparedStatement p = c.prepareStatement(deleteRequest);
            p.setInt(1,1);
            p.setInt(2,RQID);
            p.executeUpdate();
            result[0] = true;
            return result;

        } catch (SQLException e) {
            result[0] = false;
            e.printStackTrace();
            return result;

        } catch (ServletException e) {
            result[0] = false;
            e.printStackTrace();
            return result;

        }
    }


    @ApiMethod(name = "accept_request", path = "request/accept",description = "ACCEPT a new request")
    public boolean[] acceptRequest(Request request){
        String acceptRequest = "UPDATE Request SET AcceptID = ? WHERE RQID = ?";
        String checkID = "SELECT * FROM User WHERE UserID = ?";
        String checkRequest = "SELECT AcceptID FROM Request WHERE RQID = ?";

        boolean[] result = new boolean[1];
        try{

            Connection c = connectToDB();
            PreparedStatement p = c.prepareStatement(checkID);
            p.setInt(1,request.getAcceptId());
            ResultSet r = p.executeQuery();
            if (!r.next()){
                return result;
            }
            p = c.prepareStatement(checkRequest);
            p.setInt(1,request.getRqId());
            r = p.executeQuery();
            if (!r.next()){
                return result;
            } else if (r.getInt("AcceptID") != 0){
                return result;
            }


            p = c.prepareStatement(acceptRequest);
            p.setInt(1,request.getAcceptId());
            p.setInt(2,request.getRqId());
            p.executeUpdate();
            result[0] = true;
            return result;

        } catch (SQLException e) {
            result[0] = false;
            e.printStackTrace();
            return result;

        } catch (ServletException e) {
            result[0] = false;
            e.printStackTrace();
            return result;

        }


    }
    @ApiMethod(name = "new_request", path = "request/new", description = "POST a new request")
    public boolean[] newRequest(Request newrequest){
        String newRQ = "INSERT INTO Request (RequesterID,Priority,Title,Description,Location,BestBy) VALUES (?,?,?,?,?,?)";
        String[] priority = new String[]{"LOW","MED","HIGH","SOS"};
        boolean[] result = new boolean[1];
        try{
            if (!Arrays.asList(priority).contains(newrequest.getPriority())){
                return result;
            }
            Connection c = connectToDB();
            PreparedStatement p = c.prepareStatement(newRQ);
            p.setInt(1,newrequest.getRequesterId());
            p.setString(2,newrequest.getPriority());
            p.setString(3,newrequest.getTitle());
            p.setString(4,newrequest.getDescription());
            p.setString(5,newrequest.getLocation());
            p.setString(6,newrequest.getBestByString());
            p.executeUpdate();
            result[0] = true;
            return result;

        } catch (SQLException e) {
            result[0] = false;
            e.printStackTrace();
            return result;

        } catch (ServletException e) {
            result[0] = false;
            e.printStackTrace();
            return result;

        }

    }

    @ApiMethod(name = "get_all_requests",path = "request/all", description = "get all requests from DB to populate main activity", httpMethod = ApiMethod.HttpMethod.GET)
    public List<Request> getAllRequest(){
        String getAllRQ = "SELECT * FROM Request WHERE AcceptID IS NULL AND isDeleted = 0";
        List<Request> result = new LinkedList<>();

        try {
            Connection c = connectToDB();
            PreparedStatement p = c.prepareStatement(getAllRQ);
            ResultSet r = p.executeQuery();
            while (r.next()){
                Request newrequest = new Request();
                newrequest.setTitle(r.getString("Title"));
                newrequest.setBestBy(r.getTimestamp("BestBy"));
                newrequest.setDescription(r.getString("Description"));
                newrequest.setPriority(r.getString("Priority"));
                newrequest.setRequesterId(r.getInt("RequesterID"));
                newrequest.setLocation(r.getString("Location"));
                newrequest.setRqId(r.getInt("RQID"));
                result.add(newrequest);
            }

            return result;

        } catch (ServletException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    private Connection connectToDB() throws ServletException {
        Connection conn;
        ApiProxy.Environment env = ApiProxy.getCurrentEnvironment();
        Map<String, Object> attr = env.getAttributes();
        String hostname = (String) attr.get("com.google.appengine.runtime.default_version_hostname");

        String url = hostname.contains("localhost:")
                ? System.getProperty("cloudsql-local") : System.getProperty("cloudsql");
        try {
            conn = DriverManager.getConnection(url);
            return conn;
        } catch (SQLException e) {
            throw new ServletException("Unable to connect to Cloud SQL", e);
        }
    }

    @ApiMethod(name = "get_user",path = "user/get", description = "get user from ID")
    public User get_user(@Named("id")Integer id){
        String getUser = "SELECT Token,DisplayName,UserID FROM User WHERE UserID = ?";
        User user = new User();
        try {

            Connection c = connectToDB();
            PreparedStatement p = c.prepareStatement(getUser);
            p.setInt(1,id);
            ResultSet r = p.executeQuery();
            while (r.next()){

                user.setUsername(r.getString("DisplayName"));
                user.setToken(r.getString("Token"));
                user.setUserID(r.getInt("UserID"));

            }
            return user;

        } catch (ServletException e) {
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    @ApiMethod(name = "get_my_accepted",path = "request/accepted",description = "get all request i have accepted")
    public List<Request> getMyAccepted(@Named("id")Integer id){
        String getAllRQ = "SELECT * FROM Request WHERE AcceptID = ? AND isDeleted = 0";
        List<Request> result = new LinkedList<>();

        try {
            Connection c = connectToDB();
            PreparedStatement p = c.prepareStatement(getAllRQ);
            p.setInt(1,id);
            ResultSet r = p.executeQuery();
            while (r.next()){
                Request newrequest = new Request();
                newrequest.setTitle(r.getString("Title"));
                newrequest.setBestBy(r.getTimestamp("BestBy"));
                newrequest.setDescription(r.getString("Description"));
                newrequest.setPriority(r.getString("Priority"));
                newrequest.setRequesterId(r.getInt("RequesterID"));
                newrequest.setLocation(r.getString("Location"));
                newrequest.setAcceptId(r.getInt("AcceptID"));
                newrequest.setRqId(r.getInt("RQID"));
                result.add(newrequest);
            }

            return result;

        } catch (ServletException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    @ApiMethod(name = "get_my_requests",path = "request/myrequests",description = "get all request i have posted")
    public List<Request> getMyRequests(@Named("id")Integer id){
        String getAllRQ = "SELECT * FROM Request WHERE RequesterID = ? AND isDeleted = 0";
        List<Request> result = new LinkedList<>();

        try {
            Connection c = connectToDB();
            PreparedStatement p = c.prepareStatement(getAllRQ);
            p.setInt(1,id);
            ResultSet r = p.executeQuery();
            while (r.next()){
                Request newrequest = new Request();
                newrequest.setTitle(r.getString("Title"));
                newrequest.setBestBy(r.getTimestamp("BestBy"));
                newrequest.setDescription(r.getString("Description"));
                newrequest.setPriority(r.getString("Priority"));
                newrequest.setRequesterId(r.getInt("RequesterID"));
                newrequest.setLocation(r.getString("Location"));
                newrequest.setRqId(r.getInt("RQID"));
                newrequest.setAcceptId(r.getInt("AcceptID"));
                result.add(newrequest);
            }

            return result;

        } catch (ServletException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

}





