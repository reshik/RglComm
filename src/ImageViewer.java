import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.prefs.Preferences;

class ImageViewer extends JFrame {
  static class Surface extends JPanel {
    BufferedImage img;

    Surface(byte[] data) throws Exception {
      ByteArrayInputStream bis = new ByteArrayInputStream(data);
      img = ImageIO.read(bis);
      setPreferredSize(new Dimension(img.getWidth(null), img.getHeight(null)));
    }

    @Override
    public void paintComponent(Graphics g) {
      Graphics2D g2d = (Graphics2D) g;
      g2d.drawImage(img, 0, 0, null);
    }
  }

  ImageViewer (Preferences prefs, byte[] data) throws Exception {
    setTitle("ImageViewer");
    Surface surface = new Surface(data);
    add(surface);
    JMenuBar menuBar = new JMenuBar();
    setJMenuBar(menuBar);
    JMenu menu = new JMenu("File");
    menuBar.add(menu);
    JMenuItem save = new JMenuItem("Save as PNG");
    menu.add(save);
    save.addActionListener(ev -> {
      JFileChooser chooser = new JFileChooser();
      String fileDir = prefs.get("file.dir", null);
      if (fileDir != null) {
        chooser.setCurrentDirectory(new File(fileDir));
      }
      chooser.setDialogType(JFileChooser.SAVE_DIALOG);
      chooser.setSelectedFile(new File("screen.png"));
      chooser.setFileFilter(new FileNameExtensionFilter("png file","png"));
      if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
        File file = chooser.getSelectedFile();
        prefs.put("file.dir", chooser.getCurrentDirectory().toString());
        try {
          ImageIO.write(surface.img, "png", file);
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    });
    pack();
    setLocationRelativeTo(null);
    setVisible(true);
  }
}