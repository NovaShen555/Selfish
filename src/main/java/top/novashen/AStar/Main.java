package top.novashen.AStar;

import java.util.*;

public class Main {

    public static Comparator<Point> cmp = (o1, o2) -> {
        return o2.fn - o1.fn; //大的在前
    };

    public static class Point {
        public int x,y,gn,fn,parent;
        //hn可以随时计算
    }
    public static int N,M,T,SX,SY,FX,FY;
    //地图
    public static int [][]map;
    //已选中的点
    public static List<Point> closeSet=new ArrayList<>();
    //未选中的点
    public static PriorityQueue<Point> openSet= new PriorityQueue<>(cmp);
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
        for(int i=0;i<T;i++){
            int x=scanner.nextInt();
            int y=scanner.nextInt();
            map[x][y] = 1;
        }
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
        read();
        int[] curPoint = new int[2];
        while(true){
            //如果openSet不为空，则从openSet中选取优先级最高的节点
            if (!openSet.isEmpty()) {
                Point chose = openSet.poll();
                if (isFinal(chose)) {
                    //todo
                    break;
                }
                //将节点n从open_set中删除，并加入close_set中
                closeSet.add(chose);
                for (Point nearPoint : getNearPoint()){

                }
            }
        }
    }

    private static Point[] getNearPoint() {

    }

}

