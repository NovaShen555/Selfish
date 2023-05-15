package top.novashen.AStar;

import java.util.*;

public class Main {

    public static Comparator<Point> cmp = (o1, o2) -> {
        return o2.fn - o1.fn; //大的在前
    };

    public static class Point {
        public int x,y,gn,fn;
        public Point parent;
        //hn可以随时计算

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    public static int N,M,T,SX,SY,FX,FY;
    //地图 1为障碍物 2为closeSet的点 3为openSet的点
    public static int [][]map;
    //已选中的点
    public static List<Point> closeSet=new ArrayList<>();
    //未选中的点
    public static PriorityQueue<Point> openSet= new PriorityQueue<>(cmp);
    //路径
    public static List<Point> path = new ArrayList<>();

    //读入数据
    public static void read(){
        Scanner scanner=new Scanner(System.in);
        N = scanner.nextInt();
        M = scanner.nextInt();
        T = scanner.nextInt();
        SX = scanner.nextInt();
        SY = scanner.nextInt();
        FX = scanner.nextInt();
        FY = scanner.nextInt();
        //读入障碍物
        map = new int[N+1][M+1];
        for(int i=0;i<T;i++){
            int x=scanner.nextInt();
            int y=scanner.nextInt();
            map[x][y] = 1;
        }
    }

    //openJudge的读入
    public static void readOpenJudge(){
        Scanner scanner=new Scanner(System.in);
        N = scanner.nextInt();
        M = scanner.nextInt();
        map = new int[N+1][M+1];
        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= M; j++) {
                if ("1".equals(scanner.next()))
                    map[i][j] = 1;
            }
        }
        SX = scanner.nextInt();
        SY = scanner.nextInt();
        FX = scanner.nextInt();
        FY = scanner.nextInt();
    }

    //计算曼哈顿距离
    public static int manhattan(int x,int y){
        return Math.abs(x-FX)+Math.abs(y-FY);
    }

    //判断是不是终点
    public static Boolean isFinal(Point point){
        return point.x == FX && point.y == FY;
    }

    public static void main(String []args){
        // read();
        // readOpenJudge();
        //输出map
        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= M; j++)
                System.out.print(map[i][j] + " ");
            System.out.println();
        }

        //将起点加入openSet
        openSet.add(new Point(SX,SY));

        int openJudgeAns = -1;

        int[] curPoint = new int[2];
        while(true){
            //如果openSet不为空，则从openSet中选取优先级最高的节点
            if (!openSet.isEmpty()) {
                Point chose = openSet.poll();
                if (isFinal(chose)) {
                    //如果选取的节点是终点，则输出路径

                    while (chose != null) {
                        openJudgeAns ++;
                        path.add(chose);
                        chose = chose.parent;
                    }

                    break;
                }
                //将节点n从open_set中删除，并加入close_set中
                map[chose.x][chose.y] = 2;
                closeSet.add(chose);
                for (Point nearPoint : getNearPoint(chose)){
                    nearPoint.parent = chose;
                    nearPoint.gn = chose.gn + 1;
                    nearPoint.fn = nearPoint.gn + manhattan(nearPoint.x, nearPoint.y);
                    openSet.add(nearPoint);
                    map[nearPoint.x][nearPoint.y] = 3;
                }
            }
        }
        // 打印路径
        for (int i = path.size() - 1; i >= 0; i--) {
            System.out.println(path.get(i).x + " " + path.get(i).y);
        }
        System.out.println(openJudgeAns);
    }

    private static List<Point> getNearPoint(Point curPoint) {
        int[][] d = {{0,1},{1,0},{0,-1},{-1,0}};
        List<Point> resultSet = new ArrayList<>();
        int[] curP = {curPoint.x, curPoint.y};
        for (int[] i : d) {
            int[] newP = {curP[0] + i[0], curP[1] + i[1]};
            if (newP[0] < 1 || newP[0] > N || newP[1] < 1 || newP[1] > M)
                continue;
            if (map[newP[0]][newP[1]] == 1)
                continue;
            if (isClose(newP))
                continue;
            if (isOpen(newP))
                continue;
            resultSet.add(new Point(newP[0], newP[1]));
        }
        return resultSet;
    }

    private static boolean isOpen(int[] i) {
        return map[i[0]][i[1]] == 3;
    }

    private static boolean isClose(int[] i) {
        return map[i[0]][i[1]] == 2;
    }

}

