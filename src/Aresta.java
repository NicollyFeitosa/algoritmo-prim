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

    // Permite que a fila de prioridade ordene as arestas do menor peso para o maior peso.
    @Override
    public int compareTo(Aresta outraAresta) {
        return Double.compare(this.peso, outraAresta.peso);
    }
}
