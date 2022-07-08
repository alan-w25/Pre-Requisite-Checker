package prereqchecker;

import java.util.*;

/**
 * Steps to implement this class main method:
 * 
 * Step 1:
 * AdjListInputFile name is passed through the command line as args[0]
 * Read from AdjListInputFile with the format:
 * 1. a (int): number of courses in the graph
 * 2. a lines, each with 1 course ID
 * 3. b (int): number of edges in the graph
 * 4. b lines, each with a source ID
 * 
 * Step 2:
 * SchedulePlanInputFile name is passed through the command line as args[1]
 * Read from SchedulePlanInputFile with the format:
 * 1. One line containing a course ID
 * 2. c (int): number of courses
 * 3. c lines, each with one course ID
 * 
 * Step 3:
 * SchedulePlanOutputFile name is passed through the command line as args[2]
 * Output to SchedulePlanOutputFile with the format:
 * 1. One line containing an int c, the number of semesters required to take the course
 * 2. c lines, each with space separated course ID's
 */
public class SchedulePlan {
    public static void main(String[] args) {

        if ( args.length < 3 ) {
            StdOut.println("Execute: java -cp bin prereqchecker.SchedulePlan <adjacency list INput file> <schedule plan INput file> <schedule plan OUTput file>");
            return;
        }

	    // WRITE YOUR CODE HERE
        HashMap<String,ArrayList<String>> adjlist = new HashMap<>();
        HashSet<String> completedCourses = new HashSet<>();
        HashMap<String,Boolean> visited = new HashMap<>();
        CourseGraph graph = new CourseGraph(adjlist,visited,completedCourses);
        graph.makeAdjList(args[0]);

        graph.printSchedule(args[1],args[2]);

        /*&StdIn.setFile(args[1]);
        String targetCourse = StdIn.readString(); 
        System.out.println(targetCourse);
        int numTaken = StdIn.readInt(); 
        HashSet<String> taken = new HashSet<>();
        for(int i = 0; i < numTaken; i++){
            taken.add(StdIn.readString());
        }
        graph.addAllCourses(taken, completedCourses);
        System.out.println(completedCourses);
        taken.addAll(completedCourses);
        System.out.println(taken);

        HashSet<String> needToTake = graph.needToTakeInternal(taken,targetCourse);
        System.out.println(needToTake);

        HashMap<Integer,HashSet<String>> schedule = new HashMap<>(); 
        HashSet<String> currentSemester = new HashSet<>(); 
        HashSet<String> prereqs = new HashSet<>(); 
        int count = 1; 

        
        
       while(!needToTake.isEmpty()){
            for(String s : needToTake){
            prereqs.addAll(graph.dfs(s,prereqs));
            prereqs.remove(s);
            System.out.println("prereqs for : " + s + " " +prereqs);
                if(prereqs.isEmpty() || taken.containsAll(prereqs)){
                    currentSemester.add(s);
                    System.out.println("current Semester" + currentSemester);
                }
                prereqs.clear();
            }
            if(!currentSemester.isEmpty()){
                HashSet<String> copy = new HashSet<>(); 
                copy.addAll(currentSemester);
                schedule.put(count,copy);
                needToTake.removeAll(copy);
                System.out.println("need to take: " + needToTake);
                taken.addAll(copy);
                System.out.println("taken: " + taken);
                currentSemester.clear(); 
                count++; 
            }

        }
        System.out.println(schedule);*/
        
    }
}
