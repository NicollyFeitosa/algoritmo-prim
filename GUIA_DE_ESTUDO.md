# Guia de Estudo — Entendendo o Algoritmo de Prim

> Material para a equipe estudar e se preparar para a apresentação. Não é para entregar.

---

## 1. O problema em uma frase

> **"Como ligar todos os pontos gastando o mínimo possível?"**

Imagine que vocês precisam ligar 8 bairros com cabo de fibra. Existem vários trechos possíveis, cada um com um custo. Vocês querem que **todo bairro consiga falar com todo bairro**, mas sem desperdiçar cabo. A resposta é a **Árvore Geradora Mínima** (AGM), e o Prim é uma das formas de encontrá-la.

### Os três termos que precisam estar na ponta da língua

| Termo | Significado simples |
|---|---|
| **Árvore** | Grafo conectado e **sem ciclos**. Com V vértices, tem sempre **V − 1 arestas**. |
| **Geradora** | Passa por **todos** os vértices do grafo. |
| **Mínima** | Entre todas as árvores geradoras possíveis, é a de **menor soma de pesos**. |

**Por que sem ciclos?** Um ciclo significa que existe um cabo "sobrando": dá para tirar qualquer aresta do ciclo e tudo continua conectado, só que mais barato.

---

## 2. A ideia do Prim (a intuição)

Pensem numa **mancha de tinta que vai crescendo**:

1. Escolham um vértice qualquer para começar. Ele é a "mancha".
2. Olhem todas as arestas que saem da mancha para fora dela.
3. Peguem a **mais barata** e "pintem" o vértice do outro lado.
4. Repitam até a mancha cobrir o grafo inteiro.

Só isso. O algoritmo é **guloso**: em cada passo ele pega o que parece melhor naquele momento, sem pensar no futuro. A parte surpreendente é que, para esse problema, a escolha gulosa **sempre** dá a resposta ótima.

### Por que funciona? (a pergunta que o professor pode fazer)

**Propriedade do Corte**, em linguagem simples:

> Se vocês dividirem os vértices em dois grupos (os que estão dentro da árvore e os que estão fora), a aresta **mais barata** que liga os dois grupos **sempre** pode fazer parte de uma árvore mínima.

Por quê? Os dois grupos precisam ser ligados de algum jeito. Se alguém montasse uma árvore usando uma ligação mais cara entre eles, bastaria trocá-la pela mais barata: a árvore continuaria conectada e ficaria mais barata ou igual. Como o Prim sempre pega a mais barata entre "dentro" e "fora", ele nunca erra.

---

## 3. Passo a passo com o nosso exemplo

Grafo dos bairros (peso = km):

| Trecho | km |
|---|---|
| Edson Queiroz — Cocó | 4 |
| Edson Queiroz — Aldeota | 8 |
| Edson Queiroz — Messejana | 9 |
| Cocó — Aldeota | 3 |
| Cocó — Messejana | 11 |
| Cocó — Parangaba | 10 |
| Aldeota — Meireles | 2 |
| Aldeota — Centro | 4 |
| Meireles — Centro | 3 |
| Centro — Benfica | 3 |
| Centro — Parangaba | 7 |
| Benfica — Parangaba | 5 |
| Parangaba — Messejana | 12 |

Vamos começar por **Edson Queiroz**. Em cada passo, "candidatas" são as arestas que saem da árvore para fora.

**Início** — Na árvore: {Edson Queiroz}
Candidatas: Cocó (4), Aldeota (8), Messejana (9)
→ Menor: **Edson Queiroz — Cocó (4)**. Custo = 4

**Passo 2** — Na árvore: {EQ, Cocó}
Candidatas: Aldeota (8, via EQ), Messejana (9, via EQ), Aldeota (3, via Cocó), Messejana (11), Parangaba (10)
→ Menor: **Cocó — Aldeota (3)**. Custo = 7

**Passo 3** — Na árvore: {EQ, Cocó, Aldeota}
Novas candidatas vindas de Aldeota: Meireles (2), Centro (4)
→ Menor: **Aldeota — Meireles (2)**. Custo = 9

**Passo 4** — Na árvore: {EQ, Cocó, Aldeota, Meireles}
Nova candidata: Centro (3, via Meireles). Já havia Centro (4, via Aldeota).
→ Menor: **Meireles — Centro (3)**. Custo = 12
*Repare: Centro tinha dois caminhos e o Prim ficou com o mais barato.*

**Passo 5** — Na árvore: {…, Centro}
Novas candidatas: Benfica (3), Parangaba (7)
→ Menor: **Centro — Benfica (3)**. Custo = 15

**Passo 6** — A próxima menor na fila é Aldeota — Centro (4), mas **Centro já está na árvore**. Aceitá-la criaria o ciclo Aldeota → Meireles → Centro → Aldeota. **Descartada.**
Nova candidata de Benfica: Parangaba (5)
→ Menor válida: **Benfica — Parangaba (5)**. Custo = 20

**Passo 7** — Descartadas: Centro — Parangaba (7) e EQ — Aldeota (8), porque os dois lados já estão na árvore.
→ Menor válida: **Edson Queiroz — Messejana (9)**. Custo = 29

**Fim:** 8 vértices, 7 arestas (V − 1). **Custo total = 29 km.**

```
Messejana --9-- Edson Queiroz --4-- Cocó --3-- Aldeota --2-- Meireles
                                                               |
                                                               3
                                                               |
                               Parangaba --5-- Benfica --3-- Centro
```

**Dica para a apresentação:** desenhem o grafo no quadro e pintem os vértices conforme entram na árvore. Visualmente é muito fácil de entender.

---

## 4. Como o código faz isso

### Arquivos

- **`Aresta.java`**: guarda *origem*, *destino* e *peso*. Tem o `compareTo`, que ensina o Java a ordenar arestas pelo peso.
- **`Grafo.java`**: guarda, para cada vértice, a lista de vizinhos (lista de adjacência). Cada aresta é guardada **duas vezes** (A→B e B→A), porque o grafo é não direcionado.
- **`AlgoritmoPrim.java`**: o algoritmo.
- **`Main.java`**: monta o grafo dos bairros, chama o Prim e imprime o resultado.

### As três estruturas principais do `AlgoritmoPrim`

| Variável | O que é | Papel na analogia |
|---|---|---|
| `estaNaArvore[]` | vetor de `true`/`false` por vértice | quais vértices já estão "pintados" |
| `filaDeArestasCandidatas` | `PriorityQueue<Aresta>` (heap mínimo) | as arestas na borda da mancha, a mais barata sempre na frente |
| `arestasDaArvore` | lista com o resultado | os cabos que vamos instalar |

### O ciclo de vida de cada passo

```
1. poll()  → tira da fila a aresta mais barata
2. O destino já está na árvore?
      sim → descarta (formaria ciclo) e volta ao 1
      não → segue
3. Adiciona a aresta ao resultado e soma o peso
4. Marca o destino como "na árvore"
5. Coloca na fila as arestas do novo vértice que vão para fora da árvore
6. Repete até ter V − 1 arestas
```

### Por que existe o "descarta"?

A fila do Java não deixa **atualizar** uma aresta que já está lá dentro. Então, quando o Centro entrou na árvore pelo Meireles (3), a aresta Aldeota — Centro (4) continuou na fila. Quando ela chega na frente, a gente só verifica: "o Centro já está na árvore?" Está, então ela é ignorada. Essa versão se chama **Prim preguiçoso (lazy)**.

### Complexidade (resposta curta)

- **O(E log V)**, sendo E o número de arestas e V o número de vértices.
- Cada aresta entra e sai do heap no máximo duas vezes, e cada operação no heap custa log.
- Existe uma versão O(V²) com matriz, que é melhor quando o grafo é muito denso (quase todos os vértices ligados entre si).

---

## 5. Prim × Kruskal × Dijkstra (pergunta muito provável)

| | **Prim** | **Kruskal** | **Dijkstra** |
|---|---|---|---|
| Resolve | Árvore geradora mínima | Árvore geradora mínima | **Caminho mínimo** a partir de uma origem |
| Estratégia | Cresce **uma árvore** a partir de um vértice | Ordena **todas** as arestas e vai juntando "florestas" | Cresce a partir da origem pela menor **distância acumulada** |
| Estrutura-chave | Fila de prioridade | Union-Find (conjuntos disjuntos) | Fila de prioridade |
| Melhor para | Grafos densos | Grafos esparsos | — |
| Grafo desconexo | Só cobre o componente do início | Gera uma floresta naturalmente | — |

**Prim vs. Dijkstra (pegadinha clássica):** o código é quase igual. A diferença é a prioridade na fila:
- **Prim** usa o **peso da aresta** ("quanto custa esse cabo?").
- **Dijkstra** usa a **distância total desde a origem** ("quanto custa chegar até aqui?").

A AGM **não** garante o caminho mais curto entre dois pontos. Na nossa rede, de Messejana até Centro dá 9+4+3+2+3 = 21 km pela árvore, mas no grafo original existe Messejana → EQ → Cocó → Aldeota → Centro = 9+4+3+4 = **20 km**. A AGM minimiza o **total de cabo**, não a distância entre dois bairros específicos.

---

## 6. Para que serve e onde é usado hoje

- **Redes de telecomunicações e internet:** ligar cidades, prédios ou antenas com o mínimo de fibra ou cabo. Foi para isso que o algoritmo foi popularizado.
- **Redes elétricas e de água/gás:** projetar a distribuição com o menor comprimento de tubulação ou fiação.
- **Projeto de circuitos (VLSI):** ligar pinos em chips com o mínimo de fio.
- **Agrupamento de dados (clustering):** monta-se a AGM dos pontos e removem-se as k − 1 arestas mais caras, o que separa os dados em k grupos. É usado em biologia (agrupar genes), em marketing (segmentar clientes) e em análise de imagens.
- **Segmentação de imagens:** pixels são vértices e a diferença de cor é o peso; a AGM ajuda a separar regiões da imagem.
- **Aproximação do Caixeiro Viajante:** percorrer a AGM dá uma rota no máximo 2× pior que a ótima (quando as distâncias obedecem à desigualdade triangular).
- **Geração de labirintos:** Prim com pesos aleatórios gera labirintos "perfeitos" (com exatamente um caminho entre quaisquer dois pontos). É muito usado em jogos.
- **Redes de computadores:** o protocolo **STP (Spanning Tree Protocol)**, usado em switches, monta uma árvore geradora para evitar *loops* na rede. Não é o Prim exatamente, mas é o mesmo conceito.

---

## 7. História

- **1926 — Otakar Borůvka (Tchecoslováquia):** primeiro algoritmo para a AGM. O motivo era projetar uma rede elétrica eficiente na região da Morávia.
- **1930 — Vojtěch Jarník (Tchecoslováquia):** matemático que descreveu o algoritmo que hoje chamamos de Prim, num artigo em tcheco que ficou pouco conhecido fora do país.
- **1956 — Joseph Kruskal (EUA):** publica o algoritmo de Kruskal.
- **1957 — Robert C. Prim (EUA):** redescobre o algoritmo de forma independente enquanto trabalhava na **Bell Labs**, a empresa de telefonia americana. O objetivo era conectar centrais telefônicas com o menor custo. O artigo, *"Shortest connection networks and some generalizations"*, tornou o método famoso.
- **1959 — Edsger Dijkstra (Holanda):** redescobre o algoritmo mais uma vez, no mesmo artigo em que apresenta seu famoso algoritmo de caminho mínimo.

Por isso o algoritmo também é chamado de **algoritmo de Jarník**, **Prim–Jarník** ou **Prim–Dijkstra**. Essa curiosidade costuma render pontos na apresentação.

---

## 8. Roteiro sugerido para a apresentação (20 min)

| Tempo | Parte | Quem | Conteúdo |
|---|---|---|---|
| 0–2 min | Abertura | _[integrante 1]_ | Problema dos bairros e a pergunta "como ligar tudo gastando menos?" |
| 2–7 min | Conceito | _[integrante 1]_ | Árvore, geradora, mínima; definição formal; ideia gulosa; propriedade do corte |
| 7–12 min | Execução no quadro | _[integrante 2]_ | Passo a passo no grafo dos bairros, pintando os vértices |
| 12–16 min | Implementação | _[integrante 3]_ | Estruturas, laço principal, descarte de ciclo, complexidade; rodar o programa ao vivo |
| 16–20 min | Aplicações, história, perguntas | todos | Seções 6 e 7 deste guia |

---

## 9. Perguntas que podem aparecer (com respostas)

**1. E se o grafo for desconexo?**
Não existe árvore geradora. O Prim só alcança o componente do vértice inicial. Nosso código detecta isso (a árvore fica com menos de V − 1 arestas) e mostra um erro.

**2. O vértice inicial muda o resultado?**
O **custo total** não muda nunca. As arestas escolhidas podem mudar se houver pesos repetidos, porque podem existir várias AGMs com o mesmo custo.

**3. Funciona com pesos negativos?**
Sim. O Prim só compara pesos de arestas, não soma distâncias como o Dijkstra. Nosso código bloqueia pesos negativos apenas porque, no problema de cabos, distância negativa não faz sentido.

**4. E se todos os pesos forem diferentes?**
A AGM é **única**.

**5. Funciona em grafo direcionado?**
Não. Para grafos direcionados, o problema equivalente é a *arborescência geradora mínima*, resolvida pelo algoritmo de Chu–Liu/Edmonds.

**6. Por que a árvore tem exatamente V − 1 arestas?**
Cada aresta aceita traz exatamente um vértice novo para a árvore. Começamos com 1 vértice e precisamos trazer os outros V − 1.

**7. Por que usar fila de prioridade?**
Para achar a aresta mais barata em O(log E) em vez de olhar todas as arestas a cada passo, o que levaria O(E).

**8. Qual a diferença para o Kruskal?**
O Prim cresce **uma única árvore** a partir de um vértice. O Kruskal olha as arestas em ordem global de peso e vai unindo pedaços separados, usando Union-Find para evitar ciclos. Os dois chegam ao mesmo custo.

---

## 10. Resumo de uma linha

> **Prim = começar num vértice e, repetidamente, puxar para a árvore o vizinho mais barato, até todos estarem conectados.**
