#include <LiquidCrystal_I2C.h>
#include "DHT.h"
//RGB == 건물 내부의 LED 표현
const uint8_t RED = {13U};
const uint8_t GREEN = {12U};
const uint8_t BLUE = {11U};
bool rgb =false;
//BUTTON == LED를 껐다 켰다 할 수 있는 장치.
const uint8_t LED_BUTTON = {10U};
int led_count =0;

//광센서 == 건물 내부가 어두워 진다면 비상 대피로가 점점 밝아지는 역할
const uint8_t LIGHT_SENSOR = {A1};

//DHT11 == 건물 내부 화제 감지
class DHT dht(A0, 11);
int fire_count =0;
const uint8_t LED = {3U}; //정상 작동중인지 확인

//부저 == 화제 감지 및 지진 감지 이후 비상벨 울리기
const uint8_t BUZZER = {2U};
const uint8_t BUZZER_BUTTON = {4U};

//지진감지 센서 == 지진 감지
const uint8_t TILT_SW = {22U};
int tilt_count=0;

//LCD
class LiquidCrystal_I2C lcd(0x27, 16, 2);
const uint8_t I2C_BUTTON = {9U};
int i2c_count = 0;
const uint8_t human1[]{
  0b00011111, 0b00010000, 0b00010000, 0b00010000, 
  0b00010000, 0b00010011, 0b00010111, 0b00011101
};
const uint8_t human2[]{
  0b00011111, 0b00000001, 0b00011001, 0b00011001,
  0b00000001, 0b00011001, 0b00011101, 0b00010111
};
const uint8_t human3[]{
  0b00010001, 0b00010001, 0b00010011, 0b00010010,
  0b00010010, 0b00011110, 0b00011110, 0b00010000
};
const uint8_t human4[]{
  0b00010011, 0b00010001, 0b00011001, 0b00001001,
  0b00000101, 0b00000101, 0b00000011, 0b00000011
};

const uint8_t left1[]{
  0b00000000, 0b00000000, 0b00000000, 0b00000000, 
  0b00000010, 0b00000110, 0b00001111, 0b00011111
};
const uint8_t left2[]{
  0b00000000, 0b00000000, 0b00000000, 0b00000000,
  0b00000000, 0b00000000, 0b00011111, 0b00011111
};
const uint8_t left3[]{
  0b00011111, 0b00001111, 0b00000110, 0b00000010,
  0b00000000, 0b00000000, 0b00000000, 0b00000000
};
const uint8_t left4[]{
  0b00011111, 0b00011111, 0b00000000, 0b00000000,
  0b00000000, 0b00000000, 0b00000000, 0b00000000
};

const uint8_t right1[]{
  0b00000000, 0b00000000, 0b00000000, 0b00000000,
  0b00000000, 0b00000000, 0b00011111, 0b00011111
};
const uint8_t right2[]{
  0b00000000, 0b00000000, 0b00000000, 0b00000000,
  0b00010000, 0b00011100, 0b00011110, 0b00011111
};
const uint8_t right3[]{
  0b00011111, 0b00011111, 0b00000000, 0b00000000,
  0b00000000, 0b00000000, 0b00000000, 0b00000000
};
const uint8_t right4[]{
  0b00011111, 0b00011110, 0b00011000, 0b00010000,
  0b00000000, 0b00000000, 0b00000000, 0b00000000
};

const uint8_t left_right1[]{
  0b00000000, 0b00000000, 0b00000000, 0b00000000,
  0b00000000, 0b00000100, 0b00001100, 0b00011111
};
const uint8_t left_right2[]{
  0b00011111, 0b00001100, 0b00000100, 0b00000000,
  0b00000000, 0b00000000, 0b00000000, 0b00000000
};
const uint8_t left_right3[]{
  0b00000000, 0b00000000, 0b00000000, 0b00000000,
  0b00000000, 0b00000100, 0b00000110, 0b00011111
};
const uint8_t left_right4[]{
  0b00011111, 0b00000110, 0b00000100, 0b00000000,
  0b00000000, 0b00000000, 0b00000000, 0b00000000
};

void setup() {
  // put your setup code here, to run once:
  Serial.begin(115200UL);
  Serial1.begin(9600UL);
  //RGB
  pinMode(RED, OUTPUT);
  pinMode(GREEN, OUTPUT);
  pinMode(BLUE, OUTPUT);

  //BUTTON
  pinMode(LED_BUTTON, INPUT_PULLUP);

  //광센서
  pinMode(LIGHT_SENSOR, INPUT);

  //DHT11
  dht.begin();
  pinMode(LED, OUTPUT);

  //부저
  pinMode(BUZZER, OUTPUT);
  pinMode(BUZZER_BUTTON, INPUT_PULLUP);

  //지진감지
  pinMode(TILT_SW, INPUT_PULLUP);

  //LCD
  lcd.init(); //LCD 초기화
  lcd.home();
  //lcd.backlight();
  lcd.clear(); //화면을 한번 지워주세요.
  pinMode(I2C_BUTTON, INPUT_PULLUP);
}

void loop() {
  // put your main code here, to run repeatedly:
  //RGB && BUTTON
  bool button_value = digitalRead(LED_BUTTON);
  if(!button_value){
    led_count++;
    rgb=true;
    delay(100UL);
  }else{
    digitalWrite(RED, LOW);
    digitalWrite(GREEN, LOW);
    digitalWrite(BLUE, LOW);;
  }
  if(led_count==1 && rgb==true){
    digitalWrite(RED, HIGH);
    digitalWrite(GREEN, HIGH);
    digitalWrite(BLUE, HIGH);
  }else if(led_count ==2){
    digitalWrite(RED, LOW);
    digitalWrite(GREEN, LOW);
    digitalWrite(BLUE, LOW);;
    led_count=0;
    rgb=false;
  }

  //조도 센서 ==> LCD랑 조합
  uint16_t light_value = analogRead(LIGHT_SENSOR);
  light_value = constrain(light_value, 380, 957);
  uint16_t mapped_light_value = map(light_value, 380, 960, 0 ,290);
  delay(200UL);
  Serial.print(F("Mapped_Light_value: "));
  Serial.println(mapped_light_value);
  if(mapped_light_value <=200){
    lcd.backlight();
    lcd.createChar(0, human1);
    lcd.createChar(1, human2);
    lcd.createChar(2, human3);
    lcd.createChar(3, human4);
    lcd.createChar(4, left1);
    lcd.createChar(5, left2);
    lcd.createChar(6, left3);
    lcd.createChar(7, left4);
    //사람 표시등
    lcd.setCursor(7,0);
    lcd.write(byte(0));
    lcd.setCursor(8,0);
    lcd.write(byte(1));
    lcd.setCursor(7,1);
    lcd.write(byte(2));
    lcd.setCursor(8,1);
    lcd.write(byte(3));
    //좌측 표시등
    lcd.setCursor(4,0);
    lcd.write(byte(4));
    lcd.setCursor(5,0);
    lcd.write(byte(5));
    lcd.setCursor(4,1);
    lcd.write(byte(6));
    lcd.setCursor(5,1);
    lcd.write(byte(7));
  }else if(tilt_count>=3 || fire_count >=5){
    lcd.backlight();
  }else{
      lcd.noBacklight();
  }

  //화재 감지 및 지진 감지 ==> 부저 울리기
  bool tilt_state = digitalRead(TILT_SW);
  bool buzzer_button = digitalRead(BUZZER_BUTTON);
  float temperature = 0.0F;
  float percentHumidity= 0.0F;
  if(tilt_state){
    //지진 감지
    tilt_count++;
    Serial.print(F("지진 감지 횟수: "));
    Serial.println(tilt_count);
    delay(300UL);
    if(tilt_count >= 3){
      digitalWrite(RED, LOW);
      digitalWrite(GREEN, LOW);
      digitalWrite(BLUE, LOW);;
      rgb=false;
      lcd.backlight();
      delay(10UL);
      Serial1.println(F("지진이 발생하였습니다."));
      Serial1.println(F("튼튼한 탁자의 아래로 들어가 탁자 다리를 꼭 잡고 몸을 보호하세요."));
      Serial1.println(F("탁자 아래와 같이 피할 곳이 없을 때에는 방석 등으로 머리를 보호하세요."));
      for(int i=0; i<45; i++){
        for(int j=480; j<=700; j++ ){
          tone(BUZZER, j);
          delay(10UL);
        }
        for(int k=700; k>=480; --k){
          tone(BUZZER, k);
          delay(10UL);
        }
      }
      noTone(BUZZER);
      tilt_count=0;
      delay(100UL);
    }
  }
  //화제 감지
  if(dht.read()){
    digitalWrite(LED,HIGH);
    temperature =dht.readTemperature();
    percentHumidity = dht.readHumidity();
    float heat = dht.computeHeatIndex(temperature, percentHumidity);
    Serial.print(F("Heat: "));
    Serial.println(String(heat));
    if(heat >= 100.0){
      fire_count++;
      delay(500UL);
      if(fire_count >= 5){
        digitalWrite(RED, LOW);
        digitalWrite(GREEN, LOW);
        digitalWrite(BLUE, LOW);;
        rgb=false;
        lcd.backlight();
        delay(10UL);
        Serial1.println(F("화재가 발생했습니다."));
        Serial1.println(F("주변 사람들에게 알리며 신속히 대피해주세요."));
        Serial1.println(F("대피할 때는 엘리베이터를 절대 이용하지 않고 계단을 통하여 지상으로 안전하게 대피하세요."));
        Serial1.println(F("대피가 어려운 경우에는 창문으로 구조요청을 하거나 대피공간 또는 경량칸막이를 이용하여 대피하세요."));
        for(int i=0; i<45; i++){
          if(!buzzer_button){
            noTone(BUZZER);
            tilt_count=0;
            fire_count=0;
            delay(100UL);
          }
          for(int j=480; j<=700; j++ ){
            tone(BUZZER, j);
            delay(10UL);
          }
          for(int k=700; k>=480; --k){
            tone(BUZZER, k);
            delay(10UL);
          }
        }
        noTone(BUZZER);
        fire_count=0;
        delay(100UL);
      }
    }else{
      delay(350UL);
      fire_count=0;
    }
  }
  /*
  //LCD
  //사람 표시등
  bool i2c_button = digitalRead(I2C_BUTTON);
  if(!i2c_button){
    i2c_count++;
    lcd.clear();
    delay(100UL);
    lcd.backlight();
  }
  if(i2c_count==1){
    lcd.createChar(0, human1);
    lcd.createChar(1, human2);
    lcd.createChar(2, human3);
    lcd.createChar(3, human4);
    lcd.createChar(4, left1);
    lcd.createChar(5, left2);
    lcd.createChar(6, left3);
    lcd.createChar(7, left4);
    //사람 표시등
    lcd.setCursor(7,0);
    lcd.write(byte(0));
    lcd.setCursor(8,0);
    lcd.write(byte(1));
    lcd.setCursor(7,1);
    lcd.write(byte(2));
    lcd.setCursor(8,1);
    lcd.write(byte(3));
    //좌측 표시등
    lcd.setCursor(4,0);
    lcd.write(byte(4));
    lcd.setCursor(5,0);
    lcd.write(byte(5));
    lcd.setCursor(4,1);
    lcd.write(byte(6));
    lcd.setCursor(5,1);
    lcd.write(byte(7));
  }else if(i2c_count==2){
    lcd.createChar(0, human1);
    lcd.createChar(1, human2);
    lcd.createChar(2, human3);
    lcd.createChar(3, human4);
    lcd.createChar(4, right1);
    lcd.createChar(5, right2);
    lcd.createChar(6, right3);
    lcd.createChar(7, right4);
    //사람 표시등
    lcd.setCursor(7,0);
    lcd.write(byte(0));
    lcd.setCursor(8,0);
    lcd.write(byte(1));
    lcd.setCursor(7,1);
    lcd.write(byte(2));
    lcd.setCursor(8,1);
    lcd.write(byte(3));
    //우측 표시등
    lcd.setCursor(10,0);
    lcd.write(byte(4));
    lcd.setCursor(11,0);
    lcd.write(byte(5));
    lcd.setCursor(10,1);
    lcd.write(byte(6));
    lcd.setCursor(11,1);
    lcd.write(byte(7));
  }else if(i2c_count==3){
    lcd.createChar(0, human1);
    lcd.createChar(1, human2);
    lcd.createChar(2, human3);
    lcd.createChar(3, human4);
    lcd.createChar(4, left_right1);
    lcd.createChar(5, left_right2);
    lcd.createChar(6, left_right3);
    lcd.createChar(7, left_right4);
    //사람 표시등
    lcd.setCursor(7,0);
    lcd.write(byte(0));
    lcd.setCursor(8,0);
    lcd.write(byte(1));
    lcd.setCursor(7,1);
    lcd.write(byte(2));
    lcd.setCursor(8,1);
    lcd.write(byte(3));
    //좌&우 표시등
    lcd.setCursor(5,0);
    lcd.write(byte(4));
    lcd.setCursor(5,1);
    lcd.write(byte(5));
    lcd.setCursor(10,0);
    lcd.write(byte(6));
    lcd.setCursor(10,1);
    lcd.write(byte(7));
  }else if(i2c_count==4){
    i2c_count=0;
  }*/
}
