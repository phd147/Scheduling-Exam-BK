package App;

import java.util.Objects;

public class Vertex implements Comparable<Vertex> {

    private String name ;

    public Vertex(){};
    public Vertex(String name){this.name = name ;};


    public String getName() {
        return this.name;
    }


    @Override
    public int compareTo(Vertex o) {
        return this.getName().compareTo(o.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
        return Objects.equals(name, vertex.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "name='" + name + '\'' +
                '}';
    }
}
