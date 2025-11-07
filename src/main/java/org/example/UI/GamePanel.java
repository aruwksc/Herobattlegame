package org.example.UI;

import org.example.base.GameCharacter;
import org.example.facade.GameFacade;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class GamePanel extends JPanel {
    private GameFacade gameFacade;
    private boolean heroAttacking = false;
    private boolean enemyAttacking = false;
    private int attackAnimationFrame = 0;
    private Timer attackTimer;
    private Timer enemyAITimer;

    private java.util.List<Particle> particles = new java.util.ArrayList<>();

    private double heroDisplayX = 0;
    private double heroDisplayY = 0;
    private double enemyDisplayX = 0;
    private double enemyDisplayY = 0;

    public GamePanel(GameFacade gameFacade) {
        this.gameFacade = gameFacade;
        setBackground(new Color(20, 25, 35));
        setFocusable(true);

        Timer particleTimer = new Timer(30, e -> {
            updateParticles();
            updateSmoothMovement();
            repaint();
        });
        particleTimer.start();

        enemyAITimer = new Timer(2000, e -> {
            if (gameFacade.isGameStarted() &&
                    gameFacade.getEnemy() != null &&
                    gameFacade.getEnemy().isAlive() &&
                    gameFacade.getHero() != null &&
                    gameFacade.getHero().isAlive()) {
                gameFacade.enemyAttack();
                triggerAttackAnimation(false);
            }
        });
    }

    public void startEnemyAI() {
        if (enemyAITimer != null && !enemyAITimer.isRunning()) {
            enemyAITimer.start();
        }
    }

    public void stopEnemyAI() {
        if (enemyAITimer != null && enemyAITimer.isRunning()) {
            enemyAITimer.stop();
        }
    }

    private void updateSmoothMovement() {
        GameCharacter hero = gameFacade.getHero();
        GameCharacter enemy = gameFacade.getEnemy();

        double smoothSpeed = 0.15;

        if (hero != null) {
            double targetX = hero.getX();
            double targetY = hero.getY();

            heroDisplayX += (targetX - heroDisplayX) * smoothSpeed;
            heroDisplayY += (targetY - heroDisplayY) * smoothSpeed;
        }

        if (enemy != null) {
            double targetX = enemy.getX();
            double targetY = enemy.getY();

            enemyDisplayX += (targetX - enemyDisplayX) * smoothSpeed;
            enemyDisplayY += (targetY - enemyDisplayY) * smoothSpeed;
        }
    }

    public void triggerAttackAnimation(boolean isHero) {
        if (isHero) {
            heroAttacking = true;
        } else {
            enemyAttacking = true;
        }
        attackAnimationFrame = 0;

        if (attackTimer != null) {
            attackTimer.stop();
        }

        attackTimer = new Timer(50, e -> {
            attackAnimationFrame++;
            repaint();
            if (attackAnimationFrame > 10) {
                heroAttacking = false;
                enemyAttacking = false;
                attackTimer.stop();
            }
        });
        attackTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        GradientPaint gradient = new GradientPaint(
                0, 0, new Color(20, 25, 35),
                0, getHeight(), new Color(35, 45, 60)
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        drawBackgroundPattern(g2d);

        if (!gameFacade.isGameStarted()) {
            drawWelcomeScreen(g2d);
            return;
        }

        GameCharacter hero = gameFacade.getHero();
        GameCharacter enemy = gameFacade.getEnemy();

        if (hero != null && heroDisplayX == 0 && heroDisplayY == 0) {
            heroDisplayX = hero.getX();
            heroDisplayY = hero.getY();
        }
        if (enemy != null && enemyDisplayX == 0 && enemyDisplayY == 0) {
            enemyDisplayX = enemy.getX();
            enemyDisplayY = enemy.getY();
        }

        drawArena(g2d);

        if (hero != null) {
            drawCharacter(g2d, (int)heroDisplayX, (int)heroDisplayY, hero, true);
            drawHealthBar(g2d, hero, (int)heroDisplayX, (int)heroDisplayY - 50);
        }

        if (enemy != null) {
            drawCharacter(g2d, (int)enemyDisplayX, (int)enemyDisplayY, enemy, false);
            drawHealthBar(g2d, enemy, (int)enemyDisplayX, (int)enemyDisplayY - 50);
        }

        drawParticles(g2d);

        if (heroAttacking && hero != null && enemy != null) {
            drawAttackEffect(g2d, (int)heroDisplayX + 30, (int)heroDisplayY,
                    (int)enemyDisplayX, (int)enemyDisplayY);
            if (attackAnimationFrame == 1) {
                addAttackParticles((int)heroDisplayX + 30, (int)heroDisplayY);
            }
        }
        if (enemyAttacking && hero != null && enemy != null) {
            drawAttackEffect(g2d, (int)enemyDisplayX - 30, (int)enemyDisplayY,
                    (int)heroDisplayX, (int)heroDisplayY);
            if (attackAnimationFrame == 1) {
                addAttackParticles((int)enemyDisplayX - 30, (int)enemyDisplayY);
            }
        }

        drawStats(g2d);
    }

    private void drawBackgroundPattern(Graphics2D g2d) {
        g2d.setColor(new Color(255, 255, 255, 10));
        for (int i = 0; i < getWidth(); i += 40) {
            g2d.drawLine(i, 0, i, getHeight());
        }
        for (int i = 0; i < getHeight(); i += 40) {
            g2d.drawLine(0, i, getWidth(), i);
        }
    }

    private void drawArena(Graphics2D g2d) {
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = 250;

        g2d.setColor(new Color(50, 60, 75, 100));
        g2d.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2);

        g2d.setColor(new Color(40, 50, 65, 150));
        g2d.fillOval(centerX - radius + 20, centerY - radius + 20,
                (radius - 20) * 2, (radius - 20) * 2);

        g2d.setStroke(new BasicStroke(3));
        g2d.setColor(new Color(100, 150, 200, 200));
        g2d.drawOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
    }

    private void drawWelcomeScreen(Graphics2D g2d) {
        g2d.setColor(new Color(100, 200, 255));
        g2d.setFont(new Font("Arial", Font.BOLD, 48));
        String title = "RPG GAME";
        int titleWidth = g2d.getFontMetrics().stringWidth(title);

        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.drawString(title, (getWidth() - titleWidth) / 2 + 3, getHeight() / 2 - 47);

        GradientPaint titleGradient = new GradientPaint(
                0, getHeight() / 2 - 60, new Color(100, 200, 255),
                0, getHeight() / 2 - 30, new Color(50, 150, 255)
        );
        g2d.setPaint(titleGradient);
        g2d.drawString(title, (getWidth() - titleWidth) / 2, getHeight() / 2 - 50);

        g2d.setColor(new Color(200, 220, 255));
        g2d.setFont(new Font("Arial", Font.PLAIN, 20));
        String subtitle = "Паттерны проектирования";
        int subtitleWidth = g2d.getFontMetrics().stringWidth(subtitle);
        g2d.drawString(subtitle, (getWidth() - subtitleWidth) / 2, getHeight() / 2 + 10);

        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(new Color(100, 150, 200, 100));
        g2d.drawRoundRect(getWidth() / 2 - 250, getHeight() / 2 - 100,
                500, 150, 20, 20);
    }

    private void drawCharacter(Graphics2D g2d, int x, int y, GameCharacter character, boolean isHero) {
        if (!character.isAlive()) {
            g2d.setColor(new Color(100, 100, 100, 150));
            g2d.fillOval(x - 20, y + 15, 40, 15);

            g2d.setColor(new Color(150, 150, 150));
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            g2d.drawString("✕", x - 5, y + 10);
            return;
        }

        g2d.setColor(new Color(0, 0, 0, 80));
        g2d.fillOval(x - 20, y + 35, 40, 10);

        GradientPaint bodyGradient;
        if (isHero) {
            bodyGradient = new GradientPaint(
                    x, y - 10, new Color(70, 130, 220),
                    x, y + 40, new Color(50, 100, 180)
            );
        } else {
            bodyGradient = new GradientPaint(
                    x, y - 10, new Color(220, 50, 50),
                    x, y + 40, new Color(180, 30, 30)
            );
        }

        g2d.setPaint(bodyGradient);
        RoundRectangle2D body = new RoundRectangle2D.Double(x - 15, y, 30, 40, 10, 10);
        g2d.fill(body);

        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.setStroke(new BasicStroke(2));
        g2d.draw(body);

        GradientPaint headGradient = new GradientPaint(
                x, y - 30, new Color(255, 230, 190),
                x, y - 5, new Color(240, 210, 170)
        );
        g2d.setPaint(headGradient);
        g2d.fillOval(x - 15, y - 30, 30, 30);

        g2d.setColor(new Color(0, 0, 0, 50));
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawOval(x - 15, y - 30, 30, 30);

        g2d.setColor(Color.BLACK);
        g2d.fillOval(x - 8, y - 20, 5, 5);
        g2d.fillOval(x + 3, y - 20, 5, 5);

        g2d.setColor(Color.WHITE);
        g2d.fillOval(x - 6, y - 19, 2, 2);
        g2d.fillOval(x + 5, y - 19, 2, 2);

        if (isHero) {
            g2d.setColor(new Color(255, 215, 0, 200));
            g2d.setStroke(new BasicStroke(4));
            g2d.drawLine(x + 18, y + 10, x + 35, y + 10);

            g2d.setColor(new Color(255, 255, 100));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawLine(x + 18, y + 10, x + 35, y + 10);
        } else {
            g2d.setColor(new Color(150, 150, 150, 200));
            g2d.setStroke(new BasicStroke(4));
            g2d.drawLine(x - 35, y + 10, x - 18, y + 10);

            g2d.setColor(new Color(200, 200, 200));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawLine(x - 35, y + 10, x - 18, y + 10);
        }

        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        String name = character.getName();
        int nameWidth = g2d.getFontMetrics().stringWidth(name);

        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.drawString(name, x - nameWidth / 2 + 1, y - 36);

        g2d.setColor(Color.WHITE);
        g2d.drawString(name, x - nameWidth / 2, y - 37);
    }

    private void drawHealthBar(Graphics2D g2d, GameCharacter character, int x, int y) {
        int barWidth = 100;
        int barHeight = 12;
        int padding = 2;

        GradientPaint frameGradient = new GradientPaint(
                x - barWidth / 2, y, new Color(60, 70, 90),
                x + barWidth / 2, y, new Color(80, 90, 110)
        );
        g2d.setPaint(frameGradient);
        g2d.fillRoundRect(x - barWidth / 2 - padding, y - padding,
                barWidth + padding * 2, barHeight + padding * 2, 8, 8);

        g2d.setColor(new Color(30, 30, 40));
        g2d.fillRoundRect(x - barWidth / 2, y, barWidth, barHeight, 6, 6);

        float healthPercent = (float) character.getHealth() / character.getMaxHealth();
        int healthWidth = (int) (barWidth * healthPercent);

        if (healthWidth > 0) {
            Color color1, color2;
            if (healthPercent > 0.6) {
                color1 = new Color(50, 255, 100);
                color2 = new Color(30, 200, 80);
            } else if (healthPercent > 0.3) {
                color1 = new Color(255, 220, 50);
                color2 = new Color(255, 180, 30);
            } else {
                color1 = new Color(255, 80, 80);
                color2 = new Color(220, 40, 40);
            }

            GradientPaint healthGradient = new GradientPaint(
                    x - barWidth / 2, y, color1,
                    x - barWidth / 2 + healthWidth, y, color2
            );
            g2d.setPaint(healthGradient);
            g2d.fillRoundRect(x - barWidth / 2, y, healthWidth, barHeight, 6, 6);
        }

        g2d.setColor(new Color(200, 220, 255));
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawRoundRect(x - barWidth / 2, y, barWidth, barHeight, 6, 6);

        g2d.setFont(new Font("Arial", Font.BOLD, 11));
        String healthText = character.getHealth() + "/" + character.getMaxHealth();
        int textWidth = g2d.getFontMetrics().stringWidth(healthText);

        g2d.setColor(new Color(0, 0, 0, 200));
        g2d.drawString(healthText, x - textWidth / 2 + 1, y + 24);

        g2d.setColor(Color.WHITE);
        g2d.drawString(healthText, x - textWidth / 2, y + 23);
    }

    private void drawAttackEffect(Graphics2D g2d, int fromX, int fromY, int toX, int toY) {
        float progress = attackAnimationFrame / 10.0f;
        int currentX = (int) (fromX + (toX - fromX) * progress);
        int currentY = (int) (fromY + (toY - fromY) * progress);

        g2d.setColor(new Color(255, 255, 100, 200));
        g2d.setStroke(new BasicStroke(5));
        g2d.drawLine(fromX, fromY, currentX, currentY);

        g2d.setColor(new Color(255, 255, 200, 250));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(fromX, fromY, currentX, currentY);

        if (progress > 0.8) {
            int impactSize = (int) (50 * (1 - (progress - 0.8) / 0.2));

            for (int i = 0; i < 3; i++) {
                int alpha = 100 - i * 30;
                g2d.setColor(new Color(255, 150, 0, alpha));
                g2d.fillOval(toX - impactSize - i * 5, toY - impactSize - i * 5,
                        (impactSize + i * 5) * 2, (impactSize + i * 5) * 2);
            }

            g2d.setColor(new Color(255, 255, 255, 200));
            g2d.fillOval(toX - impactSize / 2, toY - impactSize / 2,
                    impactSize, impactSize);
        }
    }

    private void drawStats(Graphics2D g2d) {
        GameCharacter hero = gameFacade.getHero();
        GameCharacter enemy = gameFacade.getEnemy();

        if (hero != null) {
            drawStatPanel(g2d, 15, 20, "ГЕРОЙ", hero, true);
        }

        if (enemy != null) {
            drawStatPanel(g2d, getWidth() - 215, 20, "ПРОТИВНИК", enemy, false);
        }

        drawControls(g2d);
    }

    private void drawStatPanel(Graphics2D g2d, int x, int y, String title,
                               GameCharacter character, boolean isHero) {
        int width = 200;
        int height = 120;

        GradientPaint panelGradient = new GradientPaint(
                x, y, new Color(30, 40, 60, 200),
                x, y + height, new Color(40, 50, 70, 200)
        );
        g2d.setPaint(panelGradient);
        g2d.fillRoundRect(x, y, width, height, 15, 15);

        g2d.setColor(new Color(100, 150, 200, 150));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(x, y, width, height, 15, 15);

        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        if (isHero) {
            g2d.setColor(new Color(100, 200, 255));
        } else {
            g2d.setColor(new Color(255, 100, 100));
        }
        g2d.drawString(title, x + 10, y + 25);

        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        g2d.setColor(Color.WHITE);

        int lineY = y + 50;
        g2d.drawString("⚔ Атака: " + character.getAttack(), x + 10, lineY);
        lineY += 22;
        g2d.drawString("🛡 Защита: " + character.getDefense(), x + 10, lineY);
        lineY += 22;

        float healthPercent = (float) character.getHealth() / character.getMaxHealth() * 100;
        String healthStatus;
        if (healthPercent > 60) {
            g2d.setColor(new Color(100, 255, 100));
            healthStatus = "Отличное";
        } else if (healthPercent > 30) {
            g2d.setColor(new Color(255, 220, 100));
            healthStatus = "Среднее";
        } else {
            g2d.setColor(new Color(255, 100, 100));
            healthStatus = "Критическое";
        }
        g2d.drawString("❤ Состояние: " + healthStatus, x + 10, lineY);
    }

    private void drawControls(Graphics2D g2d) {
        int x = 15;
        int y = getHeight() - 140;
        int width = 220;
        int height = 130;

        g2d.setColor(new Color(20, 30, 45, 180));
        g2d.fillRoundRect(x, y, width, height, 15, 15);

        g2d.setColor(new Color(100, 150, 200, 100));
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawRoundRect(x, y, width, height, 15, 15);

        g2d.setColor(new Color(150, 200, 255));
        g2d.setFont(new Font("Arial", Font.BOLD, 13));
        g2d.drawString("УПРАВЛЕНИЕ", x + 10, y + 20);

        g2d.setFont(new Font("Arial", Font.PLAIN, 11));
        g2d.setColor(new Color(220, 220, 220));

        int lineY = y + 40;
        String[] controls = {
                "WASD - движение",
                "Space - атака",
                "1,2,3 - смена атаки",
                "Q,E,R - баффы",
                "F5 - новая игра"
        };

        for (String control : controls) {
            g2d.drawString("• " + control, x + 10, lineY);
            lineY += 18;
        }
    }

    private static class Particle {
        double x, y;
        double vx, vy;
        int life;
        Color color;

        Particle(double x, double y, double vx, double vy, Color color) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
            this.life = 30;
            this.color = color;
        }

        void update() {
            x += vx;
            y += vy;
            vy += 0.2;
            life--;
        }

        boolean isAlive() {
            return life > 0;
        }
    }

    private void addAttackParticles(int x, int y) {
        for (int i = 0; i < 5; i++) {
            double angle = Math.random() * Math.PI * 2;
            double speed = 2 + Math.random() * 3;
            double vx = Math.cos(angle) * speed;
            double vy = Math.sin(angle) * speed;

            Color[] colors = {
                    new Color(255, 200, 0),
                    new Color(255, 150, 0),
                    new Color(255, 100, 0)
            };
            Color color = colors[(int)(Math.random() * colors.length)];

            particles.add(new Particle(x, y, vx, vy, color));
        }
    }

    private void updateParticles() {
        particles.removeIf(p -> !p.isAlive());
        particles.forEach(Particle::update);
    }

    private void drawParticles(Graphics2D g2d) {
        for (Particle p : particles) {
            int alpha = (int) (255.0 * p.life / 30.0);
            g2d.setColor(new Color(p.color.getRed(), p.color.getGreen(),
                    p.color.getBlue(), alpha));
            g2d.fillOval((int)p.x - 3, (int)p.y - 3, 6, 6);
        }
    }
}