package top.novashen.gptLDPC;

import java.util.Arrays;

public class Decoder {

    private int[][] H; // LDPC矩阵，包含校验矩阵的信息
    private int[][] HT; // H的转置矩阵
    private int maxIterations; // 最大迭代次数
    private int numParityBits; // 校验位的数量
    private int numMessageBits; // 信息位的数量

    /**
     * 构造函数，传入LDPC矩阵和最大迭代次数
     *
     * @param H             LDPC矩阵
     * @param maxIterations 最大迭代次数
     */
    public Decoder(int[][] H, int maxIterations) {
        this.H = H;
        this.HT = transpose(H);
        this.maxIterations = maxIterations;
        this.numParityBits = H.length;
        this.numMessageBits = H[0].length - numParityBits;
    }

    /**
     * 解码方法，传入接收到的码字，返回解码后的信息位
     *
     * @param received 接收到的码字
     * @return 解码后的信息位
     */
    public int[] decode(int[] received) {
        int[] codeword = Arrays.copyOf(received, received.length);
        int[] message = new int[numMessageBits];
        int iteration = 0;

        while (iteration < maxIterations) {
            // Step 1: 计算校验方程的值
            int[] syndrome = calculateSyndrome(codeword);

            // Step 2: 如果校验方程的值全部为0，则认为解码成功
            if (isZero(syndrome)) {
                // 提取信息位并返回
                for (int i = 0; i < numMessageBits; i++) {
                    message[i] = codeword[numParityBits + i];
                }
                return message;
            }

            // Step 3: 使用比特翻转法更新码字
            for (int i = 0; i < numMessageBits; i++) {
                double sum = 0;
                int start = H.length * i;
                int end = start + H.length;
                for (int j = start; j < end; j++) {
                    int parityBit = j - start;
                    if (H[parityBit][i + numParityBits] == 1) {
                        sum += codeword[j];
                    }
                }
                codeword[i + numParityBits] = (sum >= 0) ? 0 : 1;
            }

            iteration++;
        }

        // 解码失败，返回null
        return null;
    }

    /**
     * 计算校验方程的值
     *
     * @param codeword 码字
     * @return 校验方程的值
     */
    private int[] calculateSyndrome(int[] codeword) {
        int[] syndrome = new int[numParityBits]; // 初始化校验结果的值数组
        for (int i = 0; i < numParityBits; i++) { // 遍历每一个校验方程
            double sum = 0;
            for (int j = 0; j < numMessageBits; j++) { // 遍历每一个信息位
                if (H[i][j + numParityBits] == 1) { // 如果该信息位在校验方程中有用
                    sum += codeword[j + numParityBits]; // 累加该信息位的值
                }
            }
            syndrome[i] = (sum >= 0) ? 0 : 1; // 根据累加和判断校验方程的值
        }
        return syndrome; // 返回校验方程的值数组
    }

    /**
     * 判断一个数组是否全部为0
     *
     * @param arr 数组
     * @return 是否全部为0
     */
    private boolean isZero(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 计算矩阵的转置矩阵
     *
     * @param matrix 矩阵
     * @return 转置矩阵
     */
    private int[][] transpose(int[][] matrix) {
        int[][] transpose = new int[matrix[0].length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                transpose[j][i] = matrix[i][j];
            }
        }
        return transpose;
    }
}
