<!DOCTYPE html>
<html>
    <head>
        <title>${action} estudiante</title>
        <link href="/css/style.css" rel="stylesheet">
    </head>
    <body>
        <div class="topnav">
            <a href="/">Inicio</a>
            <a href="/listar">Listar</a>
            <a <#if action == "Registrar" >class="active"</#if>href="/registrar">Registrar</a>
        </div>
        <h1>${action} estudiante</h1>
        <div class="view-student">
            <form method="POST" action="<#if action == "Registrar">/registrar<#else>/editar/${estudiante.matricula?string["0"]}</#if>">
                Matricula:<br>
                <input type="number" name="matricula" min="19620000" max="20195000" value="${estudiante.matricula?string["0"]}"/><br>
                Nombre:<br>
                <input type="text" name="nombre" value="${estudiante.nombre}"/><br>
                Apellido:<br>
                <input type="text" name="apellido" value="${estudiante.apellido}"/><br>
                Telefono:<br>
                <input type="text" name="telefono" value="${estudiante.telefono}"/><br><br>
                <button type="submit">${action}</button>
            </form>
        </div>
    </body>
</html>