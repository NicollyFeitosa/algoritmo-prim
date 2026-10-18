import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Grafo não direcionado e ponderado, representado por LISTA DE ADJACÊNCIA.
 *
 * Cada vértice tem um nome (ex.: "Aldeota") e um número interno (índice),
 * usado para acessar as listas rapidamente.
 */
public class Grafo {

    private final List<String> nomesDosVertices = new ArrayList<>();
    private final Map<String, Integer> indicePorNome = new HashMap<>();
    private final List<List<Aresta>> listaDeAdjacencia = new ArrayList<>();
    private int quantidadeDeArestas = 0;

    /**
     * Adiciona um vértice ao grafo (se ainda não existir) e devolve o seu índice.
     */
    public int adicionarVertice(String nomeDoVertice) {
        Integer indiceExistente = indicePorNome.get(nomeDoVertice);
        if (indiceExistente != null) {
            return indiceExistente;
        }

        int novoIndice = nomesDosVertices.size();
        nomesDosVertices.add(nomeDoVertice);
        indicePorNome.put(nomeDoVertice, novoIndice);
        listaDeAdjacencia.add(new ArrayList<>());
        return novoIndice;
    }

    /**
     * Adiciona uma aresta não direcionada entre dois vértices.
     * Os vértices são criados automaticamente caso ainda não existam.
     */
    public void adicionarAresta(String nomeOrigem, String nomeDestino, double peso) {
        if (peso < 0) {
            throw new IllegalArgumentException("Peso negativo não é permitido neste exemplo: " + peso);
        }

        int origem = adicionarVertice(nomeOrigem);
        int destino = adicionarVertice(nomeDestino);

        // Grafo não direcionado: a ligação vale nos dois sentidos.
        listaDeAdjacencia.get(origem).add(new Aresta(origem, destino, peso));
        listaDeAdjacencia.get(destino).add(new Aresta(destino, origem, peso));
        quantidadeDeArestas++;
    }

    public int getQuantidadeDeVertices() {
        return nomesDosVertices.size();
    }

    public int getQuantidadeDeArestas() {
        return quantidadeDeArestas;
    }

    public List<Aresta> getVizinhos(int indiceDoVertice) {
        return listaDeAdjacencia.get(indiceDoVertice);
    }

    public String getNomeDoVertice(int indiceDoVertice) {
        return nomesDosVertices.get(indiceDoVertice);
    }

    public Integer getIndiceDoVertice(String nomeDoVertice) {
        return indicePorNome.get(nomeDoVertice);
    }

    public void imprimir() {
        System.out.println("Grafo com " + getQuantidadeDeVertices() + " vértices e "
                + getQuantidadeDeArestas() + " arestas:");
        for (int vertice = 0; vertice < getQuantidadeDeVertices(); vertice++) {
            StringBuilder linha = new StringBuilder("  " + getNomeDoVertice(vertice) + " -> ");
            List<Aresta> vizinhos = getVizinhos(vertice);
            for (int posicao = 0; posicao < vizinhos.size(); posicao++) {
                Aresta aresta = vizinhos.get(posicao);
                linha.append(getNomeDoVertice(aresta.getVerticeDestino()))
                     .append(" (").append(formatarPeso(aresta.getPeso())).append(")");
                if (posicao < vizinhos.size() - 1) {
                    linha.append(", ");
                }
            }
            System.out.println(linha);
        }
    }

    public static String formatarPeso(double peso) {
        if (peso == Math.rint(peso)) {
            return String.valueOf((long) peso);
        }
        return String.valueOf(peso);
    }
}
