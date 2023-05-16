package top.novashen.AStar;

import java.util.*;

public class Main {

    // 优先队列的比较函数
    public static Comparator<Point> cmp = (o1, o2) -> {
        return o2.fn - o1.fn; //大的在前
    };

    // 点的数据结构
    public static class Point {
        // gn是到这个点的路径的花费
        // fn是估计的整个路径的花费
        public int x,y,gn,fn;
        public Point parent;
        //hn是估计函数可以随时计算

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    // 施法的数据结构
    public static class Magic {
        // 迭代权值e下降至t时添加障碍物
        public int t;
        // 障碍物坐标
        public int x;
        public int y;

        public Magic(int t, int x, int y) {
            this.t = t;
            this.x = x;
            this.y = y;
        }
    }
    // 施法队列
    // 若施法时正好在目标坐标上 则施法取消
    public static List<Magic> magicList=new ArrayList<>();
    // 询问map
    public static List<Integer> queryList=new ArrayList<>();
    // NM行列 T障碍物个数(无用) S-start F-final
    public static int N,M,T,SX,SY,FX,FY;
    // E 权值
    public static int E;
    // P 施法次数
    public static int P;
    // K 询问次数
    public static int K;
    //地图 1为障碍物 2为closeSet的点 3为openSet的点
    public static int [][]map;
    //已选中的点
    public static List<Point> closeSet=new ArrayList<>();
    //未选中的点 优先队列
    public static PriorityQueue<Point> openSet= new PriorityQueue<>(cmp);
    //路径结果
    public static List<Point> path = new ArrayList<>();

    // 初始化
    public static void init() {
        closeSet.clear();
        openSet.clear();
        path.clear();
        for (int i = 0; i <= N; i++)
            for (int j = 0; j <= M; j++)
                if (map[i][j] == 2 || map[i][j] == 3)
                    map[i][j] = 0;
    }

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

    // project的读入
    public static void readPro(){
        Scanner scanner=new Scanner(System.in);
        N = scanner.nextInt();
        M = scanner.nextInt();
        E = scanner.nextInt();
        SX = 1;
        SY = 1;
        FX = N;
        FY = M;
        //读入障碍物
        map = new int[N+1][M+1];
        for(int i = 0; i < N; i++) {
            String tmp = scanner.next();
            for (int j = 0; j < M; j++)
                if ( '1' == tmp.charAt(j))
                    map[i+1][j+1] = 1;
        }
        //读入施法次数
        P = scanner.nextInt();
        for (int i = 0; i < P; i++)
            magicList.add(new Magic(scanner.nextInt(),scanner.nextInt(),scanner.nextInt()));
        //读入询问次数
        K = scanner.nextInt();
        for (int i = 0; i < K; i++)
            queryList.add(scanner.nextInt());
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


    public static void main(String []args){
        // read();
        // readOpenJudge();

        readPro();

        //输出map
        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= M; j++)
                System.out.print(map[i][j] + " ");
            System.out.println();
        }

        while (E > 0) {
            //初始化
            init();
            //施法
            updateMap();
            // A*
            aStar();

            if (queryList.contains(E))
                //输出路径
                printPath();

            // 更新起点
            SX = path.get(path.size() - 2).x;
            SY = path.get(path.size() - 2).y;
            E --;

            System.out.println("E = " + E);
            System.out.println("SX = " + SX +" SY = " + SY);
        }
    }

    // 施法
    public static void updateMap(){
        for (Magic magic : magicList) {
            if (magic.t == E && SX != magic.x && SY != magic.y)
                map[magic.x][magic.y] = 1;
        }
    }

    // A*
    public static void aStar() {
        //将起点加入openSet
        openSet.add(new Point(SX,SY));

        while(true){
            //如果openSet不为空，则从openSet中选取优先级最高的节点
            if (!openSet.isEmpty()) {
                Point chose = openSet.poll();
                if (isFinal(chose)) {
                    //如果选取的节点是终点，则输出路径

                    while (chose != null) {
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
                    nearPoint.fn = nearPoint.gn + manhattan(nearPoint.x, nearPoint.y, E);
                    openSet.add(nearPoint);
                    map[nearPoint.x][nearPoint.y] = 3;
                }
            }
        }
    }

    //计算曼哈顿距离
    public static int manhattan(int x,int y,int e){
        return (Math.abs(x-FX)+Math.abs(y-FY))*e;
    }

    //判断是不是终点
    public static Boolean isFinal(Point point){
        return point.x == FX && point.y == FY;
    }

    private static List<Point> getNearPoint(Point curPoint) {
        int[][] d = {{0,1},{1,0},{0,-1},{-1,0},{1,1},{1,-1},{-1,-1},{-1,1}};
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

    // 打印路径
    public static void printPath() {
        for (int i = path.size() - 1; i >= 0; i--) {
            System.out.print(path.get(i).x + " " + path.get(i).y + " ");
        }
        System.out.println("\n"+path.size());
    }

}

