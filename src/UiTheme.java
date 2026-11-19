import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

/** Estilos y componentes reutilizables de la interfaz. */
public final class UiTheme {
    public static final Color BACKGROUND = new Color(9, 9, 12);
    public static final Color SIDEBAR = new Color(15, 15, 19);
    public static final Color PANEL = new Color(25, 25, 31);
    public static final Color INPUT = new Color(34, 34, 42);
    public static final Color TEXT = new Color(248, 248, 250);
    public static final Color MUTED = new Color(183, 183, 192);
    public static final Color ACCENT = new Color(229, 36, 48);
    public static final Color SUCCESS = new Color(248, 248, 250);
    public static final Color WARNING = new Color(73, 73, 82);
    public static final Color ERROR = new Color(255, 75, 86);
    public static final Color NAV_SELECTED = new Color(73, 18, 24);
    public static final Color BORDER = new Color(57, 57, 66);

    private UiTheme() { }

    public static JPanel page(String eyebrow, String title, String description, JComponent body) {
        JPanel root = new JPanel(new BorderLayout(0, 24));
        root.setBackground(BACKGROUND);
        root.setBorder(new EmptyBorder(32, 36, 30, 36));
        JPanel header = new JPanel();
        header.setBackground(BACKGROUND);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        JLabel section = label(eyebrow.toUpperCase(), 10, ACCENT, true);
        JLabel heading = label(title, 30, TEXT, true);
        JLabel subtitle = label(description, 14, MUTED, false);
        section.setAlignmentX(Component.LEFT_ALIGNMENT);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.add(section);
        header.add(Box.createVerticalStrut(8));
        header.add(heading);
        header.add(Box.createVerticalStrut(7));
        header.add(subtitle);
        root.add(header, BorderLayout.NORTH);
        root.add(body, BorderLayout.CENTER);
        return root;
    }

    public static JPanel surface() {
        JPanel panel = new JPanel();
        panel.setBackground(PANEL);
        panel.setBorder(new CompoundBorder(new LineBorder(new Color(57, 57, 66), 1, true),
                new EmptyBorder(18, 18, 18, 18)));
        return panel;
    }

    public static JPanel workArea() {
        JPanel area = new JPanel(new BorderLayout(0, 12));
        area.setBackground(BACKGROUND);
        return area;
    }

    public static JPanel controls() {
        JPanel row = surface();
        row.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 2));
        return row;
    }

    public static JLabel label(String text, int size, Color color, boolean bold) {
        JLabel label = new JLabel(text);
        label.setForeground(color);
        label.setFont(font(size, bold));
        return label;
    }

    public static Font font(int size, boolean bold) {
        return new Font("Segoe UI", bold ? Font.BOLD : Font.PLAIN, size);
    }

    public static JTextField input(String initialValue) {
        JTextField input = new JTextField(initialValue, 20);
        input.setFont(new Font("Consolas", Font.PLAIN, 15));
        input.setBackground(INPUT);
        input.setForeground(TEXT);
        input.setCaretColor(ACCENT);
        input.setSelectionColor(new Color(112, 28, 37));
        input.setBorder(new CompoundBorder(new LineBorder(new Color(72, 72, 82), 1, true),
                new EmptyBorder(9, 11, 9, 11)));
        return input;
    }

    public static JButton button(String title, boolean primary, Runnable action) {
        JButton button = new JButton(title);
        button.setForeground(Color.BLACK);
        button.setBackground(primary ? ACCENT : WARNING);
        button.setFont(font(12, true));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorder(new CompoundBorder(new LineBorder(primary ? ACCENT : BORDER, 1, true),
                new EmptyBorder(9, 14, 9, 14)));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addActionListener(event -> action.run());
        return button;
    }

    public static JTextArea trace() {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Consolas", Font.PLAIN, 14));
        area.setBackground(PANEL);
        area.setForeground(TEXT);
        area.setCaretColor(ACCENT);
        area.setBorder(new EmptyBorder(16, 16, 16, 16));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        return area;
    }

    public static JScrollPane scroll(JComponent content) {
        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(new LineBorder(new Color(57, 57, 66), 1, true));
        scroll.getViewport().setBackground(PANEL);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }
}

