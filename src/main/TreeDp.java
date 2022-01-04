package main;

import rtree.IRTree;
import rtree.Rectangle;
import rtree.TreeNode;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TreeDp extends JPanel {
    private IRTree tree = null;
    private boolean treeCreated = false;
    private List<Rectangle> startRegions = new ArrayList<>();
    private int height = 120;

    public TreeDp() {
    }

    public void addStartRegion(Rectangle rectangle) {
        startRegions.add(rectangle);
        if (startRegions.size() == 2) {
            treeCreated = true;
        }
    }

    public IRTree getTree() {
        return tree;
    }

    public void add(Rectangle rec) {
        tree.add(rec);
        repaint();
    }

    public void delete(Rectangle rec) {
        tree.delete(rec);
        repaint();
    }

    public void setTree(IRTree tree) {
        this.tree = tree;
        treeCreated = true;
        repaint();
    }

    public boolean isTreeCreated() {
        return treeCreated;
    }

    public void setTreeCreated(boolean treeCreated) {
        this.treeCreated = treeCreated;
    }

    public List<Rectangle> getStartRegions() {
        return startRegions;
    }

    public void setStartRegions(List<Rectangle> startRegions) {
        this.startRegions = startRegions;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (tree != null) {
            height = 120;
            drawTree(g, tree.getRoot(), getWidth() / 4, height, "1.");
        }
    }

    private void drawTree(Graphics g, TreeNode node, int x, int y, String level) {
        g.drawString(level + " (" + node.getRegion().getMinP().getY() + " " + node.getRegion().getMaxP().getY() + ")" +
                        "(" + node.getRegion().getMinP().getX() + " " + node.getRegion().getMaxP().getX() + ")"
                , x, y);
        if (node.isLeaf()) return;
        for (int i = 0; i < node.getSize(); i++) {
            height += 20;
            if (node.getSize() > i) {
                drawTree(g, node.getEntries().get(i), x + 10, height, level + (i + 1) + '.');
            }
        }
    }
}
