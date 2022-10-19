# Battleship game simulation developed using advanced Java concepts

**The game consists of 2 main Classes :**

-> SeverLauncher and Player, used to connect the player to the server and launch the game;

**Also, 7 packages:**
- Colors: has the colors used in the project (boards and Ascii art for ex); 
- Commands: game commands used by the players in the chat;
- Field: draws 10x10 boards constructed using Point objects and a double array;
- GameObjects: Such as Ship and ShipsEnums;
- Point: a Point object that is determined by the x and y coordinate value;
- Game: here resides most of the game logic and thread handling;
- Utils: stores de Ascii Art used and Loading Animation.
  
**Rules:**
- Press enter to begin the game;
- After checking your board decide whether to /ready or /random (to randomize board up to 3 times); 
- See if you are the attacker, in that case /attack using a letter and a number (/attack A 5 for example);
- If you hit an adversaries boat, keep attacking, else adversary attacks;
- Keep this going until one of the player has no remaining boats and wins;
- Have fun :)!

To run program, download 18 files and compile in IDE or using javac in terminal

## EXAMPLE OUTPUT

![BattleShips – Player java 19_10_2022 14_16_39](https://user-images.githubusercontent.com/103672168/196750399-03d8e092-1dbd-4ded-b34d-7b1d45cc0f76.png)

