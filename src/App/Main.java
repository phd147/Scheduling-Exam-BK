package App;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
            Graph graph = new Graph();
            graph.initGraphFromTheFiles("aListSubjectOfStudent.txt","allSubject.txt");
            graph.coloring();
            graph.printResult();
            graph.saveResultIntoFile("FinalExam.txt");
    }
}
