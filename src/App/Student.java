package App;

import java.util.List;

public class Student {
    private String name ;
    private List<Vertex> subjects ;

    public Student(){

    };
    public Student(String name,List<Vertex> subjects){
        this.name = name;
        this.subjects = subjects;
    }

    public String getName() {
        return this.name;
    }

    public List<Vertex> getSubjects(){
        return this.subjects;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", subjects=" + subjects +
                '}';
    }
}
