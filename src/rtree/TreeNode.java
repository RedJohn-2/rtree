package rtree;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {
    private Rectangle region;
    private boolean isLeaf;
    private TreeNode parent = null;
    private List<TreeNode> entries = null;

    public TreeNode(Rectangle region) {
        this.region = region;
        isLeaf = false;
        entries = new ArrayList<>();
    }

    public TreeNode(Rectangle region, TreeNode parent, boolean isLeaf) {
        this.region = region;
        this.isLeaf = isLeaf;
        if (!isLeaf) entries = new ArrayList<>();
        this.parent = parent;
    }

    public Rectangle getRegion() {
        return region;
    }

    public void setRegion(Rectangle region) {
        this.region = region;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean leaf) {
        isLeaf = leaf;
    }

    public List<TreeNode> getEntries() {
        return entries;
    }

    public void setEntries(List<TreeNode> entries) {
        this.entries = entries;
    }

    public TreeNode getParent() {
        return parent;
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    public int getSize() {
        if (isLeaf) return 0;
        return entries.size();
    }
}
