package ChallengesManager;

import FrameTools.jFrameTools;
import PropsManager.Prop;
import PropsManager.PropsList;
import connectionPackage.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddNewPropToChallengeGUI extends JFrame{
    private JLabel titleLabel;
    private JScrollPane tableScrollPane;
    private JTable propsTable;
    private JButton addButton;
    private JPanel mainPanel;
    private JTextField usedTextField;
    private PropsList propsList;
    private PropsUsedInChallengeList propsUsedInChallengeList;
    private String ChallengeID;

    public AddNewPropToChallengeGUI(PropsList propsList, PropsUsedInChallengeList propsUsedInChallengeList, String ChallengeID, JFrame jFrame){
        super("Challenge ID: " + ChallengeID);
        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.pack();
        this.setSize(400, 600);
        jFrameTools.setDefaultLocation(this);
        this.propsList = propsList;
        this.propsUsedInChallengeList = propsUsedInChallengeList;
        this.ChallengeID = ChallengeID;

        jFrame.setEnabled(false);
        addButton.setEnabled(false);

        setUpGUI();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                jFrame.setEnabled(true);
            }
        });
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onClickAdd(jFrame);
            }
        });
        propsTable.addComponentListener(new ComponentAdapter() {
        });
        propsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onClickTable();
            }
        });
    }

    public void setUpGUI(){
        this.setIconImage(jFrameTools.takeImage("icon.png"));
        usedTextField.setBorder(null);

        DefaultTableModel defaultTableModel = new DefaultTableModel();
        defaultTableModel.addColumn("ID");
        defaultTableModel.addColumn("Name");
        defaultTableModel.addColumn("Bunker");

        for(Prop prop : propsList){
            boolean isExist = false;
            for(PropUsed propUsed : propsUsedInChallengeList){
                if(prop.getID().equals(propUsed.getID())){
                    isExist = true;
                    break;
                }
            }
            if(!isExist){
                defaultTableModel.addRow(new Object[]{prop.getID(),
                        prop.getName(),
                        (Integer.parseInt(prop.getAmount()) - Integer.parseInt(prop.getUsed()))
                });
            }
        }

        propsTable.setModel(defaultTableModel);
    }

    public void onClickTable(){
        int index = propsTable.getSelectedRow();
        if(index >= 0){
            addButton.setEnabled(true);
            usedTextField.setText(propsTable.getValueAt(index, 2).toString());
        }
    }

    public void onClickAdd(JFrame jFrame){
        int index = propsTable.getSelectedRow();
        if(index >= 0){
            if(usedTextField.getText().trim().matches("[0-9]+")){
                int used = Integer.parseInt(usedTextField.getText());
                int bunker = Integer.parseInt(propsTable.getValueAt(index, 2).toString());
                if(used <= bunker){
                    Connection connection = DBConnection.getSQLConnection(jFrameTools.sql_url);

                    Prop tmp = null;
                    for(Prop prop : propsList){
                        if(prop.getID().equals(propsTable.getValueAt(index, 0).toString())){
                            tmp = prop;
                            break;
                        }
                    }

                    String sql = "INSERT INTO tbl_PropsUsedInChallenge VALUES (?, ?, ?, ?)";
                    String sql2 = "UPDATE tbl_Props SET Used = ? WHERE ID = ?";
                    try {
                        PreparedStatement preparedStatement = connection.prepareStatement(sql);
                        preparedStatement.setString(1, propsTable.getValueAt(index, 0).toString());
                        preparedStatement.setString(2, propsTable.getValueAt(index, 1).toString());
                        preparedStatement.setString(3, new String("" + used));
                        preparedStatement.setString(4, ChallengeID);

                        PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
                        preparedStatement2.setString(1, new String("" + (Integer.parseInt(tmp.getUsed()) + used)));
                        preparedStatement2.setString(2, propsTable.getValueAt(index, 0).toString());

                        preparedStatement.execute();
                        preparedStatement2.execute();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }finally {
                        jFrameTools.showOptionPane("Added!", "NotificationPackage", mainPanel);
                        jFrame.setEnabled(true);
                        this.dispose();;
                    }
                }else {
                    jFrameTools.showOptionPane("Invalid value used!\nUsed must be a number\n0 <= used <= bunker", "Error", mainPanel);
                }
            }else {
                jFrameTools.showOptionPane("Invalid value used!\nUsed must be a number\n0 <= used <= bunker", "Error", mainPanel);
            }
        }
    }
}
