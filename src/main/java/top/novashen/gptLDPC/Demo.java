package top.novashen.gptLDPC;

import java.util.Arrays;
import java.util.Random;

public class Demo {
    public static void main(String[] args) {
        // 定义 LDPC 校验矩阵
        int[][] H = {
                {1, 0, 1, 1, 0, 0},
                {0, 1, 1, 0, 1, 0},
                {1, 1, 0, 0, 0, 1}
        };

        // 初始化 LDPC 编码和解码器
        LDPC ldpc = new LDPC(H);

        // 生成随机的信息位
        int[] message = generateRandomBits(ldpc.getK());

        // 编码
        int[] codeword = ldpc.encode(message);
        System.out.println("信息位: " + Arrays.toString(message));
        System.out.println("编码结果:    " + Arrays.toString(codeword));

        // 模拟信道传输，引入错误
        int[] received = introduceErrors(codeword, 0.0);
        System.out.println("接收到的码字: " + Arrays.toString(received));

        // 解码
        int[] decoded = ldpc.decode(received);
        System.out.println("解码结果: " + Arrays.toString(decoded));
    }

    // 生成随机的信息位
    private static int[] generateRandomBits(int length) {
        Random random = new Random();
        int[] bits = new int[length];
        for (int i = 0; i < length; i++) {
            bits[i] = random.nextInt(2);
        }
        return bits;
    }

    // 模拟信道传输，引入错误
    private static int[] introduceErrors(int[] codeword, double errorRate) {
        Random random = new Random();
        int[] received = Arrays.copyOf(codeword, codeword.length);
        for (int i = 0; i < received.length; i++) {
            if (random.nextDouble() < errorRate) {
                received[i] = 1 - received[i]; // 引入错误
            }
        }
        return received;
    }
}