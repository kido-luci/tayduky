package UserManager;

import ChallengesManager.ActingRole;
import ChallengesManager.Challenge;
import ChallengesManager.ChallengesList;
import FrameTools.jFrameTools;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;

public class MyActingRoleGUI extends JFrame{
    private JScrollPane tableScrollPane;
    private JTable myActingRolesTable;
    private JButton dowloadButton;
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JScrollPane textAreaScrollPane;
    private JTextArea descriptionTextArea;
    private JPanel descriptionPanel;
    private JPanel downloadPanel;
    private MyActingRolesList myActingRolesList;

    public MyActingRoleGUI(JButton jButton, JFrame jFrame, String ID){
        super("My Acting Roles");
        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.pack();

        //disable
        jButton.setEnabled(false);
        jFrame.setEnabled(false);
        dowloadButton.setEnabled(false);

        myActingRolesList = new MyActingRolesList();
        myActingRolesList.setUpList(ID);

        setUpGUI();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                jButton.setEnabled(true);
                jFrame.setEnabled(true);
            }
        });
        myActingRolesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onClickTable();
            }
        });
        dowloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onClickDownload();
            }
        });
    }

    public MyActingRoleGUI(JButton jButton, JFrame jFrame, String ID, String title){
        super(title);
        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.pack();

        titleLabel.setText(title);

        //disable
        jButton.setEnabled(false);
        jFrame.setEnabled(false);
        dowloadButton.setVisible(false);
        downloadPanel.setVisible(false);
        descriptionPanel.setVisible(false);

        myActingRolesList = new MyActingRolesList();
        myActingRolesList.setUpList(ID);

        switch (title){
            case "Filmed Challenge":
                setUpGUIFilmed();
                break;
            case "Upcoming Challenge":
                setUpUpcoming();
                break;
        }

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                jButton.setEnabled(true);
                jFrame.setEnabled(true);
            }
        });
    }

    public void setUpGUI(){
        this.setIconImage(jFrameTools.takeImage("icon.png"));
        this.setSize(500, 550);
        jFrameTools.setDefaultLocation(this);

        myActingRolesTable.setDefaultEditor(Object.class, null);
        //border
        tableScrollPane.setBorder(null);

        printMyActingRoleTable();
    }

    public void setUpGUIFilmed(){
        this.setIconImage(jFrameTools.takeImage("icon.png"));
        this.setSize(500, 350);
        jFrameTools.setDefaultLocation(this);

        myActingRolesTable.setDefaultEditor(Object.class, null);
        //border
        tableScrollPane.setBorder(null);

        printMyActingRoleInFilmedTable();
    }

    public void setUpUpcoming(){
        this.setIconImage(jFrameTools.takeImage("icon.png"));
        this.setSize(500, 350);
        jFrameTools.setDefaultLocation(this);

        myActingRolesTable.setDefaultEditor(Object.class, null);
        //border
        tableScrollPane.setBorder(null);

        printMyActingRoleUpcomingTable();
    }

    public void printMyActingRoleTable(){
        ChallengesList challengesList = new ChallengesList();
        challengesList.setUpChallengesList();

        DefaultTableModel defaultTableModel = new DefaultTableModel();
        defaultTableModel.addColumn("Acting Role");
        defaultTableModel.addColumn("Challenge ID");
        defaultTableModel.addColumn("Start Time");
        defaultTableModel.addColumn("End Time");

        for(ActingRole actingRole : myActingRolesList){
            Challenge challenge = null;
            for(Challenge tmp : challengesList){
                if(tmp.getID().equals(actingRole.getChallengeID())){
                    challenge = tmp;
                    break;
                }
            }
            defaultTableModel.addRow(new Object[]{
                    actingRole.getActingRoleName(),
                    actingRole.getChallengeID(),
                    challenge.getStartTime(),
                    challenge.getEndTime()
            });
        }

        myActingRolesTable.setModel(defaultTableModel);


        DefaultTableCellRenderer defaultTableCellRenderer = new DefaultTableCellRenderer();
        defaultTableCellRenderer.setHorizontalAlignment(JLabel.CENTER);

        TableColumnModel propsTableColumnModel = myActingRolesTable.getColumnModel();

        propsTableColumnModel.getColumn(0).setCellRenderer(defaultTableCellRenderer);
        propsTableColumnModel.getColumn(1).setCellRenderer(defaultTableCellRenderer);
        propsTableColumnModel.getColumn(2).setCellRenderer(defaultTableCellRenderer);
        propsTableColumnModel.getColumn(3).setCellRenderer(defaultTableCellRenderer);
    }

    public void printMyActingRoleInFilmedTable(){
        ChallengesList challengesList = new ChallengesList();
        challengesList.setUpChallengesList();

        DefaultTableModel defaultTableModel = new DefaultTableModel();
        defaultTableModel.addColumn("Acting Role");
        defaultTableModel.addColumn("Challenge ID");
        defaultTableModel.addColumn("Start Time");
        defaultTableModel.addColumn("End Time");

        for(ActingRole actingRole : myActingRolesList){
            Challenge challenge = null;
            for(Challenge tmp : challengesList){
                if(tmp.getID().equals(actingRole.getChallengeID()) && tmp.getEndTime().compareTo(new Date()) < 0){
                    challenge = tmp;
                    break;
                }
            }
            if(challenge != null){
                defaultTableModel.addRow(new Object[]{
                        actingRole.getActingRoleName(),
                        actingRole.getChallengeID(),
                        challenge.getStartTime(),
                        challenge.getEndTime()
                });
            }
        }

        myActingRolesTable.setModel(defaultTableModel);


        DefaultTableCellRenderer defaultTableCellRenderer = new DefaultTableCellRenderer();
        defaultTableCellRenderer.setHorizontalAlignment(JLabel.CENTER);

        TableColumnModel propsTableColumnModel = myActingRolesTable.getColumnModel();

        propsTableColumnModel.getColumn(0).setCellRenderer(defaultTableCellRenderer);
        propsTableColumnModel.getColumn(1).setCellRenderer(defaultTableCellRenderer);
        propsTableColumnModel.getColumn(2).setCellRenderer(defaultTableCellRenderer);
        propsTableColumnModel.getColumn(3).setCellRenderer(defaultTableCellRenderer);
    }

    public void onClickTable(){
        int index = myActingRolesTable.getSelectedRow();
        if(index >= 0){
            dowloadButton.setEnabled(true);

            String ActingRole = myActingRolesTable.getValueAt(index, 0).toString();
            String ChallengeID = myActingRolesTable.getValueAt(index, 1).toString();

            for(ActingRole tmp : myActingRolesList){
                if(tmp.getChallengeID().equals(ChallengeID) && tmp.getActingRoleName().equals(ActingRole)){
                    descriptionTextArea.setText(tmp.getDescription());
                    break;
                }
            }
        }
    }

    public void printMyActingRoleUpcomingTable(){
        ChallengesList challengesList = new ChallengesList();
        challengesList.setUpChallengesList();

        DefaultTableModel defaultTableModel = new DefaultTableModel();
        defaultTableModel.addColumn("Acting Role");
        defaultTableModel.addColumn("Challenge ID");
        defaultTableModel.addColumn("Start Time");
        defaultTableModel.addColumn("End Time");

        for(ActingRole actingRole : myActingRolesList){
            Challenge challenge = null;
            for(Challenge tmp : challengesList){
                if(tmp.getID().equals(actingRole.getChallengeID()) && tmp.getEndTime().compareTo(new Date()) > 0){
                    challenge = tmp;
                    break;
                }
            }
            if(challenge != null){
                defaultTableModel.addRow(new Object[]{
                        actingRole.getActingRoleName(),
                        actingRole.getChallengeID(),
                        challenge.getStartTime(),
                        challenge.getEndTime()
                });
            }
        }

        myActingRolesTable.setModel(defaultTableModel);


        DefaultTableCellRenderer defaultTableCellRenderer = new DefaultTableCellRenderer();
        defaultTableCellRenderer.setHorizontalAlignment(JLabel.CENTER);

        TableColumnModel propsTableColumnModel = myActingRolesTable.getColumnModel();

        propsTableColumnModel.getColumn(0).setCellRenderer(defaultTableCellRenderer);
        propsTableColumnModel.getColumn(1).setCellRenderer(defaultTableCellRenderer);
        propsTableColumnModel.getColumn(2).setCellRenderer(defaultTableCellRenderer);
        propsTableColumnModel.getColumn(3).setCellRenderer(defaultTableCellRenderer);
    }

    public void onClickDownload(){
        int index = myActingRolesTable.getSelectedRow();
        if(index >= 0){
            JFileChooser fileChooser = new JFileChooser();
            Component frame;
            int retval = fileChooser.showSaveDialog(this);
            File file = fileChooser.getSelectedFile();
            if(retval == fileChooser.APPROVE_OPTION){
                if(file == null){
                    return;
                }
            }
            if(!file.getName().toLowerCase().endsWith(".txt")) {
                file = new File(file.getParentFile(), file.getName() + ".txt");
            }
            try {
                new JTextArea(
                        "Acting role: " + myActingRolesTable.getValueAt(index, 0).toString()
                        + "\nChallengeID: " + myActingRolesTable.getValueAt(index, 1).toString()
                        + "\nStart time: " + myActingRolesTable.getValueAt(index, 2).toString()
                        + "\nEnd time: " + myActingRolesTable.getValueAt(index, 3).toString()
                        + "\nDescription: " + descriptionTextArea.getText()
                        ).write(new OutputStreamWriter(new FileOutputStream(file),
                        "utf-8"));
                Desktop.getDesktop().open(file);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
