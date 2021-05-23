package com.company;

import java.util.ArrayList;
import java.util.Random;

public class QLearning {

    private final double alpha = 0.01; // Learning rate
    private final double gamma = 0.9; // Eagerness - 0 looks in the near future, 1 looks in the distant future

    private final int mazeWidth = 25;
    private final int mazeHeight = 25;
    private final int statesCount = mazeHeight * mazeWidth;
    private int trainCycle=500;

    private final int reward = 100;
    private final int penalty = -2;

    private char[][] maze;  // Maze read from file
    private int[][] R;       // Reward lookup
    private double[][] Q;    // Q learning
    private int[][] shortestPath;
    private int[] stepCounter;

    public double[] getValueCounter() {
        return valueCounter;
    }

    private double[] valueCounter;

    public char[][] getMaze() {
        return maze;
    }

    public void init() {

        R = new int[statesCount][statesCount];
        Q = new double[statesCount][statesCount];
        maze = new char[mazeHeight][mazeWidth];
        shortestPath = new int[statesCount][statesCount];
        stepCounter=new int[trainCycle];
        valueCounter= new double[trainCycle];


        createMaze();


        int i = 0;
        int j = 0;


        // We will navigate through the reward matrix R using k index
        for (int k = 0; k < statesCount; k++) {

            // We will navigate with i and j through the maze, so we need
            // to translate k into i and j
            i = k / mazeWidth;
            j = k - i * mazeWidth;

            // Fill in the reward matrix with -1
            for (int s = 0; s < statesCount; s++) {
                R[k][s] = -1;
            }

            // If not in final state or a wall try moving in all directions in the maze
            if (maze[i][j] != 'F') {

                // Try to move left in the maze
                int goLeft = j - 1;
                if (goLeft >= 0) {
                    int target = i * mazeWidth + goLeft;
                    if (maze[i][goLeft] == '0') {
                        R[k][target] = 0;
                    } else if (maze[i][goLeft] == 'F') {
                        R[k][target] = reward;
                    } else {
                        R[k][target] = penalty;
                    }
                }

                // Try to move right in the maze
                int goRight = j + 1;
                if (goRight < mazeWidth) {
                    int target = i * mazeWidth + goRight;
                    if (maze[i][goRight] == '0') {
                        R[k][target] = 0;
                    } else if (maze[i][goRight] == 'F') {
                        R[k][target] = reward;
                    } else {
                        R[k][target] = penalty;
                    }
                }

                // Try to move up in the maze
                int goUp = i - 1;
                if (goUp >= 0) {
                    int target = goUp * mazeWidth + j;
                    if (maze[goUp][j] == '0') {
                        R[k][target] = 0;
                    } else if (maze[goUp][j] == 'F') {
                        R[k][target] = reward;
                    } else {
                        R[k][target] = penalty;
                    }
                }

                // Try to move down in the maze
                int goDown = i + 1;
                if (goDown < mazeHeight) {
                    int target = goDown * mazeWidth + j;
                    if (maze[goDown][j] == '0') {
                        R[k][target] = 0;
                    } else if (maze[goDown][j] == 'F') {
                        R[k][target] = reward;
                    } else {
                        R[k][target] = penalty;
                    }
                }
            }
        }
        initializeQ();
        printR(R);

    }

    //Creates a maze and fills with random walls.
    private void createMaze() {
        for (int i = 0; i < mazeHeight; i++) {
            for (int j = 0; j < mazeWidth; j++) {
                Random rand = new Random();
                double x = rand.nextDouble() * 20;
                if (x > 15) {
                    maze[i][j] = 'X';
                } else {
                    maze[i][j] = '0';
                }

            }
        }
        maze[mazeHeight-1][mazeWidth-1] = 'F';
    }

    //Set Q values to R values
    void initializeQ() {
        for (int i = 0; i < statesCount; i++) {
            for (int j = 0; j < statesCount; j++) {
                Q[i][j] = 0;
            }
        }
    }

    // Used for debug
    void printR(int[][] matrix) {
        System.out.printf("%25s", "States: ");
        for (int i = 0; i <= statesCount; i++) {
            System.out.printf("%4s", i);
        }
        System.out.println();

        for (int i = 0; i < statesCount; i++) {
            System.out.print("Possible states from " + i + " :[");
            for (int j = 0; j < statesCount; j++) {
                System.out.printf("%4s", matrix[i][j]);
            }
            System.out.println("]");
        }
    }

    void calculateQ() {
        Random rand = new Random();

        for (int i = 0; i < trainCycle; i++) { // Train cycles
            // Select random initial state
            int crtState = rand.nextInt(statesCount);
            int step=0;
            double totalValue=0;

            while (!isFinalState(crtState)) {
                step=step+1;
                int[] actionsFromCurrentState = possibleActionsFromState(crtState);

                // Pick a random action from the ones possible
                int index = rand.nextInt(actionsFromCurrentState.length);
                int nextState = actionsFromCurrentState[index];

                // Q(state,action)= Q(state,action) + alpha * (R(state,action) + gamma * Max(next state, all actions) - Q(state,action))
                double q = Q[crtState][nextState];
                double maxQ = maxQ(nextState);
                int r = R[crtState][nextState];

                double value = q + alpha * (r + gamma * maxQ - q);

                Q[crtState][nextState] = value;

                totalValue=totalValue+value;
                crtState = nextState;
            }
            stepCounter[i]=step;
            valueCounter[i]=totalValue;
        }
    }

    boolean isFinalState(int state) {
        int i = state / mazeWidth;
        int j = state - i * mazeWidth;

        return maze[i][j] == 'F';
    }

    int[] possibleActionsFromState(int state) {
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < statesCount; i++) {
            if (R[state][i] != -1) {
                result.add(i);
            }
        }

        int[] array = new int[result.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = result.get(i);
        }
        return array;
    }

    double maxQ(int nextState) {
        int[] actionsFromState = possibleActionsFromState(nextState);
        //the learning rate and eagerness will keep the W value above the lowest reward
        double maxValue = -10;
        for (int nextAction : actionsFromState) {
            double value = Q[nextState][nextAction];

            if (value > maxValue)
                maxValue = value;
        }
        return maxValue;
    }

    void printPolicy() {
        System.out.println("\nPrint policy");
        for (int i = 0; i < statesCount; i++) {
            shortestPath[i][0] = i;
            shortestPath[i][1] = getPolicyFromState(i);
            System.out.println("From state " + shortestPath[i][0] + " goto state " + shortestPath[i][1]);
        }
    }

    int[][] findPath() {
        int i = 0;
        //int[] path;
        ArrayList<Integer> path = new ArrayList<>();
        while (true) {
            path.add(shortestPath[i][0]);
            i = shortestPath[i][1];
            if (shortestPath[i][0] == shortestPath[i][1]) {
                break;
            }
        }
        int[][] pathCoord = new int[path.size()][2];
        for (i = 0; i < pathCoord.length; i++) {
            int k = path.get(i) / mazeWidth;
            int h = path.get(i) - k * mazeWidth;
            pathCoord[i][0] = k;
            pathCoord[i][1] = h;
            System.out.println("Index : " + path.get(i) + " Coord i: " + k + " j: " + h);
        }
        return pathCoord;
    }


    int getPolicyFromState(int state) {
        int[] actionsFromState = possibleActionsFromState(state);

        double maxValue = Double.MIN_VALUE;
        int policyGotoState = state;

        // Pick to move to the state that has the maximum Q value
        for (int nextState : actionsFromState) {
            double value = Q[state][nextState];

            if (value > maxValue) {
                maxValue = value;
                policyGotoState = nextState;
            }
        }
        return policyGotoState;
    }

    void printQ() {
        System.out.println("Q matrix");
        for (int i = 0; i < Q.length; i++) {
            System.out.print("From state " + i + ":  ");
            for (int j = 0; j < Q[i].length; j++) {
                System.out.printf("%6.2f ", (Q[i][j]));
            }
            System.out.println();
        }
    }

    double[][] getQ() {
        return Q;
    }
    public int[] getStepCounter() {
        return stepCounter;
    }



}