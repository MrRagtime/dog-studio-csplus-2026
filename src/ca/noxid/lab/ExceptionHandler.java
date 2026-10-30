package ca.noxid.lab;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.FileWriter;
import java.io.IOException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        String timestamp = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("MM-dd-yyyy`HH-mm-ss")
        );

        String filename = "crash-" + timestamp + ".log";

        Path crashDirectory = Paths.get("crash");

        try {
            Files.createDirectories(crashDirectory);
        } catch (IOException e) {
        }

        Path crashFile = crashDirectory.resolve(filename);

        throwable.printStackTrace(pw);

        String crashReport =
                "============================================\n" +
                        "DOG STUDIO CRASH REPORT\n" +
                        "====================================\n" +
                        "Date: " + LocalDateTime.now() + "\n" +
                        "Thread: " + thread.getName() + "\n" +
                        "Exception: " + throwable + "\n\n" +
                        "sowwy :( \n\n" +// I'm sorry if this happens - Dr. Ragtime
                        sw +
                        "\n============================================\n";

        System.err.println(crashReport);

        try (FileWriter writer = new FileWriter(crashFile.toFile())) {
            writer.write(crashReport);
            showCrashDialog(crashReport);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showCrashDialog(String crashReport) {

        if (GraphicsEnvironment.isHeadless()) {
            return;
        }

        SwingUtilities.invokeLater(() -> {
            JDialog dialog = new JDialog(
                    (Frame) null,
                    "Dog Studio",
                    true
            );

            dialog.setDefaultCloseOperation(
                    JDialog.DISPOSE_ON_CLOSE
            );

            dialog.setSize(650, 500);
            dialog.setLocationRelativeTo(null);
            dialog.setLayout(new BorderLayout(10, 10));

            JLabel title = new JLabel("Dog Studio Crashed!");

            title.setFont(new Font("Arial", Font.BOLD, 24));

            JLabel description = new JLabel(
                    "An unexpected exception occurred. " +
                            "The crash report is below... " +
                            "sowwy :((((" // this REALLY wasn't supposed to happen. awhhhhh - Dr. Ragtime
            );

            JPanel header = new JPanel();
            header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
            header.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));

            header.add(title);
            header.add(Box.createVerticalStrut(8));
            header.add(description);

            JTextArea reportArea = new JTextArea(crashReport);

            reportArea.setEditable(false);
            reportArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            reportArea.setCaretPosition(0);
            reportArea.setLineWrap(false);

            JScrollPane scrollPane = new JScrollPane(reportArea);

            // JButtons / these things are weird to use - Dr. Ragtime
            JButton copyButton = new JButton("Copy Report.");
            JButton closeButton = new JButton("Close.");

            copyButton.addActionListener(e -> {

                StringSelection selection =
                        new StringSelection(crashReport);

                Toolkit.getDefaultToolkit()
                        .getSystemClipboard()
                        .setContents(selection, null);

                copyButton.setText("Copied!!");
            });

            closeButton.addActionListener(e -> dialog.dispose());

            JPanel buttonPanel = new JPanel(
                    new FlowLayout(FlowLayout.RIGHT)
            );

            buttonPanel.add(copyButton);
            buttonPanel.add(closeButton);

            dialog.add(header, BorderLayout.NORTH);
            dialog.add(scrollPane, BorderLayout.CENTER);
            dialog.add(buttonPanel, BorderLayout.SOUTH);

            dialog.setVisible(true);
        });
    }
}