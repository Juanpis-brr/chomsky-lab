import java.awt.BorderLayout;
import javax.swing.*;

/** Máquina de Turing para suma unaria y reconocimiento de a^n b^n. */
public final class Level0Panel extends JPanel {
    private static final String ADD = "Suma unaria: 1ⁿ + 1ᵐ";
    private static final String RECOGNIZE = "Reconocer aⁿbⁿ";
    private final JComboBox<String> machine = new JComboBox<>(new String[] {ADD, RECOGNIZE});
    private final JTextField input = UiTheme.input("111+11");
    private final JTextArea tapeView = UiTheme.trace();
    private final JLabel resultLabel = UiTheme.label("Listo para iniciar.", 13, UiTheme.MUTED, false);

    public Level0Panel() {
        setLayout(new BorderLayout());
        JPanel body = UiTheme.workArea();
        JPanel controls = UiTheme.controls();
        controls.add(UiTheme.label("MÁQUINA", 10, UiTheme.MUTED, true));
        machine.setFont(UiTheme.font(12, false));
        controls.add(machine);
        controls.add(UiTheme.label("ENTRADA", 10, UiTheme.MUTED, true));
        controls.add(input);
        controls.add(UiTheme.button("Ejecutar", true, this::run));
        JButton exampleButton = UiTheme.button("Ejemplo: 1111+11", false, this::loadExample);
        controls.add(exampleButton);
        controls.add(UiTheme.button("Reiniciar", false, this::reset));
        body.add(controls, BorderLayout.NORTH);
        body.add(UiTheme.scroll(tapeView), BorderLayout.CENTER);
        JPanel status = UiTheme.surface();
        status.setLayout(new BoxLayout(status, BoxLayout.Y_AXIS));
        status.add(UiTheme.label("ESTADO DE EJECUCIÓN", 10, UiTheme.ACCENT, true));
        status.add(Box.createVerticalStrut(7));
        status.add(resultLabel);
        body.add(status, BorderLayout.SOUTH);
        machine.addActionListener(event -> {
            reset();
            exampleButton.setText(machine.getSelectedIndex() == 0 ? "Ejemplo: 1111+11" : "Ejemplo: aaaabbbb");
        });
        showInitialTape();
        add(UiTheme.page("Nivel 0 · Tipo 0", "Máquina de Turing",
                "Cinta extendible, alfabeto de cinta, cabezal y estado activo", body));
    }

    private void loadExample() {
        input.setText(machine.getSelectedIndex() == 0 ? "1111+11" : "aaaabbbb");
        showInitialTape();
        resultLabel.setText("Ejemplo cargado. Pulsa Ejecutar.");
        resultLabel.setForeground(UiTheme.MUTED);
    }

    private void reset() {
        input.setText(machine.getSelectedIndex() == 0 ? "111+11" : "aaabbb");
        showInitialTape();
        resultLabel.setText("Máquina reiniciada. Lista para ejecutar.");
        resultLabel.setForeground(UiTheme.MUTED);
    }

    private void showInitialTape() {
        String word = input.getText().trim();
        String alphabet = machine.getSelectedIndex() == 0
                ? "Alfabeto de cinta: {1, +, □}"
                : "Alfabeto de cinta: {a, b, X, Y, □}";
        tapeView.setText(alphabet + "\n\n… □ ⊢ " + word + " □ ⊣ □ …\n       ▲\n   cabezal · estado q₀");
    }
    private void run() {
        if (machine.getSelectedIndex() == 0) addUnary();
        else recognize();
    }

    private void addUnary() {
        String word = input.getText().trim();
        if (!word.matches("1+\\+1+")) {
            result("Formato inválido. Usa 1ⁿ+1ᵐ, por ejemplo 111+11.", UiTheme.ERROR);
            return;
        }
        int plus = word.indexOf('+');
        char[] tape = (word + "□").toCharArray();
        StringBuilder trace = new StringBuilder("Alfabeto de cinta: {1, +, □}\n\n");
        tape[plus] = '1';
        tape(trace, 1, "q₀ lee '+', escribe '1' y mueve R", tape, plus + 1, "q₁");
        int head = plus + 1;
        int step = 2;
        while (head < tape.length && tape[head] == '1') {
            tape(trace, step++, "q₁ lee '1', conserva el símbolo y mueve R", tape, head, "q₁");
            head++;
        }
        tape(trace, step, "q₁ lee □ y acepta", tape, head, "q_accept");
        tapeView.setText(trace.toString());
        String result = "1".repeat(word.length() - 1);
        result("Aceptada. Resultado: " + result + " = " + result.length() + " unidades.", UiTheme.SUCCESS);
    }

    private void recognize() {
        String word = input.getText().trim();
        if (!word.matches("a+b+")) {
            result("Formato inválido. Usa aⁿbⁿ con n ≥ 1.", UiTheme.ERROR);
            return;
        }
        char[] tape = ("⊢" + word + "□").toCharArray();
        StringBuilder trace = new StringBuilder("Alfabeto de cinta: {a, b, X, Y, □}\n\n");
        int pairs = 0;
        int step = 0;
        while (true) {
            int a = find(tape, 'a');
            if (a < 0) break;
            tape[a] = 'X';
            tape(trace, ++step, "q₀ marca una a como X", tape, a, "q₁");
            int b = find(tape, 'b');
            if (b < 0) {
                tapeView.setText(trace.toString());
                result("Rechazada. No queda una b para emparejar.", UiTheme.ERROR);
                return;
            }
            tape[b] = 'Y';
            tape(trace, ++step, "q₁ marca una b como Y", tape, b, "q₂");
            pairs++;
        }
        boolean accepted = pairs > 0 && find(tape, 'b') < 0;
        tape(trace, ++step, accepted ? "q₃ verifica □ y acepta" : "q₃ encuentra una b sin marcar",
                tape, tape.length - 1, accepted ? "q_accept" : "q_reject");
        tapeView.setText(trace.toString());
        result(accepted ? "Cadena aceptada por la máquina de Turing." : "Rechazada: cantidades distintas.",
                accepted ? UiTheme.SUCCESS : UiTheme.ERROR);
    }

    private int find(char[] tape, char symbol) {
        for (int i = 1; i < tape.length - 1; i++) if (tape[i] == symbol) return i;
        return -1;
    }

    private void tape(StringBuilder trace, int step, String action, char[] tape, int head, String state) {
        trace.append("Paso ").append(step).append(" · ").append(action).append('\n').append("… □  ");
        for (char cell : tape) trace.append(cell).append("  ");
        trace.append("□ …\n      ");
        for (int i = 0; i < head; i++) trace.append("   ");
        trace.append("▲\nEstado activo: ").append(state).append("\n\n");
    }

    private void result(String text, java.awt.Color color) {
        resultLabel.setText(text);
        resultLabel.setForeground(color);
    }
}


