<!DOCTYPE html>
<html>
    <head>
        <title>Lista de estudiantes</title>
        <link href="/css/style.css" rel="stylesheet">
    </head>
    <body>
        <div class="topnav">
            <a href="/">Inicio</a>
            <a class="active" href="/listar">Listar</a>
            <a href="/registrar">Registrar</a>
        </div>
        <h1>Lista de Estudiantes</h1>
        <table class="list-student">
            <tr>
                <th>Matricula</th>
                <th>Nombre completo</th>
                <th>Telefono</th>
            </tr>
            <#list estudiantes as estudiante>
            <tr class='clickable-row' data-href='/estudiante/${estudiante.matricula?string["0"]}'>
                <td>${estudiante.matricula?string["0"]}</td>
                <td>${estudiante.nombre} ${estudiante.apellido}</td>
                <td>${estudiante.telefono}</td>
            </tr>
            </#list>
        </table>
        <script src="/scripts/jquery-3.4.1.min.js"></script>
        <script src="/scripts/script.js"></script>
    </body>
</html>