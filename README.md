# Deep Q-learning Maze

By Max Brynolf (max.brynolf@hotmail.com)

Repository link: https://github.com/MaxBrynolf/DQNMaze

## Overview

An environment in which DQN can be used to train an agent to progress through a maze game with floating physics, by controlling the acceleration in different directions. The environment can easily be altered, as well as any hyperparameters used during training. 

## Installation

Requirements:
1. Java IDE
2. Python and Jupyter Lab/Jupyter Notebook
3. [Py4J](https://github.com/py4j/py4j)

Installation:
1. Create a Java project from the "src" folder, containing all the Java-files. 
3. Include Py4J in the project (see [repository]((https://github.com/py4j/py4j) for details).
3. Open the file "Deep Q-learning.ipynb" with Jupyter.


## Usage

#### Java

To launch the environment, run the file `Maze.java`. The following parameter, given in `Maze.main`, decides whether to launch the environment in training mode or not:

```java
TrainingMode training = TrainingMode.NONE;
```

Setting this to `TrainingMode.NONE` will allow you to try the environment manually, using the arrow keys to move around. Setting it to `TrainingMode.RL` sets the application in training mode.

The world can be designed in the `Maze.startWindow` method. For example, to add a wall use `World.addWall(...)`-method.

#### Python

The file "Deep Q-learning.ipynb" contains instructions on how to train the model. First make sure to start the Java application with training mode set to `RL`, as described above. Then launch the different code cells in the notebook to train the model, according to the instructions.

## Examples

#### Maze With Mixed Types of Walls

A maze-like environment was created using the following world:

```java
world.addWall(new Wall(0, 10, 0, 300));
world.addWall(new Wall(0, 300, 290, 300));
world.addWall(new Wall(0, 300, 0, 10));
world.addWall(new Wall(290, 300, 0, 300));
world.addDeathWall(new DeathWall(95, 105, 75, 290));
world.addDeathWall(new DeathWall(195, 205, 10, 215));
world.addScoreZone(new ScoreZone(10, 95, 95, 105, 10));
world.addScoreZone(new ScoreZone(10, 95, 195, 205, 10));
world.addScoreZone(new ScoreZone(95, 105, 10, 75, 10));
world.addScoreZone(new ScoreZone(105, 195, 95, 105, 10));
world.addScoreZone(new ScoreZone(105, 195, 195, 205, 10));
world.addScoreZone(new ScoreZone(195, 205, 215, 290, 10));
world.addScoreZone(new ScoreZone(205, 290, 95, 105, 10));
world.addScoreZone(new ScoreZone(205, 290, 195, 205, 10));
world.addGoal(new Goal(205, 290, 10, 60, 10));
```

A comparison between an untrained, random agent and a trained agent is shown below.

Untrained, random agent            |  Trained agent, with slight added noise
:-------------------------:|:-------------------------:
<img src="Preview/maze random.gif" alt="random agent" width="300"/> |  <img src="Preview/maze trained.gif" alt="trained agent" width="300"/>

#### Gap at Random Offsets

A randomly placed hole in a wall can be rendered every time the game restarts by including a `Random`-member `r` and executing the following code upon reset:

```java
int holeOffset = r.nextInt(Constants.screenWidth - 100);
world.addDeathWall(new DeathWall(0, holeOffset, 145, 155));
world.addDeathWall(new DeathWall(holeOffset + 100, Constants.screenWidth, 145, 155));
```

By adding the following background environment, the hole might end up being shorter, if it starts inside a wall. This behavior makes single-ended combinations more likely and was hence intentionally kept.

```java
world.addGoal(new Goal(10, Constants.screenWidth - 10, 0, 50, 1));
world.addDeathWall(new DeathWall(0, 10, 0, 300));
world.addDeathWall(new DeathWall(0, 300, 290, 300));
world.addDeathWall(new DeathWall(290, 300, 0, 300));
```
A comparison between an untrained, random agent and a trained agent is shown below.

Untrained, random agent            |  Trained agent
:-------------------------:|:-------------------------:
<img src="Preview/gap random.gif" alt="random agent" width="300"/> |  <img src="Preview/gap trained.gif" alt="trained agent" width="300"/>

The table below visualizes how a stationary state close to the wall is passed through the convolutional neural network. As can be seen, a state of size 4 was used.

Input state | Layer 1 | Layer 2
:-------------------------:|:-------------------------:|:-------------------------:
<img src="Preview/input state.jpg" width="80" /> | <img src="Preview/features layer 1.jpg" alt="trained agent" width="280"/> | <img src="Preview/features layer 2.jpg" alt="trained agent" width="140"/>

Evidently, some key features, such as the ball position, are highlighted in certain channels, whereas other features, such as the walls or the background, are highlighted in others. Below are the convolutional weight matrices in the first layer for some of these cases. As can be seen, channel 1 identifies walls, channel 3 identifies the ball and channel 8 identifies the background. All of this assumes a stationary state where the player is not moving. If the player is moving, these circumstances are changed, meaning that the averaged weight tensor will not come into play.

Channel | Weight tensor | Averaged weight tensor | Resulting feature  |
| :------------: | :-------------: |:-------------:| :-----:|
1 | <img src="Preview/weights channel 1.jpg" width="305" /> | <img src="Preview/weights average channel 1.jpg" width="70" /> | <img src="Preview/channel 1.jpg" width="70" /> |
3 | <img src="Preview/weights channel 3.jpg" width="305" /> | <img src="Preview/weights average channel 3.jpg" width="70" /> | <img src="Preview/channel 3.jpg" width="70" /> |
8 | <img src="Preview/weights channel 8.jpg" width="305" /> | <img src="Preview/weights average channel 8.jpg" width="70" /> | <img src="Preview/channel 8.jpg" width="70" /> |
