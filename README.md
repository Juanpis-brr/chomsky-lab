# Chomsky Lab

Aplicación de escritorio hecha con Java Swing para visualizar y simular modelos de la jerarquía de Chomsky.

## Requisitos

- JDK 17 o superior (incluye `java` y `javac`).
- No requiere Maven, Gradle, base de datos ni conexión a Internet para ejecutarse.
- Visual Studio Code con Extension Pack for Java es opcional.

## Ejecutar en Visual Studio Code

1. Clona o descarga este repositorio.
2. Abre la carpeta raíz del proyecto en VS Code.
3. Abre `src/Main.java` y pulsa **Run** sobre el método `main`.

## Compilar desde PowerShell

Desde la carpeta raíz del proyecto:

```powershell
$javaFiles = Get-ChildItem src -Filter *.java | ForEach-Object { $_.FullName }
javac -encoding UTF-8 -d bin $javaFiles
java -cp bin Main
```

La compilación coloca los `.class` en `bin/`. Esa carpeta no es necesaria en Git: se vuelve a generar con el comando anterior.

## Módulos y cadenas de prueba

Los botones **Ejemplo** de cada módulo cargan una cadena lista para probar. En Nivel 0, el ejemplo cambia con la máquina seleccionada.

### Nivel 3 — AFD para `a*b*`

Alfabeto `{a, b}`. Se aceptan cero o más `a` seguidas por cero o más `b`.

- Aceptadas: `aabb`, `aaabbb`, `bbb`, cadena vacía.
- Rechazadas: `abba`, `aabx`.
- En la interfaz, **Ejemplo: aabb** carga un caso aceptado.

### Nivel 2 — Autómata de pila para `aⁿbⁿ`, `n ≥ 1`

La pila guarda una marca por cada `a` y retira una por cada `b`.

- Aceptadas: `aabb`, `aaaabbbb`.
- Rechazadas: `aabbb`, `abab`.
- En la interfaz, **Ejemplo: aaaabbbb** carga un caso aceptado.

### Nivel 1 — Cinta acotada para `aⁿbⁿcⁿ`, `n ≥ 1`

La cinta está delimitada por `⊢` y `⊣`; se empareja un símbolo de cada bloque.

- Aceptadas: `abc`, `aabbcc`, `aaabbbccc`.
- Rechazadas: `aabcc`, `abbc`, `aaccbb`.
- En la interfaz, **Ejemplo: aaabbbccc** carga un caso aceptado.

### Nivel 0 — Máquina de Turing

El selector cambia entre dos máquinas:

- **Suma unaria:** acepta cadenas `1ⁿ+1ᵐ`. `11+111` produce `11111` (2 + 3 = 5); `1111+11` produce seis unos.
- **Reconocimiento `aⁿbⁿ`:** acepta `aabb` y `aaaabbbb`; rechaza `aabbb` y entradas que no tengan bloques `a` seguidos por `b`.
- El botón de ejemplo muestra `Ejemplo: 1111+11` para la suma y `Ejemplo: aaaabbbb` para reconocimiento.

## Organización del código

- `Main.java`: ventana principal, navegación y punto de entrada.
- `HomePanel.java`: panel inicial con tarjetas para abrir los módulos.
- `Level3Panel.java` a `Level0Panel.java`: interfaz y lógica de cada simulador.
- `UiTheme.java`: colores, estilos y componentes compartidos.

Cada nivel mantiene el estado de su simulación y su traza en memoria. Los modelos son representaciones didácticas de las transiciones principales.