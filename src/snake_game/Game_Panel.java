package snake_game;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.*;

public class Game_Panel extends JPanel implements ActionListener, KeyListener {
    private int[] snake_x_length = new int[750];
    private int[] snake_y_length = new int[750];
    private int length_of_snake = 3;

    private int[] xPos = {25,50,75,100,125,150,175,200,225,250,275,300,325,350,375,400,425,450
            ,475,500,525,550,575,600,625,650,675,700,725,750,775,800,825,850};
    private int[] yPos = {75,100,125,150,175,200,225,250,275,300,325,350,375,400,425,450,475,500,525,550,575,600,625};

    private Random random = new Random();
    private int enemyX,enemyY;

    private boolean left = false;
    private boolean right = true;
    private boolean down = false;
    private boolean up = false;

    private int score = 0;
    private int moves = 0;
    private boolean gameOver = false;

    private ImageIcon snake_title = new ImageIcon(getClass().getResource("/snake_title.jpg"));
    private ImageIcon left_mouth = new ImageIcon(getClass().getResource("/left_mouth.png"));
    private ImageIcon right_mouth = new ImageIcon(getClass().getResource("/right_mouth.png"));
    private ImageIcon up_mouth = new ImageIcon(getClass().getResource("/up_mouth.png"));
    private ImageIcon down_mouth = new ImageIcon(getClass().getResource("/down_mouth.png"));
    private ImageIcon snake_image = new ImageIcon(getClass().getResource("/snake_image.png"));
    private ImageIcon enemy = new ImageIcon(getClass().getResource("/enemy.png"));

    private Timer timer;
    private int delay = 150;

    Game_Panel() {
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(true);

        timer = new Timer(delay,this);
        timer.start();

        newEnemy();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.WHITE);
        g.drawRect(24, 10, 851, 55);
        g.drawRect(24, 74, 851, 576);

        snake_title.paintIcon(this, g, 25, 11);
        g.setColor(Color.BLACK);
        g.fillRect(25,75,850,575);

        if(moves==0){
            snake_x_length[0]=100;
            snake_x_length[1]=75;
            snake_x_length[2]=50;

            snake_y_length[0]=100;
            snake_y_length[1]=100;
            snake_y_length[2]=100;
        }
        if (left){
            left_mouth.paintIcon(this,g,snake_x_length[0],snake_y_length[0]);
        }
        if (right){
            right_mouth.paintIcon(this,g,snake_x_length[0],snake_y_length[0]);
        }
        if (up){
            up_mouth.paintIcon(this,g,snake_x_length[0],snake_y_length[0]);
        }
        if (down){
            down_mouth.paintIcon(this,g,snake_x_length[0],snake_y_length[0]);
        }
        for(int i=1;i<length_of_snake;i++){
            snake_image.paintIcon(this,g,snake_x_length[i],snake_y_length[i]);
        }

        enemy.paintIcon(this, g, enemyX, enemyY);

        if(gameOver){
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD,50));
            g.drawString("Game Over",300,300);
            g.setFont(new Font("Arial", Font.PLAIN,20));
            g.drawString("Press SPACE to Restart",320,350);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial",Font.PLAIN,14));
        g.drawString("Score : " + score, 750,30);
        g.drawString("lengthOfSnake : " + length_of_snake, 750,50);

        g.dispose();
    }

    public void actionPerformed(ActionEvent e){

        for(int i = length_of_snake - 1; i > 0; i--){
            snake_x_length[i] = snake_x_length[i-1];
            snake_y_length[i] = snake_y_length[i-1];
        }

        if(left){
            snake_x_length[0] = snake_x_length[0] - 25;
        }
        if(right){
            snake_x_length[0] = snake_x_length[0] + 25;
        }
        if(up){
            snake_y_length[0] = snake_y_length[0] - 25;
        }
        if(down){
            snake_y_length[0] = snake_y_length[0] + 25;
        }

        if(snake_x_length[0] > 850){
            snake_x_length[0] = 25;
        }

        if(snake_x_length[0] < 25){
            snake_x_length[0] = 850;
        }

        if(snake_y_length[0] > 625){
            snake_y_length[0] = 75;
        }

        if(snake_y_length[0] < 75){
            snake_y_length[0] = 625;
        }

        collidesWithEnemy();
        collidesWithBody();
        repaint();
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            restart();
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT && (!right)){
            left = true;
            right = false;
            up = false;
            down = false;
            moves++;
        }

        if(e.getKeyCode() == KeyEvent.VK_RIGHT && (!left)){
            left = false;
            right = true;
            up = false;
            down = false;
            moves++;
        }

        if(e.getKeyCode() == KeyEvent.VK_UP && (!down)){
            left = false;
            right = false;
            up = true;
            down = false;
            moves++;
        }

        if(e.getKeyCode() == KeyEvent.VK_DOWN && (!up)){
            left = false;
            right = false;
            up = false;
            down = true;
            moves++;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private void newEnemy() {
        enemyX = xPos[random.nextInt(34)];
        enemyY = yPos[random.nextInt(23)];

        for(int i = length_of_snake -1; i >= 0; i--){
            if(snake_x_length[i] == enemyX && snake_y_length[i] == enemyY){
                newEnemy();

            }
        }
    }

    private void collidesWithEnemy() {
        if(snake_x_length[0] == enemyX && snake_y_length[0] == enemyY){
            newEnemy();
            length_of_snake++;
            score++;
        }
    }

    private void collidesWithBody() {
        for(int i = length_of_snake; i > 0; i--){
            if(snake_x_length[i] == snake_x_length[0] && snake_y_length[i] ==snake_y_length[0]){
                timer.stop();
                gameOver = true;
            }
        }
    }

    private void restart() {
        gameOver = false;
        moves = 0;
        score = 0;
        length_of_snake = 3;
        left = false;
        right = true;
        up = false;
        down = false;
        timer.start();
        repaint();
    }
}


