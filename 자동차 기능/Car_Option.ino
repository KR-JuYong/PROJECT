const uint8_t WATER_SENSOR {A8};
const uint8_t SERVOR_MOTOR {10U};
//깜박이
const uint8_t BUTTON_SW1{7U};
const uint8_t RELAY{12U};
const uint8_t LEFT_LED {2U};
const uint8_t RIGHT_LED {5U};
//자동 라이트
const uint8_t LIGHT_SENSOR{A3};
//const uint8_t LEDS[]{2U,3U,4U,5U,6U,7U,8U,9U};
//후진감지센서
#include "HCSR04.h"
#include "DHT.h"
HCSR04Sensor hcsr =HCSR04Sensor();
const uint8_t TRIGGER_PIN=40U;
const uint8_t ECHO_PIN=41U;
const uint8_t TEMPER_PIN {A2};
DHT dht =DHT(TEMPER_PIN, 11);
const uint8_t BUZZER_PIN=4U;
const uint8_t BUTTON_SW2{6U};

void setup() {
  // put your setup code here, to run once:
  Serial.begin(115200UL);
  Serial1.begin(9600UL);
  while(!Serial1);
  //와이퍼
  pinMode(WATER_SENSOR, INPUT);
  pinMode(SERVOR_MOTOR, OUTPUT);
  //깜박이
  pinMode(LEFT_LED, OUTPUT);
  pinMode(RIGHT_LED, OUTPUT);
  pinMode(RELAY, OUTPUT);
  pinMode(BUTTON_SW1, INPUT_PULLUP);
  //자동라이트
   pinMode(LIGHT_SENSOR,INPUT);
  /*for(auto&&i :LEDS){
    pinMode(i,OUTPUT);
  }*/
  //후진감지센서
  dht.begin();
  hcsr.begin(TRIGGER_PIN, ECHO_PIN);
  pinMode(BUTTON_SW2, INPUT_PULLUP);
  pinMode(BUZZER_PIN, OUTPUT);
}

void loop() {
  // put your main code here, to run repeatedly:
  //와이퍼
  uint16_t water_value = analogRead(WATER_SENSOR);
  water_value = constrain(water_value, 9, 650);
  uint16_t mapped_value = map(water_value, 0,650, 100, 50);
  Serial.print(F("Water value: "));
  Serial.println(water_value);
  if(water_value >5){ //비를 감지
    for(int i = 0; i<256; i +=10){
      analogWrite(SERVOR_MOTOR, i);
      delay(mapped_value);
    }
  }
  //깜박이
  static uint8_t button_count {0u};
  static uint8_t button_count2 {0u};
  static bool button_sw_state {false};
  digitalWrite(LEFT_LED, LOW);
  digitalWrite(RIGHT_LED, LOW);
  digitalWrite(RELAY, LOW); //초기화

  if(! digitalRead(BUTTON_SW1)){
    Serial.println(F("BUTTON-SWITCH-ON"));
    ++button_count;
    if(button_count ==3) button_count=0;
    if(button_count % 3 ==1){
      digitalWrite(LEFT_LED, HIGH);
      if(water_value >5){ //비를 감지
        for(int i = 0; i<256; i +=10){
        analogWrite(SERVOR_MOTOR, i);
        delay(mapped_value);
        }
      }
      Serial.println(F("LEFT_LED ON"));
      for(uint8_t i {0u}; i<5; ++i){
        digitalWrite(RELAY, HIGH);
        delay(500UL);
        digitalWrite(RELAY, LOW);
        delay(500UL);
      }
    }else if(button_count % 3 ==2){
      digitalWrite(RIGHT_LED, HIGH);
      Serial.println(F("RIGHT_LED ON"));
      for(uint8_t i {0u}; i<5; ++i){
        digitalWrite(RELAY, HIGH);
        delay(500UL);
        digitalWrite(RELAY, LOW);
        delay(500UL);
      }
    }else{
      digitalWrite(LEFT_LED, HIGH);
      digitalWrite(RIGHT_LED, HIGH);
      Serial.println(F("EMERGENCY LED ON"));
      for(uint8_t i {0u}; i<5; ++i){
        digitalWrite(RELAY, HIGH);
        delay(500UL);
        digitalWrite(RELAY, LOW);
        delay(500UL);
      }
    }
  }
  //자동라이트
  /*
  uint16_t light_value = analogRead(LIGHT_SENSOR);
  light_value=constrain(light_value, 596,1000);
  uint16_t mapped_light_value = map(light_value, 596,1000,0,202);
  Serial.print(F("Light value: "));
  Serial.println(light_value);
  Serial.print(F("Mapped_Light_value: "));
  Serial.println(mapped_light_value);
  delay(100UL);
  if(mapped_light_value>150){
    for(int i=0; i<8; ++i){
      digitalWrite(LEDS[i],LOW);
    }
  }
  if(mapped_light_value<=150){
    for(int i=0; i<8; ++i){
      digitalWrite(LEDS[i], HIGH);
    }
  }
  delay(50UL); */

  //후진감지센서
  if(! digitalRead(BUTTON_SW2)){
    Serial.println(F("후진"));
    if(dht.read()){ //온도 센서를 정상적으로 읽었다면
      float temperature  = dht.readTemperature();
      double result =0.0;
      hcsr.measureDistanceCm(temperature, &result); //Cm== 센치미터
      uint16_t mapped_Distance =map(result,0,400,0,100);
      Serial.println(mapped_Distance);
      if(result <=0 or result >=400.0){
        Serial.println(F("Out of bound"));
        Serial1.println(F("Out of bound"));
        return;
      }
      Serial.print(F("Distance: "));
      Serial.println(result);
      Serial1.print(F("Distance: "));
      Serial1.println(result);
      
      switch(mapped_Distance){
        case 0: 
          tone(BUZZER_PIN, 300.2);
          delay(10UL);
          noTone(BUZZER_PIN);
          
        case 1: 
          tone(BUZZER_PIN, 250.2);
          delay(400UL);
          noTone(BUZZER_PIN);
          delay(400UL);
        case 2: 
          tone(BUZZER_PIN, 200.2);
          delay(600UL);
          noTone(BUZZER_PIN);
          delay(600UL);
        case 3: 
          tone(BUZZER_PIN, 150.2);
          delay(800UL);
          noTone(BUZZER_PIN);
          delay(800UL);
        case 4: 
          tone(BUZZER_PIN, 100.2);
          delay(1000UL);
          noTone(BUZZER_PIN); 
          delay(1000UL); 
        }
    }
  }
}
