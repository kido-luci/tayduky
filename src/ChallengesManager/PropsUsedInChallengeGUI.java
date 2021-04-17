package ChallengesManager;

import FrameTools.jFrameTools;
import PropsManager.Prop;
import PropsManager.PropsList;
import connectionPackage.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PropsUsedInChallengeGUI extends JFrame{
    private JScrollPane tableScrollPane;
    private JTable propsTable;
    private JPanel mainPanel;
    private JTextField searchByNameTextField;
    private JButton increaseButton;
    private JButton decreaseButton;
    private JLabel titleLabel;
    private JButton addNewButton;
    private JButton refreshTableButton;
    private JTextField amountTextField;
    private JLabel bunkerLabel;
    private PropsList propsList;
    private PropsUsedInChallengeList propsUsedInChallengeList;
    private String ChallengeID;
    private String tmpID;
    private int tmpBunker;
    private int tmpUsed;
    private final JFrame thisFrame = this;

    public PropsUsedInChallengeGUI(String ChallengeID, JFrame jFrame){
        super("Props Used Management");
        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.pack();
        jFrame.setEnabled(false);
        this.ChallengeID = ChallengeID;
        tmpID = "";
        tmpBunker = 0;
        tmpUsed = 0;

        setUpGUI();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                jFrame.setEnabled(true);
            }
        });
        propsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setUpOnClickTable();
            }
        });

        increaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onClickIncrease();
            }
        });
        addNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onClickAddNew();
            }
        });
        decreaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onClickDecrease();
            }
        });
        refreshTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onClickRefresh();
            }
        });
    }

    public void setUpGUI(){
        this.setIconImage(jFrameTools.takeImage("icon.png"));
        this.setSize(400, 550);
        jFrameTools.setDefaultLocation(this);
        propsList = new PropsList();
        propsList.setUpPropsList();
        propsUsedInChallengeList = new PropsUsedInChallengeList();
        propsUsedInChallengeList.setUpPropsList(ChallengeID);
        printPropsTable(propsUsedInChallengeList);
        titleLabel.setText("Props Used In Challege(ID: " + ChallengeID + ")");
        propsTable.setDefaultEditor(Object.class, null);
        //border
        amountTextField.setBorder(null);
    }

    public void printPropsTable(PropsUsedInChallengeList tmpPropsUsedInChallengeList){
        //disable button on update
        increaseButton.setEnabled(false);
        decreaseButton.setEnabled(false);

        DefaultTableModel defaultTableModel = new DefaultTableModel();
        defaultTableModel.addColumn("Prop ID");
        defaultTableModel.addColumn("Prop Name");
        defaultTableModel.addColumn("Used");

        if(tmpPropsUsedInChallengeList.size() > 0){
            for (PropUsed propUsed: tmpPropsUsedInChallengeList) {
                defaultTableModel.addRow(propUsed.toArray());
            }
        }

        propsTable.setModel(defaultTableModel);

        DefaultTableCellRenderer defaultTableCellRenderer = new DefaultTableCellRenderer();
        defaultTableCellRenderer.setHorizontalAlignment(JLabel.CENTER);

        TableColumnModel propsTableColumnModel = propsTable.getColumnModel();
        propsTableColumnModel.getColumn(0).setMaxWidth(80);
        propsTableColumnModel.getColumn(1).setMinWidth(100);
        propsTableColumnModel.getColumn(2).setMaxWidth(80);

        propsTableColumnModel.getColumn(0).setCellRenderer(defaultTableCellRenderer);
        propsTableColumnModel.getColumn(1).setCellRenderer(defaultTableCellRenderer);
        propsTableColumnModel.getColumn(2).setCellRenderer(defaultTableCellRenderer);


        JTableHeader propsTableHeader = propsTable.getTableHeader();
        propsTableHeader.setBackground(new Color(11, 36, 58));
        propsTableHeader.setForeground(new Color(212, 201, 169));
    }

    public void setUpOnClickTable(){
        int index = propsTable.getSelectedRow();
        if(index >= 0){
            tmpID = propsTable.getValueAt(index, 0).toString();
            tmpUsed = Integer.parseInt(propsTable.getValueAt(index, 2).toString());
            for(Prop prop : propsList){
                if(prop.getID().equals(tmpID)){
                    tmpBunker = Integer.parseInt(prop.getAmount()) - Integer.parseInt(prop.getUsed());
                    break;
                }
            }
            bunkerLabel.setText("Bunker: " + tmpBunker);
            //button
            increaseButton.setEnabled(true);
            decreaseButton.setEnabled(true);
        }
    }

    public void onClickIncrease(){
        if(amountTextField.getText().trim().matches("[0-9]+")){
            int amount = Integer.parseInt(amountTextField.getText().trim());
            if(amount > 0 && amount <= tmpBunker){
                String Used = "" + (tmpUsed + amount);
                String allUsed = "";
                for(Prop prop : propsList){
                    if(prop.getID().equals(tmpID)){
                        allUsed = "" + (Integer.parseInt(prop.getUsed()) + amount);
                        break;
                    }
                }
                Connection connection = DBConnection.getSQLConnection(jFrameTools.sql_url);

                String sql1 = "UPDATE tbl_PropsUsedInChallenge SET Used = ? WHERE PropID = ? AND ChallengeID = ?";
                String sql2 = "UPDATE tbl_Props SET Used = ? WHERE ID = ?";

                try (PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
                    PreparedStatement preparedStatement2 = connection.prepareStatement(sql2)
                ) {
                    preparedStatement1.setString(1, Used);
                    preparedStatement1.setString(2, tmpID);
                    preparedStatement1.setString(3, ChallengeID);

                    preparedStatement2.setString(1, allUsed);
                    preparedStatement2.setString(2, tmpID);

                    preparedStatement1.execute();
                    preparedStatement2.execute();
                }catch (SQLException e){
                    e.printStackTrace();
                }finally {
                    jFrameTools.closeConnection(connection);
                    propsUsedInChallengeList = new PropsUsedInChallengeList();
                    propsUsedInChallengeList.setUpPropsList(ChallengeID);
                    printPropsTable(propsUsedInChallengeList);
                    propsList = new PropsList();
                    propsList.setUpPropsList();
                    jFrameTools.showOptionPane("Increase successfully!", "NotificationPackage", mainPanel);
                }
            }else {
                jFrameTools.showOptionPane("Amount must be a number\n0 < amount <= bunker", "Error", mainPanel);
            }
        }else {
            jFrameTools.showOptionPane("Amount must be a number\n0 < amount <= bunker", "Error", mainPanel);
        }
    }

    public void onClickDecrease(){
        if(amountTextField.getText().trim().matches("[0-9]+")){
            int amount = Integer.parseInt(amountTextField.getText().trim());
            if(0 < amount && amount <= tmpUsed){
                String Used = "" + (tmpUsed - amount);
                String allUsed = "";
                for(Prop prop : propsList){
                    if(prop.getID().equals(tmpID)){
                        allUsed = "" + (Integer.parseInt(prop.getUsed()) - amount);
                        break;
                    }
                }
                Connection connection = DBConnection.getSQLConnection(jFrameTools.sql_url);

                String sql1 = "UPDATE tbl_PropsUsedInChallenge SET Used = ? WHERE PropID = ? AND ChallengeID = ?";
                String sql2 = "UPDATE tbl_Props SET Used = ? WHERE ID = ?";

                try (PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
                     PreparedStatement preparedStatement2 = connection.prepareStatement(sql2)
                ) {
                    preparedStatement1.setString(1, Used);
                    preparedStatement1.setString(2, tmpID);
                    preparedStatement1.setString(3, ChallengeID);

                    preparedStatement2.setString(1, allUsed);
                    preparedStatement2.setString(2, tmpID);

                    preparedStatement1.execute();
                    preparedStatement2.execute();
                }catch (SQLException e){
                    e.printStackTrace();
                }finally {
                    jFrameTools.closeConnection(connection);
                    propsUsedInChallengeList = new PropsUsedInChallengeList();
                    propsUsedInChallengeList.setUpPropsList(ChallengeID);
                    printPropsTable(propsUsedInChallengeList);
                    propsList = new PropsList();
                    propsList.setUpPropsList();
                    jFrameTools.showOptionPane("Decrease successfully!", "NotificationPackage", mainPanel);
                }
            } else {
                jFrameTools.showOptionPane("Amount must be a number\n0 < amount <= used", "Error", mainPanel);
            }
        }else {
            jFrameTools.showOptionPane("Amount must be a number\n0 < amount <= used", "Error", mainPanel);
        }
    }

    public void onClickAddNew(){
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                AddNewPropToChallengeGUI addNewPropToChallengeGUI = new AddNewPropToChallengeGUI(propsList, propsUsedInChallengeList, ChallengeID, thisFrame);
                addNewPropToChallengeGUI.setVisible(true);
            }
        });
    }

    public void onClickRefresh(){
        propsList = new PropsList();
        propsList.setUpPropsList();
        propsUsedInChallengeList = new PropsUsedInChallengeList();
        propsUsedInChallengeList.setUpPropsList(ChallengeID);
        printPropsTable(propsUsedInChallengeList);
        jFrameTools.showOptionPane("Refresh successfully", "NotificationPackage", mainPanel);
    }
}
