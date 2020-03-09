from suds.client import Client

SOAP_URL = 'http://localhost:7777/ws/URLWebServiceImpl'
WSDL_URL = 'http://localhost:7777/ws/URLWebServiceImpl?wsdl'

client = Client(url=WSDL_URL, location=SOAP_URL)

while True:
    key = int(input('''\nPresione el numero de la opcion correspondiente

    (1) Ver los urls de un usuario
    (2) Acortar un url
    (3) Salir
    
    :'''))

    if key == 1:
        username = input('Indique el nombre de usuario: ')
        for url in client.service.getUrlsFromUser(username):
            print(url)
    
    elif key == 2:
        url = input('Indique la url: ')
        username = input('Indique el username (de ser vacío, se acortará de manera anónima): ')
        new_url = client.service.shortenUrl(url, username)
        print('New url: {0}'.format(new_url))

    elif key == 3:
        break

    input("\nPresione ENTER para continuar...")