package App;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Vetex implements Comparable<Vetex> {

    private String name ;

    public Vetex(){};
    public Vetex(String name){this.name = name ;};


    public String getName() {
        return this.name;
    }


    @Override
    public int compareTo(Vetex o) {
        return this.getName().compareTo(o.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vetex vetex = (Vetex) o;
        return Objects.equals(name, vetex.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Vetex{" +
                "name='" + name + '\'' +
                '}';
    }
}
