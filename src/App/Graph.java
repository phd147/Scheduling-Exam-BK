package App;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
public class Graph {

    private Map<Integer, List<Vertex>> result = new HashMap<>();
    private Map<Vertex,Boolean> isColored = new HashMap<>() ;
    private Map<Vertex,List<Vertex>> adjustList = new HashMap<>() ;
    private int totalOfSubject  ;
    private Map<String, Vertex> mapAllVetex = new HashMap<>() ;
    static  int studentCount = 0 ;
    static int subjectCount = 0 ;

    public Graph() throws IOException {


    }
    // Khởi tạo đồ thi Graph bằng cách đọc file và xử lí logic
    public void initGraphFromTheFiles(String studentPath,String allSubjectsPath) throws IOException {
        // Đọc file và  Set giá trị cho thuộc tính các môn theo cấu trúc dữ liệu hashTable ( cụ thể ở đây là HashMap trong java )
        Path subjectsPath = Paths.get(allSubjectsPath);
        List<String> subjectLines = Files.readAllLines(subjectsPath);

        subjectLines.stream().forEach(subjectLine -> {
            if(subjectCount >=1){
                mapAllVetex.put(subjectLine,new Vertex(subjectLine));
            };
            subjectCount ++ ;

        });
        System.out.println("-----DANH SÁCH CÁC MÔN THI-----");
        mapAllVetex.entrySet().stream().forEach(entryVetex -> System.out.println(entryVetex.getValue().getName()) );


        // Set giá trị cho thuộc tính tổng các đỉnh (MÔN)
        totalOfSubject = mapAllVetex.size();


        // Đọc file và  Set giá trị cho thuộc tính thông tin sinh viên theo cấu trúc dữ liệu đối tượng Student
        Path stdPath = Paths.get(studentPath);
        List<String> studentsLines = Files.readAllLines(stdPath);
        List<Student> students = new ArrayList<>();

        studentsLines.stream().forEach(line -> {
            if(studentCount >= 1){

                String name = line.split(":")[0];
                List<String> subjects = Arrays.asList(line.split(":")[1].split(","));
                List<Vertex> subjectOfStudent = new ArrayList<>();
                subjects.stream().forEach(str -> subjectOfStudent.add(mapAllVetex.get(str)));
                students.add(new Student(name,subjectOfStudent));
            }
            studentCount ++ ;

        });
        System.out.println("------------------------");
        System.out.println("DANH SÁCH SINH VIÊN VÀ MÔN THI CỦA SINH VIÊN ĐÓ");
        students.stream().forEach(student -> {
            System.out.print(student.getName() + " : ");
            student.getSubjects().stream().forEach(subject -> System.out.print(subject.getName()+" "));
            System.out.println();
        });

            this.isColored = this.mapAllVetex.entrySet().stream().map(entry -> entry.getValue()).collect(Collectors.toMap(vertex -> vertex, vertex -> false));

            // Khởi tạo thuộc tính bảng băm hash table ( cụ thể ở đây là hashMap<>()) cho đồ thị , có key là đỉnh và value là danh sách các đỉnh không được kề đỉnh đó
            mapAllVetex.values().stream().forEach(vertex -> {
//                System.out.println(vertex);
                Set<Vertex> conflictVertices = new HashSet<>();
                students.stream().filter(student -> student.getSubjects().contains(vertex)).forEach(student -> conflictVertices.addAll(student.getSubjects()));
                List<Vertex> incommingCoflict = new ArrayList<>(conflictVertices).stream().filter(vertexCurr -> !vertexCurr.equals(vertex)).collect(Collectors.toList());
                adjustList.put(vertex,incommingCoflict);
            });
        System.out.println("--------------------------");
        System.out.println("DANH SÁCH CÁC ĐỈNH ( MÔN ) VÀ NHỮNG ĐỈNH KHÔNG ĐƯỢC KỀ ĐỈNH ( MÔN ) ĐÓ CỦA ĐỒ THỊ");
            adjustList.entrySet().stream().forEach(entry -> {
                System.out.print(entry.getKey().getName()+" --> ");
                entry.getValue().stream().forEach(vertex -> System.out.print(vertex.getName()+"  "));
                System.out.println();
            });

    }

    public void coloring(){

        // print adjust List

        // in ra hashTable có key là đỉnh và value là danh sách các đỉnh không được liền kề của đỉnh đó

        this.adjustList.entrySet().stream().forEach(entryVetex -> {
//            System.out.print(entryVetex.getKey()+"--");
            List<Vertex> conflictVertices = entryVetex.getValue();
//            conflictVertices.stream().forEach(vertex -> System.out.print(vertex +" "));
//            System.out.println();

        });

//        adjustList.entrySet()

        int count = 1 ;

        List<Vertex> currentVertices = new ArrayList<>();

        List<Vertex> NotColoring = null ;
        Set<Vertex> NotColoringSet = null ;
        Vertex firstNotColoringVertex = null ;

        Set<Vertex> notColoringSecond = null;

        while(isColored.entrySet().stream().filter(entryVetex -> entryVetex.getValue() == true).count() != totalOfSubject ){


            // tìm danh sách những đỉnh chưa tô màu
             NotColoring = isColored.entrySet().stream().filter(entryVetex -> entryVetex.getValue() != true).map(entryVetex -> entryVetex.getKey()).collect(Collectors.toList());
             // Sắp xếp những đỉnh chưa tô màu đó theo thứ tự tăng dần theo bảng chữ cái alpha belta
             NotColoring  = NotColoring.stream().sorted(Vertex::compareTo).collect(Collectors.toList());

            // lấy phần tử đầu tiên của danh sách trên ra
             firstNotColoringVertex = NotColoring.get(0);

             //add vào danh sách các đỉnh thi kì thi thứ count
            currentVertices.add(firstNotColoringVertex);

            // add đỉnh đó vào danh sách đã tô màu
            isColored.put(firstNotColoringVertex,true);

            // add  key là count ( kì thi ) và value là danh sách kì thi thứ count tạm thời vào bảng băm hashTable kết quả
            result.put(count, currentVertices);

            // Ta tìm những đỉnh chưa tô màu tiếp theo

            NotColoring = isColored.entrySet().stream().filter(entryVetex -> entryVetex.getValue() != true).map(entryVetex -> entryVetex.getKey()).collect(Collectors.toList());
            NotColoringSet = new TreeSet<>(NotColoring);

            Iterator<Vertex> itr = NotColoringSet.iterator();
            while(itr.hasNext()){
                Vertex incomingVertex = itr.next();
               if(isConflict(adjustList.get(incomingVertex), currentVertices)){
                   currentVertices.add(incomingVertex);
                   isColored.put(incomingVertex,true);

                }
            }
            result.put(count,new ArrayList<>(currentVertices));

            // reset lại danh sách thi kì ni là rỗng
            currentVertices = new ArrayList<>() ;

            // biến count tăng lên 1 đơn vị, có nghĩa là sang đợt thi mới
            count ++ ;

        }
    }
    // In kết quả ra ngoài màn hình console của IDE
    public void printResult(){
        System.out.println("--------------------------");
        System.out.println("KẾT QUẢ SẮP XẾP LỊCH THI");
        System.out.println("Đợt thi --- Các môn");
        this.result.entrySet().stream().forEach(entry -> {
            System.out.print("      "+entry.getKey() + " --- ");
            entry.getValue().stream().forEach(entryVertex -> System.out.print(entryVertex.getName() +"  "));
            System.out.println();
        });
    }

    // Lưu kết quả vào file
    public void saveResultIntoFile(String fileSave) throws IOException {
        List<String> resultSave = new ArrayList<>();

        this.result.entrySet().stream().forEach(entry -> {
            StringBuilder sb = new StringBuilder();
            sb.append(entry.getKey() + " -----");
            entry.getValue().stream().forEach(entryVertex -> sb.append(entryVertex +"  "));
            resultSave.add(sb.toString());
        });
        Files.write(Paths.get(fileSave),resultSave, StandardOpenOption.APPEND);
    }

    // Kiểm tra xem đỉnh hiện tại đang xét có kề các đỉnh trong kì thi đang xét hay không
    private boolean isConflict(List<Vertex> conflictOfVertices, List<Vertex> currentListVertices){
            boolean notConflict = true ;
            Map<Vertex, Vertex> currentMapVetex = currentListVertices.stream().collect(Collectors.toMap(vertex -> vertex, vertex -> vertex));
            for(int i = 0; i< conflictOfVertices.size(); i++){
                if(currentMapVetex.get(conflictOfVertices.get(i)) != null){
                        notConflict = false ;
                        break ;
                }
            }
//        System.out.println(notConflict);

            return notConflict;
    }
}
