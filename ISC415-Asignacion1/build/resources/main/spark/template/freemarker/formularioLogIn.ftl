<!DOCTYPE html>
<html>
<head>
    <title>Log in</title>
    <style>
        h1 {
            color: blue;
            font-family: Arial;
            text-align: center;
        }
        input {
            width: 10rem;
            height: 1.5rem;
        }
        button {
            padding-left: 1rem;
            padding-right: 1rem;
        }
    </style>
</head>
<body>
<h1>Ejemplo de Log in</h1>
<form action="/procesarLogin/" method="post"  enctype="application/x-www-form-urlencoded">
    <input name="usuario" type="text" placeholder="Usuario"/><br/>
    <input name="contrasena" type="password" placeholder="Contrasena"/><br/>
    <button name="Enviar" type="submit">Enviar</button>
</form>
</body>
</html>