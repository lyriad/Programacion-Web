<!DOCTYPE html>
<html>
    <head>
        <title>Estudiante - ${estudiante.matricula?string["0"]}</title>
        <link href="/css/style.css" rel="stylesheet">
    </head>
    <body>
        <div class="topnav">
            <a href="/">Inicio</a>
            <a href="/listar">Listar</a>
            <a href="/registrar">Registrar</a>
        </div>
        <h1>Estudiante - ${estudiante.nombre} ${estudiante.apellido}</h1>
        <div class="view-student">
            <p class="title">Matricula:</p> <p>${estudiante.matricula?string["0"]}</p>
            <br><br>
            <p class="title">Nombre:</p> <p>${estudiante.nombre}</p>
            <br><br>
            <p class="title">Apellido:</p> <p>${estudiante.apellido}</p>
            <br><br>
            <p class="title">Telefono:</p> <p>${estudiante.telefono}</p>
            <br><br>
            <form method="GET" action="/editar/${estudiante.matricula?string["0"]}">
                <button class="btn-warn" type="submit">Editar</button>
            </form>
            <form method="POST" action="/eliminar/${estudiante.matricula?string["0"]}">
                <button class="btn-error" type="submit">Eliminar</button>
            </form>
        </div>
    </body>
</html>