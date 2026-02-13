package apps.bombcountdown;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import shared.UIInit;

public class BombCountDown {

    private static int seconds = 10;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UIInit.initChineseUI(); // Init global font / UI defaults

            JFrame frame = new JFrame("Microsoft");
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame.setSize(620, 300);
            frame.setLocationRelativeTo(null);

            // Intercept the window close (X) action
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    if(seconds>0){
                        JOptionPane.showMessageDialog(frame, "this program can not be closed");

                    }
                }
            });

            JLabel title = new JLabel("WARNING: Suspicious behavior detected", SwingConstants.CENTER);
            title.setFont(title.getFont().deriveFont(Font.BOLD, 17f));

            JLabel label = new JLabel("", SwingConstants.CENTER);
            label.setFont(label.getFont().deriveFont(Font.PLAIN, 16f));

            JButton exitBtn = new JButton("Click here to Exit");
            exitBtn.setVisible(false); // Hidden until countdown ends
            exitBtn.setFont(exitBtn.getFont().deriveFont(Font.PLAIN, 15f));
            exitBtn.addActionListener(e -> playFakeVideo(frame)); // Show GIF + play audio

            JPanel panel = new JPanel();
            panel.setBorder(BorderFactory.createEmptyBorder(60, 16, 16, 16));
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            title.setAlignmentX(Component.CENTER_ALIGNMENT);
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            exitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

            panel.add(title);
            panel.add(Box.createVerticalStrut(14));
            panel.add(label);
            panel.add(Box.createVerticalStrut(18));
            panel.add(Box.createVerticalStrut(18));
            panel.add(exitBtn);

            frame.setContentPane(panel);

            // Countdown timer (fires on the Swing EDT)
            Timer timer = new Timer(1000, e -> {
                Toolkit.getDefaultToolkit().beep();

                label.setText("Your device will explode in " + seconds + " seconds");
                seconds--;

                if (seconds < 0) {
                    ((Timer) e.getSource()).stop();

                    label.setText("Threat neutralized");
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                    JOptionPane.showMessageDialog(frame, "BOOOOOOOOOOM!!!");
                    exitBtn.setVisible(true);
                }
            });

            timer.setInitialDelay(0);
            timer.start();

            frame.setVisible(true);
        });
    }

    // Show an animated GIF and play a WAV sound
    private static void playFakeVideo(JFrame parent) {
        final Clip clip = startWav("fullBang.wav");

        ImageIcon gifIcon = loadGifIcon("yuna-matsumoto-bang.gif");
        if (gifIcon == null) {
            JOptionPane.showMessageDialog(parent, "GIF not found: yuna-matsumoto-bang.gif");
            stopClip(clip);
            return;
        }

        JLabel gifLabel = new JLabel(gifIcon);

        JDialog dialog = new JDialog(parent, "BAAAAAAAAAAAAAAAAAAAANG ! !", false); // Modal dialog
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setContentPane(gifLabel);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);

        // Stop audio when the dialog closes
        dialog.addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { stopClip(clip); }
            @Override public void windowClosed(WindowEvent e)  { stopClip(clip); }
        });

        dialog.setVisible(true);

        // Uncomment if you want to actually exit after showing the dialog
        // parent.dispose();
        // System.exit(0);
    }

    // Load a GIF from classpath first, then from a fallback disk path
    private static ImageIcon loadGifIcon(String fileName) {
        URL url = BombCountDown.class.getResource(fileName);
        if (url != null) return new ImageIcon(url);

        File f = new File("src/apps/bombcountdown/" + fileName);
        if (f.exists()) return new ImageIcon(f.getAbsolutePath());

        return null;
    }

    // Start playing a WAV file on a background thread
    private static Clip startWav(String fileName) {
        final Clip[] holder = new Clip[1];

        Thread t = new Thread(() -> {
            try {
                AudioInputStream ais = null;

                URL url = BombCountDown.class.getResource(fileName);
                if (url != null) {
                    ais = AudioSystem.getAudioInputStream(url);
                } else {
                    File f = new File("src/apps/bombcountdown/" + fileName);
                    if (f.exists()) ais = AudioSystem.getAudioInputStream(f);
                }

                if (ais == null) return;

                Clip clip = AudioSystem.getClip();
                clip.open(ais);
                holder[0] = clip;

                clip.start();
            } catch (Exception ex) {
                // ex.printStackTrace();
            }
        });

        t.setDaemon(true);
        t.start();

        return holder[0]; // May be null briefly due to threading
    }

    // Stop and release the audio clip
    private static void stopClip(Clip clip) {
        if (clip != null) {
            try {
                clip.stop();
                clip.close();
            } catch (Exception ignored) {}
        }
    }
}
