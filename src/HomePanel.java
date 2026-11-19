import java.awt.*;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.border.*;

/** Panel de bienvenida con acceso a los cuatro simuladores. */
public final class HomePanel extends JPanel {
    private static final String[][] MODULES = {
        {"NIVEL 3", "Autómata finito", "Reconoce a*b* y recorre sus estados.", "Nivel 3"},
        {"NIVEL 2", "Autómata con pila", "Comprueba aⁿbⁿ con memoria LIFO.", "Nivel 2"},
        {"NIVEL 1", "Cinta acotada", "Valida aⁿbⁿcⁿ con espacio lineal.", "Nivel 1"},
        {"NIVEL 0", "Máquina de Turing", "Ejecuta suma unaria o reconoce aⁿbⁿ.", "Nivel 0"}
    };

    public HomePanel(Consumer<String> openModule) {
        setLayout(new BorderLayout(0, 24));
        setBackground(UiTheme.BACKGROUND);
        setBorder(new EmptyBorder(32, 36, 30, 36));
        JPanel header = new JPanel();
        header.setBackground(UiTheme.BACKGROUND);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.add(UiTheme.label("LABORATORIO INTERACTIVO", 10, UiTheme.ACCENT, true));
        header.add(Box.createVerticalStrut(8));
        header.add(UiTheme.label("Jerarquía de Chomsky", 30, UiTheme.TEXT, true));
        header.add(Box.createVerticalStrut(7));
        header.add(UiTheme.label("Explora cómo cambia la memoria al reconocer distintos lenguajes.",
                14, UiTheme.MUTED, false));
        add(header, BorderLayout.NORTH);
        JPanel grid = new JPanel(new GridLayout(2, 2, 16, 16));
        grid.setBackground(UiTheme.BACKGROUND);
        for (String[] module : MODULES) grid.add(createCard(module, openModule));
        add(grid, BorderLayout.CENTER);
    }

    private JPanel createCard(String[] data, Consumer<String> openModule) {
        JPanel card = UiTheme.surface();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new CompoundBorder(new LineBorder(UiTheme.BORDER, 1, true),
                new EmptyBorder(20, 20, 20, 20)));
        JLabel level = UiTheme.label(data[0], 10, UiTheme.ACCENT, true);
        JLabel title = UiTheme.label(data[1], 20, UiTheme.TEXT, true);
        JLabel description = UiTheme.label("<html>" + data[2] + "</html>", 13, UiTheme.MUTED, false);
        level.setAlignmentX(LEFT_ALIGNMENT);
        title.setAlignmentX(LEFT_ALIGNMENT);
        description.setAlignmentX(LEFT_ALIGNMENT);
        card.add(level);
        card.add(Box.createVerticalStrut(12));
        card.add(title);
        card.add(Box.createVerticalStrut(8));
        card.add(description);
        card.add(Box.createVerticalGlue());
        JButton open = UiTheme.button("Abrir simulador  →", true, () -> openModule.accept(data[3]));
        open.setForeground(Color.BLACK);
        open.setAlignmentX(LEFT_ALIGNMENT);
        card.add(open);
        return card;
    }
}
