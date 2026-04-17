const express = require('express');
const cors = require('cors');
const { v4: uuidv4 } = require('uuid');
const fs = require('fs');
const path = require('path');

const app = express();
const PORT = 3000;

const DB_DENUNCIAS = path.join(__dirname, 'denuncias.json');
const DB_TOPICOS   = path.join(__dirname, 'topicos.json');

app.use(cors());
app.use(express.json());

function lerJSON(arquivo) {
  try {
    if (!fs.existsSync(arquivo)) return [];
    return JSON.parse(fs.readFileSync(arquivo, 'utf-8'));
  } catch { return []; }
}

function salvarJSON(arquivo, dados) {
  fs.writeFileSync(arquivo, JSON.stringify(dados, null, 2), 'utf-8');
}

// ════════════════════════════════════════
//  DENÚNCIAS
// ════════════════════════════════════════

app.get('/api/denuncias', (req, res) => {
  let lista = lerJSON(DB_DENUNCIAS);
  const { tipo, status, limit } = req.query;
  if (tipo)   lista = lista.filter(d => d.tipo === tipo);
  if (status) lista = lista.filter(d => d.status === status);
  lista.sort((a, b) => new Date(b.criadoEm) - new Date(a.criadoEm));
  if (limit)  lista = lista.slice(0, parseInt(limit));
  res.json({ total: lista.length, denuncias: lista });
});

app.get('/api/denuncias/:id', (req, res) => {
  const item = lerJSON(DB_DENUNCIAS).find(d => d.id === req.params.id);
  if (!item) return res.status(404).json({ erro: 'Denúncia não encontrada.' });
  res.json(item);
});

app.post('/api/denuncias', (req, res) => {
  const { titulo, tipo, descricao, lat, lng, localizacao, referencia, fotoUrl } = req.body;
  if (!titulo || !tipo || !descricao || lat == null || lng == null || !localizacao)
    return res.status(400).json({ erro: 'Campos obrigatórios: titulo, tipo, descricao, lat, lng, localizacao.' });
  const nova = {
    id: uuidv4(), titulo, tipo, descricao, localizacao,
    referencia: referencia || null, fotoUrl: fotoUrl || null,
    coordenadas: { lat: parseFloat(lat), lng: parseFloat(lng) },
    status: 'aberta',
    criadoEm: new Date().toISOString(), atualizadoEm: new Date().toISOString()
  };
  const lista = lerJSON(DB_DENUNCIAS);
  lista.push(nova);
  salvarJSON(DB_DENUNCIAS, lista);
  res.status(201).json({ mensagem: 'Denúncia registrada com sucesso!', denuncia: nova });
});

app.patch('/api/denuncias/:id/status', (req, res) => {
  const validos = ['aberta', 'em_analise', 'resolvida'];
  const { status } = req.body;
  if (!status || !validos.includes(status))
    return res.status(400).json({ erro: `Status inválido. Use: ${validos.join(', ')}` });
  const lista = lerJSON(DB_DENUNCIAS);
  const idx = lista.findIndex(d => d.id === req.params.id);
  if (idx === -1) return res.status(404).json({ erro: 'Denúncia não encontrada.' });
  lista[idx].status = status;
  lista[idx].atualizadoEm = new Date().toISOString();
  salvarJSON(DB_DENUNCIAS, lista);
  res.json({ mensagem: 'Status atualizado!', denuncia: lista[idx] });
});

app.delete('/api/denuncias/:id', (req, res) => {
  let lista = lerJSON(DB_DENUNCIAS);
  const antes = lista.length;
  lista = lista.filter(d => d.id !== req.params.id);
  if (lista.length === antes) return res.status(404).json({ erro: 'Denúncia não encontrada.' });
  salvarJSON(DB_DENUNCIAS, lista);
  res.json({ mensagem: 'Denúncia excluída com sucesso.' });
});

app.get('/api/stats', (req, res) => {
  const lista = lerJSON(DB_DENUNCIAS);
  const porStatus = lista.reduce((a, d) => { a[d.status] = (a[d.status] || 0) + 1; return a; }, {});
  const porTipo   = lista.reduce((a, d) => { a[d.tipo]   = (a[d.tipo]   || 0) + 1; return a; }, {});
  res.json({ total: lista.length, porStatus, porTipo });
});

// ════════════════════════════════════════
//  FÓRUM — TÓPICOS
// ════════════════════════════════════════

// GET /api/topicos?limit=N
app.get('/api/topicos', (req, res) => {
  let lista = lerJSON(DB_TOPICOS);
  lista.sort((a, b) => new Date(b.criadoEm) - new Date(a.criadoEm));
  const { limit } = req.query;
  if (limit) lista = lista.slice(0, parseInt(limit));
  res.json({ total: lista.length, topicos: lista });
});

// GET /api/topicos/:id
app.get('/api/topicos/:id', (req, res) => {
  const item = lerJSON(DB_TOPICOS).find(t => t.id === req.params.id);
  if (!item) return res.status(404).json({ erro: 'Tópico não encontrado.' });
  res.json(item);
});

// POST /api/topicos  —  body: { titulo, conteudo, autorId }
app.post('/api/topicos', (req, res) => {
  const { titulo, conteudo, autorId } = req.body;
  if (!titulo || !conteudo || !autorId)
    return res.status(400).json({ erro: 'Campos obrigatórios: titulo, conteudo, autorId.' });
  const novo = {
    id: uuidv4(), titulo, conteudo, autorId,
    usuariosLike: [], usuariosDeslike: [],
    criadoEm: new Date().toISOString()
  };
  const lista = lerJSON(DB_TOPICOS);
  lista.push(novo);
  salvarJSON(DB_TOPICOS, lista);
  res.status(201).json({ mensagem: 'Tópico publicado com sucesso!', topico: novo });
});

// POST /api/topicos/:id/like  —  body: { usuarioId }
app.post('/api/topicos/:id/like', (req, res) => {
  const { usuarioId } = req.body;
  if (!usuarioId) return res.status(400).json({ erro: 'Campo obrigatório: usuarioId.' });
  const lista = lerJSON(DB_TOPICOS);
  const idx = lista.findIndex(t => t.id === req.params.id);
  if (idx === -1) return res.status(404).json({ erro: 'Tópico não encontrado.' });
  const t = lista[idx];
  t.usuariosLike    = t.usuariosLike    || [];
  t.usuariosDeslike = t.usuariosDeslike || [];
  if (t.usuariosLike.includes(usuarioId)) {
    t.usuariosLike = t.usuariosLike.filter(u => u !== usuarioId);
  } else {
    t.usuariosLike.push(usuarioId);
    t.usuariosDeslike = t.usuariosDeslike.filter(u => u !== usuarioId);
  }
  salvarJSON(DB_TOPICOS, lista);
  res.json({ likes: t.usuariosLike.length, deslikes: t.usuariosDeslike.length,
             euCurti: t.usuariosLike.includes(usuarioId) });
});

// POST /api/topicos/:id/deslike  —  body: { usuarioId }
app.post('/api/topicos/:id/deslike', (req, res) => {
  const { usuarioId } = req.body;
  if (!usuarioId) return res.status(400).json({ erro: 'Campo obrigatório: usuarioId.' });
  const lista = lerJSON(DB_TOPICOS);
  const idx = lista.findIndex(t => t.id === req.params.id);
  if (idx === -1) return res.status(404).json({ erro: 'Tópico não encontrado.' });
  const t = lista[idx];
  t.usuariosLike    = t.usuariosLike    || [];
  t.usuariosDeslike = t.usuariosDeslike || [];
  if (t.usuariosDeslike.includes(usuarioId)) {
    t.usuariosDeslike = t.usuariosDeslike.filter(u => u !== usuarioId);
  } else {
    t.usuariosDeslike.push(usuarioId);
    t.usuariosLike = t.usuariosLike.filter(u => u !== usuarioId);
  }
  salvarJSON(DB_TOPICOS, lista);
  res.json({ likes: t.usuariosLike.length, deslikes: t.usuariosDeslike.length,
             euDescurti: t.usuariosDeslike.includes(usuarioId) });
});

// DELETE /api/topicos/:id
app.delete('/api/topicos/:id', (req, res) => {
  let lista = lerJSON(DB_TOPICOS);
  const antes = lista.length;
  lista = lista.filter(t => t.id !== req.params.id);
  if (lista.length === antes) return res.status(404).json({ erro: 'Tópico não encontrado.' });
  salvarJSON(DB_TOPICOS, lista);
  res.json({ mensagem: 'Tópico excluído com sucesso.' });
});

// ── Start ────────────────────────────────────────────────────
app.listen(PORT, () => {
  console.log(`\n🌊 Tapera API rodando em http://localhost:${PORT}`);
  console.log(`\n── Denúncias ──────────────────────────────`);
  console.log(`  GET    /api/denuncias`);
  console.log(`  GET    /api/denuncias/:id`);
  console.log(`  POST   /api/denuncias`);
  console.log(`  PATCH  /api/denuncias/:id/status`);
  console.log(`  DELETE /api/denuncias/:id`);
  console.log(`  GET    /api/stats`);
  console.log(`\n── Fórum ──────────────────────────────────`);
  console.log(`  GET    /api/topicos`);
  console.log(`  GET    /api/topicos/:id`);
  console.log(`  POST   /api/topicos`);
  console.log(`  POST   /api/topicos/:id/like`);
  console.log(`  POST   /api/topicos/:id/deslike`);
  console.log(`  DELETE /api/topicos/:id\n`);
});
