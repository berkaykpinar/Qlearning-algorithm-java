package com.company;

import javax.swing.*;
import java.awt.*;

import static java.awt.Color.*;


public class Main extends JFrame {
    private int SqrSize = 6;
    private int agentX=0;
    private int agentY=0;
    private QLearning qLearning = new QLearning();
    private char[][] maze;
    private int[][] path;

    public Main() {
        super("Q-Learning");

        getContentPane().setBackground(WHITE);
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        qLearning.init();
        qLearning.calculateQ();
        System.out.println("FLAG");
        qLearning.printQ();
        qLearning.printPolicy();
        path=qLearning.findPath();

        for(int i=0;i<qLearning.getMaze().length;i++){
            for(int j=0;j<qLearning.getMaze()[0].length;j++){
                System.out.print(qLearning.getMaze()[i][j]+" ");
            }
            System.out.println(" ");
        }
        maze=qLearning.getMaze();
        PlotEpisodeStep example = new PlotEpisodeStep(qLearning.getStepCounter());
        example.setSize(800, 400);
        example.setLocationRelativeTo(null);
        example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        example.setVisible(true);

        PlotEpisodeCost episodeCost = new PlotEpisodeCost(qLearning.getValueCounter());
        episodeCost.setSize(800, 400);
        episodeCost.setLocationRelativeTo(null);
        episodeCost.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        episodeCost.setVisible(true);

    }

    void drawRectangles(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;



        for(int i=0;i<maze.length;i++){
            for(int j=0;j<maze[0].length;j++){
                g2d.drawRect(i* SqrSize *5, j* SqrSize *5+40, SqrSize *5, SqrSize *5);
            }
        }

        for(int i=0;i<maze.length;i++){
            for(int j=0;j<maze[0].length;j++){
                if(maze[i][j]=='X'){
                    g2d.setColor(RED);
                    g2d.fillRect(i * SqrSize *5, j* SqrSize *5+40 , SqrSize *5 -1 , SqrSize *5-1);
                }
                if(maze[i][j]=='F'){
                    g2d.setColor(BLUE);
                    g2d.fillRect(i * SqrSize *5, j* SqrSize *5+40 , SqrSize *5 -1 , SqrSize *5-1);
                }

            }
        }

        for(int i=0;i<path.length;i++){
            g2d.setColor(GREEN);
            g2d.fillRect(path[i][0] * SqrSize *5, path[i][1] * SqrSize *5+40 , SqrSize *5 -1 , SqrSize *5-1 );
        }

        g2d.setColor(DARK_GRAY);
        g2d.fillRect(SqrSize *agentY*5,  SqrSize *agentX*5+40 , SqrSize *5 , SqrSize *5 );



    }

    public void paint(Graphics g) {
        super.paint(g);
        drawRectangles(g);
    }

    public static void main(String[] args) throws Exception {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main().setVisible(true);
            }
        });
    }
}
