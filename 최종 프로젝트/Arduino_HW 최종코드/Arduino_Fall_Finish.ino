#include <Wire.h>
#include <MPU6050.h>
//부저
const uint8_t BUZZER_PIN = 5;

//전원 ON/OFF
const uint8_t BUTTON_SWITCH = 22;

//전원 상태 체크
bool button_state=false;
int button_count=0;

// MPU6050 객체를 생성합니다.
MPU6050 mpu;
bool mpu_state=false;
bool fall_state=false;
// LED 핀 번호를 설정합니다.
const int LED_PIN = 3;
// 낙상 임계값을 설정합니다. (단위: g)
const float FALL_THRESHOLD = 3.0;

void setup() {
  Serial.begin(9600);
  //블루투스 시리얼 MCU MAGA에서는 TX1, RX1 사용시 메가자체적으로 Serial1으로 사용.
  //아두이노 메가의 18번 핀과 19번 핀은 이미 시리얼 통신용 핀으로 설정되어있기 때문에 소프트웨어시리얼 설정을 하지 않아도 됩니다.
  Serial1.begin(9600);
  // I2C 통신을 시작합니다.
  Wire.begin();
  //MPU6050 센서를 초기화합니다.
  mpu.initialize(); //--> 범인
  // LED 핀을 출력 모드로 설정합니다.
  pinMode(LED_PIN, OUTPUT);
  //PULL-UP 스위치
  pinMode(BUTTON_SWITCH, INPUT);
  //부저
  pinMode(BUZZER_PIN, OUTPUT);

  // MPU6050 센서가 연결되었는지 확인합니다.
  if (mpu.testConnection()) {
    Serial.println("MPU6050 센서가 연결되었습니다.");
  } else {
    Serial.println("MPU6050 센서가 연결되지 않았습니다.");
    while (true); // 무한 루프
  }
}

void loop() {
  //버튼 상태 체크
  const bool button_sw_state = digitalRead(BUTTON_SWITCH);

  if(!button_sw_state){
    button_count++;
    button_state=true;
    Serial.println(button_count);
    delay(500UL);
  }
  //전원 ON
  if(button_count==1 && button_state==true){
    digitalWrite(LED_PIN, HIGH);
    Serial.println(F("LED ON"));
    mpu_state=true;
    button_state=false;
  }else if(button_count==2 && button_state==true){ //전원 OFF
    digitalWrite(LED_PIN, LOW);
    Serial.println(F("LED OFF"));
    mpu_state=false;
    button_count=0;
    button_state=false;
    fall_state=false;
    noTone(BUZZER_PIN);
  }
  //전원 ON -> 가속도 센서 작동
  if(mpu_state==true){
  // 가속도 값을 읽어옵니다.
  int16_t ax, ay, az;
  mpu.getAcceleration(&ax, &ay, &az);

  // 가속도 값을 g 단위로 변환합니다.
  double axg = (double)ax / 16384;
  double ayg = (double)ay / 16384;
  double azg = (double)az / 16384;

  // 가속도의 크기를 계산합니다.
  double a = sqrt(axg * axg + ayg * ayg + azg * azg);

  // 가속도의 크기를 시리얼 모니터에 출력합니다.
  Serial.print("가속도의 크기: ");
  Serial.println(a);

  Serial1.print("가속도의 크기: ");
  Serial1.println(a);

  // 가속도의 크기가 임계값보다 
  //크면 낙상으로 판단합니다.
  if (a > FALL_THRESHOLD) {
    Serial.println("낙상이 감지되었습니다!");
    //낙상감지시 블루투스로 데이터 전송 
    Serial1.write('w'); // 앱으로 'W' 신호 보내기
    Serial.println("낙상이 감지 되었습니다. 앱에 'w' 데이터를 전송합니다.");
    fall_state=true;
  // 100ms 간격으로 반복합니다.
  delay(100);
  }
  }
  // 낙상감지시 동작.
  switch(fall_state){
    case true: 
      // 부저
      tone(BUZZER_PIN,500);
      //낙상 감지 LED 깜빡거림
      digitalWrite(LED_PIN, HIGH);
      delay(200UL);
      digitalWrite(LED_PIN, LOW); // LED를 끕니다.
      delay(200UL);
      break;
    case false:
      noTone(BUZZER_PIN);
      break;
  }
  // 앱으로 부터 'c' 데이터 받아오기
  if(Serial1.available()){
    //데이터를 읽어서 저장
    char data = Serial1.read();
    Serial.print("앱으로 부터 '");
    Serial.print(data);
    Serial.println("' 데이터를 받았습니다.");
    //데이터가 'c'라면 notone, no_blink_led, 
    if(data=='c'){
      fall_state=false;
      button_state=true;
    }
  }
}
