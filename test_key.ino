#include <ESP8266WiFi.h>
#include <PubSubClient.h>
#include <OneWire.h>
#include <stdio.h>

#define ANALOG_INPUT A0
#define HUMID_POWER 5
#define TEMP_DATA 4
#define PHOTO_KEY 0
#define HUMID_KEY 2

const char* WiFi_SSID = "egor_redmi";
const char* WiFi_password =  "givemewifi";
const char* MQTT_brokerAddress = "3.238.21.184";
const int MQTT_port = 1883;
const char* MQTT_PubTopic = "data/var/test";
WiFiClient espClient;
PubSubClient client(espClient);
OneWire ds(TEMP_DATA); // Создаем объект OneWire для шины 1-Wire, с помощью которого будет осуществляться работа с датчиком

void setup() {
  Serial.begin(9600);
  Serial.println("Setup serial COMPLETED!");

  pinMode(HUMID_POWER, OUTPUT);
  pinMode(TEMP_DATA, INPUT);
  pinMode(PHOTO_KEY, OUTPUT);
  pinMode(HUMID_KEY, OUTPUT);
  digitalWrite(HUMID_POWER, LOW);
  digitalWrite(HUMID_KEY, LOW);
  digitalWrite(PHOTO_KEY, LOW);
  Serial.println("Setup pins COMPLETED!");

  WiFi.begin(WiFi_SSID, WiFi_password);
  while (WiFi.status() != WL_CONNECTED)
  {
    delay(500);
    Serial.println("Connecting to WiFi..");
  }
  Serial.print("Connected to WiFi :");
  Serial.println(WiFi.SSID());
  Serial.println("Setup WiFi COMPLETED!");

  client.setServer(MQTT_brokerAddress, MQTT_port);
  while (!client.connected())
  {
    Serial.println("Connecting to MQTT...");
    if (client.connect("ESP8266"))
    {
      Serial.println("connected");
    }
    else
    {
      Serial.print("failed with state ");
      Serial.println(client.state());
      delay(2000);
    }
  }
  Serial.println("Setup MQTT COMPLETED!");
}



float getTemp() {
  // Определяем температуру от датчика DS18b20
  byte data[2]; // Место для значения температуры

  ds.reset(); // Начинаем взаимодействие со сброса всех предыдущих команд и параметров
  ds.write(0xCC); // Даем датчику DS18b20 команду пропустить поиск по адресу. В нашем случае только одно устрйоство
  ds.write(0x44); // Даем датчику DS18b20 команду измерить температуру. Само значение температуры мы еще не получаем - датчик его положит во внутреннюю память

  delay(1000); // Микросхема измеряет температуру, а мы ждем.

  ds.reset(); // Теперь готовимся получить значение измеренной температуры
  ds.write(0xCC);
  ds.write(0xBE); // Просим передать нам значение регистров со значением температуры

  // Получаем и считываем ответ
  data[0] = ds.read(); // Читаем младший байт значения температуры
  data[1] = ds.read(); // А теперь старший

  // Формируем итоговое значение:
  //    - сперва "склеиваем" значение,
  //    - затем умножаем его на коэффициент, соответсвующий разрешающей способности (для 12 бит по умолчанию - это 0,0625)
  return ((data[1] << 8) | data[0]) * 0.0625;
}

int getHumid() {
  digitalWrite(HUMID_POWER, HIGH);
  digitalWrite(HUMID_KEY, HIGH);
  delay(500);

  int humidity = analogRead(ANALOG_INPUT);

  digitalWrite(HUMID_POWER, LOW);
  digitalWrite(HUMID_KEY, LOW);
  delay(500);
  return humidity;
}

int getPhoto() {
  digitalWrite(PHOTO_KEY, HIGH);
  delay(500);

  int photo = analogRead(ANALOG_INPUT);

  digitalWrite(PHOTO_KEY, LOW);
  delay(500);
  return photo;
}

void loop() {
  float temperature = getTemp();
  int humidity = getHumid();
  int photo = getPhoto();

  char resultsStr[50];
  sprintf(resultsStr, "%.1f,%d,%d", temperature, humidity, photo);

  Serial.print("Humidity value: ");
  Serial.println(humidity);
  Serial.print("Photo value: ");
  Serial.println(photo);
  Serial.print("Temperature value: ");
  Serial.println(temperature);
  client.publish(MQTT_PubTopic, resultsStr);
  delay(3000);
  client.loop();
}
