const NIVEIS = [
    { nome: "Iniciante", min: 0 },
    { nome: "Apoiador", min: 100 },
    { nome: "Doador", min: 300 },
    { nome: "Guardião da Vida", min: 600 },
    { nome: "Herói da Vida", min: 1000 },
];

const PAPEIS = {
    Doador: "Doador",
    Administrador: "Administrador",
    ResponsavelHemocentro: "Responsável pelo hemocentro",
};

const PERMISSOES = {
    registrarDoacao: "Registrar doações",
    consultarHistorico: "Consultar o histórico",
    personalizarMascote: "Personalizar o mascote",
    confirmarDoacao: "Confirmar doações",
    gerenciarUsuarios: "Gerenciar usuários",
    gerenciarCampanhas: "Gerenciar campanhas",
    gerenciarMissoes: "Gerenciar missões",
    atualizarLocal: "Atualizar o local de doação",
};

const GRUPOS_MISSOES = [
    { tipo: "DOACAO", titulo: "Doar" },
    { tipo: "EVENTO", titulo: "Mês do Doador" },
    { tipo: "APRENDIZADO", titulo: "Aprender" },
    { tipo: "ENGAJAMENTO", titulo: "Explorar" },
];

const MISSOES_AUTOMATICAS = new Set([
    "Completar o cadastro", "Registrar uma doação", "Doação confirmada", "Três doações no ano",
    "Personalizar o mascote", "Doação do mês do doador", "Dupla doação do mês",
]);

const DESTINO_MISSAO = {
    "Conhecer os requisitos": "#como-doar",
    "Conhecer o intervalo": "#como-doar",
    "Visitar um local": "#hemocentros",
    "Ver seu histórico": "#doacoes",
};

const CHAVE_USUARIO = "hemoplay.usuarioAtual";
const IMAGEM_PADRAO = "img/mascote/cor-vermelha.jpg";

const estado = {
    usuario: null,
    mascote: null,
    missoes: [],
    tipos: [],
    locais: [],
    notificacoesVistas: new Set(),
};

const $ = (seletor, raiz = document) => raiz.querySelector(seletor);
const $$ = (seletor, raiz = document) => [...raiz.querySelectorAll(seletor)];

function h(tag, propriedades = {}, ...filhos) {
    const elemento = document.createElement(tag);
    Object.entries(propriedades).forEach(([chave, valor]) => {
        if (valor === undefined || valor === null || valor === false) {
            return;
        }
        if (chave === "classe") {
            elemento.className = valor;
        } else if (chave.startsWith("on")) {
            elemento.addEventListener(chave.slice(2), valor);
        } else {
            elemento.setAttribute(chave, valor === true ? "" : valor);
        }
    });
    filhos.flat().forEach((filho) => {
        if (filho === null || filho === undefined || filho === false) {
            return;
        }
        elemento.append(filho instanceof Node ? filho : String(filho));
    });
    return elemento;
}

function icone(caminho) {
    const svg = document.createElementNS("http://www.w3.org/2000/svg", "svg");
    svg.setAttribute("viewBox", "0 0 24 24");
    svg.setAttribute("aria-hidden", "true");
    const path = document.createElementNS("http://www.w3.org/2000/svg", "path");
    path.setAttribute("d", caminho);
    svg.append(path);
    return svg;
}

const ICONE_CHECK = "M5 12.5l4.5 4.5L19 7.5";
const ICONE_CADEADO = "M7 11V8a5 5 0 0 1 10 0v3M6 11h12v9H6z";
const ICONE_MEDALHA = "M12 2l2.9 6 6.6.9-4.8 4.6 1.2 6.5L12 16.9 6.1 20l1.2-6.5L2.5 8.9 9.1 8z";

async function api(metodo, caminho, corpo) {
    const opcoes = { method: metodo, headers: {} };
    if (corpo !== undefined) {
        opcoes.headers["Content-Type"] = "application/json";
        opcoes.body = JSON.stringify(corpo);
    }

    let resposta;
    try {
        resposta = await fetch(caminho, opcoes);
    } catch (erro) {
        throw new Error("Não foi possível falar com o servidor. Confira se a aplicação está rodando.");
    }

    const texto = await resposta.text();
    let dados = null;
    try {
        dados = texto ? JSON.parse(texto) : null;
    } catch (erro) {
        dados = null;
    }

    if (!resposta.ok) {
        throw new Error(dados && dados.erro ? dados.erro : `O servidor recusou a operação (erro ${resposta.status}).`);
    }
    return dados;
}

function lerUsuarioSalvo() {
    try {
        const valor = localStorage.getItem(CHAVE_USUARIO);
        return valor ? Number(valor) : null;
    } catch (erro) {
        return null;
    }
}

function salvarUsuario(id) {
    try {
        if (id) {
            localStorage.setItem(CHAVE_USUARIO, String(id));
        } else {
            localStorage.removeItem(CHAVE_USUARIO);
        }
    } catch (erro) {
    }
}

function avisar(titulo, texto, tipo) {
    const aviso = h("div", { classe: `aviso ${tipo || ""}` }, h("strong", {}, titulo), texto ? h("span", {}, texto) : null);
    const avisos = $("#avisos");
    avisos.append(aviso);
    while (avisos.children.length > 3) {
        avisos.firstElementChild.remove();
    }
    setTimeout(() => aviso.remove(), 4500);
}

function mostrarErro(formulario, mensagem) {
    const caixa = $(".erro-form", formulario);
    caixa.textContent = mensagem || "";
    caixa.hidden = !mensagem;
}

function lerFormulario(formulario) {
    const dados = {};
    new FormData(formulario).forEach((valor, campo) => {
        const texto = String(valor).trim();
        if (texto === "") {
            dados[campo] = null;
        } else if (campo.startsWith("id")) {
            dados[campo] = Number(texto);
        } else {
            dados[campo] = texto;
        }
    });
    return dados;
}

function formularioValido(formulario) {
    let primeiroInvalido = null;
    $$("input, select", formulario).forEach((campo) => {
        const valido = campo.checkValidity();
        campo.setAttribute("aria-invalid", String(!valido));
        if (!valido && !primeiroInvalido) {
            primeiroInvalido = campo;
        }
    });
    if (primeiroInvalido) {
        const rotulo = $(`label[for="${primeiroInvalido.id}"]`, formulario);
        mostrarErro(formulario, `${rotulo ? rotulo.textContent : "Campo"}: ${primeiroInvalido.validationMessage}`);
        primeiroInvalido.focus();
        return false;
    }
    return true;
}

async function enviando(botao, tarefa) {
    botao.disabled = true;
    try {
        return await tarefa();
    } finally {
        botao.disabled = false;
    }
}

function dataLocal(texto) {
    if (!texto) {
        return null;
    }
    if (texto.length === 10) {
        const [ano, mes, dia] = texto.split("-").map(Number);
        return new Date(ano, mes - 1, dia);
    }
    return new Date(texto);
}

function formatarData(texto, opcoes = { day: "2-digit", month: "long", year: "numeric" }) {
    const data = dataLocal(texto);
    return data ? data.toLocaleDateString("pt-BR", opcoes) : "";
}

function formatarDataHora(texto) {
    const data = dataLocal(texto);
    return data
        ? data.toLocaleString("pt-BR", { day: "2-digit", month: "short", hour: "2-digit", minute: "2-digit" })
        : "";
}

function somarDias(data, dias) {
    const nova = new Date(data);
    nova.setDate(nova.getDate() + dias);
    return nova;
}

function podeConfirmar() {
    return Boolean(estado.usuario && estado.usuario.permissoes.includes("confirmarDoacao"));
}

function imagemAtual() {
    return estado.mascote && estado.mascote.atual && estado.mascote.atual.imagem
        ? estado.mascote.atual.imagem
        : IMAGEM_PADRAO;
}

async function entrar(id) {
    const usuario = await api("GET", `/usuarios/${id}`);
    estado.usuario = usuario;
    salvarUsuario(usuario.id);

    const notificacoes = await api("GET", `/usuarios/${usuario.id}/notificacoes`);
    estado.notificacoesVistas = new Set(notificacoes.map((notificacao) => notificacao.id));
    atualizarSino(notificacoes);

    await carregarMascote(false);
    mostrarApp();
    return usuario;
}

function sair() {
    estado.usuario = null;
    estado.mascote = null;
    estado.missoes = [];
    salvarUsuario(null);
    $("#app").hidden = true;
    $("#entrada").hidden = false;
    history.replaceState(null, "", location.pathname);
    window.scrollTo(0, 0);
}

function mostrarApp() {
    $("#entrada").hidden = true;
    $("#app").hidden = false;
    $$("[data-so-confirmador]").forEach((elemento) => { elemento.hidden = !podeConfirmar(); });
    atualizarTopo();
    if (!location.hash) {
        location.hash = "#inicio";
    } else {
        abrirAba();
    }
}

function atualizarTopo() {
    $("#topo-nome").textContent = estado.usuario.nome;
    $("#topo-mascote").src = imagemAtual();
}

async function recarregarUsuario() {
    estado.usuario = await api("GET", `/usuarios/${estado.usuario.id}`);
    atualizarTopo();
}

async function verificarNovidades() {
    if (!estado.usuario) {
        return;
    }
    const notificacoes = await api("GET", `/usuarios/${estado.usuario.id}/notificacoes`);
    const novas = notificacoes
        .filter((notificacao) => !estado.notificacoesVistas.has(notificacao.id))
        .reverse();

    let novoNivel = null;
    novas.forEach((notificacao) => {
        estado.notificacoesVistas.add(notificacao.id);
        if (notificacao.tipo === "NIVEL") {
            novoNivel = notificacao;
        } else {
            const ehRecompensa = ["MISSAO", "RECOMPENSA", "CONQUISTA"].includes(notificacao.tipo);
            avisar(notificacao.titulo, notificacao.mensagem, ehRecompensa ? "recompensa" : "");
        }
    });
    atualizarSino(notificacoes);
    await recarregarUsuario();

    if (novoNivel) {
        await carregarMascote(false);
        comemorarNivel(estado.usuario.nivel);
    }
}

const CARREGAR_SECAO = {
    inicio: carregarInicio,
    doacoes: carregarDoacoes,
    missoes: carregarMissoes,
    mascote: () => carregarMascote(true),
    hemocentros: carregarHemocentros,
    "como-doar": carregarComoDoar,
    perfil: carregarPerfil,
    painel: carregarPainel,
};

async function abrirAba() {
    if (!estado.usuario) {
        return;
    }
    const pedida = (location.hash || "#inicio").slice(1);
    let aba = CARREGAR_SECAO[pedida] ? pedida : "inicio";
    if (aba === "painel" && !podeConfirmar()) {
        aba = "inicio";
    }

    $$("[data-secao]").forEach((secao) => { secao.hidden = secao.dataset.secao !== aba; });
    $$("[data-aba]").forEach((link) => {
        if (link.dataset.aba === aba) {
            link.setAttribute("aria-current", "page");
        } else {
            link.removeAttribute("aria-current");
        }
    });
    fecharSino();
    window.scrollTo(0, 0);

    try {
        await CARREGAR_SECAO[aba]();
    } catch (falha) {
        avisar("Não foi possível carregar esta página", falha.message);
    }
}

async function garantirMissoes() {
    estado.missoes = await api("GET", `/usuarios/${estado.usuario.id}/missoes`);
    return estado.missoes;
}

async function concluirVisita(nomeMissao) {
    if (!estado.missoes.length) {
        await garantirMissoes();
    }
    const missao = estado.missoes.find((item) => item.nome === nomeMissao);
    if (!missao || missao.situacao !== "DISPONIVEL" || !missao.concluidaAoVisitar) {
        return;
    }
    missao.situacao = "CONCLUIDA";
    try {
        await api("POST", `/usuarios/${estado.usuario.id}/missoes/${missao.id}/concluir`);
        await verificarNovidades();
    } catch (falha) {
        missao.situacao = "DISPONIVEL";
    }
}

function progressoNivel(pontos) {
    let indice = 0;
    NIVEIS.forEach((nivel, posicao) => {
        if (pontos >= nivel.min) {
            indice = posicao;
        }
    });
    const proximo = NIVEIS[indice + 1];
    if (!proximo) {
        return { porcentagem: 100, texto: "Você chegou ao nível máximo. Obrigado por salvar vidas." };
    }
    const atual = NIVEIS[indice];
    const porcentagem = Math.round(((pontos - atual.min) / (proximo.min - atual.min)) * 100);
    return { porcentagem, texto: `Faltam ${proximo.min - pontos} pontos para ${proximo.nome}.` };
}

async function carregarInicio() {
    const usuario = estado.usuario;
    const [doacoes, missoes, eventos, necessidades, pendentes] = await Promise.all([
        api("GET", `/usuarios/${usuario.id}/doacoes`),
        garantirMissoes(),
        api("GET", "/eventos/ativos"),
        api("GET", "/necessidades"),
        podeConfirmar() ? api("GET", `/doacoes/pendentes?idUsuario=${usuario.id}`) : Promise.resolve(null),
    ]);

    $("#inicio-mascote").src = imagemAtual();
    $("#inicio-mascote").alt = estado.mascote && estado.mascote.atual ? `Seu mascote: ${estado.mascote.atual.nome}` : "Seu mascote";
    $("#inicio-saudacao").textContent = `Olá, ${usuario.nome.split(" ")[0]}`;
    $("#inicio-nivel").textContent = usuario.nivel || "Iniciante";

    const progresso = progressoNivel(usuario.pontuacao);
    $("#inicio-barra-preenchida").style.width = `${Math.max(progresso.porcentagem, 3)}%`;
    $("#inicio-barra").setAttribute("aria-valuenow", String(progresso.porcentagem));
    $("#inicio-progresso").textContent = progresso.texto;
    $("#inicio-pontos").textContent = usuario.pontuacao;
    $("#inicio-doacoes").textContent = doacoes.filter((doacao) => doacao.status === "CONFIRMADA").length;
    $("#inicio-missoes").textContent = missoes.filter((missao) => missao.situacao === "CONCLUIDA").length;

    const caixaEvento = $("#inicio-evento");
    caixaEvento.innerHTML = "";
    eventos.forEach((evento) => {
        caixaEvento.append(h("a", { classe: "faixa faixa-evento", href: "#missoes" },
            h("h2", {}, evento.nome.replace(" (exemplo)", "")),
            h("p", {}, `${evento.descricao || ""} Até ${formatarData(evento.dataFim, { day: "2-digit", month: "long" })}.`)));
    });

    const caixaEstoque = $("#inicio-estoque");
    caixaEstoque.innerHTML = "";
    if (pendentes && pendentes.length) {
        caixaEstoque.append(h("a", { classe: "faixa faixa-evento", href: "#painel" },
            h("h2", {}, `${pendentes.length} ${pendentes.length === 1 ? "doação espera" : "doações esperam"} confirmação`),
            h("p", {}, "Abra o painel do hemocentro para confirmar.")));
    }
    const criticos = necessidades.filter((necessidade) => necessidade.status === "CRITICO");
    if (criticos.length) {
        caixaEstoque.append(h("a", { classe: "faixa faixa-alerta", href: "#hemocentros" },
            h("p", {}, "Estoque crítico hoje"),
            h("span", { classe: "tipos-criticos" }, criticos.map((necessidade) => necessidade.tipoSanguineo).join("  ")),
            h("p", {}, "Se este é o seu tipo, sua doação faz muita falta agora.")));
    }

    const proximas = missoes
        .filter((missao) => missao.situacao === "DISPONIVEL" && emAndamento(missao))
        .slice(0, 4);
    const lista = $("#inicio-lista-missoes");
    lista.innerHTML = "";
    if (!proximas.length) {
        lista.append(h("li", { classe: "ajuda" }, "Você concluiu todas as missões disponíveis."));
    }
    proximas.forEach((missao) => lista.append(itemMissao(missao)));
}

function emAndamento(missao) {
    return MISSOES_AUTOMATICAS.has(missao.nome) || missao.concluidaAoVisitar;
}

function itemMissao(missao) {
    const concluida = missao.situacao === "CONCLUIDA";
    const encerrada = missao.situacao === "ENCERRADA";
    let acao = null;
    if (concluida) {
        acao = h("span", { classe: "missao-acao ajuda" }, `Concluída em ${formatarData(missao.dataConclusao, { day: "2-digit", month: "short" })}`);
    } else if (encerrada) {
        acao = h("span", { classe: "missao-acao ajuda" }, "Encerrada");
    } else if (missao.concluidaAoVisitar) {
        acao = h("a", { classe: "missao-acao botao-link", href: DESTINO_MISSAO[missao.nome] || "#inicio" }, "Fazer agora");
    } else if (!MISSOES_AUTOMATICAS.has(missao.nome)) {
        acao = h("span", { classe: "missao-acao ajuda" }, "Em breve");
    }

    return h("li", { classe: `missao ${concluida ? "concluida" : ""} ${encerrada ? "encerrada" : ""}` },
        h("span", { classe: "missao-marca" }, concluida ? icone(ICONE_CHECK) : null),
        h("div", {},
            h("p", { classe: "missao-nome" }, missao.nome, missao.doEvento ? h("span", { classe: "selo-evento" }, "Evento") : null),
            h("p", { classe: "missao-descricao" }, missao.descricao || ""),
            acao),
        h("span", { classe: "pontos" }, `+${missao.pontuacao}`));
}

async function carregarMissoes() {
    const [missoes, conquistas] = await Promise.all([
        garantirMissoes(),
        api("GET", `/usuarios/${estado.usuario.id}/conquistas`),
    ]);

    const grupos = $("#grupos-missoes");
    grupos.innerHTML = "";
    GRUPOS_MISSOES.forEach((grupo) => {
        const doGrupo = missoes.filter((missao) => missao.tipo === grupo.tipo);
        if (!doGrupo.length) {
            return;
        }
        const concluidas = doGrupo.filter((missao) => missao.situacao === "CONCLUIDA").length;
        grupos.append(h("div", { classe: "grupo-missoes" },
            h("div", { classe: "cartao-topo" },
                h("h2", { classe: "subtitulo" }, grupo.titulo),
                h("span", { classe: "ajuda" }, `${concluidas} de ${doGrupo.length}`)),
            h("ul", { classe: "lista-missoes" }, doGrupo.map(itemMissao))));
    });

    const lista = $("#lista-conquistas");
    lista.innerHTML = "";
    conquistas.forEach((conquista) => {
        lista.append(h("li", { classe: `conquista ${conquista.desbloqueada ? "desbloqueada" : ""}` },
            h("span", { classe: "conquista-medalha" }, icone(ICONE_MEDALHA)),
            h("div", {},
                h("p", { classe: "conquista-nome" }, conquista.nome),
                h("p", {}, conquista.desbloqueada
                    ? `Desbloqueada em ${formatarData(conquista.dataDesbloqueio, { day: "2-digit", month: "short" })}`
                    : conquista.criterio),
                conquista.recompensa ? h("p", { classe: "recompensa" }, `Recompensa: ${conquista.recompensa}`) : null)));
    });
}

async function carregarMascote(desenhar) {
    estado.mascote = await api("GET", `/usuarios/${estado.usuario.id}/mascote`);
    if (estado.usuario) {
        atualizarTopo();
    }
    if (desenhar) {
        desenharMascote();
    }
}

function desenharMascote(idRecemEquipado) {
    const { atual, visuais } = estado.mascote;
    $("#mascote-atual").src = imagemAtual();
    $("#mascote-atual").alt = atual ? `Seu mascote: ${atual.nome}` : "Seu mascote";
    $("#mascote-nome").textContent = atual ? atual.nome : "";
    $("#mascote-descricao").textContent = atual ? atual.descricao || "" : "";
    const liberados = visuais.filter((visual) => visual.desbloqueado).length;
    $("#mascote-contagem").textContent = `${liberados} de ${visuais.length} visuais liberados`;

    const montar = (visual) => {
        const classes = ["visual"];
        if (!visual.desbloqueado) classes.push("bloqueado");
        if (visual.equipado) classes.push("equipado");
        if (visual.id === idRecemEquipado) classes.push("recem-equipado");
        const estadoTexto = visual.equipado ? "Equipado" : visual.desbloqueado ? "Liberado" : visual.requisito;
        return h("li", { classe: classes.join(" ") },
            h("button", {
                type: "button",
                "aria-pressed": visual.equipado ? "true" : "false",
                "aria-label": `${visual.nome}. ${estadoTexto}`,
                onclick: () => equiparVisual(visual),
            },
            h("span", { classe: "visual-imagem" },
                h("img", { src: visual.imagem || IMAGEM_PADRAO, alt: "", width: "240", height: "240", loading: "lazy" }),
                visual.desbloqueado ? null : h("span", { classe: "visual-cadeado" }, icone(ICONE_CADEADO))),
            h("span", { classe: "visual-nome" }, visual.nome),
            h("span", { classe: "visual-estado" }, estadoTexto)));
    };

    const cores = $("#visuais-cores");
    const acessorios = $("#visuais-acessorios");
    cores.innerHTML = "";
    acessorios.innerHTML = "";
    visuais.filter((visual) => visual.tipo === "COR").forEach((visual) => cores.append(montar(visual)));
    visuais.filter((visual) => visual.tipo !== "COR").forEach((visual) => acessorios.append(montar(visual)));
}

async function equiparVisual(visual) {
    if (!visual.desbloqueado) {
        avisar(`${visual.nome} está bloqueado`, `${visual.requisito}.`);
        return;
    }
    if (visual.equipado) {
        return;
    }
    try {
        estado.mascote = await api("PUT", `/usuarios/${estado.usuario.id}/mascote`, { idItem: visual.id });
        desenharMascote(visual.id);
        atualizarTopo();
        avisar("Visual equipado", visual.nome);
        await verificarNovidades();
        await carregarMascote(false);
        desenharMascote(visual.id);
    } catch (falha) {
        avisar("Não foi possível equipar", falha.message);
    }
}

async function carregarDoacoes() {
    const doacoes = await api("GET", `/usuarios/${estado.usuario.id}/doacoes`);
    desenharHistorico(doacoes);
    desenharIntervalo(doacoes);
    concluirVisita("Ver seu histórico");
}

function desenharHistorico(doacoes, idDestaque) {
    const lista = $("#lista-historico");
    lista.innerHTML = "";
    $("#historico-vazio").hidden = doacoes.length > 0;
    doacoes.forEach((doacao) => {
        const confirmada = doacao.status === "CONFIRMADA";
        const data = dataLocal(doacao.dataRegistro);
        lista.append(h("li", { classe: `registro ${doacao.id === idDestaque ? "recem" : ""}` },
            h("time", { classe: "registro-data", datetime: doacao.dataRegistro },
                h("span", { classe: "registro-dia" }, data.toLocaleDateString("pt-BR", { day: "2-digit" })),
                h("span", { classe: "registro-mes" }, data.toLocaleDateString("pt-BR", { month: "short", year: "2-digit" }).replace(".", ""))),
            h("div", {},
                h("p", { classe: "registro-local" }, doacao.local || "Local não informado"),
                h("p", { classe: "registro-detalhe" }, confirmada
                    ? `Confirmada em ${formatarDataHora(doacao.dataConfirmacao)}. +100 pontos.`
                    : "Aguardando a confirmação do hemocentro.")),
            h("span", { classe: `selo ${confirmada ? "confirmada" : ""}` }, confirmada ? "Confirmada" : "Registrada")));
    });
}

function desenharIntervalo(doacoes) {
    const caixa = $("#doacoes-intervalo");
    caixa.innerHTML = "";
    const confirmadas = doacoes
        .filter((doacao) => doacao.status === "CONFIRMADA")
        .map((doacao) => dataLocal(doacao.dataConfirmacao))
        .sort((a, b) => b - a);

    caixa.append(h("h2", {}, "Sua próxima doação"));
    if (!confirmadas.length) {
        caixa.append(h("p", {}, "Você ainda não tem doação confirmada. Antes da primeira, veja quem pode doar."),
            h("a", { classe: "botao-link", href: "#como-doar" }, "Ver os requisitos"));
        return;
    }
    const ultima = confirmadas[0];
    const opcoes = { day: "2-digit", month: "long", year: "numeric" };
    caixa.append(
        h("p", {}, "Última doação confirmada: ", h("strong", {}, ultima.toLocaleDateString("pt-BR", opcoes)), "."),
        h("p", {}, "Homens podem doar de novo a partir de ", h("strong", {}, somarDias(ultima, 60).toLocaleDateString("pt-BR", opcoes)), "."),
        h("p", {}, "Mulheres, a partir de ", h("strong", {}, somarDias(ultima, 90).toLocaleDateString("pt-BR", opcoes)), "."));
}

const STATUS_ESTOQUE = { ADEQUADO: "Adequado", ATENCAO: "Atenção", CRITICO: "Crítico" };

async function carregarHemocentros() {
    const [necessidades, campanhas] = await Promise.all([
        api("GET", "/necessidades"),
        api("GET", "/campanhas"),
    ]);

    const estoque = $("#lista-estoque");
    estoque.innerHTML = "";
    necessidades.forEach((necessidade) => {
        const cheio = Math.min(necessidade.quantidade / 120, 1);
        estoque.append(h("li", { classe: necessidade.status.toLowerCase().replace("ç", "c") },
            h("span", { classe: "estoque-tipo" }, necessidade.tipoSanguineo),
            h("span", { classe: "estoque-bolsa", "aria-hidden": "true" },
                h("span", { style: `height: ${Math.max(cheio * 100, 6)}%` })),
            h("span", { classe: "estoque-status" }, STATUS_ESTOQUE[necessidade.status] || necessidade.status)));
    });

    const listaCampanhas = $("#lista-campanhas");
    listaCampanhas.innerHTML = "";
    if (!campanhas.length) {
        listaCampanhas.append(h("li", { classe: "vazio" }, "Nenhuma campanha cadastrada."));
    }
    campanhas.forEach((campanha) => {
        const ativa = campanha.status === "ATIVA";
        listaCampanhas.append(h("li", { classe: "campanha" },
            h("div", { classe: "campanha-topo" },
                h("p", { classe: "campanha-nome" }, campanha.nome),
                h("span", { classe: "campanha-tipo" }, campanha.tipoSanguineo || "Todos os tipos")),
            h("p", {}, campanha.descricao || ""),
            h("p", {}, `${formatarData(campanha.dataInicio, { day: "2-digit", month: "short" })} a ${formatarData(campanha.dataFim, { day: "2-digit", month: "short" })}`,
                campanha.local ? `, ${campanha.local}` : ""),
            h("span", { classe: `campanha-status ${ativa ? "ativa" : ""}` }, ativa ? "Acontecendo agora" : "Planejada")));
    });

    const listaLocais = $("#lista-locais");
    listaLocais.innerHTML = "";
    estado.locais.forEach((local) => {
        listaLocais.append(h("li", { classe: "local" },
            h("p", { classe: "local-nome" }, local.nome),
            h("p", {}, `${local.endereco}, ${local.cidade} (${local.estado})`),
            local.horarioFuncionamento ? h("p", {}, local.horarioFuncionamento) : null,
            local.telefone ? h("p", {}, local.telefone) : null));
    });

    concluirVisita("Visitar um local");
}

async function carregarComoDoar() {
    await concluirVisita("Conhecer os requisitos");
}

async function carregarPerfil() {
    await recarregarUsuario();
    const usuario = estado.usuario;
    $("#ficha-mascote").src = imagemAtual();
    $("#ficha-papel").textContent = PAPEIS[usuario.tipo] || usuario.tipo;
    $("#ficha-nome").textContent = usuario.nome;
    $("#ficha-email").textContent = usuario.email;
    $("#ficha-tipo").textContent = usuario.tipoSanguineo
        ? `Tipo sanguíneo ${usuario.tipoSanguineo.replace("-", "−")}`
        : "Tipo sanguíneo não informado";

    const permissoes = $("#ficha-permissoes");
    permissoes.innerHTML = "";
    usuario.permissoes.forEach((permissao) => permissoes.append(h("li", {}, PERMISSOES[permissao] || permissao)));

    const formulario = $("#form-perfil");
    formulario.nome.value = usuario.nome || "";
    formulario.telefone.value = usuario.telefone || "";
    formulario.dataNasc.value = usuario.dataNasc || "";
    const tipo = estado.tipos.find((item) => item.rotulo === usuario.tipoSanguineo);
    formulario.idTipoSanguineo.value = tipo ? String(tipo.id) : "";
    mostrarErro(formulario, "");
}

async function carregarPainel() {
    const pendentes = await api("GET", `/doacoes/pendentes?idUsuario=${estado.usuario.id}`);
    const lista = $("#lista-pendentes");
    lista.innerHTML = "";
    $("#pendentes-vazio").hidden = pendentes.length > 0;
    pendentes.forEach((doacao) => {
        const botao = h("button", { type: "button", classe: "botao botao-pequeno" }, "Confirmar doação");
        const item = h("li", { classe: "pendente" },
            h("div", {},
                h("p", { classe: "pendente-nome" }, doacao.nomeUsuario),
                h("p", {}, `Registrada em ${formatarDataHora(doacao.dataRegistro)}. ${doacao.local || "Local não informado"}.`)),
            botao);
        botao.addEventListener("click", async () => {
            try {
                await enviando(botao, () => api("PATCH", `/doacoes/${doacao.id}/confirmar`,
                    { idUsuarioConfirmacao: estado.usuario.id }));
                item.remove();
                $("#pendentes-vazio").hidden = lista.children.length > 0;
                avisar("Doação confirmada", `${doacao.nomeUsuario} ganhou 100 pontos.`, "recompensa");
                await verificarNovidades();
            } catch (falha) {
                avisar("Não foi possível confirmar", falha.message);
            }
        });
        lista.append(item);
    });
}

function atualizarSino(notificacoes) {
    const naoLidas = notificacoes.filter((notificacao) => !notificacao.lida).length;
    const contador = $("#sino-contador");
    contador.textContent = naoLidas > 9 ? "9+" : String(naoLidas);
    contador.hidden = naoLidas === 0;
    $("#botao-sino .sr-only").textContent = naoLidas ? `Notificações, ${naoLidas} não lidas` : "Notificações";

    const lista = $("#lista-notificacoes");
    lista.innerHTML = "";
    if (!notificacoes.length) {
        lista.append(h("li", { classe: "ajuda" }, "Nada por aqui ainda."));
    }
    notificacoes.slice(0, 20).forEach((notificacao) => {
        lista.append(h("li", { classe: `notificacao ${notificacao.lida ? "" : "nao-lida"}` },
            h("strong", {}, notificacao.titulo),
            h("p", {}, notificacao.mensagem),
            h("time", { datetime: notificacao.dataEnvio }, formatarDataHora(notificacao.dataEnvio))));
    });
}

async function abrirSino() {
    $("#painel-avisos").hidden = false;
    $("#botao-sino").setAttribute("aria-expanded", "true");
    try {
        await api("PATCH", `/usuarios/${estado.usuario.id}/notificacoes/lidas`);
        $("#sino-contador").hidden = true;
    } catch (falha) {
    }
}

function fecharSino() {
    if ($("#painel-avisos").hidden) {
        return;
    }
    $("#painel-avisos").hidden = true;
    $("#botao-sino").setAttribute("aria-expanded", "false");
    api("GET", `/usuarios/${estado.usuario.id}/notificacoes`).then(atualizarSino).catch(() => {});
}

function comemorarNivel(nomeNivel) {
    $("#celebracao-mascote").src = imagemAtual();
    $("#celebracao-titulo").textContent = nomeNivel;
    $("#celebracao-texto").textContent = "Você subiu de nível. Veja se o seu mascote ganhou visuais novos.";
    $("#celebracao").hidden = false;
    $("#celebracao-fechar").focus();
}

function fecharComemoracao() {
    $("#celebracao").hidden = true;
}

function ligarEventos() {
    window.addEventListener("hashchange", abrirAba);

    $("#form-cadastro").addEventListener("submit", async (evento) => {
        evento.preventDefault();
        const formulario = evento.currentTarget;
        mostrarErro(formulario, "");
        if (!formularioValido(formulario)) {
            return;
        }
        try {
            const usuario = await enviando($("button[type=submit]", formulario),
                () => api("POST", "/usuarios", lerFormulario(formulario)));
            formulario.reset();
            await entrar(usuario.id);
            estado.notificacoesVistas.clear();
            await verificarNovidades();
            avisar("Conta criada", `Bem-vindo ao HemoPlay, ${usuario.nome.split(" ")[0]}.`);
        } catch (falha) {
            mostrarErro(formulario, falha.message);
        }
    });

    const entrarCom = async (id, formulario) => {
        mostrarErro(formulario, "");
        try {
            await entrar(id);
        } catch (falha) {
            mostrarErro(formulario, falha.message);
        }
    };
    $("#form-entrar").addEventListener("submit", (evento) => {
        evento.preventDefault();
        const id = Number($("#entrar-id").value);
        if (id) {
            entrarCom(id, evento.currentTarget);
        }
    });
    $$("[data-entrar]").forEach((botao) => {
        botao.addEventListener("click", () => entrarCom(Number(botao.dataset.entrar), $("#form-entrar")));
    });

    $("#botao-sair").addEventListener("click", sair);

    $("#form-doacao").addEventListener("submit", async (evento) => {
        evento.preventDefault();
        const formulario = evento.currentTarget;
        mostrarErro(formulario, "");
        try {
            const corpo = { idUsuario: estado.usuario.id, idLocal: lerFormulario(formulario).idLocal };
            const doacao = await enviando($("button[type=submit]", formulario), () => api("POST", "/doacoes", corpo));
            avisar("Doação registrada", "Quando o hemocentro confirmar, os pontos entram na sua conta.");
            const doacoes = await api("GET", `/usuarios/${estado.usuario.id}/doacoes`);
            desenharHistorico(doacoes, doacao.id);
            desenharIntervalo(doacoes);
            await verificarNovidades();
        } catch (falha) {
            mostrarErro(formulario, falha.message);
        }
    });

    $("#form-perfil").addEventListener("submit", async (evento) => {
        evento.preventDefault();
        const formulario = evento.currentTarget;
        mostrarErro(formulario, "");
        if (!formularioValido(formulario)) {
            return;
        }
        try {
            estado.usuario = await enviando($("button[type=submit]", formulario),
                () => api("PUT", `/usuarios/${estado.usuario.id}`, lerFormulario(formulario)));
            avisar("Alterações salvas");
            await verificarNovidades();
            await carregarPerfil();
        } catch (falha) {
            mostrarErro(formulario, falha.message);
        }
    });

    $("#botao-excluir").addEventListener("click", () => {
        $("#botao-excluir").hidden = true;
        $("#exclusao-confirmar").hidden = false;
        $("#botao-excluir-nao").focus();
    });
    $("#botao-excluir-nao").addEventListener("click", () => {
        $("#exclusao-confirmar").hidden = true;
        $("#botao-excluir").hidden = false;
    });
    $("#botao-excluir-sim").addEventListener("click", async () => {
        try {
            await enviando($("#botao-excluir-sim"), () => api("DELETE", `/usuarios/${estado.usuario.id}`));
            $("#exclusao-confirmar").hidden = true;
            $("#botao-excluir").hidden = false;
            sair();
            avisar("Conta excluída");
        } catch (falha) {
            avisar("Não foi possível excluir", falha.message);
        }
    });

    $("#detalhe-intervalo").addEventListener("toggle", (evento) => {
        if (evento.currentTarget.open) {
            concluirVisita("Conhecer o intervalo");
        }
    });

    $("#botao-sino").addEventListener("click", () => {
        if ($("#painel-avisos").hidden) {
            abrirSino();
        } else {
            fecharSino();
        }
    });
    document.addEventListener("click", (evento) => {
        if (!evento.target.closest(".sino")) {
            fecharSino();
        }
    });

    $("#celebracao-fechar").addEventListener("click", fecharComemoracao);
    document.addEventListener("keydown", (evento) => {
        if (evento.key === "Escape") {
            fecharComemoracao();
            fecharSino();
        }
    });
}

async function carregarListas() {
    const [tipos, locais] = await Promise.all([api("GET", "/tipos-sanguineos"), api("GET", "/locais")]);
    estado.tipos = tipos;
    estado.locais = locais;
    $$("select[data-tipos]").forEach((select) => {
        tipos.forEach((tipo) => select.append(new Option(tipo.rotulo, tipo.id)));
    });
    $$("select[data-locais]").forEach((select) => {
        locais.forEach((local) => select.append(new Option(`${local.nome} (${local.cidade}, ${local.estado})`, local.id)));
    });
}

async function iniciar() {
    ligarEventos();
    try {
        await carregarListas();
    } catch (falha) {
        avisar("Não foi possível carregar os dados iniciais", falha.message);
    }

    const idSalvo = lerUsuarioSalvo();
    if (idSalvo) {
        try {
            await entrar(idSalvo);
            return;
        } catch (falha) {
            salvarUsuario(null);
        }
    }
    $("#entrada").hidden = false;
}

iniciar();
