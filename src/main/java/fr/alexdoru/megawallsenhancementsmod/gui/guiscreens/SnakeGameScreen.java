package fr.alexdoru.megawallsenhancementsmod.gui.guiscreens;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumChatFormatting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SnakeGameScreen extends GuiScreen {

    private final GuiScreen parent;
    private int tickCounter;
    private float snakeX, snakeY;
    private int direction; // 0 = Up, 1 = Down, 2 = Left, 3 = Right
    private static final int GAME_WIDTH = 200;
    private static final int GAME_HEIGHT = 200;
    private static final int SNAKE_SIZE = 10;
    private int speed = 3; // Faster snake speed in ticks

    private List<float[]> snakeBody = new ArrayList<>();
    private float[] apple = new float[2];
    private int score = 0;
    private int highScore = 0;
    private boolean gameOver = false;

    private Random random = new Random();

    public SnakeGameScreen(GuiScreen parent) {
        this.parent = parent;
        this.snakeX = GAME_WIDTH / 2;
        this.snakeY = GAME_HEIGHT / 2;
        this.direction = 3; // Start moving to the right
        this.snakeBody.add(new float[]{snakeX, snakeY});
        spawnApple();
    }

    @Override
    public void initGui() {
        this.tickCounter = 0;
        this.snakeX = (this.width - GAME_WIDTH) / 2;
        this.snakeY = (this.height - GAME_HEIGHT) / 2;
        this.snakeBody.clear();
        this.snakeBody.add(new float[]{snakeX, snakeY});
        this.score = 0;
        this.gameOver = false;
        spawnApple();
    }

    private void spawnApple() {
        int gameX = (this.width - GAME_WIDTH) / 2;
        int gameY = (this.height - GAME_HEIGHT) / 2;
        apple[0] = gameX + random.nextInt(GAME_WIDTH / SNAKE_SIZE) * SNAKE_SIZE;
        apple[1] = gameY + random.nextInt(GAME_HEIGHT / SNAKE_SIZE) * SNAKE_SIZE;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // Set the background color with transparency
        drawRect(0, 0, this.width, this.height, 0x80000000); // Black with 50% transparency

        int gameX = (this.width - GAME_WIDTH) / 2;
        int gameY = (this.height - GAME_HEIGHT) / 2;

        // Calculate interpolated position for smooth movement
        float interpolatedX = snakeBody.get(0)[0];
        float interpolatedY = snakeBody.get(0)[1];
        if (snakeBody.size() > 1) {
            float previousX = snakeBody.get(1)[0];
            float previousY = snakeBody.get(1)[1];
            interpolatedX = previousX + (interpolatedX - previousX) * partialTicks;
            interpolatedY = previousY + (interpolatedY - previousY) * partialTicks;
        }

        // Draw the game area
        drawRect(gameX, gameY, gameX + GAME_WIDTH, gameY + GAME_HEIGHT, 0x80000000); // Black background with 50% transparency

        // Draw the snake (white color)
        for (int i = 0; i < snakeBody.size(); i++) {
            float[] segment = snakeBody.get(i);
            int color = i == 0 ? 0xFFFFFFFF : 0xFFFFFFFF; // White color
            drawRect((int)segment[0], (int)segment[1], (int)segment[0] + SNAKE_SIZE, (int)segment[1] + SNAKE_SIZE, color);
        }

        // Draw the apple (white color)
        drawRect((int)apple[0], (int)apple[1], (int)apple[0] + SNAKE_SIZE, (int)apple[1] + SNAKE_SIZE, 0xFFFFFFFF); // White apple

        // Draw the game over text
        if (gameOver) {
            drawCenteredString(this.fontRendererObj, EnumChatFormatting.WHITE + "Game Over! Press R to Restart", this.width / 2, gameY + GAME_HEIGHT / 2, 0xFFFFFF);
        }

        // Draw the score
        drawCenteredString(this.fontRendererObj, EnumChatFormatting.WHITE + "Score: " + score + " | High Score: " + highScore, this.width / 2, gameY - 20, 0xFFFFFF);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) { // Escape key
            mc.displayGuiScreen(this.parent);
        } else if (!gameOver) {
            if (keyCode == 200 && direction != 1) { // Up arrow key
                direction = 0;
            } else if (keyCode == 208 && direction != 0) { // Down arrow key
                direction = 1;
            } else if (keyCode == 203 && direction != 3) { // Left arrow key
                direction = 2;
            } else if (keyCode == 205 && direction != 2) { // Right arrow key
                direction = 3;
            }
        }
        if (gameOver && keyCode == 19) { // 'R' key to restart
            if (score > highScore) {
                highScore = score;
            }
            initGui();
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void updateScreen() {
        this.tickCounter++;
        if (!gameOver && tickCounter % speed == 0) { // Move the snake every 'speed' ticks
            moveSnake();
        }
    }

    private void moveSnake() {
        float[] newHead = new float[2];
        switch (direction) {
            case 0:
                newHead[1] = snakeBody.get(0)[1] - SNAKE_SIZE;
                newHead[0] = snakeBody.get(0)[0];
                break; // Up
            case 1:
                newHead[1] = snakeBody.get(0)[1] + SNAKE_SIZE;
                newHead[0] = snakeBody.get(0)[0];
                break; // Down
            case 2:
                newHead[0] = snakeBody.get(0)[0] - SNAKE_SIZE;
                newHead[1] = snakeBody.get(0)[1];
                break; // Left
            case 3:
                newHead[0] = snakeBody.get(0)[0] + SNAKE_SIZE;
                newHead[1] = snakeBody.get(0)[1];
                break; // Right
        }

        snakeBody.add(0, newHead);

        // Check for collision with walls
        int gameX = (this.width - GAME_WIDTH) / 2;
        int gameY = (this.height - GAME_HEIGHT) / 2;
        if (newHead[0] < gameX || newHead[0] >= gameX + GAME_WIDTH || newHead[1] < gameY || newHead[1] >= gameY + GAME_HEIGHT) {
            gameOver = true;
            return;
        }

        // Check for collision with itself
        for (int i = 1; i < snakeBody.size(); i++) {
            if (snakeBody.get(i)[0] == newHead[0] && snakeBody.get(i)[1] == newHead[1]) {
                gameOver = true;
                return;
            }
        }

        // Check for collision with apple
        if (newHead[0] == apple[0] && newHead[1] == apple[1]) {
            score++;
            spawnApple();
        } else {
            snakeBody.remove(snakeBody.size() - 1);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return true; // Pauses the game while the GUI is open
    }
}
