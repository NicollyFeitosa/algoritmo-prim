# Relatório — Seminário AV2 de Grafos

## Algoritmo de Prim

**Universidade de Fortaleza (Unifor)**
**Disciplina:** Grafos
**Equipe:** _[Nome 1]_, _[Nome 2]_, _[Nome 3]_
**Professor(a):** _[Nome do professor]_
**Data:** _[dd/mm/aaaa]_
**Repositório:** _[link do GitHub]_

---

## 1. Introdução

Este relatório apresenta o Algoritmo de Prim, usado para encontrar a **Árvore Geradora Mínima** (AGM) de um grafo não direcionado, conexo e ponderado. O documento traz a definição formal do problema e o pseudocódigo (Seção 2), a implementação em Java com os trechos mais importantes explicados (Seção 3) e uma aplicação prática: o projeto de uma rede de fibra óptica de custo mínimo entre bairros de Fortaleza (Seção 4).

---

## 2. Conceito

### 2.1 Definições preliminares

- **Grafo não direcionado ponderado:** um grafo G = (V, E, w), em que V é o conjunto de vértices, E é o conjunto de arestas (pares não ordenados {u, v}) e w : E → ℝ é uma função que associa um peso a cada aresta.
- **Grafo conexo:** existe caminho entre qualquer par de vértices.
- **Árvore:** grafo conexo e acíclico. Uma árvore com |V| vértices possui exatamente |V| − 1 arestas.
- **Árvore geradora** de G: subgrafo T = (V, E_T), com E_T ⊆ E, que é uma árvore e contém **todos** os vértices de G.

### 2.2 Definição formal do problema

**Problema da Árvore Geradora Mínima (Minimum Spanning Tree — MST).**
Dado um grafo G = (V, E, w) não direcionado, conexo e ponderado, encontrar uma árvore geradora T = (V, E_T) tal que o peso total

  w(T) = Σ_{e ∈ E_T} w(e)

seja **mínimo** entre todas as árvores geradoras de G.

Se G não for conexo, não existe árvore geradora; nesse caso o problema passa a ser encontrar uma *floresta* geradora mínima (uma AGM por componente conexo).

### 2.3 Ideia do Algoritmo de Prim

O Algoritmo de Prim é **guloso**. Ele mantém um conjunto S de vértices que já estão na árvore. Começa com S = {r}, sendo r um vértice qualquer, e a cada iteração escolhe a **aresta de menor peso que cruza o corte (S, V − S)**, ou seja, que liga um vértice de S a um vértice fora de S. Essa aresta entra na árvore e seu vértice externo entra em S. O processo termina quando S = V.

### 2.4 Por que o algoritmo está correto (Propriedade do Corte)

**Propriedade do Corte:** seja (S, V − S) um corte qualquer de G. Se *e* é uma aresta de peso mínimo entre as que cruzam esse corte, então existe alguma AGM de G que contém *e*.

*Esboço da prova:* suponha uma AGM T que não contém e = {u, v}. Adicionar e a T cria exatamente um ciclo. Como u ∈ S e v ∉ S, esse ciclo cruza o corte em outra aresta e'. Trocar e' por e gera uma árvore geradora T' com w(T') = w(T) − w(e') + w(e) ≤ w(T), pois w(e) ≤ w(e'). Logo T' também é mínima e contém e.

Como o Prim **sempre** escolhe a aresta mínima de um corte (S, V − S), toda aresta que ele adiciona pertence a alguma AGM. Por indução, a árvore final é uma AGM.

### 2.5 Pseudocódigo

Versão com fila de prioridade de arestas (a mesma usada na implementação):

```
PRIM(G, r):
    para cada vértice v em V:
        naArvore[v] ← falso
    T ← ∅                         // arestas da árvore
    custo ← 0
    F ← fila de prioridade vazia  // ordenada pelo peso (menor primeiro)

    naArvore[r] ← verdadeiro
    para cada aresta (r, v, p) em adj[r]:
        inserir (r, v, p) em F

    enquanto F não vazia e |T| < |V| − 1:
        (u, v, p) ← remover mínimo de F
        se naArvore[v]:
            continue               // formaria ciclo, descarta
        T ← T ∪ {(u, v)}
        custo ← custo + p
        naArvore[v] ← verdadeiro
        para cada aresta (v, x, q) em adj[v]:
            se não naArvore[x]:
                inserir (v, x, q) em F

    se |T| < |V| − 1:
        erro "grafo não conexo"
    retornar T, custo
```

### 2.6 Complexidade

| Implementação | Tempo |
|---|---|
| Matriz de adjacência + vetor (versão original) | O(V²) |
| Lista de adjacência + heap binário (**usada neste trabalho**) | O(E log E) = O(E log V) |
| Lista de adjacência + heap de Fibonacci | O(E + V log V) |

Na versão usada, cada aresta entra no heap no máximo duas vezes (uma por sentido) e cada inserção/remoção custa O(log E). Como E ≤ V², log E ≤ 2 log V, então O(E log E) = O(E log V). O espaço extra é O(V + E).

A versão O(V²) é melhor para grafos **densos** (E ≈ V²); a versão com heap é melhor para grafos **esparsos**, que são os mais comuns em problemas reais como redes de ruas e cabos.

---

## 3. Implementação

### 3.1 Escolhas e justificativa

- **Linguagem: Java.** Tipagem estática e orientação a objetos deixam claro o papel de cada parte (grafo, aresta, algoritmo). A biblioteca padrão já traz `PriorityQueue` (heap binário mínimo), então não precisamos de nenhuma dependência externa.
- **Representação: lista de adjacência.** Ocupa O(V + E) de memória e permite percorrer só os vizinhos reais de cada vértice, o que combina com grafos esparsos como o da aplicação.
- **Variante do Prim: "preguiçosa" (lazy) com heap de arestas.** Em vez de atualizar prioridades dentro do heap (operação *decrease-key*, que a `PriorityQueue` do Java não oferece), inserimos as arestas e descartamos, na hora de remover, as que levam a um vértice que já está na árvore. É mais simples de implementar e de explicar, e mantém a complexidade O(E log V).
- **Nomes de vértices como texto.** O usuário trabalha com nomes ("Aldeota", "Centro"); internamente cada nome é convertido em um índice inteiro para acesso rápido aos vetores.

### 3.2 Organização do código

| Arquivo | Responsabilidade |
|---|---|
| `Aresta.java` | Guarda origem, destino e peso; implementa `Comparable` para a fila ordenar pelo peso |
| `Grafo.java` | Lista de adjacência, mapeamento nome ↔ índice, inserção de arestas |
| `AlgoritmoPrim.java` | O algoritmo em si, com impressão opcional do passo a passo |
| `Main.java` | Monta o problema da aplicação (ou lê um arquivo) e mostra o resultado |

### 3.3 Trechos relevantes explicados

**(a) Ordenação das arestas pelo peso** — `Aresta.java`

```java
@Override
public int compareTo(Aresta outraAresta) {
    return Double.compare(this.peso, outraAresta.peso);
}
```

A `PriorityQueue` usa esse método para decidir a ordem. Assim, `poll()` sempre devolve a aresta de **menor peso**, que é a escolha gulosa do Prim.

**(b) Grafo não direcionado** — `Grafo.java`

```java
listaDeAdjacencia.get(origem).add(new Aresta(origem, destino, peso));
listaDeAdjacencia.get(destino).add(new Aresta(destino, origem, peso));
```

Cada ligação é guardada nos dois sentidos, pois um cabo entre A e B serve tanto para A → B quanto para B → A.

**(c) Laço principal** — `AlgoritmoPrim.java`

```java
while (!filaDeArestasCandidatas.isEmpty() && arestasDaArvore.size() < totalDeVertices - 1) {

    Aresta arestaMaisBarata = filaDeArestasCandidatas.poll();
    int verticeNovo = arestaMaisBarata.getVerticeDestino();

    if (estaNaArvore[verticeNovo]) {
        continue;                       // formaria ciclo
    }

    arestasDaArvore.add(arestaMaisBarata);
    custoTotal += arestaMaisBarata.getPeso();

    adicionarVerticeNaArvore(verticeNovo, estaNaArvore, filaDeArestasCandidatas);
}
```

- `poll()` remove a aresta candidata mais barata.
- Se o destino já está na árvore, as duas pontas já estão conectadas; aceitar a aresta criaria um ciclo, então ela é descartada. É isso que torna a versão "preguiçosa" correta.
- Caso contrário, a aresta é aceita, o custo é somado e o novo vértice passa a fazer parte da árvore.
- O laço para assim que a árvore tem V − 1 arestas, sem precisar esvaziar a fila.

**(d) Expansão da fronteira** — `AlgoritmoPrim.java`

```java
private void adicionarVerticeNaArvore(int vertice, boolean[] estaNaArvore,
                                      PriorityQueue<Aresta> filaDeArestasCandidatas) {
    estaNaArvore[vertice] = true;
    for (Aresta arestaVizinha : grafo.getVizinhos(vertice)) {
        if (!estaNaArvore[arestaVizinha.getVerticeDestino()]) {
            filaDeArestasCandidatas.add(arestaVizinha);
        }
    }
}
```

Quando um vértice entra na árvore, as arestas que saem dele para vértices de fora passam a cruzar o corte (S, V − S) e viram candidatas. As que levam a vértices já incluídos nem entram na fila.

**(e) Grafo desconexo**

```java
if (arestasDaArvore.size() < totalDeVertices - 1) {
    throw new IllegalStateException(
        "O grafo não é conexo: não existe árvore geradora que alcance todos os vértices.");
}
```

Se a fila acabar antes de termos V − 1 arestas, algum vértice é inalcançável a partir do vértice inicial, e o programa avisa o erro em vez de devolver uma resposta incompleta.

---

## 4. Aplicação

### 4.1 Problema escolhido

Uma provedora de internet quer levar fibra óptica a **8 bairros de Fortaleza**, entre eles Edson Queiroz, bairro da Unifor. Por causa de ruas, avenidas e postes existentes, o cabo só pode passar por certos trechos, cada um com um comprimento em quilômetros. **Objetivo:** conectar todos os bairros, de forma que qualquer bairro alcance qualquer outro pela rede, usando o **menor comprimento total de cabo**.

> Observação: as distâncias são aproximadas e foram definidas para fins didáticos.

### 4.2 Modelagem como grafo

- **Vértices (8):** Edson Queiroz, Cocó, Aldeota, Meireles, Centro, Benfica, Parangaba, Messejana.
- **Arestas (13):** trechos onde é possível passar cabo.
- **Peso:** comprimento do trecho em km.
- **Tipo:** não direcionado (o cabo serve nos dois sentidos), ponderado e conexo.

| Trecho | km | | Trecho | km |
|---|---|---|---|---|
| Edson Queiroz — Cocó | 4 | | Aldeota — Centro | 4 |
| Edson Queiroz — Aldeota | 8 | | Meireles — Centro | 3 |
| Edson Queiroz — Messejana | 9 | | Centro — Benfica | 3 |
| Cocó — Aldeota | 3 | | Centro — Parangaba | 7 |
| Cocó — Messejana | 11 | | Benfica — Parangaba | 5 |
| Cocó — Parangaba | 10 | | Parangaba — Messejana | 12 |
| Aldeota — Meireles | 2 | | | |

Lista de adjacência gerada pelo programa:

```
Edson Queiroz -> Cocó (4), Aldeota (8), Messejana (9)
Cocó          -> Edson Queiroz (4), Aldeota (3), Messejana (11), Parangaba (10)
Aldeota       -> Edson Queiroz (8), Cocó (3), Meireles (2), Centro (4)
Messejana     -> Edson Queiroz (9), Cocó (11), Parangaba (12)
Parangaba     -> Cocó (10), Centro (7), Benfica (5), Messejana (12)
Meireles      -> Aldeota (2), Centro (3)
Centro        -> Aldeota (4), Meireles (3), Benfica (3), Parangaba (7)
Benfica       -> Centro (3), Parangaba (5)
```

_[Inserir aqui a figura do grafo, desenhada no draw.io ou no GraphOnline com as 13 arestas da tabela acima.]_

### 4.3 Execução do algoritmo

Vértice inicial: **Edson Queiroz**. Saída real do programa:

```
Passo 1: adiciona Edson Queiroz -- Cocó (4)  | custo acumulado = 4
Passo 2: adiciona Cocó -- Aldeota (3)  | custo acumulado = 7
Passo 3: adiciona Aldeota -- Meireles (2)  | custo acumulado = 9
Passo 4: adiciona Meireles -- Centro (3)  | custo acumulado = 12
Passo 5: adiciona Centro -- Benfica (3)  | custo acumulado = 15
        descartada: Aldeota -- Centro (4) (formaria ciclo)
Passo 6: adiciona Benfica -- Parangaba (5)  | custo acumulado = 20
        descartada: Centro -- Parangaba (7) (formaria ciclo)
        descartada: Edson Queiroz -- Aldeota (8) (formaria ciclo)
Passo 7: adiciona Edson Queiroz -- Messejana (9)  | custo acumulado = 29
```

Evolução do conjunto S (bairros já conectados):

| Passo | Aresta escolhida | Peso | S depois do passo |
|---|---|---|---|
| 0 | — | — | {Edson Queiroz} |
| 1 | Edson Queiroz — Cocó | 4 | + Cocó |
| 2 | Cocó — Aldeota | 3 | + Aldeota |
| 3 | Aldeota — Meireles | 2 | + Meireles |
| 4 | Meireles — Centro | 3 | + Centro |
| 5 | Centro — Benfica | 3 | + Benfica |
| 6 | Benfica — Parangaba | 5 | + Parangaba |
| 7 | Edson Queiroz — Messejana | 9 | + Messejana (S = V) |

Observe o passo 4: Centro poderia ser alcançado por Aldeota (4 km) ou por Meireles (3 km). O algoritmo escolhe a mais barata, e depois descarta Aldeota — Centro porque Centro já está na árvore.

### 4.4 Solução

**Rede de fibra de custo mínimo (7 trechos = V − 1):**

```
Messejana --9-- Edson Queiroz --4-- Cocó --3-- Aldeota --2-- Meireles
                                                               |
                                                               3
                                                               |
                               Parangaba --5-- Benfica --3-- Centro
```

| Medida | Valor |
|---|---|
| Comprimento total de **todos** os trechos possíveis | 81 km |
| Comprimento da rede mínima (AGM) | **29 km** |
| Economia em relação a usar todos os trechos | 52 km (≈ 64%) |

A solução foi conferida pelo Algoritmo de Kruskal, feito à mão, que também resulta em 29 km. Isso era esperado: os pesos 3 e 4 se repetem, então podem existir AGMs diferentes, mas todas têm o mesmo custo mínimo.

### 4.5 Teste adicional

O arquivo `exemplos/grafo_classico.txt` contém um grafo de 7 vértices muito usado em livros e materiais sobre AGM. O programa encontra custo **39**, o valor conhecido para esse grafo. Também foi testado um grafo desconexo, e o programa informou corretamente que não existe árvore geradora.

---

## 5. Conclusão

O Algoritmo de Prim resolve o problema da Árvore Geradora Mínima com uma estratégia gulosa simples: sempre ligar à árvore o vértice externo mais barato. A Propriedade do Corte garante que essa escolha local leva ao ótimo global. A implementação em Java com lista de adjacência e `PriorityQueue` roda em O(E log V) e resolveu o problema da rede de fibra óptica, reduzindo de 81 km para 29 km o cabo necessário para conectar os 8 bairros.

---

## 6. Referências

- CORMEN, T. H.; LEISERSON, C. E.; RIVEST, R. L.; STEIN, C. *Algoritmos: Teoria e Prática*. 3. ed. Rio de Janeiro: Elsevier, 2012. Cap. 23 — Árvores Geradoras Mínimas.
- SEDGEWICK, R.; WAYNE, K. *Algorithms*. 4. ed. Addison-Wesley, 2011. Seção 4.3 — Minimum Spanning Trees.
- PRIM, R. C. Shortest connection networks and some generalizations. *Bell System Technical Journal*, v. 36, n. 6, p. 1389–1401, 1957.
- JARNÍK, V. O jistém problému minimálním. *Práce Moravské Přírodovědecké Společnosti*, v. 6, p. 57–63, 1930.
- DIJKSTRA, E. W. A note on two problems in connexion with graphs. *Numerische Mathematik*, v. 1, p. 269–271, 1959.
