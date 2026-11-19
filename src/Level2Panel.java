import java.awt.BorderLayout;
import java.util.*;
import javax.swing.*;

/** Autómata de pila para el lenguaje no regular a^n b^n. */
public final class Level2Panel extends JPanel {
    private final JTextField input = UiTheme.input("aaabbb");
    private final JTextArea trace = UiTheme.trace();
    private final JLabel stackLabel = UiTheme.label("Pila: [Z₀]", 14, UiTheme.ACCENT, true);
    private final JLabel resultLabel = UiTheme.label("Ingresa una cadena de a y b.", 13, UiTheme.MUTED, false);
    private final Deque<Character> stack = new ArrayDeque<>();
    private String word = "";
    private int position;
    private boolean readingB;
    private boolean valid;

    public Level2Panel() {
        setLayout(new BorderLayout());
        JPanel body = UiTheme.workArea();
        JPanel controls = UiTheme.controls();
        controls.add(UiTheme.label("CADENA", 10, UiTheme.MUTED, true));
        controls.add(input);
        controls.add(UiTheme.button("Paso siguiente", false, this::step));
        controls.add(UiTheme.button("Simular completa", true, this::simulate));
        controls.add(UiTheme.button("Ejemplo: aaaabbbb", false, this::loadExample));
        controls.add(UiTheme.button("Reiniciar", false, this::reset));
        body.add(controls, BorderLayout.NORTH);
        body.add(UiTheme.scroll(trace), BorderLayout.CENTER);
        JPanel status = UiTheme.surface();
        status.setLayout(new BoxLayout(status, BoxLayout.Y_AXIS));
        status.add(stackLabel);
        status.add(Box.createVerticalStrut(7));
        status.add(resultLabel);
        body.add(status, BorderLayout.SOUTH);
        initialize();
        add(UiTheme.page("Nivel 2 · Tipo 2", "Autómata de pila",
                "Lenguaje: aⁿbⁿ, n ≥ 1     Memoria: pila LIFO", body));
    }

    private void loadExample() {
        input.setText("aaaabbbb");
        initialize();
    }

    private void reset() {
        input.setText("aaabbb");
        initialize();
    }

    private void initialize() {
        word = input.getText().trim();
        position = 0;
        readingB = false;
        valid = true;
        stack.clear();
        stack.push('Z');
        stackLabel.setText("Pila (tope a la derecha): [Z₀]");
        resultLabel.setText("Estado inicial: q₀");
        resultLabel.setForeground(UiTheme.MUTED);
        trace.setText("δ(q₀, a, X) = (q₀, aX)     ·     δ(q₁, b, a) = (q₁, ε)\n\nInicio: [Z₀]");
    }

    private void step() {
        if (position == 0) {
            word = input.getText().trim();
            if (word.isEmpty()) {
                showResult("Ingresa una cadena para comenzar.", UiTheme.WARNING);
                return;
            }
        }
        if (position >= word.length() || !valid) {
            finish();
            return;
        }
        char symbol = word.charAt(position++);
        if (symbol == 'a' && !readingB) {
            stack.push('A');
        } else if (symbol == 'b') {
            readingB = true;
            if (stack.size() > 1 && stack.peek() == 'A') stack.pop();
            else valid = false;
        } else {
            valid = false;
        }
        String view = stackText();
        stackLabel.setText("Pila (tope a la derecha): " + view);
        trace.append("\nPaso " + position + "   lee '" + symbol + "'   pila: " + view);
        if (!valid) showResult("Transición no definida para esta entrada.", UiTheme.ERROR);
        else if (position == word.length()) finish();
    }

    private void simulate() {
        initialize();
        while (position < word.length() && valid) step();
        finish();
    }

    private String stackText() {
        StringBuilder view = new StringBuilder("[");
        Iterator<Character> symbols = stack.descendingIterator();
        while (symbols.hasNext()) {
            view.append(symbols.next());
            if (symbols.hasNext()) view.append(' ');
        }
        return view.append(']').toString();
    }

    private void finish() {
        boolean accepted = valid && readingB && stack.size() == 1 && !word.isEmpty();
        showResult(accepted ? "Aceptada: la pila volvió a Z₀." : "Rechazada: se requiere aⁿbⁿ con n ≥ 1.",
                accepted ? UiTheme.SUCCESS : UiTheme.ERROR);
    }

    private void showResult(String text, java.awt.Color color) {
        resultLabel.setText(text);
        resultLabel.setForeground(color);
    }
}

