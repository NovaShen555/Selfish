package top.novashen.gptLDPC;

public class LDPCDemo1 {
    static final int iteration_num = 10000;
    // 校验矩阵信息
    static final int M = 102;
    static final int N = 204;


    static int[][] matrix0 ;
    static {
        int[][] t1 = Constanse.m;
        int[][] t2 = C2.m2;
        int[][] t3 = new int[N][M];
        for (int i=0;i<=49;i++) {
            t3[i] = t1[i];
        }
        for (int i=0;i<=51;i++) {
            t3[i+50] = t2[i];
        }
        matrix0 = t3;
    }
    /*

*/
    public static void main(String[] args) {
        int[] code = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        int[] judge = new int[M]; // 校验比特
        int[] wrong_num = new int[N]; // 统计错误的数量

        // 比特翻转算法，最多十次迭代
        for (int i = 0; i < iteration_num; i++) {

            // 打印code的中间结果
            for (int j = 0; j < N; j++)
                System.out.print(code[j] + "  ");
            System.out.println();

            // 初始化相关向量
            for (int j = 0; j < M; j++) {
                judge[j] = 0;
            }
            for (int j = 0; j < N; j++) {
                wrong_num[j] = 0;
            }

            // 和校验矩阵做运算
            for (int j = 0; j < M; j++) {
                for (int k = 0; k < N; k++) {
                    judge[j] = judge[j] ^ (matrix0[j][k] * code[k]);
                }
            }

            // 检查校验结果
            int right_flag = 1;
            for (int j = 0; j < M; j++) {
                if (judge[j] == 1) {
                    right_flag = 0;
                }
            }

            // 校验正确，输出译码结果
            if (right_flag == 1) {
                System.out.println(">> Decoding Successfully!");
                System.out.println("Totally " + i + " iterations!");
                for (int j = 0; j < N; j++) {
                    System.out.print(code[j] + "  ");
                    if (j % 50 == 49)
                        System.out.println();
                }
                System.out.println();
                return;
            }

            // 校验错误，统计每个比特位的错误数
            for (int j = 0; j < N; j++) {
                for (int k = 0; k < M; k++) {
                    wrong_num[j] += judge[k] & matrix0[k][j];
                }
            }

            // 比特翻转
            for (int j = 0; j < N; j++) {
                if (wrong_num[j] >= 2) {
                    code[j] = code[j] == 0 ? 1 : 0;
                }
            }
        }

        System.out.println("Out of iteration!");
    }
}
