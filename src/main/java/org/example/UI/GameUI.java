package org.example.UI;

import org.example.base.GameCharacter;
import org.example.facade.GameFacade;
import org.example.observer.GameObserver;
import org.example.strategy.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameUI extends JFrame implements GameObserver {
    private GameFacade gameFacade;
    private GamePanel gamePanel;
    private JPanel controlPanel;
    private JTextArea logArea;
    private JButton attackButton, meleeButton, rangedButton, magicButton;
    private JButton shieldButton, powerButton, healthButton;
    private Timer gameTimer;
    private java.util.List<String> logs = new java.util.ArrayList<>();

    public GameUI(GameFacade gameFacade) {
        this.gameFacade = gameFacade;
        gameFacade.getObserverBus().addObserver(this);

        setTitle("RPG Game ");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initComponents();
        setupKeyBindings();
        showStartDialog();
    }

    private void showStartDialog() {
        JDialog dialog = new JDialog(this, "Создание персонажа", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(5, 2, 10, 10));

        JLabel nameLabel = new JLabel("Имя героя:");
        JTextField nameField = new JTextField("Герой");

        JLabel heroTypeLabel = new JLabel("Класс героя:");
        String[] heroTypes = {"Warrior", "Mage", "Archer"};
        JComboBox<String> heroTypeCombo = new JComboBox<>(heroTypes);

        JLabel enemyRaceLabel = new JLabel("Раса противника:");
        String[] enemyRaces = {"Human", "Elf", "Orc", "Demon"};
        JComboBox<String> enemyRaceCombo = new JComboBox<>(enemyRaces);

        JLabel enemyTypeLabel = new JLabel("Класс противника:");
        String[] enemyTypes = {"Warrior", "Mage", "Archer"};
        JComboBox<String> enemyTypeCombo = new JComboBox<>(enemyTypes);

        JButton startButton = new JButton("Начать игру");

        startButton.addActionListener(e -> {
            String heroName = nameField.getText();
            String heroType = (String)heroTypeCombo.getSelectedItem();
            String enemyRace = (String)enemyRaceCombo.getSelectedItem();
            String enemyType = (String)enemyTypeCombo.getSelectedItem();

            gameFacade.createHero(heroType, heroName);
            gameFacade.createEnemy(enemyRace, enemyType);
            gameFacade.setGameStarted(true);

            gamePanel.repaint();
            dialog.dispose();
            startGameLoop();

            // Запускаем AI противника
            gamePanel.startEnemyAI();
        });

        dialog.add(nameLabel);
        dialog.add(nameField);
        dialog.add(heroTypeLabel);
        dialog.add(heroTypeCombo);
        dialog.add(enemyRaceLabel);
        dialog.add(enemyRaceCombo);
        dialog.add(enemyTypeLabel);
        dialog.add(enemyTypeCombo);
        dialog.add(new JLabel());
        dialog.add(startButton);

        dialog.setVisible(true);
    }

    private void initComponents() {
        // Game Panel
        gamePanel = new GamePanel(gameFacade);
        add(gamePanel, BorderLayout.CENTER);

        // Control Panel
        controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(3, 4, 5, 5));
        controlPanel.setPreferredSize(new Dimension(1000, 120));

        attackButton = new JButton("Атаковать (Space)");
        attackButton.addActionListener(e -> performAttack());

        meleeButton = new JButton("Ближний бой (1)");
        meleeButton.addActionListener(e -> gameFacade.changeHeroAttackStrategy(new MeleeAttack()));

        rangedButton = new JButton("Дальний бой (2)");
        rangedButton.addActionListener(e -> gameFacade.changeHeroAttackStrategy(new RangedAttack()));

        magicButton = new JButton("Магия (3)");
        magicButton.addActionListener(e -> gameFacade.changeHeroAttackStrategy(new MagicAttack()));

        shieldButton = new JButton("Щит (Q)");
        shieldButton.addActionListener(e -> gameFacade.applyShieldBuff());

        powerButton = new JButton("Усиление (E)");
        powerButton.addActionListener(e -> gameFacade.applyPowerBuff());

        healthButton = new JButton("Лечение (R)");
        healthButton.addActionListener(e -> gameFacade.applyHealthBuff());

        JButton resetButton = new JButton("Новая игра (F5)");
        resetButton.addActionListener(e -> resetGame());

        controlPanel.add(attackButton);
        controlPanel.add(meleeButton);
        controlPanel.add(rangedButton);
        controlPanel.add(magicButton);
        controlPanel.add(shieldButton);
        controlPanel.add(powerButton);
        controlPanel.add(healthButton);
        controlPanel.add(resetButton);

        add(controlPanel, BorderLayout.SOUTH);

        // Log Area
        logArea = new JTextArea(10, 30);
        logArea.setEditable(false);
        logArea.setBackground(new Color(30, 30, 40));
        logArea.setForeground(new Color(200, 200, 200));
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setPreferredSize(new Dimension(250, 0));
        add(scrollPane, BorderLayout.EAST);
    }

    private void setupKeyBindings() {
        JPanel contentPane = (JPanel) getContentPane();
        InputMap inputMap = contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = contentPane.getActionMap();

        // Movement with smooth scaling
        inputMap.put(KeyStroke.getKeyStroke('w'), "moveUp");
        actionMap.put("moveUp", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                gameFacade.moveHero(0, -10);
                gamePanel.repaint();
            }
        });

        inputMap.put(KeyStroke.getKeyStroke('s'), "moveDown");
        actionMap.put("moveDown", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                gameFacade.moveHero(0, 10);
                gamePanel.repaint();
            }
        });

        inputMap.put(KeyStroke.getKeyStroke('a'), "moveLeft");
        actionMap.put("moveLeft", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                gameFacade.moveHero(-10, 0);
                gamePanel.repaint();
            }
        });

        inputMap.put(KeyStroke.getKeyStroke('d'), "moveRight");
        actionMap.put("moveRight", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                gameFacade.moveHero(10, 0);
                gamePanel.repaint();
            }
        });

        // Attack
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "attack");
        actionMap.put("attack", new AbstractAction() {
            public void actionPerformed(ActionEvent e) { performAttack(); }
        });

        // Strategies
        inputMap.put(KeyStroke.getKeyStroke('1'), "melee");
        actionMap.put("melee", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                gameFacade.changeHeroAttackStrategy(new MeleeAttack());
                log("Выбрана стратегия: Ближний бой");
            }
        });

        inputMap.put(KeyStroke.getKeyStroke('2'), "ranged");
        actionMap.put("ranged", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                gameFacade.changeHeroAttackStrategy(new RangedAttack());
                log("Выбрана стратегия: Дальний бой");
            }
        });

        inputMap.put(KeyStroke.getKeyStroke('3'), "magic");
        actionMap.put("magic", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                gameFacade.changeHeroAttackStrategy(new MagicAttack());
                log("Выбрана стратегия: Магия");
            }
        });

        // Buffs
        inputMap.put(KeyStroke.getKeyStroke('q'), "shield");
        actionMap.put("shield", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                gameFacade.applyShieldBuff();
                log("Применен бафф: Щит");
            }
        });

        inputMap.put(KeyStroke.getKeyStroke('e'), "power");
        actionMap.put("power", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                gameFacade.applyPowerBuff();
                log("Применен бафф: Усиление");
            }
        });

        inputMap.put(KeyStroke.getKeyStroke('r'), "health");
        actionMap.put("health", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                gameFacade.applyHealthBuff();
                log("Применен бафф: Лечение");
            }
        });

        // Reset
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), "reset");
        actionMap.put("reset", new AbstractAction() {
            public void actionPerformed(ActionEvent e) { resetGame(); }
        });
    }

    private void performAttack() {
        if (gameFacade.isGameStarted()) {
            gameFacade.heroAttack();
            gamePanel.triggerAttackAnimation(true);
            gamePanel.repaint();
        }
    }

    private void startGameLoop() {
        gameTimer = new Timer(100, e -> {
            gamePanel.repaint();
            updateLog();

            // Check game over
            if (gameFacade.getHero() != null && !gameFacade.getHero().isAlive()) {
                gameTimer.stop();
                gamePanel.stopEnemyAI();
                JOptionPane.showMessageDialog(this, "Вы проиграли!", "Игра окончена", JOptionPane.ERROR_MESSAGE);
            } else if (gameFacade.getEnemy() != null && !gameFacade.getEnemy().isAlive()) {
                gameTimer.stop();
                gamePanel.stopEnemyAI();
                JOptionPane.showMessageDialog(this, "Вы победили!", "Победа!", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        gameTimer.start();
    }

    private void updateLog() {
        StringBuilder sb = new StringBuilder();
        int start = Math.max(0, logs.size() - 20);
        for (int i = start; i < logs.size(); i++) {
            sb.append(logs.get(i)).append("\n");
        }
        logArea.setText(sb.toString());
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    private void log(String message) {
        String timestamp = new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
        String logMessage = "[" + timestamp + "] " + message;
        logs.add(logMessage);
        updateLog();
    }

    private void resetGame() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
        gamePanel.stopEnemyAI();
        logs.clear();
        gameFacade.resetGame();
        showStartDialog();
    }

    @Override
    public void onHealthChanged(GameCharacter gameCharacter, int oldHealth, int newHealth) {
        log(gameCharacter.getName() + " HP: " + oldHealth + " -> " + newHealth);
        gamePanel.repaint();
    }

    @Override
    public void onBattleEvent(String event) {
        log(event);
    }

    @Override
    public void onCharacterDeath(GameCharacter gameCharacter) {
        log(gameCharacter.getName() + " повержен!");
        gamePanel.repaint();
    }
}