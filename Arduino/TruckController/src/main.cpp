#include <Arduino.h>

int RXLED = 17;  // The RX LED has a defined Arduino pin
// The TX LED was not so lucky, we'll need to use pre-defined
// macros (TXLED1, TXLED0) to control that.
// (We could use the same macros for the RX LED too -- RXLED1,
//  and RXLED0.)

// motor one
int enA = 5;
int in1 = 18;
int in2 = 19;

// motor two
int enB = 6;
int in3 = 20;
int in4 = 21;

void setup() {
  //pinMode(RXLED, OUTPUT);  // Set RX LED as an output
  // TX LED is set as an output behind the scenes
  Serial.begin(9600); //This pipes to the serial monitor
  Serial1.begin(9600); //This is the UART, pipes to sensors attached to board
  Serial1.setTimeout(50);

  // set all the motor control pins to outputs
  pinMode(enA, OUTPUT);
  pinMode(enB, OUTPUT);
  pinMode(in1, OUTPUT);
  pinMode(in2, OUTPUT);
  pinMode(in3, OUTPUT);
  pinMode(in4, OUTPUT);
}

void loop() {

 if (Serial1.available() > 1) {

   int left = (signed char) Serial1.read();
   int right = (signed char) Serial1.read();
   Serial1.readString(); // butto via il resto

   Serial.print(" I received:");
   Serial.println(left);
   Serial.println(right);

   if (left > 0) {
     digitalWrite(in1, HIGH);
     digitalWrite(in2, LOW);
   } else {
     digitalWrite(in1, LOW);
     digitalWrite(in2, HIGH);
   }

   if (right > 0) {
     digitalWrite(in3, HIGH);
     digitalWrite(in4, LOW);
   } else {
     digitalWrite(in3, LOW);
     digitalWrite(in4, HIGH);
   }

   analogWrite(enA, abs(left) * 5);
   analogWrite(enB, abs(right) * 5);

 }

 //digitalWrite(RXLED, LOW);   // set the RX LED ON
 //TXLED0; //TX LED is not tied to a normally controlled pin so a macro is needed, turn LED OFF
 //delay(10);              // wait for a second

 //digitalWrite(RXLED, HIGH);    // set the RX LED OFF
 //TXLED1; //TX LED macro to turn LED ON
 //delay(10);              // wait for a second

}
