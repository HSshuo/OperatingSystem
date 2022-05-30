package hshuo;

import java.util.*;

/**
 * @author SHshuo
 * @data 2022/4/30--15:13
 * 磁盘调度管理算法：
 * 1、先进先出算法
 * 2、最短路劲算法
 * 3、电梯算法
 */
public class DiskSchedulingAlgorithm {
    public static void main(String[] args) {

        int[] res = new int[]{30, 50, 100, 180,	20, 90, 150, 70, 80, 10, 160};
        int ans = 0, start = 90, len = res.length;

//        FIFO
        Deque<Integer> deque = new ArrayDeque<>();
        for(int i : res){
            deque.offer(i);
        }

        while(!deque.isEmpty()){
            int temp = deque.poll();
            ans += Math.abs(temp - start);
            start = temp;
        }
        System.out.println("FIFO : " + ans / len);


//        SSF
        ans = 0;
        start = 90;
        Map<Integer, List<Integer>> map = new HashMap<>();
//        初始化map
        for(int i : res){
            map.put(Math.abs(i - start), new ArrayList<>());
        }
        for(int i : res){
            map.get(Math.abs(i - start)).add(i);
        }
//        默认是小顶堆
        Queue<Integer> queue = new PriorityQueue<>();
        for(Map.Entry<Integer, List<Integer>> entry : map.entrySet()){
            queue.offer(entry.getKey());
        }
        while(!queue.isEmpty()){
            List<Integer> temp = map.get(queue.poll());
            for(int i : temp){
                ans += Math.abs(i - start);
                start = i;
            }
        }

        System.out.println("SSF : " + ans / len);



//        Scan
        ans = 0;
        start = 90;
        Map<Integer, List<Integer>> map1 = new HashMap<>();
//        初始化map
        for(int i : res){
            map1.put(i - start, new ArrayList<>());
        }
        for(int i : res){
            map1.get(i - start).add(i);
        }

//        左侧存储负数、所以使用大顶堆
        Queue<Integer> left = new PriorityQueue<>((o1, o2) -> (o2 - o1));
        Queue<Integer> right = new PriorityQueue<>();
        for(Map.Entry<Integer, List<Integer>> entry : map1.entrySet()){
            if(entry.getKey() > 0){
                right.offer(entry.getKey());
            }else{
                left.offer(entry.getKey());
            }
        }
        while(!left.isEmpty()){
            List<Integer> temp = map1.get(left.poll());
            for(int i : temp){
                ans += Math.abs(i - start);
                start = i;
            }
        }
        while(!right.isEmpty()){
            List<Integer> temp = map1.get(right.poll());
            for(int i : temp){
                ans += Math.abs(i - start);
                start = i;
            }
        }

        System.out.println("Scan : " + ans / len);
    }
}
