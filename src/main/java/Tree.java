import java.util.ArrayList;
import java.util.List;

public class Tree {
    String sym;
    List<Tree> kids = new ArrayList<>();

    public Tree(String sym, Tree t) {
        this.sym = sym;

        if (t == null) {
        } else {
            this.kids.add(t);
        }
    }

    public Tree(String sym, List<Tree> t) {
        this.sym = sym;
        this.kids.addAll(t);

    }

    public void addChild(Tree t) {
        this.kids.add(t);
    }

    public void inserChild(Tree t) {
        this.kids.add(0, t);
    }

    @Override
    public String toString() {
        return "Tree{" +
                "sym='" + sym + '\'' +
                ", kids=" + kids +
                '}';
    }
}
