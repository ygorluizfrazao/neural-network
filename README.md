# Warning

This README was created by chat-gpt as a test

# Neural Network Implementation in Java

This repository contains a simple neural network implementation in Java. The neural network uses a sigmoid activation function and backpropagation algorithm to learn from data.

## Usage

### Creating a Neural Network

To create a new neural network, instantiate the `NeuralNetwork` class with the desired number of input, hidden, and output nodes. For example, to create a neural network with 2 input nodes, 4 hidden nodes, and 1 output node:

```java
int numInputNodes = 2;
int numHiddenNodes = 4;
int numOutputNodes = 1;

NeuralNetwork nn = new NeuralNetwork(numInputNodes, numHiddenNodes, numOutputNodes);
```

### Training the Neural Network

To train the neural network, call the train method with the training data and desired number of epochs. For example, to train the neural network with the XOR dataset for 1000 epochs:

```java
double[][] input = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
double[][] output = {{0}, {1}, {1}, {0}};

int numEpochs = 1000;

nn.train(input, output, numEpochs);
```

### Making Predictions

To make predictions with the neural network, call the predict method with the input data. For example, to predict the output for the input [0, 1]:

```java
double[] input = {0, 1};

double[] output = nn.predict(input);

System.out.println(Arrays.toString(output)); 
```

### Saving and Loading the Neural Network

To save the trained neural network to a file, call the save method with the desired file path. For example, to save the neural network to a file called model.txt:

```java
String filePath = "model.txt";

nn.save(filePath);
```

To load a previously saved neural network from a file, instantiate a new NeuralNetwork object and call the load method with the file path. For example, to load the neural network from the model.txt file:

```java
String filePath = "model.txt";

NeuralNetwork nn = new NeuralNetwork();
nn.load(filePath);
```

### Examples
Here are a few examples from the source code in the src/br/com/frazao/neuralnetwork directory:

Example 1: Creating a neural network and training it with the XOR dataset

```java
// create neural network with 2 input nodes, 2 hidden nodes, and 1 output node
NeuralNetwork nn = new NeuralNetwork(2, 2, 1);

// train neural network with XOR dataset for 1000 epochs
double[][] input = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
double[][] output = {{0}, {1}, {1}, {0}};
nn.train(input, output, 1000);

// test neural network with input [0, 1]
double[] inputTest = {0, 1};
double[] outputTest = nn.predict(inputTest);
System.out.println("Output for input " + Arrays.toString(inputTest) + ": " + Arrays.toString(outputTest));
```

#### Output:

```less
Output for input [0.0, 1.0]: [0.9942479202651088]

```
