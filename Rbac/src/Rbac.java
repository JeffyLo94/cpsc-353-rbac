/*
 * @Rbac.java
 * @author Jeffrey Lo on 2019-02-16
 *
 */

import javax.print.DocFlavor;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Rbac {

    public Map<String, ArrayList<String>> userRoleMap;
    public Map<String, ArrayList<String>> permissionsMap;
    public String lastRoleGrantedPermition = "";

    Rbac(){
        System.out.print("RBAC initialized");
        userRoleMap = new HashMap<String, ArrayList<String>>();
        permissionsMap = new HashMap<String, ArrayList<String>>();
    }

    public void readRBACFiles(String uraDir, String praDir) throws IOException {
        //TODO: Read files into stuff
        if (userRoleMap == null){
            userRoleMap = new HashMap<String, ArrayList<String>>();
        }
        if (permissionsMap == null){
            permissionsMap = new HashMap<String, ArrayList<String>>();
        }

        //URA processing
        FileReader fileIn = new FileReader(uraDir);
        BufferedReader bufRead = new BufferedReader(fileIn);
        String myLine = null;

        while ( (myLine = bufRead.readLine()) != null)
        {
            String[] uraLine = myLine.split(" ");
            String name = uraLine[0];
            String role = uraLine[1];

            //check if name exists
            setMapValues(name, role, userRoleMap);
        }
//        printUserRoles();

        //PRA processing
        fileIn = new FileReader(praDir);
        bufRead = new BufferedReader(fileIn);
        while ( (myLine = bufRead.readLine()) != null){
            String[] praLine= myLine.split(" ");
            String role = praLine[0];
            String permission = praLine[1] + " " + praLine[2];

            setMapValues(role, permission, permissionsMap);
        }
//        printPermissions();

    }

    private void setMapValues(String key, String value, Map<String, ArrayList<String>> targetMap) {
        if (targetMap.get(key) == null){
            //create new entry and add role
            targetMap.put(key, new ArrayList<String>());
            targetMap.get(key).add(value);
        } else {
            //add role to user
            targetMap.get(key).add(value);
        }
    }

    public void printUserRoles(){
        System.out.println(userRoleMap);
    }

    public void printRolesFor(String usr){
        ArrayList<String> roles = userRoleMap.get(usr);
        for(String role : roles){
            System.out.print(role + "\t");
        }
        System.out.println("\n");
    }

    public String getRoles(String usr){
        ArrayList<String> roles = userRoleMap.get(usr);
        String list = "";
        for(String role : roles){
            list.concat(role + " ");
        }
        return list;
    }

    public void printPermissions(){
        System.out.println(permissionsMap);
    }

    public void printPermissionsFor(String usr){
        ArrayList<String> roles = userRoleMap.get(usr);
        for(String oneRole : roles){
            ArrayList<String> permits = permissionsMap.get(oneRole);
            System.out.println("as " + oneRole + ":");
            for(String permission : permits){
                System.out.print("\t" + permission);
            }
            System.out.print("\n");
        }
        System.out.print("\n");
    }

    public boolean isValidUser(String test){
        if(userRoleMap.get(test)==null){
            return false;
        }
        return true;
    }

    public boolean isPermitted(String usr, String in){
        ArrayList<String> roles = userRoleMap.get(usr);
        for(String oneRole : roles){
            ArrayList<String> permits = permissionsMap.get(oneRole);
            for(String permission : permits){
                if( permission.equals(in)){
                    lastRoleGrantedPermition = oneRole;
                    return true;
                }
            }
        }

        return false;
    }


    // IO Handled Here

}

class main {
    private static void print(String str){
        System.out.println(str);
    }

    public static void main(String[] args) throws IOException {
        final Rbac RBAC = new Rbac();
        String URA_FILE = "URA.txt";
        String PRA_FILE = "PRA.txt";

        if(args.length != 0){
            if (args[1] instanceof String){
                String path = args[1];
                URA_FILE = path + URA_FILE;
                PRA_FILE = path + PRA_FILE;
            } else{
                System.out.print("Warning - missing argument for path to RBAC setup directory - using fallback");
            }
        } else {

            URA_FILE = URA_FILE;
            PRA_FILE = PRA_FILE;
        }


//        final String URA_FILE = "/Users/jeffreylo/CSUFDevelopment/cpsc-353-rbac/inFiles/URA.txt";
//        final String PRA_FILE = "/Users/jeffreylo/CSUFDevelopment/cpsc-353-rbac/inFiles/PRA.txt";

        try{
            RBAC.readRBACFiles(URA_FILE,PRA_FILE);
            print("RBAC initialized");
        } catch (Exception e){
            print("Error - Failed to initialize RBAC - could not read URA.txt and/or PRA.txt");

        }

        //Enter data using BufferReader
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));

        boolean loggedIn = false;

        while(!loggedIn){
            //login
            System.out.print("login: ");
            String name = reader.readLine();
            if (!RBAC.isValidUser(name)) {
                print("Error: user " + name + " is not in the database!");
            } else {
                print("Welcome " + name + "!");
                loggedIn = true;

                //command prompt loop
                final String PROMPT = "cmd> ";
                String cmd;
                do{
                    System.out.print(PROMPT);
                    cmd = reader.readLine();
                    if(!isQuit(cmd)){
                        if(cmd.equals("roles")){
                            RBAC.printRolesFor(name);
                        } else if(cmd.equals("permits")){
                            RBAC.printPermissionsFor(name);
                        }else {
                            boolean ok = RBAC.isPermitted(name, cmd);
                            if(ok){
                                String grantedStr = "Access granted by virtue of roles: " + RBAC.lastRoleGrantedPermition;
                                print(grantedStr);
                            } else {
                                print("Access Denied: You are not authorized to perform this action");
                            }
                        }
                    }
                }while(!isQuit(cmd));
            }
        }

        print("program ending...");
    }

    private static boolean isQuit(String command){
        command = command.toLowerCase();
        if(command.equals("q") || command.equals("quit") || command.equals("logout")){
            return true;
        }
        return false;
    }
}

