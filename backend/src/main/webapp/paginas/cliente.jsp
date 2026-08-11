<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="br.com.agrobombackend.model.Cliente"%>
<%@ page import="br.com.agrobombackend.dao.ClienteDAO"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Clientes - AgroBom</title>
</head>

<body>

	<div class="container">

		<h1>Cadastro de Clientes</h1>

		<form action="clientes" method="post">

			<label>CPF</label> <input type="text" name="cpf" required> <label>Nome</label>
			<input type="text" name="nome" required> <label>Telefone</label>
			<input type="text" name="telefone" required> <label>Endereço</label>
			<input type="text" name="endereco" required>

			<button type="submit">Cadastrar</button>

		</form>

		<h1>Clientes Cadastrados</h1>

		<table border="1">

			<tr>
				<th>CPF</th>
				<th>Nome</th>
				<th>Telefone</th>
				<th>Endereço</th>
				<th>Ações</th>
			</tr>

			<%
			ClienteDAO dao = new ClienteDAO();
			List<Cliente> clientes = dao.listarTodos();

			for (Cliente c : clientes) {
			%>

			<tr>
				<td><%=c.getCpf_cliente()%></td>
				<td><%=c.getNome_cliente()%></td>
				<td><%=c.getTelefone_cliente()%></td>
				<td><%=c.getEndereco_cliente()%></td>

				<td><a href="clientes?acao=editar&cpf=<%=c.getCpf_cliente()%>">Editar</a>

					<form action="clientes" method="post" style="display: inline;">
						<input type="hidden" name="acao" value="remover"> <input
							type="hidden" name="cpf" value="<%=c.getCpf_cliente()%>">
						<button type="submit">Remover</button>
					</form></td>
			</tr>

			<%
			}
			%>

		</table>

	</div>

</body>
</html>