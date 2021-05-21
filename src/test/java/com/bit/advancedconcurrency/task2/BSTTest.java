package com.bit.advancedconcurrency.task2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class BSTTest {
    private static BST bst;
    private static Set<Integer> initSet;

    @BeforeEach
    public void beforeEach() {
        Node root = new Node(37,
                new Node(
                        15,
                        new Node(4, null, null, false),
                        new Node(21, null, null, false),
                        true
                ),
                new Node(
                        75,
                        new Node(
                                58,
                                new Node(49, null, null, false),
                                new Node(65, null, null, false),
                                true
                        ),
                        new Node(93, null, null, false),
                        true
                ),
                true
        );
        bst = new BST(root);
        initSet = bst.traverse();
    }

    @Test
    public void testSimpleInsert(){
        bst.printTree();
        System.out.println("------------");

        // есть такой routing
        Assertions.assertTrue(bst.insert(15));
        initSet.add(15);
        Assertions.assertTrue(bst.insert(75));
        initSet.add(75);

        // есть такой лист
        Assertions.assertFalse(bst.insert(65));

        // нет таких элементов
        Assertions.assertTrue(bst.insert(52));
        initSet.add(52);
        Assertions.assertTrue(bst.insert(30));
        initSet.add(30);

        Set<Integer> result = bst.traverse();
        Assertions.assertEquals(initSet, result);
        bst.printTree();
    }

    @Test
    public void testSimpleRemove(){
        bst.printTree();
        System.out.println("------------");

        // есть такой routing
        Assertions.assertFalse(bst.remove(15));

        // есть такой лист
        Assertions.assertTrue(bst.remove(65));
        initSet.remove(65);
        Assertions.assertTrue(bst.remove(4));
        initSet.remove(4);

        // нет таких элементов
        Assertions.assertFalse(bst.remove(52));

        Set<Integer> result = bst.traverse();
        Assertions.assertEquals(initSet, result);
        bst.printTree();
    }

    @Test
    public void adfght() throws InterruptedException {
        BSTTestImpl bstTest = new BSTTestImpl(new Node(
                50_000,
                new Node(25_000, false),
                new Node(75_000, false),
                true));
        List<Operation> operations= new ArrayList<>(1000);
        Thread thread = new Thread(new BSTTestRunnable(bstTest, 100_000, 50, 50, operations));
        thread.start();
        Thread.sleep(10_000);
        thread.interrupt();
        System.out.println(operations);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 10, 50})
    public void count(int x) throws InterruptedException {

        BST bst = new BST(new Node(
                50_000,
                new Node(25_000, false),
                new Node(75_000, false),
                true));

        List<Thread> threads = new ArrayList<>();
        threads.add(new Thread(new BSTRunnable(bst, 100_000, 50, x)));
        threads.add(new Thread(new BSTRunnable(bst, 100_000, 50, x)));
        threads.add(new Thread(new BSTRunnable(bst, 100_000, 50, x)));
        threads.add(new Thread(new BSTRunnable(bst, 100_000, 50, x)));
        threads.forEach(Thread::start);
        Thread.sleep(10_000);
        threads.stream()
                .peek(Thread::interrupt)
                .forEach(thread -> {
                    try {
                        thread.join();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                });
    }

//    @Test
//    public void checkValidity() throws InterruptedException {
//
//        BSTTestImpl bst = new BST(new Node(
//                50_000,
//                new Node(25_000, false),
//                new Node(75_000, false),
//                true));
//
//        List<Thread> threads = new ArrayList<>();
//        List<Operation> operations= new ArrayList<>(1000);
//        threads.add(new Thread(new BSTTestRunnable(bstTest, 100_000, 50, 50, operations)));
//        threads.add(new Thread(new BSTRunnable(bst, 100_000, 50, 10)));
//        threads.add(new Thread(new BSTRunnable(bst, 100_000, 50, 10)));
//        threads.add(new Thread(new BSTRunnable(bst, 100_000, 50, 10)));
//        threads.forEach(Thread::start);
//        Thread.sleep(10_000);
//        threads.stream()
//                .peek(Thread::interrupt)
//                .forEach(thread -> {
//                    try {
//                        thread.join();
//                    } catch (InterruptedException ex) {
//                        ex.printStackTrace();
//                    }
//                });
//    }
}