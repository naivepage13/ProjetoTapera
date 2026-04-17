# 🌊 Tapera API — Backend do Canal de Denúncias

API REST em Node.js + Express para o projeto **Sustentabilidade Tapera (ODS 6)**.

## ▶️ Como rodar

```bash
# 1. Instalar dependências
npm install

# 2. Iniciar o servidor
node server.js
# ou, com reload automático:
npx nodemon server.js
```

O servidor sobe em **http://localhost:3000**

---

## 📡 Endpoints

| Método   | Rota                          | Descrição                          |
|----------|-------------------------------|------------------------------------|
| `GET`    | `/api/denuncias`              | Lista todas as denúncias           |
| `GET`    | `/api/denuncias/:id`          | Busca uma denúncia pelo ID         |
| `POST`   | `/api/denuncias`              | Cria uma nova denúncia             |
| `PATCH`  | `/api/denuncias/:id/status`   | Atualiza o status da denúncia      |
| `DELETE` | `/api/denuncias/:id`          | Remove uma denúncia                |
| `GET`    | `/api/stats`                  | Estatísticas gerais                |

---

## 📥 POST /api/denuncias — Body esperado

```json
{
  "titulo":      "Vazamento na Rua das Acácias",
  "tipo":        "Vazamento de Água (Rede Pública)",
  "descricao":   "Água jorrando da calçada há dois dias.",
  "lat":         -27.6875,
  "lng":         -48.5600,
  "localizacao": "Rua das Acácias, Tapera",
  "referencia":  "Em frente ao mercado (opcional)"
}
```

---

## 🔄 PATCH /api/denuncias/:id/status — Body esperado

```json
{ "status": "em_analise" }
```

**Status válidos:** `aberta` | `em_analise` | `resolvida`

---

## 📊 GET /api/stats — Exemplo de resposta

```json
{
  "total": 5,
  "porStatus": { "aberta": 3, "em_analise": 1, "resolvida": 1 },
  "porTipo":   { "Esgoto a Céu Aberto": 2, "Falta de Água": 3 }
}
```

---

## 🔍 Filtros no GET /api/denuncias

```
/api/denuncias?tipo=Falta de Água
/api/denuncias?status=aberta
/api/denuncias?limit=5
```

---

## 📁 Estrutura de arquivos

```
tapera-api/
├── server.js        ← API principal
├── denuncias.json   ← "Banco de dados" (criado automaticamente)
├── denuncia.html    ← Frontend atualizado para usar a API
├── package.json
└── README.md
```

> **Nota:** `denuncias.json` é criado automaticamente na primeira denúncia enviada.
> Para um projeto em produção, substitua por PostgreSQL ou MongoDB.
