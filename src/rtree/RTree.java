package rtree;

import rtree.utils.RTreeUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class RTree implements IRTree {
    private final int minEntries;
    private final int maxEntries;
    private TreeNode root;

    public RTree(int minEntries, Rectangle rec1, Rectangle rec2) {
        this.minEntries = minEntries;
        maxEntries = minEntries * 2;
        root = new TreeNode(RTreeUtil.findMinRoundRegion(new Rectangle[]{rec1, rec2}));
        addRegion(root, new TreeNode(rec1, root, true));
        addRegion(root, new TreeNode(rec2, root, true));
    }

    public RTree(File file) throws IOException {
        minEntries = 2;
        maxEntries = minEntries * 2;
        Rectangle[] rectangles = readFromFile(file.getAbsolutePath());
        if (rectangles.length > 1) {
            root = new TreeNode(RTreeUtil.findMinRoundRegion(new Rectangle[]{rectangles[0], rectangles[1]}));
            addRegion(root, new TreeNode(rectangles[0], root, true));
            addRegion(root, new TreeNode(rectangles[1], root, true));
            for (int i = 2; i < rectangles.length; i++) {
                add(rectangles[i]);
            }
        }
    }

    private void addRegion(TreeNode parent, TreeNode node) {
        parent.getEntries().add(node);
        parent.setRegion(RTreeUtil.findMinRoundRegion(new Rectangle[]{parent.getRegion(), node.getRegion()}));
        node.setParent(parent);
        if (parent.getSize() > maxEntries) {
            if (parent == root) {
                root = new TreeNode(parent.getRegion());
                root.getEntries().add(parent);
                parent.setParent(root);
            }
            partition(parent.getParent(), parent);
        }
    }

    private void deleteRegion(TreeNode parent, TreeNode node) {
        parent.getEntries().remove(node);
        rebuildTree(parent);
        if (parent == root && parent.getSize() == 1) {
            root = parent.getEntries().get(0);
        } else if (parent.getSize() < minEntries) {
            merge(parent);
        }
    }
    private void rebuildTree(TreeNode node) {
        Rectangle[] entriesRect = new Rectangle[node.getSize()];
        for (int i = 0; i < node.getSize(); i++) {
            entriesRect[i] = node.getEntries().get(i).getRegion();
        }
        node.setRegion(RTreeUtil.findMinRoundRegion(entriesRect));
        if (node == root) return;
        rebuildTree(node.getParent());
    }

    private void merge(TreeNode node) {
        double minArea = Double.POSITIVE_INFINITY;
        TreeNode closeNode = node.getParent().getEntries().get(0);
        for (TreeNode entry : node.getParent().getEntries()) {
            if (entry.equals(node)) continue;
            if (RTreeUtil.findMinRoundRegion(new Rectangle[]{node.getRegion(), entry.getRegion()}).getArea() - entry.getRegion().getArea() < minArea) {
                minArea = RTreeUtil.findMinRoundRegion(new Rectangle[]{node.getRegion(), entry.getRegion()}).getArea() - entry.getRegion().getArea();
                closeNode = entry;
                closeNode.setRegion(RTreeUtil.findMinRoundRegion(new Rectangle[]{node.getRegion(), entry.getRegion()}));
            }
        }

        closeNode.getEntries().addAll(node.getEntries());
        if (node.getParent().getSize() > minEntries) {
            deleteRegion(node.getParent(), node);
        }
        if (closeNode.getSize() > maxEntries) {
            partition(node.getParent(), closeNode);
        }

        deleteRegion(node.getParent(), node);

    }

    @Override
    public Set<Rectangle> search(Rectangle region) {
        return new HashSet<>(searchRegion(root, region));
    }

    private Set<Rectangle> searchRegion(TreeNode node, Rectangle region) {
        if (RTreeUtil.isCrossing(region, node.getRegion())) {
            Set<Rectangle> leaves = new HashSet<>();
            if (node.isLeaf()) {
                leaves.add(node.getRegion());
            } else {
                for (TreeNode entry : node.getEntries()) {
                    leaves.addAll(searchRegion(entry, region));
                }
            }

            return leaves;
        }

        return new HashSet<>();

    }

    @Override
    public void add(Rectangle region) {
        addNode(root, region);
    }

    private void addNode(TreeNode node, Rectangle rectangle) {
        if (node.getEntries().get(0).isLeaf() || node.getEntries().get(0) == null) {
            addRegion(node, new TreeNode(rectangle, node, true));
            return;
        }

        double minArea = Double.POSITIVE_INFINITY;
        TreeNode nodeWithMinArea = node.getEntries().get(0);
        for (TreeNode entry: node.getEntries()) {
            if (RTreeUtil.findMinRoundRegion(new Rectangle[]{rectangle, entry.getRegion()}).getArea() - entry.getRegion().getArea() < minArea) {
                nodeWithMinArea = entry;
                minArea = RTreeUtil.findMinRoundRegion(new Rectangle[]{rectangle, entry.getRegion()}).getArea() - entry.getRegion().getArea();
            }
        }
        addNode(nodeWithMinArea, rectangle);
        node.setRegion(RTreeUtil.findMinRoundRegion(new Rectangle[]{node.getRegion(), nodeWithMinArea.getRegion()}));
    }

    private void partition(TreeNode parent, TreeNode node) {
        double maxSize = 0;
        TreeNode firstNode = node.getEntries().get(0);
        TreeNode secondNode = node.getEntries().get(1);
        for (int i = 0; i < node.getEntries().size() - 1; i++) {
            for (int j = 1; j < node.getEntries().size(); j++) {
                if (RTreeUtil.findMinRoundRegion(new Rectangle[]{node.getEntries().get(i).getRegion(),
                        node.getEntries().get(j).getRegion()}).getArea() > maxSize) {
                    firstNode = node.getEntries().get(i);
                    secondNode = node.getEntries().get(j);
                    maxSize = RTreeUtil.findMinRoundRegion(new Rectangle[]{node.getEntries().get(i).getRegion(),
                            node.getEntries().get(j).getRegion()}).getArea();
                }
            }
        }
        TreeNode firstNewNode = new TreeNode(firstNode.getRegion(), parent, false);
        TreeNode secondNewNode = new TreeNode(secondNode.getRegion(), parent, false);

        for(TreeNode entry : node.getEntries()) {
            if (firstNewNode.getSize() >= minEntries + 1) {
                addRegion(secondNewNode, entry);
                continue;
            } if (secondNewNode.getSize() >= minEntries + 1) {
                addRegion(firstNewNode, entry);
                continue;
            } if (RTreeUtil.findMinRoundRegion(new Rectangle[]{entry.getRegion(), firstNewNode.getRegion()}).getArea() - firstNewNode.getRegion().getArea()
                   > RTreeUtil.findMinRoundRegion(new Rectangle[]{entry.getRegion(), secondNewNode.getRegion()}).getArea() - secondNewNode.getRegion().getArea()) {
               addRegion(secondNewNode, entry);
           } else {
               addRegion(firstNewNode, entry);
           }
        }
        parent.getEntries().remove(node);
        addRegion(parent, firstNewNode);
        addRegion(parent, secondNewNode);
    }

    @Override
    public TreeNode getRoot() {
        return root;
    }

    @Override
    public boolean contain(Rectangle region) {
        return contain(root, region);
    }

    private boolean contain(TreeNode node, Rectangle region) {
        if (node.isLeaf()) {
            return region.equals(node.getRegion());
        }

        if (RTreeUtil.findMinRoundRegion(new Rectangle[]{node.getRegion(), region}).getArea() > node.getRegion().getArea()) {
                return false;
        }

        for (TreeNode entry : node.getEntries()) {
            if (contain(entry, region)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void delete(Rectangle region) {
        delete(root, region);
    }

    private boolean delete(TreeNode node, Rectangle region) {
        if (node.isLeaf()) {
            if (region.equals(node.getRegion())) {
                deleteRegion(node.getParent(), node);
                return true;
            }
            return false;
        }

        if (RTreeUtil.findMinRoundRegion(new Rectangle[]{node.getRegion(), region}).getArea() > node.getRegion().getArea()) {
            return false;
        }

        for (TreeNode entry : node.getEntries()) {
            if (delete(entry, region)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Set<Rectangle> getLeaves() {
        return new HashSet<>(getLeaves(root));
    }

    private Set<Rectangle> getLeaves(TreeNode node) {
        Set<Rectangle> leaves = new HashSet<>();
        if (node.isLeaf()) {
            leaves.add(node.getRegion());
        } else {
            for (TreeNode entry : node.getEntries()) {
                leaves.addAll(getLeaves(entry));
            }
        }

        return leaves;
    }

    private Rectangle[] readFromFile(String nameFile) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(nameFile));
        String line;
        Scanner scanner;
        int index = 0;
        List<Rectangle> rectangles = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            scanner = new Scanner(line);
            scanner.useDelimiter("\\s+");
            Rectangle rect = new Rectangle(new Point(), new Point());
            while (scanner.hasNext()) {
                String x = scanner.next();
                if (index == 0) {
                    rect.getMinP().setY(Double.parseDouble(x));
                } else if (index == 1) {
                    rect.getMaxP().setY(Double.parseDouble(x));
                } else if (index == 2) {
                    rect.getMinP().setX(Double.parseDouble(x));
                } else {
                    rect.getMaxP().setX(Double.parseDouble(x));
                }
                index++;
            }
            rectangles.add(rect);
            index = 0;
        }
        reader.close();
        return rectangles.toArray(new Rectangle[0]);
    }
}
