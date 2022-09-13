import java.util.Random;

public class Primary extends User {
    //符号集
    private static final String[] symbols = {"+", "-", "*", "/"};

    public Primary(String account, String password, String type) {
        super(account, password, type);
    }

    public Primary() {
    }

    @Override
    public String[] getQuestionsArray(int number) {
        String[] questions = new String[number];
        //StringBuilder在操作变换的字符串的时候效率更高
        StringBuilder question = new StringBuilder();
        //以当前时间作为随机种子
        Random random = new Random(System.currentTimeMillis());
        for (int index = 0; index < number; index++) {
            //随机生成操作数数量，最少为两个
            int operandsNumber = random.nextInt(4) + 2;
            //随机生成左括号的位置
            int bracketStart = random.nextInt(operandsNumber);
            //随机生成右括号的位置
            int bracketEnd = random.nextInt(operandsNumber);
            //判断括号位置是否合法
            boolean isBracketIllegal = bracketStart < bracketEnd;
            int[] operands = new int[operandsNumber];
            for (int i = 0; i < operandsNumber; i++) {
                //操作数为1~100
                operands[i] = random.nextInt(100) + 1;
                //括号位置合法，并且当前为左括号位置
                if (isBracketIllegal && i == bracketStart) {
                    question.append("(");
                }
                question.append(operands[i]);
                //操作数合法并且当前为右括号位置
                if (isBracketIllegal && i == bracketEnd) {
                    question.append(")");
                }
                if (i < operandsNumber - 1) {
                    //随机从符号集里抽取一个符号
                    int symbolIndex = random.nextInt(symbols.length);
                    question.append(symbols[symbolIndex]);
                }
                //最后一个操作数后面为=号
                else {
                    question.append("=");
                }
            }
            questions[index] = question.toString();
            System.out.println(question);
            //重新设置问题
            question.delete(0, question.length());
        }
        return questions;
    }
}
