
// NEW: quite good GUI look
import javax.swing.*;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class GUI extends JFrame {
  private JTextPane configDisplay;
  private JTextArea configEditArea;
  private JTextArea inputArea;
  private JTextArea processedOutputArea;
  private JButton runButton;
  private JLabel statusLabel;
  private String rawConfigText = "";

  // Modern color palette - DARKER VERSION
  private static final Color DARK_NAVY = new Color(5, 10, 20);
  private static final Color DEEP_BLUE = new Color(10, 15, 30);
  private static final Color OCEAN_BLUE = new Color(20, 35, 80);
  private static final Color TEAL_ACCENT = new Color(20, 184, 166);
  private static final Color PURPLE_ACCENT = new Color(139, 92, 246);
  private static final Color PINK_ACCENT = new Color(236, 72, 153);
  private static final Color TEXT_PRIMARY = new Color(241, 245, 249);
  private static final Color TEXT_SECONDARY = new Color(148, 163, 184);
  private static final Color CARD_BG = new Color(10, 15, 30, 240);
  private static final Color BORDER_GLOW = new Color(20, 184, 166, 60);

  public GUI() {
    setTitle("Obscura");
    setSize(1200, 850);
    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    setLocationRelativeTo(null);

    // Add window listener for auto-cleanup on close
    addWindowListener(new java.awt.event.WindowAdapter() {
      @Override
      public void windowClosing(java.awt.event.WindowEvent windowEvent) {
        cleanupFiles();
        System.exit(0);
      }
    });

    // Create main panel with stunning gradient background
    JPanel mainPanel = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Multi-stop gradient for aurora effect - DARKER
        GradientPaint gradient1 = new GradientPaint(
            0, 0, DARK_NAVY,
            0, getHeight() / 2, new Color(8, 15, 35));
        g2d.setPaint(gradient1);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        GradientPaint gradient2 = new GradientPaint(
            0, getHeight() / 2, new Color(8, 15, 35),
            0, getHeight(), new Color(15, 8, 30));
        g2d.setPaint(gradient2);
        g2d.fillRect(0, getHeight() / 2, getWidth(), getHeight() / 2);

        // Add aurora-like radial gradients - MORE SUBTLE
        RadialGradientPaint tealGlow = new RadialGradientPaint(
            getWidth() * 0.2f, getHeight() * 0.3f,
            getWidth() * 0.5f,
            new float[] { 0.0f, 0.6f, 1.0f },
            new Color[] {
                new Color(20, 184, 166, 25),
                new Color(20, 184, 166, 10),
                new Color(20, 184, 166, 0)
            });
        g2d.setPaint(tealGlow);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        RadialGradientPaint purpleGlow = new RadialGradientPaint(
            getWidth() * 0.8f, getHeight() * 0.6f,
            getWidth() * 0.4f,
            new float[] { 0.0f, 0.6f, 1.0f },
            new Color[] {
                new Color(139, 92, 246, 20),
                new Color(139, 92, 246, 8),
                new Color(139, 92, 246, 0)
            });
        g2d.setPaint(purpleGlow);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        RadialGradientPaint pinkGlow = new RadialGradientPaint(
            getWidth() * 0.5f, getHeight() * 0.8f,
            getWidth() * 0.3f,
            new float[] { 0.0f, 0.7f, 1.0f },
            new Color[] {
                new Color(236, 72, 153, 18),
                new Color(236, 72, 153, 6),
                new Color(236, 72, 153, 0)
            });
        g2d.setPaint(pinkGlow);
        g2d.fillRect(0, 0, getWidth(), getHeight());
      }
    };
    mainPanel.setLayout(new BorderLayout(15, 15));
    mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

    // Stunning title panel with gradient text effect
    JPanel titlePanel = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
      }
    };
    titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
    titlePanel.setOpaque(false);
    titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

    // Main title
    JLabel titleLabel = new JLabel("obscura") {
      @Override
      protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Gradient text effect
        GradientPaint gradient = new GradientPaint(
            0, 0, TEAL_ACCENT,
            getWidth(), 0, PURPLE_ACCENT);
        g2d.setPaint(gradient);
        g2d.setFont(getFont());
        FontMetrics fm = g2d.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(getText())) / 2;
        int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();

        // Subtle glow effect
        g2d.setColor(new Color(20, 184, 166, 50));
        g2d.drawString(getText(), x - 1, y - 1);
        g2d.drawString(getText(), x + 1, y + 1);

        // Main text
        g2d.setPaint(gradient);
        g2d.drawString(getText(), x, y);
      }
    };
    titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
    titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    titleLabel.setPreferredSize(new Dimension(300, 70));

    // Subtitle
    JLabel subtitleLabel = new JLabel("by Bogdan Trigubov");
    subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    subtitleLabel.setForeground(TEXT_SECONDARY);
    subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

    titlePanel.add(titleLabel);
    titlePanel.add(Box.createVerticalStrut(5));
    titlePanel.add(subtitleLabel);

    mainPanel.add(titlePanel, BorderLayout.NORTH);

    // Standard tabbed pane
    JTabbedPane tabbedPane = new JTabbedPane();
    tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    tabbedPane.setBackground(new Color(30, 41, 59));
    tabbedPane.setForeground(new Color(219, 234, 254));

    // Input/Output Tab
    JPanel splitPanel = createStyledPanel();
    splitPanel.setLayout(new BorderLayout(10, 10));

    // Input section
    JPanel inputPanel = createStyledPanel();
    inputPanel.setLayout(new BorderLayout(5, 5));

    JLabel inputLabel = createStyledLabel("Input", 16, true);
    inputLabel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

    inputArea = createStyledTextArea();
    JScrollPane inputScrollPane = createStyledScrollPane(inputArea);

    inputPanel.add(inputLabel, BorderLayout.NORTH);
    inputPanel.add(inputScrollPane, BorderLayout.CENTER);

    // Output section
    JPanel outputPanel = createStyledPanel();
    outputPanel.setLayout(new BorderLayout(5, 5));

    JPanel outputHeaderPanel = new JPanel(new BorderLayout());
    outputHeaderPanel.setOpaque(false);
    outputHeaderPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

    JLabel outputLabel = createStyledLabel("Output", 16, true);
    JButton copyButton = createGlowButton("Copy", PURPLE_ACCENT, new Dimension(100, 35));

    outputHeaderPanel.add(outputLabel, BorderLayout.WEST);
    outputHeaderPanel.add(copyButton, BorderLayout.EAST);

    processedOutputArea = createStyledTextArea();
    processedOutputArea.setEditable(false);
    processedOutputArea.setForeground(TEAL_ACCENT);
    JScrollPane outputScrollPane = createStyledScrollPane(processedOutputArea);

    outputPanel.add(outputHeaderPanel, BorderLayout.NORTH);
    outputPanel.add(outputScrollPane, BorderLayout.CENTER);

    // Split pane with custom styling
    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, inputPanel, outputPanel);
    splitPane.setDividerLocation(550);
    splitPane.setResizeWeight(0.5);
    splitPane.setDividerSize(3);
    splitPane.setBorder(BorderFactory.createEmptyBorder());
    splitPane.setOpaque(false);
    splitPane.setBackground(new Color(0, 0, 0, 0));

    splitPanel.add(splitPane, BorderLayout.CENTER);
    tabbedPane.addTab("  Input / Output  ", splitPanel);

    // Configuration Tab WITH LIVE PREVIEW
    JPanel configPanel = createStyledPanel();
    configPanel.setLayout(new BorderLayout(5, 5));

    JPanel configHeaderPanel = new JPanel(new BorderLayout());
    configHeaderPanel.setOpaque(false);
    configHeaderPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

    JLabel configLabel = createStyledLabel("Configuration", 16, true);
    JButton saveConfigButton = createGlowButton("Save Config", TEAL_ACCENT, new Dimension(120, 35));

    configHeaderPanel.add(configLabel, BorderLayout.WEST);
    configHeaderPanel.add(saveConfigButton, BorderLayout.EAST);

    configPanel.add(configHeaderPanel, BorderLayout.NORTH);

    // Config editor (left side)
    configEditArea = createStyledTextArea();
    configEditArea.setFont(new Font("JetBrains Mono", Font.PLAIN, 13));

    configEditArea.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
      public void insertUpdate(javax.swing.event.DocumentEvent e) {
        updateRender();
      }

      public void removeUpdate(javax.swing.event.DocumentEvent e) {
        updateRender();
      }

      public void changedUpdate(javax.swing.event.DocumentEvent e) {
        updateRender();
      }

      private void updateRender() {
        SwingUtilities.invokeLater(() -> updateMarkdownRender());
      }
    });

    // Config live preview (right side)
    configDisplay = new JTextPane();
    configDisplay.setEditable(false);
    configDisplay.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    configDisplay.setBackground(DEEP_BLUE);
    configDisplay.setForeground(TEXT_PRIMARY);
    configDisplay.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    configDisplay.setOpaque(false);

    HTMLEditorKit configKit = new HTMLEditorKit();
    configDisplay.setEditorKit(configKit);

    // Split pane for editor and preview
    JSplitPane configSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    configSplitPane.setDividerLocation(550);
    configSplitPane.setResizeWeight(0.5);
    configSplitPane.setDividerSize(3);
    configSplitPane.setBorder(BorderFactory.createEmptyBorder());
    configSplitPane.setOpaque(false);

    JScrollPane editScrollPane = createStyledScrollPane(configEditArea);
    JScrollPane displayScrollPane = createStyledScrollPane(configDisplay);

    configSplitPane.setLeftComponent(editScrollPane);
    configSplitPane.setRightComponent(displayScrollPane);

    configPanel.add(configSplitPane, BorderLayout.CENTER);
    tabbedPane.addTab("  Configuration  ", configPanel);

    mainPanel.add(tabbedPane, BorderLayout.CENTER);

    // Bottom panel with buttons and status
    JPanel bottomPanel = new JPanel(new BorderLayout());
    bottomPanel.setOpaque(false);
    bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

    // Button panel
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
    buttonPanel.setOpaque(false);

    runButton = createGlowButton("Run", TEAL_ACCENT, new Dimension(140, 50));
    runButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
    JButton clearButton = createGlowButton("Clear", new Color(100, 116, 139), new Dimension(140, 50));
    clearButton.setFont(new Font("Segoe UI", Font.BOLD, 16));

    buttonPanel.add(runButton);
    buttonPanel.add(clearButton);

    // Status label with glow effect
    statusLabel = new JLabel("Ready") {
      @Override
      protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Glow effect
        g2d.setColor(new Color(20, 184, 166, 30));
        FontMetrics fm = g2d.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(getText())) / 2;
        int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
        g2d.drawString(getText(), x - 1, y);
        g2d.drawString(getText(), x + 1, y);
        g2d.drawString(getText(), x, y - 1);
        g2d.drawString(getText(), x, y + 1);

        super.paintComponent(g);
      }
    };
    statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    statusLabel.setForeground(TEAL_ACCENT);
    statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
    statusLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

    bottomPanel.add(buttonPanel, BorderLayout.CENTER);
    bottomPanel.add(statusLabel, BorderLayout.SOUTH);

    mainPanel.add(bottomPanel, BorderLayout.SOUTH);

    // Action listeners
    runButton.addActionListener(e -> runConfigScript());
    saveConfigButton.addActionListener(e -> saveConfigFile());
    clearButton.addActionListener(e -> {
      inputArea.setText("");
      processedOutputArea.setText("// Output cleared\n// Add input text and click Run to process...");
      statusLabel.setText("Input and output cleared");
    });

    copyButton.addActionListener(e -> {
      String outputText = processedOutputArea.getText();
      if (!outputText.isEmpty() && !outputText.startsWith("//")) {
        StringSelection selection = new StringSelection(outputText);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
        statusLabel.setText("✓ Output copied to clipboard!");
      } else {
        statusLabel.setText("No output to copy");
      }
    });

    loadInputOutputFiles();
    openConfigFile();

    add(mainPanel);
  }

  private JPanel createStyledPanel() {
    JPanel panel = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Card background
        g2d.setColor(CARD_BG);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

        // Subtle border glow
        g2d.setColor(BORDER_GLOW);
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
      }
    };
    panel.setOpaque(false);
    return panel;
  }

  private JLabel createStyledLabel(String text, int fontSize, boolean bold) {
    JLabel label = new JLabel(text);
    label.setFont(new Font("Segoe UI", bold ? Font.BOLD : Font.PLAIN, fontSize));
    label.setForeground(TEXT_PRIMARY);
    return label;
  }

  private JTextArea createStyledTextArea() {
    JTextArea textArea = new JTextArea();
    textArea.setFont(new Font("JetBrains Mono", Font.PLAIN, 13));
    textArea.setBackground(DEEP_BLUE);
    textArea.setForeground(TEXT_PRIMARY);
    textArea.setCaretColor(TEAL_ACCENT);
    textArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    textArea.setLineWrap(true);
    textArea.setWrapStyleWord(true);
    return textArea;
  }

  private JScrollPane createStyledScrollPane(JComponent component) {
    JScrollPane scrollPane = new JScrollPane(component);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setBorder(BorderFactory.createEmptyBorder());
    scrollPane.setOpaque(false);
    scrollPane.getViewport().setOpaque(false);
    scrollPane.getViewport().setBackground(DEEP_BLUE);
    return scrollPane;
  }

  private JButton createGlowButton(String text, Color baseColor, Dimension size) {
    JButton button = new JButton(text) {
      @Override
      protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color currentColor = baseColor;
        if (getModel().isPressed()) {
          currentColor = baseColor.darker();
        } else if (getModel().isRollover()) {
          currentColor = baseColor.brighter();
        }

        // Glow effect
        for (int i = 10; i > 0; i--) {
          g2d.setColor(new Color(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), i * 8));
          g2d.fillRoundRect(-i, -i, getWidth() + i * 2, getHeight() + i * 2, 15 + i, 15 + i);
        }

        // Button background
        g2d.setColor(currentColor);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

        g2d.dispose();
        super.paintComponent(g);
      }
    };

    button.setFont(new Font("Segoe UI", Font.BOLD, 13));
    button.setForeground(Color.WHITE);
    button.setContentAreaFilled(false);
    button.setBorderPainted(false);
    button.setFocusPainted(false);
    button.setPreferredSize(size);
    button.setCursor(new Cursor(Cursor.HAND_CURSOR));

    return button;
  }

  private void saveInputToFile() {
    try {
      String inputText = inputArea.getText();
      try (BufferedWriter writer = new BufferedWriter(new FileWriter("input.txt"))) {
        writer.write(inputText);
      }
    } catch (IOException e) {
      JOptionPane.showMessageDialog(this, "Error saving input: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void runConfigScript() {
    statusLabel.setText("⚡ Processing...");
    saveInputToFile();

    SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
      @Override
      protected String doInBackground() throws Exception {
        ProcessBuilder pb = new ProcessBuilder("python3", "obscura.py");
        pb.redirectErrorStream(true);
        Process process = pb.start();

        int exitCode = process.waitFor();
        if (exitCode != 0) {
          throw new RuntimeException("Script failed with exit code: " + exitCode);
        }
        return "Success";
      }

      @Override
      protected void done() {
        try {
          get();
          updateMarkdownRender();
          statusLabel.setText("✓ Processing complete!");
          Timer timer = new Timer(200, e -> loadOutputFile());
          timer.setRepeats(false);
          timer.start();
        } catch (Exception e) {
          statusLabel.setText("✗ Processing failed: " + e.getMessage());
        }
      }
    };
    worker.execute();
  }

  private void openConfigFile() {
    File configFile = new File("config.md");
    if (!configFile.exists()) {
      return;
    }

    try {
      StringBuilder content = new StringBuilder();
      try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
        String line;
        while ((line = reader.readLine()) != null) {
          content.append(line).append("\n");
        }
      }

      rawConfigText = content.toString();
      configEditArea.setText(rawConfigText);
      updateMarkdownRender();
      statusLabel.setText("✓ Config loaded");
    } catch (IOException e) {
      statusLabel.setText("Failed to load config");
    }
  }

  private String convertMarkdownToHtml(String markdown) {
    String[] lines = markdown.split("\n", -1);
    StringBuilder html = new StringBuilder();

    for (String line : lines) {
      String processedLine = line;

      if (processedLine.matches("^# .*$")) {
        processedLine = processedLine.replaceAll("^# (.*)$",
            "<h1 style='color:#14b8a6; font-size:24px; margin-bottom:16px;'>$1</h1>");
      } else if (processedLine.matches("^## .*$")) {
        processedLine = processedLine.replaceAll("^## (.*)$",
            "<h2 style='color:#8b5cf6; font-size:20px; margin-bottom:12px;'>$1</h2>");
      } else if (processedLine.matches("^### .*$")) {
        processedLine = processedLine.replaceAll("^### (.*)$",
            "<h3 style='color:#ec4899; font-size:18px; margin-bottom:8px;'>$1</h3>");
      } else if (processedLine.matches("^SEP:\\s*<\\s*>\\s*$")) {
        processedLine = "<span style='color:#14b8a6; font-weight:bold;'>SEP: &lt; &gt;</span>";
      } else if (processedLine.matches("^SEP:\\s*<\\s*$")) {
        processedLine = "<span style='color:#14b8a6; font-weight:bold;'>SEP: &lt; </span>";
      } else if (processedLine.matches("^SEP:\\s*>\\s*>\\s*$")) {
        processedLine = "<span style='color:#14b8a6; font-weight:bold;'>SEP: &gt;&gt;</span>";
      } else if (processedLine.matches("^SEP:\\s*->\\s*$")) {
        processedLine = "<span style='color:#14b8a6; font-weight:bold;'>SEP: -&gt;</span>";
      } else if (processedLine.matches("^SEP:\\s*,\\s*$")) {
        processedLine = "<span style='color:#14b8a6; font-weight:bold;'>SEP: ,</span>";
      } else if (processedLine.matches("^SEP:\\s*\\[\\s*$")) {
        processedLine = "<span style='color:#14b8a6; font-weight:bold;'>SEP: [</span>";
      } else {
        processedLine = processedLine.replaceAll("`(.*?)`",
            "<code style='background-color:#1e293b; color:#14b8a6; padding:2px 6px; border-radius:3px; font-family:JetBrains Mono;'>$1</code>");
        processedLine = processedLine.replaceAll("\\*\\*(.*?)\\*\\*", "<strong style='color:#f1f5f9;'>$1</strong>");
        processedLine = processedLine.replaceAll("\\*(.*?)\\*", "<em style='color:#e2e8f0;'>$1</em>");
        processedLine = processedLine.replaceAll("\"([^\"]*)\"",
            "<span style='color:#8b5cf6; font-weight:bold;'>\"$1\"</span>");
        processedLine = processedLine.replaceAll("->",
            "<span style='color:#ec4899; font-weight:bold; font-size:16px;'>→</span>");
        processedLine = processedLine.replaceAll(";;(.*)",
            "<span style='color:#64748b; font-style:italic;'>;;$1</span>");
      }

      html.append(processedLine).append("<br>");
    }

    return html.toString();
  }

  private void updateMarkdownRender() {
    String text = configEditArea.getText();
    String htmlContent = convertMarkdownToHtml(text);
    configDisplay.setText("<html><body style='color:#f1f5f9; font-family:Segoe UI; font-size:14px; line-height:1.6;'>"
        + htmlContent + "</body></html>");
    configDisplay.setCaretPosition(0);
  }

  private void saveConfigFile() {
    try {
      String configText = configEditArea.getText();
      try (BufferedWriter writer = new BufferedWriter(new FileWriter("config.md"))) {
        writer.write(configText);
      }
      rawConfigText = configText;
      updateMarkdownRender();
      statusLabel.setText("✓ Configuration saved!");
    } catch (Exception e) {
      statusLabel.setText("✗ Failed to save configuration");
    }
  }

  private void cleanupFiles() {
    try {
      try (BufferedWriter writer = new BufferedWriter(new FileWriter("input.txt"))) {
        writer.write("");
      }
      try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {
        writer.write("");
      }
    } catch (IOException e) {
      System.err.println("Error cleaning up files: " + e.getMessage());
    }
  }

  private void loadOutputFile() {
    File outputFile = new File("output.txt");
    if (outputFile.exists()) {
      try {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(outputFile))) {
          String line;
          while ((line = reader.readLine()) != null) {
            content.append(line).append("\n");
          }
        }
        String contentStr = content.toString();
        if (contentStr.trim().isEmpty()) {
          processedOutputArea.setText("// Output is empty\n// Add input and click Run...");
        } else {
          processedOutputArea.setText(contentStr);
        }
      } catch (IOException e) {
        processedOutputArea.setText("Error loading output: " + e.getMessage());
      }
    }
  }

  private void loadInputOutputFiles() {
    File inputFile = new File("input.txt");
    if (inputFile.exists()) {
      try {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
          String line;
          while ((line = reader.readLine()) != null) {
            content.append(line).append("\n");
          }
        }
        inputArea.setText(content.toString());
      } catch (IOException e) {
        inputArea.setText("// Add your text here...");
      }
    } else {
      inputArea.setText("// Add your text here...");
    }

    loadOutputFile();
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (Exception e) {
        e.printStackTrace();
      }

      GUI gui = new GUI();
      gui.setVisible(true);
    });
  }
}
