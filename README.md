[![Build Status](https://travis-ci.org/Anuken/Koru.svg?branch=master)](https://travis-ci.org/Anuken/Koru/)

# Koru

A 2D sandbox game, written using libGDX and Java 8. Partially made as a testing ground for Entity Component systems, AI, world generation, biome generation and shaders.

**This project is no longer being worked on. I have moved on to other things.**  
No issues with the game will be fixed.

![axe-wielding crabs](http://i.imgur.com/6bft2gp.png)

![reflections](https://i.imgur.com/cMqFB6c.gif)

### Current Features
- infinite procedurally generated world
- biomes and cave systems
- basic building, resource collection
- reflective water shaders
- GUI made with [uCore Scene](https://github.com/Anuken/uCore)
- multiplayer, made with Kryonet
- crabs
- lighting system, using a modified version of box2D lights
- ingame chat

### Building

1. Install **JDK 8** - **not any other version!** If you fail to use the right version, the build may fail.
2. Open a terminal/command prompt, and run `./gradlew server:run` (linux/mac) / `gradlew.bat server:run` (windows)
3. Open another terminal/cmd tab and run `./gradlew desktop:run` (linux/mac) / `gradlew.bat desktop:run` (windows)
4. Press "connect" in the game window.
5. ~~Experience a broken game that will never be fixed.~~

#### Troubleshooting?

I haven't touched this project in at least 2 years, so if you run into problems, you're on your own.

~~### Roadmap~~
- proper player characters
- crafting
- more blocks
- passive creatures
- enemies
- saving entities in chunks
- block entities and data
