import java.util.Arrays;
import java.util.List;

public class Jwt {

    /**
     * 参数列表：
     * args[0] command：encode 编码，decode 解码。
     * args[1] secret：秘钥。
     * args[2] claim1=value1：数据项1=数据项1的值。
     * ...
     * args[N] claimN[=valueN]：数据项N=数据项N的值。注意，"=数据项N的值"是可选的，当不传值时会使用默认值。
     *
     * @param args 参数列表
     */
    public static void main(String[] args) {
        List<String> argList = Arrays.asList(args);
        System.out.println(argList);
    }

}
