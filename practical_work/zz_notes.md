# 🎮 Arcade Simulator
🎯 Objetivo do Tema:\
Gerir máquinas de arcade e jogadores:
  - máquinas de diferentes tipos (corridas, tiros, dança)
  - pontuações
  - registo de jogos
  - ranking de jogadores

## 🛠️ Requesitos:
  - Criar máquinas de direntes tipos
  - Criar jogadores
  - Registar jogos (máquina + jogador)
  - Gravar pontuações
  - Listar high scores
  - Procurar jogador
  - Guardar dados

## 🟦 Objetivos Gerais
###### 👉 Se cumprida esta secção, garantidos 10–15 valores.
### 🎯 Funcionalidades
✔Herança:
 - 1 superclasse
 - 2 ou mais subclasses diferentes

✔Encapsulamento (Todos os atributos privados)

📘Trabalho Prático:
 - 6 Setters com validações reais
 - Getters quando necessário

✔Sobrecarga
 - Pelo menos um método sobrecarregado
 - Pelo menos dois construtores diferentes

✔Gestão de Listas
 - Usar ArrayList para armazenar objetos
 - Funções para adicionar, remover, listar e procurar

✔Menu interativo
 - Criar objeto
 - Listar objetos
 - Procurar por nome/ID
 - Remover objeto
 - Guardar no ficheiro
 - Carregar do ficheiro
 - Sair (voltar ao menu anterior)

✔Leitura e escrita em ficheiros
 - Guardar dados em .txt ou .csv.

##### 🎯Funcionalidades Práticas Obrigatórias
o projeto deve ter, no mínimo:
  - 🔹Criação de novos objetos
    - ✔ Construtores com validação
    - ✔ Sobrecarga recomendada
  - 🔹Listar objetos
    - ✔ Listar todos
    - ✔ Listar por categoria/tipo/subclasse
  - 🔹Procuras
    - ✔ Procurar por nome
    - ✔ Procurar por ID
    - ✔ Procurar por categoria
  - 🔹Remoção
    - ✔ Remover objetos de listas
    - ✔ Validação se existe / não existe
  - 🔹Ações práticas específicas do tema
    - Exemplos: fazer reparação, adicionar novo tipo de jogo
    - Mínimo obrigatórios: 2 ações práticas reais.
  - 🔹Guardar dados em ficheiro
    - ✔ Ao sair
    - ✔ .txt ou .csv
  - 🔹Carregar dados de ficheiro
    - ✔ Ao iniciar o programa
    - ✔ Validar valores
  - 🔹 Menu interativo

## 🟦 Funcionalidades Opcionais
###### Estas opções aumentam a nota para 16–20 valores.
⭐ Pesquisa avançada
(ex.: filtrar por idade, nível, categoria)

⭐ Relatórios automáticos
(ex.: ranking, faturas, estatísticas)

⭐ Submenus bem organizados
(ex.: menu Heróis, menu Missões...)

⭐ Uso de ficheiros mais complexos
CSV → JSON → binário (opcional)

⭐ Mais tipos de subclasses

⭐ Polimorfismo aplicado
ArrayList<Animal> com override verdadeiro

⭐ Sistema de eventos
alertas, duelos, simulações...

⭐ Auto increment IDs
ex.: gerar ID automaticamente


# ABOUT POLYMORPHISM:
Step 1: Define a Base Class and a Subclass
```java

class Animal {
public void sound() {
System.out.println("Animal makes a sound");
}
}

class Dog extends Animal {
@Override
public void sound() {
System.out.println("Dog barks");
}
}

class Cat extends Animal {
@Override
public void sound() {
System.out.println("Cat meows");
}
}
```
Step 2: Using an ArrayList to Demonstrate Polymorphism
```java

import java.util.ArrayList;

public class PolymorphismExample {
public static void main(String[] args) {
ArrayList<Animal> animals = new ArrayList<>();

        animals.add(new Dog());
        animals.add(new Cat());
        
        for(Animal animal : animals) {
            animal.sound(); // Calls the overridden method
        }
    }
}```
Cinder juntou-se.