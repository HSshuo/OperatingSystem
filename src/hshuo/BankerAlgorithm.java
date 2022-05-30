package hshuo;

import java.util.Random;

/**
 * @author SHshuo
 * @data 2022/5/29--16:58
 * 银行家算法，避免出现死锁
 */
public class BankerAlgorithm {

    class PCB {

        /**
         * 数组x，数组的列的下标0表示A类资源；1表示B类资源，以此类推；
         * 数组y，数组的行的下标0表示进程初始时刻需要的资源；1表示进程占用的资源（Allocation）；2表示进程
         * 仍然需要的资源。
         */
        int[][] pcb;

        /**
         *进程名称
         */
        String name;

        /**
         * 使用循环链表结构完成时间片轮询、
         */
        PCB next;

        public PCB(){};
        public PCB(int[][] pcb, String name){
            this.pcb = pcb;
            this.name = name;
        }
    }


    /**
     * 系统的可用资源
     */
    int[] systemResource = new int[]{5, 3, 8, 2, 10};


    /**
     * 定义哨兵节点
     */
    PCB head = new PCB();


    /**
     * 防止死循环，无法生成安全序列
     */
    int index = 0;
    int len = 0;


    /**
     * 存储安全序列信息
     */
    StringBuffer buffer = new StringBuffer();


    /**
     * 初始化进程
     */
    public void initPCB(){
        PCB p1 = new PCB(new int[][]{{3, 3, 5, 0, 5}, {0, 0, 0, 0, 0}, {3, 3, 5, 0, 5}}, "p1");
        PCB p2 = new PCB(new int[][]{{5, 3, 8, 1, 2}, {0, 0, 0, 0, 0}, {5, 3, 8, 1, 2}}, "p2");
        PCB p3 = new PCB(new int[][]{{2, 1, 2, 0, 4}, {0, 0, 0, 0, 0}, {2, 1, 2, 0, 4}}, "p3");
        PCB p4 = new PCB(new int[][]{{4, 0, 7, 0, 5}, {0, 0, 0, 0, 0}, {4, 0, 7, 0, 5}}, "p4");
        PCB p5 = new PCB(new int[][]{{1, 2, 3, 2, 5}, {0, 0, 0, 0, 0}, {1, 2, 3, 2, 5}}, "p5");
        PCB p6 = new PCB(new int[][]{{3, 2, 6, 2, 9}, {0, 0, 0, 0, 0}, {3, 2, 6, 2, 9}}, "p6");

//        构造进程链
        head.next = p1;
        p1.next = p2;
        p2.next = p3;
        p3.next = p4;
        p4.next = p5;
        p5.next = p6;
        p6.next = head;

//        初始化进程连长度
        len = 7;
    }


    /**
     * 删除进程节点
     * @param pcb
     */
    public void deletePCB(PCB pcb){
        PCB node = head;
//        遍历一圈
        while(node.next != head){
            if(node.next == pcb){
                node.next = node.next.next;
                break;
            }
            node = node.next;
        }
    }


    /**
     * 当前进程是否被分配过资源
     * @param pcb
     * @return
     */
    public boolean isAllocationPCB(PCB pcb){
        for(int i = 0; i < pcb.pcb[0].length; i++){
            if(pcb.pcb[1][i] != 0){
                return false;
            }
        }
        return true;
    }


    /**
     * 根据进程的仍需要的资源（need）与当前系统的可用资源（available）比较
     * @param pcb
     * @return
     */
    public boolean isSafityPCB(PCB pcb){
        for(int i = 0; i < systemResource.length; i++){
            if(pcb.pcb[2][i] > systemResource[i]){
                return false;
            }
        }
        return true;
    }


    /**
     * 打印信息
     * @param pcb
     */
    public void print(PCB pcb){
        System.out.println("****************************");
        System.out.print(pcb.name + "进程占有的资源为（allocation）：");
        for(int i = 0; i < pcb.pcb[0].length; i++){
            System.out.print((char) (i + 65) + ":" + pcb.pcb[1][i] + "、  ");
        }
        System.out.println();
        System.out.print(pcb.name + "进程仍需要的资源为（need）：");
        for(int i = 0; i < pcb.pcb[0].length; i++){
            System.out.print((char) (i + 65) + ":" + pcb.pcb[2][i] + "、  ");
        }
        System.out.println();
        System.out.print("当前系统的可用资源为（available）：");
        for(int i = 0; i < systemResource.length; i++){
            System.out.print((char) (i + 65) + ":" + systemResource[i] + "、  ");
        }
        System.out.println();
        System.out.println("当前的安全序列为：" + buffer.toString());
        System.out.println();
    }


    /**
     * 进程请求资源
     * @param pcb
     */
    public void allocationPCB(PCB pcb){
//        如果是哨兵节点直接跳出
        if(pcb.pcb == null){
            return;
        }

        /**
         * 初始化进程的占有资源（allocation），通过随机数分配；
         * 生成[min, max]的随机数的函数为rand.nextInt(max - min + 1) + min;
         */
        if(isAllocationPCB(pcb)){
            for(int i = 0; i < pcb.pcb[0].length; i++){
//                保证每个进程初次都可以被分配到资源
                int min = Math.min(pcb.pcb[2][i], systemResource[i]) / 2;
//                分配占用资源[0,min]
                pcb.pcb[1][i] += new Random().nextInt(min - 0 + 1) + 0;
//                更新仍需要的资源
                pcb.pcb[2][i] -= pcb.pcb[1][i];
//                更新系统可用的资源
                systemResource[i] -= pcb.pcb[1][i];
            }
            buffer.append(pcb.name);
            print(pcb);
        }else{
            /**
             * 判断进程是否安全，如果安全加入到安全队列中;
             * 并释放当前进程的资源（系统的当前资源available + 进程占有的资源（allocation）;
             * 同时在进程链删除该进程
             */
            if(isSafityPCB(pcb)){
                for(int i = 0; i < systemResource.length; i++){
                    systemResource[i] += pcb.pcb[1][i];
                }
                deletePCB(pcb);
                buffer.append(pcb.name);
                print(pcb);

                len--;
                index = 0;
            }else{
                index++;
                System.out.println(pcb.name + "当前分配不安全，进入下一个进程");
            }
        }
    }

    public static void main(String[] args) {
        BankerAlgorithm test = new BankerAlgorithm();
        test.initPCB();

        PCB node = test.head;
        while (node.next != node){
            if(test.len == test.index + 1){
                System.out.println("无法生成安全序列");
                break;
            }
            test.allocationPCB(node.next);
            node = node.next;
        }
    }
}
