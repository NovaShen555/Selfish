package top.novashen.gptLDPC;

import java.util.Arrays;
import java.util.Random;

public class Demo2 {

    private static int[][] H = {
            {1, 1, 0, 1, 0, 0},
            {1, 0, 1, 0, 1, 0},
            {1, 1, 1, 0, 0, 1}
    };
    private static int[][] D = {
            {1, 0, 0, 1, 0, 1},
            {0, 1, 0, 1, 1, 1},
            {0, 0, 1, 0, 1, 1}
    };

    private static final int N = H[0].length;
    private static final int M = H.length;
    private static int K = N - M;

    private static int MAX_ITERATIONS = 10;
    private static double EPSILON = 1e-6;

    private static double p = 0.8;

    private static int[] encode(int[] infoBits) {
        int[] codeword = new int[N];

        for (int i = 0; i < N; i++) {
            int parity = 0;
            for (int j = 0; j < M; j++) {
                // System.out.println(i+" "+j);
                if (D[j][i] == 1 && infoBits[j] == 1) {
                    parity += 1;
                }
            }
            codeword[i] = parity % 2;
        }



        return codeword;
    }



    // 计算收到的信息与H矩阵相乘是否全为零
    private static boolean check(int[] receivedCodeword) {
        int[] result = new int[M];
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                //计算H矩阵的每一行与接收到的码字的乘积
                result[i] += receivedCodeword[j] * H[i][j];
                result[i] %= 2;
            }
        }

        // 输出检查结果
        System.out.println("Check result "+Arrays.toString(result));

        for (int i = 0; i < M; i++) {
            if (result[i] != 0) {
                return false;
            }
        }
        return true;
    }


    private static int[] decode(int[] receivedCodeword) {
        // 看看是不是本来就对
        if (check(receivedCodeword))
            return receivedCodeword;

        // M为校验矩阵H的行数，N为列数，llr表示对数似然比处理后的矩阵
        double[][] llr = new double[M][N];

        // 对于H中的每一个元素
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                // 如果该元素为1
                if (H[i][j] == 1) {
                    // 利用接收到的码字进行对数似然比的计算
                    if (receivedCodeword[j] == 1)
                        llr[i][j] = Math.log((1 - p) / p);
                    else
                        llr[i][j] = Math.log(p / (1 - p));
                }
            }
        }

        //输出llr矩阵
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                System.out.print(llr[i][j] + " ");
            }
            System.out.println();
        }

        // decodedBits为最终解码结果，beliefs为每个比特的置信度
        int[] decodedBits = new int[N];
        // 置信度，后验概率
        double[] beliefs = new double[N];
        // 概率矩阵
        double[][] E = new double[M][N];

        // 迭代更新置信度
        for (int iter = 0; iter < MAX_ITERATIONS; iter++) {
            // 计算概率矩阵E
            for (int i = 0; i < M; i++) {
                for (int j = 0; j < N; j++) {
                    double mulTan = 1.0;
                    for (int j1 = 0; j1 < N; j1++) {
                        if (j1 == j || H[i][j1] == 0)
                            continue;
                        mulTan *= Math.tanh(llr[i][j1] / 2.0);
                      //  System.out.println("--------");
                      //  System.out.println(llr[i][j1] / 2.0);
                      //  System.out.println(Math.tanh(llr[i][j1] / 2.0));
                       // System.out.println(Math.tanh(-0.6931471805599454));
                    }
                   // System.out.println("+++++++");
                  //  System.out.println(i+" "+j);
                  //  System.out.println(mulTan);
                    if (H[i][j] == 0)
                        E[i][j] = 0;
                    else if (mulTan == 1.0)
                        E[i][j] = 1.0;
                    else if (mulTan == -1.0)
                        E[i][j] = -1.0;
                    else
                        E[i][j] = Math.log( (1.0 + mulTan) / (1.0 - mulTan));
                  //  E[i][j] = Math.log( (1.0 + mulTan) / (1.0 - mulTan));
                }
            }

            // //输出概率矩阵
            // System.out.println("-----------------");
            // for (int i = 0; i < M; i++) {
            //     for (int j = 0; j < N; j++) {
            //         System.out.print(E[i][j] + " ");
            //     }
            //     System.out.println();
            // }

            // 计算后验概率
            for (int i = 0; i < M; i++)
                for (int j = 0; j < N; j++)
                    beliefs[j] += E[i][j];
            System.out.println(Arrays.toString(beliefs));
            for (int i = 0; i < N; i++) {
                if (receivedCodeword[i] == 1)
                    beliefs[i] += Math.log((1.0 - p) / p);
                else
                    beliefs[i] += Math.log(p / (1.0 - p));
            }
            System.out.println(Math.log((1.0 - p) / p));
            System.out.println("-----------------");
            // 将置信度转换为比特值
            for (int i = 0; i < N; i++) {
                // System.out.println(beliefs[i]);
                // 负数判决为 1 ，正数判决为 0
                decodedBits[i] = beliefs[i] < 0 ? 1 : 0;
            }

            // 输出解码结果
            System.out.println(Arrays.toString(decodedBits));

            // 验算结果是否正确
            if (check(decodedBits)) {
                System.out.println("successful");
                break;
            }

            // 清空llr
            llr = new double[M][N];

            // 如果不正确，更新llr
            for (int i = 0; i < M; i++) {
                for (int j = 0; j < N; j++) {
                    for (int i2 = 0;i2 < M; i2++) {
                        if (i2 == i)
                            continue;
                        llr[i][j] += E[i2][j];
                    }
                    if (decodedBits[j] == 1)
                        llr[i][j] += Math.log((1 - p) / p);
                    else
                        llr[i][j] += Math.log(p / (1 - p));
                }
            }

            // 输出更新后的llr
            System.out.println("=======================");
            for (int i = 0; i < M; i++) {
                for (int j = 0; j < N; j++) {
                    System.out.print(llr[i][j] + " ");
                }
                System.out.println();
            }

        }

        return decodedBits;
    }

        public static void main(String[] args) {

        int[] infoBits = {1, 1, 0}; // Information bits to be encoded
        int[] codeword = encode(infoBits); // LDPC encoding
        System.out.println("Codeword: " + Arrays.toString(codeword));

        // Simulate channel: Add some random errors to the codeword
        int[] receivedCodeword = new int[codeword.length];
        Random rand = new Random();
        for (int i = 0; i < codeword.length; i++) {
            double errorProb = 0.2; // Probability of error
            receivedCodeword[i] = codeword[i] ^ (rand.nextDouble() < errorProb ? 1 : 0);
        }

        System.out.println("Received Codeword with Errors: " + Arrays.toString(receivedCodeword));

        int[] decodedBits = decode(receivedCodeword); // LDPC decoding
        System.out.println("Decoded Bits: " + Arrays.toString(decodedBits));
    }
}

