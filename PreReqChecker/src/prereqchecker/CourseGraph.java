package prereqchecker;

import java.util.*;

public class CourseGraph {
    private HashMap<String, ArrayList<String>> adjlist;
    private HashMap<String, Boolean> visited;
    private HashSet<String> completedCourses;

    // constructors for course graph
    public CourseGraph(HashMap<String, ArrayList<String>> map) {
        this.adjlist = map;
    }

    public CourseGraph(HashMap<String, ArrayList<String>> map1, HashMap<String, Boolean> map2, HashSet<String> set) {
        this.adjlist = map1;
        this.visited = map2;
        this.completedCourses = set;
    }

    // makes adjlist
    public HashMap<String, ArrayList<String>> makeAdjList(String filename) {
        StdIn.setFile(filename);
        int numClasses = StdIn.readInt();
        for (int i = 0; i < numClasses; i++) {
            String course = StdIn.readString();
            ArrayList<String> list = new ArrayList<String>();
            this.adjlist.put(course, list);
        }
        int numReqs = StdIn.readInt();
        for (int j = 0; j < numReqs; j++) {
            String courseKey = StdIn.readString();
            String prereq = StdIn.readString();

            this.adjlist.get(courseKey).add(prereq);
        }
        return this.adjlist;

    }

    // prints adjlist
    public void printAdjList(String outFile) {
        StdOut.setFile(outFile);
        Iterator it = this.adjlist.entrySet().iterator();
        while (it.hasNext()) {

            Map.Entry mapElement = (Map.Entry) it.next();
            String s = String.valueOf(mapElement.getKey());
            StdOut.print(s);
            StdOut.print(" ");
            for (int i = 0; i < this.adjlist.get(s).size(); i++) {
                StdOut.print(this.adjlist.get(s).get(i));
                StdOut.print(" ");
            }
            StdOut.println();
        }
    }

    public String isValidPreReq(String preReqFile) {
        StdIn.setFile(preReqFile);
        String course1 = StdIn.readString();
        String potentialPreReq = StdIn.readString();
        this.completedCourses = dfs(potentialPreReq, this.completedCourses);

        if (completedCourses.contains(course1)) {
            return "NO";
        } else {
            return "YES";
        }
    }

    public void printIsValidPreReq(String inFile, String outFile) {
        StdOut.setFile(outFile);
        StdOut.println(isValidPreReq(inFile));
    }

    // dfs for valid prereq
    // adds all prereqs to the
    public HashSet<String> dfs(String id, HashSet<String> set) {
        Iterator it = this.adjlist.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry mapElement = (Map.Entry) it.next();
            String s = String.valueOf(mapElement.getKey());
            this.visited.put(s, false);
        }
        dfsUtilValidPrereq(id, this.visited, set);
        return set;
    }

    // finds all the direct and indirect prerequisites of a courseid
    private void dfsUtilValidPrereq(String current, HashMap<String, Boolean> visitedCourses,
            HashSet<String> completedCourses) {
        completedCourses.add(current);
        visitedCourses.replace(current, true);
        if (this.adjlist.get(current) == null)
            return;
        for (String dest : this.adjlist.get(current)) {
            if (!visitedCourses.get(dest)) {
                // adding the class that you've already taken to the
                dfsUtilValidPrereq(dest, visitedCourses, completedCourses);
            }
        }

    }

    // eligible returns a list of all the courses you are able to take
    public ArrayList<String> eligible(String filename) {
        StdIn.setFile(filename);
        // taken is the courses from the input file
        // eligibleCourses is going to be returned
        ArrayList<String> taken = new ArrayList<String>();
        ArrayList<String> eligibleCourses = new ArrayList<String>();
        int numTaken = StdIn.readInt();

        // adding all the courses that you've taken to the taken arraylist
        for (int i = 0; i < numTaken; i++) {
            String courseName = StdIn.readString();
            taken.add(courseName);
        }
        // for every course in the taken, you want to add all its indirect and direct
        // prereqs to a huge hashset
        for (int j = 0; j < taken.size(); j++) {
            String current = taken.get(j);
            this.completedCourses = dfs(current, this.completedCourses);
            for (String s : this.completedCourses) {
                this.completedCourses.add(s);
            }
        }
        Iterator it = this.adjlist.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry mapElement = (Map.Entry) it.next();
            String s = String.valueOf(mapElement.getKey());
            // StdOut.println(s);
            HashSet<String> tempSet = new HashSet<>();
            tempSet = dfs(s, tempSet);
            tempSet.remove(s);
            // StdOut.println(tempSet);
            if (!completedCourses.contains(s)) {
                if (completedCourses.containsAll(tempSet)) {
                    eligibleCourses.add(s);
                }
            }

        }
        return eligibleCourses;
    }

    public void printEligible(String inFile, String outFile) {
        StdOut.setFile(outFile);
        ArrayList<String> eligible = eligible(inFile);
        for (int i = 0; i < eligible.size(); i++) {
            StdOut.println(eligible.get(i));
        }
    }

    public HashSet<String> needToTake(String filename) {
        StdIn.setFile(filename);

        // first we want to read the target then read the number of taken
        String target = StdIn.readString();
        int numTaken = StdIn.readInt();
        ArrayList<String> taken = new ArrayList<>();
        HashSet<String> targetPrereqs = new HashSet<>();
        ArrayList<String> toRemove = new ArrayList<>();

        for (int i = 0; i < numTaken; i++) {
            String course = StdIn.readString();
            taken.add(course);
        }
        // add all indirect and direct prereqs to completed
        for (int j = 0; j < taken.size(); j++) {
            String current = taken.get(j);
            this.completedCourses = dfs(current, this.completedCourses);
            for (String s : this.completedCourses) {
                this.completedCourses.add(s);
            }
        }
        targetPrereqs = dfs(target, targetPrereqs);
        for (String prereq : targetPrereqs) {
            if (completedCourses.contains(prereq)) {
                toRemove.add(prereq);
            }
        }
        for (int x = 0; x < toRemove.size(); x++) {
            targetPrereqs.remove(toRemove.get(x));
        }
        targetPrereqs.remove(target);
        return targetPrereqs;
    }

    public HashSet<String> needToTakeInternal(HashSet<String> classesTaken, String target) {
        HashSet<String> targetPrereqs = new HashSet<>();
        ArrayList<String> toRemove = new ArrayList<>();

        // add all indirect and direct prereqs to completed
        addAllCourses(classesTaken, this.completedCourses);

        targetPrereqs = dfs(target, targetPrereqs);

        for (String prereq : targetPrereqs) {
            if (completedCourses.contains(prereq)) {
                toRemove.add(prereq);
            }
        }
        for (int x = 0; x < toRemove.size(); x++) {
            targetPrereqs.remove(toRemove.get(x));
        }
        targetPrereqs.remove(target);
        return targetPrereqs;
    }

    // adds all prereq indirect and direct from a collection to a collection
    public void addAllCourses(HashSet<String> set1, HashSet<String> set2) {
        for (String j : set1) {
            set2 = dfs(j, set2);
            for (String s : set2) {
                set2.add(s);
            }
        }
    }

    public void printNeedToTake(String filename, HashSet<String> set) {
        StdOut.setFile(filename);
        for (String left : set) {
            StdOut.println(left);
        }
    }

    public void clearCompletedCourses() {
        this.completedCourses.clear();
    }

    public HashMap<Integer, HashSet<String>> schedulePlan(String filename) {
        StdIn.setFile(filename);
        clearCompletedCourses();
        String targetCourse = StdIn.readString();
        int numTaken = StdIn.readInt();
        HashSet<String> taken = new HashSet<>();
        for (int i = 0; i < numTaken; i++) {
            taken.add(StdIn.readString());
        }

        addAllCourses(taken, this.completedCourses);
        taken.addAll(this.completedCourses);
        HashSet<String> needToTake = needToTakeInternal(taken, targetCourse);
        HashMap<Integer, HashSet<String>> schedule = new HashMap<>();
        HashSet<String> currentSemester = new HashSet<>();
        HashSet<String> prereqs = new HashSet<>();

        int count = 1;
        while (!needToTake.isEmpty()) {
            for (String s : needToTake) {
                prereqs.addAll(dfs(s, prereqs));
                prereqs.remove(s);
                if (prereqs.isEmpty() || taken.containsAll(prereqs)) {
                    currentSemester.add(s);
                }
                prereqs.clear();
            }
            if (!currentSemester.isEmpty()) {
                HashSet<String> copy = new HashSet<>();
                copy.addAll(currentSemester);
                schedule.put(count, copy);
                needToTake.removeAll(copy);
                taken.addAll(copy);
                currentSemester.clear();
                count++;
            }

        }
        return schedule;

    }

    public void printSchedule(String fileIn, String fileOut) {
        HashMap<Integer, HashSet<String>> schedule = schedulePlan(fileIn);
        StdOut.setFile(fileOut);
        StdOut.println(schedule.size());
        Iterator it = schedule.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry mapElement = (Map.Entry) it.next();
            int semester = Integer.parseInt(String.valueOf(mapElement.getKey()));
            for (String s : schedule.get(semester)) {
                StdOut.print(s + " ");
            }
            StdOut.println();
        }
    }

    public HashSet<String> getCompletedCourses() {
        return completedCourses;
    }

    public HashMap<String, ArrayList<String>> getAdjList() {
        return adjlist;
    }

}
