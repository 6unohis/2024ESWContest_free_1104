// 임의의 로드셀 무게 추가 -> python으로 전송
#include<Constants.h>
#include<StepperMulti.h>
#include<Servo.h>
#include<TimerOne.h>
#include<CUP.h>
#include<Waterpump.h>
#include<Queue.h>
#include<HX711.h>

/* Hardware definition */
Waterpump waterpump(WATERPUMP_EN, WATERPUMP_PIN_1, WATERPUMP_PIN_2);

StepperMulti VERTICAL_STEPPER(SPR, 25, 29, 27, 31);
StepperMulti HORIZONTAL_STEPPER(SPR, 24, 30, 28, 26);

Queue queue(QUEUE_SPEED, QUEUE_MOTOR_EN, QUEUE_MOTOR_PIN_1, QUEUE_MOTOR_PIN_2);

CUP CurrentCUP(0, 0, 0);
CUP NextCUP(0, 0, 0);

HX711 LoadCellScale;

/* Servo motor definition */
Servo HORIZONTAL_STEPPER_wrist;
Servo HORIZONTAL_STEPPER_finger;
Servo VERTICAL_STEPPER_finger;

Servo remove_lid_servo;

Servo small_cup_servo;
Servo regular_cup_servo;
Servo large_cup_servo;

Servo QUEUE_door;

volatile int reset_horizontal_direct;

/* 수직, 수평 이동 그리퍼 위치 변수 */
volatile float current_horizontal_pos;
volatile float current_vertical_pos;

volatile float next_horizontal_pos;
volatile float next_vertical_pos;

/* 컵 적재 위치 저장 변수 */
volatile int horizontal_pos_cup;

/* 컵 정보 volatile 변수 */
volatile int cup_size, cup_size_entrance, holder_exist;
volatile int cup_cnt[5];

/* current main state, queue state */
volatile int main_current_state = RESET;
volatile int queue_current_state = QUEUE_STOP;

/* last main state, queue state */
volatile int main_last_state;
volatile int queue_last_state;

/* 현재 처리 중인 컵 정보 저장 변수 */
volatile int current_cup_size = 0;
volatile int current_entrance_size = 0;
volatile int current_holder_exist = 0;

/* 처리 예정인 컵 정보 저장 변수 */
volatile int next_cup_size = 0;
volatile int next_entrance_size = 0;
volatile int next_holder_exist = 0;

/* 인터럽트 루틴 GLITCH */
volatile int end_time = 0, start_time = 0;

/* reset state 판별 변수 */
volatile int reset_flag = 0;
volatile int no_interrupt = 0;
volatile int all_stop = 0;

/* 시리얼 통신 송수신 데이터 저장 변수 */
volatile byte tx_rx_data = 0;

/* 스텝 모터 초기화 함수 */
/* 이를 통해 스텝 모터의 과부하를 방지할 수 있음 */
void step_clear(){
    digitalWrite(24, LOW);
    digitalWrite(26, LOW);
    digitalWrite(28, LOW);
    digitalWrite(30, LOW);

    digitalWrite(25, LOW);
    digitalWrite(27, LOW);
    digitalWrite(29, LOW);
    digitalWrite(31, LOW);
    
    digitalWrite(40, LOW);
    digitalWrite(42, LOW);
    digitalWrite(44, LOW);
    digitalWrite(46, LOW);
}

/* 실수 단위의 cm to 스텝 모터의 step 변환 함수 */
int  cm2step(float cm){
    return (SPR / CmPerCycle * cm);
}

/* 사이즈별 적재 컵 개수 확인 및 적재함 -> 보관함 이동 함수 */
void cleaning_cup(){
    if(cup_cnt[1] >= small_cnt_threshold || cup_cnt[2] >= regular_cnt_threshold || cup_cnt[3] >= large_cnt_threshold){
        if(cup_cnt[1] >= small_cnt_threshold){
          small_cup_servo.write(cleaning_cup_angle);
          cup_cnt[1] = 0;
        }
        if(cup_cnt[2] >= regular_cnt_threshold){
          regular_cup_servo.write(cleaning_cup_angle);
          cup_cnt[2] = 0;
        }
        if(cup_cnt[3] >= large_cnt_threshold){
          large_cup_servo.write(cleaning_cup_angle);
          cup_cnt[3] = 0;
        }
        delay(1500);
    }

    small_cup_servo.write(default_cup_angle);
    regular_cup_servo.write(default_cup_angle);
    large_cup_servo.write(default_cup_angle);
}

/* 세척 과정 이후 컵 내부 물기 제거를 위한 함수 */
void wash_post_process(){
    HORIZONTAL_STEPPER_wrist.write(HORIZONTAL_STEPPER_wrist_wash - 30);
    delay(400);
    HORIZONTAL_STEPPER_wrist.write(HORIZONTAL_STEPPER_wrist_wash);
    delay(400);
    HORIZONTAL_STEPPER_wrist.write(HORIZONTAL_STEPPER_wrist_wash - 30);
    delay(400);
    HORIZONTAL_STEPPER_wrist.write(HORIZONTAL_STEPPER_wrist_wash);
    delay(400);
    HORIZONTAL_STEPPER_wrist.write(HORIZONTAL_STEPPER_wrist_wash - 30);
    delay(400);
    HORIZONTAL_STEPPER_wrist.write(HORIZONTAL_STEPPER_wrist_wash);
    delay(400);
    HORIZONTAL_STEPPER_wrist.write(HORIZONTAL_STEPPER_wrist_wash - 30);
    delay(400);
    HORIZONTAL_STEPPER_wrist.write(HORIZONTAL_STEPPER_wrist_wash);
    delay(400);
    HORIZONTAL_STEPPER_wrist.write(HORIZONTAL_STEPPER_wrist_wash - 30);
    delay(400);
    HORIZONTAL_STEPPER_wrist.write(HORIZONTAL_STEPPER_wrist_wash);
}

/* 수평 이동 그리퍼 이동 함수 */
void HORIZONTAL_STEPPER_move(){
    int steps = abs(cm2step(current_horizontal_pos - next_horizontal_pos));

    if(current_horizontal_pos > next_horizontal_pos){
        for(int i = 0; i < steps; i++){
            HORIZONTAL_STEPPER.setStep(1);
            delayMicroseconds(HORIZONTAL_STEPPER_DELAY);
        }
    }else{
        for(int i = 0; i < steps; i++){
            HORIZONTAL_STEPPER.setStep(-1);
            delayMicroseconds(HORIZONTAL_STEPPER_DELAY);
        }
    }

    current_horizontal_pos = next_horizontal_pos;
}

/* 수직 이동 그리퍼 이동 함수 */
void VERTICAL_STEPPER_move(){
    int steps = abs(cm2step(current_vertical_pos - next_vertical_pos));

    if(current_vertical_pos > next_vertical_pos){ // up
        for(int i = 0; i < steps; i++){
            VERTICAL_STEPPER.setStep(1);
            delayMicroseconds(VERTICAL_STEPPER_DELAY);
        }
    }else{ // down
        for(int i = 0; i < steps; i++){ 
            VERTICAL_STEPPER.setStep(-1);
            delayMicroseconds(VERTICAL_STEPPER_DELAY);
        }
    }

    current_vertical_pos = next_vertical_pos;
}

/* 수직 이동 그리퍼 서보 모터 작동 함수 */
/* 토크가 강한 서보 모터이기에 작은 각도 단위로 Servo 라이브러리를 활용하여 최적화 */
void VERTICAL_STEPPER_finger_move(int src, int dst){
    if(src < dst){
        for(int i = src; i < dst; i++){
            VERTICAL_STEPPER_finger.write(i);
            delay(10);
        }
    }else{
        for(int i = src; i > dst; i--){
            VERTICAL_STEPPER_finger.write(i);
            delay(10);
        }
    }
}

/* 뚜껑 제거 장치 돌출 함수 */
void remove_lid_servo_in(){
    remove_lid_servo.write(REMOVE_LID_ANGLE[current_entrance_size]);
}

/* 뚜껑 제거 장치 초기화 함수 */
void remove_lid_servo_reset(){
    remove_lid_servo.write(REMOVE_LID_ANGLE[0]);
}

/* 서보 모터 할당 함수 */
void servo_attach(){
    HORIZONTAL_STEPPER_wrist.attach(HORIZONTAL_STEPPER_wrist_pin);
    HORIZONTAL_STEPPER_finger.attach(HORIZONTAL_STEPPER_finger_pin);
    VERTICAL_STEPPER_finger.attach(VERTICAL_STEPPER_finger_pin);

    remove_lid_servo.attach(remove_lid_servo_pin);
    small_cup_servo.attach(small_cup_servo_pin);
    regular_cup_servo.attach(regular_cup_servo_pin);
    large_cup_servo.attach(large_cup_servo_pin);

    QUEUE_door.attach(QUEUE_DOOR_PIN);
}

/* 서보 모터 할당 해제 함수 */
/* 위 servo_attach와 servo_detach 함수를 통해 각 서보 모터의 과부하 방지 */
void servo_detach(){
    HORIZONTAL_STEPPER_wrist.detach();
    HORIZONTAL_STEPPER_finger.detach();

    remove_lid_servo.detach();
    small_cup_servo.detach();
    regular_cup_servo.detach();
    large_cup_servo.detach();

    QUEUE_door.detach();
}

/* 큐체인 위치 조정 함수 */
/* 리미트 스위치 기준 역방향 이동 후 일부 정방향 이동 */
void queue_positioning(int speed){
    no_interrupt = 0;
    analogWrite(QUEUE_MOTOR_EN, speed + 20);
    digitalWrite(QUEUE_MOTOR_PIN_1, LOW);
    digitalWrite(QUEUE_MOTOR_PIN_2, HIGH);
    
    no_interrupt = 0;
    delay(200);
            
    no_interrupt = 0;
    while(!no_interrupt){
        analogWrite(QUEUE_MOTOR_EN, speed - 20);
        digitalWrite(QUEUE_MOTOR_PIN_1, HIGH);
        digitalWrite(QUEUE_MOTOR_PIN_2, LOW);
    }
    queue.Off();
    delay(300);
    no_interrupt = !no_interrupt;
}

/* 수평 이동 그리퍼 토크 할당 함수 */
void horizontal_step_hold(){
    digitalWrite(24, HIGH);
    digitalWrite(26, LOW);
    digitalWrite(28, LOW);
    digitalWrite(30, HIGH);
}

/* 수직 이동 그리퍼 토크 할당 함수 */
void vertical_step_hold(){
    digitalWrite(25, HIGH);
    digitalWrite(27, LOW);
    digitalWrite(29, LOW);
    digitalWrite(31, HIGH);
}

/* 리미트 스위치 동작에 따른 인터럽트 루틴 함수 */
/* 수직, 수평 이동 그리퍼에 대한 처리가 주가 되며 state를 변환하여 다른 동작이 이루어지도록 함 */
void reset_state_change(){
    end_time = millis();
    step_clear();
    if(end_time - start_time > GLITCH){
        

        if(main_current_state == RESET_HORIZONTAL_INTERRUPT){
            main_current_state = RESET_HORIZONTAL_DEFAULT;
        }else if(main_current_state == RESET_VERTICAL_DEFAULT){
            main_current_state = STARTING;
        }else if(main_current_state == HORIZONTAL_HOLD_CUP){
            main_current_state = HORIZONTAL_WASTE_CUP;
        }
    }
    start_time = end_time;
}

/* 리셋 버튼 동작 버튼 */
/* ONLY WAITING을 통해 전체 기기 작동을 일시정지하며, 이후 한 번 더 동작하는 경우 reset state 진행 */
void reset_interrupt(){
    end_time = millis();
    step_clear();
    if(end_time - start_time > GLITCH){
        
        if(main_current_state != ONLY_WAITING){
            reset_flag = 1;
            main_current_state = ONLY_WAITING;
        }else{
            main_current_state = RESET;
        }
    }
    start_time = end_time;
}

/* 큐체인 리미트 스위치 인터럽트 루틴 */
void queue_state_change(){
    end_time = millis();
    queue.Off();
    delayMicroseconds(1000);

    if(end_time - start_time > GLITCH && !no_interrupt && (queue_current_state == QUEUE_STOP || main_current_state == RESET_SERVO)) no_interrupt = !no_interrupt; 

    if(main_current_state == RESET_QUEUE) main_current_state = RESET_SERVO;
    else{
        if(queue_current_state == QUEUE_GO) queue_current_state = QUEUE_STOP;
        else if(queue_current_state == QUEUE_STOP) queue_current_state = QUEUE_GO;
    }
    start_time = end_time;
}

void setup(){
    Serial.begin(9600);

    HORIZONTAL_STEPPER.setSpeed(HORIZONTAL_STEPPER_SPEED);
    VERTICAL_STEPPER.setSpeed(VERTICAL_STEPPER_SPEED);
    
    servo_attach();

    queue_current_state = QUEUE_READY_CUP;

    pinMode(ECHO_PIN, INPUT);
    pinMode(TRIG_PIN, OUTPUT);

    /* 로드셋 활성화 및 스케일 조정 */
    LoadCellScale.begin(LOADCELL_DATA_PIN, LOADCELL_CLK_PIN);
    LoadCellScale.set_scale(18.575);              
    LoadCellScale.tare(20);

    /* 인터럽트 핀에 대한 인터럽트 루틴 할당 */
    attachInterrupt(digitalPinToInterrupt(RESET_PIN), reset_interrupt, CHANGE);
    attachInterrupt(digitalPinToInterrupt(HORIZONTAL_STEPPER_reset_pin), reset_state_change, RISING);
    attachInterrupt(digitalPinToInterrupt(VERTICAL_STEPPER_reset_pin), reset_state_change, RISING);
    attachInterrupt(digitalPinToInterrupt(QUEUE_INTERRUPT_PIN), queue_state_change, RISING);
}

void loop(){
    // 시리얼 데이터에 state 저장 및 전송 
    tx_rx_data = 0x00;
    tx_rx_data = main_current_state << 3;
    Serial.println(tx_rx_data);

    step_clear();

    // 동일한 state 반복 시 오류 발생 -> reset state 선언
    if(main_current_state != STARTING && main_current_state == main_last_state) main_current_state = RESET;    
    main_last_state = main_current_state;

    switch(main_current_state){
        // 대기 state
        case ONLY_WAITING: 
            while(main_current_state == ONLY_WAITING){}

        // reset state 시작부
        case RESET: 
            reset_flag = 0;
            for(int i = 0; i < 4; i++) cup_cnt[i] = 0;

            main_current_state = RESET_QUEUE;
            break;
        
        // 큐체인 초기화 state
        case RESET_QUEUE:
            while(main_current_state == RESET_QUEUE){
                analogWrite(QUEUE_MOTOR_EN, 100);
                digitalWrite(QUEUE_MOTOR_PIN_1, LOW);
                digitalWrite(QUEUE_MOTOR_PIN_2, HIGH);
            }
            break;

        // 서보 모터 초기화 state
        case RESET_SERVO:
        
            delay(50);
            
            queue_positioning(100); // 이동한 큐체인 위치 조정
            
            analogWrite(QUEUE_MOTOR_EN, 80);
            digitalWrite(QUEUE_MOTOR_PIN_1, LOW);
            digitalWrite(QUEUE_MOTOR_PIN_2, HIGH);
            delay(100);

            queue.Off();

            delay(1500);
        
            queue_current_state = QUEUE_READY_CUP;

            // 할당된 모든 서보 모터 초기값 호출 및 할당 해제
            cleaning_cup();
            remove_lid_servo_reset();
            HORIZONTAL_STEPPER_wrist.write(HORIZONTAL_STEPPER_wrist_default);
            HORIZONTAL_STEPPER_finger.write(HORIZONTAL_STEPPER_finger_default);
            VERTICAL_STEPPER_finger_move(VERTICAL_STEPPER_finger_minimum, VERTICAL_STEPPER_finger_default);

            delay(1000);
            QUEUE_door.write(20);
            delay(300);
            
            servo_detach();

            main_current_state = RESET_HORIZONTAL_INTERRUPT;
            break;

        // 수평 이동 그리퍼 인터럽트 발생 state
        case RESET_HORIZONTAL_INTERRUPT:
            current_horizontal_pos = horizontal_pos_reset;
            while(main_current_state == RESET_HORIZONTAL_INTERRUPT){
                HORIZONTAL_STEPPER.setStep(1);
            }

            break;

        // 수평 이동 그리퍼 초기 위치 이동 state
        case RESET_HORIZONTAL_DEFAULT:
            next_horizontal_pos = horizontal_pos_default;
            HORIZONTAL_STEPPER_move();

            main_current_state = RESET_VERTICAL_DEFAULT;
            break;

        // 수직 이동 그리퍼 인터럽트 발생 및 초기 위치 이동 state
        case RESET_VERTICAL_DEFAULT:
            for(int i = 0; i < 100; i++){
                VERTICAL_STEPPER.setStep(-1);
                delayMicroseconds(VERTICAL_STEPPER_DELAY);
            }

            current_vertical_pos = vertical_pos_default;
            while(main_current_state == RESET_VERTICAL_DEFAULT){
                VERTICAL_STEPPER.setStep(1);
            }
            break;

        // main state 시작 state
        case STARTING:
            servo_attach();
            switch(queue_current_state){
                // 큐체인이 이동할 때의 queue state
                case QUEUE_GO:
                    QUEUE_door.write(20);
                    CurrentCUP.modifyCupInfo(NextCUP.getCupSize(), NextCUP.getEntranceSize(), NextCUP.getExistHolder()); // Next Cup -> Current Cup 할당

                    current_cup_size = CurrentCUP.getCupSize();
                    current_entrance_size = CurrentCUP.getEntranceSize();
                    current_holder_exist = CurrentCUP.getExistHolder();

                    if(!reset_flag){
                        if(current_cup_size){ // 처리 예정인 컵이 존재하는 경우 수직 이동 그리퍼는 컵을 잡을 준비
                            servo_attach();
                            VERTICAL_STEPPER_finger_move(VERTICAL_STEPPER_finger_default, VERTICAL_STEPPER_finger_ready_cup[current_entrance_size]);
                        }                        

                        while(queue_current_state == QUEUE_GO && !reset_flag){ // 큐체인 회전
                            digitalWrite(QUEUE_MOTOR_PIN_1, LOW);
                            digitalWrite(QUEUE_MOTOR_PIN_2, HIGH);

                            if(current_cup_size == 0x03){ // 처리 예정인 컵이 Large 컵 기준인 경우 큐체인 속도 향상
                                for(int i = QUEUE_SPEED + 50; queue_current_state == QUEUE_GO && i > QUEUE_SPEED; i -= 10){
                                  analogWrite(QUEUE_MOTOR_EN, i);
                                  delay(100);
                                }
                            }else{ // 처리 예정인 컵이 Large가 아닌 경우 큐체인 속도 유지
                                for(int i = QUEUE_SPEED + 20; queue_current_state == QUEUE_GO && i > QUEUE_SPEED; i -= 10){
                                  analogWrite(QUEUE_MOTOR_EN, i);
                                  delay(100);
                                }
                            }
                        }
                    }
                    break;

                // 큐체인 정지 queue state
                case QUEUE_STOP:
                    vertical_step_hold(); // 스텝 모터 토크 활성화
                    delay(200);
                    
                    // 큐체인 위치 조정
                    if(current_cup_size == 0x03){ 
                        queue_positioning(QUEUE_SPEED + 10);
                    }else queue_positioning(QUEUE_SPEED);

                    analogWrite(QUEUE_MOTOR_EN, QUEUE_SPEED - 20);
                    digitalWrite(QUEUE_MOTOR_PIN_1, LOW);
                    digitalWrite(QUEUE_MOTOR_PIN_2, HIGH);
                    delay(100);

                    queue.Off();

                    QUEUE_door.write(160); // 컵 투입구 개방
                    delay(500);

                    // Jetson Xavier NX에 ready signal 송신
                    tx_rx_data = 1 << 7;
                    Serial.println(tx_rx_data);
                    delay(500);

                    // Jetson Xavier NX에서 컵 정보 수신
                    tx_rx_data = 0xFF;

                    while(tx_rx_data == 0xFF && !reset_flag){
                        if(Serial.available() > 0){
                            tx_rx_data = Serial.read();

                            if(tx_rx_data % 8){
                                tx_rx_data = 0xFF;
                            }else{ 
                                // 전송된 1Byte 데이터 구성
                                // 홀더 1bit / 컵 크기 2bits / 입구 크기 2bits / don't care 3bits
                                tx_rx_data = tx_rx_data >> 3;
                                byte tmp = tx_rx_data & 0x03;
                                next_entrance_size = tmp;

                                tx_rx_data = tx_rx_data >> 2;
                                tmp = tx_rx_data & 0x03;
                                next_cup_size = tmp;

                                tx_rx_data = tx_rx_data >> 2;
                                tmp = tx_rx_data & 0x01;
                                next_holder_exist = tmp;
                                
                                NextCUP.modifyCupInfo(next_cup_size, next_entrance_size, next_holder_exist);
                            }

                            delay(50);
                        }
                    }
                    delay(4000);

                    if(!reset_flag){
                        if(current_cup_size){ // 처리 예정인 컵이 존재하는 경우 
                            cup_cnt[current_cup_size]++;

                            if(next_cup_size){
                                queue_current_state = QUEUE_GO;
                            }else{
                                queue_current_state = QUEUE_READY_CUP;
                            }

                            main_current_state = VERTICAL_HOLD_CUP;
                            break;
                        }else{
                            // 현재 처리중인 컵이 없다면 python 입장에서도 response를 기다리면안됨
                            // 즉, current가 없다면 python으로 0x1000_0000을 전달하여 PY_READY로 돌아가도록 해야함
                            tx_rx_data = 1 << 7;
                            Serial.println(tx_rx_data);

                            if(next_cup_size){
                                queue_current_state = QUEUE_GO;
                            }else{
                                queue_current_state = QUEUE_READY_CUP;
                            }

                            break;
                        }
                    }

                    break;

                // 큐체인 대기 state
                case QUEUE_READY_CUP:
                    QUEUE_door.write(160); // 컵 투입구 개방
                    delay(300);

                    // 초음파 센서 활용 컵 인식 대기
                    long duration = 0;
                    int distance = 999;
                    
                    while(!reset_flag && distance > 7 && queue_current_state == QUEUE_READY_CUP){
                        digitalWrite(TRIG_PIN, LOW);
                        delayMicroseconds(2);
                        digitalWrite(TRIG_PIN, HIGH);
                        delayMicroseconds(10);
                        digitalWrite(TRIG_PIN, LOW);

                        duration = pulseIn(ECHO_PIN, HIGH);

                        distance = duration * 0.034 / 2;
                        
                        delay(100);
                    }
                    
                    delay(4000);
                    queue_current_state = QUEUE_GO;
                    break;
            }
            break;
            
        // 투입된 컵 수직 처리 시작 state
        case VERTICAL_HOLD_CUP:
            delay(300);
            vertical_step_hold(); // 수직 이동 그리퍼 토크 활성화 -> 이동 제한
            
            remove_lid_servo_in(); // 뚜껑 제거 장치 활성화

            VERTICAL_STEPPER_finger_move(VERTICAL_STEPPER_finger_ready_cup[current_entrance_size], VERTICAL_STEPPER_finger_remove_lid[current_entrance_size]);
            delay(500);

            if(!reset_flag){
                main_current_state = VERTICAL_REMOVE_LID_HOLDER;
            }
            break;

        // 홀더 및 뚜껑 제거 state
        case VERTICAL_REMOVE_LID_HOLDER:
            next_vertical_pos = vertical_pos_remove_lid;
            VERTICAL_STEPPER_move(); // 뚜껑 제거를 위한 수직 이동
            
            delay(500);
            
            remove_lid_servo_reset(); // 뚜껑 제거 장치 초기화
            delay(500);
  
            next_vertical_pos = vertical_pos_holder;
            VERTICAL_STEPPER_move(); // 홀더 제거를 위한 수직 이동
            
            if(!reset_flag){
                if(current_holder_exist){ // 홀더 유무에 따른 state 변화
                    main_current_state = HORIZONTAL_HOLD_HOLDER;
                }else{
                    main_current_state = HORIZONTAL_HOLD_CUP;
                }
            }
            break;

        // 수평 이동 그리퍼 홀더 제거 state
        case HORIZONTAL_HOLD_HOLDER:
            horizontal_step_hold();
            vertical_step_hold();

            // 수직 이동 그리퍼는 컵을 약하게 잡도록 제어
            // 수평 이동 그리퍼는 홀더를 잡도록 finger 각도 제어
            VERTICAL_STEPPER_finger_move(VERTICAL_STEPPER_finger_remove_lid[current_entrance_size], VERTICAL_STEPPER_finger_ready_cup[current_entrance_size]);
            delay(500);
            HORIZONTAL_STEPPER_finger.write(HORIZONTAL_STEPPER_finger_holder); 
            delay(500);

            // 컵 수직 이동을 통한 홀더 제거
            next_vertical_pos = vertical_pos_remove_holder;
            VERTICAL_STEPPER_move();

            if(!reset_flag){
                main_current_state = HORIZONTAL_PLACE_HOLDER;
            }
            break;

        // 홀더 제거 state
        case HORIZONTAL_PLACE_HOLDER:
            vertical_step_hold();
            HORIZONTAL_STEPPER_finger.write(HORIZONTAL_STEPPER_finger_default);
            delay(500);

            if(!reset_flag){
                main_current_state = HORIZONTAL_HOLD_CUP;
            }
            break;

        // 수직 이동 그리퍼 -> 수평 이동 그리퍼 컵 전달 state
        case HORIZONTAL_HOLD_CUP:
            vertical_step_hold();
            next_vertical_pos = vertical_pos_cup;
            VERTICAL_STEPPER_move();

            // 수평 이동 그리퍼가 일정 각도인 상태에서 수직 이동 그리퍼의 각도를 초기값으로 선언
            // 수직 이동 그리퍼에서 수평 이동 그리퍼로 컵이 전달되며 수평 이동 그리퍼의 각도를 더 강하게 제어하여 컵을 잡을 수 있도록 함
            HORIZONTAL_STEPPER_finger.write(HORIZONTAL_STEPPER_finger_cup);
            delay(500);
            VERTICAL_STEPPER_finger_move(VERTICAL_STEPPER_finger_ready_cup[current_entrance_size], VERTICAL_STEPPER_finger_default);
            delay(1000);
            
            HORIZONTAL_STEPPER_finger.write(HORIZONTAL_STEPPER_finger_ready_cup[current_cup_size]);
            delay(500);
            HORIZONTAL_STEPPER_finger.write(HORIZONTAL_STEPPER_finger_cup);
            delay(500);
            
            // 수직 이동 그리퍼 초기 위치 이동
            current_vertical_pos = vertical_pos_default;
            while(!reset_flag && main_current_state == HORIZONTAL_HOLD_CUP){
                VERTICAL_STEPPER.setStep(1);
            }
            break;

        // 폐수, 뚜껑, 빨대 제거 state
        case HORIZONTAL_WASTE_CUP:
            // 폐수 처리 위치로 수평 이동 그리퍼 이동
            next_horizontal_pos = horizontal_pos_waste;
            HORIZONTAL_STEPPER_move();
            
            // 폐수 처리를 위한 수평 이동 그리퍼 wrist 제어
            HORIZONTAL_STEPPER_wrist.write(HORIZONTAL_STEPPER_wrist_waste);
            delay(1000);

            if(!reset_flag){
                main_current_state = HORIZONTAL_WASH_CUP;
            }
            break;

        // 세척 state
        case HORIZONTAL_WASH_CUP:
            HORIZONTAL_STEPPER_wrist.write(HORIZONTAL_STEPPER_wrist_wash);

            // 수평 이동 그리퍼 세척부 이동
            next_horizontal_pos = horizontal_pos_wash;
            HORIZONTAL_STEPPER_move();

            // 워터펌프 활성화 및 종료
            waterpump.On();
            delay(2500);
            waterpump.Off();
            delay(300);

            // 폐수 처리부 이동 및 물기 제거
            next_horizontal_pos = horizontal_pos_waste;
            HORIZONTAL_STEPPER_move();
            wash_post_process();

            if(!reset_flag){
                main_current_state = HORIZONTAL_LOAD_CUP;
            }
            break;

        // 컵 적재 state
        case HORIZONTAL_LOAD_CUP:
            
            // 로드셀 활성화를 통한 폐수 무게 측정
            LoadCellScale.power_up();
            delay(300);
            int waste_weight = (LoadCellScale.get_units() / 1000);
            LoadCellScale.power_down();

            // 적재를 위한 수평 이동 그리퍼 wrist 각도 제어 (small의 경우 각도를 조금 더 할당해 안정적인 적재)
            if(current_cup_size == 0x01) HORIZONTAL_STEPPER_wrist.write(0);
            else HORIZONTAL_STEPPER_wrist.write(HORIZONTAL_STEPPER_wrist_load);

            // 현재 컵 입구 크기에 맞는 적재 위치 저장
            horizontal_pos_cup = horizontal_pos_cup_size[current_entrance_size];
            next_horizontal_pos = horizontal_pos_cup;
            HORIZONTAL_STEPPER_move();

            // 컵 적재부에 삽입
            HORIZONTAL_STEPPER_finger.write(HORIZONTAL_STEPPER_finger_cup + 20);
            delay(1500);
            HORIZONTAL_STEPPER_finger.write(HORIZONTAL_STEPPER_finger_default);
            delay(1000);

            // 수평 이동 그리퍼 초기 위치 이동 및 wrist 각도 초기화
            next_horizontal_pos = horizontal_pos_default;
            HORIZONTAL_STEPPER_move();
            HORIZONTAL_STEPPER_wrist.write(HORIZONTAL_STEPPER_wrist_default);
            delay(500);

            // 현재 저장중인 컵 개수에 따른 적재부 -> 보관함 이동 여부 판단
            cleaning_cup();

            delay(200);

            // 로드셀 폐수 무게 Jetson Xavier NX에 송신
            Serial.println(waste_weight);

            if(!reset_flag){
                servo_detach();
                main_current_state = STARTING;
            }
            break;
    }
}
