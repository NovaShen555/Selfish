package top.novashen.gptLDPC;

import java.util.Random;
import java.util.Arrays;

public class LDPC {
    private int[][] H; // 校验矩阵
    private int[][] G; // 生成矩阵
    private int N; // 码字长度
    private int K; // 信息位长度
    private int M; // 校验位长度

    public LDPC(int[][] H) {
        this.H = H;
        this.G = transpose(H);
        this.N = H[0].length;
        this.M = H.length;
        this.K = N - M;
    }

    // 编码
    public int[] encode(int[] message) {
        int[] codeword = new int[N];
        for (int i = 0; i < N; i++) {
            int sum = 0;
            for (int j = 0; j < K; j++) {
                sum += G[i][j] * message[j];
            }
            codeword[i] = sum % 2;
        }
        return codeword;
    }

    // 解码
    public int[] decode(int[] received) {
        int[] llr = new int[N];
        int[] decoded = new int[K];

        // 初始化对数似然比（LLR）
        for (int i = 0; i < N; i++) {
            if (received[i] == 0) {
                llr[i] = 1;
            } else {
                llr[i] = -1;
            }
        }

        // 迭代译码
        for (int iter = 0; iter < 10; iter++) {
            int[] syndromes = computeSyndromes(llr);
            boolean[] isCheckNodeUpdated = new boolean[M];

            for (int j = 0; j < M; j++) {
                for (int i = 0; i < N; i++) {
                    if (H[j][i] == 1) {
                        llr[i] = 2 * syndromes[j] - llr[i];
                    }
                }
                if (syndromes[j] == 0) {
                    isCheckNodeUpdated[j] = true;
                }
            }

            for (int i = 0; i < N; i++) {
                int sum = 0;
                for (int j = 0; j < M; j++) {
                    if (H[j][i] == 1) {
                        sum += syndromes[j];
                    }
                }
                llr[i] = sum;
            }
            // System.out.println("isCheckNodeUpdated:");
            // System.out.println(Arrays.toString(isCheckNodeUpdated));
            for (int i = 0; i < N; i++) {
                if (!isCheckNodeUpdated[i]) {
                    if (llr[i] > 0) {
                        decoded[i] = 0;
                    } else {
                        decoded[i] = 1;
                    }
                }
            }

            int[] reconstructed = encode(decoded);
            boolean isConverged = true;

// 检查是否收敛
            for (int i = 0; i < N; i++) {
                if (reconstructed[i] != received[i]) {
                    isConverged = false;
                    break;
                }
            }
            if (isConverged) {
                break; // 如果收敛，退出迭代
            }
        }

        return decoded;
    }

    public int getK() {
        return K;
    }

    // 计算校验位
    private int[] computeSyndromes(int[] llr) {
        int[] syndromes = new int[M];
        for (int j = 0; j < M; j++) {
            int sum = 0;
            for (int i = 0; i < N; i++) {
                if (H[j][i] == 1) {
                    sum += llr[i];
                }
            }
            syndromes[j] = sum % 2;
        }
        return syndromes;
    }

    // 转置矩阵
    private int[][] transpose(int[][] matrix) {
        int[][] transposed = new int[matrix[0].length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                transposed[j][i] = matrix[i][j];
            }
        }
        return transposed;
    }
}