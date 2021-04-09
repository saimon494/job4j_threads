package ru.job4j.pool;

import java.util.Arrays;

public class MergeSort {

    public static int[] sort(int[] array) {
        return sort(array, 0, array.length - 1);
    }

    private static int[] sort(int[] array, int from, int to) {
        // при следующем условии, массив из одного элемента
        // делить нечего, возвращаем элемент
        if (from == to) {
            return new int[] {array[from]};
        }
        // попали сюда, значит в массиве более одного элемента
        // находим середину
        int mid = (from + to) / 2;
        // объединяем отсортированные части
        return merge(
                // сортируем левую часть
                sort(array, from, mid),
                // сортируем правую часть
                sort(array, mid + 1, to)
        );
    }

    // Метод объединения двух отсортированных массивов
    public static int[] merge(int[] left, int[] right) {
        int li = 0;
        int ri = 0;
        int resI = 0;
        int[] result = new int[left.length + right.length];
        while (resI != result.length) {
            if (li == left.length) {
                result[resI++] = right[ri++];
            } else if (ri == right.length) {
                result[resI++] = left[li++];
            } else if (left[li] < right[ri]) {
                result[resI++] = left[li++];
            } else {
                result[resI++] = right[ri++];
            }
        }
        return result;
    }

    public static void main(String[] args) {
        int[] array1 = {5, 8, 10, 13, 55, 3, 4};
        int[] array2 = {2, 1, 16};
        int[] array1s = sort(array1);
        int[] array2s = sort(array2);
        System.out.println(Arrays.toString(array1s));
        System.out.println(Arrays.toString(array2s));
        System.out.println(Arrays.toString(merge(array1s, array2s)));
    }
}