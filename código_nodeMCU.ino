//https://www.youtube.com/watch?v=V02StJVbslE

#include <ESP8266WiFi.h>
#include <PubSubClient.h> // https://github.com/knolleary/pubsubclient/releases/tag/v2.3
#include <ArduinoJson.h> // https://github.com/bblanchon/ArduinoJson/releases/tag/v5.0.7


//-------- Customise these values -----------
const char* ssid = "teste2";
const char* password = "12345678";

#define ORG "cd7gkk"
#define DEVICE_TYPE "padlockbeta"
#define DEVICE_ID "padlock01"
#define TOKEN "123456789"
//-------- Customise the above values --------

char server[] = ORG ".messaging.internetofthings.ibmcloud.com";
char authMethod[] = "use-token-auth";
char token[] = TOKEN;
char clientId[] = "d:" ORG ":" DEVICE_TYPE ":" DEVICE_ID;
const char eventTopic[] = "iot-2/evt/status/fmt/json";
const char cmdTopic[] = "iot-2/cmd/led/fmt/json";

int cont = 0;                         // Realiza uma contagem contínua
WiFiClient wifiClient;

void callback(char* topic, byte* payload, unsigned int payloadLength)
{
  Serial.print("Message arrived [");
  Serial.print(topic);
  Serial.print("] ");
  if ((char)payload[0] == '0')
  { // Caso receba o caractere 0
    digitalWrite(13, LOW);        // Desliga o LED
    Serial.println("LOW");
    Serial.println("pegou low");
  }
  if ((char)payload[0] == '1')
  { // Caso receba o caractere 1
    digitalWrite(13, HIGH);        // Liga o LED
    Serial.println("HIGH");
    Serial.println("pegou HIGh");
  }
}
PubSubClient client(server, 1883, callback, wifiClient);

void setup()
{
  Serial.begin(9600);                // Taxa de transmissão 9600 ou 115200 dBs
  pinMode(13, OUTPUT);              // LED13 que será acionado pelo IBM
  pinMode(2, OUTPUT);               // LED2 indica falha na conexão com a internet
  client.setCallback(callback);      // Ajusta a função de retorno
  wifiConnect();                     // Cria o void wifiConnect
  mqttConnect(); 
  // Cria o void mqttConnect
}
void wifiConnect()
{ // Função void wifiConnect
  Serial.print("Conectando a Rede ");
  Serial.print(ssid);                // Indica a Rede que o ESP32 irá se conectar
  WiFi.begin(ssid, password);        // Conecta ao ssid e o password configurado
  while (WiFi.status() != WL_CONNECTED)
  { // Enquanto estiver desconectado
    delay(500);                      // Aguarda 500 milissegundos
    Serial.print(".");
  }
  Serial.print("Wi-Fi conectado, Endereço de IP: ");
  Serial.println(WiFi.localIP());     // Indica o endereço de IP
}
void mqttConnect()
{ // Função void mqttConnect
  if (!!!client.connected())
  {
    Serial.print("Reconectando MQTT do cliente ");
    Serial.println(server);                       // Indica o endereço do servidor
    while (!!!client.connect(clientId, authMethod, token) )
    {
      Serial.print(".");
      delay(500);
    }
    if (client.subscribe(cmdTopic))
    {
      Serial.println("Resposta OK");              // Se a resposta for OK
      digitalWrite(2, LOW);                            // LED 2 OFF
    }
    else
    {
      Serial.println("Resposta FALHOU");   // Se a resposta falhar
      digitalWrite(2, HIGH);                          // LED 2 ON
    }
  }
}
void loop()
{
  if (!client.loop())
  {
    mqttConnect();
  }
  String payload = "{\"d\":{\"adc\":";      // Inicia uma String associando ao endereço
  payload += cont;                          // Atribui o valor de leitura de cont a String
  payload += "}}";                          // Finaliza a String
  Serial.print("Enviando payload: ");
  Serial.println(payload);                  // Escreve a String no monitor Serial
  client.publish(eventTopic, (char*) payload.c_str() );  // Publica a String
  cont ++;                                  // Aumenta em 1 o valor de cont
  delay(500);                               // Aguarda 500 milissegundos
}
