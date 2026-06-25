const API_URL = "http://localhost:8080";

// Instanciação dos modais do Bootstrap para podermos abrir/fechar via JS
const modalCliente = new bootstrap.Modal(document.getElementById('modalCliente'));
const modalProduto = new bootstrap.Modal(document.getElementById('modalProduto'));

// Executa assim que a página carrega completamente
document.addEventListener("DOMContentLoaded", () => {
    verificarAutenticacao();
    document.getElementById("nomeUsuario").textContent = localStorage.getItem("usuario_guara");
    
    // Carrega as tabelas iniciais
    carregarClientes();
    carregarProdutos();

    // Ouvintes de envio dos formulários
    document.getElementById("formCliente").addEventListener("submit", salvarCliente);
    document.getElementById("formProduto").addEventListener("submit", salvarProduto);
});

function verificarAutenticacao() {
    const token = localStorage.getItem("token_guara");
    if (!token) {
        window.location.href = "login.html"; // Chuta de volta se não tiver logado
    }
}

function logout() {
    localStorage.clear();
    window.location.href = "login.html";
}

// Configuração padrão dos cabeçalhos com o Token JWT salvo no Login
function getHeaders() {
    return {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${localStorage.getItem("token_guara")}`
    };
}

// ==================== LÓGICA DE CLIENTES ====================

async function carregarClientes() {
    try {
        const response = await fetch(`${API_URL}/clientes`, { method: "GET", headers: getHeaders() });
        if (response.status === 403) return logout(); // Token expirou ou inválido
        
        const clientes = await response.json();
        const tbody = document.getElementById("tabelaClientes");
        const comboClientes = document.getElementById("vendaClienteSelect");
        tbody.innerHTML = "";
        comboClientes.innerHTML = '<option value="">Selecione um Cliente</option>';

        clientes.forEach(c => {
            tbody.innerHTML += `
                <tr>
                    <td>${c.id}</td>
                    <td>${c.nome}</td>
                    <td>${c.cpf}</td>
                    <td>${c.email}</td>
                    <td>${c.telefone}</td>
                    <td>${c.endereco}</td>
                    <td>
                        <button class="btn btn-warning btn-sm me-1" onclick="carregarClienteParaEdicao(${c.id})">Editar</button>
                        <button class="btn btn-danger btn-sm" onclick="deletarCliente(${c.id})">Excluir</button>
                    </td>
                </tr>
            `;
            // Alimenta também o combo de clientes na tela de Vendas
            comboClientes.innerHTML += `<option value="${c.id}">${c.nome} (CPF: ${c.cpf})</option>`;
        });
    } catch (err) { console.error("Erro ao carregar clientes", err); }
}

function abrirModalCadastroCliente() {
    document.getElementById("formCliente").reset();
    document.getElementById("clienteId").value = "";
    document.getElementById("clienteCpf").disabled = false; // CPF só se cria, não se edita na sua regra
    document.getElementById("modalClienteTitulo").textContent = "Cadastrar Cliente";
    modalCliente.show();
}

async function salvarCliente(e) {
    e.preventDefault();
    const id = document.getElementById("clienteId").value;
    
    const dadosCliente = {
        nome: document.getElementById("clienteNome").value,
        cpf: document.getElementById("clienteCpf").value,
        email: document.getElementById("clienteEmail").value,
        telefone: document.getElementById("clienteTelefone").value,
        endereco: document.getElementById("clienteEndereco").value
    };

    const url = id ? `${API_URL}/clientes/${id}` : `${API_URL}/clientes`;
    const metodo = id ? "PUT" : "POST";

    try {
        const response = await fetch(url, {
            method: metodo,
            headers: getHeaders(),
            body: JSON.stringify(dadosCliente)
        });

        const resultado = await response.json();

        if (response.ok) {
            modalCliente.hide();
            carregarClientes();
        } else {
            alert(resultado.erro || "Verifique as validações dos campos!");
        }
    } catch (err) { alert("Erro de conexão com o servidor."); }
}

async function carregarClienteParaEdicao(id) {
    try {
        const response = await fetch(`${API_URL}/clientes/${id}`, { method: "GET", headers: getHeaders() });
        const c = await response.json();

        document.getElementById("clienteId").value = c.id;
        document.getElementById("clienteNome").value = c.nome;
        document.getElementById("clienteCpf").value = c.cpf;
        document.getElementById("clienteCpf").disabled = true; // Desabilita pois o seu service não altera CPF no PUT
        document.getElementById("clienteEmail").value = c.email;
        document.getElementById("clienteTelefone").value = c.telefone;
        document.getElementById("clienteEndereco").value = c.endereco;

        document.getElementById("modalClienteTitulo").textContent = "Editar Cliente";
        modalCliente.show();
    } catch (err) { console.error(err); }
}

async function deletarCliente(id) {
    if (confirm("Tem certeza que deseja excluir este cliente?")) {
        const response = await fetch(`${API_URL}/clientes/${id}`, { method: "DELETE", headers: getHeaders() });
        if (response.ok) {
            carregarClientes();
        } else {
            const err = await response.json();
            alert("Erro ao excluir cliente: " + err.erro);
        }
    }
}

// ==================== LÓGICA DE PRODUTOS ====================

async function carregarProdutos() {
    try {
        const response = await fetch(`${API_URL}/produtos`, { method: "GET", headers: getHeaders() });
        const produtos = await response.json();
        const tbody = document.getElementById("tabelaProdutos");
        const comboProdutos = document.getElementById("vendaProdutoSelect");
        tbody.innerHTML = "";
        comboProdutos.innerHTML = "";

        produtos.forEach(p => {
            tbody.innerHTML += `
                <tr>
                    <td>${p.id}</td>
                    <td>${p.nome}</td>
                    <td>${p.descricao || ''}</td>
                    <td>R$ ${p.preco.toFixed(2)}</td>
                    <td>${p.quantidadeEstoque}</td>
                    <td>
                        <button class="btn btn-warning btn-sm me-1" onclick="carregarProdutoParaEdicao(${p.id})">Editar</button>
                        <button class="btn btn-danger btn-sm" onclick="deletarProduto(${p.id})">Excluir</button>
                    </td>
                </tr>
            `;
            // Alimenta o combo na tela de vendas, desabilitando se o estoque for 0
            const disponivel = p.quantidadeEstoque > 0 ? "" : "disabled";
            comboProdutos.innerHTML += `<option value="${p.id}" data-preco="${p.preco}" data-nome="${p.nome}" ${disponivel}>
                ${p.nome} - R$ ${p.preco.toFixed(2)} (Estoque: ${p.quantidadeEstoque})
            </option>`;
        });
    } catch (err) { console.error("Erro ao carregar produtos", err); }
}

function abrirModalCadastroProduto() {
    document.getElementById("formProduto").reset();
    document.getElementById("produtoId").value = "";
    document.getElementById("modalProdutoTitulo").textContent = "Cadastrar Produto";
    modalProduto.show();
}

async function salvarProduto(e) {
    e.preventDefault();
    const id = document.getElementById("produtoId").value;
    
    const dadosProduto = {
        nome: document.getElementById("produtoNome").value,
        descricao: document.getElementById("produtoDescricao").value,
        preco: parseFloat(document.getElementById("produtoPreco").value),
        quantidadeEstoque: parseInt(document.getElementById("produtoEstoque").value)
    };

    const url = id ? `${API_URL}/produtos/${id}` : `${API_URL}/produtos`;
    const metodo = id ? "PUT" : "POST";

    try {
        const response = await fetch(url, {
            method: metodo,
            headers: getHeaders(),
            body: JSON.stringify(dadosProduto)
        });

        const resultado = await response.json();

        if (response.ok) {
            modalProduto.hide();
            carregarProdutos();
        } else {
            alert(resultado.erro || "Preço ou Estoque inválidos!");
        }
    } catch (err) { alert("Erro de conexão com o servidor."); }
}

async function carregarProdutoParaEdicao(id) {
    try {
        const response = await fetch(`${API_URL}/produtos/${id}`, { method: "GET", headers: getHeaders() });
        const p = await response.json();

        document.getElementById("produtoId").value = p.id;
        document.getElementById("produtoNome").value = p.nome;
        document.getElementById("produtoDescricao").value = p.descricao;
        document.getElementById("produtoPreco").value = p.preco;
        document.getElementById("produtoEstoque").value = p.quantidadeEstoque;

        document.getElementById("modalProdutoTitulo").textContent = "Editar Produto";
        modalProduto.show();
    } catch (err) { console.error(err); }
}

async function deletarProduto(id) {
    if (confirm("Tem certeza que deseja excluir este produto?")) {
        const response = await fetch(`${API_URL}/produtos/${id}`, { method: "DELETE", headers: getHeaders() });
        if (response.ok) carregarProdutos();
    }
}

// ==================== LÓGICA DE VENDAS E CARRINHO ====================

let carrinho = [];

function adicionarItemCarrinho() {
    const combo = document.getElementById("vendaProdutoSelect");
    const idProduto = combo.value;
    const nome = combo.options[combo.selectedIndex].getAttribute("data-nome");
    const preco = parseFloat(combo.options[combo.selectedIndex].getAttribute("data-preco"));
    const qtd = parseInt(document.getElementById("vendaQuantidade").value);

    if (!idProduto || qtd <= 0) return alert("Selecione um produto e uma quantidade válida!");

    // Verifica se já tem no carrinho
    const index = carrinho.findIndex(item => item.produtoId == idProduto);
    if (index > -1) {
        carrinho[index].quantidade += qtd;
    } else {
        carrinho.push({ produtoId: parseInt(idProduto), nome, preco, quantidade: qtd });
    }

    atualizarCarrinho();
}

function removerItemCarrinho(index) {
    carrinho.splice(index, 1);
    atualizarCarrinho();
}

function atualizarCarrinho() {
    const lista = document.getElementById("listaCarrinho");
    lista.innerHTML = "";
    let total = 0;

    carrinho.forEach((item, index) => {
        const subtotal = item.preco * item.quantidade;
        total += subtotal;
        lista.innerHTML += `
            <li class="list-group-item d-flex justify-content-between align-items-center">
                <div>
                    <strong>${item.quantidade}x</strong> ${item.nome}
                    <br><small class="text-muted">R$ ${item.preco.toFixed(2)} / un</small>
                </div>
                <div class="d-flex align-items-center">
                    <span class="me-3 fw-bold">R$ ${subtotal.toFixed(2)}</span>
                    <button class="btn btn-sm btn-outline-danger" onclick="removerItemCarrinho(${index})">X</button>
                </div>
            </li>
        `;
    });

    document.getElementById("valorTotalVenda").textContent = `R$ ${total.toFixed(2)}`;
}

async function finalizarVenda() {
    const clienteId = document.getElementById("vendaClienteSelect").value;
    
    if (!clienteId) return alert("Selecione um cliente para a venda.");
    if (carrinho.length === 0) return alert("O carrinho está vazio.");

    const payload = {
        clienteId: parseInt(clienteId),
        itens: carrinho.map(item => ({ produtoId: item.produtoId, quantidade: item.quantidade }))
    };

    try {
        const response = await fetch(`${API_URL}/vendas`, {
            method: "POST",
            headers: getHeaders(),
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            alert("Venda registrada com sucesso!");
            carrinho = [];
            atualizarCarrinho();
            carregarProdutos(); // Para atualizar o estoque no combo e na tabela
        } else {
            const err = await response.json();
            alert("Erro ao finalizar venda: " + err.erro);
        }
    } catch (error) {
        alert("Erro na conexão com o servidor ao finalizar venda.");
    }
}

// ==================== LÓGICA DE RELATÓRIOS ====================

async function gerarRelatorio(e) {
    e.preventDefault();
    const inicio = document.getElementById("relatorioInicio").value;
    const fim = document.getElementById("relatorioFim").value;

    try {
        const response = await fetch(`${API_URL}/vendas/relatorio?inicio=${inicio}&fim=${fim}`, {
            method: "GET",
            headers: getHeaders()
        });

        if (response.ok) {
            const vendas = await response.json();
            const tbody = document.getElementById("tabelaRelatorio");
            tbody.innerHTML = "";

            if (vendas.length === 0) {
                tbody.innerHTML = `<tr><td colspan="6" class="text-center">Nenhuma venda encontrada no período.</td></tr>`;
                return;
            }

            let totalGeral = 0;

            vendas.forEach(v => {
                totalGeral += v.valorTotal;
                
                // Formata os produtos para virar uma lista na tabela
                const listaProdutos = v.produtos.map(p => `<li>${p}</li>`).join("");
                
                // Formata data de ISO para dd/MM/yyyy HH:mm
                const data = new Date(v.data).toLocaleString("pt-BR");

                tbody.innerHTML += `
                    <tr>
                        <td>${v.idVenda}</td>
                        <td>${data}</td>
                        <td>${v.nomeCliente}</td>
                        <td>${v.nomeVendedor}</td>
                        <td><ul class="mb-0 ps-3">${listaProdutos}</ul></td>
                        <td class="fw-bold">R$ ${v.valorTotal.toFixed(2)}</td>
                    </tr>
                `;
            });

            // Adiciona uma linha com o Somatório Total
            tbody.innerHTML += `
                <tr class="table-primary">
                    <td colspan="5" class="text-end fw-bold">TOTAL VENDIDO NO PERÍODO:</td>
                    <td class="fw-bold fs-5">R$ ${totalGeral.toFixed(2)}</td>
                </tr>
            `;

        } else {
            alert("Erro ao gerar relatório. Verifique as datas.");
        }
    } catch (error) {
        alert("Erro ao conectar ao servidor para relatórios.");
    }
}