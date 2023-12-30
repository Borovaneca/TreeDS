package implementations;

import interfaces.AbstractTree;

import java.util.*;
import java.util.stream.Collectors;

public class Tree<E> implements AbstractTree<E> {

    private E value;
    private Tree<E> parent;
    private List<Tree<E>> children;

    @SafeVarargs
    public Tree(E value, Tree<E>... children) {
        this.value = value;
        this.parent = null;
        this.children = new ArrayList<>();
        for (Tree<E> child : children) {
            this.children.add(child);
            child.parent = this;
        }
    }

    @Override
    public List<E> orderBfs() {
        ArrayDeque<Tree<E>> queue = new ArrayDeque<>();
        queue.offer(this);
        List<E> output = new ArrayList<>();
        while (!queue.isEmpty()) {
            Tree<E> current = queue.poll();
            output.add(current.value);
            for (Tree<E> child : current.children) {
                queue.offer(child);
            }
        }
        return output;
    }

    @Override
    public List<E> orderDfs() {
        return doDfs(this, new ArrayList<>());
    }

    private List<E> doDfs(Tree<E> eTree, List<E> result) {
        for (Tree<E> child : eTree.children) {
            doDfs(child, result);
        }
        result.add(eTree.value);
        return result;
    }

    @Override
    public void addChild(E parentKey, Tree<E> child) {
        Tree<E> node = getNode(this, parentKey);
        node.children.add(child);
    }

    private Tree<E> getNode(Tree<E> eTree, E parentKey) {
        for (Tree<E> child : eTree.children) {
            Tree<E> node = getNode(child, parentKey);
            if (node.value.equals(parentKey)) {
                return node;
            }
        }
        return eTree;
    }


    @Override
    public void removeNode(E nodeKey) {
        Tree<E> node = getNodeWhereContainNode(this, nodeKey);
        Tree<E> child = node.children.stream().filter(n -> n.value.equals(nodeKey)).findFirst().orElse(null);
        node.children.remove(child);
    }

    private Tree<E> getNodeWhereContainNode(Tree<E> eTree, E nodeKey) {
        for (Tree<E> child : eTree.children) {
            List<Tree<E>> collect = child.children.stream().filter(n -> n.value.equals(nodeKey)).collect(Collectors.toList());
            if (collect.isEmpty()) {
                getNodeWhereContainNode(child, nodeKey);
            } else {
                return collect.get(0);
            }
        }
        return eTree;
    }

    @Override
    public void swap(E firstKey, E secondKey) {
        Tree<E> first = getNode(this, firstKey);
        Tree<E> second = getNode(this, secondKey);

        Tree<E> parent1 = first.parent;
        Tree<E> parent2 = second.parent;

        int firstIndex = parent1.children.indexOf(first);
        int secondIndex = parent2.children.indexOf(second);

        parent2.children.set(secondIndex, first);
        parent1.children.set(firstIndex, second);

        first.parent = parent2;
        second.parent = parent1;
    }
}



