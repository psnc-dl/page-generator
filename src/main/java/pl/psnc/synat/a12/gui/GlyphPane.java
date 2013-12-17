package pl.psnc.synat.a12.gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import pl.psnc.synat.a12.model.LetterBox;

public class GlyphPane extends JPanel implements ChangeListener {

    private static final long serialVersionUID = -2407761653657677477L;
    private final SpinnerNumberModel model;
    private final Character glyph;
    private List<LetterBox> letters;
    private List<JLabel> glyphLabels = new ArrayList<JLabel>();


    public GlyphPane(Character glyph, List<LetterBox> letters, int offset) {
        super();
        this.letters = letters;
        this.glyph = glyph;

        model = new SpinnerNumberModel(offset, -100, 100, 1);
        model.addChangeListener(this);

        setLayout(new FlowLayout(FlowLayout.LEFT));
        add(new JLabel(getText(glyph, letters.size()).toString()));
        add(new JSpinner(model));
        showGlyphs(offset);
    }


    private StringBuilder getText(Character glyph, int size) {
        StringBuilder text = new StringBuilder().append(size).append(": ").append(glyph).append("(\\u")
                .append(Integer.toHexString(glyph.charValue())).append(")");
        return text;
    }


    public void stateChanged(ChangeEvent e) {
        int offset = model.getNumber().intValue();

        for (int i = 0; i < letters.size(); i++) {
            glyphLabels.get(i).setIcon(getGlyphIcon(offset, letters.get(i)));
        }
    }


    public int getOffset() {
        return model.getNumber().intValue();
    }


    public Character getGlyph() {
        return glyph;
    }


    private void showGlyphs(int offset) {
        for (LetterBox letterBox : letters) {
            JLabel label = new JLabel();
            label.setIcon(getGlyphIcon(offset, letterBox));

            add(Box.createHorizontalStrut(10));
            add(label);
            glyphLabels.add(label);
        }
    }


    private ImageIcon getGlyphIcon(int offset, LetterBox letterBox) {
        BufferedImage originImage = letterBox.getImage();
        int y = Math.max(originImage.getHeight() - offset, originImage.getHeight());

        BufferedImage image = new BufferedImage(originImage.getWidth(), y + 1, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, image.getWidth(), y + 1);
        graphics.drawImage(originImage, 0, 0, null);
        graphics.setColor(Color.BLUE);
        graphics.drawLine(0, originImage.getHeight() - offset, image.getWidth(), originImage.getHeight() - offset);
        return new ImageIcon(image);
    }
}
