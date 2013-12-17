package pl.psnc.synat.a12.gui;

import java.awt.Container;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import org.apache.log4j.Logger;

import pl.psnc.synat.a12.model.LetterBox;
import pl.psnc.synat.a12.generator.custom.LettersProvider;

public class MainForm extends JFrame {

    private static final long serialVersionUID = 6547317070325142501L;
    private final static Logger logger = Logger.getLogger(MainForm.class);
    private Map<Character, Integer> offsets = new HashMap<Character, Integer>();
    private List<GlyphPane> glyphPanes = new LinkedList<GlyphPane>();
    private LettersProvider alph;
    private Container mainPane;

    public void init() {
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        createMenuBar();

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setVisible(true);
        ActionsController.getInstance().setWindow(this);
        mainPane = getContentPane();
    }

    private void showGlyphs() {
        if (alph != null) {
            setContentPane(mainPane);
            mainPane.removeAll();

            for (Entry<Character, List<LetterBox>> glyph : alph.getAlphabet().entrySet()) {
                GlyphPane pane = new GlyphPane(glyph.getKey(), glyph.getValue(), getOffset(glyph.getKey()));

                add(pane);
                add(Box.createVerticalStrut(15));
                glyphPanes.add(pane);
            }
            setContentPane(new JScrollPane(getContentPane()));
            setVisible(true);
        }
    }

    private int getOffset(char c) {
        if (offsets != null && offsets.containsKey(c)) {
            return offsets.get(c);
        }
        return 0;
    }

    private void createMenuBar() {
        ActionsController controller = ActionsController.getInstance();
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = createMenu(menuBar, "File", KeyEvent.VK_F);
        controller.registerFileOpen(createItem(fileMenu, "Open"));
        controller.registerSaveOffsets(createItem(fileMenu, "Save offsets"));
        fileMenu.addSeparator();
        controller.registerExit(createItem(fileMenu, "Exit"));

        JMenu filter = createMenu(menuBar, "Filter", KeyEvent.VK_I);
        controller.registerItalics(createCheckbox(filter, "Skip italics"));
        controller.registerGothic(createCheckbox(filter, "Skip gothic"));
        controller.registerNoise(createCheckbox(filter, "Skip noise"));

        JMenu generator = createMenu(menuBar, "Generate", KeyEvent.VK_G);
        controller.registerFileOpen(createItem(generator, "Load text"));
        controller.registerSaveOffsets(createItem(generator, "Save image"));

        this.setJMenuBar(menuBar);
    }

    private JMenu createMenu(JMenuBar menuBar, String name, int key) {
        JMenu fileMenu = new JMenu(name);
        fileMenu.setMnemonic(key);
        menuBar.add(fileMenu);
        return fileMenu;
    }

    private JMenuItem createItem(JMenu menu, String name) {
        JMenuItem menuItem;
        menuItem = new JMenuItem(name);
        menu.add(menuItem);
        return menuItem;
    }

    private JCheckBoxMenuItem createCheckbox(JMenu menu, String name) {
        JCheckBoxMenuItem menuItem;
        menuItem = new JCheckBoxMenuItem(name);
        menu.add(menuItem);
        return menuItem;
    }

    public void setAlphabet(LettersProvider alphabet) {
        alph = alphabet;
        showGlyphs();
        logger.info("glyphs shown");
    }

    public void setOffsets(Map<Character, Integer> offsets) {
        this.offsets = offsets;
    }

    public Map<Character, Integer> getOffsets() {
        for (GlyphPane pane : glyphPanes) {
            offsets.put(pane.getGlyph(), pane.getOffset());
        }
        return offsets;
    }
}
