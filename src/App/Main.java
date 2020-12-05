package App;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        //  Khởi tạo một đồ thị
            Graph graph = new Graph();
            // khởi tạo các thuộc tính trong đồ thị như ( Tổng số các đỉnh(MÔN), danh sách các đỉnh(MÔN) của đồ thị , một bộ từ điển có key là đỉnh(MÔN), value là danh sách các đỉnh(MÔN) không được kề đỉnh(MÔN) đó )
            graph.initGraphFromTheFiles("aListSubjectOfStudent.txt","allSubject.txt");

            // Thực hiện thuật toán tô màu và lưu vào bộ từ điển gồm key là số đợt thi , value là danh sách các môn thi của dợt đó )
            graph.coloring();
            graph.printResult();
            graph.saveResultIntoFile("FinalExam.txt");
    }
}
