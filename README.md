# Algoritmo de Prim — Árvore Geradora Mínima (Java)

Implementação do Algoritmo de Prim em Java, aplicada ao problema de **ligar bairros de Fortaleza com fibra óptica usando o menor total de cabo**.

## Dependências

- **Java JDK 11 ou superior** (testado com OpenJDK 17)
- Nenhuma biblioteca externa

Para conferir se o Java está instalado:

```bash
java -version
javac -version
```

Se não estiver instalado (Ubuntu/Debian):

```bash
sudo apt install openjdk-17-jdk
```

No Windows/macOS, baixe o JDK em https://adoptium.net.

## Estrutura do projeto

```
algoritmo-prim/
├── src/
│   ├── Main.java            # ponto de entrada; monta o problema e mostra o resultado
│   ├── AlgoritmoPrim.java   # o algoritmo de Prim
│   ├── Grafo.java           # grafo com lista de adjacência
│   └── Aresta.java          # aresta (origem, destino, peso)
├── exemplos/
│   └── grafo_classico.txt   # grafo extra para testes
└── README.md
```

## Como executar

Na pasta raiz do projeto (`algoritmo-prim/`):

**1. Compilar**

```bash
javac -encoding UTF-8 -d out src/*.java
```

**2. Executar o problema principal (rede de fibra em Fortaleza)**

```bash
java -cp out Main
```

**3. (Opcional) Executar com um grafo de arquivo**

```bash
java -cp out Main exemplos/grafo_classico.txt
```

Também é possível escolher o vértice inicial:

```bash
java -cp out Main exemplos/grafo_classico.txt "D"
```

## Formato do arquivo de entrada

Uma aresta por linha, no formato `Origem;Destino;Peso`. Linhas vazias ou começando com `#` são ignoradas.

```
# comentário
A;B;7
A;D;5
B;C;8
```

## Saída esperada (problema principal)

```
--- Árvore Geradora Mínima encontrada ---
  Edson Queiroz -- Cocó (4)
  Cocó -- Aldeota (3)
  Aldeota -- Meireles (2)
  Meireles -- Centro (3)
  Centro -- Benfica (3)
  Benfica -- Parangaba (5)
  Edson Queiroz -- Messejana (9)

Quantidade de arestas: 7 (V - 1 = 7)
Custo total: 29
```

O programa também imprime o grafo e cada passo do algoritmo, incluindo as arestas descartadas por formarem ciclo.
