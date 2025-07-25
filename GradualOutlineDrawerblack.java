import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class GradualOutlineDrawerblack extends JPanel {
    private BufferedImage originalImage;
    private BufferedImage outlineImage;
    private boolean[][] edgeMatrix;
    private int delay = 1; // Adjust delay to control drawing speed

    public GradualOutlineDrawerblack(String imagePath) {
        try {
            // Load the image from the provided path
            originalImage = ImageIO.read(new File(imagePath));
            outlineImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_ARGB);

            // Apply edge detection on the colored image
            int width = originalImage.getWidth();
            int height = originalImage.getHeight();
            edgeMatrix = new boolean[width][height];

            for (int y = 1; y < height - 1; y++) {
                for (int x = 1; x < width - 1; x++) {
                    // Get RGB values for the current pixel and surrounding pixels
                    Color color00 = new Color(originalImage.getRGB(x - 1, y - 1));
                    Color color01 = new Color(originalImage.getRGB(x - 1, y));
                    Color color02 = new Color(originalImage.getRGB(x - 1, y + 1));
                    Color color10 = new Color(originalImage.getRGB(x, y - 1));
                    Color color11 = new Color(originalImage.getRGB(x, y));
                    Color color12 = new Color(originalImage.getRGB(x, y + 1));
                    Color color20 = new Color(originalImage.getRGB(x + 1, y - 1));
                    Color color21 = new Color(originalImage.getRGB(x + 1, y));
                    Color color22 = new Color(originalImage.getRGB(x + 1, y + 1));

                    // Apply Sobel operator on each color channel (Red, Green, Blue)
                    int gxRed = (-color00.getRed() - 2 * color01.getRed() - color02.getRed() +
                            color20.getRed() + 2 * color21.getRed() + color22.getRed());
                    int gyRed = (-color00.getRed() - 2 * color10.getRed() - color20.getRed() +
                            color02.getRed() + 2 * color12.getRed() + color22.getRed());

                    int gxGreen = (-color00.getGreen() - 2 * color01.getGreen() - color02.getGreen() +
                            color20.getGreen() + 2 * color21.getGreen() + color22.getGreen());
                    int gyGreen = (-color00.getGreen() - 2 * color10.getGreen() - color20.getGreen() +
                            color02.getGreen() + 2 * color12.getGreen() + color22.getGreen());

                    int gxBlue = (-color00.getBlue() - 2 * color01.getBlue() - color02.getBlue() +
                            color20.getBlue() + 2 * color21.getBlue() + color22.getBlue());
                    int gyBlue = (-color00.getBlue() - 2 * color10.getBlue() - color20.getBlue() +
                            color02.getBlue() + 2 * color12.getBlue() + color22.getBlue());

                    // Combine gradients for all channels
                    int edgeValueRed = (int) Math.sqrt(gxRed * gxRed + gyRed * gyRed);
                    int edgeValueGreen = (int) Math.sqrt(gxGreen * gxGreen + gyGreen * gyGreen);
                    int edgeValueBlue = (int) Math.sqrt(gxBlue * gxBlue + gyBlue * gyBlue);

                    // Average edge values to get the final edge strength
                    int edgeValue = (edgeValueRed + edgeValueGreen + edgeValueBlue) / 3;

                    // Mark as edge if the value exceeds a certain threshold
                    edgeMatrix[x][y] = (edgeValue > 70); // Adjust the threshold as needed
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading image: " + e.getMessage());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (outlineImage != null) {
            int x = (getWidth() - outlineImage.getWidth()) / 2;
            int y = (getHeight() - outlineImage.getHeight()) / 2;
            g.drawImage(outlineImage, x, y, null);
        }
    }

    public void drawOutline() {
        int width = outlineImage.getWidth();
        int height = outlineImage.getHeight();

        // Gradually draw the outline
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (edgeMatrix[x][y]) {
                    outlineImage.setRGB(x, y, Color.BLACK.getRGB());
                    repaint();
                    try {
if(x%5==0)
                        Thread.sleep(delay); // Delay to simulate drawing effect
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        // Ask user for next action
        SwingUtilities.invokeLater(() -> showCompletionOptions());
    }

    private void showCompletionOptions() {
        int option = JOptionPane.showOptionDialog(
            this,
            "Outline drawing is complete. What would you like to do?",
            "Drawing Complete",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            new String[]{"Save Outline", "Choose Another Image", "Exit"},
            "Save Outline"
        );

        switch (option) {
            case 0: // Save Outline
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Save Outline");
                int result = fileChooser.showSaveDialog(this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();
                    try {
                        ImageIO.write(outlineImage, "png", fileToSave);
                        JOptionPane.showMessageDialog(this, "Outline saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(this, "Error saving outline: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                break;
            case 1: // Choose Another Image
                main(new String[]{}); // Restart the application
                break;
            case 2: // Exit
                System.exit(0);
                break;
            default:
                break;
        }
    }

    public static void main(String[] args) {
        // Allow user to select an image file
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select an Image");
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            JFrame frame = new JFrame("Gradual Outline Drawer");
            frame.setUndecorated(false); // Allow close operation
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            // Add title labels
            JLabel title = new JLabel("Outline Drawing of Your Image", JLabel.CENTER);
            JLabel subtitle = new JLabel("By mbmaster", JLabel.CENTER);
            title.setFont(new Font("Arial", Font.BOLD, 18));
            subtitle.setFont(new Font("Arial", Font.ITALIC, 14));

            // Add spacing between components
            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
            topPanel.add(title, BorderLayout.NORTH);
            topPanel.add(subtitle, BorderLayout.SOUTH);

            // Add the drawing panel
            GradualOutlineDrawerblack panel = new GradualOutlineDrawerblack(selectedFile.getAbsolutePath());

            // Layout components
            frame.add(topPanel, BorderLayout.NORTH);
            frame.add(panel, BorderLayout.CENTER);

            // Frame settings
            frame.setSize(1500, 1200); // Adjusted for typical laptop screens
            frame.setLocationRelativeTo(null); // Center the frame on the screen
            frame.setVisible(true);

            // Start drawing gradually in a separate thread
            new Thread(panel::drawOutline).start();
        } else {
            System.out.println("No file selected. Exiting...");
        }
    }
}
