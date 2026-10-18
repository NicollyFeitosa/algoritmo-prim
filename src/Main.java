import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Ponto de entrada do programa.
 *
 * PROBLEMA ESCOLHIDO (Aplicação):
 *   Uma provedora de internet quer ligar, com cabos de fibra óptica, 8 bairros
 *   de Fortaleza (incluindo o bairro da Unifor, Edson Queiroz). Cada aresta é um
 *   trecho onde é possível passar cabo, e o peso é o comprimento em km.
 *   Objetivo: conectar TODOS os bairros gastando o MENOR total de cabo.
 *   Isso é exatamente a Árvore Geradora Mínima, encontrada pelo Algoritmo de Prim.
 *
 * Uso:
 *   java -cp out Main                          -> roda o exemplo embutido
 *   java -cp out Main arquivo.txt              -> lê o grafo de um arquivo
 *   java -cp out Main arquivo.txt "Centro"     -> escolhe o vértice inicial
 */
public class Main {

    public static void main(String[] argumentos) {
        Grafo grafo;
        String nomeDoVerticeInicial;

        if (argumentos.length >= 1) {
            grafo = lerGrafoDoArquivo(argumentos[0]);
            nomeDoVerticeInicial = argumentos.length >= 2 ? argumentos[1] : grafo.getNomeDoVertice(0);
        } else {
            grafo = criarGrafoDaRedeDeFibra();
            nomeDoVerticeInicial = "Edson Queiroz";
        }

        Integer verticeInicial = grafo.getIndiceDoVertice(nomeDoVerticeInicial);
        if (verticeInicial == null) {
            System.err.println("Vértice inicial não encontrado: " + nomeDoVerticeInicial);
            System.exit(1);
        }

        System.out.println("==============================================");
        System.out.println("            ALGORITMO DE PRIM");
        System.out.println("==============================================\n");
        grafo.imprimir();

        System.out.println("\nVértice inicial: " + nomeDoVerticeInicial);
        System.out.println("\n--- Execução passo a passo ---");

        AlgoritmoPrim algoritmoPrim = new AlgoritmoPrim(grafo, true);
        List<Aresta> arvoreGeradoraMinima;
        try {
            arvoreGeradoraMinima = algoritmoPrim.executar(verticeInicial);
        } catch (IllegalStateException erro) {
            System.err.println("\nErro: " + erro.getMessage());
            System.exit(1);
            return;
        }

        System.out.println("\n--- Árvore Geradora Mínima encontrada ---");
        for (Aresta aresta : arvoreGeradoraMinima) {
            System.out.println("  " + algoritmoPrim.descreverAresta(aresta));
        }
        System.out.println("\nQuantidade de arestas: " + arvoreGeradoraMinima.size()
                + " (V - 1 = " + (grafo.getQuantidadeDeVertices() - 1) + ")");
        System.out.println("Custo total: " + Grafo.formatarPeso(algoritmoPrim.getCustoTotal()));
    }

    /**
     * Monta o grafo do problema de aplicação: rede de fibra óptica entre bairros
     * de Fortaleza. As distâncias (em km) são aproximadas e servem para ilustração.
     */
    private static Grafo criarGrafoDaRedeDeFibra() {
        Grafo grafo = new Grafo();
        grafo.adicionarAresta("Edson Queiroz", "Cocó", 4);
        grafo.adicionarAresta("Edson Queiroz", "Aldeota", 8);
        grafo.adicionarAresta("Edson Queiroz", "Messejana", 9);
        grafo.adicionarAresta("Cocó", "Aldeota", 3);
        grafo.adicionarAresta("Cocó", "Messejana", 11);
        grafo.adicionarAresta("Cocó", "Parangaba", 10);
        grafo.adicionarAresta("Aldeota", "Meireles", 2);
        grafo.adicionarAresta("Aldeota", "Centro", 4);
        grafo.adicionarAresta("Meireles", "Centro", 3);
        grafo.adicionarAresta("Centro", "Benfica", 3);
        grafo.adicionarAresta("Centro", "Parangaba", 7);
        grafo.adicionarAresta("Benfica", "Parangaba", 5);
        grafo.adicionarAresta("Parangaba", "Messejana", 12);
        return grafo;
    }

    /**
     * Lê um grafo de um arquivo de texto. Cada linha deve ter o formato:
     *     Origem;Destino;Peso
     * Linhas vazias ou começando com '#' são ignoradas.
     */
    private static Grafo lerGrafoDoArquivo(String caminhoDoArquivo) {
        Grafo grafo = new Grafo();
        List<String> linhas;
        try {
            linhas = Files.readAllLines(Path.of(caminhoDoArquivo), StandardCharsets.UTF_8);
        } catch (IOException erro) {
            System.err.println("Não foi possível ler o arquivo: " + caminhoDoArquivo);
            System.exit(1);
            return grafo;
        }

        int numeroDaLinha = 0;
        for (String linha : linhas) {
            numeroDaLinha++;
            String linhaLimpa = linha.trim();
            if (linhaLimpa.isEmpty() || linhaLimpa.startsWith("#")) {
                continue;
            }

            String[] partes = linhaLimpa.split(";");
            if (partes.length != 3) {
                System.err.println("Linha " + numeroDaLinha + " inválida (use Origem;Destino;Peso): " + linha);
                System.exit(1);
            }

            try {
                double peso = Double.parseDouble(partes[2].trim().replace(',', '.'));
                grafo.adicionarAresta(partes[0].trim(), partes[1].trim(), peso);
            } catch (IllegalArgumentException erro) {
                System.err.println("Linha " + numeroDaLinha + " com peso inválido: " + linha);
                System.exit(1);
            }
        }

        if (grafo.getQuantidadeDeVertices() == 0) {
            System.err.println("O arquivo não contém nenhuma aresta.");
            System.exit(1);
        }
        return grafo;
    }
}
