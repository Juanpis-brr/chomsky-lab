import java.awt.BorderLayout;
import javax.swing.*;

/** Autómata finito determinista del lenguaje regular a*b*. */
public final class Level3Panel extends JPanel {
    private final JTextField input = UiTheme.input("aaabbb");
    private final JTextArea trace = UiTheme.trace();
    private final JLabel stateLabel = UiTheme.label("Estado actual: q₀", 14, UiTheme.ACCENT, true);
    private final JLabel resultLabel = UiTheme.label("Escribe una cadena para comenzar.", 13, UiTheme.MUTED, false);
    private String word = "";
    private int position;
    private int state;

    public Level3Panel() {
        setLayout(new BorderLayout());
        JPanel body = UiTheme.workArea();
        JPanel controls = UiTheme.controls();
        controls.add(UiTheme.label("CADENA", 10, UiTheme.MUTED, true));
        controls.add(input);
        controls.add(UiTheme.button("Paso siguiente", false, this::step));
        controls.add(UiTheme.button("Simular completa", true, this::simulate));
        controls.add(UiTheme.button("Ejemplo: aabb", false, this::loadExample));
        controls.add(UiTheme.button("Reiniciar", false, this::reset));
        body.add(controls, BorderLayout.NORTH);
        body.add(UiTheme.scroll(trace), BorderLayout.CENTER);
        JPanel status = UiTheme.surface();
        status.setLayout(new BoxLayout(status, BoxLayout.Y_AXIS));
        status.add(stateLabel);
        status.add(Box.createVerticalStrut(7));
        status.add(resultLabel);
        body.add(status, BorderLayout.SOUTH);
        reset();
        add(UiTheme.page("Nivel 3 · Tipo 3", "Autómata finito determinista",
                "Lenguaje: a*b*     Alfabeto: {a, b}     Finales: q₀ y q₁", body));
    }

    private void step() {
        if (word.isEmpty()) {
            word = input.getText().trim();
            position = 0;
            state = 0;
            trace.setText("Procesamiento símbolo a símbolo\nq₀");
        }
        if (position >= word.length()) {
            result("La cadena ya terminó.", UiTheme.MUTED);
            return;
        }
        char symbol = word.charAt(position++);
        state = transition(state, symbol);
        trace.append("\nLee '" + symbol + "' → " + stateName(state));
        stateLabel.setText("Estado actual: " + stateName(state) + "     Posición: " + position + "/" + word.length());
        if (position == word.length()) finish();
    }

    private void simulate() {
        word = input.getText().trim();
        position = 0;
        state = 0;
        trace.setText("Procesamiento símbolo a símbolo\nq₀");
        while (position < word.length()) {
            char symbol = word.charAt(position++);
            state = transition(state, symbol);
            trace.append("\nLee '" + symbol + "' → " + stateName(state));
        }
        finish();
    }

    private int transition(int current, char symbol) {
        if ((symbol != 'a' && symbol != 'b') || current == 2 || (current == 1 && symbol == 'a')) return 2;
        return symbol == 'a' ? 0 : 1;
    }

    private String stateName(int value) {
        return value == 0 ? "q₀" : value == 1 ? "q₁" : "qₓ";
    }

    private void finish() {
        boolean accepted = state < 2;
        stateLabel.setText("Estado final: " + stateName(state));
        result(accepted ? "Cadena aceptada por el autómata." : "Cadena rechazada.",
                accepted ? UiTheme.SUCCESS : UiTheme.ERROR);
    }

    private void loadExample() {
        input.setText("aabb");
        resetSimulation();
    }

    private void reset() {
        input.setText("aaabbb");
        resetSimulation();
    }

    private void resetSimulation() {
        word = "";
        position = 0;
        state = 0;
        stateLabel.setText("Estado actual: q₀");
        result("Escribe una cadena para comenzar.", UiTheme.MUTED);
        trace.setText("Diagrama de estados\n\n→ (q₀)* --b--> (q₁)* --a--> [qₓ]\n"
                + "q₀ --a--> q₀    q₁ --b--> q₁    qₓ --a,b--> qₓ");
    }
    private void result(String text, java.awt.Color color) {
        resultLabel.setText(text);
        resultLabel.setForeground(color);
    }
}




