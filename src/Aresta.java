/**
 * Representa uma aresta (ligação) entre dois vértices do grafo.
 *
 * Como o grafo é NÃO direcionado, a mesma ligação é guardada duas vezes
 * na lista de adjacência: uma saindo da origem e outra saindo do destino.
 */
public class Aresta implements Comparable<Aresta> {

    private final int verticeOrigem;
    private final int verticeDestino;
    private final double peso;

    public Aresta(int verticeOrigem, int verticeDestino, double peso) {
        this.verticeOrigem = verticeOrigem;
        this.verticeDestino = verticeDestino;
        this.peso = peso;
    }

    public int getVerticeOrigem() {
        return verticeOrigem;
    }

    public int getVerticeDestino() {
        return verticeDestino;
    }

    public double getPeso() {
        return peso;
    }

    /**
     * Permite que a fila de prioridade (PriorityQueue) ordene as arestas
     * do menor peso para o maior peso.
     */
    @Override
    public int compareTo(Aresta outraAresta) {
        return Double.compare(this.peso, outraAresta.peso);
    }
}
