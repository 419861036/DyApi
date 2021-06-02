//package kkd.common.index;
//
//import java.util.*;
//import com.xuedi.IO.*;
//import com.xuedi.maths.*;
//////// DisposeRoot ///////中的key参数有些问题
//public class BTree {
//  //用于记录每个节点中的键值数量
//  public int keyAmount;
//  //树的根节点
//  public Node root;
//  public BTree(int keyAmount) {
//    this.keyAmount = keyAmount;
//    this.root = new Node(keyAmount);
//  }
//  //在B树中插入叶节点/////////////////////////////////////////////////////////////
//  public void insert(long key,Object pointer)
//  {
//    //找到应该插入的节点
//    Node theNode = search(key,root);
//    //在叶节点中找到空闲空间，有的话就把键放在那里
//    if( !isFull(theNode) )
//    {
//      putKeyToNode(key,pointer,theNode);
//    }else{
//      //如果在适当的叶节点没有空间，就把该叶节点分裂成两个，并正确分配键值
//      Node newNode = separateLeaf(key,pointer,theNode);
//      //如果分裂的是根节点，就新建一个新的根节点将新建的节点作为他的字节点
//      if( isRoot(theNode) )
//      {
//        DisposeRoot(theNode,newNode,newNode.keys[0]);
//      }else{
//        //将新建立的节点的指针插入到上层节点
//        insertToInnerNode(theNode.parent,newNode,newNode.keys[0]);
//      }
//    }
//  }
//  //lowerNode是下级节点分离后新建立的那个节点///////////////////////////////////////
//  //upperNode是lowerNode的上层节点
//  private void insertToInnerNode(Node upperNode,Node lowerNode,long key)
//  {
//    //上层节点有空位就直接插入
//    if( !isFull(upperNode) )
//    {
//      putKeyToNode(key,lowerNode,upperNode);
//      //重置父节点指针
//      pointerRedirect(upperNode);
//      return;
//    }else{
//      //如果分裂的是根节点，就新建一个新的根节点将新建的节点作为他的子节点
//      Node newNode;
//      if( isRoot(upperNode) )
//      {
//        newNode = separateInnerNode(key,lowerNode,upperNode);
//        Node newRoot = new Node(this.keyAmount);
//        newRoot.pointer[0] = upperNode;
//        newRoot.pointer[1] = newNode;
//        upperNode.parent = newRoot;
//        newNode.parent   = newRoot;
//        newRoot.keyAmount = 1;
//        newRoot.keys[0] = key;
//        root = newRoot;
//        //重置父节点指针
//        pointerRedirect(upperNode);
//        return;
//      }else{
//        //上层非根节点没有空位进行分裂和插入操作
//        newNode = separateInnerNode(key,lowerNode,upperNode);
//        //重置父节点指针
//        pointerRedirect(upperNode);
//        //记录要向上插入的键值在源节点中的位置(该键值在separateInnerNode()被保留在srcNode中)
//        int keyToUpperNodePosition = upperNode.keyAmount;
//        //向上递归插入
//        insertToInnerNode(upperNode.parent,newNode,upperNode.keys[keyToUpperNodePosition]);
//        //重置父节点指针
//        pointerRedirect(newNode);
//      }
//    }
//  }
//  //将对应的内部节点进行分裂并正确分配键值，返回新建的节点
//  private Node separateInnerNode(long key,Object pointer,Node srcNode)
//  {
//    Node newNode = new Node(this.keyAmount);
//   //因为我在Node中预制了一个位置用于插入，而下面的函数(putKeyToLeaf())不进行越界检查
//   //所以可以将键-指针对先插入到元节点，然后再分别放到两个节点中
//   putKeyToNode(key,pointer,srcNode);
//   //先前节点后来因该有(n+1)/2取上界个键－值针对
//   int ptrSaveAmount = (int)com.xuedi.maths.NumericalBound.getBound(0,(double)(this.keyAmount+1)/2);
//   int keySaveAmount = (int)com.xuedi.maths.NumericalBound.getBound(0,(double)(this.keyAmount)/2);
//   int keyMoveAmount = (int)com.xuedi.maths.NumericalBound.getBound(1,(double)(this.keyAmount)/2);
//   //(n+1)/2取上界个指针和n/2取上界个键留在源节点中
//   //剩下的n+1)/2取下界个指n/2取下界个键留在源节点中
//   for (int k = ptrSaveAmount; k < srcNode.keyAmount; k++) {
//       newNode.add(srcNode.keys[k], srcNode.pointer[k]);
//   }
//   newNode.pointer[newNode.keyAmount] = srcNode.pointer[srcNode.pointer.length-1];
//   srcNode.keyAmount = keySaveAmount;
//   return newNode;
//  }
//  //将对应的叶节点进行分裂并正确分配键值，返回新建的节点///////////////////////////////
//  private Node separateLeaf(long key,Object pointer,Node srcNode)
//  {
//    Node newNode = new Node(this.keyAmount);
//    //兄弟间的指针传递
//    newNode.pointer[this.keyAmount] = srcNode.pointer[this.keyAmount];
//    //因为我在Node中预制了一个位置用于插入，而下面的函数(putKeyToLeaf())不进行越界检查
//    //所以可以将键-指针对先插入到元节点，然后再分别放到两个节点中
//    putKeyToNode(key,pointer,srcNode);
//    //先前节点后来因该有(n+1)/2取上界个键－值针对
//    int oldNodeSize = (int)com.xuedi.maths.NumericalBound.getBound(0,(double)(this.keyAmount+1)/2);
//    for(int k = oldNodeSize; k <= this.keyAmount; k++)
//    {
//      newNode.add(srcNode.keys[k],srcNode.pointer[k]);
//    }
//    srcNode.keyAmount = oldNodeSize;
//    //更改指针--让新节点成为就节点的右边的兄弟
//    srcNode.pointer[this.keyAmount] = newNode;
//    return newNode;
//  }
//  //把键值放到叶节点中--这个函数不进行越界检查////////////////////////////////////////
//  private void putKeyToNode(long key,Object pointer,Node theNode)
//  {
//    int position = getInsertPosition(key,theNode);
//    //进行搬迁动作--------叶节点的搬迁
//    if( isLeaf(theNode) )
//    {
//        if(theNode.keyAmount <= position)
//        {
//           theNode.add(key,pointer);
//           return;
//        }
//        else{
//            for (int j = theNode.keyAmount - 1; j >= position; j--) {
//              theNode.keys[j + 1] = theNode.keys[j];
//              theNode.pointer[j + 1] = theNode.pointer[j];
//            }
//            theNode.keys[position] = key;
//            theNode.pointer[position] = pointer;
//        }
//     }else{
//          //内部节点的搬迁----有一定的插入策略：
//          //指针的插入比数据的插入多出一位
//          for (int j = theNode.keyAmount - 1; j >= position; j--) {
//            theNode.keys[j + 1] = theNode.keys[j];
//            theNode.pointer[j + 2] = theNode.pointer[j+1];
//          }
//          theNode.keys[position] = key;
//          theNode.pointer[position+1] = pointer;
//        }
//    //键值数量加1
//    theNode.keyAmount++;
//  }
//  //获得正确的插入位置
//  private int getInsertPosition(long key,Node node)
//  {
//    //将数据插入到相应的位置
//    int position = 0;
//    for (int i = 0; i < node.keyAmount; i++) {
//        if (node.keys[i] > key)
//          break;
//        position++;
//    }
//    return position;
//  }
//  //有用的辅助函数////////////////////////////////////////////////////////////////
//  //判断某个结点是否已经装满了
// private boolean isFull(Node node)
// {
//   if(node.keyAmount >= this.keyAmount)
//     return true;
//   else
//     return false;
// }
// //判断某个节点是否是叶子结点
// private  boolean isLeaf(Node node)
// {
//   //int i = 0;
//   if(node.keyAmount == 0)
//     return true;
//   //如果向下的指针是Node型，则肯定不是叶子节点
//   if(node.pointer[0] instanceof Node)
//     return false;
//   return true;
// }
// private boolean isRoot(Node node)
// {
//   if( node.equals(this.root) )
//     return true;
//   return false;
// }
// //给内部节点中的自己点重新定向自己的父亲
//  private void pointerRedirect(Node node)
//  {
//    for(int i = 0; i <= node.keyAmount; i++)
//    {
//      ((Node)node.pointer[i]).parent = node;
//    }
//  }
//  //新建一个新的根节点将新建的节点作为他的字节点
//  private void DisposeRoot(Node child1,Node child2,long key)
//  {
//        Node newRoot = new Node(this.keyAmount);
//        newRoot.pointer[0] = child1;
//        newRoot.pointer[1] = child2;
//        newRoot.keyAmount = 1;
//        newRoot.keys[0] = key;
//        root = newRoot;
//        //如果两个孩子是叶节点就让他们两个相连接
//        if( isLeaf(child1) )
//        {
//          //兄弟间的指针传递
//          child2.pointer[this.keyAmount] = child1.pointer[this.keyAmount];
//          child1.pointer[this.keyAmount] = child2;
//        }
//        pointerRedirect(root);
//        return;
//  }
// ///////////////////////////////////////////////////////////////////////////////
//  //用于寻找键值key所在的或key应该插入的节点
//  //key为键值,curNode为当前节点--一般从root节点开始
//  public Node search(long key,Node curNode)
//  {
//    if (isLeaf(curNode))
//      return curNode;
//    for (int i = 0; i < this.keyAmount; i++)
//    {
//        if (key < curNode.keys[i]) //判断是否是第一个值
//          return search(key, (Node) curNode.pointer[i]);
//        else if (key >= curNode.keys[i]) {
//          if (i == curNode.keyAmount - 1) //如果后面没有值
//          {
//            //如果key比最后一个键值大,则给出最后一个指针进行递归查询
//            return search(key,(Node) curNode.pointer[curNode.keyAmount]);
//          }
//          else {
//            if (key < curNode.keys[i + 1])
//              return search(key, (Node) curNode.pointer[i + 1]);
//          }
//        }
//    }
//    //永远也不会到达这里
//    return null;
//  }
//}