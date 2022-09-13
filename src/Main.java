import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    private int state = LOGIN;//代表程序状态，初始状态为登录状态
    private static final int LOGIN = 0;//定义宏LOGIN
    private static final int LOGGED_IN = 1;//定义宏LOGGED_IN
    private User user;//当前程序的使用用户
    private static Scanner scanner = new Scanner(System.in);//实例化输入对象

    private void processLogin() throws SQLException, ClassNotFoundException {
        System.out.println("请输入用户名、密码");
        String account = scanner.next();
        String password = scanner.next();
        //根据输入的账号密码获取用户实例对象
        user = User.login(account, password);
        if (user == null) {
            System.out.println("请输入正确的用户名、密码");
        } else {
            System.out.println("当前选择为" + user.getDifficultyType() + "出题");
            //将状态更新为已登录
            state = LOGGED_IN;
        }
    }

    private void processLoggedIn() {
        System.out.println("准备生成" + user.getDifficultyType() + "数学题目，请输入生成题目数量（输入-1将退出当前用户，重新登录）：");
        //接收用户输入
        String scannerContent = scanner.next();
        //监测用户输入是否为int型数据
        try {
            int questionsNumber = Integer.parseInt(scannerContent);
            //-1为退出登录，将状态重置为登录状态，并将用户对象置为null
            if (questionsNumber == -1) {
                state = LOGIN;
                user = null;
            } else if (questionsNumber >= 10 && questionsNumber <= 30) {
                System.out.println("指令正确");
                //开始生产题目，传入的参数为：用户的难度类型，题目数量
                user.generateQuestionsByType(user.getDifficultyType(), questionsNumber);
            } else {
                System.out.println("请输入正确的指令");
            }
        }
        //用户输入为非int型，监测是否为“切换为”指令
        catch (Exception NumberFormatException) {
            if (scannerContent.startsWith("切换为")) {
                String type = scannerContent.substring(3);
                //重置用户的难度类型
                if (type.equals("小学") || type.equals("初中") || type.equals("高中")) {
                    user.setDifficultyType(type);
                } else {
                    System.out.println("请输入正确的指令");
                }
            } else {
                System.out.println("请输入正确的指令");
            }
        }
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        System.out.println("中小学数学卷子自动生成程序");
        Main main = new Main();
        while (true) {
            //根据程序状态实现不同功能
            switch (main.state) {
                case LOGIN:
                    //实现登录功能
                    main.processLogin();
                    break;
                case LOGGED_IN:
                    //实现登录后功能
                    main.processLoggedIn();
                    break;
            }
        }
    }
}
