package App;

import javax.management.NotificationEmitter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class Graph {
    private Map<Integer, List<Vetex>> result = new HashMap<>();
    private Map<Vetex,Boolean> isColored = new HashMap<>() ;
    private Map<Vetex,List<Vetex>> adjustList = new HashMap<>() ;
    private int totalOfSubject  ;
    private Map<String, Vetex> mapAllVetex = new HashMap<>() ;

    public Graph() throws IOException {
        System.out.println("default constructor");

    }




    public void initGraphFromTheFiles(String studentPath,String allSubjectsPath) throws IOException {




           // init Vetex
//            Set<String> allSubjects = new HashSet<>();
//            students.stream().forEach(student -> {
//                allSubjects.addAll(student.getSubjects());
//            });
//
//            allSubjects.stream().forEach(subject -> allVetexs.add(new Vetex(subject)));


        Path subjectsPath = Paths.get(allSubjectsPath);
        List<String> subjectLines = Files.readAllLines(subjectsPath);



        subjectLines.stream().forEach(subjectLine -> {
//
          mapAllVetex.put(subjectLine,new Vetex(subjectLine));

        });

        // init total
        totalOfSubject = mapAllVetex.size();


        // init all Student

        Path stdPath = Paths.get(studentPath);
        List<String> studentsLines = Files.readAllLines(stdPath);
        List<Student> students = new ArrayList<>();


        studentsLines.stream().forEach(line -> {
            String name = line.split(":")[0];
            List<String> subjects = Arrays.asList(line.split(":")[1].split(","));
            List<Vetex> subjectOfStudent = new ArrayList<>();
            subjects.stream().forEach(str -> subjectOfStudent.add(mapAllVetex.get(str)));
            students.add(new Student(name,subjectOfStudent));
        });
//        students.stream().forEach(student -> System.out.println(student));


            this.isColored = this.mapAllVetex.entrySet().stream().map(entry -> entry.getValue()).collect(Collectors.toMap(vetex -> vetex , vetex -> false));


            // init adjustList

            mapAllVetex.values().stream().forEach(vetex -> {
                System.out.println(vetex);
                Set<Vetex> conflictVetex = new HashSet<>();
                students.stream().filter(student -> {
//                   System.out.println(student);
                   return student.getSubjects().contains(vetex);
               }).forEach(student -> conflictVetex.addAll(student.getSubjects()));
                List<Vetex> incommingCoflict = new ArrayList<>(conflictVetex).stream().filter(vetexCurr -> !vetexCurr.equals(vetex)).collect(Collectors.toList());
                adjustList.put(vetex,incommingCoflict);
//                System.out.print(vetex+" --> " +incommingCoflict);
//                System.out.println();
            });


    }





    public void coloring(){

//



        // print adjust List
        this.adjustList.entrySet().stream().forEach(entryVetex -> {
            System.out.print(entryVetex.getKey()+"--");
            List<Vetex> conflictVetex = entryVetex.getValue();
            conflictVetex.stream().forEach( vetex -> System.out.print(vetex+" "));
            System.out.println();

        });


        int count = 1 ;

        List<Vetex> currentVetex = new ArrayList<>();

        List<Vetex> NotColoring = null ;
        Set<Vetex> NotColoringSet = null ;
        Vetex firstNotColoringVetex = null ;

        Set<Vetex> notColoringSecond = null;



        while(isColored.entrySet().stream().filter(entryVetex -> entryVetex.getValue() == true).count() != totalOfSubject ){
            System.out.println("con dinh chua to");
             NotColoring = isColored.entrySet().stream().filter(entryVetex -> entryVetex.getValue() != true).map(entryVetex -> entryVetex.getKey()).collect(Collectors.toList());
             NotColoring  = NotColoring.stream().sorted(Vetex::compareTo).collect(Collectors.toList());
//             NotColoringSet = new TreeSet<>(NotColoring);
            System.out.print("not coloring set");
//             NotColoringSet.stream().forEach(t -> System.out.print(t));
            System.out.println();

             firstNotColoringVetex = NotColoring.get(0);
            System.out.println("current Vetex " +firstNotColoringVetex);
            // current list Vetex
            currentVetex.add(firstNotColoringVetex);

            // add zo da to mau
            isColored.put(firstNotColoringVetex,true);

            // add zo result
            result.put(count,currentVetex);

            // tim cai chua to mau

            NotColoring = isColored.entrySet().stream().filter(entryVetex -> entryVetex.getValue() != true).map(entryVetex -> entryVetex.getKey()).collect(Collectors.toList());
            NotColoringSet = new TreeSet<>(NotColoring);

            Iterator<Vetex> itr = NotColoringSet.iterator();
            while(itr.hasNext()){
                Vetex incomingVetex = itr.next();
               if(isConflict(adjustList.get(incomingVetex),currentVetex)){
                   currentVetex.add(incomingVetex);
                   isColored.put(incomingVetex,true);

                }
            }
            result.remove(count);
            result.put(count,new ArrayList<>(currentVetex));
            currentVetex = new ArrayList<>() ;

            count ++ ;

        }
    }


    public void printResult(){

        this.result.entrySet().stream().forEach(entry -> {
            System.out.print(entry.getKey() + " -----");
            entry.getValue().stream().forEach(entryVetex -> System.out.print(entryVetex+"  "));
            System.out.println();
        });
    }


    public void saveResultIntoFile(String fileSave) throws IOException {
        List<String> resultSave = new ArrayList<>();

        this.result.entrySet().stream().forEach(entry -> {
            StringBuilder sb = new StringBuilder();
            sb.append(entry.getKey() + " -----");
            entry.getValue().stream().forEach(entryVetex -> sb.append(entryVetex+"  "));
            resultSave.add(sb.toString());
        });
        Files.write(Paths.get(fileSave),resultSave, StandardOpenOption.APPEND);
    }


    private boolean isConflict(List<Vetex> conflictOfVetex,List<Vetex> currentListVetex){
            boolean notConflict = true ;
            Map<Vetex,Vetex> currentMapVetex = currentListVetex.stream().collect(Collectors.toMap(vetex -> vetex, vetex -> vetex ));
            for(int i = 0;i<conflictOfVetex.size(); i++){
                if(currentMapVetex.get(conflictOfVetex.get(i)) != null){
                        notConflict = false ;
                        break ;
                }
            }
        System.out.println(notConflict);

            return notConflict;
    }

}
