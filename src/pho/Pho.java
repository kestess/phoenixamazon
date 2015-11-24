package pho;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Pho {
    
    HashMap<String, List<String>> friends;
    HashMap<String, List<String>> courses;
    
    public List<String> getDirectFriendsForUser(String user) {
        return friends.get(user);
    }
    
    public List<String> getAttendedCoursesForUser(String user) {
        return courses.get(user);
    }
    
    public List<String> getRankedCourses(String user) {
        
        List<String> directFriends = getDirectFriendsForUser(user);
        HashSet<String> networkedFriends = new HashSet<>(directFriends);
        directFriends.stream()
                      .map(u -> getDirectFriendsForUser(u))
                      .flatMap(l -> l.stream())
                      // .distinct()
                      .filter(u -> !u.equals(user))
                      .forEach(networkedFriends::add);
        
        System.out.println(networkedFriends);
        
        Map<String, Long> networkedFriendsCourses= networkedFriends.stream()
                      .map(u -> getAttendedCoursesForUser(u))
                      .flatMap(l -> l.stream())
                      .filter(cl -> !getAttendedCoursesForUser(user).contains(cl))
                      .collect(Collectors.groupingBy(o -> o, Collectors.counting()));
        
        System.out.println(networkedFriendsCourses);
                 
        List<String> orderedClasses = networkedFriendsCourses.entrySet()
                      .stream()
                      .sorted(Comparator.comparing(Map.Entry::getValue))
                      .map(Map.Entry::getKey)
                      .collect(Collectors.toList());
        
        Collections.reverse(orderedClasses);
        
        System.out.println(orderedClasses);
        
        return orderedClasses;
    }
    
    Pho() {
       friends = new HashMap<>();
       friends.put("joe",   new ArrayList<>(Arrays.asList("sue", "amy")));
       friends.put("sue",   new ArrayList<>(Arrays.asList("joe", "amy", "carol")));
       friends.put("amy",   new ArrayList<>(Arrays.asList("joe", "sue","alice","bob")));
       friends.put("alice", new ArrayList<>(Arrays.asList("amy")));
       friends.put("bob",   new ArrayList<>(Arrays.asList("amy", "carol", "john")));
       friends.put("carol", new ArrayList<>(Arrays.asList("sue", "bob", "john")));
       friends.put("john",  new ArrayList<>(Arrays.asList("bob", "carol")));
       
       courses = new HashMap<>();
       courses.put("joe",   new ArrayList<>(Arrays.asList("101", "102")));
       courses.put("sue",   new ArrayList<>(Arrays.asList("101", "102", "103")));
       courses.put("amy",   new ArrayList<>(Arrays.asList("101", "102", "103", "104")));
       courses.put("alice",   new ArrayList<>(Arrays.asList("101", "103", "104")));
       courses.put("bob",   new ArrayList<>(Arrays.asList("101", "102", "105")));
       courses.put("carol",   new ArrayList<>(Arrays.asList("102", "108")));
       courses.put("john",   new ArrayList<>(Arrays.asList("102", "110")));
    }

    public static void main(String[] args) { 
        Pho pho = new Pho();
        System.out.println(pho.getRankedCourses("carol"));
    }
    
}
