import java.util.Random;

public class High extends User {
    //符号集
    private static final String[] symbols = {"+", "-", "*", "/"};
    private static final String[] highSymbols = {"sin", "cos", "tan"};

    public High(String account, String password, String type) {
        super(account, password, type);
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
            int juniorSymbolNum = random.nextInt(operandsNumber) + 1;
            //根号个数
            int RadicalNum = random.nextInt(juniorSymbolNum + 1);
            //平方个数
            int squareNum = juniorSymbolNum - RadicalNum;
            //高中符号个数
            int highSymbolNum = operandsNumber == 1 ? 1 : random.nextInt(operandsNumber) + 1;

            //是否有括号
            boolean isBracketIllegal = bracketStart < bracketEnd;
            int[] operands = new int[operandsNumber];
            for (int i = 0; i < operandsNumber; i++) {

                //是否加高中符号
                boolean addHighSymbol = operandsNumber == 1 || random.nextBoolean();
                //是否加根号
                boolean addRadical = !addHighSymbol && random.nextBoolean();
                //是否加平方
                boolean addSquare = !addRadical && random.nextBoolean();
                operands[i] = random.nextInt(100) + 1;
                if (isBracketIllegal && i == bracketStart) {
                    //在括号外加根号
                    boolean outsideRadical = random.nextBoolean();
                    //在括号外加高中符号
                    boolean outsideHighSymbol = !outsideRadical && random.nextBoolean();
                    if (outsideRadical && RadicalNum > 0) {
                        question.append("√");
                        RadicalNum--;
                    }
                    if (outsideHighSymbol && highSymbolNum > 0) {
                        question.append(highSymbols[random.nextInt(highSymbols.length)]);
                        highSymbolNum--;
                    }
                    question.append("(");
                }
                //普通根号
                if (addRadical && RadicalNum > 0) {
                    question.append("√");
                    RadicalNum--;
                }
                if (addHighSymbol && highSymbolNum > 0) {
                    question.append(highSymbols[random.nextInt(highSymbols.length)]);
                    highSymbolNum--;
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

    public High() {
    }
}
