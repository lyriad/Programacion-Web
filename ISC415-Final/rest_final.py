import requests
import json

TOKEN = ''
headers = {}

while True:
    key = int(input('''\nPresione el numero de la opcion correspondiente

    (1) Solicitar un token
    (2) Configurar su token
    (3) Ver datos de un usuario, incluyendo sus urls
    (4) Acortar un url
    (5) Salir
    
    :'''))

    if key == 1:
        TOKEN = requests.post('http://isc415-final.herokuapp.com/token').json()['token']
        headers['token'] = TOKEN
        print("Su nuevo token: {0}".format(TOKEN))
    
    elif key == 2:
        new_token = input('Indique su nuevo token: ')
        headers['token'] = new_token
        print('Su token ha sido modificado')

    elif key == 3:
        username = input('Indique el username: ')
        response = requests.get('http://isc415-final.herokuapp.com/api/urls/' + username, headers=headers)
        print(json.dumps(response.json(), sort_keys=True, indent=4))

    elif key == 4:
        url = input('Indique la url: ')
        username = input('Indique el username (de ser vacío, se acortará de manera anónima): ')
        data = {'url': url, 'username': username}
        response = requests.post('http://isc415-final.herokuapp.com/api/urls/create', headers=headers, data=data)
        try:
            print(json.dumps(response.json(), sort_keys=True, indent=4))
        except:
            print('Error: ' + response.text)

    elif key == 5:
        break

    input("\nPresione ENTER para continuar...")