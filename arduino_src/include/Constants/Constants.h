#ifndef CONSTANTS_H
#define CONSTANTS_H

#define GLITCH 100

#define VERTICAL_STEPPER_SPEED 200
#define HORIZONTAL_STEPPER_SPEED 300
#define SPR 200

#define VERTICAL_STEPPER_DELAY (60 * 1000 * 1000 / SPR / VERTICAL_STEPPER_SPEED) + 50
#define HORIZONTAL_STEPPER_DELAY (60 * 1000 * 1000 / SPR / HORIZONTAL_STEPPER_SPEED) + 50

#define RESET_INTERRUPT_PIN              19
#define HORIZONTAL_STEPPER_reset_pin     20
#define VERTICAL_STEPPER_reset_pin       21
#define HORIZONTAL_STEPPER_wrist_pin     7
#define HORIZONTAL_STEPPER_finger_pin    8
#define VERTICAL_STEPPER_finger_pin      9
#define regular_large_pin                10
#define small_regular_pin                11

const float pi = 3.14;
const float CmPerCycle = 4.71;

/* position definition */
const float horizontal_pos_default = 48;
const float horizontal_pos_reset = 95;
const float horizontal_pos_wash = 25;
const float horizontal_pos_waste = 35;
const float horizontal_pos_cup_1 = 75;
const float horizontal_pos_cup_2 = 90;

const float vertical_pos_default = 0;
const float vertical_pos_lid = 3;
const float vertical_pos_holder = 12;

/* horizontal servo motor angle definition */
const int HORIZONTAL_STEPPER_finger_default = 120;
const int HORIZONTAL_STEPPER_wrist_default = 120;
const int HORIZONTAL_STEPPER_wrist_load = 120;

const int HORIZONTAL_STEPPER_finger_holder = 45;
const int HORIZONTAL_STEPPER_wrist_waste = 45;
const int HORIZONTAL_STEPPER_wrist_wash  = 45;

/* vertical servo motor angle definition */
const int VERTICAL_STEPPER_finger_default = 175;

/* trash bin servo motor angle definition */
const int small_regular_small = 150;
const int small_regular_regular = 170;
const int regular_large_regular = 50;
const int regular_large_large = 80;

#endif 
