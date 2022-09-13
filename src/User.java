import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;

public abstract class User {
    private String type;//用户类型
    private String account;//用户账号
    private String password;//用户密码
    private String difficultyType;//难度类型
    //下面的变量需要设置为自己的数据库数据
    private static final String URL = "jdbc:mysql://localhost:3306/testgeneration?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";//数据库连接地址
    private static final String USER = "root";//数据库用户名
    private static final String PASSWORD = "1234";//数据库密码

    public User() {
    }

    public User(String account, String password, String type) {
        this.account = account;
        this.password = password;
        this.type = type;
        difficultyType = type;
    }

    public String getType() {
        return type;
    }

    public String getAccount() {
        return account;
    }

    public void setDifficultyType(String difficultyType) {
        this.difficultyType = difficultyType;
    }

    public String getDifficultyType() {
        return difficultyType;
    }

    public static User login(String account, String password) throws SQLException, ClassNotFoundException {
        //1.加载驱动程序
        Class.forName("com.mysql.cj.jdbc.Driver");
        String searchedType;//从数据库获取到的类型
        String sql = "SELECT identity from user where account=? and password=?";
        //数据库的运行很耗费资源，所以需要及时关闭
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            //下面使用的是PreparedStatement，用法会贴在下面
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setObject(1, account);
                preparedStatement.setObject(2, password);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        //从数据库获取类型
                        searchedType = resultSet.getString("identity");
                        //返回实例对象
                        switch (searchedType) {
                            case "小学":
                                return new Primary(account, password, searchedType);
                            case "初中":
                                return new JuniorHigh(account, password, searchedType);
                            case "高中":
                                return new High(account, password, searchedType);
                        }
                    }
                    //查询失败，返回null
                    else {
                        return null;
                    }
                }
            }
        }
        return null;
    }
    //由子类实现的抽象方法
    public abstract String[] getQuestionsArray(int number);

    //根据输入的题目数量生成题目，并将其添加到数据库
    public void generateQuestions(int number) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        try {
            //测试重复问题，addQuestionsToDatabase会返回添加失败的个数，getQuestionsArray是根据不同类型的对象生成的，也就是不同难度的题目生成方法
            int failNum = addQuestionsToDatabase(getQuestionsArray(number), timestamp);
            while (failNum != 0) {
                failNum = addQuestionsToDatabase(getQuestionsArray(failNum), timestamp);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    //返回添加失败的问题的个数
    protected int addQuestionsToDatabase(String[] questions, Timestamp time) throws ClassNotFoundException, SQLException {
        int count = 0;
        //1.加载驱动程序
        Class.forName("com.mysql.cj.jdbc.Driver");
        String sql = "INSERT INTO testgeneration.questions (question, account, created_time) VALUES (?, ?, ?);";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                //创建文件对象
                File file = new File("questionsData/" + account);
                //如果没有用户的文件就创建文件夹
                if (!file.exists()) {
                    file.mkdir();
                }
//                System.out.println(file.getPath());
                //创建文件写入对象
                try (FileWriter fileWriter = new FileWriter(file.getPath() + "/" + new SimpleDateFormat("yyyy年-MM月-dd日-HH时-mm分-ss秒").format(time) + ".txt", true)) {
                    //在多次写入时preparedStatement就能大大降低运行成本
                    for (int i = 0; i < questions.length; i++) {
                        preparedStatement.setObject(1, questions[i]);
                        preparedStatement.setObject(2, account);
                        preparedStatement.setObject(3, time);
                        try {
                            preparedStatement.executeUpdate();
                            fileWriter.write(questions[i] + "\r\n");
                        }
                        //如果重复则会报错，并将重复的题目数量+1
                        catch (SQLException e) {
                            System.out.println("'" + questions[i] + "'" + "与之前的题目重复，准备重新出题");
                            count++;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        return count;
    }
}
