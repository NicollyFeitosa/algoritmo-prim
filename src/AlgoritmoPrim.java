import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Ideia: começamos com um único vértice e, a cada passo, adicionamos
 * à árvore a aresta MAIS BARATA que liga um vértice que já está na árvore
 * a um vértice que ainda não está. Repetimos até todos os vértices entrarem.
 *
 * Usamos uma fila de prioridade (heap mínimo) para descobrir rapidamente
 * qual é a aresta mais barata disponível. Complexidade: O(E log E).
 */
public class AlgoritmoPrim {

    private final Grafo grafo;
    private final boolean mostrarPassoAPasso;

    private final List<Aresta> arestasDaArvore = new ArrayList<>();                                                                                                                                                                                 
    private double custoTotal = 0;

    public AlgoritmoPrim(Grafo grafo, boolean mostrarPassoAPasso) {
        this.grafo = grafo;
        this.mostrarPassoAPasso = mostrarPassoAPasso;
    }

    public List<Aresta> executar(int verticeInicial) {
        int totalDeVertices = grafo.getQuantidadeDeVertices();
        boolean[] estaNaArvore = new boolean[totalDeVertices];
        PriorityQueue<Aresta> filaDeArestasCandidatas = new PriorityQueue<>();

        arestasDaArvore.clear();
        custoTotal = 0;                                              
                               
        // Passo inicial: o vértice de partida entra na árvore e todas as arestas que saem dele viram candidatas.
        adicionarVerticeNaArvore(verticeInicial, estaNaArvore, filaDeArestasCandidatas);
        int numeroDoPasso = 1;
                                                                                                        
        // A árvore está completa quando tiver (V - 1) arestas.
        while (!filaDeArestasCandidatas.isEmpty() && arestasDaArvore.size() < totalDeVertices - 1) {

            // Pega a aresta candidata mais barata.
            Aresta arestaMaisBarata = filaDeArestasCandidatas.poll();
            int verticeNovo = arestaMaisBarata.getVerticeDestino();

            // Se o destino já está na árvore, essa aresta formaria um CICLO: descartamos.
            if (estaNaArvore[verticeNovo]) {
                if (mostrarPassoAPasso) {
                    System.out.println("        descartada: " + descreverAresta(arestaMaisBarata)
                            + " (formaria ciclo)");
                }
                continue;
            }

            // Aresta aceita: entra na árvore.
            arestasDaArvore.add(arestaMaisBarata);
            custoTotal += arestaMaisBarata.getPeso();

            if (mostrarPassoAPasso) {
                System.out.println("Passo " + numeroDoPasso + ": adiciona " + descreverAresta(arestaMaisBarata)
                        + "  | custo acumulado = " + Grafo.formatarPeso(custoTotal));
            }
            numeroDoPasso++;

            // O novo vértice entra na árvore e suas arestas viram candidatas.
            adicionarVerticeNaArvore(verticeNovo, estaNaArvore, filaDeArestasCandidatas);
        }

        if (arestasDaArvore.size() < totalDeVertices - 1) {
            throw new IllegalStateException(
                    "O grafo não é conexo: não existe árvore geradora que alcance todos os vértices.");
        }

        return arestasDaArvore;
    }                    

    /**
     * Marca o vértice como pertencente à árvore e coloca na fila
     * todas as arestas que levam a vértices que ainda estão FORA da árvore.
     */                              
    private void adicionarVerticeNaArvore(int vertice, boolean[] estaNaArvore,
                                          PriorityQueue<Aresta> filaDeArestasCandidatas) {
        estaNaArvore[vertice] = true;
        for (Aresta arestaVizinha : grafo.getVizinhos(vertice)) {
            if (!estaNaArvore[arestaVizinha.getVerticeDestino()]) {
                filaDeArestasCandidatas.add(arestaVizinha);
            }
        }
    }

    public double getCustoTotal() {
        return custoTotal;
    }

    public String descreverAresta(Aresta aresta) {
        return grafo.getNomeDoVertice(aresta.getVerticeOrigem()) + " -- "
                + grafo.getNomeDoVertice(aresta.getVerticeDestino())
                + " (" + Grafo.formatarPeso(aresta.getPeso()) + ")";
    }
}
