package com.example.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author wangshuo
 * @Date 2021-07-27 16:28
 * @Version V1.0
 */

public enum SettlementType {

    /**
     * 没有结算
     */
    NONE(0),
    /**
     * 钢厂结算
     */
    STEEL(1),
    /**
     * 供货人结算
     */
    SUPPLIER(1 << 1);
//    /**
//     * 司机结算
//     */
//    DRIVER(1 << 2);


    private int code;

    public static SettlementType judgeValue(String name) {

        SettlementType[] values = SettlementType.values();
        for (SettlementType value : values) {
            if (value.name().equals(name)) {
                return value;
            }
        }
        return null;
    }

    SettlementType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    /**
     * 判断是否当前状态
     *
     * @param type
     * @return
     */
    public boolean is(SettlementType type) {
        return (type.code & this.code) == this.code;
    }

    /**
     * 判断是否当前状态
     *
     * @param code
     * @return
     */
    public boolean is(int code) {
        return (code & this.code) == this.code;
    }

    /**
     * 从状态中增加当前状态
     *
     * @param type
     * @return
     */
    public int add(SettlementType type) {
        return type.code | this.code;
    }

    /**
     * 从状态中增加当前状态
     *
     * @param code
     * @return
     */
    public int add(int code) {
        return code | this.code;
    }

    /**
     * 从状态中增加当前状态
     *
     * @param code
     * @return
     */
    public List<Integer> add(List<Integer> code) {
        if (code == null) {
            return null;
        }
        return code.stream().map(ir -> ir | this.code).distinct().collect(Collectors.toList());
    }

    /**
     * 从状态中移除当前状态
     *
     * @param type
     * @return
     */
    public int remove(SettlementType type) {
        return type.code ^ this.code;
    }

    /**
     * 从状态中移除当前状态
     *
     * @param code
     * @return
     */
    public int remove(int code) {
        return code ^ this.code;
    }

    /**
     * 从状态中移除当前状态
     *
     * @param code
     * @return
     */
    public List<Integer> remove(List<Integer> code) {
        if (code == null) {
            return null;
        }
        return code.stream().map(ir -> ir ^ this.code).distinct().collect(Collectors.toList());
    }

    /**
     * 返回包含当前类型的所有组合
     *
     * @return
     */
    public List<Integer> in() {
        final List<Integer> list = new ArrayList<>();
        if (this.code == NONE.code) {
            list.add(0);
            return list;
        }
        Stack<Integer> stack = new Stack<>();
        final Integer[] types = Arrays.stream(SettlementType.values()).filter(type -> this.code != type.code).map(type -> type.code).toArray(Integer[]::new);
        for (int i = 1; i <= types.length; i++) {
            f(list, this.code, stack, types, i, 0, 0);
            list.addAll(stack);
            stack.removeAllElements();
        }
        return list.stream().sorted().distinct().collect(Collectors.toList());
    }

    /**
     * 返回包含当前类型的所有组合
     *
     * @return
     */
    public List<Integer> notIn() {
        return remove(in());
    }

    /**
     * @param list
     * @param code
     * @param shu  元素
     * @param targ 要选多少个元素
     * @param has  当前有多少个元素
     * @param cur  当前选到的下标
     */
    private void f(List<Integer> list, int code, Stack<Integer> stack, Integer[] shu, int targ, int has, int cur) {
        if (has == targ) {
            final Stack<Integer> clone = (Stack<Integer>) stack.clone();
            int codeT = code;
            while (!clone.empty()) {
                codeT = clone.pop() | codeT;
            }
            list.add(codeT);
            return;
        }
        for (int i = cur; i < shu.length; i++) {
            if (!stack.contains(shu[i])) {
                stack.add(shu[i]);
                f(list, code, stack, shu, targ, has + 1, i);
                stack.pop();
            }
        }

    }

    public static void main(String[] args) {
        System.out.println(SettlementType.SUPPLIER.notIn());
    }
}
