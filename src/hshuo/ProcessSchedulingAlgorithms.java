package hshuo;


/**
 * @author SHshuo
 * @data 2022/5/12--14:08
 * 进程调度算法
 */
public class ProcessSchedulingAlgorithms {
    public static void main(String[] args) {
        int[][] res = new int[][]{{0, 6}, {2, 50}, {5, 20}, {5, 10}, {12, 40}, {15, 8}};
        FCFS(res);

        res = new int[][]{{0, 6}, {2, 50}, {5, 20}, {5, 10}, {12, 40}, {15, 8}};
        SJF(res);

        res = new int[][]{{0, 6}, {2, 50}, {5, 20}, {5, 10}, {12, 40}, {15, 8}};
        HRRN(res);
    }


    /**
     * 高响应比优先
     * @param res
     */
    public static void HRRN(int[][] res){
//        根据选择排序、按照响应比重新排序
        int time = 6;
        for (int i = 1; i < 6; i++){
            int max = i;
            for(int j = i + 1; j < 6; j++){
                if(res[j][0] <= time){
                    if(getHRRN(res, max, time) < getHRRN(res, j, time)){
                        max = j;
                    }
                }else{
                    break;
                }
            }

            time += res[max][1];

            if(max != i){
                swap(res, i, max);
            }
        }

        System.out.println("*****************高响应比作业的调度顺序: A、D、C、F、B、E");
        Cal(res);
    }



    /**
     * 最短作业优先
     * @param res
     */
    public static void SJF(int[][] res){
//        根据选择排序、按照最短作业重新排序
        int time = 6;
        for (int i = 1; i < 6; i++){
            int min = i;
            for(int j = i + 1; j < 6; j++){
                if(res[j][0] <= time){
                    if(res[min][1] > res[j][1]){
                        min = j;
                    }
                }else{
                    break;
                }
            }

            time += res[min][1];

            if(min != i){
                swap(res, i, min);
            }
        }

        System.out.println("*****************短作业优先的调度顺序: A、D、F、C、E、B");
        Cal(res);
    }


    /**
     * 先来先服务
     * @param res
     */
    private static void FCFS(int[][] res) {
        System.out.println("*****************先来先服务作业的调度顺序: A、B、C、D、E、F");
        Cal(res);
    }


    /**
     * 计算响应比 1 + 等待时间 / 要求服务时间
     * @param res
     * @param i
     * @param time
     * @return
     */
    public static double getHRRN(int[][] res, int i, int time){
        return 1 + (Math.abs(time - res[i][0]) / (double) res[i][1]);
    }


    /**
     * 交换元素
     * @param res
     * @param i
     * @param j
     */
    public static void swap(int[][] res, int i, int j){
        for(int k = 0; k < 2; k++){
            int temp = res[i][k];
            res[i][k] = res[j][k];
            res[j][k] = temp;
        }
    }


    /***
     * 默认根据FCFS的顺序依次计算
     * @param res
     */
    public static void Cal(int[][] res){
//        平均周转时间 = 当前时间 + 服务时间 - 提交时间
        int[] avgTime = new int[6];
//        平均带权周转时间 = 平均周转时间 / 服务时间
        double[] avgWeightedTime = new double[6];
//        当前时间
        int time = 0;

        for(int i = 0; i < 6; i++){
            avgTime[i] = res[i][1] + time - res[i][0];
            avgWeightedTime[i] = avgTime[i] / res[i][1];
            time += res[i][1];
        }


        int ans = 0;
        for(int i : avgTime){
            ans += i;
        }
        System.out.println("平均周转时间: " + ans / 6.0);

        double weightedAns = 0.0;
        for(double i : avgWeightedTime){
            weightedAns += i;
        }
        System.out.println("平均带权周转时间: " + weightedAns / 6);
    }
}
