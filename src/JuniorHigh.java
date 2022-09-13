import java.util.Random;

public class JuniorHigh extends User {
    //符号集
    private static final String[] symbols = {"+", "-", "*", "/"};

    public JuniorHigh(String account, String password, String type) {
        super(account, password, type);
    }

    public JuniorHigh() {
    }

    @Override
    public String[] getQuestionsArray(int number) {
        String[] questions = new String[number];
        StringBuilder question = new StringBuilder();
        Random random = new Random(System.currentTimeMillis());
        for (int index = 0; index < number; index++) {
            //操作数数量，1到5个
            int operandsNumber = random.nextInt(5) + 1;
            //括号起始点
            int bracketStart = random.nextInt(operandsNumber);
            //括号终止点
            int bracketEnd = random.nextInt(operandsNumber);
            //初中符号个数
            int juniorSymbolNum = operandsNumber == 1 ? 1 : random.nextInt(operandsNumber) + 1;
            //根号个数
            int RadicalNum = random.nextInt(juniorSymbolNum + 1);
            //平方个数
            int squareNum = juniorSymbolNum - RadicalNum;
            //是否有括号
            boolean isBracketIllegal = bracketStart < bracketEnd;
            int[] operands = new int[operandsNumber];
            for (int i = 0; i < operandsNumber; i++) {
                //是否加根号
                boolean addRadical = operandsNumber == 1 ? RadicalNum == 1 : random.nextBoolean();
                //是否加平方
                boolean addSquare = operandsNumber == 1 ? !addRadical : !addRadical && random.nextBoolean();
                operands[i] = random.nextInt(100) + 1;
                if (isBracketIllegal && i == bracketStart) {
                    //在括号外加根号
                    boolean outsideRadical = random.nextBoolean();
                    if (outsideRadical && RadicalNum > 0) {
                        question.append("√");
                        RadicalNum--;
                    }
                    question.append("(");
                }
                //普通根号
                if (addRadical && RadicalNum > 0) {
                    question.append("√");
                    RadicalNum--;
                }
                question.append(operands[i]);
                //普通平方
                if (addSquare && squareNum > 0) {
                    question.append("²");
                    squareNum--;
                }
                if (isBracketIllegal && i == bracketEnd) {
                    boolean outsideSquare = random.nextBoolean();

                    question.append(")");
                    //括号外平方
                    if (outsideSquare && squareNum > 0) {
                        question.append("²");
                        squareNum--;
                    }
                }
                if (i < operandsNumber - 1) {
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
            question.delete(0, question.length());
        }
        return questions;
    }

}
