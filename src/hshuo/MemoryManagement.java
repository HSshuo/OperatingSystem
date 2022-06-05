package hshuo;

/**
 * @author SHshuo
 * @data 2022/6/5--17:04
 * <p>
 * 内存管理
 */
public class MemoryManagement {

    /**
     * 已分区表
     */
    class AllocatedBlock {

        /**
         * 进程名称
         */
        char name;

        /**
         * 进程占用资源
         */
        int size;

        /**
         * 已分区表的起始地址
         */
        int startAddress;

        /**
         * 连接指针
         */
        AllocatedBlock next;

        /**
         * 有参、无参构造函数
         *
         * @param name
         * @param size
         * @param startAddress
         */
        public AllocatedBlock(char name, int size, int startAddress) {
            this.name = name;
            this.size = size;
            this.startAddress = startAddress;
        }

        public AllocatedBlock(int size, int startAddress) {
            this.size = size;
            this.startAddress = startAddress;
        }

        public AllocatedBlock() {

        }
    }


    /**
     * 空闲分区表
     */
    class FreeBlock {

        /**
         * 空闲分区表的大小
         */
        int size;

        /**
         * 空闲分区表的起始地址
         */
        int startAddress;

        /**
         * 连接指针
         */
        FreeBlock next;

        /**
         * 有参、无参构造函数
         *
         * @param size
         * @param startAddress
         */
        public FreeBlock(int size, int startAddress) {
            this.size = size;
            this.startAddress = startAddress;
        }

        public FreeBlock(int size) {
            this.size = size;
        }

        public FreeBlock() {

        }
    }


    /**
     * 定义哨兵节点
     */
    AllocatedBlock allocatedBlockHead = new AllocatedBlock(0, 0);
    FreeBlock freeBlockHead = new FreeBlock(1024);


    /**
     * 初始化资源分配
     * 分配已分区表的时候，按照地址的有小到大的顺序，使得每个节点都相邻；
     * 每次更新空闲分区表
     * @param res
     */
    public void allocatedSurface(int[][] res) {
        AllocatedBlock head = allocatedBlockHead;
        FreeBlock node = freeBlockHead;

        for (int i = 0; i < res.length; i++) {
//            已分区表
            AllocatedBlock pre = findAllocatedStartAddress();
            AllocatedBlock allocatedBlock = new AllocatedBlock((char) res[i][0], res[i][1], pre.startAddress + pre.size);
            head.next = allocatedBlock;
            head = head.next;

//            空闲分区表
            int systemSize = findFreeBlockedSize();
            if (systemSize == 1024) {
                FreeBlock freeBlock = new FreeBlock(systemSize - allocatedBlock.size, allocatedBlock.startAddress + allocatedBlock.size);
                node.next = freeBlock;
            } else {
                node.next.size = systemSize - res[i][1];
                node.next.startAddress = allocatedBlock.startAddress + allocatedBlock.size;
            }

//            打印
            print();
        }
    }


    /**
     * 寻找空闲分区表的大小（取巧）、正常应该写合并
     * @return
     */
    private int findFreeBlockedSize() {
        FreeBlock head = freeBlockHead;
        while (head.next != null) {
            if (head.next.next == null) {
                return head.next.size;
            }
            head = head.next;
        }

        return head.size;
    }


    /**
     * 头插法
     * 回收空闲分区、将要回收的已分区节点插入到空闲分区链表中；
     * 进行判断，如果节点的起始地址+占用资源 >= 空闲链表的起始地址，就修改；否则插入
     * @param node
     */
    public void recycleFreeBlock(AllocatedBlock node) {
        FreeBlock head = freeBlockHead;
        if (node.startAddress + node.size >= head.next.startAddress) {
            head.next.size += node.size;
        } else {
            FreeBlock temp = new FreeBlock(node.size, node.startAddress);
            temp.next = head.next;
            head.next = temp;
        }
    }


    /**
     * 寻找已分区表的上一个节点、为了分配当前节点的起始地址
     * @return
     */
    public AllocatedBlock findAllocatedStartAddress() {
        AllocatedBlock head = allocatedBlockHead;
        while (head.next != null) {
            if (head.next.next == null) {
                return head.next;
            }
            head = head.next;
        }

        return head;
    }


    /**
     * 删除已分区表
     * @param node
     */
    public void deleteAllocatedBlock(AllocatedBlock node) {
        AllocatedBlock head = allocatedBlockHead;
        while (head.next != null) {
            if (head.next.next == node) {
                head.next.next = node.next;
            }
            head = head.next;
        }
    }


    /**
     * 打印
     */
    public void print() {
        System.out.println("-------------------------------------------------------------");
        System.out.println("当前的已分区表:  ");
        AllocatedBlock head = allocatedBlockHead;
        while (head.next != null) {
            System.out.println("进程名：" + head.next.name + ", 起始地址：" + head.next.startAddress
                    + ", 占用大小：" + head.next.size);
            head = head.next;
        }
        System.out.println();
        System.out.println("当前的空闲分区表:  ");
        FreeBlock node = freeBlockHead;
        while (node.next != null) {
            System.out.println("起始地址：" + node.next.startAddress + ", 占用大小：" + node.next.size);
            node = node.next;
        }
        System.out.println();
    }


    public static void main(String[] args) {
        MemoryManagement management = new MemoryManagement();
        /**
         * 表示需要分配的进程：进程名称（char）、进程占用资源的大小
         */
        int[][] res = new int[][]{{65, 40}, {66, 104}, {67, 210}, {68, 35}, {69, 610}};
        management.allocatedSurface(res);

        System.out.println("***********************回收进程 B、C、D***********************");
        AllocatedBlock head = management.allocatedBlockHead.next.next;
        for (int i = 1; i < 4; i++) {
            management.deleteAllocatedBlock(head);
            management.recycleFreeBlock(head);
            management.print();
            head = head.next;
        }
    }
}
