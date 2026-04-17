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

// ════════════════════════════════════════
//  PERFIS DE USUÁRIO
// ════════════════════════════════════════

const DB_PERFIS = path.join(__dirname, 'perfis.json');

// GET /api/perfis
app.get('/api/perfis', (req, res) => {
  const lista = lerJSON(DB_PERFIS);
  lista.sort((a, b) => new Date(b.criadoEm) - new Date(a.criadoEm));
  res.json({ total: lista.length, perfis: lista });
});

// GET /api/perfis/:id
app.get('/api/perfis/:id', (req, res) => {
  const item = lerJSON(DB_PERFIS).find(p => p.id === req.params.id);
  if (!item) return res.status(404).json({ erro: 'Perfil não encontrado.' });
  res.json(item);
});

// POST /api/perfis  —  body: { nome, email, telefone?, bairro? }
app.post('/api/perfis', (req, res) => {
  const { nome, email, telefone, bairro } = req.body;
  if (!nome || !email)
    return res.status(400).json({ erro: 'Campos obrigatórios: nome, email.' });

  const lista = lerJSON(DB_PERFIS);

  // E-mail deve ser único
  if (lista.find(p => p.email === email))
    return res.status(409).json({ erro: 'Já existe um perfil com este e-mail.' });

  const novo = {
    id: uuidv4(),
    nome, email,
    telefone: telefone || null,
    bairro: bairro || null,
    criadoEm: new Date().toISOString(),
    atualizadoEm: new Date().toISOString()
  };
  lista.push(novo);
  salvarJSON(DB_PERFIS, lista);
  res.status(201).json({ mensagem: 'Perfil cadastrado com sucesso!', perfil: novo });
});

// PUT /api/perfis/:id  —  body: { nome, email, telefone?, bairro? }
app.put('/api/perfis/:id', (req, res) => {
  const { nome, email, telefone, bairro } = req.body;
  if (!nome || !email)
    return res.status(400).json({ erro: 'Campos obrigatórios: nome, email.' });

  const lista = lerJSON(DB_PERFIS);
  const idx = lista.findIndex(p => p.id === req.params.id);
  if (idx === -1) return res.status(404).json({ erro: 'Perfil não encontrado.' });

  // Verifica duplicidade de e-mail (ignora o próprio perfil)
  const duplicado = lista.find(p => p.email === email && p.id !== req.params.id);
  if (duplicado)
    return res.status(409).json({ erro: 'Já existe outro perfil com este e-mail.' });

  lista[idx] = {
    ...lista[idx],
    nome, email,
    telefone: telefone || null,
    bairro: bairro || null,
    atualizadoEm: new Date().toISOString()
  };
  salvarJSON(DB_PERFIS, lista);
  res.json({ mensagem: 'Perfil atualizado com sucesso!', perfil: lista[idx] });
});

// DELETE /api/perfis/:id
app.delete('/api/perfis/:id', (req, res) => {
  let lista = lerJSON(DB_PERFIS);
  const antes = lista.length;
  lista = lista.filter(p => p.id !== req.params.id);
  if (lista.length === antes) return res.status(404).json({ erro: 'Perfil não encontrado.' });
  salvarJSON(DB_PERFIS, lista);
  res.json({ mensagem: 'Perfil excluído com sucesso.' });
});

// ════════════════════════════════════════
//  AUTENTICAÇÃO (Cadastro / Login)
// ════════════════════════════════════════

const bcrypt = require('bcrypt');
const jwt    = require('jsonwebtoken');

const DB_USUARIOS  = path.join(__dirname, 'usuarios.json');
const JWT_SECRET   = process.env.JWT_SECRET || 'tapera-secret-local-2026';
const SALT_ROUNDS  = 10;

/**
 * POST /api/auth/cadastro
 * Cria uma conta nova.
 * Body: { nome, email, senha, confirmarSenha, aceitaTermos? }
 */
app.post('/api/auth/cadastro', async (req, res) => {
  const { nome, email, senha, confirmarSenha, aceitaTermos } = req.body;

  if (!nome || !email || !senha || !confirmarSenha)
    return res.status(400).json({ erro: 'Campos obrigatórios: nome, email, senha, confirmarSenha.' });

  if (senha !== confirmarSenha)
    return res.status(400).json({ erro: 'As senhas não coincidem.' });

  if (senha.length < 6)
    return res.status(400).json({ erro: 'A senha deve ter pelo menos 6 caracteres.' });

  const usuarios = lerJSON(DB_USUARIOS);

  if (usuarios.find(u => u.email === email))
    return res.status(409).json({ erro: 'Já existe uma conta com este e-mail.' });

  const senhaHash = await bcrypt.hash(senha, SALT_ROUNDS);

  const novoUsuario = {
    id: uuidv4(),
    nome,
    email,
    senhaHash,
    aceitaTermos: aceitaTermos || false,
    criadoEm: new Date().toISOString()
  };

  usuarios.push(novoUsuario);
  salvarJSON(DB_USUARIOS, usuarios);

  // Gera token JWT já no cadastro (login automático)
  const token = jwt.sign({ id: novoUsuario.id, email: novoUsuario.email }, JWT_SECRET, { expiresIn: '7d' });

  res.status(201).json({
    mensagem: 'Conta criada com sucesso!',
    token,
    usuario: { id: novoUsuario.id, nome: novoUsuario.nome, email: novoUsuario.email }
  });
});

/**
 * POST /api/auth/login
 * Autentica um usuário existente.
 * Body: { email, senha }
 */
app.post('/api/auth/login', async (req, res) => {
  const { email, senha } = req.body;

  if (!email || !senha)
    return res.status(400).json({ erro: 'Campos obrigatórios: email, senha.' });

  const usuarios = lerJSON(DB_USUARIOS);
  const usuario  = usuarios.find(u => u.email === email);

  // Mensagem genérica para não revelar se o e-mail existe
  if (!usuario)
    return res.status(401).json({ erro: 'E-mail ou senha incorretos.' });

  const senhaCorreta = await bcrypt.compare(senha, usuario.senhaHash);
  if (!senhaCorreta)
    return res.status(401).json({ erro: 'E-mail ou senha incorretos.' });

  const token = jwt.sign({ id: usuario.id, email: usuario.email }, JWT_SECRET, { expiresIn: '7d' });

  res.json({
    mensagem: 'Login realizado com sucesso!',
    token,
    usuario: { id: usuario.id, nome: usuario.nome, email: usuario.email }
  });
});

/**
 * GET /api/auth/me
 * Retorna os dados do usuário autenticado.
 * Header: Authorization: Bearer <token>
 */
app.get('/api/auth/me', (req, res) => {
  const auth = req.headers.authorization;
  if (!auth || !auth.startsWith('Bearer '))
    return res.status(401).json({ erro: 'Token não fornecido.' });

  try {
    const payload  = jwt.verify(auth.split(' ')[1], JWT_SECRET);
    const usuarios = lerJSON(DB_USUARIOS);
    const usuario  = usuarios.find(u => u.id === payload.id);
    if (!usuario) return res.status(404).json({ erro: 'Usuário não encontrado.' });
    res.json({ id: usuario.id, nome: usuario.nome, email: usuario.email, criadoEm: usuario.criadoEm });
  } catch {
    res.status(401).json({ erro: 'Token inválido ou expirado.' });
  }
});

// ════════════════════════════════════════
//  CONTATO / MENSAGENS
// ════════════════════════════════════════

const DB_MENSAGENS = path.join(__dirname, 'mensagens.json');

const ASSUNTOS_VALIDOS = [
  'Dúvida sobre denúncia',
  'Problema técnico no site',
  'Parceria/Projeto',
  'Outro'
];

/**
 * POST /api/contato
 * Salva uma mensagem de contato.
 * Body: { assunto, mensagem, nome?, email? }
 */
app.post('/api/contato', (req, res) => {
  const { assunto, mensagem, nome, email } = req.body;

  if (!assunto || !mensagem)
    return res.status(400).json({ erro: 'Campos obrigatórios: assunto, mensagem.' });

  if (!ASSUNTOS_VALIDOS.includes(assunto))
    return res.status(400).json({ erro: `Assunto inválido. Use: ${ASSUNTOS_VALIDOS.join(', ')}` });

  if (mensagem.trim().length < 10)
    return res.status(400).json({ erro: 'A mensagem deve ter pelo menos 10 caracteres.' });

  const nova = {
    id: uuidv4(),
    assunto,
    mensagem: mensagem.trim(),
    nome:  nome  || null,
    email: email || null,
    lida: false,
    criadoEm: new Date().toISOString()
  };

  const lista = lerJSON(DB_MENSAGENS);
  lista.push(nova);
  salvarJSON(DB_MENSAGENS, lista);

  res.status(201).json({ mensagem: 'Mensagem enviada com sucesso!', id: nova.id });
});

/**
 * GET /api/contato
 * Lista todas as mensagens recebidas (para painel admin).
 * Query params opcionais: lida=true|false
 */
app.get('/api/contato', (req, res) => {
  let lista = lerJSON(DB_MENSAGENS);
  lista.sort((a, b) => new Date(b.criadoEm) - new Date(a.criadoEm));

  if (req.query.lida !== undefined) {
    const filtro = req.query.lida === 'true';
    lista = lista.filter(m => m.lida === filtro);
  }

  res.json({ total: lista.length, mensagens: lista });
});

/**
 * PATCH /api/contato/:id/lida
 * Marca uma mensagem como lida.
 */
app.patch('/api/contato/:id/lida', (req, res) => {
  const lista = lerJSON(DB_MENSAGENS);
  const idx = lista.findIndex(m => m.id === req.params.id);
  if (idx === -1) return res.status(404).json({ erro: 'Mensagem não encontrada.' });

  lista[idx].lida = true;
  salvarJSON(DB_MENSAGENS, lista);
  res.json({ mensagem: 'Mensagem marcada como lida.', item: lista[idx] });
});

/**
 * DELETE /api/contato/:id
 * Remove uma mensagem.
 */
app.delete('/api/contato/:id', (req, res) => {
  let lista = lerJSON(DB_MENSAGENS);
  const antes = lista.length;
  lista = lista.filter(m => m.id !== req.params.id);
  if (lista.length === antes) return res.status(404).json({ erro: 'Mensagem não encontrada.' });
  salvarJSON(DB_MENSAGENS, lista);
  res.json({ mensagem: 'Mensagem excluída com sucesso.' });
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
  console.log(`  DELETE /api/topicos/:id`);
  console.log(`\n── Perfis ─────────────────────────────────`);
  console.log(`  GET    /api/perfis`);
  console.log(`  GET    /api/perfis/:id`);
  console.log(`  POST   /api/perfis`);
  console.log(`  PUT    /api/perfis/:id`);
  console.log(`  DELETE /api/perfis/:id`);
  console.log(`\n── Autenticação ───────────────────────────`);
  console.log(`  POST   /api/auth/cadastro`);
  console.log(`  POST   /api/auth/login`);
  console.log(`  GET    /api/auth/me`);
  console.log(`\n── Contato ────────────────────────────────`);
  console.log(`  POST   /api/contato`);
  console.log(`  GET    /api/contato`);
  console.log(`  PATCH  /api/contato/:id/lida`);
  console.log(`  DELETE /api/contato/:id\n`);
});
