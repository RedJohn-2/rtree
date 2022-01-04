package main;
import rtree.Point;
import rtree.RTree;
import rtree.Rectangle;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MainWindow extends JFrame {
    private final TreeDp dp;
    private final GraphicDp gdp = new GraphicDp();
    private TextField bottom = new TextField("Нижняя граница");
    private TextField top = new TextField("Верхняя граница");
    private TextField left = new TextField("Левая граница");
    private TextField right = new TextField("Правая граница");
    private TextField minEntries = new TextField("Минимальное количество узлов");
    private Label answer = new Label();

    public MainWindow() throws HeadlessException {
        dp = new TreeDp();
        JButton createTree = new JButton("Создать дерево");
        JButton addTree = new JButton("Добавить регион");
        JButton searchRegions = new JButton("Найти пересекаемые регионы");
        JButton deleteRegions = new JButton("Удалить регион");
        JButton containRegions = new JButton("Проверить существование региона");
        JButton loadButton = new JButton("Загрузить из файла");
        answer.setSize(15, 100);
        dp.add(createTree);
        dp.add(addTree);
        dp.add(deleteRegions);
        dp.add(containRegions);
        dp.add(searchRegions);
        dp.add(loadButton);
        dp.add(bottom);
        dp.add(top);
        dp.add(left);
        dp.add(right);
        dp.add(minEntries);
        dp.add(answer);

        addTree.addActionListener(e -> {
            if (dp.isTreeCreated()) {
                dp.add(new Rectangle(new Point(Double.parseDouble(left.getText()), Double.parseDouble(bottom.getText())),
                        new Point(Double.parseDouble(right.getText()), Double.parseDouble(top.getText()))));
                gdp.add(new Rectangle(new Point(Double.parseDouble(left.getText()), Double.parseDouble(bottom.getText())),
                        new Point(Double.parseDouble(right.getText()), Double.parseDouble(top.getText()))));
            } else {
                dp.addStartRegion(new Rectangle(new Point(Double.parseDouble(left.getText()), Double.parseDouble(bottom.getText())),
                        new Point(Double.parseDouble(right.getText()), Double.parseDouble(top.getText()))));
            }
        });

        deleteRegions.addActionListener(e -> {
            if (dp.isTreeCreated()) {
                dp.delete(new Rectangle(new Point(Double.parseDouble(left.getText()), Double.parseDouble(bottom.getText())),
                        new Point(Double.parseDouble(right.getText()), Double.parseDouble(top.getText()))));
                gdp.delete(new Rectangle(new Point(Double.parseDouble(left.getText()), Double.parseDouble(bottom.getText())),
                        new Point(Double.parseDouble(right.getText()), Double.parseDouble(top.getText()))));
            }
        });

        searchRegions.addActionListener(e -> {
            if (dp.isTreeCreated()) {
                StringBuilder ans = new StringBuilder();
                for (Rectangle rectangle : dp.getTree().search(new Rectangle(new Point(Double.parseDouble(left.getText()), Double.parseDouble(bottom.getText())),
                        new Point(Double.parseDouble(right.getText()), Double.parseDouble(top.getText()))))) {
                    ans.append("{(").append(rectangle.getMinP().getY()).append("; ").append(rectangle.getMaxP().getY()).append(") ").append("(").append(rectangle.getMinP().getX()).append("; ").append(rectangle.getMaxP().getX()).append(")}");
                }

                answer.setText(ans.toString());
            }
        });

        containRegions.addActionListener(e -> {
            if (dp.isTreeCreated()) {
                answer.setText("" + dp.getTree().contain(new Rectangle(new Point(Double.parseDouble(left.getText()), Double.parseDouble(bottom.getText())),
                        new Point(Double.parseDouble(right.getText()), Double.parseDouble(top.getText())))));
            }
        });

        createTree.addActionListener(e -> {
            if (dp.isTreeCreated()) {
                dp.setTree(new RTree(Integer.parseInt(minEntries.getText()), dp.getStartRegions().get(0), dp.getStartRegions().get(1)));
                gdp.setTree(new RTree(Integer.parseInt(minEntries.getText()), dp.getStartRegions().get(0), dp.getStartRegions().get(1)));
            }
        });

        loadButton.addActionListener(e -> {
            JFileChooser fileOpen = new JFileChooser();
            int ret = fileOpen.showDialog(null, "Открыть файл");
            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = fileOpen.getSelectedFile();
                try {
                    if (!dp.isTreeCreated()) {
                        dp.setTree(new RTree(file));
                        gdp.setTree(new RTree(file));
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        createJFrame();

        this.add(dp);

    }

    private void createJFrame() {
        JFrame mw1 = new JFrame();
        mw1.add(gdp);
        mw1.setSize(800, 800);
        mw1.setVisible(true);
    }

}
