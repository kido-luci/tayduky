package PropsManager;

import FrameTools.jFrameTools;
import LoadingGUI.LoadingUIJFrame;
import connectionPackage.DBConnection;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class PropsManagerGUI extends JFrame{
    private JPanel mainPanel;
    private JTable propsTable;
    private JButton addNewButton;
    private JButton deletePropButton;
    private JTextArea descriptionTextArea;
    private JTextField nameTextField;
    private JTextField amountTextField;
    private JLabel ImageLabel;
    private JButton refreshButton;
    private JButton refreshTableButton;
    private JTextField searchByNameTextField;
    private JButton browserButton;
    private JComboBox statusComboBox;
    private JScrollPane tableScrollPane;
    private JScrollPane textAreaScrollPane;
    private JLabel statusLabel;
    private JButton saveChangeButton;
    private JButton removeImageButton;
    private JTextField usedTextField;
    private JButton statisticsButton;
    private PropsList propsList;
    private String tmpImage;
    private String tmpSearchText;
    private String tmpName;
    private final JFrame thisFrame = this;

    public PropsManagerGUI(JButton jButton, JFrame jFrame){
        super("Props Management");
        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.pack();
        jButton.setEnabled(false);
        jFrame.setEnabled(false);
        tmpImage = "No image";
        tmpName = "";
        checkSearchInRealTime();
        setUpGUI();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                jButton.setEnabled(true);
                jFrame.setEnabled(true);
            }
        });
        propsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setUpOnClickTable();
            }
        });
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshAllSettingField();
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                jButton.setEnabled(true);
            }
        });
        addNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNewProp();
            }
        });
        browserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectImage();
            }
        });
        refreshTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshSearchText();
                takePropsListInDB();
                printPropsTable(propsList);
            }
        });
        deletePropButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedProp();
            }
        });
        tableScrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI(){
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(28, 78, 99);
            }
        });
        tableScrollPane.getHorizontalScrollBar().setUI(new BasicScrollBarUI(){
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(28, 78, 99);
            }
        });

        removeImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageLabel.setText("No image");
                ImageLabel.setIcon(null);
                tmpImage = "No image";
            }
        });
        saveChangeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProp();
            }
        });
        statisticsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onClickStatisticsButton();
            }
        });
    }

    public void setUpGUI(){
        this.setSize(950, 670);
        jFrameTools.setDefaultLocation(this);
        this.setIconImage(jFrameTools.takeImage("icon.png"));

        takePropsListInDB();
        printPropsTable(propsList);

        propsTable.setDefaultEditor(Object.class, null);
        //border
        searchByNameTextField.setBorder(null);
        nameTextField.setBorder(null);
        amountTextField.setBorder(null);
        tableScrollPane.setBorder(null);
        propsTable.setBorder(null);
        descriptionTextArea.setBorder(null);
        textAreaScrollPane.setBorder(null);
        usedTextField.setBorder(null);
        //comboBox
        statusComboBox.setSelectedIndex(3);
        //scroll
        tableScrollPane.getVerticalScrollBar().setBackground(new Color(1, 39, 68));
        tableScrollPane.getHorizontalScrollBar().setBackground(new Color(1, 39, 68));
        //textField
        usedTextField.setEditable(false);
    }

    public void takePropsListInDB(){
        propsList = new PropsList();
        propsList.setUpPropsList();
    }

    public void printPropsTable(PropsList tmpPropsList){
        //disable button on update
        deletePropButton.setEnabled(false);
        saveChangeButton.setEnabled(false);

        DefaultTableModel defaultTableModel = new DefaultTableModel();
        defaultTableModel.addColumn("ID");
        defaultTableModel.addColumn("Name");
        defaultTableModel.addColumn("Amount");
        defaultTableModel.addColumn("Used");
        defaultTableModel.addColumn("Status");
        defaultTableModel.addColumn("Image");

        if(tmpPropsList.size() > 0){
            for (Prop prop: tmpPropsList) {
                defaultTableModel.addRow(prop.toArray());
            }
        }

        propsTable.setModel(defaultTableModel);

        DefaultTableCellRenderer defaultTableCellRenderer = new DefaultTableCellRenderer();
        defaultTableCellRenderer.setHorizontalAlignment(JLabel.CENTER);

        TableColumnModel propsTableColumnModel = propsTable.getColumnModel();
        propsTableColumnModel.getColumn(0).setMaxWidth(50);
        propsTableColumnModel.getColumn(1).setMinWidth(80);
        propsTableColumnModel.getColumn(2).setMaxWidth(60);
        propsTableColumnModel.getColumn(3).setMaxWidth(60);
        propsTableColumnModel.getColumn(4).setMinWidth(70);
        propsTableColumnModel.getColumn(5).setMinWidth(80);

        propsTableColumnModel.getColumn(0).setCellRenderer(defaultTableCellRenderer);
        propsTableColumnModel.getColumn(1).setCellRenderer(defaultTableCellRenderer);
        propsTableColumnModel.getColumn(2).setCellRenderer(defaultTableCellRenderer);
        propsTableColumnModel.getColumn(3).setCellRenderer(defaultTableCellRenderer);
        propsTableColumnModel.getColumn(4).setCellRenderer(defaultTableCellRenderer);
        propsTableColumnModel.getColumn(5).setCellRenderer(defaultTableCellRenderer);

        /*JTableHeader propsTableHeader = propsTable.getTableHeader();
        propsTableHeader.setBackground(new Color(11, 36, 58));
        propsTableHeader.setForeground(new Color(212, 201, 169));*/
    }

    public void setUpOnClickTable(){
        int index = propsTable.getSelectedRow();
        Prop prop = null;

        if(index >= 0){
            prop = searchPropByID(propsTable.getValueAt(index,0).toString());
            saveChangeButton.setEnabled(true);
        }else {
            saveChangeButton.setEnabled(false);
        }

        if(prop != null){
            deletePropButton.setEnabled(true);
            tmpName = prop.getName();
            if(prop.getImage().equals("No image")){
                ImageLabel.setText("No image");
                ImageLabel.setIcon(null);
            }else {
                ImageLabel.setText("");
                ImageLabel.setIcon(new ImageIcon(jFrameTools.takeImage(prop.getImage()).getScaledInstance(113, 113, Image.SCALE_SMOOTH)));
            }
            nameTextField.setText(prop.getName());
            amountTextField.setText(prop.getAmount());
            usedTextField.setText(prop.getUsed());
            descriptionTextArea.setText(prop.getDescription());
            tmpImage = prop.getImage();

            int statusComboBoxIndex = 0;
            switch (prop.getStatus()){
                case "Very Good":
                    statusComboBoxIndex = 0;
                    break;
                case "Good":
                    statusComboBoxIndex = 1;
                    break;
                case "Quiet Good":
                    statusComboBoxIndex = 2;
                    break;
                case "Normal":
                    statusComboBoxIndex = 3;
                    break;
                case "Not Bad":
                    statusComboBoxIndex = 4;
                    break;
                case "Bad":
                    statusComboBoxIndex = 5;
                    break;
            }
            statusComboBox.setSelectedIndex(statusComboBoxIndex);
        }
    }

    public Prop searchPropByID(String ID){
        for(Prop prop : propsList){
            if(prop.getID().equals(ID)){
                return prop;
            }
        }
        return null;
    }

    public void refreshAllSettingField(){
        ImageLabel.setText("No image");
        ImageLabel.setIcon(null);
        nameTextField.setText("Không có tên");
        amountTextField.setText("0");
        usedTextField.setText("0");
        descriptionTextArea.setText("Không có mô tả");
        tmpImage = "No image";
        statusComboBox.setSelectedIndex(3);
        searchByNameTextField.setText("");
    }

    public void createNewProp(){
        String Error = "";

        String Image = tmpImage;

        String Name = nameTextField.getText().trim();
        if(Name.length() == 0){
            Error += "Name can't be blank\n";
        }else {
            for(Prop prop : propsList){
                if(prop.getName().toLowerCase().trim().equals(Name.toLowerCase())){
                    Error += "The props name already exists\n";
                    break;
                }
            }
        }

        String Amount = amountTextField.getText().trim();
        if(!Amount.matches("[0-9]+")) {
            Error += "Amount must be a integer >= 0\n";
        }

        String Description = descriptionTextArea.getText().trim();
        if(Description.length() == 0){
            Description = "Không có mô tả";
        }

        String Status = statusComboBox.getSelectedItem().toString();

        if(Error.length() != 0){
            jFrameTools.showOptionPane(Error, "Error", mainPanel);

        }else {
            Connection connection = DBConnection.getSQLConnection(jFrameTools.sql_url);

            String sql = "INSERT INTO tbl_Props VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, "00" + (propsList.getLastID() + 1));
                preparedStatement.setString(2, Name);
                preparedStatement.setString(3, Description);
                preparedStatement.setString(4, Image);
                preparedStatement.setString(5, Amount);
                preparedStatement.setString(6, "0");
                preparedStatement.setString(7, Status);

                preparedStatement.execute();
            }catch (SQLException e){
                e.printStackTrace();
            }finally {
                jFrameTools.closeConnection(connection);
                refreshSearchText();
                takePropsListInDB();
                printPropsTable(propsList);
                jFrameTools.showOptionPane("Added!", "NotificationPackage", mainPanel);
            }
        }
    }

    public void selectImage(){
        File file = jFrameTools.choseFile(this);
        if(file != null){
            String copyFilePath = System.getProperty("user.dir") + "\\src\\image\\" + file.getName();
            System.out.println(copyFilePath);
            File fileCopy = new File(copyFilePath);

            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            setVisible(false);
                            LoadingUIJFrame loadingUIJFrame = new LoadingUIJFrame();
                            loadingUIJFrame.setVisible(true);
                            tmpImage = file.getName();

                            try{
                                if(!fileCopy.exists()){
                                    jFrameTools.copyFile(file, fileCopy.getAbsolutePath());
                                }
                                Thread.sleep(3000);
                            }catch (Exception e){
                                e.printStackTrace();
                            }finally {
                                ImageIcon avatarImage = new ImageIcon(jFrameTools.takeImage(file.getName()).getScaledInstance(113, 113, Image.SCALE_SMOOTH));
                                ImageLabel.setText("");
                                tmpImage = file.getName();
                                ImageLabel.setIcon(avatarImage);
                                loadingUIJFrame.setVisible(false);
                                setVisible(true);
                                loadingUIJFrame.dispose();
                            }
                        }
                    }).start();
                }
            });
        }
    }

    public void deleteSelectedProp(){
        int index = propsTable.getSelectedRow();

        if(index >= 0){
            Connection connection = DBConnection.getSQLConnection(jFrameTools.sql_url);

            String sql1 = "DELETE FROM tbl_PropsUsedInChallenge WHERE PropID = ?";
            String sql2 = "DELETE FROM tbl_Props WHERE ID = ?";

            String ID = propsTable.getValueAt(index, 0).toString();

            try (
                    PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
                    PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
            ) {
                preparedStatement1.setString(1, ID);
                preparedStatement2.setString(1, ID);

                preparedStatement1.execute();
                preparedStatement2.execute();
            }catch (SQLException e){
                e.printStackTrace();
            }finally {
                jFrameTools.closeConnection(connection);
                jFrameTools.showOptionPane("Deleted successfully!", "NotificationPackage", mainPanel);
                refreshSearchText();
                takePropsListInDB();
                printPropsTable(propsList);
            }
        }
    }

    public void searchPropByName(String Name){
        PropsList tmpPropsList = new PropsList();

        for(Prop prop: propsList){
            if((prop.getName().toLowerCase().trim()).matches(".*" + Name.toLowerCase().trim() + ".*")){
                tmpPropsList.add(prop);
            }
        }

        printPropsTable(tmpPropsList);
    }

    public void updateProp(){
        String Error = "";

        String Image = tmpImage;

        String Name = nameTextField.getText().trim();

        if(Name.length() == 0){
            Error += "Name can't be blank\n";
        }else if(!Name.toLowerCase().equals(tmpName.trim().toLowerCase())){
            for(Prop prop : propsList){
                if(prop.getName().toLowerCase().trim().equals(Name.toLowerCase())){
                    Error += "The props name already exists\n";
                    break;
                }
            }
        }

        String Amount = amountTextField.getText().trim();
        if(!Amount.matches("[0-9]+")) {
            Error += "Amount must be a integer >= 0\n";
        }else if(Integer.parseInt(Amount) < Integer.parseInt(usedTextField.getText())){
            Error += "Amount >= Used\n";
        }

        String Description = descriptionTextArea.getText().trim();
        if(Description.length() == 0){
            Description = "Không có mô tả";
        }

        String Status = statusComboBox.getSelectedItem().toString();

        String ID = propsTable.getValueAt(propsTable.getSelectedRow(), 0).toString();

        if(Error.length() != 0){
            jFrameTools.showOptionPane(Error, "Error", mainPanel);

        }else {
            Connection connection = DBConnection.getSQLConnection(jFrameTools.sql_url);

            String sql = "UPDATE tbl_Props SET Name = ?, Description = ?, Image = ?, Amount = ?, Status = ? WHERE ID = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(6, ID);
                preparedStatement.setString(1, Name);
                preparedStatement.setString(2, Description);
                preparedStatement.setString(3, Image);
                preparedStatement.setString(4, Amount);
                preparedStatement.setString(5, Status);

                preparedStatement.execute();
            }catch (SQLException e){
                e.printStackTrace();
            }finally {
                jFrameTools.closeConnection(connection);
                refreshSearchText();
                takePropsListInDB();
                printPropsTable(propsList);
                jFrameTools.showOptionPane("Update prop successfully!", "NotificationPackage", mainPanel);
            }
        }
    }

    public void checkSearchInRealTime(){
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true){
                            try{
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            finally {
                                String searchText = searchByNameTextField.getText().trim().toLowerCase();
                                if(!searchText.equals(tmpSearchText)){
                                    if(searchText.length() != 0){
                                        searchPropByName(searchText);
                                    }else {
                                        printPropsTable(propsList);
                                    }
                                }
                                tmpSearchText = searchText;
                            }
                        }
                    }
                }).start();
            }
        });
    }

    public void refreshSearchText(){
        tmpSearchText = "";
        searchByNameTextField.setText("");
    }

    public void onClickStatisticsButton(){
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PropsStatisticsGUI(statisticsButton, thisFrame).setVisible(true);
            }
        });
    }
}