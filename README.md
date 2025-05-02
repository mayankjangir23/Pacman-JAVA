A simple yet engaging Pacman game built using Java and Swing for graphical user interface rendering.

This project is a recreation of the classic Pacman arcade game, where the player controls Pacman to collect all the food pellets while avoiding ghosts in a maze.

Whether you're a fan of retro games or learning Java game development, this project demonstrates core game mechanics, event handling, collision detection, and simple AI — all in a clean, object-oriented design.

✨ Features

- Classic Pacman gameplay: Navigate the maze and collect all food dots to win the level.

- Four animated ghosts: Blue, red, orange, and pink ghosts with random movement patterns to challenge the player.

- Lives counter: Player starts with multiple lives and loses one when caught by a ghost.

- Score tracking: Earn points for each food pellet collected — track your progress!

- Game Over screen: Displays final score and allows easy restart.

- Keyboard controls: Intuitive arrow key controls for smooth navigation.

- Automatic map reset: Level resets automatically after all food is cleared.

- Simple GUI with smooth rendering: Clean, responsive visuals built using Java Swing and custom sprites.

- Lightweight and cross-platform: Runs on any system with Java installed — no external libraries required.

- Restartable game: After Game Over, simply press an arrow key to restart and play again instantly.

- Optimized code structure: Clean and modular object-oriented design — easy to understand and extend.

💡 Code Overview
App.java

- Sets up the JFrame window
  
- Initializes and embeds the Pacman JPanel
  
Pacman.java
- Contains the game logic, rendering, and event handling
- Uses an inner Block class to represent:
- Walls
- Ghosts
- Pacman
- Food
