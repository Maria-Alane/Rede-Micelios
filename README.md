# Micélios 🍄

Micélios é um aplicativo Android experimental inspirado na ideia de redes naturais de conexão — assim como o micélio conecta raízes na natureza, o app busca conectar pessoas por meio de momentos, conversas e círculos de interação chamados **Hyphas**.

O projeto começou como uma aplicação local para explorar fundamentos de Android moderno e evoluiu para uma base com **backend remoto usando Firebase**, permitindo autenticação, persistência em nuvem e fluxos mais próximos de um produto real.

---

## ✨ Proposta do app

A proposta do Micélios é criar uma rede social mais intimista, centrada em:

- **Hyphas**: pequenos círculos sociais privados.
- **Momentos**: publicações curtas associadas a uma hypha.
- **Chat**: conversa entre membros da hypha.
- **Perfil**: identidade e atividade recente do usuário dentro do app.

---

## 🚀 Estado atual do projeto

Versão atual: **Frontend funcional com backend Firebase integrado**.

O app já possui:

### 🔐 Autenticação
- Criação de conta com **nome, e-mail e senha**.
- Login com conta existente.
- Persistência de sessão via **Firebase Authentication**.
- Validação de sessão ao abrir o app.

### 🏠 Feed / Home
- Exibição de momentos recentes das hyphas do usuário.
- Feed baseado nos dados do **Firestore**.
- Atualização orientada ao usuário autenticado.

### 🍄 Hyphas
- Listagem das hyphas do usuário.
- Tela dedicada para criação de nova hypha.
- Convite de membro via **busca por e-mail**.
- Tela de detalhe da hypha com:
  - nome
  - descrição
  - tipo
  - membros
  - momentos associados

### ✍️ Momentos
- Publicação de momento em uma hypha selecionada.
- Seleção de hypha por lista visual.
- Persistência remota no Firestore.

### 💬 Chat
- Chat por hypha
- Lista de mensagens com diferenciação entre:
  - mensagens enviadas
  - mensagens recebidas
- Atualização em tempo real via Firestore

### 👤 Perfil
- Exibição do nome e bio do usuário
- Visualização das hyphas em que participa
- Exibição de momentos recentes do próprio usuário

---

## 🧱 Tecnologias e bibliotecas

- **Kotlin**
- **Android SDK**
- **ViewBinding**
- **Navigation Component**
- **RecyclerView**
- **Material Design Components**
- **Coroutines**
- **StateFlow**
- **Firebase Authentication**
- **Cloud Firestore**
- **Hilt** (injeção de dependência)

---

## 🏗️ Arquitetura e organização

O projeto está evoluindo para uma arquitetura organizada, com separação entre:

- **presentation**: Fragments, ViewModels, adapters e estados de UI
- **data**:
  - repositórios
  - implementações remotas com Firebase
  - DTOs
  - mappers
- **domain**: modelos centrais do app
- **di**: módulos de injeção com Hilt

### 📐 Padrões utilizados
- MVVM
- Repository Pattern
- Injeção de dependência com Hilt
- Fluxos reativos com `Flow` / `StateFlow`

---

## 📱 Telas

### Boas-vindas / Login
<img width="383" height="864" alt="image" src="https://github.com/user-attachments/assets/87c943fb-0e25-492b-a2a5-acde96cb6090" />

### Feed
<img width="387" height="861" alt="image" src="https://github.com/user-attachments/assets/99a3257c-37d6-4274-a492-0c54cd148542" />


### Lista de Hyphas
<img width="388" height="858" alt="image" src="https://github.com/user-attachments/assets/5cbca008-7745-423c-86e3-5bb7ff93a2dd" />


### Criar Hypha
<img width="384" height="858" alt="image" src="https://github.com/user-attachments/assets/69e54a8f-d45f-4553-8cd9-c2098f8232ed" />


### Detalhe da Hypha
<img width="385" height="861" alt="image" src="https://github.com/user-attachments/assets/3090fc03-4c9b-4a9f-932b-f83d149f32a1" />


### Postar Momento
<img width="384" height="862" alt="image" src="https://github.com/user-attachments/assets/9904410a-057d-4197-a7a5-20c4bcf52967" />


### Chat
<img width="384" height="863" alt="image" src="https://github.com/user-attachments/assets/74199634-0745-43cd-9ef3-32dd58cac717" />


### Perfil
<img width="383" height="862" alt="image" src="https://github.com/user-attachments/assets/95a61a3b-d756-459d-8b67-b1c6453898c4" />


---

## 🔥 Backend Firebase

O projeto utiliza Firebase para suportar os fluxos principais da aplicação:

### Firebase Authentication
Responsável por:
- cadastro de usuário
- login
- manutenção da sessão autenticada

### Cloud Firestore
Responsável por persistir:
- usuários
- hyphas
- membros de hyphas
- momentos
- mensagens

---

## 📂 Estrutura do projeto

```text
com.example.micelios
│
├── data
│   ├── mapper
│   ├── remote
│   │   ├── auth
│   │   ├── dto
│   │   └── firestore
│   └── repository
│
├── di
│
├── domain
│   └── model
│
└── presentation
    ├── auth
    ├── chat
    ├── home
    ├── hypha
    ├── moments
    ├── profile
    └── common



### Projeto em Desenvolvimento 
