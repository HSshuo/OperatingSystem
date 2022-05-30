package hshuo;


import java.util.Objects;

/**
 * @author SHshuo
 * @data 2022/5/14--18:28
 * 基于优先数的时间片轮转调度算法
 * 核心思想就是维护head的下一个节点就是优先级最高的进程
 */
public class PriorityNumbersSchedulingAlgorithms {

    static class PCB{
        /**
         * 进程名；
         * 作为进程的标识
         */
        char name;

        /**
         * 指针；
         * 进程根据指针形成环形链表
         */
        PCB next;

        /**
         * 到达时间
         */
        Integer startTime;

        /**
         * 要求运行时间/服务时间
         */
        Integer RequiredRuntime;

        /**
         * 已运行时间
         */
        Integer elapsedTime = 0;

        /**
         * 优先数
         */
        Integer priorityNumber = 0;

        /**
         * 进程状态；
         * 进程只有两种状态：就绪状态为0、结束状态为1；
         * 默认为就绪状态
         */
        Integer state = 0;

        public PCB() {

        }
        public PCB(Integer priorityNumber){
            this.priorityNumber = priorityNumber;
        }

        public PCB(char name, Integer startTime, Integer RequiredRuntime){
            this.name = name;
            this.startTime = startTime;
            this.RequiredRuntime = RequiredRuntime;
        }
    }


    /**
     * 定义两个哨兵节点
     */
    static PCB head, tail;

    /**
     * 初始化哨兵节点
     */
    static {
        head = new PCB();
        tail = new PCB(101);
        head.next = tail;
    }

    /**
     * 给定的进程参数
     */
    static int[][] res = new int[][]{{65, 0, 6}, {66, 1, 4}, {67, 2, 10}, {68, 3, 5}, {69, 6, 1}};

    /**
     * 当前的时间
     */
    static int time = 0;


    /**
     * 删除进程块
     * 将进程的状态设置为结束状态，并且从就绪进程中删除
     * @param pcb
     */
    public static void deleteProcess(PCB pcb){
        pcb.state = 1;

        PCB node = preNode(pcb);
        node.next = node.next.next;
    }


    /**
     * 寻找当前节点的前一个节点
     * @param pcb
     * @return
     */
    public static PCB preNode(PCB pcb){
        PCB node = head;
        while(node.next != tail){
            if(node.next == pcb){
                break;
            }
            node = node.next;
        }
        return node;
    }


    /**
     * 插入PCB节点
     * 默认定义优先数为0，即最高，插入到头结点的位置
     * @param node
     */
    public static void insertHeadProcess(PCB node){
        node.next = head.next;
        head.next = node;
    }


    /**
     * 插入排序；
     * 维护就绪进程是按照优先数从小到大的顺序
     */
    public static void insertionSort(){
        PCB node = head.next;
        while(node != tail){
            if(node.priorityNumber >= node.next.priorityNumber){
                swap(node, node.next);
            }else{
                break;
            }
        }
    }


    /**
     * 交换两个PCB的位置
     * 交换的时候已经进行了node = node.next的操作
     * @param node
     * @param nextNode
     */
    private static void swap(PCB node, PCB nextNode) {
        PCB pre = preNode(node);
        pre.next = nextNode;
        node.next = nextNode.next;
        nextNode.next = node;
    }


    /**
     * 打印就绪队列，查看状态
     */
    public static void print(){
        PCB node = head.next;

        if(node == tail){
            System.out.println("就绪队列为空");
        }else{
            System.out.println("**********************当前就绪队列的状态*********************");
            while(node != tail){
                System.out.println("进程名：" + node.name + ", 到达时间：" + node.startTime
                        + ", 服务时间：" + node.RequiredRuntime + ", 已经运行时间："
                        + node.elapsedTime+ ", 优先数：" + node.priorityNumber);
                node = node.next;
            }
            System.out.println();
        }
    }


    public static void main(String[] args) {
        boolean flag = false;
        for(int i = 0; i < 5; i++){

//            防止数组越界，等于4的时候不会再有下一个进程等待加入
            if(i == 4){
                flag = true;
            }

//            如果进程到达
            if(res[i][1] <= time){
                PCB temp = new PCB((char)res[i][0], res[i][1], res[i][2]);
//                默认是优先级为0，最高所以插入到头结点
                insertHeadProcess(temp);
                time++;
            }

            while(head.next != tail){

//                打印
                print();

//                每次的head节点就是优先级最高的进程
                PCB node = head.next;
                node.elapsedTime++;
                node.priorityNumber = node.priorityNumber + 2 == 100 ? 100 : node.priorityNumber + 2;

                if(Objects.equals(node.RequiredRuntime, node.elapsedTime)){
//                    如果一致,说明进程运行完毕,从就绪队列中删除
                    deleteProcess(node);
                }else{
//                    每次将操作后的都将该节点插入排序
                    insertionSort();
                }

//                下一个进程到达，不在进行就绪队列的操作，应该插入新的节点
                if(!flag && res[i + 1][1] == time){
                    break;
                }
                time++;
            }
        }
    }

}
