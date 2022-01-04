package main;

import rtree.IRTree;
import rtree.Rectangle;
import rtree.TreeNode;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GraphicDp extends JPanel {
    private IRTree tree = null;
    private List<Color> colors;

    public GraphicDp() {
        colors = new ArrayList<>();
    }

    public IRTree getTree() {
        return tree;
    }

    public void setTree(IRTree tree) {
        this.tree = tree;
        repaint();
    }

    public void add(Rectangle rectangle) {
        tree.add(rectangle);
        repaint();
    }

    public void delete(Rectangle rec) {
        tree.delete(rec);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        addColors();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        if (tree != null) {
            drawRoot(g, tree.getRoot(), 0, getHeight());
            drawBorder(g, tree.getRoot(), getHeight());
        }
    }

    private void addColors() {
        colors.add(Color.BLUE);
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.ORANGE);
        colors.add(Color.CYAN);
        colors.add(Color.MAGENTA);
    }

    private void drawRoot(Graphics g, TreeNode node, int index, int h) {
        g.setColor(colors.get(index));
        g.fillRect((int)node.getRegion().getMinP().getX(), (int)(h - node.getRegion().getMaxP().getY()),
                (int)(node.getRegion().getMaxP().getX() - node.getRegion().getMinP().getX()),
                (int)(node.getRegion().getMaxP().getY() - node.getRegion().getMinP().getY()));
        drawTree(g, node, index + 1, h);
    }

    private void drawTree(Graphics g, TreeNode node, int index, int h) {
        g.setColor(colors.get(index));
        if (!node.isLeaf()) {
            for (TreeNode entry : node.getEntries()) {
                g.fillRect((int)entry.getRegion().getMinP().getX(), (int)(h - entry.getRegion().getMaxP().getY()),
                        (int)(entry.getRegion().getMaxP().getX() - entry.getRegion().getMinP().getX()),
                        (int)(entry.getRegion().getMaxP().getY() - entry.getRegion().getMinP().getY()));
            }

            for (TreeNode entry : node.getEntries()) {
                drawTree(g, entry, index + 1, h);
            }
        }
    }

    private void drawBorder(Graphics g, TreeNode node, int h) {
        g.setColor(Color.BLACK);
        g.drawRect((int)node.getRegion().getMinP().getX(), (int)(h - node.getRegion().getMaxP().getY()),
                (int)(node.getRegion().getMaxP().getX() - node.getRegion().getMinP().getX()),
                (int)(node.getRegion().getMaxP().getY() - node.getRegion().getMinP().getY()));
        if (!node.isLeaf()) {
            for (TreeNode entry : node.getEntries()) {
                drawBorder(g, entry, h);
            }
        }
    }
}
