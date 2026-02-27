Com base no código da solução (HTML, CSS e JavaScript para as páginas `index.html`, `denuncia.html`, `forum.html`, `infoMap.html`, `perfil.html`) e nos requisitos de entrega, segue uma proposta de estrutura e conteúdo para o arquivo **`README.md`** do seu repositório GitHub, respeitando todos os critérios de avaliação:

-----

# 🌐 Sustentabilidade Tapera: Monitoramento ODS 6

[]https://github.com/douglasCarmo/Tapera-ODS6.git

\<hr\>

## 🎯 Objetivo do Projeto

Este projeto consiste no desenvolvimento de um **Sistema Web Completo com Front-End Funcional** focado na **Gestão e Monitoramento do Saneamento Básico no bairro Tapera**, alinhado com o **Objetivo de Desenvolvimento Sustentável (ODS) 6: Água Potável e Saneamento**.

O sistema foi desenvolvido como parte da avaliação da disciplina de Desenvolvimento de Sistema Web, visando demonstrar a aplicação de boas práticas de codificação, design de software, versionamento e documentação técnica.

\<hr\>

## ✨ Funcionalidades e Atendimento aos Requisitos

O sistema atende aos requisitos funcionais e não funcionais definidos, provendo uma interface responsiva e interativa para a comunidade:

| Requisito Funcional | Descrição | Status |
| :--- | :--- | :--- |
| **Página Inicial (`index.html`)** | Apresenta o projeto, a ODS 6 e estatísticas simuladas de engajamento da comunidade (Denúncias, Problemas Resolvidos, Publicações no Fórum). | ✅ Implementado |
| **Denúncias (`denuncia.html`)** | Permite o registro e a visualização de denúncias de problemas de saneamento (vazamentos, esgoto a céu aberto, etc.) com persistência simulada via **`localStorage`**. | ✅ Implementado |
| **Fórum Comunitário (`forum.html`)** | Espaço para a comunidade criar e visualizar tópicos de discussão sobre sustentabilidade e saneamento no bairro, com persistência simulada via **`localStorage`**. | ✅ Implementado |
| **Mapa Interativo (`infoMap.html`)** | Exibe um mapa da região (**Leaflet.js**) com marcações simuladas de pontos de interesse (ETE, denúncias ativas, pontos de inspeção da rede). | ✅ Implementado |
| **Perfil do Usuário (`perfil.html`)** | Tela para o usuário visualizar e editar informações de perfil, com persistência simulada e exibição de estatísticas individuais (Denúncias Enviadas, Problemas Resolvidos). | ✅ Implementado |
| **Navegação** | Menu de navegação fixo e responsivo, garantindo acesso rápido a todas as seções. | ✅ Implementado |

| Requisito Não Funcional | Atendimento |
| :--- | :--- |
| **Responsividade** | Implementação utilizando o sistema de **Grid e Componentes do Bootstrap 5**, garantindo adaptação total a dispositivos móveis e desktops. |
| **Qualidade do Código** | Código limpo, legível, com identação padronizada e uso consistente de boas práticas de HTML5, CSS3 e JavaScript. |
| **Documentação** | Comentários detalhados no código-fonte e documentação técnica completa no `README.md`. |
| **Versionamento** | Histórico de `commits` claro e descritivo, seguindo um padrão de mensagem (ver seção abaixo). |

\<hr\>

## 🛠️ Tecnologias Utilizadas

Este projeto de Front-End foi desenvolvido com as seguintes tecnologias:

  * **HTML5:** Estrutura semântica das páginas.
  * **CSS3 (`style.css`):** Estilização customizada e visual do sistema.
  * **JavaScript (`script.js`):** Lógica de interação, manipulação do DOM e simulação de *backend* (utilizando **`localStorage`** para persistência de dados).
  * **[Bootstrap 5](https://getbootstrap.com/):** Framework CSS para a base do design responsivo, componentes (Navbar, Cards, Formulários, Botões, etc.) e usabilidade.
  * **[Leaflet.js](https://leafletjs.com/):** Biblioteca JavaScript para a criação do mapa interativo na página `infoMap.html`.
  * **[Bootstrap Icons](https://icons.getbootstrap.com/):** Para ícones visuais em todo o sistema.

\<hr\>

## 📂 Estrutura do Repositório (Design e Organização)

O repositório está organizado de forma clara para facilitar a manutenção e a expansão.

```
sustentabilidade-tapera/
├── index.html            # Página inicial do sistema
├── denuncia.html         # Página de registro e visualização de denúncias
├── forum.html            # Página do fórum comunitário
├── infoMap.html          # Página do mapa interativo (Leaflet)
├── perfil.html           # Página de perfil do usuário (simulado)
├── style.css             # Estilos CSS customizados e variáveis de tema
├── script.js             # Lógica JavaScript (interação, persistência local)
├── README.md             # Documentação principal (este arquivo)
└── img/                  # Pasta para imagens e ativos (ex: Logo.png)
```

\<hr\>

## ⚙️ Como Executar o Projeto

Para visualizar e testar o sistema web, siga os passos simples abaixo:

1.  **Clone o Repositório:**
    ```bash
    git clone [LINK DO SEU REPOSITÓRIO]
    ```
2.  **Acesse a Pasta do Projeto:**
    ```bash
    cd sustentabilidade-tapera
    ```
3.  **Abra no Navegador:**
      * Simplesmente abra o arquivo `index.html` em qualquer navegador web (Chrome, Firefox, Edge, etc.).

Como o projeto é puramente Front-End (HTML, CSS e JavaScript), não é necessário um servidor web ou instalações adicionais.

\<hr\>

## 📝 Qualidade do Código e Documentação

O projeto foi construído com forte foco na **Qualidade do Software** e **Documentação**:

  * **Comentários:** Utilização de comentários claros em português para explicar a função de blocos complexos de código (em HTML, CSS e, principalmente, no `script.js` onde a lógica de persistência de dados e manipulação do DOM reside).
  * **Boas Práticas:**
      * Uso de classes e IDs significativos.
      * Separação de responsabilidades (HTML para estrutura, CSS para estilo, JS para comportamento).
      * Uso de variáveis CSS (`:root`) em `style.css` para cores e transições.
      * Funções em `script.js` (ex: `validateInput`, `showNotification`, funções CRUD de `localStorage`) para promover a reutilização e modularidade.
  * **Versionamento:** O histórico de commits segue um padrão claro, indicando o tipo de alteração (ex: `feat:`, `fix:`, `style:`, `docs:`) para facilitar a rastreabilidade e a compreensão da evolução do projeto.

\<hr\>

## 📅 Prazo e Detalhes da Entrega

| Detalhe | Valor |
| :--- | :--- |
| **Início do Desenvolvimento** | 30/07/2025 |
| **Prazo Final (Vencimento)** | **26/11/2025** |
| **Formato de Entrega** | Link do repositório público no GitHub (encaminhado via PDF) |

\<hr\>

## 🧑‍💻 Autor(es)

| Arthur Moreira | Matrícula (Opcional) | Contato (moreirasantosarthur06@gmail.com) |
| :--- | :--- | :--- |
| Douglas do Carmo| [Sua Matrícula] | [Seu Email / Link do LinkedIn] |
| Gleyson Ferreira | [Matrícula do Colega 1] | [Email do Colega 1] |
| Jamil Cherem | [Matrícula do Colega 2] | [Email do Colega 2] |
