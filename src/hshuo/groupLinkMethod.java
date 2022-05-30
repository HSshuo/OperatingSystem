package hshuo;

/**
 * @author SHshuo
 * @data 2022/4/30--15:15
 * 成组链接法
 * 思路：建立一个超级块，第一个节点空闲空间的个数、剩下节点存储空闲空间；
 * 参考：https://www.bilibili.com/video/BV1HE41167kj?spm_id_from=333.337.search-card.all.click
 */
public class groupLinkMethod {

    public class Block{

        public int data = 0;

        /**
         * 维护连接关系
         */
        public Block[] nextBlock;

    }


    /**
     * 创建超级块
     */
    Block[] blocks;


    public groupLinkMethod(){
//        初始化超级块
        blocks = new Block[11];
        Block block = new Block();
        blocks[0] = block;
    }


    public static void main(String[] args) {

        groupLinkMethod groupLinkMethod = new groupLinkMethod();

        System.out.println("********************正在进行空间回收操作********************");
        for(int i = 1; i <= 15; i++){
            groupLinkMethod.add(i);
            groupLinkMethod.print();
        }


        System.out.println("********************正在进行空间分配操作********************");
        for(int i = 0; i < 12; i++){
            groupLinkMethod.remove();
            groupLinkMethod.print();
        }
    }


    /**
     * 空间回收
     * @param i
     */
    public void add(int i){
        if(blocks[0].data == 10){
            System.out.println("正在将超级块中的数据存放在次级块中");
            resize();
        }

//        默认添加的是元素
        Block temp = new Block();
        temp.data = i;

        blocks[0].data++;
        blocks[blocks[0].data] = temp;
    }


    /**
     * 重构
     */
    public void resize(){
//        将两个组链接起来
        Block[] temp = new Block[11];
        Block block = new Block();
        temp[0] = block;
        temp[1] = block;

        temp[0].data = 1;
        temp[1].nextBlock = blocks;

//        重新赋值给超级块
        blocks = temp;
    }


    /**
     * 分配空间
     */
    public void remove(){
        if(blocks[0].data == 0){
            System.out.println("没有空闲空间了");
            return;
        }

        Block[] element = blocks[blocks[0].data].nextBlock;

        if(element == null){
            blocks[0].data--;
        }else{
            System.out.println("正在将次级块中的数据移入到超级块中");
            for(Block b : element){
                add(b.data);
            }
        }
    }


    /**
     * 打印
     */
    public void print(){
        System.out.println("此时尚有的空间数：" + (10 - blocks[0].data));
    }

}
