import java.util.*;
import java.util.regex.Pattern;

public class Main {
    static String[][] matrix = {
            {"", "ID", "INT", "+", "-", "*", "/", "if", "then",	"else", "(", ")", ">", "<", "==", "!=", "=", ";", "$"},
            {"assign", "ID = expr ;", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
            {"comp", "", "", "", "", "", "", "if cond then stmt else stmt", "", "", "", "", "", "", "", "", "", "", ""},
            {"cond", "ID cond'", "INT cond'", "", "", "", "", "", "", "", "( expr ) cond'", "", "", "", "", "", "", "", ""},
            {"cond'", "", "", "", "", "", "", "", "", "", "", "", "> factor", "< factor", "== factor", "!= factor", "", "", ""},
            {"expr", "term expr''", "term expr''", "", "", "", "", "", "", "", "term expr''", "", "", "", "", "", "", "", ""},
            {"expr'", "", "", "+ term", "- term", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
            {"expr''", "", "", "expr' expr''", "expr' expr''", "", "", "", "", "", "", "ϵ", "", "", "", "", "", "ϵ", ""},
            {"factor", "ID", "INT", "", "", "", "", "", "", "", "( expr )", "", "", "", "", "", "", "", ""},
            {"prog", "stmt_list", "", "", "", "", "", "", "stmt_list", "", "", "", "", "", "", "", "", "", ""},
            {"stmt", "assign", "", "", "", "", "", "comp", "", "", "", "", "", "", "", "", "", "", ""},
            {"stmt_list", "stmt stmt_list'", "", "", "", "", "", "stmt stmt_list'", "", "", "", "", "", "", "", "", "", "", ""},
            {"stmt_list'", "ID = expr ; stmt_list'", "", "", "", "", "", "if cond then stmt else stmt stmt_list'", "", "", "", "", "", "", "", "", "", "", "ϵ"},
            {"term", "factor term''", "factor term''", "", "", "", "", "", "", "", "factor term''", "", "", "", "", "", "", "", ""},
            {"term'", "", "", "", "", "* factor", "/ factor", "", "", "", "", "", "", "", "", "", "", "", ""},
            {"term''", "", "", "ϵ", "ϵ", "term' term''", "term' term''", "", "", "", "", "ϵ", "", "", "", "", "", "ϵ", ""},
    };
    static Map<String, Integer> nonTerm = new HashMap<>() {{
        put("assign", 1);
        put("comp", 2);
        put("cond", 3);
        put("cond'", 4);
        put("expr", 5);
        put("expr'", 6);
        put("expr''", 7);
        put("factor", 8);
        put("prog", 9);
        put("stmt", 10);
        put("stmt_list", 11);
        put("stmt_list'", 12);
        put("term", 13);
        put("term'", 14);
        put("term''", 15);
    }};
    static Map<String, Integer> term = new HashMap<>() {{
        put("ID", 1);
        put("INT", 2);
        put("+", 3);
        put("-'", 4);
        put("*", 5);
        put("/", 6);
        put("if", 7);
        put("then", 8);
        put("else", 9);
        put("(", 10);
        put(")", 11);
        put(">", 12);
        put("<", 13);
        put("==", 14);
        put("!=", 15);
        put("=", 16);
        put(";", 17);
        put("$", 18);
    }};
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        Stack<String> stack = new Stack<>();

        stack.add("$");
        stack.add("stmt_list");
        input += " $";
        List<String> s = new LinkedList<>(Arrays.asList(input.split(" ")));

        while (!s.get(0).equals("$")) {
            String X = stack.peek();
            String in = s.get(0);
            Pattern pattern = Pattern.compile("-?\\d+");
            if (pattern.matcher(in).matches())
                in = "INT";
            if (!term.containsKey(in)) {
                pattern = Pattern.compile("[a-z][a-zA-Z\\d]*");
                if (pattern.matcher(in).matches())
                    in = "ID";
            }

            if (term.containsKey(X) || X.equals("$")) {
                if (X.equals(in)) {
                    stack.pop();
                    s.remove(0);
                } else {
                    System.out.println("Программа некорректна");
                    return;
                }
            } else {
                int i = nonTerm.get(stack.peek());
                int j = term.get(in);
                String rule = matrix[i][j];
                if (rule.equals("")) {
                    System.out.println("Программа некорректна");
                    return;
                }
                if (rule.equals("ϵ")) {
                    System.out.println(stack.pop() + " -> " + rule);
                    continue;
                }
                String[] ruleTokens = rule.split(" ");
                String non = stack.pop();
                for (int k = ruleTokens.length - 1; k >= 0; k--) {
                    stack.add(ruleTokens[k]);
                }
                System.out.println(non + " -> " + rule);
            }
        }
        System.out.println("Программа корректна");
    }
}
