package TradingPlatform;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.IOException;

/**
 * The main GUI class. Calls other GUI classes to construct the SPS Trading client application.
 */
public class GUIMain extends JFrame {

    private static final long serialVersionUID = 692675871418401803L;

    // Screen Ratio
    private static final float WIDTH_RATIO = 1.5f;
    private static final float HEIGHT_RATIO = 1.25f;

    // Colours
    public static final Color cust1 = new Color(38,139,133);
    public static final Color cust2 = new Color(51,61,68);
    public static final Color cust3 = new Color(72,191,146);
    public static final Color DARK_JUNGLE_GREEN = new Color(13, 27, 30);

    public static Object[] getColours(){
        return new Object[]{cust1, cust2, cust3};
    }
    // Display the window.
    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    // the screen height
    public static int height = (int)screenSize.getHeight();
    public static int tabHeight = height/2;

    // the screen width
    public static int width = (int)screenSize.getWidth();
    public static int tabWidth = width - (width/3);
    //Font
    public static String FONT = "SansSerif";
    //orgTab
    public JPanel orgTab = new JPanel();

    /**
     * Creates the GUI, and calls other GUI classes to add them to the main JTabbedPane
     * @param user The current User
     */
    public GUIMain(User user)  {
        super("SPS Trading");
        UIManager.put("TabbedPane.selected", cust1);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.background", cust1);

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainframe = new JPanel();
        mainframe.setLayout(new GridBagLayout());
        GridBagConstraints position = new GridBagConstraints();

        JTabbedPane pagePane = new JTabbedPane();

        JPanel homeTab = new JPanel();
        new GUIHome(homeTab, user);

        JPanel orgHomeTab = new JPanel();
        new GUIOrgHome(orgHomeTab, user);

        JPanel profileTab = new JPanel();
        new GUIProfile(profileTab, user);

        pagePane.add("Home", homeTab);
        pagePane.add("Organisation Home", orgHomeTab);
        pagePane.add("My Profile", profileTab);

        if (user.getAccountType() == AccountType.ADMINISTRATOR){
            JPanel adminTab = new JPanel();
            new GUIAdmin(adminTab, new ITAdministrator(user.getUserID()));
            pagePane.add("Admin", adminTab);
            adminTab.setBackground(DARK_JUNGLE_GREEN);
            adminTab.setForeground(Color.LIGHT_GRAY);
        }

        position.weighty = 0;
        position.gridy = 0;
        JLabel title = new JLabel("SPS Trading");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Verdana", Font.BOLD, 50));
        mainframe.add(title , position);

        position.gridy = 1;
        mainframe.add(pagePane, position);
        mainframe.setBackground(DARK_JUNGLE_GREEN);

        pagePane.setPreferredSize(new Dimension(width-width/10, height - height/4));
        pagePane.setMinimumSize(new Dimension(width/2, height/2));

        getContentPane().add(mainframe);
        getContentPane().setBackground(DARK_JUNGLE_GREEN);

        // Get size of device screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        float screenWidth = (float)screenSize.getWidth();
        float screenHeight = (float)screenSize.getHeight();
        int windowWidth = (int)(screenWidth / WIDTH_RATIO);
        int windowHeight = (int)(screenHeight / HEIGHT_RATIO);

        setPreferredSize(new Dimension(windowWidth, windowHeight));
        setLocation(new Point(0, 0));
        pack();
        setVisible(true);
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);

        orgTab.setAutoscrolls(true);

        pagePane.setForeground(Color.BLACK);
        pagePane.setBackground(cust3);

        homeTab.setBackground(DARK_JUNGLE_GREEN);
        homeTab.setForeground(Color.LIGHT_GRAY);

        orgHomeTab.setBackground(DARK_JUNGLE_GREEN);
        orgHomeTab.setForeground(Color.LIGHT_GRAY);

        profileTab.setBackground(DARK_JUNGLE_GREEN);
        profileTab.setForeground(Color.LIGHT_GRAY);

    }

    /**
     * Creates a JScrollPane to store a table
     * @param table The JTable wanted to be stored
     * @return tradesScrollTable
     */
    public static JScrollPane tablePane(JTable table){

        JScrollPane tradesScrollTable = new JScrollPane(table);
        tradesScrollTable.setBackground(cust3);
        tradesScrollTable.getVerticalScrollBar().setBackground(cust2);
        tradesScrollTable.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = cust1;
            }
        });
        return tradesScrollTable;
    }

    /**
     * Creates a DefaultTabelModel to store the data in
     * @param data A String[][] that holds the table data
     * @param headingType A String[] that holds the table headings
     * @return a DefaultTableModel, model
     */
    public static DefaultTableModel constructTable(String[][] data, String[] headingType){
        DefaultTableModel model = new DefaultTableModel(data, headingType);
        return model;
    }

    /**
     * Creates the table to be displayed
     * @param model A DefaultTableModel with the data to go in the table
     * @return A JTable, table
     */
    public static JTable tableCreator (DefaultTableModel model){
        JTable table = new JTable(model);
        JTableHeader anHeader = table.getTableHeader();
        anHeader.setBackground(cust1);
        return table;
    }


}
