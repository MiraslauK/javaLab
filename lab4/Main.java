import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class Main extends JFrame {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private JFileChooser fileChooser = null;
    private JCheckBoxMenuItem showAxisMenuItem;
    private JCheckBoxMenuItem showMarkersMenuItem;
    private GraphicsDisplay display = new GraphicsDisplay();
    private boolean fileLoaded = false;
    public Main() {
        super("Plotting function graphs based on pre-prepared files");
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - WIDTH)/2,
                (kit.getScreenSize().height - HEIGHT)/2);

        setExtendedState(MAXIMIZED_BOTH);
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        // Создать действие по открытию файла
        Action openGraphicsAction = new AbstractAction("Open graph file") {
            public void actionPerformed(ActionEvent event) {
                if (fileChooser==null) {
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));
                }
                if (fileChooser.showOpenDialog(Main.this) == JFileChooser.APPROVE_OPTION)
                    openGraphics(fileChooser.getSelectedFile());
            }
        };
        fileMenu.add(openGraphicsAction);
        JMenu graphicsMenu = new JMenu("Graph");
        menuBar.add(graphicsMenu);
        Action showAxisAction = new AbstractAction("Show axis") {
            public void actionPerformed(ActionEvent event) {
                display.setShowAxis(showAxisMenuItem.isSelected());
            }
        };
        showAxisMenuItem = new JCheckBoxMenuItem(showAxisAction);
        graphicsMenu.add(showAxisMenuItem);
        showAxisMenuItem.setSelected(true);
        Action showMarkersAction = new AbstractAction("Show markers") {
            public void actionPerformed(ActionEvent event) {
                display.setShowMarkers(showMarkersMenuItem.isSelected());
            }
        };
        showMarkersMenuItem = new JCheckBoxMenuItem(showMarkersAction);
        graphicsMenu.add(showMarkersMenuItem);
        // Элемент по умолчанию включен (отмечен флажком)
        showMarkersMenuItem.setSelected(true);
        // Зарегистрировать обработчик событий, связанных с меню "График"

        graphicsMenu.addMenuListener(new GraphicsMenuListener());
        // Установить GraphicsDisplay в цент граничной компоновки
        getContentPane().add(display, BorderLayout.CENTER);
    }
    // Считывание данных графика из существующего файла
    protected void openGraphics(File selectedFile) {
        try {
            // Шаг 1 - Открыть поток чтения данных, связанный с входным файловым потоком
            DataInputStream in = new DataInputStream(new
                    FileInputStream(selectedFile));
		/* Шаг 2 - Зная объѐм данных в потоке ввода можно вычислить,
		* сколько памяти нужно зарезервировать в массиве:
		* Всего байт в потоке - in.available() байт;
		* Размер одного числа Double - Double.SIZE бит, или
		Double.SIZE/8 байт;
		* Так как числа записываются парами, то число пар меньше в
		2 раза
		*/
            Double[][] graphicsData = new
                    Double[in.available()/(Double.SIZE/8)/2][];
            // Шаг 3 - Цикл чтения данных (пока в потоке есть данные)
            int i = 0;
            while (in.available()>0) {
                // Первой из потока читается координата точки X
                Double x = in.readDouble();
                // Затем - значение графика Y в точке X
                Double y = in.readDouble();
                // Прочитанная пара координат добавляется в массив
                graphicsData[i++] = new Double[] {x, y};
            }
            // Шаг 4 - Проверка, имеется ли в списке в результате чтения хотя бы одна пара координат
            if (graphicsData!=null && graphicsData.length>0) {
                // Да - установить флаг загруженности данных
                fileLoaded = true;
                // Вызывать метод отображения графика
                display.showGraphics(graphicsData);
            }
            // Шаг 5 - Закрыть входной поток
            in.close();
        } catch (FileNotFoundException ex) {
            // В случае исключительной ситуации типа "Файл не найден" показать сообщение об ошибке
            JOptionPane.showMessageDialog(Main.this, "File not found", "Error loading data", JOptionPane.WARNING_MESSAGE);
            return;
        } catch (IOException ex) {
            // В случае ошибки ввода из файлового потока показать сообщение об ошибке
            JOptionPane.showMessageDialog(Main.this, "Error reading point coordinates from file", "Error loading data",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
    }
    public static void main(String[] args) {
        // Создать и показать экземпляр главного окна приложения
        Main frame = new Main();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    // Класс-слушатель событий, связанных с отображением меню
    private class GraphicsMenuListener implements MenuListener {
        // Обработчик, вызываемый перед показом меню
        public void menuSelected(MenuEvent e) {
            // Доступность или недоступность элементов меню "График" определяется загруженностью данных
            showAxisMenuItem.setEnabled(fileLoaded);
            showMarkersMenuItem.setEnabled(fileLoaded);
        }
        // Обработчик, вызываемый после того, как меню исчезло с экрана
        public void menuDeselected(MenuEvent e) {
        }
        // Обработчик, вызываемый в случае отмены выбора пункта меню (очень редкая ситуация)
        public void menuCanceled(MenuEvent e) {
        }
    }
}
