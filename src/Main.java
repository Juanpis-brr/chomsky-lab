import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

/** Ventana principal de Chomsky Lab y punto de entrada de la aplicación. */
public final class Main extends JFrame {
    private final CardLayout layout = new CardLayout();
    private final JPanel pages = new JPanel(layout);
    private final Map<String, JButton> links = new LinkedHashMap<>();

    public Main() {
        super("Chomsky Lab | Simulador de autómatas");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 680));
        setSize(1180, 780);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(UiTheme.BACKGROUND);
        add(createSidebar(), BorderLayout.WEST);
        add(pages, BorderLayout.CENTER);
        addPages();
        showPage("Inicio");
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Swing usa su tema predeterminado si no está disponible el tema del sistema.
        }
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }

    private JPanel createSidebar() {
        JPanel side = new JPanel();
        side.setBackground(UiTheme.SIDEBAR);
        side.setPreferredSize(new Dimension(256, 0));
        side.setBorder(new EmptyBorder(26, 16, 18, 16));
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));

        JPanel brand = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        brand.setOpaque(false);
        JLabel mark = UiTheme.label("C", 18, UiTheme.BACKGROUND, true);
        mark.setHorizontalAlignment(SwingConstants.CENTER);
        mark.setPreferredSize(new Dimension(38, 38));
        mark.setOpaque(true);
        mark.setBackground(UiTheme.ACCENT);
        brand.add(mark);
        JPanel name = new JPanel();
        name.setOpaque(false);
        name.setLayout(new BoxLayout(name, BoxLayout.Y_AXIS));
        name.add(UiTheme.label("CHOMSKY LAB", 16, UiTheme.TEXT, true));
        name.add(Box.createVerticalStrut(3));
        name.add(UiTheme.label("LABORATORIO DE AUTÓMATAS", 9, UiTheme.MUTED, true));
        brand.add(name);
        brand.setAlignmentX(Component.LEFT_ALIGNMENT);
        side.add(brand);
        side.add(Box.createVerticalStrut(34));
        side.add(UiTheme.label("ESPACIO DE TRABAJO", 10, UiTheme.MUTED, true));
        side.add(Box.createVerticalStrut(10));
        addLink(side, "⌂    Inicio", "Inicio");
        side.add(Box.createVerticalStrut(18));
        side.add(UiTheme.label("JERARQUÍA DE CHOMSKY", 10, UiTheme.MUTED, true));
        side.add(Box.createVerticalStrut(8));
        addLink(side, "03    Lenguajes regulares", "Nivel 3");
        addLink(side, "02    Lenguajes con pila", "Nivel 2");
        addLink(side, "01    Cinta acotada", "Nivel 1");
        addLink(side, "00    Máquina de Turing", "Nivel 0");
        side.add(Box.createVerticalGlue());

        JPanel footer = UiTheme.surface();
        footer.setLayout(new BoxLayout(footer, BoxLayout.Y_AXIS));
        footer.add(UiTheme.label("MODO DIDÁCTICO", 10, UiTheme.ACCENT, true));
        footer.add(Box.createVerticalStrut(7));
        footer.add(UiTheme.label("Explora estados, memoria y\ntransiciones de cada modelo.", 11, UiTheme.MUTED, false));
        footer.setAlignmentX(Component.LEFT_ALIGNMENT);
        side.add(footer);
        return side;
    }

    private void addLink(JPanel side, String title, String page) {
        JButton link = new JButton(title);
        link.setAlignmentX(Component.LEFT_ALIGNMENT);
        link.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        link.setPreferredSize(new Dimension(224, 42));
        link.setHorizontalAlignment(SwingConstants.LEFT);
        link.setFont(UiTheme.font(12, false));
        link.setForeground(UiTheme.MUTED);
        link.setBackground(UiTheme.SIDEBAR);
        link.setBorder(new EmptyBorder(10, 12, 10, 8));
        link.setFocusPainted(false);
        link.setContentAreaFilled(false);
        link.setOpaque(true);
        link.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        link.addActionListener(event -> showPage(page));
        links.put(page, link);
        side.add(link);
        side.add(Box.createVerticalStrut(4));
    }

    private void addPages() {
        addPage("Inicio", new HomePanel(this::showPage));
        addPage("Nivel 3", new Level3Panel());
        addPage("Nivel 2", new Level2Panel());
        addPage("Nivel 1", new Level1Panel());
        addPage("Nivel 0", new Level0Panel());
    }

    private void addPage(String name, JPanel page) {
        pages.add(page, name);
    }

    private void showPage(String name) {
        layout.show(pages, name);
        links.forEach((page, link) -> {
            boolean active = page.equals(name);
            link.setBackground(active ? UiTheme.NAV_SELECTED : UiTheme.SIDEBAR);
            link.setForeground(active ? UiTheme.TEXT : UiTheme.MUTED);
            link.setFont(UiTheme.font(12, active));
        });
    }
}

