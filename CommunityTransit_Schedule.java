//	Evan Bariquit
//	CS320, Farag
//	Assignment 1 - Community Transit Schedule
//		This program is meant to pull the list of bus stops of for the Community
//		Transit via https://www.communitytransit.org/busservice/schedules/ .
//		The user enters the initial of their desired destination, and the
//		program lists all matching options. From this list, the user picks and
//		enters a route number. The program then outputs the stop number and
//		location, for both directions of the route.

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;
import java.net.*;

public class CommunityTransit_Schedule {
	public static void main(String[] args) throws Exception {
		
		// Establish connection and setup BufferedReader
		URLConnection ct = new URL("https://www.communitytransit.org/busservice/schedules/").openConnection();
		ct.setRequestProperty("user-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, "
									+ "like Gecko) Chrome/23.0.1271.95 Safari/537.11");
	    BufferedReader in = new BufferedReader(new InputStreamReader(ct.getInputStream()));
        
	    // Parse source code into String
	    String inputLine = "";
        String text = "";
        while ((inputLine = in.readLine()) != null) {
            text += inputLine + "\n";
        }
        //System.out.println(text);					// used for viewing source code & formulating regex
        in.close();									// close buffer
        
        // Prompt user for initial of their destination
        Scanner reader = new Scanner(System.in);
        System.out.print("Enter the 1st letter of your destination: ");
        String initial_str = reader.next();
//        reader.close();
        char initial_lwr = initial_str.charAt(0);
        char initial = Character.toUpperCase(initial_lwr);					// Make the initial UpperCase
        
        // Lists the matches
        Pattern pattern1 = Pattern.compile("<h3>(.*?)</h3>(.*?)<hr", Pattern.DOTALL);
        Matcher matcher1 = pattern1.matcher(text); 
        while(matcher1.find()){
        	String destination = matcher1.group(1);
        	String parseThis = matcher1.group(2);
        	if(destination.charAt(0) == initial) {
            	System.out.println("Destination: " + destination);
            	Pattern pattern2 = Pattern.compile(".*?(<a href=\"/schedules/route/(.*?)\""
            											+ "(>|\\sclass=&quot;text-success&quot;>)(.*?)</a>)+.*?"
            																							, Pattern.DOTALL);
            	Matcher matcher2 = pattern2.matcher(parseThis);
            	while(matcher2.find()){
            			System.out.println("Bus Number: " + matcher2.group(2));
            	}
                System.out.println("+++++++++++++++++++++++++++++++++++");
        	}        	
        }
        
        // Prompt user for their desired route
        //Scanner reader2 = new Scanner(System.in);
        System.out.print("Please enter a route: ");
        String route = reader.next();
        reader.close();
        URL url = new URL("https://www.communitytransit.org/busservice/schedules/route/"+route);
        System.out.println("\nThe link for your route is: " + url + "\n");
        
        // Open and read next page
        ct = url.openConnection();
		ct.setRequestProperty("user-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
	    BufferedReader in2 = new BufferedReader(new InputStreamReader(ct.getInputStream()));
	    String inputLine2 = "";
        String text2 = "";
        while ((inputLine2 = in2.readLine()) != null) {
            text2 += inputLine2 + "\n";
        }
        //System.out.println(text2);					// used for viewing source code & formulating regex
        in2.close();									// close buffer
        
        Pattern pattern3 = Pattern.compile("<h2>.*?<small>(.*?)</small></h2>(.*?)</thead>", Pattern.DOTALL);
        Matcher matcher3 = pattern3.matcher(text2);
        while(matcher3.find()){
        	String routeDest = matcher3.group(1);
        	System.out.println("Direction: " +routeDest);
            Pattern pattern4 = Pattern.compile("<p>(.*?)</p>");	
            Matcher matcher4 = pattern4.matcher(matcher3.group(2));
            int countStops = 1;
            while(matcher4.find()){
            	System.out.println("Stop #"+countStops+": "+matcher4.group(1));
            	countStops++;
            }
            System.out.println("+++++++++++++++++++++++++++++++++++");

        }
        
	} /* Close Main */
	
} /* Close Class */
