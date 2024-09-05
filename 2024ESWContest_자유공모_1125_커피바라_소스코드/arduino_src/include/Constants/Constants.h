#ifndef CONSTANTS_H
#define CONSTANTS_H

#define GLITCH                      1

/* queue state machine */
#define QUEUE_GO                    0
#define QUEUE_STOP                  1
#define QUEUE_READY_CUP             2

/* main state machine */
#define ONLY_WAITING                0
#define RESET                       1
#define RESET_QUEUE                 2   
#define RESET_SERVO                 3
#define RESET_HORIZONTAL_INTERRUPT  4   
#define RESET_HORIZONTAL_DEFAULT    5 
#define RESET_VERTICAL_DEFAULT      6
#define STARTING                    7
#define VERTICAL_HOLD_CUP           8
#define VERTICAL_REMOVE_LID_HOLDER  9
#define HORIZONTAL_HOLD_HOLDER      10
#define HORIZONTAL_PLACE_HOLDER     11
#define HORIZONTAL_HOLD_CUP         12
#define HORIZONTAL_WASTE_CUP        13
#define HORIZONTAL_WASH_CUP         14
#define HORIZONTAL_LOAD_CUP         15

/* Queue Chain DC motor speed */
const int QUEUE_SPEED                      = 140;

/* pin num definition */
const int LOADCELL_DATA_PIN                 = 35;
const int LOADCELL_CLK_PIN                  = 36;

const int RESET_PIN                         = 2;
const int QUEUE_DOOR_PIN                    = 3;

const int WATERPUMP_EN                      = 4;
const int WATERPUMP_PIN_1                   = 5;
const int WATERPUMP_PIN_2                   = 6;

const int QUEUE_MOTOR_EN                    = 7;
const int QUEUE_MOTOR_PIN_1                 = 8;
const int QUEUE_MOTOR_PIN_2                 = 9;

const int HORIZONTAL_STEPPER_wrist_pin      = 22;
const int HORIZONTAL_STEPPER_finger_pin     = 23;
const int VERTICAL_STEPPER_finger_pin       = 18;

const int small_cup_servo_pin               = 13;
const int regular_cup_servo_pin             = 12;
const int large_cup_servo_pin               = 11;
const int remove_lid_servo_pin              = 17;

const int VERTICAL_STEPPER_reset_pin        = 19;
const int HORIZONTAL_STEPPER_reset_pin      = 20;
const int QUEUE_INTERRUPT_PIN               = 21;

const int ECHO_PIN = 45;
const int TRIG_PIN = 47;

// vertical, horizontal stepper motor speed 
// Steps Per Revolution (회전 당 스텝 수) definition
const int VERTICAL_STEPPER_SPEED            = 250;
const int HORIZONTAL_STEPPER_SPEED          = 300;
const int SPR                               = 200;

/* stepper motor constants definition */
const int VERTICAL_STEPPER_DELAY            = ((60L * 1000L * 1000 / SPR / VERTICAL_STEPPER_SPEED) + 50);
const int HORIZONTAL_STEPPER_DELAY          = ((60L * 1000L * 1000 / SPR / HORIZONTAL_STEPPER_SPEED) + 50);
const float pi = 3.14;
const float CmPerCycle = 4.71;

/* position definition */
const float horizontal_pos_default          = 41.5;
const float horizontal_pos_reset            = 1.5;
const float horizontal_pos_wash             = 13;
const float horizontal_pos_waste            = 23;

const float vertical_pos_default            = 0;
const float vertical_pos_remove_lid         = 2;
const float vertical_pos_remove_holder      = 3;
const float vertical_pos_holder             = 12;
const float vertical_pos_cup                = 12;

/* horizontal servo motor angle definition */
const int HORIZONTAL_STEPPER_finger_default = 115;
const int HORIZONTAL_STEPPER_wrist_default  = 130;
const int HORIZONTAL_STEPPER_wrist_load     = 5;

const int HORIZONTAL_STEPPER_finger_holder  = 55;
const int HORIZONTAL_STEPPER_finger_cup     = 40;
const int HORIZONTAL_STEPPER_wrist_waste    = 5;
const int HORIZONTAL_STEPPER_wrist_wash     = 25;

/* vertical servo motor angle definition */
const int VERTICAL_STEPPER_finger_minimum   = 40;
const int VERTICAL_STEPPER_finger_default   = 155;
 
/* cup cnt threshold */
const int small_cnt_threshold               = 23;
const int regular_cnt_threshold             = 15;
const int large_cnt_threshold               = 10;

const int cleaning_cup_angle                = 180;
const int default_cup_angle                 = 0;

/* actuator delay for removing lid */   
const int REMOVE_LID_ANGLE[7]               = {170, 70, 107, 120, 70, 107, 120};
const float horizontal_pos_cup_size[7]      = {0, 96.5, 82, 64, 96.5, 82, 64};

/* finger angle */
const int HORIZONTAL_STEPPER_finger_ready_cup[7] = {0, 58, 61, 64, 58, 61, 64};
const int VERTICAL_STEPPER_finger_remove_lid[7]  = {0, 30, 30, 80, 30, 30, 80};
const int VERTICAL_STEPPER_finger_cup[7]         = {0, 60, 70, 85, 60, 70, 85};
const int VERTICAL_STEPPER_finger_ready_cup[7]   = {0, 75, 95, 120, 75, 95, 120};


#endif
