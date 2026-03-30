# Sistema de Monitoramento Participativo de Saneamento Básico
## Uma Intervenção de Ciência Cidadã para o Alcance da ODS 6 no Bairro Tapera, Florianópolis/SC

> **Repositório:** Plataforma web de denúncia, visualização geoespacial e debate comunitário sobre infraestrutura hídrica e sanitária.
> **Domínio:** Tecnologia Cívica · Engenharia de Software Sustentável · Saúde Ambiental Urbana
> **Versão:** 1.0.0 · **Licença:** MIT

---

## Sumário

1. [Contextualização e Problema de Pesquisa](#1-contextualização-e-problema-de-pesquisa)
2. [Referencial Teórico](#2-referencial-teórico)
3. [Objetivos](#3-objetivos)
4. [Alinhamento com a Agenda 2030 — ODS 6](#4-alinhamento-com-a-agenda-2030--ods-6)
5. [Arquitetura do Sistema e Fluxo de Dados](#5-arquitetura-do-sistema-e-fluxo-de-dados)
6. [Metodologia de Desenvolvimento Sustentável](#6-metodologia-de-desenvolvimento-sustentável)
7. [Teoria da Mudança e Indicadores de Impacto](#7-teoria-da-mudança-e-indicadores-de-impacto)
8. [Instalação e Uso](#8-instalação-e-uso)
9. [Limitações e Trabalhos Futuros](#9-limitações-e-trabalhos-futuros)
10. [Referências](#10-referências)

---

## 1. Contextualização e Problema de Pesquisa

O acesso universal a água potável e saneamento adequado constitui um direito humano fundamental, reconhecido pela Resolução A/RES/64/292 da Assembleia Geral das Nações Unidas (ONU, 2010). No Brasil, a Lei nº 14.026/2020 — Marco Legal do Saneamento Básico — estabeleceu metas ambiciosas de universalização até 2033, com atenção especial à coleta e tratamento de esgoto. Contudo, a distância entre o arcabouço normativo e a realidade vivenciada em comunidades periféricas persiste como um desafio empírico de alta complexidade.

O **bairro da Tapera**, localizado na porção sul da Ilha de Santa Catarina (Florianópolis/SC), exemplifica essa dicotomia. Inserido em zona de relevante interesse ecológico — adjacente à Lagoa do Peri e à APA do Entorno Costeiro —, o bairro concentra assentamentos com histórico de deficiências no sistema de esgotamento sanitário, evidenciado por ocorrências recorrentes de descarte irregular de efluentes em corpos hídricos e pelo comprometimento da balneabilidade em áreas litorâneas próximas (FATMA, 2022).

A **assimetria de informação** entre cidadãos, concessionária de serviços (CASAN) e poder público municipal representa o principal obstáculo à resolução tempestiva dessas falhas. A ausência de mecanismos padronizados de reporte, aliada à baixa capilaridade dos canais institucionais de denúncia, resulta em Lead Times elevados entre a identificação de um problema e a intervenção técnica correspondente.

Este projeto propõe, portanto, uma **intervenção sociotécnica** fundamentada nos princípios da Ciência Cidadã (*Citizen Science*) e da Tecnologia Cívica (*Civic Tech*), com vistas a preencher a lacuna de monitoramento ambiental participativo nesse território.

---

## 2. Referencial Teórico

### 2.1 Ciência Cidadã e Monitoramento Ambiental Participativo

A Ciência Cidadã é definida por Haklay (2013) como a participação do público em geral em atividades científicas, incluindo a coleta, categorização e análise de dados. No contexto do monitoramento ambiental urbano, essa abordagem tem demonstrado potencial para suprir deficiências de cobertura espacial e temporal dos sistemas de monitoramento convencionais (BONNEY et al., 2014).

O conceito de **Monitoramento Ambiental Participativo (MAP)** — framework adotado neste projeto — articula a produção de dados por não especialistas com protocolos de validação e encaminhamento institucional, conferindo legitimidade científica e operacional às informações geradas (IRWIN, 1995).

### 2.2 Tecnologia Cívica e Governança Urbana

A literatura sobre *GovTech* e Tecnologia Cívica evidencia que plataformas digitais de baixo custo podem catalisar a participação social na gestão de serviços públicos (LINDERS, 2012). Autores como O'Reilly (2011) argumentam que o "governo como plataforma" (*Government as a Platform*) pressupõe a abertura de dados e canais para que a sociedade civil contribua ativamente na identificação e solução de problemas urbanos.

### 2.3 Eco-Design de Software

O paradigma de **Engenharia de Software Verde** (*Green Software Engineering*) propõe a adoção de práticas de desenvolvimento que minimizem o consumo energético e a pegada de carbono digital (DICK et al., 2010). Princípios como eficiência computacional, minimização de requisições de rede e arquitetura *serverless* são operacionalizados neste projeto como critérios de design.

---

## 3. Objetivos

### Objetivo Geral

Desenvolver e disponibilizar uma plataforma web de código aberto para o monitoramento participativo de infraestrutura hídrica e sanitária no bairro Tapera, Florianópolis/SC, promovendo o engajamento cívico e o fortalecimento da governança local no âmbito da ODS 6.

### Objetivos Específicos

- **OE1** — Implementar um módulo de denúncias georreferenciadas para registro de ocorrências de esgoto a céu aberto, vazamentos e contaminação de mananciais;
- **OE2** — Disponibilizar um mapa interativo com visualização em tempo real das ocorrências reportadas e da cobertura da rede de saneamento;
- **OE3** — Prover um fórum comunitário moderado para disseminação de conhecimento técnico sobre reuso de água, fossas sépticas e práticas de conservação hídrica;
- **OE4** — Construir uma interface acessível, responsiva e de baixo consumo computacional, adequada a dispositivos de entrada de custo reduzido.

---

## 4. Alinhamento com a Agenda 2030 — ODS 6

O Objetivo de Desenvolvimento Sustentável 6 — *"Assegurar a disponibilidade e gestão sustentável da água e saneamento para todas e todos"* (ONU, 2015) — é operacionalizado neste sistema por meio do mapeamento direto entre funcionalidades técnicas e metas globais, conforme exposto no Quadro 1.

**Quadro 1 — Matriz de Alinhamento entre Funcionalidades e Metas da ODS 6**

| Meta ODS 6 | Descrição da Meta | Funcionalidade Correspondente | Indicador de Impacto Esperado |
|:---:|---|---|---|
| **6.1** | Acesso universal e equitativo à água potável segura e acessível | Mapa Interativo de Cobertura | Identificação de domicílios em áreas sem cobertura de rede tratada |
| **6.3** | Melhoria da qualidade da água, redução da poluição e dos descartes irregulares | Módulo de Denúncias Georreferenciadas | Redução do Lead Time entre identificação e reparo de vazamentos de esgoto |
| **6.b** | Apoio e fortalecimento da participação das comunidades locais na gestão da água e do saneamento | Fórum Comunitário | Aumento do Índice de Engajamento Cívico (IEC) mensurado pelo volume de interações qualificadas |

*Fonte: Elaboração própria com base em ONU (2015) e IPEA (2021).*

---

## 5. Arquitetura do Sistema e Fluxo de Dados

O sistema adota uma arquitetura **client-side pura**, eliminando dependências de infraestrutura de servidor dedicado e maximizando a resiliência operacional. O fluxo de dados segue o modelo Input–Processamento–Output (IPO), conforme descrito abaixo.

```
┌─────────────────────────────────────────────────────────────────────┐
│                        FLUXO DE DADOS — IPO                         │
│                                                                     │
│  ┌─────────────┐     ┌──────────────────────┐     ┌─────────────┐  │
│  │    INPUT     │     │    PROCESSAMENTO      │     │   OUTPUT    │  │
│  │─────────────│     │──────────────────────│     │─────────────│  │
│  │ • Formulário │────▶│ • Validação de dados  │────▶│ • Mapa      │  │
│  │   de denúncia│     │ • Georreferenciamento │     │   interativo│  │
│  │             │     │ • Serialização JSON   │     │             │  │
│  │ • Postagens  │────▶│ • Armazenamento em    │────▶│ • Feed do   │  │
│  │   no fórum   │     │   localStorage        │     │   fórum     │  │
│  │             │     │                       │     │             │  │
│  │ • Consultas  │────▶│ • Filtragem e         │────▶│ • Relatório │  │
│  │   ao mapa   │     │   renderização        │     │   de cobertura│ │
│  └─────────────┘     └──────────────────────┘     └─────────────┘  │
│                                                                     │
│  Persistência: Window.localStorage (client-side, sem servidor)      │
└─────────────────────────────────────────────────────────────────────┘
```

### 5.1 Componentes Principais

| Componente | Tecnologia | Responsabilidade |
|---|---|---|
| Interface de Usuário | HTML5 + Bootstrap 5.3 | Renderização responsiva e acessível |
| Lógica de Negócio | JavaScript (ES6+) | Validação, persistência e filtragem de dados |
| Visualização Geoespacial | Leaflet.js + OpenStreetMap | Mapa interativo de ocorrências |
| Persistência Local | Web Storage API (localStorage) | Armazenamento de denúncias e posts |

### 5.2 Modelo de Dados — Entidade Denúncia

```json
{
  "id": "uuid-v4",
  "timestamp": "ISO 8601",
  "tipo": "esgoto_ceu_aberto | vazamento | contaminacao | outro",
  "descricao": "string (max. 500 chars)",
  "coordenadas": {
    "lat": "float",
    "lng": "float"
  },
  "status": "aberta | em_analise | resolvida",
  "evidencias": "base64 (opcional)"
}
```

---

## 6. Metodologia de Desenvolvimento Sustentável

O desenvolvimento do sistema seguiu princípios estabelecidos pela **Green Software Foundation** (2023) e pelo framework de **Eco-Design de Software**, operacionalizados nas seguintes diretrizes:

### 6.1 Eficiência Energética e Computacional

A adoção de arquitetura *client-side* pura elimina a necessidade de servidores de aplicação em operação contínua, reduzindo a pegada de carbono digital associada ao processamento e transmissão de dados. Segundo estimativas do *Shift Project* (2021), aplicações web com backend intensivo podem consumir até 10× mais energia por transação do que equivalentes estáticos.

### 6.2 Acessibilidade e Inclusão Digital

O uso do framework Bootstrap 5 assegura conformidade com as diretrizes **WCAG 2.1 (Nível AA)** do W3C, garantindo acessibilidade a usuários com deficiências visuais e motoras. A natureza responsiva da interface permite uso pleno em dispositivos móveis de entrada, democratizando o acesso ao monitoramento ambiental em um público que depende predominantemente de *smartphones* para acesso à internet (CETIC.br, 2023).

### 6.3 Código Aberto e Soberania Tecnológica

A publicação sob licença MIT e o uso exclusivo de bibliotecas de código aberto (Leaflet, Bootstrap, OpenStreetMap) garantem que a solução possa ser replicada, adaptada e mantida por comunidades técnicas locais sem dependência de fornecedores proprietários, em consonância com os princípios de soberania tecnológica (MANSELL; TREMBLAY, 2013).

---

## 7. Teoria da Mudança e Indicadores de Impacto

A **Teoria da Mudança** (*Theory of Change*) do projeto postula que o aumento da visibilidade das falhas de infraestrutura sanitária — mediado por dados produzidos pelos próprios moradores — é condição necessária (ainda que não suficiente) para induzir respostas institucionais mais ágeis e eficazes.

**Figura 1 — Cadeia Lógica de Impacto**

```
INSUMOS           ATIVIDADES          PRODUTOS           RESULTADOS         IMPACTO
─────────         ──────────          ────────           ──────────         ───────
Plataforma   →   Denúncias       →   Base de        →   Redução do    →   Melhoria da
web              georrefe-           dados de           Lead Time de       balneabilidade
                 renciadas           ocorrências        atendimento        e saúde pública

Comunidade   →   Postagens       →   Acervo de      →   Aumento do    →   Empoderamento
engajada         no fórum            conhecimento       Letramento         cívico e
                                     comunitário        Ambiental          resiliência
                                                                           urbana
```

### 7.1 Indicadores de Monitoramento e Avaliação (M&A)

| Indicador | Definição Operacional | Meta (12 meses) | Método de Coleta |
|---|---|---|---|
| **Lead Time de Reparo (LTR)** | Tempo médio (dias) entre o registro de denúncia e a resolução reportada | Redução de 30% vs. linha de base | Análise de logs da plataforma |
| **Taxa de Engajamento Cívico (TEC)** | Número de usuários ativos únicos / mês | ≥ 150 usuários | Analytics de sessão |
| **Índice de Letramento Ambiental (ILA)** | Posts no fórum com conteúdo técnico validado / total de posts | ≥ 40% | Análise de conteúdo (coding qualitativo) |
| **Cobertura Geoespacial (CG)** | % do território do bairro com ≥ 1 denúncia registrada | ≥ 70% da área habitada | Análise do shapefile de ocorrências |

---

## 8. Instalação e Uso

Por tratar-se de uma aplicação *client-side*, não há dependências de backend ou banco de dados. A instalação resume-se a:

```bash
# Clone o repositório
git clone https://github.com/<usuario>/tapera-saneamento.git

# Acesse o diretório
cd tapera-saneamento

# Abra o arquivo principal em qualquer navegador moderno
# (Chrome 90+, Firefox 88+, Safari 14+, Edge 90+)
open index.html
```

> **Nota:** Para habilitar a geolocalização automática no módulo de denúncias, o arquivo deve ser servido via protocolo `https://` ou `http://localhost`. Navegadores modernos bloqueiam a API de Geolocalização em contextos inseguros.

Para servir localmente com Python:

```bash
python3 -m http.server 8080
# Acesse: http://localhost:8080
```

---

## 9. Limitações e Trabalhos Futuros

### 9.1 Limitações Identificadas

- **Persistência Local:** O uso de `localStorage` restringe o compartilhamento de dados entre dispositivos e impede a consolidação de um repositório coletivo de ocorrências em escala. Isso constitui uma limitação fundamental para estudos epidemiológicos e de análise espacial em larga escala.
- **Ausência de Autenticação:** A falta de mecanismo de autenticação de usuários impede a responsabilização por denúncias fraudulentas e dificulta o acompanhamento longitudinal por denunciante.
- **Validação Técnica das Denúncias:** O sistema não implementa, na versão atual, protocolos de validação cruzada das ocorrências por pares ou por técnicos habilitados, o que pode comprometer a confiabilidade dos dados para fins de advocacy institucional.

### 9.2 Agenda de Pesquisa e Desenvolvimento

- **Integração com API da CASAN:** Desenvolvimento de endpoint de integração para encaminhamento automatizado de ordens de serviço à concessionária;
- **Backend com Supabase ou Firebase:** Migração da persistência para banco de dados distribuído, habilitando análise agregada de ocorrências;
- **Módulo de Análise Preditiva:** Aplicação de algoritmos de agrupamento espacial (DBSCAN) para identificação de hotspots de infraestrutura deficiente;
- **Estudo de Caso Longitudinal:** Condução de pesquisa-ação participativa com moradores da Tapera para avaliação de eficácia e refinamento iterativo do sistema.

---

## 10. Referências

BONNEY, R. et al. **Citizen science: A developing tool for expanding science knowledge and scientific literacy**. *BioScience*, v. 59, n. 11, p. 977–984, 2009. DOI: 10.1525/bio.2009.59.11.7.

BRASIL. **Lei nº 14.026, de 15 de julho de 2020** — Marco Legal do Saneamento Básico. Brasília: Presidência da República, 2020.

CETIC.br — Centro Regional de Estudos para o Desenvolvimento da Sociedade da Informação. **Pesquisa sobre o Uso das Tecnologias de Informação e Comunicação nos Domicílios Brasileiros — TIC Domicílios 2023**. São Paulo: Comitê Gestor da Internet no Brasil, 2023.

DICK, M. et al. **Green software engineering**. In: NAUMANN, S.; DICK, M. (Eds.). *Proceedings of the 1st International Workshop on Green and Sustainable Software*. ACM, 2010.

FATMA — Fundação do Meio Ambiente de Santa Catarina. **Monitoramento da Balneabilidade das Praias Catarinenses — Relatório Técnico 2022**. Florianópolis: FATMA, 2022.

GREEN SOFTWARE FOUNDATION. **Software Carbon Intensity (SCI) Specification**. Versão 1.0. Seattle: GSF, 2023. Disponível em: https://greensoftware.foundation. Acesso em: 2024.

HAKLAY, M. **Citizen science and volunteered geographic information: Overview and typology of participation**. *Crowdsourcing Geographic Knowledge*, p. 105–122, 2013.

IPEA — Instituto de Pesquisa Econômica Aplicada. **ODS 6 — Água Potável e Saneamento: Relatório Brasileiro de Monitoramento dos ODS**. Brasília: IPEA, 2021.

IRWIN, A. **Citizen Science: A Study of People, Expertise and Sustainable Development**. London: Routledge, 1995.

LINDERS, D. **From e-government to we-government: Defining a typology for citizen coproduction in the age of social media**. *Government Information Quarterly*, v. 29, n. 4, p. 446–454, 2012.

MANSELL, R.; TREMBLAY, G. **Renewing the Knowledge Societies Vision: Towards Knowledge Societies for Peace and Sustainable Development**. Paris: UNESCO, 2013.

ONU — Organização das Nações Unidas. **Resolução A/RES/64/292: O Direito Humano à Água e ao Saneamento**. Nova York: Assembleia Geral da ONU, 2010.

ONU — Organização das Nações Unidas. **Transformando Nosso Mundo: A Agenda 2030 para o Desenvolvimento Sustentável**. Nova York: ONU, 2015.

O'REILLY, T. **Government as a platform**. *Innovations: Technology, Governance, Globalization*, v. 6, n. 1, p. 13–40, 2011.

SNIS — Sistema Nacional de Informações sobre Saneamento. **Diagnóstico dos Serviços de Água e Esgotos — 2022**. Brasília: Ministério das Cidades, 2023.

THE SHIFT PROJECT. **Lean ICT: Towards Digital Sobriety**. Paris: The Shift Project, 2021.

---

<div align="center">

**"A tecnologia serve como ponte entre o dado bruto e a cidadania ativa,
elemento essencial para a resiliência urbana em comunidades vulnerárias."**

*Desenvolvido como contribuição à Agenda 2030 e ao Marco Legal do Saneamento Básico (Lei nº 14.026/2020)*

[![ODS 6](https://img.shields.io/badge/ODS-6%20Água%20Potável%20e%20Saneamento-009FDC)](https://odsbrasil.gov.br/objetivo/objetivo?n=6)
[![Licença MIT](https://img.shields.io/badge/Licença-MIT-green)](LICENSE)
[![Ciência Cidadã](https://img.shields.io/badge/Abordagem-Ciência%20Cidadã-brightgreen)](https://ecsa.citizen-science.net)

</div>
