package FrameTools;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;

public class jFrameTools {
    public static String sql_url = "jdbc:sqlserver://localhost:1433;databaseName=BigAssignment_PRJ311;user=sa;password=123123qq";

    public static void showOptionPane(String textMassage, String Title, JPanel jPanel){
        UIManager.put("OptionPane.messageFont", new Font("Segoe Print", Font.BOLD, 12));
        UIManager.put("OptionPane.buttonFont", new Font("Segoe Print", Font.BOLD, 12));
        UIManager.put("OptionPane.background", new Color(1,39,68));
        UIManager.put("OptionPane.messageForeground", new Color(225,225,225));
        UIManager.put("Button.background", new Color(2, 135, 190));
        UIManager.put("Button.foreground", new Color(225, 225, 225));
        UIManager.put("Panel.background",new Color(1,39,68));
        JOptionPane.showMessageDialog(jPanel, textMassage, Title, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void closeConnection(Connection connection){
        if(connection != null){
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public static void setDefaultLocation(JFrame jFrame){
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        int width = (dimension.width / 2) - (jFrame.getWidth() / 2);
        int height = (dimension.height / 2) - (jFrame.getHeight() / 2);
        Point point = new Point(
                width,
                height
        );
        jFrame.setLocation(point);
    }

    public static void copyFile(File file, String copyFilePath){
        try {
            Files.copy(Paths.get(file.getAbsolutePath()), Paths.get(copyFilePath));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public static File choseFile(JFrame jFrame) {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter imageFiler = new FileNameExtensionFilter("<image>", "jpg", "png");
        fileChooser.setFileFilter(imageFiler);
        fileChooser.setMultiSelectionEnabled(false);
        File file = null;

        int x = fileChooser.showDialog(jFrame, "Select image");
        if(x == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
        }
        return file;
    }

    public static Image takeImage(String source){
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.getImage(System.getProperty("user.dir") + "\\src\\image\\" + source);
        return image;
    }
}
