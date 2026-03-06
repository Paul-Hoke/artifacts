import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class SpriteGenerator {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java SpriteGenerator <skin_name> <output_path>");
            return;
        }

        String skinName = args[0];
        String outputPath = args[1];
        int size = 64;

        try {
            BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = image.createGraphics();

            // Set color based on skin name category
            Color bgColor = Color.GRAY;
            if (skinName.contains("forest")) bgColor = new Color(34, 139, 34); // Forest Green
            else if (skinName.contains("mountain")) bgColor = new Color(105, 105, 105); // Dim Gray
            else if (skinName.contains("desert")) bgColor = new Color(210, 180, 140); // Tan
            else if (skinName.contains("water") || skinName.contains("sea")) bgColor = new Color(30, 144, 255); // Dodger Blue

            // Background
            g2d.setColor(bgColor);
            g2d.fillRect(0, 0, size, size);

            // Simple pattern/border
            g2d.setColor(bgColor.darker());
            g2d.drawRect(0, 0, size - 1, size - 1);
            g2d.drawLine(0, 0, size, size);
            g2d.drawLine(0, size, size, 0);

            // Text
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 10));
            FontMetrics fm = g2d.getFontMetrics();
            int x = (size - fm.stringWidth(skinName)) / 2;
            int y = (size + fm.getAscent()) / 2 - 2;
            
            // Text shadow for readability
            g2d.setColor(Color.BLACK);
            g2d.drawString(skinName, x + 1, y + 1);
            g2d.setColor(Color.WHITE);
            g2d.drawString(skinName, x, y);

            g2d.dispose();

            File file = new File(outputPath);
            ImageIO.write(image, "png", file);
            System.out.println("Generated local sprite: " + file.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
