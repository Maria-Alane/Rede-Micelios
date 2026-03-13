# Micélios 🍄

Micélios é um aplicativo Android experimental inspirado na ideia de redes naturais de conexão — assim como o micélio conecta raízes na natureza, o app busca conectar pessoas através de momentos, conversas e círculos de interação chamados **Hyphas**.

O objetivo do projeto é explorar arquitetura Android moderna com Kotlin, Room, Navigation e MVVM, simulando uma pequena rede social baseada em compartilhamento de momentos.

---

#  Funcionalidades atuais

Versão atual: **Base funcional inicial**

O aplicativo já possui:

### Autenticação local

* Tela inicial de entrada no aplicativo
* Usuário informa seu nome ao entrar pela primeira vez
* Dados do usuário são armazenados localmente com **Room**

### Perfil

* Tela de perfil com exibição do nome do usuário
* Dados carregados do banco local

### Feed (Home)

* Exibição de momentos compartilhados
* Lista carregada do banco de dados local

### Hyphas

* Criação de Hyphas (círculos de interação)
* Listagem simples das Hyphas criadas

### Momentos

* Criação de momentos dentro de uma Hypha
* Exibição de momentos no feed

### Chat

* Envio de mensagens dentro de uma Hypha
* Histórico de mensagens salvo localmente

---

#  Arquitetura

O projeto segue o padrão **MVVM (Model–View–ViewModel)** com separação em camadas.

```
presentation
    ├── auth
    ├── home
    ├── hypha
    ├── moments
    ├── chat
    └── profile

data
    ├── local
    │     ├── dao
    │     ├── database
    │     └── entity
    └── repository

domain
    └── model
```

### Camadas

**Presentation**

* Fragments
* ViewModels
* ViewBinding
* Navigation

**Data**

* Room Database
* DAOs
* Repositories

**Domain**

* Modelos utilizados pela camada de apresentação

---

# 🧠 Tecnologias utilizadas

* **Kotlin**
* **Android SDK**
* **MVVM**
* **Room Database**
* **Kotlin Coroutines**
* **StateFlow**
* **Navigation Component**
* **ViewBinding**
* **RecyclerView**

---

#  Banco de Dados

O banco local utiliza **Room** e possui as seguintes entidades principais:

```
User
Hypha
Moment
Message
Reaction
```

Essas entidades representam:

| Entidade | Função                               |
| -------- | ------------------------------------ |
| User     | Usuário do aplicativo                |
| Hypha    | Grupo ou círculo social              |
| Moment   | Publicação feita dentro de uma Hypha |
| Message  | Mensagem enviada no chat             |
| Reaction | Reações aos momentos                 |

---

# 🎯 Objetivo do projeto

Este projeto foi criado com o objetivo de:

* praticar arquitetura Android moderna
* estruturar um app completo com Room e Navigation
* explorar boas práticas de organização de código
* servir como base para futuras evoluções de produto

---

# 📌 Status

🚧 Em desenvolvimento.

A versão atual representa a **estrutura base funcional do aplicativo**, com fluxo principal navegável e persistência local de dados.
