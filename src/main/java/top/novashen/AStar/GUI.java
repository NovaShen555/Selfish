package top.novashen.AStar;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

class NetWork extends JFrame {
    public int n = 20;
    public int m = 10;
    // 起点终点
    public int SX = 1,SY = 1,FX = n,FY = m;
    // 地图
    public int [][]map;
    // 路径
    public List<Main.Point> pathF;
    public NetWork(int n,int m, int SX, int SY, int[][] map, List<Main.Point> pathF) {
        this.n = n;
        this.m = m;
        this.SX = SX;
        this.SY = SY;
        this.map = map;
        this.pathF = pathF;
        FX = n;
        FY = m;

        // 设置窗体大小
        this.setSize(m*20+40, n*20+60);
        // 设置窗体大小不可改变
        this.setResizable(false);
        // 设置默认关闭方式，关闭窗体的同时结束程序
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 将窗体显示出来
        this.setVisible(true);

    }

    // 起点在（20,40）
    public void paint(Graphics g) {
        int dx = 20;
        int dy = 20;
        if (n > 100 || m > 100) {
            dx = 10;
            dy = 10;
        }
        // 绘制网格
        for (int i = 0; i <= n; i++) {
            g.drawLine(20, 40 + i * dy, 20 + m * dx, 40 + i * dy);
        }
        for (int i = 0; i <= m; i++) {
            g.drawLine(20 + i * dx, 40, 20 + i * dx, 40 + n * dy);
        }
        // 填充颜色，全部填充为青色
        g.setColor(Color.cyan);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++)
                g.fillRect(20 + j * dx + 1, 40 + i * dy + 1, dx - 1, dy - 1);
        }
        // 障碍物为黑色
        g.setColor(Color.black);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++)
                if (map[i][j] == 1)
                    g.fillRect(20 + j * dx + 1, 40 + i * dy + 1, dx - 1, dy - 1);
        }
        // 路径为黄色
        g.setColor(Color.yellow);
        for (Main.Point point : pathF) {
            g.fillRect(20 + (point.y - 1) * dx + 1, 40 + (point.x - 1) * dy + 1, dx - 1, dy - 1);
        }
        // 起点为红色
        g.setColor(Color.red);
        g.fillRect(20 + (SX - 1) * dx + 1, 40 + (SY - 1) * dy + 1, dx - 1, dy - 1);
        // 终点为绿色
        g.setColor(Color.green);
        g.fillRect(20 + (FX - 1) * dx + 1, 40 + (FY - 1) * dy + 1, dx - 1, dy - 1);

    }

}

public class GUI {
    public static void main(String[] args) {

        int[][] map ={  {0,0,0,0,0},
                        {0,0,1,0,0},
                        {0,0,1,0,0},
                        {0,0,1,0,0},
                        {0,0,0,0,0} };
        List<Main.Point> pathF = new ArrayList<>();
        pathF.add(new Main.Point(1,1));
        pathF.add(new Main.Point(2,1));
        pathF.add(new Main.Point(3,1));
        pathF.add(new Main.Point(4,1));
        pathF.add(new Main.Point(4,2));
        pathF.add(new Main.Point(4,3));
        pathF.add(new Main.Point(4,4));
        new NetWork(5,5,1,2,map,pathF);

    }

}
