# SquaresGame
A basic game written in Java to develop skills / for fun.

---Premise--- 

1 or 2 player game where the player(s) avoid a square which falls from the top to the bottom of the screen. 
On collision with the square the player will lose a life. Players have 3 lives.
Each time the square reaches the bottom of the screen, it respawns at the top with increased speed.
Player(s) collect tokens which increase their score and decrease the speed of the square.
Players have a limited amount of Power Points which can be used to perform special actions (see below)
Once the square reaches maximum speed, it instead to grows in size. 
In this stage of the game the square can intermittently 'frail'. When frail, clicking on the square will decrease its size, and restore player(s) Power Points.

Try to get the highest score possible!

---1 player controls---

Arrow keys to move.
Spint Power = 'F_KEY' or 'PERIOD'
Invincibility Power = 'C_KEY' or 'SLASH'

---2 player controls---

Player 1: 

Arrow keys to move. 
Power to make Player 2 small and fast: 'SLASH'

Player 2:

WASD key to move.
Power to make Player 1 invincible: 'C_KEY'

---info to launch---

Uses Android Studio, Libgdx, and Gradle.

DesktopLauncher Android Gradle Plugin Version: 4.0.1 Gradle Version: 6.1.1 Java SDK: 13

Warning: Does not curremtly run on M1 mac chips :(

---Images of the game---

Main menu screen: https://github.com/user-attachments/assets/df87cc73-d62b-47ef-badc-808e3797774a

Preferences screen: https://github.com/user-attachments/assets/2d2f6195-e7ec-4dc7-92cc-9a3b6b5813a4

Example 1 player game screen: https://github.com/user-attachments/assets/a4847836-aa42-4908-acee-697fbe8d47e5

Example 2 player game screen: https://github.com/user-attachments/assets/bc7998f2-5ee8-4455-a0d1-c814c63abc0d

Example 2 player game screen with 'Frail' enemy: https://github.com/user-attachments/assets/7adbadc1-c9b7-4134-ba6c-62311551ad0d


