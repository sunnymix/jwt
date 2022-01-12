import java.util.Arrays;
import java.util.List;

public class Jwt {

    /**
     * 参数列表：
     * args[0] command
     * args[1] secret
     * args[2] claim1=value1
     * ...
     * args[N] claimN[=valueN]
     *
     * @param args 参数列表
     */
    public static void main(String[] args) {
        List<String> argList = Arrays.asList(args);
        System.out.println(argList);
    }

}
