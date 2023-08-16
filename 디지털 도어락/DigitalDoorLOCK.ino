#include <Keypad.h>
//패스워드 설정
const String RIGHT_PASSWORD {String("1234ABCD")};
String password {String("")}; //버튼을 눌렀을 때 저장하기 위한 임시 String
//KEYPAD 조합
const char KEYS[][4] = { //4X4 배열 --> 2차원 배열 사용
  {'1', '2', '3', 'A'},
  {'4', '5', '6', 'B'},
  {'7', '8', '9', 'C'},
  {'*', '0', '#', 'D'}
};

uint8_t rowPins[] = {43U, 42U, 41U, 40U};  //colpins는 내림차순으로 설정.
uint8_t colPins[] = {44U, 45U, 46U, 47U}; //header 파일에 보면 rowpins는 오름차순
class Keypad keypad {Keypad((char*)KEYS, rowPins, colPins, 4, 4)}; // 4,4 --> 4X4이다.
//서보모터
const uint8_t SERVOR_MOTOR{10U};
//부저
const uint8_t BUZZER_PIN =4U;
int tone_value[]={524,660,784,524, 660, 524};
int wrong_count =0;
bool wrong_value =false;
void setup() {
  // put your setup code here, to run once:
  Serial.begin(115200UL); //메가속도이며 유선이기에 (TX0, RX0)을 사용하여 ->115200UL을 사용한다
  Serial1.begin(9600UL); //Bluetooth이므로 9600UL이고 --> 만약 모듈이 4.2 이상이라면 115200UL이 가능하다.
  //함수포인터에다가 함수를 넘겨줘야 함.
  keypad.addEventListener(keyEvent); //이벤트 등록 (C#)

  //서보모터
  pinMode(SERVOR_MOTOR, OUTPUT);
  //부저
  pinMode(BUZZER_PIN, OUTPUT);
}

void loop() {
  // put your main code here, to run repeatedly:
  if(keypad.getKeys()){ //버튼이 눌렸는지 아닌지 확인하는 메소드.
    keypad.getKey(); //이벤트 등록 함수를 호출.
  }
  if(wrong_count==3 && wrong_value){
    Serial1.println(F("잠금장치가 실행됩니다."));
    wrong_value=false;
    delay(60000UL); //1분 대기
    Serial1.println(F("잠금장치가 해제되었습니다."));
  }else if(wrong_count==5 && wrong_value){
    wrong_value=false;
    Serial1.println(F("잠금장치가 실행됩니다."));
    delay(300000UL);
    Serial1.println(F("잠금장치가 해제되었습니다."));
  }
}

void keyEvent(char event_key){ //내가 버튼을 누를 때 마다 그 객체가 되는 char값이 넘어간다.
  switch(keypad.getState()){
    case PRESSED:
      Serial.print(F("Enter : "));
      Serial.println(event_key);
      Serial1.print(F("Enter : "));
      Serial1.println(event_key);
      delay(10UL); //버튼 입력을 받기위한 딜레이
      tone(BUZZER_PIN,670);
      delay(150UL);
      noTone(BUZZER_PIN);
      switch(event_key){
        case '*':
          check_password(); //패스워드를 체크하기 위한 함수 호출.
          break;
        case '#':
         password = ""; //입력한 패스워드 리셋.
         Serial.println(F("RESET"));
         Serial1.println(F("RESET")); //리셋 확인 메세지
         break;
        default:
          password.concat(event_key); //임시 패스워드 문자열에 문자가 연속 저장됨. --> concat
      }
  }
}

void check_password(){
  if(RIGHT_PASSWORD == password){
    Serial.println(F("PIN is right"));
    Serial1.println(F("도어락이 열렸습니다."));
    password = ""; //리셋
    wrong_count=0;
    wrong_value==false;
    for(int i=0; i<=3; ++i){
      tone(BUZZER_PIN, tone_value[i]);
      delay(150UL);
      noTone(BUZZER_PIN);
    }
     for(int i{0}; i<256; i+=10){
      analogWrite(SERVOR_MOTOR, 254);
      delay(100UL);
    }
    delay(3000UL);
    for(int i{0}; i<256; i+=10){
      analogWrite(SERVOR_MOTOR, 44);
      delay(100UL);
    }
  }else{
    Serial.println(F("PIN is wrong"));
    Serial.println(F("도어락이 열리지 않았습니다."));
    password = "";
    wrong_count++;
    wrong_value=true;
    Serial1.print(F("비밀번호를 "));
    Serial1.print(wrong_count);
    Serial1.println(F("회 틀렸습니다."));
    for(int i=4; i<=5; ++i){
      tone(BUZZER_PIN, tone_value[i] );
      delay(150UL);
    }
    noTone(BUZZER_PIN);
  }
}