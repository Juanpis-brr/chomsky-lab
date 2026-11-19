import java.awt.BorderLayout;
import javax.swing.*;

/** Simula una cinta linealmente acotada para a^n b^n c^n. */
public final class Level1Panel extends JPanel {
    private final JTextField input = UiTheme.input("aabbcc");
    private final JTextArea tapeView = UiTheme.trace();
    private final JLabel resultLabel = UiTheme.label("Fronteras de la cinta: ⊢ y ⊣.", 13, UiTheme.MUTED, false);

    public Level1Panel() {
        setLayout(new BorderLayout());
        JPanel body = UiTheme.workArea();
        JPanel controls = UiTheme.controls();
        controls.add(UiTheme.label("CADENA", 10, UiTheme.MUTED, true));
        controls.add(input);
        controls.add(UiTheme.button("Validar y recorrer", true, this::run));
        controls.add(UiTheme.button("Ejemplo: aaabbbccc", false, this::loadExample));
        controls.add(UiTheme.button("Reiniciar", false, this::reset));
        body.add(controls, BorderLayout.NORTH);
        body.add(UiTheme.scroll(tapeView), BorderLayout.CENTER);
        JPanel status = UiTheme.surface();
        status.setLayout(new BoxLayout(status, BoxLayout.Y_AXIS));
        status.add(UiTheme.label("CINTA ACOTADA", 10, UiTheme.ACCENT, true));
        status.add(Box.createVerticalStrut(7));
        status.add(resultLabel);
        body.add(status, BorderLayout.SOUTH);
        showInitialTape();
        add(UiTheme.page("Nivel 1 · Tipo 1", "Autómata linealmente acotado",
                "Lenguaje: aⁿbⁿcⁿ, n ≥ 1     Espacio de cinta proporcional a la entrada", body));
    }

    private void loadExample() {
        input.setText("aaabbbccc");
        showInitialTape();
        showResult("Ejemplo cargado. Pulsa Validar y recorrer.", UiTheme.MUTED);
    }

    private void reset() {
        input.setText("aabbcc");
        showInitialTape();
        showResult("Cinta reiniciada. Lista para validar.", UiTheme.MUTED);
    }

    private void showInitialTape() {
        StringBuilder cells = new StringBuilder("⊢  ");
        for (char symbol : input.getText().trim().toCharArray()) {
            cells.append(symbol).append("  ");
        }
        cells.append("⊣");
        tapeView.setText("Cinta de trabajo\n\n" + cells + "\n     ▲\n   cabezal\n\n"
                + "El cabezal se desplaza sin cruzar los delimitadores.");
    }
    private void run() {
        String word = input.getText().trim();
        if (word.length() > 80) {
            showResult("Usa una entrada de hasta 80 símbolos para visualizar la cinta.", UiTheme.WARNING);
            return;
        }
        if (!word.matches("a+b+c+")) {
            tapeView.setText("La máquina se detiene: la cinta debe contener los bloques a*, b* y c* en orden.");
            showResult("Rechazada. La cadena debe respetar el orden de los tres bloques.", UiTheme.ERROR);
            return;
        }
        int aCount = count(word, 'a');
        int bCount = count(word, 'b');
        int cCount = count(word, 'c');
        if (aCount != bCount || bCount != cCount) {
            tapeView.setText("Conteo de bloques\n\na = " + aCount + "\nb = " + bCount + "\nc = " + cCount
                    + "\n\nNo se puede emparejar un símbolo de cada bloque.");
            showResult("Rechazada. Los tres bloques deben tener la misma longitud.", UiTheme.ERROR);
            return;
        }

        char[] tape = ("⊢" + word + "⊣").toCharArray();
        StringBuilder history = new StringBuilder("Recorrido bidireccional de la cinta\n\n");
        int step = 0;
        for (int a = 1; a < tape.length - 1 && tape[a] == 'a'; a++) {
            tape[a] = 'X';
            int b = findNext(tape, a + 1, 'b');
            tape[b] = 'Y';
            int c = findNext(tape, b + 1, 'c');
            tape[c] = 'Z';
            snapshot(history, ++step, "marca a, b y c", tape, c);
            snapshot(history, ++step, "regresa a la frontera izquierda", tape, 1);
        }
        snapshot(history, ++step, "verifica la frontera derecha", tape, tape.length - 1);
        tapeView.setText(history.toString());
        showResult("Aceptada. Cada a se emparejó con una b y una c.", UiTheme.SUCCESS);
    }

    private int count(String word, char symbol) {
        int total = 0;
        for (char current : word.toCharArray()) if (current == symbol) total++;
        return total;
    }

    private int findNext(char[] tape, int start, char symbol) {
        for (int i = start; i < tape.length - 1; i++) if (tape[i] == symbol) return i;
        return -1;
    }

    private void snapshot(StringBuilder history, int step, String action, char[] tape, int head) {
        history.append("Paso ").append(step).append(" · ").append(action).append('\n');
        for (char cell : tape) history.append(' ').append(cell).append("  ");
        history.append('\n').append("     ");
        for (int i = 0; i < head; i++) history.append("    ");
        history.append("▲\n\n");
    }

    private void showResult(String text, java.awt.Color color) {
        resultLabel.setText(text);
        resultLabel.setForeground(color);
    }
}


