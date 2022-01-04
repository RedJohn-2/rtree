package rtree;

import java.io.IOException;
import java.util.Set;

public interface IRTree {

    void add(Rectangle region);

    Set<Rectangle> search(Rectangle region);

    TreeNode getRoot();

    boolean contain(Rectangle region);

    void delete(Rectangle region);

    Set<Rectangle> getLeaves();

    //Rectangle[] readFromFile(String nameFile) throws IOException;
}
