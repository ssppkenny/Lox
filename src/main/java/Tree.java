import java.util.ArrayList;
import java.util.List;

public class Tree {
    int id;
    String sym;
    int rule;
    int nkids;
    Token tok;
    List<Tree> kids = new ArrayList<>();
    ;


    public Tree(String sym, int id, Tree t) {
        this.sym = sym;
        if (t == null) {
        } else {
            this.kids.add(t);
        }
    }

    public Tree(String sym, int id, List<Tree> t) {
        this.sym = sym;
        this.kids.addAll(t);

    }

    public void addChild(Tree t) {
        this.kids.add(t);
    }

}
